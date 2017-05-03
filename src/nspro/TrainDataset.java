/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nspro;

/**
 *
 * @author Gezahegn
 */
public class TrainDataset {
    
    
    private BayesSpamFilter bayesSpamFilter;
    private String[] validateSpam;
    private String[] validateHam;

    public TrainDataset(BayesSpamFilter bayesSpamFilter, String[] validateSpam, String[] validateHam) {
        this.bayesSpamFilter = bayesSpamFilter;
        this.validateSpam = validateSpam;
        this.validateHam = validateHam;
    }

    public BayesSpamFilter getBayesSpamFilter() {
        return bayesSpamFilter;
    }

    public void setBayesSpamFilter(BayesSpamFilter bayesSpamFilter) {
        this.bayesSpamFilter = bayesSpamFilter;
    }

    public String[] getValidateSpam() {
        return validateSpam;
    }

    public void setValidateSpam(String[] validateSpam) {
        this.validateSpam = validateSpam;
    }

    
    
    
    public Performance performanceCalc(String[] spam, String[] ham, double spamThreshold) {
        double prevThreshold = bayesSpamFilter.getSpamThreshhold();
        bayesSpamFilter.setSpamThreshhold(spamThreshold);  
        
        
        
        Performance per = new Performance();
        int tp = 0;
        int fp = 0;
        int fn = 0;

        for (String s : spam) {
            if (bayesSpamFilter.isSpam(s)) {
                tp++;
            } else {
                fn++;
            }
        }

        for (String h : ham) {
            if (bayesSpamFilter.isSpam(h)) {
                fp++;
            }
        }
        per.setPrecision((double) tp / (tp + fp));
        per.setRecall((double) tp / (tp + fn));

        bayesSpamFilter.setSpamThreshhold(prevThreshold);
        return per;
    }
    
    
     public Performance train(String[] testSpam, String[] testHam, int iterations) {
        iterations++;

        Performance max = null;
        double bestThreshold = 0;
        for (double i = 1; i < iterations; i++) {
            Performance perf = performanceCalc(validateSpam, validateHam, i/iterations);
            
            if(max == null || max.getF1Score() < perf.getF1Score()){
                max = perf;
                bestThreshold = i/iterations;
                
            }
        }
        
        getBayesSpamFilter().setSpamThreshhold(bestThreshold);
        
        return performanceCalc(testHam, testHam, bestThreshold);
    }
    
    
}
