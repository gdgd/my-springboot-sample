package com.example.demo.controller;

import com.example.demo.service.S3Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class HelloController {


    private final S3Service s3Service;
    private final String bucketName = "your-bucket-name"; // ここを実際のバケット名に
    private final String inputKey = "Input.txt";
    private final String outputKey = "output.txt";

    public HelloController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {

        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            System.out.println("==== Request Headers ====");
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
                System.out.println(header + ": " + request.getHeader(header));
            }
            System.out.println("=========================");

            // 1. Input.txtをS3からダウンロード
            File inputFile = s3Service.downloadFile(bucketName, inputKey);

            // 2. output.txtにHello！を書き込み
            File outputFile = File.createTempFile("output", ".txt");
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write("Hello！");
            }

            // 3. output.txtをS3にアップロード
            s3Service.uploadFile(bucketName, outputKey, outputFile);

            // tempファイル削除
            inputFile.delete();
            outputFile.delete();

            return "hello!";
        } catch (IOException e) {
            e.printStackTrace();
            return "エラー: " + e.getMessage();
        }
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