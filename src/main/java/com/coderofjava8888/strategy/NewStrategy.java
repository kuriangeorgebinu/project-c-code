package com.coderofjava8888.strategy;

import com.ib.client.Bar;
import java.time.Instant;
import java.time.ZoneOffset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class NewStrategy {

    double avgCurOpnPrvWAP;
    double tesla3, tesla6, tesla9;
    String signalDecision = "NO";
    String executionOrNo;
    int b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12;
    private static final DecimalFormat dft = new DecimalFormat("###.###########");
    private double value5;
    private double value6;
    private double day;

    public String executionDeterminer(String currentSymbolFUT, ArrayList<Bar> barInput) throws InterruptedException, ParseException {
        Instant instant = Instant.now();
        int min = instant.atZone(ZoneOffset.UTC).getMinute();
        int minute = min;
        String myExpireDate = expireMonth(currentSymbolFUT);
        String dateNow = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date1 = LocalDate.parse(dateNow, df);
        LocalDate date2 = LocalDate.parse(myExpireDate, df);
        long dateDiff = ChronoUnit.DAYS.between(date1, date2);
        day = (double) dateDiff;
        double month = (Double.valueOf(dateDiff)) / (365.0 / 12.0);
        System.out.println("Differences-#Days_[spot-vs-Expiration]=  " + dateDiff);
        System.out.println("Difference-#Month_[spot-vs-Expiration]=  " + month);

        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        int intValue5 = calendar.get(Calendar.HOUR_OF_DAY);
        double value5 = Double.valueOf(intValue5);
        value6 = value6Calc(intValue5);

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
        if ((minute != 20 && minute != 40) && (minute != 0)) {
            minute = min;
        }

        System.out.println("min =   " + min + "    Minute " + minute);
        System.out.println("barInput.size()  " + barInput.size());

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

        if (barInput.size() < 12) {
            return executionOrNo = "NO";
        }
        avgCurOpnPrvWAP = (barInput.get(b12).wap() + barInput.get(b12).close()) / 2.0;
        tesla3 = 0.3 * ((avgCurOpnPrvWAP - barInput.get(b1).wap()) + (avgCurOpnPrvWAP - barInput.get(b2).wap()) + (avgCurOpnPrvWAP - barInput.get(b3).wap()) + (avgCurOpnPrvWAP - barInput.get(b4).wap()));
        tesla6 = 0.6 * ((avgCurOpnPrvWAP - barInput.get(b5).wap()) + (avgCurOpnPrvWAP - barInput.get(b6).wap()) + (avgCurOpnPrvWAP - barInput.get(b7).wap()) + (avgCurOpnPrvWAP - barInput.get(b8).wap()));
        tesla9 = 0.9 * ((avgCurOpnPrvWAP - barInput.get(b9).wap()) + (avgCurOpnPrvWAP - barInput.get(b10).wap()) + (avgCurOpnPrvWAP - barInput.get(b11).wap()) + (avgCurOpnPrvWAP - barInput.get(b12).wap()));

        if (tesla3 < tesla6 && tesla6 < tesla9) {
            signalDecision = "BUY";
        }
       if (tesla3 > tesla6 && tesla6 < tesla9) {
            signalDecision = "NO";
        }

        if (tesla3 > tesla6 && tesla6 > tesla9) {
            signalDecision = "SELL";
        }
        if (tesla3 < tesla6 && tesla6 > tesla9) {
            signalDecision = "NO";
        }

        TimeUnit.SECONDS.sleep(3);
        System.out.println(" ");
        System.out.println("barInput.get(b12).wap()  =  " + barInput.get(b12).wap());
        System.out.println("barInput.get(b12).close()=  " + barInput.get(b12).close());
        System.out.println("avgCurOpnPrvWAP          =  " + dft.format(avgCurOpnPrvWAP));
        System.out.println(" ");
        System.out.println("volume   " + barInput.get(b12).volume());
        System.out.println("count    " + barInput.get(b12).count());
        System.out.println("WAP      " + barInput.get(b12).wap());
        System.out.println("minute   " + minute);
        System.out.println("day      " + day);
        System.out.println("month    " + month);
        System.out.println(" ");
        System.out.println("tesla3         " + dft.format(tesla3));
        System.out.println("tesla6         " + dft.format(tesla6));
        System.out.println("tesla9         " + dft.format(tesla9));
        System.out.println("value5         " + value5);
        System.out.println("value6         " + value6);
        System.out.println("signalDecision " + signalDecision);

        var liveBarPriorClassification = new LiveBarPriorClassification(
                barInput.get(b12).open(),
                barInput.get(b12).high(),
                barInput.get(b12).low(),
                barInput.get(b12).close(),
                barInput.get(b12).volume(),
                barInput.get(b12).wap(),
                barInput.get(b12).count(),
                minute,
                day,
                month,
                tesla3,
                tesla6,
                tesla9,
                value5,
                value6,
                signalDecision
        );

        System.out.println("currentSymbolFUT: "+currentSymbolFUT);

        executionOrNo = liveBarPriorClassification.executeOrNot(currentSymbolFUT);

        System.out.println("===========================================");
        System.out.println("executionOrNo   " + executionOrNo);
        System.out.println("===========================================");
        System.out.println("");
        return executionOrNo;

    }

    public String getSignalDecision() {
        return signalDecision;
    }

    public void setSignalDecision(String signalDecision) {
        this.signalDecision = signalDecision;
    }

    private String expireMonth(String incomingMonth) {
        String monthExpire = switch (incomingMonth) {
            case "CAD" ->
                "03/14/2023";
            default ->
                "03/13/2023";
        };
        return monthExpire;
    }

    private double value6Calc(int spotHour) {
        spotHour = switch (spotHour) {
            case 7 ->
                0;
            case 8 ->
                1;
            case 9 ->
                2;
            case 10 ->
                3;
            case 11 ->
                4;
            case 12 ->
                5;
            case 13 ->
                6;
            case 14 ->
                7;
            case 15 ->
                8;
            case 16 ->
                9;
            default ->
                0;
        };
        return spotHour;
    }

}
