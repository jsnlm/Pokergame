
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
/** 
	This class is the main program of the game. All GUI components are found here. Parts of the game are organized
	into steps. When the player is done with a step, another step will load. It makes it easier to load a completely different panel
	instead of repainting all of the existing GUI
	@author Raymond and Jason
 */
public class PokerGame{

	// Initialize all swing objects.
	private JFrame f = new JFrame("Poker Game"); //create Frame

	// Create the Menu bar at top of frame
	private JMenuBar mb = new JMenuBar(); 									// Menubar
	private JMenu mnuFile = new JMenu("File"); 								// File Entry on Menu bar
	private JMenuItem mnuItemNewGame = new JMenuItem("New Game"); 			// Quit sub item
	private JMenuItem mnuItemResumeGame = new JMenuItem("Resume Game"); 	// Quit sub item
	private JMenuItem mnuItemQuit = new JMenuItem("Quit"); 							// Help Menu entry

	//Create panels to represents steps of the game
	JPanel steps; 
	private JPanel pnlStep0 = new JPanel();
	private JPanel pnlStep1 = new JPanel(); 
	private JPanel pnlStep2 = new JPanel(); 
	private JPanel pnlStep3 = new JPanel(); 
	private JPanel pnlStep4 = new JPanel();
	final static String STEP0 = "Page 0";
	final static String STEP1 = "Page 1";
	final static String STEP2 = "Page 2";
	final static String STEP3 = "Page 3";
	final static String STEP4 = "Page 4";

	//Two players for game. User and Dealer.
	private Player player;
	private Player dealer;

	//Displays on the Panels.
	int bet = 0;
	double playerHand = 0;
	double dealerHand = 0;
	String playerHandName;
	String dealerHandName;

	//For high scores
	String highScoreName = ("Raymond Zhuang");
	Double highScoreCoins = null;


	//Used to remove the hassle of changing the image paths from computer to computer
	private String fileDir;

	//Buttons used in the game
	private JButton btnStart = new JButton("Start");
	private JButton btnBet = new JButton("Bet");
	private JButton btnNext = new JButton("Next");
	private JButton btnNextRound = new JButton("Next Round");
	private JButton btnFinish = new JButton("Finish");
	private JButton btnEnd = new JButton ("End");
	private JButton btnGameOver = new JButton ("Restart");
	
	//TextFields for entering player name
	JTextField textField = new JTextField("Player", 20);

	//Check boxes to indicate switching cards.
	JCheckBox cb1 = new JCheckBox("Change Card 1                    ");
	JCheckBox cb2 = new JCheckBox("Change Card 2                    ");
	JCheckBox cb3 = new JCheckBox("Change Card 3                    ");
	JCheckBox cb4 = new JCheckBox("Change Card 4                    ");
	JCheckBox cb5 = new JCheckBox("Change Card 5      ");   

	// Images
	private ArrayList<ImageIcon> mCards = new ArrayList<ImageIcon>();



