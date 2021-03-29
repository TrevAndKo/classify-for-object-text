package ru.classificator.preprocessingdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class InTeM {

    private ArrayList<WordWithStats> innerWordList;

    public ArrayList<WordWithStats> getInnerWordList() {
        return innerWordList;
    }

    public void setInnerWordList(ArrayList<WordWithStats> innerWordList) {
        this.innerWordList = innerWordList;
    }
    private ArrayList<WordLengths> forFWeight;
    private HashSet<FriqCount> forQWeight = new HashSet<FriqCount>();
    private long totalNumberOfWords = 0;

    public InTeM() {
        innerWordList = new ArrayList<WordWithStats>();
    }
    public InTeM(ArrayList<WordWithStats> list) {
        this.innerWordList = list;
        totalNumberOfWords = this.countWords();
    }
    public int addWord(WordWithStats word)
    {
        int index = innerWordList.indexOf(word);
        WordWithStats tempWord = new WordWithStats();
        if (index >=0) {
            tempWord = innerWordList.get(index);
            tempWord.setCount(tempWord.getCount() + word.getCount());
            innerWordList.set(index, tempWord);
        } else {
            innerWordList.add(word);
        }
        totalNumberOfWords = this.countWords();
        return 0;
    }

    public void calculateIntem(){
        for (WordWithStats word : this.innerWordList){
            word.setIntem(word.getQWeight() - word.getFWeight());
        }
        Collections.sort(this.innerWordList);
    }


    public void calculateQWeight(){

        this.fillForQWeightList();

        for (WordWithStats word : this.innerWordList){
            for (FriqCount item : this.forQWeight){
                if (item.getFriq() == word.getCount()){
                    word.setQWeight(item.getqWeight());
                }
            }
        }
    }
    //Filter for Nouns
    public void filterByPartOfSpeach(int typeOfPart) {
        //1 - noun
        int i = 0;
        while (i < this.innerWordList.size()){
            if (this.innerWordList.get(i).getPartOfSpeach() != typeOfPart){
                this.innerWordList.remove(i);
            }
            else {
                i++;
            }
        }
    }
    public void filterByPartOfSpeach(ArrayList<Integer> typesOfPart) {
        //1 - noun
        int i = 0;
        boolean flag = false;
        while (i < this.innerWordList.size()){
            flag = false;
            for (Integer j : typesOfPart) {
                if (this.innerWordList.get(i).getPartOfSpeach() == j){
                    flag = true;
                }
            }
            if (flag == false) {
                this.innerWordList.remove(i);
            }
            else {
                i++;
            }
        }
    }

    //Q-weight РѕРЅ Р¶Рµ С„СѓРЅРєС†РёРѕРЅР°Р»СЊРЅС‹Р№ РІРµСЃ, СЂР°СЃСЃС‡РёС‚С‹РІР°РµРјС‹Р№ РїРѕ РґР»РёРЅРµ СЃР»РѕРІР°.
    public void calculateFWeight()
    {
        this.forFWeight = fillForFWeightList();

        //РїРѕРґСЃС‡РµС‚ РЅР°РєРѕРїР»РµРЅРЅС‹С…
        long tempSum = 0;
        WordLengths tempWord = new WordLengths();
        for (int i=0; i < this.forFWeight.size(); i++) {
            tempWord = forFWeight.get(i);
            tempSum += tempWord.getNumberOfWords();
            tempWord.setTotalCurSum(tempSum);
            forFWeight.set(i, tempWord);
        }

        long totalNumberOfWords = tempSum;

        //РџРѕРґСЃС‡РµС‚ F РІРµСЃРѕРІ
        for (int i=0; i< this.forFWeight.size(); i++) {
            tempWord = forFWeight.get(i);
            if (tempWord.getNumberOfWords() == 0) continue;
            tempWord.setFweight((tempSum - tempWord.getTotalCurSum()) * 1.0 / tempSum);
            forFWeight.set(i, tempWord);
        }

        //Р·Р°РїРёСЃР°С‚СЊ F-РІРµСЃ РЅР°Р·Р°Рґ РІ РђrrayList РІ СЃР»РѕРІРЅРёРє.
        WordWithStats word = new WordWithStats();
        for (int i=0; i<this.innerWordList.size();i++) {
            word = this.innerWordList.get(i);
            word.setFWeight(this.forFWeight.get(word.getWord().length()).getFWeight());
            this.innerWordList.set(i, word);
        }
    }

    private long countWords() {
        long count = 0;
        for (WordWithStats word : this.innerWordList) {
            count += word.getCount();
        }
        return count;
    }

    private ArrayList<WordLengths> fillForFWeightList() {
        ArrayList<WordLengths> retList = new ArrayList<WordLengths>();
        for (int i = 0; i < 100; i++)
        {
            retList.add(new WordLengths());
        }
        for (WordWithStats word : this.innerWordList){
            int wordsCount = retList.get(word.getWord().length()).getNumberOfWords();
            if (wordsCount == 0) {
                retList.set(word.getWord().length(), new WordLengths(1));
            } else {
                retList.set(word.getWord().length(),
                        new WordLengths(wordsCount + 1));
            }

        }
        return retList;
    }

    /**
     * Fill temp table for Q-weight calculate
     */
    private void fillForQWeightList() {
        for (WordWithStats word : this.innerWordList){
            FriqCount item = new FriqCount();
            item.setFriq(word.getCount());
            this.forQWeight.add(item);
        }

        for (FriqCount item : this.forQWeight){
            int cnt = 0;
            for (WordWithStats word : this.innerWordList){
                if (word.getCount() == item.getFriq()){
                    cnt += 1;//word.getCount();
                }
            }
            item.setFriqCount(cnt);
        }

        for (FriqCount item1 : this.forQWeight){
            int cnt = 0;
            for (FriqCount item2 : this.forQWeight){
                if (item2.getFriq() >= item1.getFriq()){
                    cnt += item2.getFriqCount();
                }
            }
            item1.setTotalFriqSum(cnt);
            item1.setqWeight((this.getInnerWordList().size() - cnt) * 1.0 / this.getInnerWordList().size());
        }
    }

    public ArrayList<WordWithStats> getMarkems(int count){
        ArrayList<WordWithStats> markemsList = new ArrayList<WordWithStats>();
        for (WordWithStats word : this.innerWordList){
            //if (word.getPartOfSpeach() == 1) {
            markemsList.add(word);
            count--;
            //}
            if (count <= 0) break;
        }
        return markemsList;
    }

    public ArrayList<WordWithStats> getMarkems(double value){
        ArrayList<WordWithStats> markemsList = new ArrayList<WordWithStats>();
        for (WordWithStats word : this.innerWordList){
            if (word.getIntem() >= value){
                markemsList.add(word);
            }
            else {
                break;
            }
        }
        return markemsList;
    }

    public double getIntem (String word) {
        double intem = 0.0;
        for (WordWithStats wordWithStats : this.innerWordList) {
            if (wordWithStats.getWord().equals(word)) {
                intem = wordWithStats.getIntem();
                break;
            }
        }
        return intem;
    }

////////////////////////////////////////////////////


    private class WordLengths {

        //private byte length;
        private int numberOfWords;
        private long totalCurSum;
        private double FWeight;

        public WordLengths() {
        }
        public WordLengths(int _numberOfWords) {
            this.numberOfWords = _numberOfWords;
        }
        public int getNumberOfWords(){
            return this.numberOfWords;
        }
        public void setNumberOfWords (int  _count){
            this.numberOfWords = _count;
        }
        public long getTotalCurSum(){
            return this.totalCurSum;
        }
        public void setTotalCurSum (long _count){
            this.totalCurSum = _count;
        }
        public double getFWeight(){
            return this.FWeight;
        }
        public void setFweight (double _count){
            this.FWeight = _count;
        }

    }

    private class FriqCount {

        private int friq;
        private int friqCount;
        private long totalFriqSum;
        private double qWeight;

        public FriqCount() {
        }

        public int getFriq() {
            return friq;
        }

        public void setFriq(int friq) {
            this.friq = friq;
        }

        public int getFriqCount() {
            return friqCount;
        }

        public void setFriqCount(int friqCount) {
            this.friqCount = friqCount;
        }

        public long getTotalFriqSum() {
            return totalFriqSum;
        }

        public void setTotalFriqSum(long totalFriqSum) {
            this.totalFriqSum = totalFriqSum;
        }

        public double getqWeight() {
            return qWeight;
        }

        public void setqWeight(double qWeight) {
            this.qWeight = qWeight;
        }

        @Override
        public boolean equals(Object obj){
            return this.getFriq() == ((FriqCount) obj).getFriq();
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 13 * hash + this.friq;
            return hash;
        }


    }

}
