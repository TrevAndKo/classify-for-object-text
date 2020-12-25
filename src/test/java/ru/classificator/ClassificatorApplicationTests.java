package ru.classificator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.classificator.controllers.MainController;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClassificatorApplicationTests {

    @Autowired
    private MainController controller;

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

}