	// Constructor for the GUI 
	public PokerGame(){
		//Create two instances or objects of class Player
		player = new Player();
		dealer = new Player();

		// initialised 6 card image icon
		// Create 6 cards using 6 different images 

		System.out.println("System.getProperty " + System.getProperty("user.dir") );
		fileDir = System.getProperty("user.dir").replace("\\", "/") + "/src/images/" ;
		System.out.println("imageDir " + fileDir );

		mCards.add(new ImageIcon(fileDir + "Card1.gif"));
		mCards.add(new ImageIcon(fileDir + "Card2.gif"));
		mCards.add(new ImageIcon(fileDir + "Card3.gif"));
		mCards.add(new ImageIcon(fileDir + "Card4.gif"));
		mCards.add(new ImageIcon(fileDir + "Card5.gif"));
		mCards.add(new ImageIcon(fileDir + "Card6.gif"));

		// Set MenuBar
		f.setJMenuBar(mb);

		//Build Menus
		mnuFile.add(mnuItemNewGame); 		// Create New g=Game option
		mnuFile.add(mnuItemResumeGame);  	// Create Resume Game option
		mnuFile.add(mnuItemQuit); 			// Create Quit option
		mb.add(mnuFile);        			// Add Menu items to MenuBar
		// Setup Main Frame
		f.getContentPane().setLayout(new BorderLayout());

		//Steps:------------------------------

		//Load Step 0
		loadStep0();


		//Add panels that contain the steps.
		steps = new JPanel(new CardLayout());
		steps.add(pnlStep0, STEP0);
		steps.add(pnlStep1, STEP1);
		steps.add(pnlStep2, STEP2);
		steps.add(pnlStep3, STEP3);
		steps.add(pnlStep4, STEP4);

		//Add panels to the frame
		f.getContentPane().add(steps, BorderLayout.CENTER);        

		//Add Menu listener to make buttons do something      
		mnuItemNewGame.addActionListener(new ListenMenuNewGame());
		mnuItemResumeGame.addActionListener(new ListenMenuResumeGame());
		mnuItemQuit.addActionListener(new ListenMenuQuit());
		//mnuItemInstruction.addActionListener(new ListenMenuInstruction());

		btnStart.addActionListener(new ListenButtonStart());    
		btnBet.addActionListener(new ListenButtonBet());
		btnNext.addActionListener(new ListenButtonNext());
		btnNextRound.addActionListener(new ListenButtonNextRound());
		btnFinish.addActionListener(new ListenButtonFinish());
		btnEnd.addActionListener(new ListenButtonEnd());
		btnGameOver.addActionListener(new ListenButtonRestart());

	}
	//Get image icons by passing index
	public ImageIcon getImageIcon(int i)
	{	if (i < 6) {
		return mCards.get(i);
	} else {
		return null;
	}
	}
	// Load Step0 and its contents
	public void loadStep0() {


		//Use to retrieve the highscores from a text file

		Scanner s2 = null;
		try {

			s2 = new Scanner(new BufferedReader(new FileReader(fileDir + "HighScore.txt")));
			highScoreName = s2.nextLine();
			highScoreCoins = s2.nextDouble();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (s2 != null) {
				s2.close();
			}
		}
		
		//The title of the game 
		JLabel lblTitle = new JLabel("SHAPE POKER");
		lblTitle.setFont(new Font("Bauhaus 93", Font.BOLD, 72));
		
		JLabel lblBeginning = new JLabel("Click New Game or Resume Game to begin under, File...");
		lblBeginning.setFont(new Font("Times New Roman", Font.BOLD, 20));	

		JLabel highScore = new JLabel("| High Score: " + highScoreName + " $" + highScoreCoins + " |");    	
		highScore.setFont(new Font("Serif", Font.PLAIN, 20));
		
		pnlStep0.add(new JLabel("============================================================================================================================="));
		pnlStep0.add(lblTitle);
		pnlStep0.add(new JLabel("============================================================================================================================="));
		pnlStep0.add(lblBeginning);
		pnlStep0.add(new JLabel("============================================================================================================================="));
		pnlStep0.add(highScore);
		pnlStep0.add(new JLabel("============================================================================================================================="));

	}

	// Load Step1 and its contents
	public void loadStep1() {
		pnlStep1.add(new JLabel("========================================================="));
		pnlStep1.add(btnStart);  
		pnlStep1.add(new JLabel("========================================================="));
		pnlStep1.add(new JLabel(new ImageIcon(fileDir + "Instructions.gif")));
	}

