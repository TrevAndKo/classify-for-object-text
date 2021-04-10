package ru.classificator.preprocessingdata;

import ru.classificator.classificatorweka.Classify;

import java.util.HashSet;
import java.util.TreeMap;

public class TreadForGettingVectorOfWords extends Thread {

    private InTeM _InTeM;
    private HashSet<String> _listNouns;
    private String _inputText;
    private String _modelChoose;
    private Thread t;

    private TreeMap<String, String> _personClass;
    private TreeMap<String, String> _objectClass;
    private TreeMap<String, String> _somethingClass;
    protected GettingWordData GettingWordData;
    private Classify ClassifyWord;
    private PreprocessingOfText PreprocessingOfText;

    public TreadForGettingVectorOfWords(HashSet<String> listNouns, String inputText, String modelChoose,
                                        String threadName, InTeM InTem) {
        this._InTeM = InTem;
        this._listNouns = listNouns;
        this._inputText = inputText;
        this._modelChoose = modelChoose;
        t = new Thread(this, threadName);

        this._personClass = new TreeMap<>();
        this._objectClass = new TreeMap<>();
        this._somethingClass = new TreeMap<>();
        this.GettingWordData = ru.classificator.preprocessingdata.GettingWordData.getInstance();
        PreprocessingOfText = ru.classificator.preprocessingdata.PreprocessingOfText.getInstance();
        ClassifyWord = new Classify();
    }

    public void Start() {
        t.start();
    }

    public void run() {
        for (String noun: this._listNouns) {

            String classOfNoun = ClassifyWord.classifyObject(PreprocessingOfText.getVectorOfWord(noun, this._inputText),
                    this._modelChoose);

            switch (classOfNoun) {
                case "person":
                    this._personClass.put(noun, classOfNoun);
                    break;

                case "object":
                    this._objectClass.put(noun, classOfNoun);
                    break;

                case "something":
                    this._somethingClass.put(noun, classOfNoun);
                    break;
            }
        }
    }

    public Thread getThread() { return t; }

    public TreeMap<String, String> getPersonClass () { return this._personClass; }

    public TreeMap<String, String> getObjectClass () { return this._objectClass; }

    public TreeMap<String, String> getSomethingClass () { return this._somethingClass; }
}
