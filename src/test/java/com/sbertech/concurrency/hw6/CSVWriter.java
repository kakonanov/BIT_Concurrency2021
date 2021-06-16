package com.sbertech.concurrency.hw6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;

public class CSVWriter {
    private final Path path;

    public CSVWriter(Path path) {
        this.path = path;
        printColumns();
    }

    private void printColumns() {
        try {
            Files.write(path, Collections.singleton("load,size,queueSize,pocketNum,value"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(double load, int size, int queueSize, int pocketNum, long[][] array) {
        String param = load + "," + size + "," + queueSize + "," + pocketNum;
        Arrays.stream(array)
                .flatMapToLong(Arrays::stream)
                .forEach(l -> {
                    try {
                        Files.write(path, Collections.singleton(param + "," + l), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }

}
