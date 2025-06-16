package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.nio.charset.StandardCharsets;

@RestController
public class HelloController {

    private static final String BUCKET_NAME = "your-s3-bucket-name";
    private static final String OBJECT_KEY = "/output.txt";

    private final S3Client s3Client = S3Client.builder()
            .region(Region.of("ap-northeast-1")) // 適宜リージョンを指定
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {

        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("==== Request Headers ====");
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            System.out.println(header + ": " + request.getHeader(header));
        }
        System.out.println("=========================");

        byte[] content = "hello".getBytes(StandardCharsets.UTF_8);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(OBJECT_KEY)
                        .contentType("text/plain")
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(content)
        );

        return "hello!";
    }

    
    @GetMapping("/")
    public String main(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("==== Request Headers ====");
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            System.out.println(header + ": " + request.getHeader(header));
        }
        System.out.println("=========================");
        return "success!";
    }

}