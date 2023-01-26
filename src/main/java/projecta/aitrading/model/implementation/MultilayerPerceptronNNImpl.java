package projecta.aitrading.model.implementation;

import projecta.aitrading.model.BaseModel;
import projecta.aitrading.model.modelSerializer.MultiLayerPerceptronSerializer;
import projecta.aitrading.service.CsvReader;
import projecta.aitrading.utils.ConfusionMatrix;
import projecta.aitrading.utils.DataFrameUtils;
import projecta.aitrading.utils.LoggingUtils;
import projecta.aitrading.utils.Pair;
import smile.base.mlp.Layer;
import smile.base.mlp.OutputFunction;
import smile.classification.MLP;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.math.MathEx;
import smile.math.TimeFunction;
import smile.validation.ClassificationValidation;

import java.io.*;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class MultilayerPerceptronNNImpl implements BaseModel {

    /**
     * This class is responsible for the implementation of the Random Forest algorithm
     * it will be used to decide whether to execute flag should be set to true or false
     */

    // initialized in static initialisation block
    public static final long[] seeds;

    private final String trainingPath;
    private final String testingPath;
    private final String modelPath;
    private MLP model;
    private final List<Pair<String, Predicate<Double>>> predicatePerColumn;
    private Map<String, Map<String, Byte>> stringCategoryMapByteCategory;
    private Map<String, Map<Byte, String>> byteCategoryMapStringCategory;
    private static final int numberOfClasses = 2;
    private static final double learningRate = 0.1;
    private static final double momentum = 0.1;
    private static final int epoch = 100;

    private static final String[] inputColumns = new String[]{
            "Open", "High", "Low", "Close", "Volume",
            "WAP", "Count", "Minute", "Tesla3",
            "Tesla6", "Tesla9", "Decision"
    };

    private static final String outputColumn = "EXECUTE";


    public static final Formula formula = Formula.of("EXECUTE",
            "Open", "High", "Low", "Close", "Volume", "WAP", "Count", "Minute", "Tesla3", "Tesla6", "Tesla9", "Decision"
    );


    public static void main(String... args) {

    }

    public MultilayerPerceptronNNImpl(String trainDataSetPath, String testDateSetPath, String modelPath, List<Pair<String, Predicate<Double>>> predicatePerColumn) throws IOException, URISyntaxException {
        MathEx.setSeed(19650218);
        this.trainingPath = trainDataSetPath;
        this.testingPath = testDateSetPath;
        this.modelPath = modelPath;
        this.predicatePerColumn = predicatePerColumn;
    }

    /**
     * This method will train the model using the training data set
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    public void train() throws IOException, URISyntaxException {
        LoggingUtils.print("Start training");
        DataFrame byteOnlyData = getDataFrameReady(trainingPath, "TRAINING");

        var net = new MLP(inputColumns.length,
                Layer.sigmoid(numberOfClasses),
                Layer.mle(1, OutputFunction.SIGMOID)
        );

        net.setLearningRate(TimeFunction.constant(learningRate));
        net.setMomentum(TimeFunction.constant(momentum));


        var pairTraining = getDataFrameAsArray(byteOnlyData);
        for (int currentEpoch = 0; currentEpoch < epoch; currentEpoch++) {
            net.update(pairTraining.getFirst(), pairTraining.getSecond());
        }

        LoggingUtils.print("Finished training");
        this.model = net;
        MultiLayerPerceptronSerializer multiLayerPerceptronSerializer = new MultiLayerPerceptronSerializer(this.model);
        saveModelToFile(multiLayerPerceptronSerializer);
    }

    private void saveModelToFile(Object serObj) {
        try {
            FileOutputStream fileOut = new FileOutputStream(this.modelPath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            LoggingUtils.print("The object was successfully written to a file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will evaluate the model precision using the testing data set
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    public void test() throws IOException, URISyntaxException {
        var dataFrame = getDataFrameReady(testingPath, "TEST");

        var predictedStr = new String[dataFrame.size()];
        var actualStr = new String[dataFrame.size()];
        var pair = getDataFrameAsArray(dataFrame);

        for (int i = 0; i < dataFrame.size(); i++) {
            var row = dataFrame.get(i);
            var input = pair.getFirst()[i];

            var prediction = model.predict(input);
            var predictedStringCategory = byteCategoryMapStringCategory.get("EXECUTE").getOrDefault(Integer.valueOf(prediction).byteValue(), "NONE");
            var actualStringCategory = byteCategoryMapStringCategory.get("EXECUTE").get(row.getByte("EXECUTE"));

            predictedStr[i] = predictedStringCategory;
            actualStr[i] = actualStringCategory;

            LoggingUtils.print(MessageFormat.format("Prediction: {0} - Actual: {1}", predictedStringCategory, actualStringCategory));
        }

        var confusionMatrix = new ConfusionMatrix(predictedStr, actualStr);
        LoggingUtils.print(confusionMatrix.toString());
    }

    /**
     * Evaluate the precision of the model
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    public void evaluateModelPrecision() throws IOException, URISyntaxException {
        DataFrame byteOnlyTrainingData = getDataFrameReady(trainingPath, "Evaluation");
        DataFrame byteOnlyTestData = getDataFrameReady(testingPath, "Evaluation");

        var pairTrainingData = getDataFrameAsArray(byteOnlyTrainingData);
        var pairTestData = getDataFrameAsArray(byteOnlyTestData);


        var classificationValidation = ClassificationValidation.of(
                pairTrainingData.getFirst(), pairTrainingData.getSecond(), pairTestData.getFirst(), pairTestData.getSecond(), (x, y) -> {
                    var tempModel = new MLP(inputColumns.length,
                            Layer.sigmoid(numberOfClasses),
                            Layer.mle(100, OutputFunction.SIGMOID),
                            Layer.mle(150, OutputFunction.SIGMOID),
                            Layer.mle(1, OutputFunction.SIGMOID)

                    );
                    tempModel.setLearningRate(TimeFunction.constant(learningRate));
                    tempModel.setMomentum(TimeFunction.constant(momentum));
                    tempModel.update(x, y);
                    return tempModel;
                }
        );

        LoggingUtils.print(MessageFormat.format("Evaluation metrics = {0}", classificationValidation.toString()));
        int[] truth = classificationValidation.truth;
        int[] prediction = classificationValidation.prediction;
        for (int i = 0; i < truth.length; i++) {
            var predictedStringCategory = byteCategoryMapStringCategory.get("EXECUTE").getOrDefault(Integer.valueOf(prediction[i]).byteValue(), "NONE");
            var actualStringCategory = byteCategoryMapStringCategory.get("EXECUTE").getOrDefault(Integer.valueOf(truth[i]).byteValue(), "NONE");
            LoggingUtils.print(MessageFormat.format("Prediction: {0} - Actual: {1}", predictedStringCategory, actualStringCategory));
        }
        System.out.println(MessageFormat.format("Confusion Matrix = {0}", classificationValidation.confusion.toString()));

    }

    private Pair<double[][], int[]> getDataFrameAsArray(DataFrame dataFrame) {
        double[][] input = new double[dataFrame.size()][];
        int[] result = new int[dataFrame.size()];
        for (int i = 0; i < dataFrame.size(); i++) {
            Tuple tuple = dataFrame.get(i);
            double[] x = new double[inputColumns.length];
            for (int j = 0; j < inputColumns.length; j++) {
                if (dataFrame.column(inputColumns[j]).type().isDouble()) {
                    x[j] = tuple.getDouble(inputColumns[j]);
                } else if (dataFrame.column(inputColumns[j]).type().isInt()) {
                    x[j] = tuple.getInt(inputColumns[j]);
                } else if (dataFrame.column(inputColumns[j]).type().isByte()) {
                    x[j] = tuple.getByte(inputColumns[j]);
                } else {
                    throw new RuntimeException("Unsupported type");
                }
            }
            Short y = null;
            if (dataFrame.column(outputColumn).type().isDouble()) {
                y = Double.valueOf(tuple.getDouble(outputColumn)).shortValue();
            } else if (dataFrame.column(outputColumn).type().isInt()) {
                y = Integer.valueOf(tuple.getInt(outputColumn)).shortValue();
            } else if (dataFrame.column(outputColumn).type().isByte()) {
                y = Byte.valueOf(tuple.getByte(outputColumn)).shortValue();
            } else {
                throw new RuntimeException("Unsupported type");
            }
            input[i] = x;
            result[i] = y;
        }

        return new Pair<>(input, result);
    }

    private DataFrame getDataFrameReady(String path, String phase) throws IOException, URISyntaxException {
        var trainingData = CsvReader.read(path, formula);

        this.stringCategoryMapByteCategory = DataFrameUtils.mapCategoricalColumns(trainingData, "EXECUTE", "Decision");
        this.byteCategoryMapStringCategory = DataFrameUtils.mapValuesToCategoricalColumns(trainingData, "EXECUTE", "Decision");

        var byteOnlyData = DataFrameUtils.toByteCategoricalDataFrame(trainingData, stringCategoryMapByteCategory);
        byteOnlyData = formula.frame(byteOnlyData);

        LoggingUtils.format("{0} | Before Filter, data size: {1}", phase, byteOnlyData.size());
        for (var pair : predicatePerColumn) {
            byteOnlyData = DataFrameUtils.filter(byteOnlyData, pair.getFirst(), pair.getSecond());
        }
        LoggingUtils.format("{0} | After Filter, data size: {1}", phase, byteOnlyData.size());

        byteOnlyData = formula.frame(byteOnlyData);

        LoggingUtils.print("Schema: " + byteOnlyData.schema());
        LoggingUtils.print("Formula: " + formula);

        return byteOnlyData;
    }


    public String predict(double open, double high, double low, double close, double wap, double volume,
                          double count, double minute, double tesla3, double tesla6, double tesla9, String decision) {
        if (model == null) {
            throw new IllegalStateException("Model is not trained yet");
        }

        Byte decision_byte = stringCategoryMapByteCategory.get("Decision").get(decision);

        double[] input = new double[]{open, high, low, close, wap, volume, count, minute, tesla3, tesla6, tesla9, decision_byte};

        int prediction = model.predict(input);

        return byteCategoryMapStringCategory.get("EXECUTE").getOrDefault(Integer.valueOf(prediction).byteValue(), "NONE");
    }


    static {
        seeds = new long[]{
                342317953, 521642753, 72070657, 577451521, 266953217, 179976193,
                374603777, 527788033, 303395329, 185759582, 261518209, 461300737,
                483646580, 532528741, 159827201, 284796929, 655932697, 26390017,
                454330473, 867526205, 824623361, 719082324, 334008833, 699933293,
                823964929, 155216641, 150210071, 249486337, 713508520, 558398977,
                886227770, 74062428, 670528514, 701250241, 363339915, 319216345,
                757017601, 459643789, 170213767, 434634241, 414707201, 153100613,
                753882113, 546490145, 412517763, 888761089, 628632833, 565587585,
                175885057, 594903553, 78450978, 212995578, 710952449, 835852289,
                415422977, 832538705, 624345857, 839826433, 260963602, 386066438,
                530942946, 261866663, 269735895, 798436064, 379576194, 251582977,
                349161809, 179653121, 218870401, 415292417, 86861523, 570214657,
                701581299, 805955890, 358025785, 231452966, 584239408, 297276298,
                371814913, 159451160, 284126095, 896291329, 496278529, 556314113,
                31607297, 726761729, 217004033, 390410146, 70173193, 661580775,
                633589889, 389049037, 112099159, 54041089, 80388281, 492196097,
                912179201, 699398161, 482080769, 363844609, 286008078, 398098433,
                339855361, 189583553, 697670495, 709568513, 98494337, 99107427,
                433350529, 266601473, 888120086, 243906049, 414781441, 154685953,
                601194298, 292273153, 212413697, 568007473, 666386113, 712261633,
                802026964, 783034790, 188095005, 742646355, 550352897, 209421313,
                175672961, 242531185, 157584001, 201363231, 760741889, 852924929,
                60158977, 774572033, 311159809, 407214966, 804474160, 304456514,
                54251009, 504009638, 902115329, 870383757, 487243777, 635554282,
                564918017, 636074753, 870308031, 817515521, 494471884, 562424321,
                81710593, 476321537, 595107841, 418699893, 315560449, 773617153,
                163266399, 274201241, 290857537, 879955457, 801949697, 669025793,
                753107969, 424060977, 661877468, 433391617, 222716929, 334154852,
                878528257, 253742849, 480885528, 99773953, 913761493, 700407809,
                483418083, 487870398, 58433153, 608046337, 475342337, 506376199,
                378726401, 306604033, 724646374, 895195218, 523634541, 766543466,
                190068097, 718704641, 254519245, 393943681, 796689751, 379497473,
                50014340, 489234689, 129556481, 178766593, 142540536, 213594113,
                870440184, 277912577};
    }


}

