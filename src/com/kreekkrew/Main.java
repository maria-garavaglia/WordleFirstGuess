package com.kreekkrew;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final Path solutionsFilename = Path.of(System.getProperty("user.dir"), "solutions.txt");
    private static final Path allFilename = Path.of(System.getProperty("user.dir"), "allwords.txt");
    private static final int wordLength = 5;

    private static List<String> solutionList = new ArrayList<>();
    private static List<String> allwordsList = new ArrayList<>();

    public static void main(String[] args)
    {
//        int test = getScore("props", "proxy");
//        System.out.println("Score: " + test);

        buildWordLists();

        List<WordScore> scores = new ArrayList<>();
        final var numSolutions = solutionList.size();

        System.out.println("Calculating Scores...");
        for(var guess : allwordsList)
        {
            var scoreSum = 0;
            for(var solution : solutionList)
            {
                scoreSum += getScore(guess, solution);
            }
            double avgScore = (double)scoreSum / numSolutions;

            scores.add(new WordScore(guess, avgScore));
        }

        System.out.println("Sorting by average score...");
        scores.sort((w1, w2) -> Double.compare(w2.getScore(), w1.getScore()));

        System.out.println("Finished!\n");
        System.out.println("Top 10 Scores:");
        for(int index = 0; index < Math.min(scores.size(), 10); index++)
        {
            var score = scores.get(index);
            System.out.println("#" + (index + 1) + ": " + score.getWord() + " (" + score.getScore() + ")");
        }
    }

    public static void buildWordLists()
    {
        try(Stream<String> lines = Files.lines(solutionsFilename))
        {
            solutionList = lines.collect(Collectors.toList());
            allwordsList.addAll(solutionList);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        try(Stream<String> lines = Files.lines(allFilename))
        {
            allwordsList.addAll(lines.collect(Collectors.toList()));
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        System.out.println("Found " + allwordsList.size() + " words with " + solutionList.size() + " solutions.");
    }

    private static int getScore(String guess, String solution)
    {
        if(guess.length() != wordLength || solution.length() != wordLength)
        {
            System.err.println("Invalid word length: " + guess + "/" + solution);
            return 0;
        }

        List<Character> guessChars = guess.toLowerCase()
            .chars()
            .mapToObj(e -> (char)e)
            .collect(Collectors.toList());
        List<Character> solChars = solution.toLowerCase()
            .chars()
            .mapToObj(e -> (char)e)
            .collect(Collectors.toList());

        List<Character> log = new ArrayList<>();

        var scoreTotal = 0;

        for(int index = 0; index < wordLength; index++)
        {
            var guessLetter = guessChars.get(index);
            var solLetter = solChars.get(index);
            // Green: 3 pts
            if(solLetter != null && solLetter.equals(guessLetter))
            {
                scoreTotal += 3;
                log.add(solLetter);
                solChars.set(index, null);
            }
            // Yellow: 2 pts
            else if(solChars.contains(guessLetter))
            {
                scoreTotal += 2;
                var location = solChars.indexOf(guessLetter);
                log.add(solChars.get(location));
                solChars.set(location, null);
            }
            else if(!log.contains(guessLetter))
            {
                scoreTotal += 1;
            }
        }

        return scoreTotal;
    }
}
