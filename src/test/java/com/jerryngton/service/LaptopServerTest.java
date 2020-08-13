package com.jerryngton.service;

import com.jerryngton.pcbook.sample.Generator;
import com.jerryngton.protobuf.pb.CreateLaptopRequest;
import com.jerryngton.protobuf.pb.LaptopServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.*;

import java.util.UUID;

import static org.junit.Assert.*;

public class LaptopServerTest {
    @Rule
    public final GrpcCleanupRule rule = new GrpcCleanupRule(); //automatic graceful shutdown channel in the end of a test


    private LaptopStore store;
    private LaptopServer server;
    private ManagedChannel channel;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        InProcessServerBuilder serverBuilder = InProcessServerBuilder.forName(serverName).directExecutor();

        store = new InMemoryLaptopStore();
        server = new LaptopServer(serverBuilder, 0 , store);
        server.start();

        channel = rule.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void createLaptopWithValidId() {
        var generator = new Generator();
        var laptop = generator.NewLaptop();
        var request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        var blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
        var response = blockingStub.createLaptop(request);

        assertNotNull(response);
        assertEquals(laptop.getId(), response.getId());

        var found = store.find(response.getId());
        assertNotNull(found);
    }

    @Test
    public void createLaptopWithEmptyId() {
        var generator = new Generator();
        var laptop = generator.NewLaptop().toBuilder().setId("").build();
        var request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        var blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
        var response = blockingStub.createLaptop(request);

        assertNotNull(response);
        assertFalse(response.getId().isEmpty());

        var found = store.find(response.getId());
        assertNotNull(found);
    }

    @Test(expected = StatusRuntimeException.class)
    public void createLaptopWithInvalidId() {
        var generator = new Generator();
        var laptop = generator.NewLaptop().toBuilder().setId("not-uuid-type").build();
        var request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        var blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
        var response = blockingStub.createLaptop(request);
    }

    @Test(expected = StatusRuntimeException.class)
    public void createLaptopWithAlreadyExistsId() throws Exception {
        var generator = new Generator();
        var id = UUID.randomUUID().toString();
        var laptop = generator.NewLaptop().toBuilder().setId(id).build();
        store.save(laptop);
        var request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();
        var blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
        blockingStub.createLaptop(request);
    }
}