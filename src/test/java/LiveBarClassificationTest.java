import com.coderofjava8888.strategy.LiveBarPriorClassification;


public class LiveBarClassificationTest {


    public static void testExecuteOrNot() {
        String currency = "AUD";
        double open =0.647;
        double high =0.64765;
        double low = 0.6468;
        double close = 0.64755;
        int volume = 239;
        double wap = 0.647245;
        int count= 96;
        double minute= 40;
        double day= 81;
        double month= 2.663014;
        double tesla3= -0.00096;
        double tesla6= -0.00015;
        double tesla9= 0.0011112;
        double value5= 1;
        double value6= 0;
        String decision = "SELL";

        LiveBarPriorClassification liveBarPriorClassification = new LiveBarPriorClassification(open, high, low, close,volume, wap, count, minute, day, month, tesla3, tesla6, tesla9, value5, value6, decision);
        String executeOrNot = liveBarPriorClassification.executeOrNot("AUD");
        System.out.println("The Decision is "+executeOrNot);
    }

    public static void main(String [] args) {
        testExecuteOrNot();
    }


}
