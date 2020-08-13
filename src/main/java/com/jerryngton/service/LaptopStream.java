package com.jerryngton.service;

import com.jerryngton.protobuf.pb.Laptop;

public interface LaptopStream {
    void send(Laptop laptop);
}
