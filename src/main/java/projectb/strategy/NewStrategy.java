package projectb.strategy;

import projecta.aitrading.EntryPointRandomForestTrainTest;
import projecta.aitrading.model.modelSerializer.RandomForestModelSerializer;
import projecta.aitrading.utils.LoggingUtils;
import projectb.ib.client.Bar;
import smile.classification.RandomForest;
import smile.data.AbstractTuple;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.data.measure.NominalScale;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.data.vector.BaseVector;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

//  For classification
public class NewStrategy {

    double avgCurOpnPrvWAP;
    double tesla3, tesla6, tesla9;
    String signalDecision;
    String execution;
    int b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12;
    int minute = 0;
    private static final Formula formula = Formula.of("EXECUTE", "WAP", "Count", "Minute", "Tesla3", "Tesla6", "Tesla9", "Decision");
    private static final String modelPath = "random-forest";

    private static final StructType inputDataSchema = DataTypes.struct(
            new StructField("EXECUTE", DataTypes.DoubleType),
            new StructField("WAP", DataTypes.DoubleType),
            new StructField("Count", DataTypes.DoubleType),
            new StructField("Minute", DataTypes.DoubleType),
            new StructField("Tesla3", DataTypes.DoubleType),
            new StructField("Tesla6", DataTypes.DoubleType),
            new StructField("Tesla9", DataTypes.DoubleType),
            new StructField("Decision", DataTypes.DoubleType));


    public String executionDeterminer(ArrayList<Bar> barInput) throws InterruptedException {  // throws InterruptedException {
        Instant instant = Instant.now();
        String prediction = "";
        int min = instant.atZone(ZoneOffset.UTC).getMinute();
        if (min >= 0 && min <= 2) {
            minute = 0;
        }
        if (min >= 58 && min <= 59) {
            minute = 0;
        }
        if (min >= 19 && min <= 22) {
            minute = 20;
        }
        if (min >= 39 && min <= 42) {
            minute = 40;
        }

        System.out.println("min =   " + min + "    Minute " + minute);

        System.out.println("barInput.size()  " + barInput.size());

        if (barInput.size() >= 12) {

            if (barInput.size() == 13) {
                b12 = barInput.size() - 1;
                b11 = b12 - 1;
                b10 = b11 - 1;
                b9 = b10 - 1;
                b8 = b9 - 1;
                b7 = b8 - 1;
                b6 = b7 - 1;
                b5 = b6 - 1;
                b4 = b5 - 1;
                b3 = b4 - 1;
                b2 = b3 - 1;
                b1 = b2 - 1;
            }
            if (barInput.size() == 12) {
                b12 = 11;
                b11 = 10;
                b10 = 9;
                b9 = 8;
                b8 = 7;
                b7 = 6;
                b6 = 5;
                b5 = 4;
                b4 = 3;
                b3 = 2;
                b2 = 1;
                b1 = 0;
            }

            avgCurOpnPrvWAP = barInput.get(b12).wap() * barInput.get(b12).close();
            tesla3 = 0.3 * ((avgCurOpnPrvWAP - barInput.get(b1).wap()) + (avgCurOpnPrvWAP - barInput.get(b2).wap()) + (avgCurOpnPrvWAP - barInput.get(b3).wap()) + (avgCurOpnPrvWAP - barInput.get(b4).wap()));
            tesla6 = 0.6 * ((avgCurOpnPrvWAP - barInput.get(b5).wap()) + (avgCurOpnPrvWAP - barInput.get(b6).wap()) + (avgCurOpnPrvWAP - barInput.get(b7).wap()) + (avgCurOpnPrvWAP - barInput.get(b8).wap()));
            tesla9 = 0.9 * ((avgCurOpnPrvWAP - barInput.get(b9).wap()) + (avgCurOpnPrvWAP - barInput.get(b10).wap()) + (avgCurOpnPrvWAP - barInput.get(b11).wap()) + (avgCurOpnPrvWAP - barInput.get(b12).wap()));

            if (tesla3 < tesla6 && tesla6 < tesla9) {
                signalDecision = "BUY";
            }

            if (tesla3 > tesla6 && tesla6 > tesla9) {
                signalDecision = "SELL";
            }

            System.out.println("volume  " + barInput.get(b12).volume());
            System.out.println("count  " + barInput.get(b12).count());
            System.out.println("WAP  " + barInput.get(b12).wap());
            System.out.println("minute  " + minute);
            System.out.println(" ");
            System.out.println("tesla3  " + tesla3);
            System.out.println("tesla6  " + tesla6);
            System.out.println("tesla9  " + tesla9);
            System.out.println("signal  " + signalDecision);


            DataFrame inputDF = constructInputDataFrame(1, barInput.get(b12).open(),
                    barInput.get(b12).high(), barInput.get(b12).low(), barInput.get(b12).close(), barInput.get(b12).wap(), barInput.get(b12).count(),
                    minute, tesla3, tesla6, tesla9, convertDecisionToInteger(signalDecision));

            Tuple inputDataFrame = inputDF.get(0);
            RandomForestModelSerializer randomForestModelSerializer = (RandomForestModelSerializer) readObjectFromFile(modelPath);
            if (randomForestModelSerializer != null) {
                RandomForest randomForest = randomForestModelSerializer.getRandomForest();
                var predictionInteger = randomForest.predict(inputDataFrame);
                prediction = predictionInteger == 0 ? "NO" : "EXECUTE";
            }

//        LiveBarPriorClassification test = new LiveBarPriorClassification(barInput.get(12).volume(), barInput.get(12).count(), barInput.get(12).wap(), tesla3, tesla6, tesla9, signalDecision);
//TEMPORARY -
        }

//TEMPORARY -    ERASE/EDIT BELOW
//  GOAL IS TO OBTAIN EXECUTE OR NO RETURN FROM MACHINE LEARNING PIECE.
        
        boolean execute = prediction.equals("EXECUTE");
        String executionOrNo = execute ? signalDecision : "NO";  //  <--- This is temporary.  Need link here to ML piece
        barInput.clear();
        return executionOrNo;

    }

