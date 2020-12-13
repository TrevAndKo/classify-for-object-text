package tempClassify;

public class Text {
    private String _author;
    private String _title;
    private String _text;

    Text () {
        this._author = "Не задан";
        this._title = "Не задано";
        this._text = "Не задано";
    }

    Text (String author, String title, String text) {
        this._author = author;
        this._title = title;
        this._text = text;
    }

    Text (Text text) {
        this._author = text._author;
        this._title = text._title;
        this._text = text._text;
    }

    public String getAuthor () { return _author; }

    public String getTitle () { return _title; }

    public String getText () {return _text; }

    public void setAuthor (String author) { this._author = author; }

    public void setTitle (String title) { this._title = title; }

    public void setText (String text) { this._text = text; }


}


