/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nspro;

/**
 * This class checks the precision and recall values
 *
 * @author Gezahegn
 */
public class Performance {

    private double precision;
    private double recall;

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getF1Score() {
        return 2 * (precision * recall) / (precision + recall);
    }

}
