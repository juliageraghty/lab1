package com.example.java;

public class stockInfo {
    Double max;
    Double min;
    Double sum;

    public stockInfo(Double max, Double min, Double sum) {
        this.max = max;
        this.min = min;
        this.sum = sum;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double input) {
        this.max = input;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double input) {
        this.min = input;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double input) {
        this.sum = input;
    }

}
