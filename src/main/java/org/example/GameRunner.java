package org.example;

import java.util.Scanner;

public class GameRunner {


    public static void main(String[] args) {
        // play some music
        String filepath = "CasinoJazz.wav";
        PlayMusic music = new PlayMusic();
        music.playMusic(filepath);


        Scanner sc = new Scanner(System.in);
        Deck deck = new Deck();

        deck.printDeck(5);
        deck.shuffle();
        System.out.println();
        deck.printDeck(5);


    }
}

