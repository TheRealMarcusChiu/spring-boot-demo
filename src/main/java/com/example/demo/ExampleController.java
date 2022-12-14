package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class ExampleController {

    @GetMapping(value = "/zip")
    public ResponseEntity<StreamingResponseBody> getZipFileStream() {
        StreamingResponseBody stream = this::writeToStream;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

    Stream<Student> students = Stream.of(
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1),
            new Student("Name", 1)
    );

    private void writeToStream(OutputStream os) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(os));
        ZipEntry zipEntry = new ZipEntry("data.csv");
        zipOut.putNextEntry(zipEntry);
        Writer writer = new BufferedWriter(new OutputStreamWriter(zipOut, StandardCharsets.UTF_8.newEncoder()));

        String header = displayHeader();
        writer.write(header + "\n");
        writer.flush();

        ObjectWriter objectWriter = test();
        students.forEachOrdered(student -> {
            try {
                String string = objectWriter.writeValueAsString(student);
                writer.write(string);
                writer.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private ObjectWriter test() {
        // 1 - Initiatize Mapper
        CsvMapper mapper = new CsvMapper();

        // 2.1 - Initialize Schema (from POJO adheres to @JsonIgnore, etc)
        CsvSchema schema = mapper.schemaFor(Student.class);

        // 3 - Initialize Writer
        return mapper.writerFor(Student.class).with(schema);
    }

    public static String displayHeader() {
        CsvMapper mapper = new CsvMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(Student.class);
        BeanDescription beanDescription = mapper.getSerializationConfig().introspect(javaType);
        List<BeanPropertyDefinition> properties = beanDescription.findProperties();

        List<String> columnNames = properties.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
        String header = String.join(",", columnNames);

        return header;
    }
//    @GetMapping(value = "/csv")
//    public ResponseEntity<StreamingResponseBody> getCsvFile() {
//        StreamingResponseBody stream = output -> {
//            Writer writer = new BufferedWriter(new OutputStreamWriter(output));
//            writer.write("name,rollNo"+"\n");
//            for (int i = 1; i <= 10000; i++) {
//                Student st = new Student("Name" + i, i);
//                writer.write(st.getName() + "," + st.getRollNo() + "\n");
//                writer.flush();
//            }
//        };
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv")
//                .contentType(MediaType.TEXT_PLAIN)
//                .body(stream);
//    }

//    @GetMapping(value = "/zip")
//    public ResponseEntity<StreamingResponseBody> getZipFileStream() {
//        StreamingResponseBody stream = output -> writeToStream(output);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.zip")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(stream);
//    }
//    public void writeToStream(OutputStream os) throws IOException {
//        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(os));
//        ZipEntry e = new ZipEntry("data.csv");
//        zipOut.putNextEntry(e);
//        Writer writer = new BufferedWriter(new OutputStreamWriter(zipOut, Charset.forName("UTF-8").newEncoder()));
//        for (int i = 1; i <= 1000; i++) {
//            Student st = new Student("Name" + i, i);
//            writer.write(st.getName() + "," + st.getRollNo() + "\n");
//            writer.flush();
//        }
//        if (writer != null) {
//            try {
//                writer.flush();
//                writer.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }

//    @GetMapping(value="/download-6")
//    public ResponseEntity<StreamingResponseBody> streamData() {
//        StreamingResponseBody responseBody = response -> {
//            for (int i = 1; i <= 1000; i++) {
//                response.write(("Data stream line - " + i + "\n").getBytes());
//            }
//        };
//        return ResponseEntity.ok()
//                .contentType(MediaType.TEXT_PLAIN)
//                .body(responseBody);
//    }

//    @RequestMapping("/download-0")
//    public void downloadDocument(final HttpServletResponse response) throws IOException {
//        final String jsonPersonal = " some json encoded data here";
//
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Transfer-Encoding", "binary");
//        response.setContentType("application/json");
//        response.setContentLength(jsonPersonal.length());
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"example.json\"");
//
//        IOUtils.copy(IOUtils.toInputStream(jsonPersonal), response.getOutputStream());
//
//        response.flushBuffer();
//    }

//    @GetMapping("/download-1")
//    public StreamingResponseBody streamingResponseBody(HttpServletResponse response) throws IOException {
//        response.setContentType("application/x-iso9660-image");
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"proxmox-ve_7.2-1.iso\"");
//        InputStream inputStream = new FileInputStream("/Users/marcuschiu/Desktop/iso/proxmox-ve_7.2-1.iso");
//        return outputStream -> {
//            int nRead;
//            byte[] data = new byte[1024];
//            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
//                System.out.println("Writing some bytes of file...");
//                outputStream.write(data, 0, nRead);
//            }
//        };
//    }

    // Download File using InputStream to HttpServletResponse
//    @GetMapping("download-2")
//    public void getSteamingFile1(HttpServletResponse response) throws IOException {
//        response.setContentType("application/x-iso9660-image");
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"proxmox-ve_7.2-1.iso\"");
//        InputStream inputStream = new FileInputStream("/Users/marcuschiu/Desktop/iso/proxmox-ve_7.2-1.iso");
//        int nRead;
//        while ((nRead = inputStream.read()) != -1) {
//            response.getWriter().write(nRead);
//        }
//    }

    // InputStreamResource
