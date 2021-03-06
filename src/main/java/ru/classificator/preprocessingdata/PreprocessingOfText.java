package ru.classificator.preprocessingdata;

import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import ru.textanalysis.tawt.ms.external.sp.BearingPhraseExt;
import ru.textanalysis.tawt.ms.external.sp.OmoFormExt;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PreprocessingOfText {

    protected GettingWordData GettingWordData = ru.classificator.preprocessingdata.GettingWordData.getInstance();

    private static class SingeltonPreprocessingOfText {
        private final static PreprocessingOfText instance = new PreprocessingOfText();
    }

    public static PreprocessingOfText getInstance() { return SingeltonPreprocessingOfText.instance; }

    private void createXML(int numberOfFile, String word, String isName, String author, String title,
                                  String typeOfSpeech, String gender, String animate, String frequency,
                                  String mainWord, String dependentVerbs, String dependenceOfVerb, String dependentNoun,
                                  String length, String dependentAdjective, String dependenceOfAdjective) {


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement =
                    doc.createElement( "doc");

            rootElement.appendChild(setWordChara(doc,"word", word));
            rootElement.appendChild(setWordChara(doc,"isName", isName));
            rootElement.appendChild(setWordChara(doc, "author", author));
            rootElement.appendChild(setWordChara(doc, "title", title));
            rootElement.appendChild(setWordChara(doc, "typeOfSpeech", typeOfSpeech));
            rootElement.appendChild(setWordChara(doc, "gender", gender));
            rootElement.appendChild(setWordChara(doc, "animate", animate));
            rootElement.appendChild(setWordChara(doc, "frequency", frequency));
            rootElement.appendChild(setWordChara(doc,"length", length));
            rootElement.appendChild(setWordChara(doc, "mainWord", mainWord));
            rootElement.appendChild(setWordChara(doc,"dependentVerbs", dependentVerbs));
            rootElement.appendChild(setWordChara(doc,"dependenceOfVerb", dependenceOfVerb));
            rootElement.appendChild(setWordChara(doc,"dependentNoun", dependentNoun));
            rootElement.appendChild(setWordChara(doc, "dependentAdjective", dependentAdjective));
            rootElement.appendChild(setWordChara(doc,"dependenceOfAdjective", dependenceOfAdjective));


            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);


            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File(numberOfFile + ".xml"));

            transformer.transform(source, console);
            transformer.transform(source, file);
            System.out.println("Создание XML файла закончено");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createXML(int numberOfFile, Word word) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement =
                    doc.createElement( "doc");

            rootElement.appendChild(setWordChara(doc,"word", word.getWord()));
            rootElement.appendChild(setWordChara(doc,"isName", Integer.toString(word.getIsName())));
            rootElement.appendChild(setWordChara(doc, "author", word.getAuthor()));
            rootElement.appendChild(setWordChara(doc, "title", word.getTitle()));
            rootElement.appendChild(setWordChara(doc, "typeOfSpeech", word.getTypeOfSpeech()));
            rootElement.appendChild(setWordChara(doc, "gender", Integer.toString(word.getGender())));
            rootElement.appendChild(setWordChara(doc, "animate", Integer.toString(word.getAnimate())));
            rootElement.appendChild(setWordChara(doc, "frequency", Integer.toString(word.getFrequency())));
            rootElement.appendChild(setWordChara(doc, "length", Integer.toString(word.getLength())));
            rootElement.appendChild(setWordChara(doc, "mainWord", Integer.toString(word.getMainWord())));
            rootElement.appendChild(setWordChara(doc,"dependentVerbs",
                    Integer.toString(word.getDependentVerbs())));
            rootElement.appendChild(setWordChara(doc,"dependenceOfVerb",
                    Integer.toString(word.getDependenceOfVerb())));
            rootElement.appendChild(setWordChara(doc,"dependentNoun", Integer.toString(word.getDependentNoun())));
            rootElement.appendChild(setWordChara(doc, "dependentAdjective",
                    Integer.toString(word.getDependentAdjective())));
            rootElement.appendChild(setWordChara(doc,"dependenceOfAdjective",
                    Integer.toString(word.getDependenceOfAdjective())));


            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            StreamResult file = new StreamResult(new File("Список слов/" + numberOfFile + ".xml"));

            transformer.transform(source, file);
            System.out.println("Создание XML файла " + numberOfFile + ".xml закончено");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Node setWordChara(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    public Text readXMLText (String path) {

        Text text = new Text ();

        File xmlFile = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();


            NodeList nodeList = doc.getElementsByTagName("doc");

            text = getText(nodeList.item(0));

            return text;

        } catch (Exception exc) {
            exc.printStackTrace();
            return text;
        }
    }

    public Word getWordFromXML (String path) {
        Word wordChara = new Word ();

        File xmlFile = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("doc");

            wordChara = getWord(nodeList.item(0));

            return wordChara;

        } catch (Exception exc) {
            exc.printStackTrace();
            return wordChara;
        }
    }

    public String getVectorOfWord (String word, String text) {

        HashSet <String> listOfAllNoun = GettingWordData.getListAllNoun(GettingWordData.
                toLowerSentence(text));

        return String.valueOf(GettingWordData.checkNameOfAWord(word)) + "," +
                String.valueOf(GettingWordData.checkGenderOfAWord(word)) + "," +
                String.valueOf(GettingWordData.getAnimateOfAWord(word)) + "," +
                String.valueOf(getFrequency(word, text))+ "," +
                String.valueOf(GettingWordData.getLengthOfWord(word)) + "," +
                String.valueOf(countMain(word, text)) + "," +
                String.valueOf(countDependsFromWord(word, "VERB", text)) + "," +
                String.valueOf(countDependsWord(word, "VERB", text)) + "," +
                String.valueOf(countDependsFromWord(word, "ADJ", text)) + "," +
                String.valueOf(countDependsWord(word, "ADJ", text)) + "," +
                String.valueOf(countDependsFromWord(word, "NOUN", text));
    }

    private int processOfWords (String path, int count) {

        Text textFromXML = readXMLText(path);

        HashSet <String> listOfAllNoun = GettingWordData.getListAllNoun(GettingWordData.
                toLowerSentence(textFromXML.getText()));

        for (String noun: listOfAllNoun) {
            Word word = new Word();
            word.setWord(GettingWordData.getInitFormOfAWord(noun));
            word.setIsName(GettingWordData.checkNameOfAWord(noun));
            word.setAuthor(textFromXML.getAuthor());
            word.setTitle(textFromXML.getTitle());
            word.setTypeOfSpeech("NOUN");
            word.setGender(GettingWordData.checkGenderOfAWord(noun));
            word.setAnimate(GettingWordData.getAnimateOfAWord(noun));
            word.setFrequency(getFrequency(noun, textFromXML.getText()));
            word.setLength(GettingWordData.getLengthOfWord(noun));
            word.setMainWord(countMain(noun, textFromXML.getText()));
            word.setDependentVerbs(countDependsFromWord(noun, "VERB", textFromXML.getText()));
            word.setDependenceOfVerb(countDependsWord(noun, "VERB", textFromXML.getText()));
            word.setDependentAdjective(countDependsFromWord(noun, "ADJ", textFromXML.getText()));
            word.setDependenceOfAdjective(countDependsWord(noun, "ADJ", textFromXML.getText()));
            word.setDependentNoun(countDependsFromWord(noun, "NOUN", textFromXML.getText()));
            createXML(count++, word);
        }
        return count;
    }

    public void processOfText (String directory) {
        File dir = new File(directory); //path указывает на директорию
        int count = 0;
        for ( File file : dir.listFiles() ){
            if (file.isFile())
                count = processOfWords(file.getPath(), count);
            System.out.println("Работа с файлом " + file.getPath() + " завершена.");
        }
    }

    private Text getText(Node node) {
        Text text = new Text ();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            text.setAuthor(getTagValue("author", element));
            text.setTitle(getTagValue("title", element));
            text.setModel(getTagValue("model", element));
            text.setText(getTagValue("text", element));
        }
        return text;
    }

    private Word getWord(Node node) {
        Word word = new Word ();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            word.setWord(getTagValue("word", element));
            word.setIsName(Integer.parseInt(getTagValue("isName", element)));
            word.setAuthor(getTagValue("author", element));
            word.setTitle(getTagValue("title", element));
            word.setTypeOfSpeech(getTagValue("typeOfSpeech", element));
            word.setGender(Integer.parseInt(getTagValue("gender", element)));
            word.setAnimate(Integer.parseInt(getTagValue("animate", element)));
            word.setFrequency(Integer.parseInt(getTagValue("frequency", element)));
            word.setLength(Integer.parseInt(getTagValue("length", element)));
            word.setMainWord(Integer.parseInt(getTagValue("mainWord", element)));
            word.setDependentVerbs(Integer.parseInt(getTagValue("dependentVerbs", element)));
            word.setDependenceOfVerb(Integer.parseInt(getTagValue("dependenceOfVerb", element)));
            word.setDependentNoun(Integer.parseInt(getTagValue("dependentNoun", element)));
            word.setDependentAdjective(Integer.parseInt(getTagValue("dependentAdjective", element)));
            word.setDependenceOfAdjective(Integer.parseInt(getTagValue("dependenceOfAdjective", element)));
        }

        return word;
    }

    private String getTagValue(String tag, Element element) {

        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();

        Node node;

        for(int index = 0; index < nodeList.getLength(); index++){
            if(nodeList.item(index) instanceof CharacterData){
                CharacterData child  = (CharacterData) nodeList.item(index);
                String data = child.getData();

                if(data != null && data.trim().length() > 0)
                    return child.getData();
            }
        }
        node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public int getFrequency (String wordToCheck, String text) {
        int count = 1;
        List<List<String>> listFromText = GettingWordData.getListFromAllText(text);
        for (List <String> sentence: listFromText) {
            for (String word: sentence) {
                if (word.toLowerCase().equals(wordToCheck.toLowerCase())) { count++; }
            }
        }
        return count;
    }

    public int countMain (String word, String text) {
        List<String> listAllSent = GettingWordData.getListOfSentences(text);
        int countFreq = 0;

        for (String sentence : listAllSent) {
            if (getListOfMainWords(sentence).contains(word)) {
                countFreq++;
            }
        }
        return countFreq;
    }

    public HashSet<String> getListOfMainWords (String sentence) {
        List<BearingPhraseExt> treeSentence = GettingWordData.sp.getTreeSentenceWithoutAmbiguity(sentence);
        HashSet<String> listOfMainWord = new HashSet<>();
        for (BearingPhraseExt word: treeSentence) {
            if (word.getMainOmoForms().isEmpty()) {
                continue;
            }
            listOfMainWord.add(word
                    .getMainOmoForms().get(0).
                            getCurrencyOmoForm().getInitialFormString());

        }
        return listOfMainWord;
    }


    // 17 - сущ. 18, 19 - прил. 20, 21 - гл.


    public int countDependsFromWord(String word, String typeOfSpeech, String text) {
        List<String> listAllSent = GettingWordData.getListOfSentences(text);
        int countFreq = 0;

        for (String sentence : listAllSent) {
            if (sentence.contains(word)) {
                List<BearingPhraseExt> treeSentence = GettingWordData.sp.getTreeSentenceWithoutAmbiguity(sentence);
                for (BearingPhraseExt wordFromSentence : treeSentence) {
                    for (OmoFormExt w : wordFromSentence.getMainOmoForms()) {
                        if (w.haveDep() && word.equals(w.getCurrencyOmoForm().getInitialFormString())) {

                            switch (typeOfSpeech) {
                                case ("VERB"):
                                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                            Byte.parseByte("20")) ||
                                            GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                                    Byte.parseByte("21"))) {
                                        countFreq++;
                                    }
                                    break;

                                case ("ADJ"):
                                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                        Byte.parseByte("18")) ||
                                        GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                                Byte.parseByte("19"))) {
                                    countFreq++;
                                    }
                                    break;

                                case ("NOUN"):
                                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                            Byte.parseByte("17"))) {
                                        countFreq++;
                                    }
                                    break;
                            }

                        }

                    }
                }
            }
        }
        return countFreq;
    }

    public int countDependsWord (String word, String typeOfSpeech, String text) {
        List<String> listAllSent = GettingWordData.getListOfSentences(text);
        int countFreq = 0;
        for (String sentence : listAllSent) {
            if (sentence.contains(word)) {
                List<BearingPhraseExt> treeSentence = GettingWordData.sp.getTreeSentenceWithoutAmbiguity(sentence);
                for (BearingPhraseExt wordFromSentence : treeSentence) {
                    for (OmoFormExt w : wordFromSentence.getMainOmoForms()) {
                        if (w.haveMain() && word.equals(w.getCurrencyOmoForm().getInitialFormString())) {

                            switch (typeOfSpeech) {
                                case ("VERB"):
                                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                            Byte.parseByte("20")) ||
                                            GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                                    Byte.parseByte("21"))) {
                                        countFreq++;
                                    }
                                    break;

                                case ("ADJ"):
                                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                            Byte.parseByte("18")) ||
                                            GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                                    Byte.parseByte("19"))) {
                                        countFreq++;
                                    }
                                    break;

                                case ("NOUN"):
                                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(word).contains(
                                            Byte.parseByte("17"))) {
                                        countFreq++;
                                    }
                                    break;
                            }

                        }

                    }
                }
            }
        }
        return countFreq;
    }

    public void updateFileData (String directory) {
        File dir = new File(directory); //path указывает на директорию
        List<File> lst = new ArrayList<>();
        for ( File file : dir.listFiles() ){
            if (file.isFile()) {
                Word word = getWordFromXML(file.getPath());
                String filePath = word.getTitle() + ". " + word.getAuthor() + ".txt";
                // обновляем файл с помощью FileWriter
                appendUsingFileWriter(filePath, word.toStringVector());
            }
            System.out.println("Работа с файлом " + file.getPath() + " завершена.");
        }
    }

    private void appendUsingFileWriter(String filePath, String text) {
        File file = new File(filePath);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file,true);
            fr.write(text);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
