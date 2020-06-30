package com.example.virtwallet;

import java.util.HashMap;

import java.math.BigDecimal;
import java.util.Map;

public class VirtWallet {
    private String name;
    private BigDecimal totalMoney;
    private HashMap<Double, Integer> cash;
    private HashMap<Double, Integer> coins;
    public static final String CASH = "cash";
    public static final String COIN = "coin";

    /**
     * construct a brand new wallet
     */
    public VirtWallet(String name) {
        this.name = name;
        this.totalMoney = new BigDecimal("0.00");
        this.cash = new HashMap<Double, Integer>();
        this.coins = new HashMap<Double, Integer>();
    }

    /**
     * construct from a saved wallet
     */
    public VirtWallet(String name, HashMap<Double, Integer> cash, HashMap<Double, Integer> coins) {
        this.name = name;
        this.cash = cash;
        this.coins = coins;
        this.totalMoney = calcTotal(cash,coins);
    }

    /**
     * adds money into the wallet and updates the total money
     * @param amount - amount of money
     * @param type - type of money being added to wallet
     */
    public void addMoney(double amount, String type) {
        //update totalMoney using BigDecimal
        BigDecimal decAmount = new BigDecimal(amount);
        totalMoney=totalMoney.add(decAmount);

        //update the cash hashmap
        if(type.equals(CASH)) {
            if (cash.containsKey(amount)) {
                cash.put(amount, cash.get(amount) + 1);
            } else {
                cash.put(amount, 1);
            }
        } else {
            if(coins.containsKey(amount)) {
                coins.put(amount, coins.get(amount) + 1);
            } else {
                coins.put(amount, 1);
            }
        }
    }

    /**
     * subtract money into the wallet and updates the total money
     * @param amount - amount of money
     * @param type - type of money being added to wallet
     */
    public void removeMoney(double amount, String type) {
        //update totalMoney using BigDecimal
        BigDecimal decAmount = new BigDecimal(-amount);
        totalMoney=totalMoney.add(decAmount);

        //update the cash hashmap
        if (type.equals(CASH)) {
            if (cash.containsKey(amount)) {
                cash.put(amount, cash.get(amount) - 1);
                if(cash.get(amount) == 0) {
                    cash.remove(amount);
                }
            }
        } else {
            if (coins.containsKey(amount)) {
                coins.put(amount, coins.get(amount) - 1);
                if(coins.get(amount) == 0) {
                    coins.remove(amount);
                }
            }
        }
    }

    /**
     * calculates total money by taking in the cash and coins hashmaps
     * @param cash - hashmap for cash
     * @param coins - hashmap for coins
     * @return BigDecimal total amount of money
     */
    public BigDecimal calcTotal(HashMap<Double, Integer> cash, HashMap<Double, Integer> coins) {
        BigDecimal total = new BigDecimal(0);
        if(cash != null || cash.size() > 0 ) {
            BigDecimal amount;
            for (double each: cash.keySet()) {
                amount = new BigDecimal(each * cash.get(each));
                total = total.add(amount);
            }
        }

        if(coins != null || coins.size() > 0 ) {
            BigDecimal amount;
            for (double each: coins.keySet()) {
                amount = new BigDecimal(each * coins.get(each));
                total = total.add(amount);
            }
        }
        return total;
    }

    public BigDecimal getTotal() {
        return totalMoney;
    }
    public String getStringTotal() {
        return String.format("%.2f",totalMoney);
    }

    public HashMap<Double,Integer> getCash() {
        return cash;
    }
    public HashMap<Double,Integer> getCoins() {
        return coins;
    }
    public String toString() {
        return "total is: " + String.format("%.2f",getTotal()) + "\nCash: " + cash + "\nCoins: " + coins;
    }
}
