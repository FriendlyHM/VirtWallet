package com.example.virtwallet;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.*;

public class VirtWalletTest {

    @Test
    public void addAmount() {
        String expectedTotal = "8.02";
        String expected = "total is: " + expectedTotal + "\nCash: {1.0=3, 5.0=1}\nCoins: {0.01=2}";
        double delta = .001;

        VirtWallet wallet = new VirtWallet("test");
        wallet.addMoney(5.00, "cash");
        wallet.addMoney(1.00, "cash");
        wallet.addMoney(1.00, "cash");
        wallet.addMoney(1.00, "cash");
        wallet.addMoney(0.01, "coin");
        wallet.addMoney(0.01, "coin");

        BigDecimal outputTotal = wallet.getTotal();
        String output = wallet.toString();

        assertEquals(expectedTotal, String.format("%.2f",outputTotal));
        assertEquals(expected, output);
    }

    @Test
    public void decreaseAmount() {
        String expectedTotal = "5.25";
        String expected = "total is: " + expectedTotal + "\nCash: {5.0=1}\nCoins: {0.25=1}";
        double delta = .1;
        VirtWallet wallet = new VirtWallet("test");
        wallet.addMoney(5.00,"cash");
        wallet.addMoney(5.00,"cash");
        wallet.addMoney(0.25,"coins");
        wallet.addMoney(0.25,"coins");
        wallet.removeMoney(5.00, "cash");
        wallet.removeMoney(0.25, "coin");
        BigDecimal outputTotal = wallet.getTotal();
        String output = wallet.toString();

        assertEquals(expectedTotal, String.format("%.2f",outputTotal));
        assertEquals(expected, output);

    }

    @Test
    public void calcTotal() {
        String expectedTotal = "1.02";
        VirtWallet wallet = new VirtWallet("test");
        HashMap<Double,Integer> cash = new HashMap<>();
        HashMap<Double,Integer> coins = new HashMap<>();
        cash.put(0.01,2);
        coins.put(0.25,4);
        String outputTotal = String.format("%.2f", wallet.calcTotal(cash, coins));
        assertEquals(expectedTotal, outputTotal);
    }
}