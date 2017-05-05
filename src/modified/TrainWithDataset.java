/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modified;

import nspro.BayesSpamFilter;
import nspro.Performance;

/**
 *
 * @author Gezahegn
 */
public class TrainWithDataset {

    private BayesSpamFilter spamfilter;
    private String[] validationSpam;
    private String[] validationHam;

    public TrainWithDataset(BayesSpamFilter spamfilter, String[] validationSpam, String[] validationHam) {
        this.spamfilter = spamfilter;
        this.validationSpam = validationSpam;
        this.validationHam = validationHam;
    }
    
    /**
     * calculate the best threshold values for spam validation 
     * and ham validation
     * @param spam
     * @param ham
     * @param spamThreshold
     * @return 
     */

    public Performance calculatePerformance(String[] spam, String[] ham, double spamThreshold) {
        double prevThreshold = spamfilter.getSpamThreshhold();
        spamfilter.setSpamThreshhold(spamThreshold);

        Performance per = new Performance();
        int tp = 0;
        int fp = 0;
        int fn = 0;

        for (String s : spam) {
            if (spamfilter.isSpam(s)) {
                tp++;
            } else {
                fn++;
            }
        }

        for (String h : ham) {
            if (spamfilter.isSpam(h)) {
                fp++;
            }
        }
        per.setPrecision((double) tp / (tp + fp));
        per.setRecall((double) tp / (tp + fn));

        spamfilter.setSpamThreshhold(prevThreshold);
        return per;
    }

    /**
     *
     * @param testSpam
     * @param testHam
     * @param iterations
     * @return
     */
    public Performance train(String[] testSpam, String[] testHam, int iterations) {
        iterations++;

        Performance max = null;
        double bestThreshold = 0;
        for (double i = 1; i < iterations; i++) {
            Performance perf = calculatePerformance(validationSpam, validationHam, i/iterations);
            
            if(max == null || max.getF1Score() < perf.getF1Score()){
                max = perf;
                bestThreshold = i/iterations;
                
            }
        }
        
        getSpamFilter().setSpamThreshhold(bestThreshold);
        
        return calculatePerformance(testHam, testHam, bestThreshold);
    }
    
    

   private BayesSpamFilter getSpamFilter(){
       return spamfilter;
   }

}
