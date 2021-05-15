package ru.classificator.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.classificator.classificatorweka.Classify;
import ru.classificator.services.PreprocessingOfText;
import ru.classificator.entities.Word;
import ru.classificator.services.TextService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

@Controller
@Service
@Profile("client")
public class MainController {

    @Autowired
    private TextService textService;

    private PreprocessingOfText PreprocessingOfText
            = ru.classificator.services.PreprocessingOfText.getInstance();
    private Classify ClassifyWord = new Classify();

    @GetMapping("/")
    public String greeting(Model model) {

        model.addAttribute("title", "Главная страница");

        model.addAttribute("models", textService.getTitle());
        return "home";
    }

    @PostMapping("/")
    public String postText(@RequestParam String inputText, @RequestParam String modelChoose, Model model) {

        if (inputText.isEmpty()) {
            return "error-input-text";
        }

        if (modelChoose.equals("Выберите модель")) {
            modelChoose = "Гимназистки. Лидианка";
        }

        System.out.println("Старт обработки запроса.");

        TreeMap<String, String> personClass = new TreeMap<>();
        TreeMap<String, String> objectClass = new TreeMap<>();
        TreeMap<String, String> somethingClass = new TreeMap<>();

        HashSet <Word> listOfNouns =  PreprocessingOfText.processOfWords(inputText);

        for (Word noun: listOfNouns) {
            String vector = noun.toStringVector();
            String [] word = vector.split(",");

            String classOfNoun = ClassifyWord.classifyObject(vector, textService.getModel(modelChoose));

                switch (classOfNoun) {
                    case "person":
                        personClass.put(vector, classOfNoun);
                        break;

                    case "object":
                        objectClass.put(vector, classOfNoun);
                        break;

                    case "something":
                        somethingClass.put(vector, classOfNoun);
                        break;
                }

        }

        System.out.println("Обработка завершена.");
        model.addAttribute("listPersonMale", filterNoun(1, 1, personClass));
        model.addAttribute("listPersonFemale", filterNoun(-1, 1, personClass));
        model.addAttribute("listPersonNon", filterNoun(0, 1, personClass));

        model.addAttribute("listObjectMale", filterNoun(1, 0, objectClass));
        model.addAttribute("listObjectFemale", filterNoun(-1, 0, objectClass));
        model.addAttribute("listObjectNon", filterNoun(0, 0, objectClass));

    //    model.addAttribute("listSomething", somethingClass);

        model.addAttribute("modelChoose", modelChoose);
        return "result";
    }


    public ArrayList<String> filterNoun(int gender, int animate, TreeMap<String, String> list) {
        ArrayList<String> sortedList = new ArrayList<>();
        list.forEach((vector, classOfNoun) -> {
            String [] vectorSplit = vector.split(",");

            if (Integer.parseInt(vectorSplit[1]) == gender && Integer.parseInt(vectorSplit[2]) == animate) {
                sortedList.add(vectorSplit[vectorSplit.length - 1]);
            }
        });
        return sortedList;
    }

}