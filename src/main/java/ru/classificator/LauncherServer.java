package ru.classificator;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class LauncherServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ClassificatorApplication.class)
                .profiles("server")
                .run(args);
    }
}
