package PreprocessingData;

import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tawt.jmorfsdk.loader.JMorfSdkFactory;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters;
import ru.textanalysis.tawt.sp.api.SyntaxParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GettingWordData {

    public JMorfSdk jMorfSdk = JMorfSdkFactory.loadFullLibrary();
    public static SyntaxParser sp = new SyntaxParser();

    private static class SingeltonGettingWordData {
        private final static GettingWordData instance = new GettingWordData();
    }

    public static GettingWordData getInstance () {
        sp.init();
        return SingeltonGettingWordData.instance;
    }

    public List<String> getListOfWords (String text) { // Получает необработанный список слов из предложения
        List<String> listWords = Arrays.asList(toLowerSentence(text)
                .split("[—–,.?!;:\"{}\\[\\]()<>@#№$%*-=*-+]+\\s|[\\s\\n]+"));
        return listWords;
    }

    public List<String> getListOfSentences (String text) { // Получает необработанный список предложений из текста
        List <String> listSentence = Arrays.asList(toLowerSentence(text).split("[.!?]+\\s"));
        return listSentence;
    }

    public List<List<String>> getListFromAllText (String text) { // Получает необработанный список слов из текста
        List<List<String>> listofWords = new ArrayList<List<String>>();
        getListOfSentences(text).forEach((sentence) -> {
            listofWords.add(getListOfWords(sentence));
        });
        return listofWords;
    }

    public HashSet<String> getListAllNoun (String text) {
        HashSet <String> listAllNoun = new HashSet <String>();
        for (String sentence: getListOfSentences(text)) {
            for (String word: getListOfWords(sentence)) {
                if (checkNoun(word)) {
                    listAllNoun.add(word);
                }
            }
        }
        return listAllNoun;
    }

    public String toLowerSentence (String text) { return text.toLowerCase(); } // Приводит текст к нижнему регистру

    public boolean checkNoun(String word){ // Проверяет, является ли слово существительным
        List<Byte> temp = jMorfSdk.getTypeOfSpeeches(word);
        if (
                (jMorfSdk.getTypeOfSpeeches(word).contains(Byte.parseByte("17"))&&
                        (!jMorfSdk.getTypeOfSpeeches(word).contains(Byte.parseByte("12"))))
        ) {
            return true;
        }
        else return false;
    }

    public String getInitFormOfAWord (String word) {
        try {
            return jMorfSdk.getAllCharacteristicsOfForm(word).get(0).getInitialFormString();
        }
        catch (Exception e) {
            System.out.println("Ошибка при получении начальной формы слова: " + word);
            return word;
        }
    }

    public boolean checkNumberOfAWord (String word) { // Возвращает true, если число единственное, в иных случаях false
        if (jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                getTheMorfCharacteristics(MorfologyParameters.Numbers.class) == MorfologyParameters.Numbers.SINGULAR) {
            return true;
        }
        else return false;
    }

    public boolean checkCaseOfAWord (String word) { // Возвращает true, если падеж именительный, в иных случаях false
        if (jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                getTheMorfCharacteristics(MorfologyParameters.Case.class) == MorfologyParameters.Case.NOMINATIVE) {
            return true;
        }
        else return false;
    }

    public int checkGenderOfAWord (String word) { // Возвращает 1, если род мужское, возвращает -1,
        // если род женский, и 0 в иных случаях
        if (jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                getTheMorfCharacteristics(MorfologyParameters.Gender.class) == MorfologyParameters.Gender.MANS) {
            return 1;
        }
        else if (jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                getTheMorfCharacteristics(MorfologyParameters.Gender.class) == MorfologyParameters.Gender.FEMININ) {
            return -1;
        }
        else return 0;
    }

    public int checkNameOfAWord (String word) { // Возвращает 1, если слово является именем, фамилией или отчеством,
        // возвращает -1, если аббревиатура, в иных случаях 0
        if (jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                getTheMorfCharacteristics(MorfologyParameters.Name.class) == MorfologyParameters.Name.NAME ||
                jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                        getTheMorfCharacteristics(MorfologyParameters.Name.class) == MorfologyParameters.Name.PART ||
                jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                        getTheMorfCharacteristics(MorfologyParameters.Name.class) == MorfologyParameters.Name.SURN){
            return 1;
        }
        else if (jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                getTheMorfCharacteristics(MorfologyParameters.Name.class) == MorfologyParameters.Name.ABBREVIATION) {
            return -1;
        }
        else return 0;
    }

    public int getAnimateOfAWord (String word) { // Возвращает true, если слово одушевлённое, false в иных случаях
        if (jMorfSdk.getAllCharacteristicsOfForm(word).get(0).
                getTheMorfCharacteristics(MorfologyParameters.Animacy.class) == MorfologyParameters.Animacy.ANIMATE) {
            return 1;
        }
        else return 0;
    }



}
