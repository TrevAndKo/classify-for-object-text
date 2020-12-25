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
import java.util.HashMap;

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

        HashMap<String, String> nounAndClass = new HashMap<>();

        for (String noun: GettingWordData.getListAllNoun(inputText)) {
            nounAndClass.put(noun, ClassifyWord.classifyObject(PreprocessingOfText.getVectorOfWord(noun, inputText),
                    textService.getModel(modelChoose)));
        }

        model.addAttribute("list", nounAndClass);
        return "result";
    }

}