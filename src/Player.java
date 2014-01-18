import java.util.Random;

/** 
 	This class is used for the two players in the game. One for the dealer and one for the player.
 	Changes made to a player in terms of coins, rounds and things special to the player only are made here
 	The main program will then get the return values from this class. It also makes it easier to code and more
	@author Raymond and Jason
 */

public class Player {
	private double coins ;
	private int wins;
	private int rounds ;
	private int card1;
	private int card2;
	private int card3;
	private int card4;
	private int card5;
	private double ranks;

	
	//Create a constructor to assign values to property above
	public Player() {
		
		this.coins = 40;
		this.wins = 0;
		this.rounds = 1;
		Random r = new Random();
		this.card1 = r.nextInt(6);
		this.card2 = r.nextInt(6);
		this.card3 = r.nextInt(6);
		this.card4 = r.nextInt(6);
		this.card5 = r.nextInt(6);
		this.ranks = 0;
	
	}

	//Allow others to use the variables since the ones above are private
	
	public double getCoins() {
		return coins;
	}

	public void setCoins(double coins) {
		this.coins = coins;
	}
	
	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public int getCard1() {
		return card1;
	}

	public void setCard1(int card1) {
		this.card1 = card1;
	}

	public int getCard2() {
		return card2;
	}

	public void setCard2(int card2) {
		this.card2 = card2;
	}

	public int getCard3() {
		return card3;
	}

	public void setCard3(int card3) {
		this.card3 = card3;
	}

	public int getCard4() {
		return card4;
	}

	public void setCard4(int card4) {
		this.card4 = card4;
	}

	public int getCard5() {
		return card5;
	}

	public void setCard5(int card5) {
		this.card5 = card5;
	}
		
	public double getRanks() {
		return ranks;
	}

	public void setRanks(double ranks) {
		this.ranks = ranks;
	}

	

}
