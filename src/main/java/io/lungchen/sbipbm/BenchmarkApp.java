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
        Integer numOfRequests = 10000;
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
        XYChart chart = new XYChartBuilder().title("Benchmark").height(400).width(600).theme(Styler.ChartTheme.Matlab).yAxisTitle("Response Time (ms)").build();
        chart.addSeries("rtt", y);

        // Show it
        SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
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
                        while (y.size() > 50) {
                            y.remove(0);
                        }

                        chart.updateXYSeries("rtt", null, y, null);
                        sw.repaintChart();


                ++id;
            }
        } catch (Exception e) {
            System.exit(1);
        }

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.getMessage();
        }


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

                y.add(timeElapsed.doubleValue());

                while (y.size() > 50) {
                    y.remove(0);
                }

                chart.updateXYSeries("rtt", null, y, null);
                sw.repaintChart();
                ++id;
            }
        } catch (Exception e) {
            System.exit(1);
        }


    }
}