package com.jerryngton.serializer;

import com.jerryngton.protobuf.pb.Laptop;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Serializer {
    public void writeBinaryFile(Laptop laptop, String filename) throws IOException {
        FileOutputStream outStream = new FileOutputStream(filename);
        laptop.writeTo(outStream);
        outStream.close();
    }

    public Laptop readBinaryFile(String fileName) throws IOException {
        FileInputStream inStream = new FileInputStream(fileName);
        Laptop laptop = Laptop.parseFrom(inStream);
        inStream.close();
        return laptop;
    }


}
