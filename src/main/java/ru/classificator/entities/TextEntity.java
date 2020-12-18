package ru.classificator.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity

@Data

@Table(name = "texts_table")

public class TextEntity {
    @Id

    @Column(name = "id_text")

    @GenericGenerator(name = "generator", strategy = "increment")

    @GeneratedValue(generator = "generator")

    private Integer id;

    @Column(name = "text_title")
    private String textTitle;

    @Column(name = "text_author")
    private String textAuthor;

    @Column(name = "text", length = 1000000000 )
    private String text;

    @Column(name = "model")
    private String model;

//    @Column(name = "model")
//    private String model;
    public TextEntity(){}

    public Integer getId() {
        return id;
    }

    public String getTextTitle() {
        return this.textTitle;
    }

    public TextEntity setTextTitle(String title) {
        this.textTitle = title;
        return this;
    }

    public String getTextAuthor() {
        return this.textAuthor;
    }

    public TextEntity setTextAuthor(String author) {
        this.textAuthor = author;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public TextEntity setText(String text) {
        this.text = text;
        return this;
    }

    public String getModel () { return this.model; }

    public void setModel (String model) { this.model = model; }

}