	// Load Step2 and its contents
	public void loadStep2() {    	    	

		JLabel lblRounds = new JLabel("|  Round " + player.getRounds() + " ");
		lblRounds.setForeground(Color.RED);
		JLabel lblCoins = new JLabel("|  Money $" + player.getCoins() + " ");
		lblCoins.setForeground(Color.RED);
		JLabel lblWins = new JLabel("|  Win Counter: " + player.getWins() + " ");
		lblWins.setForeground(Color.RED);
		JLabel lblBet = new JLabel("|  Bet "+bet+" Coins |");
		lblBet.setForeground(Color.RED);

		lblRounds.repaint();
		pnlStep2.add(lblRounds);

		lblCoins.repaint();
		pnlStep2.add(lblCoins);

		lblWins.repaint();
		pnlStep2.add(lblWins);

		lblBet.repaint();
		pnlStep2.add(lblBet);

		System.out.println("Bet "+bet+" Coins");
		System.out.println("Win Counter: " + player.getWins());

		JLabel lText=new JLabel("SELECT WHICH CARDS YOU WANT TO SWITCH OUT. YOU CAN BET UP TO 10 DOLLARS.");
		lText.setFont(new Font("Times New Roman", Font.PLAIN,12));
		pnlStep2.add(lText);

		//pnlStep2.add(new JLabel("Dealer: "));
		pnlStep2.add(new JLabel("==========================================================="));	
		pnlStep2.add(new JLabel("DEALER"));
		pnlStep2.add(new JLabel("==========================================================="));
		pnlStep2.add(new JLabel(new ImageIcon(fileDir + "CardBack.gif")));
		pnlStep2.add(new JLabel(new ImageIcon(fileDir + "CardBack.gif")));
		pnlStep2.add(new JLabel(new ImageIcon(fileDir + "CardBack.gif")));
		pnlStep2.add(new JLabel(new ImageIcon(fileDir + "CardBack.gif")));
		pnlStep2.add(new JLabel(new ImageIcon(fileDir + "CardBack.gif")));

		//pnlStep2.add(new JLabel("Player: "));
		pnlStep2.add(new JLabel("============================================================"));	
		pnlStep2.add(new JLabel("YOU"));
		pnlStep2.add(new JLabel("============================================================"));
		pnlStep2.add(new JLabel(getImageIcon(player.getCard1())));
		pnlStep2.add(new JLabel(getImageIcon(player.getCard2())));
		pnlStep2.add(new JLabel(getImageIcon(player.getCard3())));
		pnlStep2.add(new JLabel(getImageIcon(player.getCard4())));
		pnlStep2.add(new JLabel(getImageIcon(player.getCard5()))); 	 	
		pnlStep2.add(new JLabel("============================================================================================================================="));

		// add image and check box

		pnlStep2.add(new JLabel("    "));
		pnlStep2.add(cb1);
		pnlStep2.add(cb2);
		pnlStep2.add(cb3);
		pnlStep2.add(cb4);
		pnlStep2.add(cb5);

		pnlStep2.add(btnBet);
		pnlStep2.add(btnNext); 

	}
	public void loadStep3() {	

		pnlStep3.add(new JLabel("=============================================================================================================================")); 	
		pnlStep3.add(new JLabel("==========================================================="));
		pnlStep3.add(new JLabel("DEALER"));
		pnlStep3.add(new JLabel("==========================================================="));

		pnlStep3.add(new JLabel(getImageIcon(dealer.getCard1())));
		pnlStep3.add(new JLabel(getImageIcon(dealer.getCard2())));
		pnlStep3.add(new JLabel(getImageIcon(dealer.getCard3())));
		pnlStep3.add(new JLabel(getImageIcon(dealer.getCard4())));
		pnlStep3.add(new JLabel(getImageIcon(dealer.getCard5())));

		pnlStep3.add(new JLabel("============================================================"));	
		pnlStep3.add(new JLabel("YOU"));
		pnlStep3.add(new JLabel("============================================================"));
		pnlStep3.add(new JLabel(getImageIcon(player.getCard1())));
		pnlStep3.add(new JLabel(getImageIcon(player.getCard2())));
		pnlStep3.add(new JLabel(getImageIcon(player.getCard3())));
		pnlStep3.add(new JLabel(getImageIcon(player.getCard4())));
		pnlStep3.add(new JLabel(getImageIcon(player.getCard5())));
		pnlStep3.add(new JLabel("============================================================================================================================="));

		//Rounds the numbers to 3 decimal places    	
		DecimalFormat df = new DecimalFormat("#.###");

		//Displays the number of points for each hand
		pnlStep3.add(new JLabel("| Dealer's Hand: " + dealerHandName + " |"));
		pnlStep3.add(new JLabel("| Your Hand: " + playerHandName + " |"));
		pnlStep3.add(new JLabel("============================================================================================================================="));
		
		//System.out.println("Dealer's Hand: " + dealer.handName() + " |");  
		//System.out.println("Your Hand: " + player.handName() + " |"); 
		
		pnlStep3.add(new JLabel("| Dealer's Hand Value: " + df.format(dealer.getRanks()) + " Points |"));
		pnlStep3.add(new JLabel(" Your Hand Value: " + df.format(player.getRanks()) + " Points |"));
		pnlStep3.add(new JLabel("============================================================================================================================="));

		// check who is the winner
		if (dealer.getRanks() > player.getRanks())
		{
			JLabel lose = new JLabel("Sorry!\n You lost the round!\n ");
			lose.setForeground(Color.RED);
			pnlStep3.add(lose);

			System.out.println("You lost the round!"); 
			System.out.println("You have lost : " + bet + " Dollars");
			pnlStep3.add(new JLabel("You have lost : " + bet + " Dollars from your bet"));
			pnlStep3.add(new JLabel("============================================================================================================================="));
			player.setWins(player.getWins() - 1);
			if (player.getWins() <= -1)	{
				player.setWins(0);
			}
		}
		else if (dealer.getRanks() == player.getRanks())
		{
			JLabel draw = new JLabel("Draw!\n You didn't win any money!\n ");
			draw.setForeground(Color.DARK_GRAY);
			pnlStep3.add(draw);

			pnlStep3.add(new JLabel("Draw!\n You didn't win any money!\n "));
			pnlStep3.add(new JLabel("============================================================================================================================="));

			System.out.println( "Draw");
			System.out.println( "You didn't win any money");
			player.setCoins(player.getCoins() + bet);
		}
		else if (dealer.getRanks() < player.getRanks())
		{
			JLabel win = new JLabel("Congratulations!\n You won the round!\n ");
			win.setForeground(Color.GREEN);
			pnlStep3.add(win);

			System.out.println("Congratulations!");
			System.out.println("You won the round!");
			player.setCoins( player.getCoins() + playerHand * bet); 
			System.out.println("You have won : " + playerHand * bet);

			pnlStep3.add(new JLabel("You have won : " + playerHand * bet));
			pnlStep3.add(new JLabel("============================================================================================================================="));
			player.setWins(player.getWins() + 1);
		}


		//If all 20 rounds are completed, Button Next Round is removed and replaced by Button Finish
		if (player.getRounds() >= 20 && player.getCoins() > 0) 
		{	
			//How many coins you have at the end of the round
			System.out.println("You now have : $" +  player.getCoins());
			pnlStep3.add(new JLabel("You now have : $" +  player.getCoins()));
			pnlStep3.add(btnFinish);    		
		}
		else if (player.getRounds() < 20 && player.getCoins() > 0) 
		{	
			//How many coins you have at the end of the round
			System.out.println("You now have : $" +  player.getCoins());
			pnlStep3.add(new JLabel("You now have : $" +  player.getCoins()));
			pnlStep3.add(btnNextRound);    		
		}
		else
		{	
			//If player runs out of money

			JLabel gameOver = new JLabel("| GAME OVER | You ran out of money D:");
			gameOver.setForeground(Color.RED);
			gameOver.setFont(new Font("Serif", Font.BOLD, 30));
			pnlStep3.add(gameOver);
			pnlStep3.add(btnGameOver);
		}

	}
	public void loadStep4() {

		Scanner s3 = null;
		try {

			s3 = new Scanner(new BufferedReader(new FileReader(fileDir + "HighScore.txt")));
			highScoreName = s3.nextLine();
			highScoreCoins = s3.nextDouble();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (s3 != null) {
				s3.close();
			}
		}

		JLabel endLine1 = new JLabel ("CONGRATULATIONS!");
		endLine1.setFont(new Font("Brush Script MT", Font.PLAIN, 72));
		endLine1.setForeground(Color.ORANGE);
		JLabel endLine2 = new JLabel ("You have successfully completed the game!");
		endLine2.setFont(new Font("Brush Script MT", Font.PLAIN, 30));
		JLabel endLine3 = new JLabel(" You have finished with ");
		endLine3.setFont(new Font("Calibri (Body)", Font.PLAIN, 20));
		JLabel endLine4 = new JLabel ("$" + player.getCoins());
		endLine4.setFont(new Font("Bauhaus 93", Font.PLAIN, 36));

		pnlStep4.add(endLine1);
		pnlStep4.add(endLine2);
		pnlStep4.add(endLine3);
		pnlStep4.add(endLine4);

		//Determines if the player beat the set high score
		if (highScoreCoins < player.getCoins()) {
			JLabel endLine5 = new JLabel ("Please enter your name...    ");
			endLine5.setFont(new Font("Calibri (Body)", Font.PLAIN, 20));

			JLabel endLine6 = new JLabel ("|A new Highscore!|");
			endLine6.setFont(new Font("Calibri (Body)", Font.PLAIN, 20));
			System.out.println("highscore");
			System.out.println(highScoreCoins);

			pnlStep4.add(endLine6); 
			pnlStep4.add(endLine5);
			pnlStep4.add(textField);
		}  	else { 
			JLabel endLine7 = new JLabel ("|Sorry, you did not beat the Highscore|");
			System.out.println("no highscore");
			endLine7.setFont(new Font("Calibri (Body)", Font.PLAIN, 20));
			pnlStep4.add(endLine7);
		}   

		pnlStep4.add(btnEnd);
	}

