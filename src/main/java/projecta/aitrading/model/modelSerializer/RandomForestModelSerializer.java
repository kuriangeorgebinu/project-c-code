package projecta.aitrading.model.modelSerializer;

import smile.classification.RandomForest;

import java.io.Serializable;

public class RandomForestModelSerializer implements Serializable {

    private static final long serialVersionUID = 1L;

    private RandomForest randomForest;

    public RandomForestModelSerializer(RandomForest model) {
        this.randomForest = model;
    }

    public RandomForest getRandomForest() {
        return randomForest;
    }

    public void setRandomForest(RandomForest randomForest) {
        this.randomForest = randomForest;
    }
}
