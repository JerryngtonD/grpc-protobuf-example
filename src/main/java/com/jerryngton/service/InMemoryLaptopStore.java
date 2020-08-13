package com.jerryngton.service;

import com.jerryngton.protobuf.pb.Filter;
import com.jerryngton.protobuf.pb.Laptop;
import com.jerryngton.protobuf.pb.Memory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryLaptopStore implements LaptopStore {
    private ConcurrentMap<String, Laptop> data;

    public InMemoryLaptopStore() {
        data = new ConcurrentHashMap<>(0);
    }


    @Override
    public void save(Laptop laptop) throws Exception {
        if (data.containsKey(laptop.getId())) {
            throw new AlreadyExistsException("Laptop already exist with this id: " + laptop.getId());
        }

        //deep copy
        Laptop other = laptop.toBuilder().build();
        data.put(other.getId(), other);
    }

    @Override
    public Laptop find(String id) {
        if (!data.containsKey(id)) {
            return null;
        }

        //deep copy
        Laptop other = data.get(id).toBuilder().build();
        return other;
    }

    @Override
    public void search(Filter filter, LaptopStream stream) {
        for (var entry : data.entrySet()) {
            var laptop = entry.getValue();
            if (isQualified(filter, laptop)) {
                stream.send(laptop.toBuilder().build());
            }
        }
    }

    private boolean isQualified(Filter filter, Laptop laptop) {
        if (laptop.getPriceUsd() > filter.getMaxPriceUsd()) {
            return false;
        }

        if (laptop.getCpu().getNumberCores() < filter.getMinCpuCores()) {
            return false;
        }

        if (laptop.getCpu().getMaxGhz() < filter.getMinCpuGhz()) {
            return false;
        }

        if (toBit(laptop.getRam()) < toBit(filter.getMinRam())) {
            return false;
        }

        return true;
    }

    private long toBit(Memory ram) {
        long value = ram.getValue();

        switch(ram.getUnit()) {
            case BIT:
                return value;
            case BYTE:
                return value << 3;
            case KILOBYTE:
                return value << 13;
            case MEGABYTE:
                return value << 23;
            case GIGABYTE:
                return value << 33;
            case TERABYTE:
                return value << 43;
            default:
                return 0;
        }
    }
}
