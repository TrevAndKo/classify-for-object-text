package ru.classificator.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.classificator.entities.TextEntity;
import ru.classificator.preprocessingdata.Text;
import ru.classificator.repositories.TextRepository;

import java.io.File;
import java.util.List;

@Profile("server")
@Service
@RequiredArgsConstructor

public class TextService {

    private final TextRepository textRepository;

//    private static class SingeltonTextService {
//        private final static TextService instance = new TextService();
//    }
//
//    public static TextService getInstance () {
//        return SingeltonTextService.instance;
//    }


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
        File dir = new File(directory); //path указывает на директорию
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                TextEntity textEntity = new TextEntity();
                setTextEntity(textEntity, file.getPath());
                this.save(textEntity);
                System.out.println("Работа с файлом " + file.getPath() + " завершена.");
            }
        }
    }

    public void save(TextEntity textEntity) {
        textRepository.save(textEntity);
    }

    public List<TextEntity> getAll() {
        return textRepository.findAll();
    }



}
