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
import java.io.*;
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
                           String dependentAdjective, String dependenceOfNoun, String intem) {


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
            rootElement.appendChild(setWordChara(doc, "InTeM", intem));


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
            rootElement.appendChild(setWordChara(doc,"InTeM", Double.toString(word.getIntem())));


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


    private int processOfWords (String path, int count, InTeM InTeM) {

        List <String> listOfSentence = GettingWordData.getListOfSentences(readXMLText(path).getText());
        HashSet <Word> listOfNouns = new HashSet<>();

        for (String sentence: listOfSentence) {
            List<BearingPhraseExt> treeSentence = GettingWordData.sp.getTreeSentenceWithoutAmbiguity(sentence);
            for (BearingPhraseExt word: treeSentence) {
                String tempWord = word.getMainOmoForms().get(0).getCurrencyOmoForm().getInitialFormString();
                if (GettingWordData.checkNoun(tempWord)) {
                    Word noun = new Word(word.getMainOmoForms().get(0).
                            getCurrencyOmoForm().getInitialFormString());
                    if (getDataAboutMain(word)) {
                        noun.setMainWord(1);
                    }
                    countDependsFromWord(word, noun);
                    countDependsWord(word, noun);

                    if (listOfNouns.contains(noun)) {
                        updateWordInList(noun, listOfNouns);
                    }

                    else {
                        noun.setIsName(GettingWordData.checkNameOfAWord(tempWord));
                        noun.setGender(GettingWordData.checkGenderOfAWord(tempWord));
                        noun.setAnimate(GettingWordData.getAnimateOfAWord(tempWord));
                        noun.setFrequency(getFrequency(tempWord, sentence));
                        noun.setIntem(InTeM.getIntem(tempWord));
                        listOfNouns.add(noun);
                    }

                }
            }
        }
        return count; // счётчик файлов
    }

    public void updateWordInList (Word noun, HashSet <Word> listOfNouns) {
        for (Word word: listOfNouns) {
            if (word.getWord().equals(noun)) {
                word.updateWord(noun);
            }
        }
    }

    public void processOfText (String directory) {
        File dir = new File(directory); //path указывает на директорию
        int count = 76;
        for ( File file : dir.listFiles() ){
            if (file.isFile()) {

                InTeM InTeM = createIntem(readXMLText(file.getPath()).getText());

                count = processOfWords(file.getPath(), count, InTeM);
            }
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
            word.setMainWord(Integer.parseInt(getTagValue("mainWord", element)));
            word.setDependentVerbs(Integer.parseInt(getTagValue("dependentVerbs", element)));
            word.setDependenceOfVerb(Integer.parseInt(getTagValue("dependenceOfVerb", element)));
            word.setDependentNoun(Integer.parseInt(getTagValue("dependentNoun", element)));
            word.setDependentAdjective(Integer.parseInt(getTagValue("dependentAdjective", element)));
            word.setDependenceOfNoun(Integer.parseInt(getTagValue("dependenceOfNoun", element)));
            word.setIntem(Double.parseDouble(getTagValue("InTeM", element)));
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

    public int getFrequency (String wordToCheck, String sentence) {
        int count = 0;
        for (String word: GettingWordData.getListOfWords(sentence)) {
            if (word.toLowerCase().equals(wordToCheck.toLowerCase())) { count++; }
        }
        return count;
    }


    public boolean getDataAboutMain (BearingPhraseExt word) {
            if (word.getMainOmoForms().isEmpty()) { return false; }
            else { return true; }
    }


    // 17 - сущ. 18, 19 - прил. 20, 21 - гл.


    public void countDependsFromWord(BearingPhraseExt word, Word noun) {
        for (OmoFormExt w : word.getMainOmoForms()) {
            if (w.getCurrencyOmoForm().getInitialFormString().equals(word)) {
                for (OmoFormExt t : w.getDependentWords()) {
                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                            getInitialFormString()).contains(Byte.parseByte("20")) ||
                            GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                    getInitialFormString()).contains(Byte.parseByte("21"))) {
                        noun.setDependentVerbs(noun.getDependentVerbs() + 1);
                    }

                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                            getInitialFormString()).contains(Byte.parseByte("18")) ||
                            GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                    getInitialFormString()).contains(Byte.parseByte("19"))) {
                        noun.setDependentNoun(noun.getDependentNoun() + 1);
                    }

                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                            getInitialFormString()).contains(Byte.parseByte("18")) ||
                            GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                    getInitialFormString()).contains(Byte.parseByte("19"))) {
                        noun.setDependentAdjective(noun.getDependentAdjective() + 1);
                    }
                }
            }
        }

    }

    public void countDependsWord (BearingPhraseExt word, Word noun) {

        for (OmoFormExt w : word.getMainOmoForms()) {
            for (OmoFormExt t: w.getDependentWords()) {
                if (t.getCurrencyOmoForm().getInitialFormString().equals(word)) {
                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                            getInitialFormString()).contains(Byte.parseByte("20")) ||
                            GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                    getInitialFormString()).contains(Byte.parseByte("21"))) {
                        noun.setDependenceOfVerb(noun.getDependenceOfVerb() + 1);
                    }

                    if (GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                            getInitialFormString()).contains(Byte.parseByte("18")) ||
                            GettingWordData.jMorfSdk.getTypeOfSpeeches(t.getCurrencyOmoForm().
                                    getInitialFormString()).contains(Byte.parseByte("19"))) {
                        noun.setDependenceOfNoun(noun.getDependenceOfNoun() + 1);
                    }
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

    public InTeM createIntem (String text) {
        InTeM InTeM = new InTeM();

        List<String> listAllWords = GettingWordData.getListOfWords(text);
        HashSet<String> listAllNoun = GettingWordData.getListAllNoun(text);

        HashMap <String, Integer> tableOfNoun = new HashMap<>();
        for (String noun : listAllNoun) {
            tableOfNoun.put(noun, 0);
        }

        int id = 0;
        for (String wordFromText : listAllWords) {
            String word = GettingWordData.getInitFormOfAWord(wordFromText);

            if (tableOfNoun.containsKey(GettingWordData.getInitFormOfAWord(word))) {
                int count = tableOfNoun.get(GettingWordData.getInitFormOfAWord(word)) + 1;
                tableOfNoun.remove(GettingWordData.getInitFormOfAWord(word));
                tableOfNoun.put(word, count);
            }

        }

        for (HashMap.Entry<String, Integer> word : tableOfNoun.entrySet()) {
            InTeM.addWord(new WordWithStats(word.getKey(), 1, word.getValue(), getFrequency(word.getKey(), text),
                    id++));
        }
        InTeM.calculateFWeight();
        InTeM.calculateQWeight();
        InTeM.calculateIntem();

        return InTeM;
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