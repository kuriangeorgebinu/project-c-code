package com.coderofjava8888.utils;

import com.aminekili.aitrading.utils.LoggingUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PythonGateway {

    private static final String pythonScriptPath = "ml_test.py";

    private PythonGateway() {}

    public static PythonGateway createGateway() {
        return new PythonGateway();
    }

    public String executeCommand(String commandType, String ... commandArguments) throws IOException, InterruptedException {

        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(pythonScriptPath);
        command.add(commandType);
        command.addAll(Arrays.asList(commandArguments));
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        Process process = processBuilder.start();

        InputStream errorStream = process.getErrorStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        String errorLine;
        while ((errorLine = errorReader.readLine())!=null) {
            LoggingUtils.print(errorLine);
        }

        // Read output from the process
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
            LoggingUtils.print(line);
        }

        // Wait for the process to finish
        int exitCode = process.waitFor();
        LoggingUtils.print("Process exited with code " + exitCode);

        return response.toString();
    }

}
