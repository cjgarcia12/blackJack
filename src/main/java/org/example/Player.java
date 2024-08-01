package org.example;

import java.util.Scanner;
import java.util.ArrayList;

//implementation of blackjack player
public class Player {
    private ArrayList<Card> cards;
    private int money;

    public Player() {
        this.cards = new ArrayList<>();
        this.money = 1000;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }



}