package com.jerryngton.pcbook.sample;

import com.google.protobuf.Timestamp;
import com.jerryngton.protobuf.pb.*;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class Generator {
    private final Random rand;

    {
        rand = new Random();
    }

    public Keyboard NewKeyboard() {
        return Keyboard.newBuilder()
                .setLayout(randomKeyboardLayout())
                .setBacklit(rand.nextBoolean())
                .build();
    }

    public CPU NewCPU() {
        String brand = randomCPUBrand();
        String name = randomCPUName(brand);

        int cores = randomInt(2, 8);
        int threads = randomInt(cores, 12);

        double minGhz = randomDouble(2.0, 3.5);
        double maxGhz = randomDouble(minGhz, 5.0);

        return CPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setNumberCores(cores)
                .setNumberThreads(threads)
                .setMinGhz(minGhz)
                .setMaxGhz(maxGhz)
                .build();
    }

    public GPU NewGPU() {
        String brand = randomGPUBrand();
        String name = randomGPUName(brand);

        double minGhz = randomDouble(2.0, 3.5);
        double maxGhz = randomDouble(minGhz, 5.0);

        Memory memory = Memory.newBuilder()
                .setValue(randomInt(2, 6))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return GPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setMinGhz(minGhz)
                .setMaxGhz(maxGhz)
                .setMemory(memory)
                .build();
    }

    private String randomGPUName(String brand) {
        if (brand.equals("NVIDIA")) {
            return randomStringFromSet(
                    "RTX 260",
                    "RTX 2200",
                    "GTX 4589",
                    "GTX 091"
            );
        }

        return randomStringFromSet(
                "AMD 260",
                "CLEAR GONE 2200",
                "Something steel 4589",
                "AXC 091"
        );
    }

    public Memory NewRAM() {
        return Memory.newBuilder()
                .setValue(randomInt(4, 16))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();
    }

    public Storage NewSSD() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(128, 1024))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return Storage.newBuilder()
                .setDriver(Storage.Driver.SSD)
                .setMemory(memory)
                .build();
    }

    public Storage NewHDD() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(128, 1024))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return Storage.newBuilder()
                .setDriver(Storage.Driver.HDD)
                .setMemory(memory)
                .build();
    }

    public Screen NewScreen() {
        int height = randomInt(1080, 4320);
        int width = height * 16 / 9;

        Screen.Resolution resolution = Screen.Resolution.newBuilder()
                .setHeight(height)
                .setWidth(width)
                .build();

        return Screen.newBuilder()
                .setSizeInch(randomFloat(13, 17))
                .setResolution(resolution)
                .setPanel(randomScreenPanel())
                .setMultitouch(rand.nextBoolean())
                .build();
    }

    public Laptop NewLaptop() {
        String brand = randomLaptopBrand();
        String name = randomLaptopName(brand);

        double weightKg = randomDouble(1.0, 3.0);
        double priceUSD = randomDouble(2000, 3500);

        int releaseYear = randomInt(2015, 2019);

        return Laptop.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setBrand(brand)
                .setName(name)
                .setCpu(NewCPU())
                .addGpus(NewGPU())
                .addStorages(NewSSD())
                .addStorages(NewHDD())
                .setScreen(NewScreen())
                .setKeyboard(NewKeyboard())
                .setWeightKg(weightKg)
                .setPriceUsd(priceUSD)
                .setReleaseYear(releaseYear)
                .setUpdatedAt(timestampNow())
                .build();

    }

    private Timestamp timestampNow() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }

    private String randomLaptopName(String brand) {
        switch(brand) {
            case "Apple":
                return randomStringFromSet("Macbook Air", "Macbook Pro");

            case "Dell":
                return randomStringFromSet("Latitude", "Vostro", "AllienWare");

            default:
                return randomStringFromSet("Thinkpad X1", "Thinkpad P1", "Thinkpad PS3");
        }
    }

    private String randomLaptopBrand() {
        return randomStringFromSet("Apple", "Dell", "Lenovo");
    }

    private Screen.Panel randomScreenPanel() {
        if (rand.nextBoolean()) {
            return Screen.Panel.IPS;
        }
        return Screen.Panel.OLED;
    }

    private float randomFloat(float min, float max) {
        return min + rand.nextFloat() * (max - min);
    }

    private String randomGPUBrand() {
        return randomStringFromSet("NVIDIA, AMD");
    }


    private int randomInt(int min, int max) {
        return min + rand.nextInt(max - min + 1);
    }

    private double randomDouble(double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }

    private String randomCPUName(String brand) {
        if (brand.equals("Intel")) {
            return randomStringFromSet(
                    "Xeao E-220M",
                    "Intel-234",
                    "AMD-A9",
                    "AMD-A8",
                    "AMD-A7",
                    "AMD-A6",
                    "AMD-A5"
            );
        }
        return randomStringFromSet(
                "Intel Core i7",
                "AMD Ryzen",
                "Intel Velocity",
                "Intel Studio",
                "AMD Studio"
        );
    }

    private String randomCPUBrand() {
        return randomStringFromSet("Intel", "Amd");
    }

    private String randomStringFromSet(String... brands) {
        int size = brands.length;
        if (size == 0) return "";

        return brands[rand.nextInt(size)];
    }

    private Keyboard.Layout randomKeyboardLayout() {
        switch (rand.nextInt(3)) {
            case 1:
                return Keyboard.Layout.QWERTY;
            case 2:
                return Keyboard.Layout.QWERTZ;
            default:
                return Keyboard.Layout.AZERTY;
        }
    }

    public static void main(String[] args) {
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptop();
        System.out.println(laptop);
    }
}
