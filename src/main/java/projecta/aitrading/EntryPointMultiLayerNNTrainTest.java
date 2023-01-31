package projecta.aitrading;

import projecta.aitrading.model.implementation.MultilayerPerceptronNNImpl;
import projecta.aitrading.utils.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class EntryPointMultiLayerNNTrainTest {

    public static final List<String> trainingPaths = Arrays.asList("src/main/resources/AUD_train.csv",
            "src/main/resources/JPY_train.csv", "src/main/resources/GBP_train.csv", "src/main/resources/CHF_train.csv",
            "src/main/resources/EUR_train.csv");
    public static final List<String> testingPaths = Arrays.asList("src/main/resources/AUD_test.csv",
            "src/main/resources/JPY_test.csv", "src/main/resources/GBP_test.csv", "src/main/resources/CHF_test.csv",
            "src/main/resources/EUR_test.csv");

    public static void trainAndTestModel() {
        try {
            Predicate<Double> filterMinute = val -> val == 20 || val == 40 || val == 0;
            var multilayerNN = new MultilayerPerceptronNNImpl(
                    trainingPaths, testingPaths, "multilayer-neural-network",
                    List.of(
                            new Pair<>("Minute", filterMinute)
                    )
            );
            multilayerNN.train();
            multilayerNN.test();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        trainAndTestModel();
    }
}
