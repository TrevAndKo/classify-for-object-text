package ru.classificator.preprocessingdata;

public class WordWithStats implements Comparable<WordWithStats>{

    private String word;
    private int partOfSpeach;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartOfSpeach() {
        return partOfSpeach;
    }

    public void setPartOfSpeach(int partOfSpeach) {
        this.partOfSpeach = partOfSpeach;
    }
    private int count;
    private double relFrequency;

    private double fWeight;
    private double qWeight;
    private double intem;

    public WordWithStats(){

    }

    public double getIntem() {
        return intem;
    }

    public void setIntem(double intem) {
        this.intem = intem;
    }
    public WordWithStats(String _word, int _part, int _count, double _relFrq, int _id ){
        this.word = _word;
        this.partOfSpeach = _part;
        this.count = _count;
        this.relFrequency = _relFrq;
        this.id = _id;
    }

    public WordWithStats(String _word, int _part, int _count, double _relFrq ){
        this(_word,  _part,  _count,  _relFrq,0);
    }


    //set get for all
    public String getWord(){
        return this.word;
    }
    public void setWord (String _word){
        this.word = _word;
    }
    public int getCount(){
        return this.count;
    }
    public void setCount (int  _count){
        this.count = _count;
    }
    public double getRelFreq(){
        return this.relFrequency;
    }
    public void setRelFreq (double _freq){
        this.relFrequency = _freq;
    }
    public double getFWeight(){
        return this.fWeight;
    }
    public void setFWeight (double _freq){
        this.fWeight = _freq;
    }
    public double getQWeight(){
        return this.qWeight;
    }
    public void setQWeight (double _freq){
        this.qWeight = _freq;
    }
    @Override
    public boolean equals(Object other){

        if (this == other) return true;
        if (other == null) return false;
        if(this.getClass() != other.getClass()) return false;
        return this.word.equals(((WordWithStats)other).getWord());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = this.word.hashCode();
        return hash;
    }

    @Override
    public int compareTo(WordWithStats o) {
        int res = 0;
        if (this.getIntem() < o.getIntem()){
            res = 1;
        }
        else if (this.getIntem() == o.getIntem()){
            res = 0;
        }
        else{
            res = -1;
        }
        return res;
    }

}
