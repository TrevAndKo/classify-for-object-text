package ru.classificator.services;

import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import ru.classificator.entities.Text;
import ru.classificator.entities.Word;
import ru.textanalysis.tawt.ms.external.sp.BearingPhraseExt;
import ru.textanalysis.tawt.ms.external.sp.OmoFormExt;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PreprocessingOfText {

    public GettingWordData GettingWordData = ru.classificator.services.GettingWordData.getInstance();

    private static class SingeltonPreprocessingOfText {
        private final static PreprocessingOfText instance = new PreprocessingOfText();
    }

    public static PreprocessingOfText getInstance() { return SingeltonPreprocessingOfText.instance; }

    private void createXML(int numberOfFile, String word, String isName, String author, String title,
                           String typeOfSpeech, String gender, String animate, String frequency,
                           String mainWord, String dependentVerbs, String dependenceOfVerb, String dependentNoun,
                           String dependentAdjective, String dependenceOfNoun) {


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
            rootElement.appendChild(setWordChara(doc, "mainWord", mainWord));
            rootElement.appendChild(setWordChara(doc,"dependentVerbs", dependentVerbs));
            rootElement.appendChild(setWordChara(doc,"dependenceOfVerb", dependenceOfVerb));
            rootElement.appendChild(setWordChara(doc,"dependentNoun", dependentNoun));
            rootElement.appendChild(setWordChara(doc,"dependenceOfNoun", dependenceOfNoun));
            rootElement.appendChild(setWordChara(doc, "dependentAdjective", dependentAdjective));

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
            rootElement.appendChild(setWordChara(doc, "mainWord", Integer.toString(word.getMainWord())));
            rootElement.appendChild(setWordChara(doc,"dependentVerbs",
                    Integer.toString(word.getDependentVerbs())));
            rootElement.appendChild(setWordChara(doc,"dependenceOfVerb",
                    Integer.toString(word.getDependenceOfVerb())));
            rootElement.appendChild(setWordChara(doc,"dependentNoun", Integer.toString(word.getDependentNoun())));
            rootElement.appendChild(setWordChara(doc,"dependenceOfNoun",
                    Integer.toString(word.getDependenceOfNoun())));
            rootElement.appendChild(setWordChara(doc, "dependentAdjective",
                    Integer.toString(word.getDependentAdjective())));

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

        Text text = new Text();

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


    public void recursionProcessing (String checkWord, List <OmoFormExt> listDataAboutPoint, HashSet <Word> listOfNouns, String flag) {
        for (OmoFormExt word: listDataAboutPoint) {
            if (flag.equals(word.getCurrencyOmoForm().getInitialFormString())) {
                continue;
            } else {
                if (GettingWordData.getInitFormOfAWord(checkWord).equals(word.getCurrencyOmoForm().getInitialFormString()) && (
                        GettingWordData.checkNoun(checkWord))) {
                    Word temp = new Word(GettingWordData.getInitFormOfAWord(checkWord));
                    countDependsFromWord(word, temp);
                    countDependsWord(word, temp);
                    temp.setFrequency(1);
                    if (listOfNouns.contains(temp)) {
                        updateWordInList(temp, listOfNouns);
                    } else {
                        listOfNouns.add(temp);
                    }
                    flag = temp.getWord();
                    break;
                }
                else {
                    recursionProcessing(checkWord, word.getDependentWords(), listOfNouns, flag);
                }
            }


        }
    }


    public HashSet <Word> processOfWords (String text) {
        long startSin = System.nanoTime();

        List <String> listOfSentence = GettingWordData.getListOfSentences(text);

        HashSet <Word> listOfNouns = new HashSet<>();

        for (String sentence: listOfSentence) {
            List<BearingPhraseExt> treeSentence = GettingWordData.sp.getTreeSentenceWithoutAmbiguity(sentence);
            for (String checkWord: GettingWordData.getListOfWords(sentence)) {
                String flag = "";
                try {
                    for (BearingPhraseExt mainWord: treeSentence) {
                        if (flag.equals(mainWord.getMainOmoForms().get(0)
                                .getCurrencyOmoForm().getInitialFormString())) {
                            continue;
                        } else {
                            if (GettingWordData.getInitFormOfAWord(checkWord).equals(mainWord.getMainOmoForms().get(0)
                                    .getCurrencyOmoForm().getInitialFormString()) &&
                                    (GettingWordData.checkNoun(checkWord))) {

                                Word temp = new Word(GettingWordData.getInitFormOfAWord(checkWord));
                                temp.setMainWord(1);
                                for (OmoFormExt word: mainWord.getMainOmoForms()) {
                                    countDependsFromWord(word, temp);
                                    countDependsWord(word, temp);
                                }
                                temp.setFrequency(1);
                                if (listOfNouns.contains(temp)) {
                                    updateWordInList(temp, listOfNouns);
                                } else {
                                    listOfNouns.add(temp);
                                }

                                flag = checkWord;
                            }
                            else {
                                for (OmoFormExt word: mainWord.getMainOmoForms()) {
                                    recursionProcessing(checkWord, word.getDependentWords(), listOfNouns, flag);
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                    System.out.println("Ошибка при работе со словом: " + checkWord);
                    continue;
                }
            }

        }

        long finish = System.nanoTime();
        long elapsed = finish - startSin;
        System.out.println("Время на семантико-синтаксический анализ, сек: " + elapsed/1000000000);
        long startMorf = System.nanoTime();
        for (Word word: listOfNouns) {
            word.setIsName(GettingWordData.checkNameOfAWord(word.getWord()));
            word.setGender(GettingWordData.checkGenderOfAWord(word.getWord()));
            word.setAnimate(GettingWordData.getAnimateOfAWord(word.getWord()));
          //  word.setFrequency(getFrequency(word.getWord(), text));
        }
        finish = System.nanoTime();
        elapsed = finish - startMorf;
        System.out.println("Время на морфологический анализ, сек: " + elapsed/1000000000);
        elapsed = finish - startSin;
        System.out.println("Время на полный анализ, сек: " + elapsed/1000000000);

        return listOfNouns;
    }

    public void updateWordInList (Word noun, HashSet <Word> listOfNouns) {
        for (Word word: listOfNouns) {
            if (word.equals(noun)) {
                word.updateWord(noun);
            }
        }
    }

    public void processOfText (String directory) {
        File dir = new File(directory); //path указывает на директорию
        int count = 0;
        for ( File file : dir.listFiles() ){
            if (file.isFile()) {

                Text text = readXMLText(file.getPath());
                for (Word word: processOfWords(text.getText())) {
                    try {
                        word.setAuthor(text.getAuthor());
                        word.setTitle(text.getTitle());
                        word.setTypeOfSpeech("NOUN");
                        createXML(count++, word);
                    } catch (Exception e) {
                        System.out.println("Ошибка при работе со словом: " + word.getWord());
                        continue;
                    }

                }
            }
            System.out.println("Работа с файлом " + file.getPath() + " завершена.");
        }
    }

    private Text getText(Node node) {
        Text text = new Text();
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
            word.setMainWord(Integer.parseInt(getTagValue("mainWord", element)));
            word.setDependentVerbs(Integer.parseInt(getTagValue("dependentVerbs", element)));
            word.setDependenceOfVerb(Integer.parseInt(getTagValue("dependenceOfVerb", element)));
            word.setDependentNoun(Integer.parseInt(getTagValue("dependentNoun", element)));
            word.setDependentAdjective(Integer.parseInt(getTagValue("dependentAdjective", element)));
            word.setDependenceOfNoun(Integer.parseInt(getTagValue("dependenceOfNoun", element)));
        }

        return word;
    }

    private String getTagValue(String tag, Element element) {

        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();

        for(int index = 0; index < nodeList.getLength(); index++){
            if (nodeList.item(index) instanceof CharacterData){
                CharacterData child  = (CharacterData) nodeList.item(index);
                String data = child.getData();

                if(data != null && data.trim().length() > 0)
                    return child.getData();
            }
        }
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    // 17 - сущ. 18, 19 - прил. 20, 21 - гл.

    public void countDependsFromWord(OmoFormExt word, Word noun) {

            if (word.getCurrencyOmoForm().getInitialFormString().equals(noun.getWord())) {
                String flag = "";
                for (OmoFormExt t : word.getDependentWords()) {
                    if (flag.equals(t.getCurrencyOmoForm().getInitialFormString())) {
                        continue;
                    } else {
                        if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                getInitialFormString()).contains(Byte.parseByte("20")) ||
                                GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                        getInitialFormString()).contains(Byte.parseByte("21"))||
                                GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                        getInitialFormString()).contains(Byte.parseByte("1"))) {
                            noun.setDependentVerbs(noun.getDependentVerbs() + 1);
                            flag = noun.getWord();
                            continue;
                        }

                        if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                getInitialFormString()).contains(Byte.parseByte("17"))) {
                            noun.setDependentNoun(noun.getDependentNoun() + 1);
                            flag = noun.getWord();
                            continue;
                        }

                        if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                getInitialFormString()).contains(Byte.parseByte("18")) ||
                                GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                        getInitialFormString()).contains(Byte.parseByte("19"))) {
                            noun.setDependentAdjective(noun.getDependentAdjective() + 1);
                            flag = noun.getWord();
                            continue;
                        }
                    }

            }
        }

    }

    public void countDependsWord (OmoFormExt word, Word tempWord) {

            for (OmoFormExt t: word.getDependentWords()) {

                if (GettingWordData.checkNoun(t.getCurrencyOmoForm().getInitialFormString())) {
                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                            getInitialFormString()).contains(Byte.parseByte("20")) ||
                            GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                    getInitialFormString()).contains(Byte.parseByte("21"))||
                            GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                    getInitialFormString()).contains(Byte.parseByte("1"))) {
                        tempWord.setDependenceOfVerb(tempWord.getDependenceOfVerb() + 1);
                    }

                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                            getInitialFormString()).contains(Byte.parseByte("17"))) {
                        tempWord.setDependenceOfNoun(tempWord.getDependenceOfNoun() + 1);
                    }

                }
        }

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

    public void preparingTrainingSample (String path, String nameFile, int size) {

        List <String> trainPerson = new ArrayList<>();
        List <String> trainObject = new ArrayList<>();
        List <String> trainSomething = new ArrayList<>();

        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = "Старт считывания";

            while (line != null) {

                if ((line.indexOf("person") != -1) && (trainPerson.size() <= size)) {
                    trainPerson.add(line);
                }

                if ((line.indexOf("object") != -1) && (trainObject.size() <= size)) {
                    trainObject.add(line);
                }

                if ((line.indexOf("something") != -1) && (trainSomething.size() <= size)) {
                    trainSomething.add(line);
                }

                if ((trainPerson.size() == size) && (trainObject.size() == size) && (trainSomething.size() == size)) {
                    break;
                }

                line = reader.readLine();
            }

            PrintWriter writer = new PrintWriter(nameFile, "UTF-8");
            for (String word : trainPerson) {
                writer.println(word);
            }

            for (String word : trainObject) {
                writer.println(word);
            }

            for (String word : trainSomething) {
                writer.println(word);
            }

            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}