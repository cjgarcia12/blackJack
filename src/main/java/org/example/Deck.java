package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class Deck implements DeckActions{

    private List<Card> deck;

    public Deck(){
        this.deck = new ArrayList<>();
        for (Suits suit : Suits.values()){
            for (Rank rank : Rank.values()){
                deck.add(new Card(rank, suit));
            }
        }
    }
    @Override
    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    @Override
    public Card dealNextCard() {
        Card nextCard = this.deck.get(0);
        this.deck.remove(0);
        return nextCard;

    }

    @Override
    public void printDeck(int numToPrint) {
        for (int i = 0; i < numToPrint; i++) {
            System.out.println(this.deck.get(i).getRank() + " of " + this.deck.get(i).getSuit());
        }
    }
}
