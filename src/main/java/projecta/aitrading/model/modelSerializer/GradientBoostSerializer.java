package projecta.aitrading.model.modelSerializer;

import smile.classification.GradientTreeBoost;

import java.io.Serializable;

public class GradientBoostSerializer implements Serializable {

    private static final long serialVersionUID = 1L;

    private GradientTreeBoost gradientTreeBoost;

    public GradientBoostSerializer(GradientTreeBoost gradientTreeBoost) {
        this.gradientTreeBoost = gradientTreeBoost;
    }

    public GradientTreeBoost getGradientTreeBoost() {
        return gradientTreeBoost;
    }

    public void setGradientTreeBoost(GradientTreeBoost gradientTreeBoost) {
        this.gradientTreeBoost = gradientTreeBoost;
    }
}
