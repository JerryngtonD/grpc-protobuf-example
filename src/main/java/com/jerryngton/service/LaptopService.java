package com.jerryngton.service;

import com.jerryngton.protobuf.pb.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LaptopService extends LaptopServiceGrpc.LaptopServiceImplBase {
    public static final Logger logger = Logger.getLogger(LaptopService.class.getName());

    private LaptopStore store;

    public LaptopService(LaptopStore store) {
        this.store = store;
    }

    @Override
    public void createLaptop(CreateLaptopRequest request, StreamObserver<CreateLaptopResponse> responseObserver) {
        Laptop laptop = request.getLaptop();
        String id = laptop.getId();

        logger.info("Got a create-laptop  request with id: " + id);

        UUID uuid;
        if (id.isEmpty()) {
            uuid = UUID.randomUUID();
        } else {
            try {
                uuid = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                        .withDescription(e.getMessage())
                        .asRuntimeException());
                return;
            }
        }

        //heavy processing
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //check cancelation from client cause deadline
//        if (Context.current().isCancelled()) {
//            logger.info("request is cancelled");
//            responseObserver.onError(
//                    Status.CANCELLED
//                            .withDescription("request is cancelled")
//                            .asRuntimeException()
//            );
//            return;
//        }

        Laptop other = laptop.toBuilder().setId(uuid.toString()).build();

        try {
            store.save(other);
        } catch (AlreadyExistsException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
            return;
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
            return;
        }

        CreateLaptopResponse response = CreateLaptopResponse.newBuilder()
                .setId(other.getId())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        logger.info("The laptop with this id was succesfully saved to the store: " + other.getId());
    }

    @Override
    public void searchLaptop(SearchLaptopRequest request, StreamObserver<SearchLaptopResponse> responseObserver) {
        Filter filter = request.getFilter();
        logger.info("got a search-laptop request with filter: " + filter);

        store.search(filter, laptop -> {
            logger.info("found laptop with ID: " + laptop.getId());
            var response = SearchLaptopResponse.newBuilder().setLaptop(laptop).build();
            responseObserver.onNext(response);
        });

        responseObserver.onCompleted();
        logger.info("search laptops completed");
    }
}
