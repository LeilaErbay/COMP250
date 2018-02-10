/* STUDENT: LEILA ERBAY
 * ID: 260672158
 */


import java.util.*;

class CardDeck {
    LinkedList<Integer> deck;

    // constructor, creates a deck with n cards, placed in increasing order
    CardDeck(int n) {
	deck = new LinkedList<Integer> ();
	for (int i=1;i<=n;i++) deck.addLast(new Integer(i));
    }

    // executes the card trick
    public void runTrick() {
	while (!deck.isEmpty()) {
	    // remove the first card and remove it
	    Integer topCard = deck.removeFirst();
	    System.out.println("Showing card "+topCard);

	    // if there's nothing left, we are done
	    if (deck.isEmpty()) break;
	    
	    // otherwise, remove the top card and place it at the back.
	    Integer secondCard = deck.removeFirst();
	    deck.addLast(secondCard);

	    System.out.println("Remaining deck: "+deck);

	}
    }
    public void setupDeck(int n) {
    
    	//create  a new list that will create a deck that is out of order for card trick to work
    	LinkedList newDeck = new LinkedList <Integer>();
    	
    	//set the original deck to the empty deck
    	this.deck = newDeck;
    		//counting down from the input value
    		for (;n>1;n--) {
    			//add the greatest value of the input to the first position of the list
    			this.deck.addFirst(n);
    			//this is equivalent to a right shift of the list
    			this.deck.addFirst(this.deck.removeLast());
    		}
    		//add one at the end of the deck to ensure the deck is properly disordered
    		this.deck.addFirst(1);

    }
   
    public static void main(String args[]) {
	// this is just creating a deck with cards in increasing order, and running the trick. 
	CardDeck d = new CardDeck(10);
	d.runTrick();

	// this is calling the method you are supposed to write, and executing the trick.
	CardDeck e = new CardDeck(0);
	e.setupDeck(0);
	e.runTrick();
    }
}

    
