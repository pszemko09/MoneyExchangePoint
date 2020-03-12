package com.example;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class HistoricRates {
    private static final int MEASURED_PERIOD = 14;
    private String urlFormat = "http://data.fixer.io/api/%s?access_key=%s&base=EUR&symbols=%s";

    private LocalDate currentDate;
    private static Map<String, BigDecimal> dailyRatesFromTwoWeeks = new LinkedHashMap<>();

    public HistoricRates(){
        currentDate = LocalDate.now();

        for(int i = 0; i <= MEASURED_PERIOD; i++){
            String newStringDate = currentDate.minusDays(MEASURED_PERIOD - i).toString();

            try {
                String urlAddressFormat = String.format(urlFormat, newStringDate, RateProvider.getKey("/key.pem"),MainView.getChosenCurrencyTo().toString());
                JSONObject json = JSONReader.readJsonFromUrl(urlAddressFormat);
                BigDecimal currencyRateToEuro = new BigDecimal(json.getJSONObject("rates").getDouble(MainView.getChosenCurrencyTo().toString()));
                dailyRatesFromTwoWeeks.put(newStringDate, currencyRateToEuro);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Chart createChart(){
        Chart chart = new Chart();
        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.LINE);

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setStacking(Stacking.NORMAL);
        configuration.setPlotOptions(plotOptions);

        XAxis xaxis = new XAxis();
        xaxis.setTitle("Date");
        YAxis yaxis = new YAxis();
        yaxis.setTitle("Currency Rate [EUR]");

        DataSeries series = new DataSeries();
        dailyRatesFromTwoWeeks.forEach((k, v) -> {
            series.add(new DataSeriesItem(k, v));
            xaxis.addCategory(k);

        });

        configuration.addyAxis(yaxis);
        configuration.addxAxis(xaxis);
        configuration.addSeries(series);

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.HORIZONTAL);
        legend.setVerticalAlign(VerticalAlign.BOTTOM);

        configuration.setTitle("Daily average rate");

        return chart;
    }


}
