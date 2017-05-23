/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nspro;

import java.util.HashMap;
import java.util.Map;

/**
 * This class filter the spam based bayes algorithm
 *
 * @author Gezahegn
 */
public class BayesSpamFilter {

    Map<String, Double> spam;
    Map<String, Double> ham;
    int spamFeedings = 0;
    int hamFeedings = 0;
    private final static double epsilon = 0.05;
    private double spamThreshhold = 0.5;

    private void feed(String content, boolean isSpam) {
        Map<String, Double> map = isSpam ? spam : ham;

        for (String word : content.split("\\s+")) {
            Double val = map.containsKey(word) ? map.get(word) : 0.;
            map.put(word, ++val);
        }
    }

    /**
     * make the listed word sequential
     *
     * @param map
     * @param size
     */
    private void normalize(Map<String, Double> map, int size) {
        if (size == 0) {
            size = 1;
        }
        for (String word : map.keySet()) {
            map.put(word, map.get(word) / size);
        }
    }

    private void fillMap(Map<String, Double> map, Map<String, Double> otherMap) {

        for (String word : otherMap.keySet()) {
            if (!map.containsKey(word)) {
                map.put(word, epsilon);
            }
        }
    }

    public double isSpamProbability(String content) {
        //the map will contain all the words that were found in the message with their frequencies. 
        String[] words = content.split("\\s+");
         //String[] words1 = content.split("\\s+");
        double result = 1d;

        for (String word : words) {
            if (spam.containsKey(word)) {
                result *= ham.get(word) / spam.get(word);
            }
        }

        return 1d / (1d + result);
    }

    public boolean isSpam(String content) {
        return isSpamProbability(content) > spamThreshhold;
    }

    public double getSpamThreshhold() {
        return spamThreshhold;
    }

    public void setSpamThreshhold(double spamThreshhold) {
        this.spamThreshhold = spamThreshhold;
    }

    public BayesSpamFilter(String[] trainingSpam, String[] trainingHam) {
        spam = new HashMap<>();
        ham = new HashMap<>();

        for (String spam : trainingSpam) {
            feed(spam, true);
        }

        for (String ham : trainingHam) {
            feed(ham, false);
        }

        spamFeedings = trainingSpam.length;
        hamFeedings = trainingHam.length;

        int min = Math.min(spamFeedings, hamFeedings);

        if (min != 0) {
            spamFeedings /= min;
            hamFeedings /= min;
        }

        fillMap(spam, ham);
        fillMap(ham, spam);

        normalize(spam, spamFeedings);
        normalize(ham, hamFeedings);
    }
    

}
