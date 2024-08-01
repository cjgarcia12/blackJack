package org.example;

import java.util.Scanner;
import java.util.ArrayList;

//implementation of blackjack player
public class Player {
    private int score;
    private ArrayList<Card> cards;
    private int money;

    public Player() {
        this.score = 0;
        this.cards = new ArrayList<>();
        this.money = 1000;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    public int getScore(int score) {
        return this.score;
    }


    @Override
    public String toString() {
        return "cards: " + this.cards + "\nScore: " + this.score;
    }



}