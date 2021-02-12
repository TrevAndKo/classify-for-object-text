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
import ru.classificator.preprocessingdata.PreprocessingOfText;
import ru.classificator.services.TextService;
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

        TreeMap<String, String> personClass = new TreeMap<>();
        TreeMap<String, String> objectClass = new TreeMap<>();
        TreeMap<String, String> somethingClass = new TreeMap<>();

        for (String noun: GettingWordData.getListAllNoun(inputText)) {

            String classOfNoun = ClassifyWord.classifyObject(PreprocessingOfText.getVectorOfWord(noun, inputText),
                    textService.getModel(modelChoose));

            switch (classOfNoun) {
                case "person":
                    personClass.put(noun, classOfNoun);
                    break;

                case "object":
                    objectClass.put(noun, classOfNoun);
                    break;

                case "something":
                    somethingClass.put(noun, classOfNoun);
                    break;
            }
        }

        System.out.println("Обработка завершена.");
        model.addAttribute("listPerson", personClass);
        model.addAttribute("listObject", objectClass);
        model.addAttribute("listSomething", somethingClass);
        return "result";
    }

}