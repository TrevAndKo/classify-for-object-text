package ru.classificator.preprocessingdata;

public class PreparingDataForTraining {
    public static void main(String[] args) {

        PreprocessingOfText PreprocessingOfText = new PreprocessingOfText();
        PreprocessingOfText.processOfText("Список текстов/");
        PreprocessingOfText.updateFileData("Список слов/");
    }
}