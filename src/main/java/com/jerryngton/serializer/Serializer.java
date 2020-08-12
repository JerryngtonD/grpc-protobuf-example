package com.jerryngton.serializer;

import com.google.protobuf.util.JsonFormat;
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

    public void writeJSONFile(Laptop laptop, String filename) throws IOException {
        JsonFormat.Printer printer = JsonFormat.printer()
                .includingDefaultValueFields()
                .preservingProtoFieldNames();

        String jsonString = printer.print(laptop);

        FileOutputStream outputStream = new FileOutputStream(filename);
        outputStream.write(jsonString.getBytes());
        outputStream.close();
    }

    public static void main(String[] args) throws IOException {
        Serializer serializer = new Serializer();
        Laptop laptop = serializer.readBinaryFile("laptop.bin");

        serializer.writeJSONFile(laptop, "laptop.json");
    }


}
