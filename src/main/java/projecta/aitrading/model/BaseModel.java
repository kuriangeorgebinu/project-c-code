package projecta.aitrading.model;

import java.io.IOException;
import java.net.URISyntaxException;

public interface BaseModel {

    void test() throws IOException, URISyntaxException;

    void train() throws IOException, URISyntaxException;

    void evaluateModelPrecision() throws IOException, URISyntaxException;

}
