package ru.classificator.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.classificator.entities.TextEntity;
import ru.classificator.preprocessingdata.Text;
import ru.classificator.repositories.TextRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Profile("server")
@Service
@RequiredArgsConstructor

public class TextService {

    @Autowired
    Environment environment;

    private final TextRepository textRepository;

    protected final ru.classificator.preprocessingdata.PreprocessingOfText PreprocessingOfText =
            ru.classificator.preprocessingdata.PreprocessingOfText.getInstance();

    public void setTextEntity(TextEntity textEntity, String path) {
        Text text = PreprocessingOfText.readXMLText(path);
        textEntity.setTextTitle(text.getTitle());
        textEntity.setTextAuthor(text.getAuthor());
        textEntity.setModel(text.getModel());
        textEntity.setText(text.getText());
    }

    public void processOfText(String directory) {
        File dir = new File(directory);
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                TextEntity textEntity = new TextEntity();
                setTextEntity(textEntity, file.getPath());
                this.save(textEntity);
                System.out.println("Работа с файлом " + file.getPath() + " завершена.");
            }
        }
        System.out.println("Загрузка данных завершена. Сервер готов к работе. Запустите клиент по адресу: localhost: " +
                environment.getProperty("local.server.port"));
    }

    public void save(TextEntity textEntity) {
        textRepository.save(textEntity);
    }

    public List<TextEntity> getAll() {
        return textRepository.findAll();
    }

    public List<String> getTitle () {
        ArrayList<TextEntity> listTexts = (ArrayList<TextEntity>) getAll();
        ArrayList<String> listTitle = new ArrayList<>();
        for (TextEntity text: listTexts) {
            listTitle.add(text.getTextTitle());
        }
        return listTitle;
    }

    public String getModel (String title) {
        ArrayList<TextEntity> listTexts = (ArrayList<TextEntity>) getAll();
        for (TextEntity text: listTexts) {
            if (text.getTextTitle().equals(title))
                return text.getModel();

        }
        return "Lidianka";
    }

}
