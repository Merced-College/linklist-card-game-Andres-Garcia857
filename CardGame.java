/**
 * CardGame.java
 * @author Andres Garcia
 * @since 11/23/2024
 * This class uses LinkedLists and ArrayLists to play BlackJack
*/

//package linkedLists;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class CardGame {
	
	private static LinkList cardList = new LinkList();  // make list

	public static void main(String[] args) {

		// File name to read from
        String fileName = "cards.txt"; // Ensure the file is in the working directory or specify the full path
        
        // Scanner to read user's input
        Scanner scan = new Scanner(System.in);

        // Read the file and create Card objects
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into components
                String[] details = line.split(","); // Assuming comma-separated values
                if (details.length == 4) {
                    // Parse card details
                    String suit = details[0].trim();
                    String name = details[1].trim();
                    int value = Integer.parseInt(details[2].trim());
                    String pic = details[3].trim();

                    // Create a new Card object
                    Card card = new Card(suit, name, value, pic);

                    // Add the Card object to the list
                    cardList.add(card);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
		
        // List of cards and their total values for the player and dealer
		ArrayList<Card> playerHand = new ArrayList<Card>();
        int playerTotal = 0;
        ArrayList<Card> dealerHand = new ArrayList<Card>();
        int dealerTotal = 0;

        // Adds two cards to the player's and dealer's while tracking their total
		for(int i = 0; i < 2; i++){
			playerHand.add(cardList.getFirst());
            dealerHand.add(cardList.getFirst());

            playerTotal += playerHand.get(i).getCardValue();
            dealerTotal += dealerHand.get(i).getCardValue();
        }

        // User's turn
        playerTotal = playerTurn(scan, playerHand, playerTotal);
        if (playerTotal > 21) {
            System.out.println("You busted! Dealer wins.");
            return;
        }

        // Dealer's turn
        dealerTotal = dealerTurn(dealerHand, dealerTotal);
        determineWinner(playerTotal, dealerTotal);

        scan.close();
	}//end main

    // Allows the user to hit, adding the card to their hand, ending when they stand or go over
    public static int playerTurn(Scanner scanner, ArrayList<Card> playerHand, int playerTotal) {
        while (true) {
            System.out.println("Your total is " + playerTotal + ". Do you want to hit or stand?");
            String action = scanner.nextLine().toLowerCase();
            // If user hit, grab card from deck and add it to the player's hand
            if (action.equals("hit")) {
                Card newCard = cardList.getFirst();
                playerHand.add(newCard);
                playerTotal += newCard.getCardValue();
                System.out.println("You drew a " + newCard.getCardValue() + " of " + newCard.getCardSuit());
                if (playerTotal > 21) {
                    break; // If the user's hand goes over, stop the turn
                }
            } else if (action.equals("stand")) {
                break; // Else if the user stands, stop the turn
            } else {
                // Else, request the user to input again
                System.out.println("Invalid action. Please type 'hit' or 'stand'.");
            }
        }
        return playerTotal; // Return the updated player's hand's value
    }// end playerTurn

    // Allows the dealer to hit, adding the card to their hand, ending when they stand or go over
    public static int dealerTurn(ArrayList<Card> dealerHand, int dealerTotal) {
        // While the dealer's hand isn't over 17, add card to their hand
        while (dealerTotal < 17) {
            Card newCard = cardList.getFirst();
            dealerHand.add(newCard);
            dealerTotal += newCard.getCardValue();
        }
        System.out.println("Dealer's total is " + dealerTotal);
        return dealerTotal; // Return the updated dealer's hand's value
    }// end dealerTurn

    // Decide the winner between the player and the dealer
    public static void determineWinner(int playerTotal, int dealerTotal) {
        // If the dealer's hand went over or the player's hand is closer to 21, the player wins
        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            System.out.println("You win!");
        } else if (dealerTotal == playerTotal) {
            // Else if the player's and dealer's total is the same, it's a tie
            System.out.println("It's a tie!");
        } else {
            // Else, the dealer wins
            System.out.println("Dealer wins!");
        }
    }// end determineWinner
}//end class
