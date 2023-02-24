package com.aminekili.aitrading.model.implementation;

import com.aminekili.aitrading.model.BaseModel;
import com.coderofjava8888.utils.PythonCommands;

public class PythonMLModel implements BaseModel {

    private static PythonMLModel audSingleton;
    private static PythonMLModel chfSingleton;
    private static PythonMLModel eurSingleton;
    private static PythonMLModel gbpSingleton;
    private static PythonMLModel jpySingleton;
    private static PythonMLModel cadSingleton;
    private static PythonMLModel mxpSingleton;

    private final String currency;

    public PythonMLModel(String currency) {
        this.currency = currency;
    }


    @Override
    public void train() throws Exception {
        PythonCommands.executeTrainModelPythonCommand(currency);
    }


    @Override
    public String predict(double open, double high, double low, double close, double volume, double wap, double count, double minute, double day, double month, double tesla3, double tesla6, double tesla9, double value5, double value6, String decision) throws Exception{
        return PythonCommands.executePredictModelCommand(String.valueOf(open), String.valueOf(high), String.valueOf(low), String.valueOf(close), String.valueOf(volume), String.valueOf(wap),
                String.valueOf(count), String.valueOf(minute), String.valueOf(day), String.valueOf(month), String.valueOf(tesla3), String.valueOf(tesla6), String.valueOf(tesla9),
                String.valueOf(value5), String.valueOf(value6), decision);
    }

    private static PythonMLModel activate(String currentSymbolFUT) {
        try {
            var pythonML = new PythonMLModel(currentSymbolFUT);
            pythonML.train();
            return pythonML;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PythonMLModel getInstance(String currentSymbolFUT) {
        switch (currentSymbolFUT){
            case "AUD" -> {
                if (audSingleton == null){
                    audSingleton = activate(currentSymbolFUT);
                }
                return audSingleton;
            }
            case "CHF" -> {
                if (chfSingleton == null){
                    chfSingleton = activate(currentSymbolFUT);
                }
                return chfSingleton;
            }
            case "EUR" -> {
                if (eurSingleton == null){
                    eurSingleton = activate(currentSymbolFUT);
                }
                return eurSingleton;
            }
            case "GBP" -> {
                if (gbpSingleton == null){
                    gbpSingleton = activate(currentSymbolFUT);
                }
                return gbpSingleton;
            }
            case "JPY" -> {
                if (jpySingleton == null){
                    jpySingleton = activate(currentSymbolFUT);
                }
                return jpySingleton;
            }
            case "CAD" -> {
                if (cadSingleton == null){
                    cadSingleton = activate(currentSymbolFUT);
                }
                return cadSingleton;
            }
            case "MXP" -> {
                if (mxpSingleton == null){
                    mxpSingleton = activate(currentSymbolFUT);
                }
                return mxpSingleton;
            }
            default -> throw new IllegalArgumentException("Not a valid currentSymbolFUT");
        }
    }
}
