package ru.classificator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ClassificatorApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ClassificatorApplication.class)
                .profiles("server")
                .run(args);
    }
}