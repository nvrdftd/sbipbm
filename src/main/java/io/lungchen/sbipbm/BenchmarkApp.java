package io.lungchen.sbipbm;

import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

public class BenchmarkApp {

    public static void main(String[] args) {

        Integer numOfAccounts = 2;
        Integer numOfRequests = 500;
        Integer numOfTests = 5;
        // create a few accounts
        ArrayList<Account> accounts = new ArrayList<Account>();

        AccountFactory accountFactory = null;

        try {
            accountFactory = AccountFactory.getAccountFactory();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        if (accountFactory == null) System.exit(1);

        Integer id = 0;
        RequestClient requestClient = RequestClient.getRequestClient();

        try {

            for (int i = 0; i < numOfAccounts; ++i) {

                Account account = accountFactory.newAccount();

                accounts.add(account);

                AccountRequestBody requestBody = new AccountRequestBody();

                requestBody.setId(id);
                requestBody.setTimestamp(System.currentTimeMillis());
                requestBody.setKey(Base64.getEncoder().encodeToString(account.getPublicKey().getEncoded()));
                requestBody.setAccountId(account.getUuid().toString());
                requestBody.setInitialBalance(100000.00);
                requestBody.setSignature(account.sign(requestBody.toString()));

                requestClient.post("http://localhost:8080/account", requestBody);

                ++id;
            }
        } catch (Exception e) {
            System.exit(1);
        }

        ArrayList<Double> y = new ArrayList<>();
        y.add(0.0);

        // Create Chart
        XYChart chart = new XYChartBuilder().height(400).width(600).theme(Styler.ChartTheme.GGPlot2).yAxisTitle("Round-trip Time (ms)").build();
        chart.addSeries("get-balance", y);

        // Show it
        SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
        sw.setTitle("Benchmark");
        sw.displayChart();

        // get-balance operation

        Account account = accounts.get(0);

        try {
            for (int i = 0; i < numOfRequests; ++i) {

                BalanceRequestBody requestBody = new BalanceRequestBody();

                requestBody.setId(id);
                requestBody.setTimestamp(System.currentTimeMillis());
                requestBody.setKey(Base64.getEncoder().encodeToString(account.getPublicKey().getEncoded()));
                requestBody.setAccountId(account.getUuid().toString());
                requestBody.setSignature(account.sign(requestBody.toString()));



                        Long startTime = System.nanoTime();

                        requestClient.get("http://localhost:8080/get-balance", requestBody);

                        Long endTime = System.nanoTime();

                        Long timeElapsed = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

                        y.add(timeElapsed.doubleValue());

                        // Limit the total number of points
                        while (y.size() > 30) {
                            y.remove(0);
                        }

                        chart.updateXYSeries("get-balance", null, y, null);
                        sw.repaintChart();


                ++id;
            }
        } catch (Exception e) {
            System.exit(1);
        }

        ArrayList<Double> y1 = new ArrayList<>();
        y1.add(0.0);

        // Create Chart
        XYChart chart1 = new XYChartBuilder().height(400).width(600).theme(Styler.ChartTheme.GGPlot2).yAxisTitle("Round-trip Time (ms)").build();
        chart1.addSeries("send", y1);

        // Show it
        SwingWrapper<XYChart> sw1 = new SwingWrapper<XYChart>(chart1);
        sw1.setTitle("Benchmark");
        sw1.displayChart();


        // transfer from one account to another account
        Account fromAccount = accounts.get(0);
        Account toAccount = accounts.get(1);

        try {
            for (int i = 0; i < numOfRequests; ++i) {
                TransferRequestBody requestBody = new TransferRequestBody();

                requestBody.setId(id);
                requestBody.setTimestamp(System.currentTimeMillis());
                requestBody.setKey(Base64.getEncoder().encodeToString(fromAccount.getPublicKey().getEncoded()));
                requestBody.setFrom(fromAccount.getUuid().toString());
                requestBody.setTo(toAccount.getUuid().toString());
                requestBody.setAmount(1.0);
                requestBody.setSignature(fromAccount.sign(requestBody.toString()));
                Long startTime = System.nanoTime();
                requestClient.post("http://localhost:8080/send", requestBody);
                Long endTime = System.nanoTime();
                Long timeElapsed = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

                y1.add(timeElapsed.doubleValue());

                while (y1.size() > 30) {
                    y1.remove(0);
                }

                chart1.updateXYSeries("send", null, y1, null);
                sw1.repaintChart();
                ++id;
            }
        } catch (Exception e) {
            System.exit(1);
        }

//        try {
//            ArrayList<Double> y2 = new ArrayList<>();
//            y2.add(0.0);
//
//
//            // Create Chart
//            XYChart chart2 = new XYChartBuilder().height(400).width(600).theme(Styler.ChartTheme.GGPlot2).yAxisTitle("QPS").build();
//            chart2.addSeries("throughput", y2);
//
//            // Show it
//            SwingWrapper<XYChart> sw2 = new SwingWrapper<XYChart>(chart2);
//            sw2.setTitle("Benchmark");
//            sw2.displayChart();
//
//            for (int i = 0; i < numOfTests; ++i) {
//                Long startTime = System.nanoTime();
//                for (int j = 0; j < numOfRequests; ++j) {
//                    TransferRequestBody requestBody = new TransferRequestBody();
//
//                    requestBody.setId(id);
//                    requestBody.setTimestamp(System.currentTimeMillis());
//                    requestBody.setKey(Base64.getEncoder().encodeToString(fromAccount.getPublicKey().getEncoded()));
//                    requestBody.setFrom(fromAccount.getUuid().toString());
//                    requestBody.setTo(toAccount.getUuid().toString());
//                    requestBody.setAmount(1.0);
//                    requestBody.setSignature(fromAccount.sign(requestBody.toString()));
//                    requestClient.post("http://localhost:8080/send", requestBody);
//
//                    ++id;
//                }
//
//                Long endTime = System.nanoTime();
//
//                Long timeElapsed = TimeUnit.NANOSECONDS.toSeconds(endTime - startTime);
//
//                y2.add(numOfRequests / timeElapsed.doubleValue());
//
//                while (y2.size() > 10) {
//                    y2.remove(0);
//                }
//
//                chart2.updateXYSeries("throughput", null, y2, null);
//                sw2.repaintChart();
//                ++id;
//
//            }
//
//        } catch (Exception e) {
//            System.exit(1);
//        }

    }
}