	//Makes the appropriate number of coins to be bet next round based on the win counter
	private void setBetByWins(){    	

		int wins = player.getWins();

		if (wins <= 2){
			bet = 1;
			player.setCoins(player.getCoins() - 1);
		}
		else if (wins > 2 && wins <= 5){
			bet = 2;
			player.setCoins(player.getCoins() - 2);
		}
		else if (wins > 5 && wins <= 8){
			bet = 3;
			player.setCoins(player.getCoins() - 3);
		}
		else if (wins > 8){
			bet = 4;
			player.setCoins(player.getCoins() - 4);    	
		}
		
		if (player.getCoins() < 0){
			player.setCoins(player.getCoins() + bet);
			bet = 1;
		}
	}

	public class ListenMenuInstruction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.out.println("Instructions");         
		}
	}

	public class ListenMenuNewGame implements ActionListener{
		public void actionPerformed(ActionEvent e){

			System.out.println("New Game");
			
			//Sets the initial values
			bet = 1;
			player.setWins(0);
			player.setCoins(40);
			player.setRounds(1);

			//Erases data in the SaveData.txt
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter(fileDir + "SaveData.txt"));
				out.write("0.0");	
				out.newLine();
				out.write("0");
				out.newLine();
				out.write("0");
			} catch (IOException e1) {
				e1.printStackTrace();

			} finally {
				//Close the BufferedWriter
				try {
					if (out != null) {
						out.flush();
						out.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			//Used as initial bet
			setBetByWins();
			
			//Load the next step
			pnlStep1.removeAll();
			steps.remove(pnlStep1);
			loadStep1();
			steps.add(pnlStep1,STEP1);

			CardLayout cl = (CardLayout)(steps.getLayout());
			cl.show(steps, STEP1);           
		}
	}

	public class ListenMenuResumeGame implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.out.println("Resume Game");  

			Scanner s = null;
			try {

				s = new Scanner(new BufferedReader(new FileReader(fileDir + "SaveData.txt")));
				player.setCoins(s.nextDouble());
				player.setRounds(s.nextInt());
				player.setWins(s.nextInt());
				player.setCoins(player.getCoins() + 1);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} finally {
				if (s != null) {
					s.close();
				}
			}

			if (player.getRounds() == 0){

				System.out.println("Sorry! There is no saved data");
				pnlStep0.removeAll();
				steps.remove(pnlStep0);
				loadStep0();
				JLabel noData = new JLabel("Sorry! There is no saved data");
				pnlStep0.add(noData);
				steps.add(pnlStep0,STEP0);

				player.setCoins(50);
				player.setRounds(1);


				CardLayout cl = (CardLayout)(steps.getLayout());
				cl.show(steps, STEP0);
			}
			else{            
				//Used for the initial bet amount
				setBetByWins();
				
				//Load the next step
				pnlStep1.removeAll();
				steps.remove(pnlStep1);
				loadStep1();
				steps.add(pnlStep1,STEP1);

				CardLayout cl = (CardLayout)(steps.getLayout());
				cl.show(steps, STEP1);
			}
		}
	}

	public class ListenButtonNext implements ActionListener{
		public void actionPerformed(ActionEvent e){
			// Get label of the button clicked in event passed in
			String arg = e.getActionCommand();    
			if (arg.equals("Next")) {
				System.out.println("Go to Step3");
				Random r = new Random();
				if (cb1.isSelected()) {               	
					player.setCard1(r.nextInt(6));
					cb1.setSelected(false);
				}
				if (cb2.isSelected()) {               	
					player.setCard2(r.nextInt(6));
					cb2.setSelected(false);
				}
				if (cb3.isSelected()) {               	
					player.setCard3(r.nextInt(6));
					cb3.setSelected(false);
				}
				if (cb4.isSelected()) {               	
					player.setCard4(r.nextInt(6));
					cb4.setSelected(false);
				}
				if (cb5.isSelected()) {               	
					player.setCard5(r.nextInt(6));
					cb5.setSelected(false);

				}

				//Call PokerGameUtil to change dealer's card
				int[] dealerCardValueA = new int [5];
				dealerCardValueA[0] = dealer.getCard1() + 1;
				dealerCardValueA[1] = dealer.getCard2() + 1;
				dealerCardValueA[2] = dealer.getCard3() + 1;
				dealerCardValueA[3] = dealer.getCard4() + 1;
				dealerCardValueA[4] = dealer.getCard5() + 1;

				System.out.println("Dealer's: " + dealerCardValueA[0] + " " + dealerCardValueA[1] + " " + dealerCardValueA[2] + " " + dealerCardValueA[3] + " " + dealerCardValueA[4]);
				dealerCardValueA = PokerGameUtil.dealerAI(dealerCardValueA);
				System.out.println("Dealer's: " + dealerCardValueA[0] + " " + dealerCardValueA[1] + " " + dealerCardValueA[2] + " " + dealerCardValueA[3] + " " + dealerCardValueA[4]);

				dealer.setCard1(dealerCardValueA[0] - 1);
				dealer.setCard2(dealerCardValueA[1] - 1);
				dealer.setCard3(dealerCardValueA[2] - 1);
				dealer.setCard4(dealerCardValueA[3] - 1);
				dealer.setCard5(dealerCardValueA[4] - 1);

				//Call PokerGameUtil to calculate ranking
				int[] userCardValueA = new int [5];
				userCardValueA[0] = player.getCard1() + 1;
				userCardValueA[1] = player.getCard2() + 1;
				userCardValueA[2] = player.getCard3() + 1;
				userCardValueA[3] = player.getCard4() + 1;
				userCardValueA[4] = player.getCard5() + 1;
				
				//Gets all of the values including ranks and name of hand
				player.setRanks(PokerGameUtil.handRanker (userCardValueA ));
				
				System.out.println("Player Hand: " + PokerGameUtil.getHand());  
				
				playerHand = PokerGameUtil.getHand();
				System.out.println("player hand" + playerHand);
				
				dealer.setRanks(PokerGameUtil.handRanker (dealerCardValueA ));
				
				dealerHand = PokerGameUtil.getHand();
				System.out.println("dealer hand" + dealerHand);
				
				System.out.println("Dealer Hand: " + PokerGameUtil.getHand());   

				System.out.println("Dealer's Rank: " + dealer.getRanks());
				System.out.println("Player's Rank: " + player.getRanks());
				
				if (playerHand == 1.5){
					playerHandName = ("High Card");	
				}
				else if (playerHand == 2){
					playerHandName = ("One Pair");
				}
				else if (playerHand == 2.5){
					playerHandName = ("Two Pair");
				}
				else if (playerHand == 3){
					playerHandName = ("Triple");
				}
				else if (playerHand == 3.5){
					playerHandName = ("Full House");
				}
				else if (playerHand == 4){
					playerHandName = ("Quadruple");
				}
				else if (playerHand == 4.5){
					playerHandName = ("Quintuple");
				}
				
				if (dealerHand == 1.5){
					dealerHandName = ("High Card");	
				}
				else if (dealerHand == 2){
					dealerHandName = ("One Pair");
				}
				else if (dealerHand == 2.5){
					dealerHandName = ("Two Pair");
				}
				else if (dealerHand == 3){
					dealerHandName = ("Triple");
				}
				else if (dealerHand == 3.5){
					dealerHandName = ("Full House");
				}
				else if (dealerHand == 4){
					dealerHandName = ("Quadruple");
				}
				else if (dealerHand == 4.5){
					dealerHandName = ("Quintuple");
				}

				//Load the next step
				steps.remove(pnlStep3);
				pnlStep3.removeAll();
				loadStep3();
				steps.add(pnlStep3,STEP3);

				CardLayout cl = (CardLayout)(steps.getLayout());
				cl.show(steps, STEP3);
			}   
		}
	}

	public class ListenButtonStart implements ActionListener{
		public void actionPerformed(ActionEvent e){
			// Get label of the button clicked in event passed in
			String arg = e.getActionCommand();    
			if (arg.equals("Start")) {
				System.out.println("Go to Step1");
				
				//Load the next step
				pnlStep2.removeAll();
				steps.remove(pnlStep2);
				loadStep2();
				steps.add(pnlStep2,STEP2);

				CardLayout cl = (CardLayout)(steps.getLayout());
				cl.show(steps, STEP2);
			}   
		}
	}
	public class ListenButtonBet implements ActionListener{
		public void actionPerformed(ActionEvent e){
			// Get label of the button clicked in event passed in
			String arg = e.getActionCommand();    
			if (arg.equals("Bet")) {
				
				//Betting 
				bet = bet + 1;     
				System.out.println("Bet"+bet+"Coin");
				System.out.println("Player Coins before " + player.getCoins());
				player.setCoins(player.getCoins() - 1);
				System.out.println("Player Coins" + player.getCoins());
				
				//Cannnot bet over 10 dollars
				if (bet > 10){
					bet = bet - 1;
					player.setCoins(player.getCoins() + 1);
				}
				
				//Cannot bet if player has no money
				if (player.getCoins() < 0){
					bet = bet - 1;
					player.setCoins(player.getCoins() + 1);
				}

				//Refreshes the panel to accept new values
				pnlStep2.removeAll();
				steps.remove(pnlStep2);
				loadStep2();
				steps.add(pnlStep2,STEP2);

				CardLayout cl = (CardLayout)(steps.getLayout());
				cl.show(steps, STEP2);
			}   
		}
	}

	public class ListenButtonNextRound implements ActionListener{
		public void actionPerformed(ActionEvent e){
			// Get label of the button clicked in event passed in
			String arg = e.getActionCommand();    
			if (arg.equals("Next Round")) {
				System.out.println("Go to Step2");

				//Re-distributes random cards at next round to player
				Random r = new Random();                      	
				player.setCard1(r.nextInt(6));                               	
				player.setCard2(r.nextInt(6));                                         	
				player.setCard3(r.nextInt(6));                                            	
				player.setCard4(r.nextInt(6));                                            	
				player.setCard5(r.nextInt(6));

				//Re-distributes random cards at next round to dealer                                	
				dealer.setCard1(r.nextInt(6));                               	
				dealer.setCard2(r.nextInt(6));                                         	
				dealer.setCard3(r.nextInt(6));                                            	
				dealer.setCard4(r.nextInt(6));                                            	
				dealer.setCard5(r.nextInt(6));

				//Increase the rounds passed by 1
				player.setRounds(player.getRounds() + 1);

				//Makes the appropriate number of coins to be bet next round based on the win counter
				setBetByWins();
				BufferedWriter out = null;
				try {
					out = new BufferedWriter(new FileWriter(fileDir + "SaveData.txt"));
					out.write(String.valueOf(player.getCoins()));	
					out.newLine();
					out.write(String.valueOf(player.getRounds()));
					out.newLine();
					out.write(String.valueOf(player.getWins()));
				} catch (IOException e1) {
					//System.out.print("ERROR : " + e);
					e1.printStackTrace();

				} finally {
					//Close the BufferedWriter
					try {
						if (out != null) {
							out.flush();
							out.close();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}


				//Refreshes the panel to accept new cards
				steps.remove(pnlStep2);
				pnlStep2.removeAll();                
				loadStep2();
				steps.add(pnlStep2,STEP2);


				CardLayout cl = (CardLayout)(steps.getLayout());               
				cl.show(steps, STEP2);
			}   
		}
	}
	public class ListenButtonFinish implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String arg = e.getActionCommand();    
			if (arg.equals("Finish")) {
				
				//loads the end screen
				steps.remove(pnlStep4);
				pnlStep4.removeAll();                
				loadStep4();
				steps.add(pnlStep4,STEP4);

				CardLayout cl = (CardLayout)(steps.getLayout());               
				cl.show(steps, STEP4);
			}
		}
	}

	public class ListenButtonEnd implements ActionListener{
		public void actionPerformed(ActionEvent e ){
			String arg = e.getActionCommand();
			if (arg.equals("End")){

				//Determining if player beat the high score
				if (highScoreCoins < player.getCoins()){
					BufferedWriter out2 = null;
					try {
						out2 = new BufferedWriter(new FileWriter(fileDir + "HighScore.txt"));
						out2.write(textField.getText());	
						out2.newLine();
						out2.write(String.valueOf(player.getCoins()));
					} catch (IOException e1) {
						e1.printStackTrace();

					} finally {
						//Close the BufferedWriter
						try {
							if (out2 != null) {
								out2.flush();
								out2.close();
							}
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				} 

				//Loads the next step
				steps.remove(pnlStep0);
				pnlStep0.removeAll();                
				loadStep0();
				steps.add(pnlStep0,STEP0);

				CardLayout cl = (CardLayout)(steps.getLayout());               
				cl.show(steps, STEP0);
			}
		}
	}
	public class ListenButtonRestart implements ActionListener{
		public void actionPerformed(ActionEvent e ){
			String arg = e.getActionCommand();
			if (arg.equals("Restart")){   			
				
				//Restarts game if player loses
				steps.remove(pnlStep0);
				pnlStep0.removeAll();                
				loadStep0();
				steps.add(pnlStep0,STEP0);

				CardLayout cl = (CardLayout)(steps.getLayout());               
				cl.show(steps, STEP0);
			}
		}
	}
	
	public class ListenMenuQuit implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.out.println("Quit");
			System.exit(0);  
		}
	}
	public class ListenCloseWdw extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			System.exit(0);         
		}
	}

	public void launchFrame(){
		// Display Frame
		f.setLocation(50,50);
		Dimension minSize = new Dimension(900,800);
		f.setMinimumSize(minSize);
		f.setResizable(false);

		//f.setMinimumSize(minSize);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//f.pack(); //Adjusts panel to components for display

		f.setVisible(true);
	}

	public static void main(String args[]){
		PokerGame gui = new PokerGame();
		gui.launchFrame();

		//pnlCenter.add(mCards.get(mCards.size() - 1), BorderLayout.CENTER)

	}
}

