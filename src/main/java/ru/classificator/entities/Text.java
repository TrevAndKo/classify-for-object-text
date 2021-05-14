package ru.classificator.entities;

public class Text {
    private String _author;
    private String _title;
    private String _text;
    private String _model;

    public Text () {
        this._author = "Не задан";
        this._title = "Не задано";
        this._text = "Не задано";
        this._model = "Не задано";
    }

    public Text (String author, String title, String text, String model) {
        this._author = author;
        this._title = title;
        this._text = text;
        this._model = model;
    }

    public Text (Text text) {
        this._author = text._author;
        this._title = text._title;
        this._text = text._text;
        this._model = text._model;
    }

    public String getAuthor () { return _author; }

    public String getTitle () { return _title; }

    public String getText () {return _text; }

    public String getModel () { return _model; }

    public void setAuthor (String author) { this._author = author; }

    public void setTitle (String title) { this._title = title; }

    public void setText (String text) { this._text = text; }

    public void setModel (String model) { this._model = model; }

}


