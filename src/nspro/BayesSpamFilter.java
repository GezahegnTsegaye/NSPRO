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
    public int spamFeedings = 0;
    public int hamFeedings = 0;
    
    private final static double equalProbability = 0.05;  //to consider not to say every messages is not spam so we have to star like 50%
    private double spamThreshhold = 0.5;

    private void fillMap(Map<String, Double> map, Map<String, Double> otherMap) {

        for (String word : otherMap.keySet()) {
            if (!map.containsKey(word)) {
                map.put(word, equalProbability);
            }
        }
    }
    
    /**
     * this method checks dictionary word probability occurrence
     * in the spam training datasets
     * @param content
     * @return 
     */

    public double isSpamProbability(String content) {
        String[] words = content.split("\\s+");

        double result = 1d;

        for (String word : words) {
            if (spam.containsKey(word)) {
                result *= ham.get(word) / spam.get(word);
            }
        }

        return 1d / (1d + result);
    }
    
    
    public double getSpamThreshhold() {
        return spamThreshhold;
    }

    public void setSpamThreshhold(double spamThreshhold) {
        this.spamThreshhold = spamThreshhold;
    }
    
    /**
     * check spam probability occurrence 
     * from the spam threshold value we set in the above
     * 
     * @param content
     * @return 
     */

    public boolean isSpam(String content) {
        return isSpamProbability(content) > spamThreshhold;
    }


    private void feed(String content, boolean isSpam) {
        Map<String, Double> map = isSpam ? spam : ham;

        for (String word : content.split("\\s+")) {
            Double val = map.containsKey(word) ? map.get(word) : 0.;
            map.put(word, ++val);
        }
    }

    private void normalize(Map<String, Double> map, int size) {
        if (size == 0) {
            size = 1;
        }

        for (String word : map.keySet()) {
            map.put(word, map.get(word) / size);
        }
    }
    
    /**
     * check if it is spam or not 
     * @param trainingSpam
     * @param trainingHam 
     */

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
