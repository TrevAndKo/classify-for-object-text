package ru.classificator.controllers;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.classificator.classificatorweka.Classify;
import ru.classificator.preprocessingdata.GettingWordData;
import ru.classificator.preprocessingdata.PreprocessingOfText;
import ru.classificator.preprocessingdata.Word;
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

    private GettingWordData GettingWordData = ru.classificator.preprocessingdata.GettingWordData.getInstance();
    private PreprocessingOfText PreprocessingOfText
            = ru.classificator.preprocessingdata.PreprocessingOfText.getInstance();
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

        System.out.println("Старт обработки запроса.");

        TreeMap<String, Pair<String, String>> personClass = new TreeMap<>();
        TreeMap<String, Pair<String, String>> objectClass = new TreeMap<>();
        TreeMap<String, Pair<String, String>> somethingClass = new TreeMap<>();

        HashSet <Word> ttt =  PreprocessingOfText.processOfWords(inputText);

        for (Word noun: ttt) {
            String vector = noun.toStringVector();

            String classOfNoun = ClassifyWord.classifyObject(vector, textService.getModel(modelChoose));

                switch (classOfNoun) {
                    case "person":
                        personClass.put(noun.getWord(), new Pair (classOfNoun, vector));
                        break;

                    case "object":
                        objectClass.put(noun.getWord(), new Pair (classOfNoun, vector));
                        break;

                    case "something":
                        somethingClass.put(noun.getWord(), new Pair (classOfNoun, vector));
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

        model.addAttribute("listSomething", somethingClass);

        return "result";
    }


    public ArrayList<String> filterNoun(int gender, int animate, TreeMap<String, Pair<String, String>> list) {
        ArrayList<String> sortedList = new ArrayList<>();
        list.forEach((word, chara) -> {
            String[] vector = chara.getValue().split(",");
            if (Integer.parseInt(vector[1]) == gender && Integer.parseInt(vector[1]) == gender) {
                sortedList.add(word);
            }
        });
        return sortedList;
    }

}