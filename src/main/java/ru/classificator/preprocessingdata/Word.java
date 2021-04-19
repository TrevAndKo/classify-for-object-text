package ru.classificator.preprocessingdata;

import java.util.Objects;

public class Word {

    private String _word;
    private int _isName;
    private String _author;
    private String _title;
    private String _typeOfSpeech;
    private int _gender;
    private int _animate;
    private int _frequency;
    private int _mainWord;
    private int _dependentVerbs;
    private int _dependenceOfVerb;
    private int _dependentNoun;
    private int _dependentceOfNoun;
    private int _dependentAdjective;
    private double _intem;

    Word () {
        this._word = "Не задано";
        this._isName = 0;
        this._author = "Не задано";
        this._title = "Не задано";
        this._typeOfSpeech = "Не задано";
        this._gender = 0;
        this._animate = 0;
        this._frequency = 0;
        this._mainWord = 0;
        this._dependentVerbs = 0;
        this._dependenceOfVerb = 0;
        this._dependentNoun = 0;
        this._dependentceOfNoun = 0;
        this._dependentAdjective = 0;
        this._intem = 0.0;
    }

    Word (String word) {
        super();
        this._word = word;
    }

    Word (String word, int isName, String author, String title, int gender, int animate,
          int frequency, int mainWord, int dependentVerbs, int dependenceOfVerb,
          int dependentNoun, int dependentAdjective, int dependenceOfAdjective, double intem) {
        this._word = word;
        this._isName = isName;
        this._author = author;
        this._title = title;
        this._typeOfSpeech = "NOUN";
        this._gender = gender;
        this._animate = animate;
        this._frequency = frequency;
        this._mainWord = mainWord;
        this._dependentVerbs = dependentVerbs;
        this._dependenceOfVerb = dependenceOfVerb;
        this._dependentNoun = dependentNoun;
        this._dependentAdjective = dependentAdjective;
        this._dependentceOfNoun = dependenceOfAdjective;
        this._intem = intem;
    }

    Word (Word word) {
        this._word = word._word;
        this._isName = word._isName;
        this._author = word._author;
        this._title = word._title;
        this._gender = word._gender;
        this._animate = word._animate;
        this._frequency = word._frequency;
        this._mainWord = word._mainWord;
        this._dependentVerbs = word._dependentVerbs;
        this._dependenceOfVerb = word._dependenceOfVerb;
        this._dependentNoun = word._dependentNoun;
        this._dependentAdjective = word._dependentAdjective;
        this._dependentceOfNoun = word._dependentceOfNoun;
        this._intem = word._intem;
    }

    public String getWord() { return _word; }

    public int getIsName () { return _isName; }

    public String getAuthor() {
        return _author;
    }

    public String getTitle() { return _title; }

    public String getTypeOfSpeech() { return _typeOfSpeech; }

    public int getGender () { return _gender; }

    public int getAnimate () { return _animate; }

    public int getFrequency () { return _frequency; }

    public int getMainWord () { return _mainWord; }

    public int getDependentVerbs () { return _dependentVerbs; }

    public int getDependenceOfVerb () { return _dependenceOfVerb; }

    public int getDependentNoun () { return _dependentNoun; }

    public int getDependentAdjective () { return _dependentAdjective; }

    public int getDependenceOfNoun () { return _dependentceOfNoun; }

    public double getIntem () { return _intem; }

    public void setWord (String word) { this._word = word; }

    public void setIsName (int isName) { this._isName = isName; }

    public void setAuthor (String author) { this._author = author; }

    public void setTitle (String title) { this._title = title; }

    public void setTypeOfSpeech (String typeOfSpeech) { this._typeOfSpeech = typeOfSpeech; }

    public void setGender (int gender) { this._gender = gender; }

    public void setAnimate (int animate) { this._animate = animate; }

    public void setFrequency (int frequency) { this._frequency = frequency; }

    public void setMainWord (int mainWord) { this._mainWord = mainWord; }

    public void setDependentVerbs (int dependentVerbs) { this._dependentVerbs = dependentVerbs; }

    public void setDependenceOfVerb (int dependenceOfVerb) { this._dependenceOfVerb = dependenceOfVerb; }

    public void setDependentNoun (int dependentNoun) { this._dependentNoun = dependentNoun; }

    public void setDependentAdjective (int dependentAdjective) { this._dependentAdjective = dependentAdjective; }

    public void setDependenceOfNoun (int dependenceOfAdjective) { this._dependentceOfNoun =
            dependenceOfAdjective; }

    public void setIntem (double intem) { this._intem = intem; }

    public String toStringVector () {
        return (this.getIsName() + "," + this.getGender() + "," + this.getAnimate() + "," +
                this.getFrequency() + "," + this.getMainWord() + "," + this.getDependentVerbs() +
                "," +  this.getDependenceOfVerb() + "," + this.getDependentNoun()+ "," + this.getDependenceOfNoun()
                + "," + this.getDependentAdjective() /*+ "," + this.getIntem()*/ + "," + this.getWord() + "\n");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Word word = (Word) o;
        return _word.equals(word._word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_word);
    }

    public void updateWord (Word word) {

        this._frequency = this._frequency + word.getFrequency();
        this._mainWord = this._mainWord + word.getMainWord();
        this._dependentVerbs = this._dependentVerbs + word.getDependentVerbs();
        this._dependenceOfVerb = this._dependenceOfVerb + word.getDependenceOfVerb();
        this._dependentNoun = this._dependentNoun + word.getDependentNoun();
        this._dependentAdjective = this._dependentAdjective + word.getDependentAdjective();
        this._dependentceOfNoun = this._dependentceOfNoun + word.getDependenceOfNoun();
        this._intem = word.getIntem();
    }

}