package com.kreekkrew;

public class WordScore
{
    private String word;
    private double avgScore;

    public WordScore(String word, double avgScore)
    {
        this.word = word;
        this.avgScore = avgScore;
    }

    public String getWord(){return word;}
    public double getScore(){return avgScore;}
}
