package com.jerryngton.serializer;

import com.jerryngton.pcbook.sample.Generator;
import com.jerryngton.protobuf.pb.Laptop;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class SerializerTest {

    @Test
    public void writeAndReadBinaryFile() throws IOException {
        String binaryFile = "laptop.bin";
        Laptop laptopToWrite = new Generator().NewLaptop();

        Serializer serializer = new Serializer();
        serializer.writeBinaryFile(laptopToWrite, binaryFile);

        Laptop laptopByReading = serializer.readBinaryFile(binaryFile);
        Assert.assertEquals(laptopToWrite, laptopByReading);
    }

}