    private DataFrame constructInputDataFrame(int executeValue, double open, double high, double low, double close, double wapValue, double countValue, double minuteValue, double tesla3Value, double tesla6Value, double tesla9Value, double decisionInteger) {
        int[][] inputExecute = {{executeValue}};
        double [][] inputOpen = {{open}};
        double [][] inputHigh = {{high}};
        double [][] inputLow = {{low}};
        double [][] inputClose = {{close}};
        double[][] inputWAP = {{wapValue}};
        double[][] inputCount = {{countValue}};
        double[][] inputMinute = {{minuteValue}};
        double[][] inputTesla3 = {{tesla3Value}};
        double[][] inputTesla6 = {{tesla6Value}};
        double[][] inputTesla9 = {{tesla9Value}};
        double[][] decision = {{decisionInteger}};
        DataFrame executeDF = DataFrame.of(inputExecute, "EXECUTE");
        DataFrame openDF = DataFrame.of(inputOpen, "Open");
        DataFrame highDF = DataFrame.of(inputHigh, "High");
        DataFrame lowDF = DataFrame.of(inputLow, "Low");
        DataFrame closeDF = DataFrame.of(inputClose, "Close");
        DataFrame wapDF = DataFrame.of(inputWAP, "WAP");
        DataFrame countDF = DataFrame.of(inputCount, "Count");
        DataFrame minuteDF = DataFrame.of(inputMinute, "Minute");
        DataFrame tesla3DF = DataFrame.of(inputTesla3, "Tesla3");
        DataFrame tesla6DF = DataFrame.of(inputTesla6, "Tesla6");
        DataFrame tesla9DF = DataFrame.of(inputTesla9, "Tesla9");
        DataFrame decisionDF = DataFrame.of(decision, "Decision");
        return executeDF.merge(openDF, highDF, lowDF, closeDF, wapDF, countDF, minuteDF, tesla3DF, tesla6DF, tesla9DF, decisionDF);
    }

    public double convertDecisionToInteger(String signalDecision) {
        if (signalDecision.equals("NO")) {
            return 0;
        } else if (signalDecision.equals("SELL")) {
            return 2;
        } else if (signalDecision.equals("BUY")) {
            return 1;
        }
        return -1;
    }

    private Object readObjectFromFile(String filePath) {
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            LoggingUtils.print("The model has been read from file");
            objectIn.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
