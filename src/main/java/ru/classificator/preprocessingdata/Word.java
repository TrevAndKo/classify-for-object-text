package ru.classificator.preprocessingdata;

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
    private int _dependentAdjective;
    private int _dependenceOfAdjective;
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
        this._dependentAdjective = 0;
        this._dependenceOfAdjective = 0;
        this._intem = 0.0;
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
        this._dependenceOfAdjective = dependenceOfAdjective;
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
        this._dependenceOfAdjective = word._dependenceOfAdjective;
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

    public int getDependenceOfAdjective () { return _dependenceOfAdjective; }

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

    public void setDependenceOfAdjective (int dependenceOfAdjective) { this._dependenceOfAdjective =
            dependenceOfAdjective; }

    public void setIntem (double intem) { this._intem = intem; }

    public String toStringVector () {
        return (this.getIsName() + "," + this.getGender() + "," + this.getAnimate() + "," +
                this.getFrequency() + "," + this.getMainWord() + "," + this.getDependentVerbs() +
                "," +  this.getDependenceOfVerb() + "," + this.getDependentNoun() + "," + this.getDependentAdjective()
                + "," + this.getDependenceOfAdjective() + "," + this.getIntem() + "," + this.getWord() + "\n");
    }

}

