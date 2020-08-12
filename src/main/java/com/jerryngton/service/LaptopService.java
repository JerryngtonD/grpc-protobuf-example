package com.jerryngton.service;

import com.jerryngton.protobuf.pb.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.UUID;
import java.util.logging.Logger;

public class LaptopService extends LaptopServiceGrpc.LaptopServiceImplBase {
    public static final Logger logger = Logger.getLogger(LaptopService.class.getName());

    @Override
    public void createLaptop(CreateLaptopRequest request, StreamObserver<CreateLaptopResponse> responseObserver) {
        super.createLaptop(request, responseObserver);
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
            }
            return;
        }

        Laptop other = laptop.toBuilder().setId(uuid.toString()).build();
        //TODO: Save other laptop to in-memory store


    }

    @Override
    public void searchLaptop(SearchLaptopRequest request, StreamObserver<SearchLaptopResponse> responseObserver) {
        super.searchLaptop(request, responseObserver);
    }

    @Override
    public StreamObserver<UploadImageRequest> uploadImage(StreamObserver<UploadImageResponse> responseObserver) {
        return super.uploadImage(responseObserver);
    }

    @Override
    public StreamObserver<RateLaptopRequest> rateLaptop(StreamObserver<RateLaptopResponse> responseObserver) {
        return super.rateLaptop(responseObserver);
    }
}