//    @GetMapping("/download-3")
//    public InputStreamResource FileSystemResource(HttpServletResponse response) throws IOException {
//        response.setContentType("application/x-iso9660-image");
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"proxmox-ve_7.2-1.iso\"");
//        InputStreamResource resource = new InputStreamResource(new FileInputStream("/Users/marcuschiu/Desktop/iso/proxmox-ve_7.2-1.iso"));
//        return resource;
//    }

//    @GetMapping("/download-4")
//    public ResponseEntity<Resource> download(String param) throws IOException {
//        InputStreamResource resource = new InputStreamResource(new FileInputStream("/Users/marcuschiu/Desktop/iso/proxmox-ve_7.2-1.iso"));
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"proxmox-ve_7.2-1.iso\"")
//                .body(resource);
//    }

//    @GetMapping("/download-5")
//    public ResponseEntity<Resource> download5() {
//        InputStream inputStream = repeat("Hello World! ".getBytes(StandardCharsets.UTF_8), 76978456);
//        InputStreamResource resource = new InputStreamResource(inputStream);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"example.txt\"")
//                .body(resource);
//    }

//    public static final Stream<String> streamString = Stream.of("Hello", "world", "hello");
//    public static InputStream repeat(byte[] sample, int times) {
//        return new InputStream() {
//            private long pos = 0;
//            private final long total = (long) sample.length * times;
//
//            public int read() {
//                System.out.println(pos);
//                return pos < total ?
//                        sample[(int) (pos++ % sample.length)] :
//                        -1;
//            }
//        };
//    }

//    @GetMapping(value="/download-6")
//    public ResponseEntity<StreamingResponseBody> streamData() {
//        StreamingResponseBody responseBody = response -> {
//            for (int i = 1; i <= 1000; i++) {
//                response.write(("Data stream line - " + i + "\n").getBytes());
//            }
//        };
//        return ResponseEntity.ok()
//                .contentType(MediaType.TEXT_PLAIN)
//                .body(responseBody);
//    }

//    @GetMapping("/download-7")
//    public ResponseEntity<StreamingResponseBody> streamJson() {
//        int maxRecords = 1000;
//        StreamingResponseBody responseBody = response -> {
//            for (int i = 1; i <= maxRecords; i++) {
//                Student st = new Student("Name" + i, i);
//                ObjectMapper mapper = new ObjectMapper();
//                String jsonString = mapper.writeValueAsString(st) +"\n";
//                response.write(jsonString.getBytes());
//                response.flush();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"download-7.txt\"")
//                .body(responseBody);
//    }

//    Stream<Student> students = Stream.of(
//            new Student("marcus", 1),
//            new Student("marcus", 1),
//            new Student("marcus", 1),
//            new Student("marcus", 1),
//            new Student("marcus", 1));
//
//    @GetMapping("/download-8")
//    public StreamingResponseBody streamingResponseBody2(HttpServletResponse response) throws IOException {
//        response.setContentType("application/x-iso9660-image");
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.txt\"");
//        return outputStream -> {
//            students.forEachOrdered(student -> {
//                ObjectMapper mapper = new ObjectMapper();
//                try {
//                    System.out.println("output");
//                    outputStream.write(mapper.writeValueAsString(student).getBytes());
//                    outputStream.flush();
//                    Thread.sleep(100);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        };
//    }

    @Data
    @AllArgsConstructor
    static class Student {
        private String name;
        private Integer id;
    }
}
