package ru.classificator.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.classificator.entities.TextEntity;
import ru.classificator.services.TextService;

import java.util.List;

@Profile("server")
@Service
@RequiredArgsConstructor
public class InitiateUtils implements CommandLineRunner {

    private final TextService textService;

    public void run(String... args) throws Exception {

        textService.processOfText("Список текстов/");

        List <TextEntity> all = textService.getAll();
    }
}
