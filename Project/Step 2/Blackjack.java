import java.util.Scanner;
import java.util.ArrayList;

public class Blackjack{
	
	
	public static void main( String[] args ) {
		
		BlackjackPlayer dealer = new BlackjackPlayer("Dealer");
		Deck d = new Deck(1);
		Scanner kb = new Scanner(System.in);
		String user_name = "nick", choice = "";
		Boolean replay = true;
		Card c;
		int user_total, dealer_total;
		int pay_out_multiplier;
		
		
		System.out.println( "Welcome to Blackjack\n" );
// 		System.out.print( "What is your name? \n> " );
// 		user_name = kb.nextLine();
		BlackjackPlayer user = new BlackjackPlayer(user_name);
		
		initializeRound(d, user, dealer);
		
		user_total = user.findTotal();
		dealer_total = dealer.findTotal();
		
		// Check to see if either player received Blackjack		
		pay_out_multiplier = checkForBJ(user_total, dealer_total);
		if ( pay_out_multiplier != 2 ) {
			dealer.showHandAndTotal();
			d = finishRound( d, user, dealer );
			System.out.println ( pay_out_multiplier );
			// testing reloading deck			
// 			System.out.println( "Deck d: " + d.cards + "\nSize: " + d.cards.size() );
			System.exit(0);
		} else {
			pay_out_multiplier = 0;
		}
			
	
		// player's turn
		user_total = userTurn(d, user);		
		
		System.out.println( "\n" + user.name + " ended the round with " + user_total + ".\n" );

		// dealer's turn
		// dealer will only play if user has not busted			
		if ( user_total <= 21 ) {
			choice = "";
			System.out.println( "*******************\n** " + dealer.name + "'s turn **\n*******************\n" );
			while ( dealer_total <= 21 && ! choice.equals("stay") ) {
				// Dealer will hit until it has 17 or greater
				dealer.showHandAndTotal();
				if ( dealer_total < 17 ) {
					if ( dealer_total <= user_total && user_total <= 21 ) { 
						choice = "hit";
						c = d.dealCard();
						System.out.println( "\nDealer chooses to hit.\nHe draws a " + c + ".\n" );
						dealer.hand.add(c);
						dealer_total = dealer.findTotal();
					} else {
						choice = "stay";
					}
				}
				else
				{
					choice = "stay";
					System.out.println( "Dealer stays.\n" );
				}
			}
		}

		System.out.println( dealer.name + " ended the round with " + dealer_total + ".\n" );
		System.out.println( "******************\n** End of Round **\n******************\n" );

		// Determine the winner and reload the deck
		pay_out_multiplier = checkForWin( user_total, dealer_total );
		System.out.println( pay_out_multiplier );
		d = finishRound( d, user, dealer );
// testing reloading deck			
// 		System.out.println( "Deck d: " + d.cards + "\nSize: " + d.cards.size() );

	}
	
	
	
	/** 
	 * Shuffles the deck and deals two cards to each player
	 *
	 */
	public static void initializeRound( Deck d, BlackjackPlayer p, BlackjackPlayer dealer ) {
		int i;
		
		d.shuffle();
		
// Testing win logic
// 			dealer.hand.add(new Card("A", "S", 10));
// 			dealer.hand.add(new Card("J", "S", 10));
// 			p.hand.add(new Card("A", "S", 11));
// 			p.hand.add(new Card("J", "S", 10));

		// Deals 2 cards from top of deck to the players
		for (i = 0; i < 2; i++ )
			dealer.hand.add(d.dealCard()); 	
		for (i = 0; i < 2; i++ )
			p.hand.add(d.dealCard());

		p.showHandAndTotal();
		System.out.println( "\n=-=-=-=-=-=-=-=-=-=-=--=-=\n\n" + dealer.name + " has [" + dealer.hand.get(0) + ", **]" );
		System.out.println( dealer.name + "'s total is hidden.\n" );
// Testing BJ game logic
// 		dealer.showHandAndTotal();
// 		System.out.println( "Did you get blackjack: " + checkForBJ(p.findTotal()) );
// 		System.out.println( "Did the dealer: " + checkForBJ(dealer.findTotal()) );

			
	}
	
	/**
	 * Used after initial deal to see if a Blackjack was dealt	
	 * This will return a payout rate of 1, 0, -1 if one was dealt 
	 * otherwise it will return 2
	 */
	public static int checkForBJ(int u_total, int d_total) {	
		if ( u_total == 21 && d_total != 21 ) {
			System.out.println( "*******************\n** End of Round **\n*******************\n" );
			System.out.println( "You got Blackjack! You win this round.\n" );
			return 1;
		} else if ( u_total == 21 && d_total == 21 ) {
			System.out.println( "*******************\n** End of Round **\n*******************\n" );
			System.out.println( "You both got Blackjack. The round is a draw.\n" );		
			return 0;
		} else if ( d_total == 21 ) {
			System.out.println( "*******************\n** End of Round **\n*******************\n" );
			System.out.println( "Dealer got Blackjack. You lose this round.\n" );		
			return -1;
		} else {
			return 2;
		}
	}
	
	public static int userTurn( Deck d, BlackjackPlayer u ) {
		int u_total = u.findTotal();
		String choice = "";
		Scanner kb = new Scanner(System.in);
		Card c;
		
		System.out.println( "\n*****************\n** " + u.name + "'s turn **\n*****************\n" );
		while ( u_total <= 21 && ! choice.equals("stay") ) {
			System.out.println( "Would you like to \"hit\" or \"stay\"?" );
			u.showHandAndTotal();
			System.out.print( "> " );
			choice = kb.next();
		
			if ( choice.equals("hit") ) {
				c = d.dealCard();
				System.out.println( "\nYou drew a " + c + ".\n" );
				u.hand.add(c);
				u_total = u.findTotal();					
			} 	
		}
		return u_total;
	}

	/**
	 * Used at the end of the round to determine the payout rate
	 * Will return a payout rate of 1, 0, or -1
	 */ 
	public static int checkForWin(int u_total, int d_total) {
		if ( u_total <= 21 ) {
			if ( d_total <= 21 ) {
				if ( u_total > d_total ) {
					System.out.println( "You beat Dealer!\n" );
					return 1;
				} else if ( u_total == d_total ) {
					System.out.println( "You Drew.\n" );
					return 0;
				} else {
					System.out.println( "Dealer beat you\n" );
					return -1;
				}
			} else {
				System.out.println( "Dealer busted! You win!!\n" );
				return 1;
			}
		} else {
			System.out.println( "you busted\n" );
			return -1;
		}
	}
	
	/**
	 * Removes any cards from the two players hands and inserts them back into the deck
	 *
	 */
	public static Deck finishRound( Deck d, BlackjackPlayer u, BlackjackPlayer dealer ) {
		ArrayList<Card> clean_up = new ArrayList<Card>();
		Card c;
		
		clean_up = u.clearHand();
		while ( clean_up.size() > 0 ) {
			c = clean_up.remove(0);
			d.cards.add(c);
		}
		clean_up = dealer.clearHand();
		while ( clean_up.size() > 0 ) {
			c = clean_up.remove(0);
			d.cards.add(c);
		}
				
		
		return d;
	}

}