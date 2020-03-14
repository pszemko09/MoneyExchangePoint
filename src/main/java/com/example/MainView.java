package com.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

@Route("")
public class MainView extends VerticalLayout {
    private VerticalLayout mainLayout;

    private final TextField convertedSumField = new TextField("Sum after exchange");

    private Button convertButton;
    private NumberField sumField;
    private static ComboBox<Currency> currencyFromSelect;
    private static ComboBox<Currency> currencyToSelect;

    private HistoricRates historicRates;
    private Component chartComponent = new Label("If chart is available it will be shown here.");

    public MainView(){
        add(
                mainLayout = new VerticalLayout(new H1("Exchange Office"), buildForm(), convertedSumField, chartComponent)
        );

        Binder<Money> binder = new Binder<>(Money.class);
        binder.forField(sumField)
                .asRequired("Sum to exchange is required.")
                .withValidator(new DoubleRangeValidator("Sum must be positive value.", 0.0, Double.MAX_VALUE))
                .bind(Money::getAmountOfMoney, Money::setAmountOfMoney);

        binder.forField(currencyFromSelect)
                .asRequired("specify the currency.")
                .bind(Money::getCurrency, Money::setCurrency);

        binder.forField(currencyToSelect).asRequired("specify the currency.")
                .asRequired("specify the currency.")
                .bind(Money::getCurrency, Money::setCurrency);
        binder.readBean(new Money());

        binder.addStatusChangeListener(statusChangeEvent -> {
            if(!currencyFromSelect.isEmpty() && !currencyToSelect.isEmpty() && !sumField.isEmpty()){
                convertButton.setEnabled(true);
            }
        });

        convertButton.addClickListener(buttonClickEvent -> {
            Money money = new Money(sumField.getValue(), currencyFromSelect.getValue());
            try {
                BigDecimal moneyInNewCurrency = Converter.convert(money, currencyToSelect.getValue());
                convertedSumField.setValue(moneyInNewCurrency.toString());            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if(currencyFromSelect.getValue() == Currency.EUR && !currencyToSelect.isEmpty()){
                historicRates = new HistoricRates();
                Chart newChart = historicRates.createChart();
                mainLayout.replace(chartComponent, newChart);
                chartComponent = newChart;
            }
            if(currencyFromSelect.getValue() != Currency.EUR && !currencyToSelect.isEmpty()){
                Label infoLabel = new Label("Chart is not available.");
                mainLayout.replace(chartComponent, infoLabel );
                chartComponent = infoLabel;
            }

        });
    }

    private Component buildForm(){
        sumField = new NumberField("Sum");

        currencyFromSelect = new ComboBox<>("Currency from", Arrays.asList(Currency.values()));
        currencyToSelect = new ComboBox<>("Currency to", Arrays.asList(Currency.values()));

        convertButton = new Button("Convert");
        convertButton.setEnabled(false);

        HorizontalLayout formLayout = new HorizontalLayout(sumField, currencyFromSelect, currencyToSelect, convertButton);
        Div wrapperLayout = new Div(formLayout);
        formLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        wrapperLayout.setWidth("100%");

        return wrapperLayout;
    }

    public static Currency getChosenCurrencyTo(){
        return currencyToSelect.getValue();
    }
}
