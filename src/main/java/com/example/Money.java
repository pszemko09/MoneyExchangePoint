package com.example;

public class Money {
    private Double amountOfMoney;
    private Currency currency;

    public Money(){

    }

    public Money(final Double money, final Currency currency){
        if(money != null && currency != null){
            this.amountOfMoney = money;
            this.currency = currency;
        }
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }
}
