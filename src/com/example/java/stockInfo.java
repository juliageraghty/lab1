package com.example.java;

public class stockInfo {
    String symbol;
    Double max;
    Double min;
    Double sum;

    public stockInfo(String symbol, Double max, Double min, Double sum) {
        this.symbol = symbol;
        this.max = max;
        this.min = min;
        this.sum = sum;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String input) {
        this.symbol = input;
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
