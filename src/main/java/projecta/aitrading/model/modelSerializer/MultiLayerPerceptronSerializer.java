package projecta.aitrading.model.modelSerializer;

import smile.classification.MLP;

import java.io.Serializable;

public class MultiLayerPerceptronSerializer implements Serializable {

    private static final long serialVersionUID = 1L;

    private MLP multiLayerPerceptron;

    public MultiLayerPerceptronSerializer(MLP multiLayerPerceptron) {
        this.multiLayerPerceptron = multiLayerPerceptron;
    }

    public MLP getMultiLayerPerceptron() {
        return multiLayerPerceptron;
    }

    public void setMultiLayerPerceptron(MLP multiLayerPerceptron) {
        this.multiLayerPerceptron = multiLayerPerceptron;
    }
}
