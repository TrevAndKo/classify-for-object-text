package ru.classificator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.classificator.entities.TextEntity;
import ru.classificator.repositories.TextRepository;
import ru.classificator.services.TextService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

@Controller
public class TextsController {

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private TextService textService;

    @GetMapping("/texts")
    public String textMain (Model model) {

        Iterable <TextEntity> texts = textRepository.findAll();

        model.addAttribute("texts", texts);
        model.addAttribute("listAuthor", textService.getListAuthor());

        return "text-main";
    }

    @GetMapping("/texts/{id}")
    public String textDetails (@PathVariable(value = "id") Integer textId, Model model) {

        if (textRepository.existsById(textId)) {
            Optional <TextEntity> text = textRepository.findById(textId);
            ArrayList <TextEntity> result = new ArrayList<>();
            text.ifPresent(result::add);
            model.addAttribute("text", result);
            return "text-details";
        }

        else return "error";

    }

    @GetMapping("/texts/title")
    public String textSortedByTitle (Model model) {

        ArrayList<TextEntity> sortedList = (ArrayList<TextEntity>) textRepository.findAll();

                Collections.sort(sortedList, new Comparator<TextEntity>() {
                @Override
                public int compare(TextEntity o1, TextEntity o2) {
                    return o1.getTextTitle().compareTo(o2.getTextTitle());
                }
            });

        Iterable <TextEntity> texts = sortedList;
        model.addAttribute("texts", texts);

        return "sort-by-title";
    }

    @GetMapping("/texts/author")
    public String textSortedByAuthor (Model model) {

        ArrayList<TextEntity> sortedList = (ArrayList<TextEntity>) textRepository.findAll();

        Collections.sort(sortedList, new Comparator<TextEntity>() {
            @Override
            public int compare(TextEntity o1, TextEntity o2) {
                return o1.getTextAuthor().compareTo(o2.getTextAuthor());
            }
        });

        Iterable <TextEntity> texts = sortedList;
        model.addAttribute("texts", texts);

        return "sort-by-author";
    }

    @PostMapping("/texts/byAuthor")
    public String textFilterByAuthor (String author, Model model) {

        ArrayList<TextEntity> sortedList = (ArrayList<TextEntity>) textService.getAuthor(author);
        model.addAttribute("texts", sortedList);

        return "text-sorted-author";
    }

}
