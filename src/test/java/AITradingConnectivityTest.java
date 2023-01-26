import projecta.aitrading.model.modelSerializer.RandomForestModelSerializer;
import projecta.aitrading.utils.LoggingUtils;
import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.Tuple;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class AITradingConnectivityTest {

    public static void main(String [] args) {
        testConnectivity();
    }

    public static void testConnectivity() {
        int [][] inputExecute = {{0}};
        double [][] inputWAP = {{0.7173}};
        double [][] inputCount = {{161}};
        double [][] inputMinute = {{40}};
        double [][] inputTesla3 = {{-0.000162}};
        double [][] inputTesla6 = {{-0.001056}};
        double [][] inputTesla9 = {{-0.00216}};
        double [][] decisionInteger = {{2}};
        DataFrame executeDF = DataFrame.of(inputExecute, "EXECUTE");
        DataFrame wapDF = DataFrame.of(inputWAP, "WAP");
        DataFrame countDF = DataFrame.of(inputCount, "Count");
        DataFrame minuteDF = DataFrame.of(inputMinute, "Minute");
        DataFrame tesla3DF = DataFrame.of(inputTesla3, "Tesla3");
        DataFrame tesla6DF = DataFrame.of(inputTesla6, "Tesla6");
        DataFrame tesla9DF = DataFrame.of(inputTesla9, "Tesla9");
        DataFrame decisionDF = DataFrame.of(decisionInteger, "Decision");
        DataFrame inputDF = executeDF.merge(wapDF, countDF, minuteDF, tesla3DF, tesla6DF, tesla9DF, decisionDF);
        Tuple inputDataFrame = inputDF.get(0);
        RandomForestModelSerializer randomForestModelSerializer = (RandomForestModelSerializer) readObjectFromFile("random-forest");
        if (randomForestModelSerializer!=null) {
            RandomForest randomForest = randomForestModelSerializer.getRandomForest();
            var predictionInteger = randomForest.predict(inputDataFrame);
            String prediction = predictionInteger == 0 ? "NO" : "EXECUTE";
            System.out.println("The predicted decision is "+prediction);
        }
    }

    private static Object readObjectFromFile(String filePath) {
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
