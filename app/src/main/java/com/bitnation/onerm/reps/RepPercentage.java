package com.bitnation.onerm.reps;

/**
 * Created by Michael on 4/19/2016.
 */
public class RepPercentage {

    private int percent;

    private int weight;

    public RepPercentage() {

    }

    public RepPercentage(int percent, int weight) {
        this.percent = percent;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
