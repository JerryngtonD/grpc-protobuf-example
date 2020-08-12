package com.jerryngton.service;

import com.jerryngton.protobuf.pb.Laptop;

public interface LaptopStore {
    void save(Laptop laptop) throws Exception;
    Laptop find(String id);
}
