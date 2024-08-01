package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameRunner {

    public static int bet(int currentMoney) {
        Scanner scanner = new Scanner(System.in);
        int value = 0;
        boolean stop = false;
        while (!stop) {
            value = scanner.nextInt();
            if (value % 5 == 0 && value <= currentMoney) {
                stop = true;
            } else {
                if (value > currentMoney) {
                    System.out.println("You cannot bet more than you have. You currently have $" + currentMoney);
                    System.out.println("Enter your bet (Must be divisible by 5 and less than or equal to your current money): ");
                } else {
                    System.out.println("Your bet must be in increments of 5...");
                    System.out.println("Enter your bet (Must be divisible by 5 and less than or equal to your current money): ");
                }
            }
        }
        return value;
    }

    public static int drawCards(Deck deck, List<Card> hand, int num) {
        for (int i = 0; i < num; i++) {
            hand.add(deck.dealNextCard());
        }
        return calculateScore(hand);
    }

    public static int calculateScore(List<Card> hand) {
        int score = 0;
        int numAces = 0;

        for (Card card : hand) {
            if (card.getRank() == Rank.ACE) {
                numAces++;
            }
            score += card.getValue();
        }

        // Adjust for aces
        while (score > 21 && numAces > 0) {
            score -= 10; // Reduce the value of an ace from 11 to 1
            numAces--;
        }

        return score;
    }

    public static int hitOrStand(Player player, Deck deck, List<Card> hand, int bet) {
        Scanner sc = new Scanner(System.in);
        boolean stop = false;
        int finalValue = 0;
        while (!stop) {
            System.out.println("Your current hand: " + hand);
            System.out.println("Your current score: " + calculateScore(hand));
            System.out.println("Do you want to hit or stand? (h/s)");
            String decision = sc.nextLine();
            if (decision.equalsIgnoreCase("h")) {
                drawCards(deck, hand, 1);
                if (calculateScore(hand) > 21) {
                    System.out.println("You busted! Final hand: " + hand);
                    player.setMoney(player.getMoney() - bet);
                    finalValue = calculateScore(hand);
                    stop = true;
                }
            } else if (decision.equalsIgnoreCase("s")) {
                finalValue = calculateScore(hand);
                stop = true;
            } else {
                System.out.println("Invalid input. Please enter 'h' to hit or 's' to stand.");
            }
        }
        return finalValue;
    }

    public static int dealer(Deck deck, List<Card> hand) {
        boolean stop = false;
        int finalValue = 0;
        do {
            finalValue = calculateScore(hand);
            System.out.println("Dealer's hand: " + hand);
            System.out.println("Dealer's score: " + finalValue);
            if (finalValue >= 17) {
                System.out.println("Dealer must stay! Final hand: " + hand);
                stop = true;
            } else {
                drawCards(deck, hand, 1);
            }
        }
        while (!stop);
        return finalValue;
    }

    public static int doubleDown(Player player, Deck deck, List<Card> hand, int bet) {
        int newBet = bet * 2;
        if (newBet <= player.getMoney()) {
            player.setMoney(player.getMoney() - bet); // Deduct the additional bet amount
            System.out.println("Doubling down! Your new bet is $" + newBet);
            drawCards(deck, hand, 1);
            System.out.println("Your final hand: " + hand);
            System.out.println("Your final score: " + calculateScore(hand));
            return newBet;
        } else {
            System.out.println("You don't have enough money to double down.");
            return bet; // Return the original bet if doubling down is not possible
        }
    }

    public static boolean split(Player player, Deck deck, List<Card> hand, int bet) {
        if (hand.size() == 2 && hand.get(0).getValue() == hand.get(1).getValue()) {
            System.out.println("Splitting your hand.");
            List<Card> hand1 = new ArrayList<>();
            List<Card> hand2 = new ArrayList<>();
            hand1.add(hand.get(0));
            hand2.add(hand.get(1));
            drawCards(deck, hand1, 1);
            drawCards(deck, hand2, 1);

            System.out.println("Playing first hand...");
            int playerValue1 = hitOrStand(player, deck, hand1, bet);
            System.out.println("Playing second hand...");
            int playerValue2 = hitOrStand(player, deck, hand2, bet);

            System.out.println("Your final first hand: " + hand1);
            System.out.println("Your final first hand score: " + playerValue1);
            System.out.println("Your final second hand: " + hand2);
            System.out.println("Your final second hand score: " + playerValue2);

            // Play dealer's hand and compare results for each hand
            List<Card> dealerHand = new ArrayList<>();
            drawCards(deck, dealerHand, 2);
            int dealerValue = dealer(deck, dealerHand);

            System.out.println("Dealer's final hand: " + dealerHand);
            System.out.println("Dealer's final score: " + dealerValue);

            if (dealerValue > 21 || playerValue1 > dealerValue) {
                System.out.println("You win first hand!");
                player.setMoney(player.getMoney() + bet);
            } else if (playerValue1 < dealerValue) {
                System.out.println("Dealer wins first hand!");
                player.setMoney(player.getMoney() - bet);
            } else {
                System.out.println("First hand is a tie!");
            }

            if (dealerValue > 21 || playerValue2 > dealerValue) {
                System.out.println("You win second hand!");
                player.setMoney(player.getMoney() + bet);
            } else if (playerValue2 < dealerValue) {
                System.out.println("Dealer wins second hand!");
                player.setMoney(player.getMoney() - bet);
            } else {
                System.out.println("Second hand is a tie!");
            }

            System.out.println("Your money $" + player.getMoney());
            return true;
        } else {
            System.out.println("You cannot split this hand.");
            return false;
        }
    }

    public static void main(String[] args) {
        // play some music
        String filepath = "CasinoJazz.wav";
//        PlayMusic music = new PlayMusic();
//        music.playMusic(filepath);

        Scanner sc = new Scanner(System.in);
        Player dealer = new Player();
        Player player = new Player();
        player.setMoney(1000); // Initial money for the player
        Deck deck = new Deck();
        deck.shuffle();

        boolean stop = false;
        System.out.println("Welcome to Casino Jazz!\nWhat is your name?");
        String playerName = sc.nextLine();
        System.out.println("Hello " + playerName + "! We will play some blackjack. We will start you off with $1000");

        while (!stop) {
            System.out.println("How much would you like to bet? (Must be divisible by 5 and not more than your current money)");
            int bet = bet(player.getMoney());
            System.out.println("Let's Deal!");

            List<Card> playerHand = new ArrayList<>();
            drawCards(deck, playerHand, 2);

            System.out.println("Your initial hand: " + playerHand);
            int initialScore = calculateScore(playerHand);
            System.out.println("Your initial score: " + initialScore);

            if (initialScore == 21) {
                System.out.println("Blackjack! You win!");
                player.setMoney(player.getMoney() + bet * 2);
            } else {
                if (playerHand.size() == 2 && playerHand.get(0).getValue() == playerHand.get(1).getValue()) {
                    System.out.println("You have a pair! Do you want to split? (y/n)");
                    String splitDecision = sc.nextLine();
                    if (splitDecision.equalsIgnoreCase("y")) {
                        boolean splitResult = split(player, deck, playerHand, bet);
                        if (splitResult) {
                            System.out.println("Do you want to play another round? (y/n)");
                            String anotherRound = sc.nextLine();
                            if (!anotherRound.equalsIgnoreCase("y")) {
                                stop = true;
                            }
                            continue;
                        }
                    }
                }

                System.out.println("Do you want to double down? (y/n)");
                String doubleDownDecision = sc.nextLine();
                if (doubleDownDecision.equalsIgnoreCase("y")) {
                    bet = doubleDown(player, deck, playerHand, bet);
                    if (calculateScore(playerHand) <= 21) {
                        List<Card> dealerHand = new ArrayList<>();
                        drawCards(deck, dealerHand, 2);
                        int dealerValue = dealer(deck, dealerHand);

                        System.out.println("Dealer's final hand: " + dealerHand);
                        System.out.println("Dealer's final score: " + dealerValue);

                        if (dealerValue > 21 || calculateScore(playerHand) > dealerValue) {
                            System.out.println("You win!");
                            player.setMoney(player.getMoney() + bet);
                        } else if (calculateScore(playerHand) < dealerValue) {
                            System.out.println("Dealer wins!");
                            player.setMoney(player.getMoney() - bet);
                        } else {
                            System.out.println("It's a tie!");
                        }

                        System.out.println("Your money left: $" + player.getMoney());
                    } else {
                        System.out.println("BUST! You lost.");
                        int money = player.getMoney() - bet;
                        System.out.println("Your money left: $" + money);
                    }
                } else {
                    int playerValue = hitOrStand(player, deck, playerHand, bet);

                    if (playerValue <= 21) {
                        System.out.println("Your final hand: " + playerHand);
                        System.out.println("Your final score: " + playerValue);
                        System.out.println("Your money left: $" + player.getMoney());

                        List<Card> dealerHand = new ArrayList<>();
                        drawCards(deck, dealerHand, 2);
                        int dealerValue = dealer(deck, dealerHand);

                        System.out.println("Dealer's final hand: " + dealerHand);
                        System.out.println("Dealer's final score: " + dealerValue);

                        if (dealerValue > 21 || playerValue > dealerValue) {
                            System.out.println("You win!");
                            player.setMoney(player.getMoney() + bet);
                        } else if (playerValue < dealerValue) {
                            System.out.println("Dealer wins!");
                            player.setMoney(player.getMoney() - bet);
                        } else {
                            System.out.println("It's a tie!");
                        }

                        System.out.println("Your money left: $" + player.getMoney());
                    }
                }
            }

            if (player.getMoney() <= 0) {
                System.out.println("You are out of money. Game over.");
                break;
            }

            System.out.println("Do you want to play another round? (y/n)");
            String anotherRound = sc.nextLine();
            if (!anotherRound.equalsIgnoreCase("y")) {
                stop = true;
            }
        }
    }
}
