package com.coderofjava8888.strategy;

import com.aminekili.aitrading.model.implementation.PythonMLModel;
import com.aminekili.aitrading.model.implementation.RandomForestImpl;
import com.aminekili.aitrading.utils.LoggingUtils;

public class LiveBarPriorClassification {

    private double open, high, low, close;
    private long volume;
    private double wap;
    private int count;
    private double minute;
    private double day;
    private double month;
    private double tesla3, tesla6, tesla9;
    private double value5, value6;
    private String decision;

    
    // 
    public LiveBarPriorClassification(double open, double high, double low, double close,long volume, double wap, int count, double minute, double day, double month, double tesla3, double tesla6, double tesla9, double value5, double value6, String decision) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.wap = wap;
        this.count = count;
        this.minute = minute;
        this.day = day;
        this.month = month;
        this.tesla3 = tesla3;
        this.tesla6 = tesla6;
        this.tesla9 = tesla9;
        this.value5 = value5;
        this.value6 = value6;
        this.decision = decision;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getMonth() {
        return month;
    }

    public void setMonth(double month) {
        this.month = month;
    }

    public double getValue5() {
        return value5;
    }

    public void setValue5(double value5) {
        this.value5 = value5;
    }

    public double getValue6() {
        return value6;
    }

    public void setValue6(double value6) {
        this.value6 = value6;
    }

    public String executeOrNot(String currentSymbolFUT) {
        try {
            //var ml = RandomForestImpl.getInstance(currentSymbolFUT);
            var ml = PythonMLModel.getInstance(currentSymbolFUT);  //open, high, low, close,
            return ml.predict(open, high, low, close, volume, wap, count, minute, day, month, tesla3, tesla6, tesla9, value5, value6, decision);
        } catch (Exception e) {
            e.printStackTrace();
            return "NO";
        }
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }
    public double getMinute() {
        return minute;
    }

    public void setMinute(double minute) {
        this.minute = minute;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getWap() {
        return wap;
    }

    public void setWap(double wap) {
        this.wap = wap;
    }

    public double getTesla3() {
        return tesla3;
    }

    public void setTesla3(double tesla3) {
        this.tesla3 = tesla3;
    }

    public double getTesla6() {
        return tesla6;
    }

    public void setTesla6(double tesla6) {
        this.tesla6 = tesla6;
    }

    public double getTesla9() {
        return tesla9;
    }

    public void setTesla9(double tesla9) {
        this.tesla9 = tesla9;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

}
