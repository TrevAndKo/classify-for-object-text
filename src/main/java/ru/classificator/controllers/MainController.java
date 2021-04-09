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
import ru.classificator.preprocessingdata.GettingWordData;
import ru.classificator.preprocessingdata.InTeM;
import ru.classificator.preprocessingdata.PreprocessingOfText;
import ru.classificator.preprocessingdata.TreadForGettingVectorOfWords;
import ru.classificator.services.TextService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

@Controller
@Service
@Profile("client")
public class MainController {

    @Autowired
    private TextService textService;
    private GettingWordData GettingWordData = ru.classificator.preprocessingdata.GettingWordData.getInstance();
    private PreprocessingOfText PreprocessingOfText = ru.classificator.preprocessingdata.PreprocessingOfText
            .getInstance();


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

        TreeMap<String, String> personClass = new TreeMap<>();
        TreeMap<String, String> objectClass = new TreeMap<>();
        TreeMap<String, String> somethingClass = new TreeMap<>();
        int numTread = 4;


        ArrayList<String> listAllNouns = new ArrayList<>();
        for (String noun: GettingWordData.getListAllNoun(inputText)) {
            listAllNouns.add(noun);
        }

        int size = (int) Math.floor(listAllNouns.size()/numTread);
        int count = 0;
        ArrayList<HashSet<String>> listsOfNouns = new ArrayList<>();
        for (int i = 0; i < numTread; i++) {
            HashSet<String> tempList = new HashSet<>();
            for (int j = count; j < (count + size); j++) {
                tempList.add(listAllNouns.get(j));
            }

            listsOfNouns.add(tempList);
            count = count + size;

            if (i == 2) {
                size = listAllNouns.size() - size*3;
            }
        }

        InTeM InTeM = PreprocessingOfText.createIntem(inputText);

        ArrayList<TreadForGettingVectorOfWords> treads = new ArrayList<>();
        for (HashSet<String> listOfNouns : listsOfNouns) {
            treads.add(new TreadForGettingVectorOfWords(listOfNouns, inputText, textService.getModel(modelChoose),
                    "Поток " + listsOfNouns.indexOf(listOfNouns), InTeM));
        }

        treads.get(0).Start();
        treads.get(1).Start();

        try {
            treads.get(0).getThread().join();
            treads.get(1).getThread().join();
        }
        catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
//        for (TreadForGettingVectorOfWords tread : treads) {
//            tread.Start();
//        }
//
//        try {
//            for (TreadForGettingVectorOfWords tread : treads) {
//                tread.getThread().join();
//            }
//        }
//        catch (InterruptedException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//
//        for (TreadForGettingVectorOfWords tread : treads) {
//            personClass.putAll(tread.getPersonClass());
//            objectClass.putAll(tread.getObjectClass());
//            somethingClass.putAll(tread.getSomethingClass());
//        }


        System.out.println("Обработка завершена.");
        model.addAttribute("listPerson", personClass);
        model.addAttribute("listObject", objectClass);
        model.addAttribute("listSomething", somethingClass);
        return "result";
    }

}