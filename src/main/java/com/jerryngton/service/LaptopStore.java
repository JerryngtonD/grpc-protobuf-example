package com.jerryngton.service;

import com.jerryngton.protobuf.pb.Filter;
import com.jerryngton.protobuf.pb.Laptop;
import io.grpc.Context;

public interface LaptopStore {
    void save(Laptop laptop) throws Exception;
    Laptop find(String id);
    void search(Context ctx, Filter filter, LaptopStream stream);
}

