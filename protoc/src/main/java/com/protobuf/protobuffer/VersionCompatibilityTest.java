package com.protobuf.protobuffer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.protobuf.models.Television;

public class VersionCompatibilityTest {

    public static void main(String[] args) throws IOException {

        Path pathV1 =  Paths.get("tv-v1");
       // Path pathV2 =  Paths.get("tv-v2");

        Television television = Television.newBuilder()
                .setBrand("sony")
                .setYear(2021)
                .build();

        Files.write(pathV1, television.toByteArray());

        //
        byte[] bytes = Files.readAllBytes(pathV1);
        System.out.println(
                Television.parseFrom(bytes)
        );


    }


}