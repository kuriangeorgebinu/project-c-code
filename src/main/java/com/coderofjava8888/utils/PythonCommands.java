package com.coderofjava8888.utils;

import java.io.IOException;

public class PythonCommands {

    private static final PythonGateway pythonGateway = PythonGateway.createGateway();

    public static void executeTrainModelPythonCommand(String currency) throws IOException, InterruptedException {
        pythonGateway.executeCommand("train", currency);
    }

    public static String executePredictModelCommand(String open,
                                                    String high,
                                                    String low,
                                                    String close,
                                                    String volume,
                                                    String wap,
                                                    String count,
                                                    String minute,
                                                    String day,
                                                    String month,
                                                    String tesla3,
                                                    String tesla6,
                                                    String tesla9,
                                                    String value5,
                                                    String value6,
                                                    String decision) throws IOException, InterruptedException {
        String executeDecision = pythonGateway.executeCommand("predict", open, high, low, close, volume, wap, count, minute,day,
        month, tesla3, tesla6, tesla9, value5, value6, decision);
        return executeDecision;
    }

}
