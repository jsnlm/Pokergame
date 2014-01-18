
import java.lang.Math;
import java.util.Random;
import java.util.Scanner;
import java.io.*;	

/**
	This class is for reading the values of each hand and deciding the value that they are worth. When the calculations are finished
	The values are returned to PokerGame.java . This class also is in charge of the deler AI. It makes the dealer choose which cards that
	it doesn't want
	@author Raymond and Jason
	
 */
public class PokerGameUtil {
	
	private static double hand = 0;


	
	public static double getHand() {
		return hand;
	}
	
	public static double rankTheRestOfTheSingles(int[] numberOfCards, double rank){
		for (int x=5; x>-1; x--) 	
		{
			if (numberOfCards[x]==1)
			{
				
				rank += (x+1)*(Math.pow(10,(x-8)));
			}
		}
	return rank;
	}	

	public static double handRanker ( int[] CardValueA)
	//********************************Ranks the player's hands	
	//------------------------Determines if it is a 5 of a kind
	{
		int doubleCounter;						// counts the number of doubles
		int[] NumberOfCardsA = new int [6];
		int loopCounter, secondaryLoopCounter;
		double rank=0;
		doubleCounter = 0;		
		
		for (int x=0; x<5; x++) 		
		{
			for (int y=0; y!=6; y++)
			{
				if (CardValueA[x] == (y+1))
				{
					NumberOfCardsA[y] += 1;
				}
			}
		}
		for (int x=0; x<6; x++) 		
		{			
			//------------------------Determines if it is a 5 of a kind
			if (NumberOfCardsA[x]==5)
			{
				rank = 7+(.1*(x+1));
				hand = 4.5;
				System.out.println("five of a kind");
			}
			//------------------------Determines if it is a 4 of a kind
			else if (NumberOfCardsA[x]==4)
			{
				rank = 6+(.1*(x+1));
				hand = 4;
				System.out.println("four of a kind");
			}
			//------------------------Determines if it is a triple	
		}
		for (int x=0; x<6; x++) 		
		{			
			if (NumberOfCardsA[x]==3)
			{
				rank = 4+(.1*(x+1));
				hand = 3;
				System.out.println("three of a kind");
				//--------------------If there is a triple, this will determine if it has a double.(Checks if it is a full house)
				for (int y=0; y!=6; y++) 		
				{			
					if (NumberOfCardsA[y]==2)
					{
						rank += 1 + (0.01*(y+1));
						hand = 3.5;
						System.out.println("three of a kind and a double");
					}
				}
			}
		}
		//------------------------Determines if it is a single pair or a double pair		
		for (int x=0; x<6; x++)
		{
			if (NumberOfCardsA[x] == 2)
			{
				doubleCounter += 1;
			}
		}

		if (rank == 0)
		{
			//------------------------Ranks hand if it is a single pair
			if (doubleCounter == 1)
			{
				for (int x=0; x<6; x++) 		
				{			
					if (NumberOfCardsA[x]==2)
					{
						rank = 1+(.1*(x+1));
						hand = 2;
						System.out.println("pair");
					}
				}
			}
			//------------------------Ranks hand if it is a two pair
			else if (doubleCounter == 2)
			{
				System.out.println("two pair");
				rank = 2;
				hand = 2.5;
				loopCounter = 5;
				secondaryLoopCounter = 1;
				while (loopCounter>-1)
				{			
					if (NumberOfCardsA[loopCounter] == 2)
					{
						rank += (Math.pow(10,(-secondaryLoopCounter)))*(loopCounter+1);
						secondaryLoopCounter+=1;
					}
					loopCounter = loopCounter-1;
				}
			}
		}
		

		rank  = rankTheRestOfTheSingles(NumberOfCardsA, rank);
		if (rank < 1)
		{
			hand = 1.5;
		}

		return rank;
	}

	public static int[] dealerAI ( int[] dealerCardValueA)	{	

		Random rand = new Random();		
		int secondLoopCounter, loopCounter;			// a counter used to keep track of the number of loops in a while loop
		int singlesCounter	;						// a counter used in determining the rank of a junk hand
		int[] dealerNumberOfCardsA = new int [6];	// the array number represents the card's value

			//********************************Changes the dealer's cards. 
			//------------------------Resets the number of cards there are so that the program can recount the number of each card
			for (int x=0; x<6;x++)
			{
				dealerNumberOfCardsA[x]=0;
			}
			//------------------------Counts how many of each card there are
			for (int x=0; x<5; x++) 		
			{
				for (int y=0; y!=6; y++)
				{
					if (dealerCardValueA[x] == (y+1))
					{
						dealerNumberOfCardsA[y] += 1;
					}
				}
			}

			//--------------------------------This will count the number of singles
			singlesCounter = 0;		
			for (int x=0; x<6; x++)
			{
				if (dealerNumberOfCardsA[x] == 1)
				{
					singlesCounter+=1;
				}
			} 
			//--------------------------------If there is a hand present, randomize all singles
			if (singlesCounter < 5)
			{
				//System.out.println("not singles");
				for (int y=0; y<6; y++) 	
				{
					if (dealerNumberOfCardsA[y] == 1)
					{

						for (int i=0; i<5; i++)
						{
							if (dealerCardValueA[i]==(y+1))
							{
								dealerCardValueA[i] = rand.nextInt(6) + 1;

							}
						}						
					}
				}
			}
			//--------------------------------If there are only singles, it will randomize all but the two best cards.
			else if (singlesCounter == 5)
			{
				loopCounter = 0;
				secondLoopCounter = 0;
				while (loopCounter<3  )
				{
					System.out.println("looping" + loopCounter);

					if (dealerNumberOfCardsA[secondLoopCounter] == 1)
					{
						for (int i=0; i<5; i++)
						{
							if (dealerCardValueA[i]==(secondLoopCounter))
							{
								dealerCardValueA[i] = rand.nextInt(6) + 1;
								loopCounter += 1;
							}
						}			
						
						//System.out.println(y + "was randomized ");
					}
					secondLoopCounter+=1;
				}
			}
			System.out.println("Dealer AI finished runnning");
			return dealerCardValueA;
	}

}	
