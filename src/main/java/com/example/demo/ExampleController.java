package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
public class ExampleController {

    @RequestMapping("/download-0")
    public void downloadDocument(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //jsonPersonal is the string that you're going to create dynamically in your code
        final String jsonPersonal = " some json encoded data here";

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setContentType("application/json");
        response.setContentLength(jsonPersonal.length());
        response.setHeader("Content-Disposition", "attachment");

        //this copies the content of your string to the output stream
        IOUtils.copy(IOUtils.toInputStream(jsonPersonal), response.getOutputStream());

        response.flushBuffer();
    }

    @GetMapping("/download-1")
    public StreamingResponseBody streamingResponseBody(HttpServletResponse response) throws IOException {
        response.setContentType("application/x-iso9660-image");
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"proxmox-ve_7.2-1.iso\"");
        InputStream inputStream = new FileInputStream("/Users/marcuschiu/Desktop/iso/proxmox-ve_7.2-1.iso");
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                System.out.println("Writing some bytes of file...");
                outputStream.write(data, 0, nRead);
            }
        };
    }

    // Download File using InputStream to HttpServletResponse
    @GetMapping("download-2")
    public void getSteamingFile1(HttpServletResponse response) throws IOException {
        response.setContentType("application/x-iso9660-image");
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"proxmox-ve_7.2-1.iso\"");
        InputStream inputStream = new FileInputStream("/Users/marcuschiu/Desktop/iso/proxmox-ve_7.2-1.iso");
        int nRead;
        while ((nRead = inputStream.read()) != -1) {
            response.getWriter().write(nRead);
        }
    }

    // InputStreamResource
    @GetMapping("download-3")
    public InputStreamResource FileSystemResource(HttpServletResponse response) throws IOException {
        response.setContentType("application/x-iso9660-image");
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"proxmox-ve_7.2-1.iso\"");
        InputStreamResource resource = new InputStreamResource(new FileInputStream("/Users/marcuschiu/Desktop/iso/proxmox-ve_7.2-1.iso"));
        return resource;
    }

    @GetMapping("/download-4")
    public ResponseEntity<Resource> download(String param) throws IOException {
        InputStreamResource resource = new InputStreamResource(new FileInputStream("/Users/marcuschiu/Desktop/iso/proxmox-ve_7.2-1.iso"));

        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"proxmox-ve_7.2-1.iso\"")
                .body(resource);
    }

    @GetMapping("/download-5")
    public ResponseEntity<Resource> download5() {
        InputStream inputStream = repeat("Hello World! ".getBytes(StandardCharsets.UTF_8), 76978456);
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"example.txt\"")
                .body(resource);
    }

    public static final Stream<String> streamString = Stream.of("Hello", "world", "hello");
    public static InputStream repeat(byte[] sample, int times) {
        return new InputStream() {
            private long pos = 0;
            private final long total = (long) sample.length * times;

            public int read() {
                System.out.println(pos);
                return pos < total ?
                        sample[(int) (pos++ % sample.length)] :
                        -1;
            }
        };
    }

    @GetMapping(value="/download-6")
    public ResponseEntity<StreamingResponseBody> streamData() {
        StreamingResponseBody responseBody = response -> {
            for (int i = 1; i <= 1000; i++) {
                try {
                    Thread.sleep(10);
                    response.write(("Data stream line - " + i + "\n").getBytes());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(responseBody);
    }

    @GetMapping("/download-7")
    public ResponseEntity<StreamingResponseBody> streamJson() {
        int maxRecords = 1000;
        StreamingResponseBody responseBody = response -> {
            for (int i = 1; i <= maxRecords; i++) {
                Student st = new Student("Name" + i, i);
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(st) +"\n";
                response.write(jsonString.getBytes());
                response.flush();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"download-7.txt\"")
                .body(responseBody);
    }

    Stream<Student> students = Stream.of(
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1),
            new Student("marcus", 1));

    @GetMapping("/download-8")
    public StreamingResponseBody streamingResponseBody2(HttpServletResponse response) throws IOException {
        response.setContentType("application/x-iso9660-image");
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"test.txt\"");
        return outputStream -> {
            students.forEachOrdered(student -> {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    System.out.println("output");
                    outputStream.write(mapper.writeValueAsString(student).getBytes());
                    outputStream.flush();
                    Thread.sleep(100);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }

    @Data
    @AllArgsConstructor
    static class Student {
        private String name;
        private Integer id;
    }
}
