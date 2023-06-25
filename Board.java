package Othello;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
public class Board {

	public static Position[][] positions = new Position[8][8];

	public static Player firstPlayer;
	public static Player secondPlayer;
	public static Player currentPlayer;
	public static Player otherPlayer;

	/*
	 * This constructor will take given Player values and set the 
	 * fields to them, while the integer will decide  what the 
	 * starting positions are and set the unplayable positions
	 * in the corners
	 */
	public Board(Player p1, Player p2, int start) {

		firstPlayer = p1;
		secondPlayer = p2;
		currentPlayer = p1;
		otherPlayer = p2;

		//Placing empty positions on board
		for (int row = 0; row<positions.length; row++) {
			for (int col = 0; col<positions[row].length; col++) {
				positions[row][col] = new PlayablePosition(Position.EMPTY);
			}
		}
		//Un-playable positions (corners)
		positions[0][0] = new UnplayablePosition();
		positions[0][7] = new UnplayablePosition();
		positions[7][0] = new UnplayablePosition();
		positions[7][7] = new UnplayablePosition();

		//Starting Positions
		if(start == 1) {
			positions[2][2].setPiece(Position.WHITE);
			positions[2][3].setPiece(Position.BLACK);
			positions[3][2].setPiece(Position.BLACK);
			positions[3][3].setPiece(Position.WHITE);
		}
		else if(start == 2) {
			positions[4][2].setPiece(Position.WHITE);
			positions[4][3].setPiece(Position.BLACK);
			positions[5][2].setPiece(Position.BLACK);
			positions[5][3].setPiece(Position.WHITE);
		}
		else if(start == 3) {
			positions[2][4].setPiece(Position.WHITE);
			positions[3][4].setPiece(Position.BLACK);
			positions[2][5].setPiece(Position.BLACK);
			positions[3][5].setPiece(Position.WHITE);
		}
		else if(start == 4) {
			positions[4][4].setPiece(Position.WHITE);
			positions[5][4].setPiece(Position.BLACK);
			positions[4][5].setPiece(Position.BLACK);
			positions[5][5].setPiece(Position.WHITE);
		}
	}
	
	
	/*
	 * This constructor will populate the board just like the first
	 * but will the pass its String parameter to the load method
	 * which will initialize all the saved positions as per the 
	 * text file
	 */
	public Board (String fileName) {
		for (int row = 0; row<positions.length; row++) {
			for (int col = 0; col<positions[row].length; col++) {
				positions[row][col] = new PlayablePosition(Position.EMPTY);
			}
		}
		positions[0][0] = new UnplayablePosition();
		positions[0][7] = new UnplayablePosition();
		positions[7][0] = new UnplayablePosition();
		positions[7][7] = new UnplayablePosition();
		loadGame(fileName);
	}
	
	public void play() {
		Scanner input = new Scanner(System.in);
		drawBoard();int ticker=0; int piecesOnBoard = 8;
		while(ticker!=2 && piecesOnBoard<63) {
/*
 * The game will keep going until the board is full or until
 * no players can make a move.
 */
			if(canPlayerMove() == true) {
//If the player can move, he will have 3 options
				System.out.print("1. Save Game\n2. Concede Game\n3. Make Move\n\n"
						+ currentPlayer.getName()+" ("+currentPlayer.getColour()+
						") please enter an option: ");
				int option = input.nextInt();

				if (option == 1) {
//This will save the game and quit using system.exit
					saveGame();
					System.out.print("Game has been saved and quit.");System.exit(0);}//Save
				else if(option == 2) {
//This option will give the win to the other player and quit
					System.out.print(currentPlayer.getName()+" has conceded.\n"
							+otherPlayer.getName()+" is the winner!");
				System.exit(0);}
				else if(option == 3) {
//This option makes a move, asks for the x and y coordinates and passes them
//to validMove. If it returns true, it passes them to makeMove. If not, it
//says the move is invalid and the player can pick and option again
					System.out.println(currentPlayer.getName()+" ("+currentPlayer.getColour()+
							") enter position:");
					int x = input.nextInt();
					int y = input.nextInt();
					if(validMove(x, y) == true) {
						makeMove(x, y);ticker =0;piecesOnBoard++;}
					else{System.out.println("\nInvalid move, try again\n");}}}
			else if(canPlayerMove() == false && piecesOnBoard<64) {
//If the player cannot make a move, they will have 3 options
				System.out.println("No valid moves!");
				System.out.print("1. Save Game\n2. Concede Game\n3. Forfeit Turn\n\n"
						+ "Please enter an option: ");
				int option = input.nextInt();
				if (option == 1) {
//Option 1 saves and quits the game
					saveGame();
					System.out.print("Game has been saved and quit.");System.exit(0);}
				else if(option == 2) {
//Option 2 concedes, gives win to the other and ends the game
					System.out.print(currentPlayer.getName()+" has conceded.\n"
							+otherPlayer.getName()+" is the winner!");
				System.exit(0);}
//Option 3 gives turn to the other player, redraws the board and adds 1 to the ticker
				else if(option == 3) {changePlayer();drawBoard();ticker++;}}
		}System.out.print(gameResult());input.close();

	}


	public void drawBoard() {
		System.out.println("\n\n  0\t1\t2\t3\t4\t5\t6\t7");
		for (int row = 0; row<positions.length; row++) {
			System.out.print(row+" ");
			for (int col = 0; col<positions[row].length; col++) {
				System.out.print(positions[row][col].getPiece()+"	");
			}
			System.out.print("\n\n");
		}
		System.out.println();
	}


	public void makeMove(int x, int y) {
//Sets the positions to current player's colour and uses all the check methods
		positions[y][x].setPiece(currentPlayer.getColour());
		checkLeft(x, y,true);
		checkRight(x, y,true);
		checkUp(x, y,true);
		checkDown(x, y,true);
		checkLeftUp(x, y,true);
		checkRightUp(x, y,true);
		checkLeftDown(x, y,true);
		checkRightDown(x, y,true);
//changes player and draws board
		changePlayer();
		drawBoard();}


	public void changePlayer() {
		if (currentPlayer == firstPlayer) {
			currentPlayer = secondPlayer;
			otherPlayer = firstPlayer;
		}
		else {currentPlayer = firstPlayer; otherPlayer = secondPlayer;}
	}

//All these methods check their own direction. Each is done individually
//so that they can be verified individually to see if they work.
	
	private boolean checkLeft(int x, int y, boolean check) {

		if(x == 0 || positions[y][x-1].getClass()== UnplayablePosition.class) {
			return false;
		}
		else{

			for (int i = (x-1); i>=0; i--) {
				if(positions[y][i].getPiece() == Position.EMPTY) {break;}
				if (positions[y][i].getPiece() == currentPlayer.getColour()
						&& positions[y][x-1].getPiece() == otherPlayer.getColour())

				{
					for (int j = x; j>i;j--) {
						if(check == true) {flip(j, y, currentPlayer);}
					}
					return true;
				}
			}	
		}
		return false;
	}
	private boolean checkRight(int x, int y, boolean check) {
		if(x== 7 || positions[x+1][y].getClass()== UnplayablePosition.class) {
			return false;
		}
		else{

			for (int i = (x+1); i<=7; i++) {
				if(positions[y][i].getPiece() == Position.EMPTY) {break;}
				if (positions[y][i].getPiece() == currentPlayer.getColour()
						&& positions[y][x+1].getPiece() == otherPlayer.getColour()) 
				{
					for (int j = x; j<i;j++) {
						if(check == true) {flip(j, y, currentPlayer);}
					}
					return true;
				}
			}	
		}
		return false;
	}
	private boolean checkUp(int x, int y, boolean check) {
		if(y == 7 || positions[y+1][x].getClass()== UnplayablePosition.class) {
			return false;
		}
		else {
			for (int i = (y+1); i<8; i++) {
				if(positions[i][x].getPiece() == Position.EMPTY) {break;}
				if(positions[i][x].getPiece() == currentPlayer.getColour()
						&& positions[y+1][x].getPiece() == otherPlayer.getColour()) {
					for (int j = y; j<i; j++) {
						if(check == true) {flip(x, j, currentPlayer);}
					}return true;
				}
			}
		}return false;
	}
	private boolean checkDown(int x, int y, boolean check) {
		if(y == 0 || positions[y-1][x].getClass()== UnplayablePosition.class) {
			return false;
		}
		else {
			for (int i = (y-1); i>=0; i--) {
				if(positions[i][x].getPiece() == Position.EMPTY) {break;}
				if(positions[i][x].getPiece() == currentPlayer.getColour()
						&& positions[y-1][x].getPiece() == otherPlayer.getColour()) {
					for (int j = y; j>i; j--) {
						if(check == true) {flip(x, j, currentPlayer);}
					}return true;
				}
			}
		}return false;
	}
	private boolean checkLeftUp(int x, int y, boolean check) {
		if(x==0 || y==0 || positions[y-1][x-1].getClass() == UnplayablePosition.class)
		{
			return false;
		}
		else {
			int i = y-1;
			for(int j = x-1; j>0; i--,j--) {
				if(i<0) {break;}
				if(positions[i][j].getPiece() == Position.EMPTY) {break;}
				if(positions[i][j].getPiece() == currentPlayer.getColour()
						&& positions[y-1][x-1].getPiece()==otherPlayer.getColour()) {
					int s = y;
					for (int t = x; t>j; s--,t--) {if(check == true) {flip(t, s, currentPlayer);}}
					return true;
				}
			}
			return false;
		}
	}
	private boolean checkRightUp(int x, int y, boolean check) {
		if(x == 7 || y==0 || positions[y-1][x+1].getClass() == UnplayablePosition.class)
		{return false;}
		else {
			int i = y-1;
			for(int j = x+1; j<8; i--,j++) {
				if(i<0) {break;}
				if(positions[i][j].getPiece() == Position.EMPTY) {break;}
				if(positions[i][j].getPiece() == currentPlayer.getColour()
						&& positions[y-1][x+1].getPiece()==otherPlayer.getColour()) {
					int s = y;
					for (int t = x; t<j; s--,t++) {if(check == true) {flip(t, s, currentPlayer);}}
					return true;
				}
			}
			return false;
		}
	}
	private boolean checkLeftDown(int x, int y, boolean check) {
		if(x == 0 || y==7 || positions[y+1][x-1].getClass() == UnplayablePosition.class)
		{return false;}
		else {
			int i = y+1;
			for(int j = x-1; j>0; i++,j--) {
				if(i>7) {break;}
				if(positions[i][j].getPiece() == Position.EMPTY) {break;}
				if(positions[i][j].getPiece() == currentPlayer.getColour()
						&& positions[y+1][x-1].getPiece()==otherPlayer.getColour()) {
					int s = y;
					for (int t = x; t>j; s++,t--) {if(check == true) {flip(t, s, currentPlayer);}}
					return true;
				}
			}
			return false;
		}
	}
	private boolean checkRightDown(int x, int y, boolean check) {
		if(x == 7 || y==7 || positions[y+1][x+1].getClass() == UnplayablePosition.class)
		{return false;}
		else {
			int i = y+1;
			for(int j = x+1; j<8; i++,j++) {
				if(i>7) {break;}
				if(positions[i][j].getPiece() == Position.EMPTY) {break;}
				if(positions[i][j].getPiece() == currentPlayer.getColour()
						&& positions[y+1][x+1].getPiece()==otherPlayer.getColour()) {
					int s = y;
					for (int t = x; t<j; s++,t++) {if(check == true) {flip(t, s, currentPlayer);}}
					return true;
				}
			}
			return false;
		}
	}

	private boolean validMove(int x, int y) {
		if(x<9&&y<9&&positions[y][x].canPlay()==true) {
//uses the check methods to check if a move is valid. As soon as one
//comes true, the method returns true
			if(checkLeft(x,y,false)==true||checkRight(x,y,false)==true||
					checkUp(x,y,false)==true||checkDown(x,y,false)==true||
					checkLeftUp(x,y,false)==true||checkRightUp(x,y,false)==true||
					checkLeftDown(x,y,false)==true||checkRightDown(x,y,false)==true) 
			{return true;}else {return false;}
		}else {return false;}
	}
	private boolean canPlayerMove() {
//Uses valid move methods for all open positions, returns
//true if one good spot is found
		boolean canMove = false;
		for (int row = 0; row<positions.length; row++) {
			for (int col = 0; col<positions[row].length; col++) {
				if(validMove(col,row) == true) {canMove =true;}
			}
		}return canMove;
	}
	private String gameResult() {
		int black=0;int white=0;
		for (int row = 0; row<positions.length; row++) {
			for (int col = 0; col<positions[row].length; col++) {
				if (positions[row][col].getPiece() == Position.BLACK)
				{black++;}
				else if(positions[row][col].getPiece() == Position.WHITE)
				{white++;}
			}
		}
		if(black == white) {return "The game is a tie!";}
		else if(black>white) {return firstPlayer.getName()+" is the winner!";}
		else{return secondPlayer.getName()+" is the winner!";}

	}
	private void flip(int x, int y, Player current) {
		positions[y][x].setPiece(current.getColour());
	}

	/*
	 * This saves the game as a .txt file. It saves the first second and
	 * current player. It then saves the board positions as characters.
	 * It also asks for a file name when executed. It will save to the
	 * current directory and overwrite a file if it has the same name
	 */
	private void saveGame() {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter a file name with no spaces: ");
		String fileName = input.next()+".txt";
		PrintStream output;
		try {
			output = new PrintStream(new File(fileName));
			output.println(firstPlayer.getName());
			output.println(secondPlayer.getName());
			output.println(currentPlayer.getName());
			for (int row = 0; row<positions.length;row++) {
				String s = "";
				for (int col = 0; col<positions[row].length; col++) {
					s+=positions[row][col].getPiece();
				}
				output.println(s);
			}output.close();
		}catch (FileNotFoundException e) {e.printStackTrace();}input.close();
	}

	/*
	 * The load method takes the file name as a parameter. It will pull the file 
	 * from the current directory and the first 3 lines are used for the player's names
	 * while the rest are added to a string. The string is then parsed and the character
	 * are matched with their Position value (Playable, Unplayable, black or white, etc)
	 */
	public static void loadGame(String fileName) {
		
		Scanner inputStream = null;
		
		try {
			inputStream = new Scanner(new FileInputStream(fileName));
		}catch(FileNotFoundException e) {
			System.out.print("File could not be found. Please start again.");
			System.exit(0);
		}
		String name1 = inputStream.nextLine();String name2 = inputStream.nextLine();
		String nameCurrent = inputStream.nextLine();
		
		firstPlayer = new Player(name1, Position.BLACK);secondPlayer = new Player(name2, Position.WHITE);
		
		if (nameCurrent.equals(name1)) {
			currentPlayer = firstPlayer;
			otherPlayer = secondPlayer;}
		else{
			currentPlayer = secondPlayer;
			otherPlayer = firstPlayer;
		}
		for (int row = 0; row < positions.length;row++) {
			String s = inputStream.nextLine();
			for (int col=0; col<positions[row].length;col++) {
				char piece = s.charAt(col);
				
				if(piece == Position.BLACK) {
					positions[row][col].setPiece(Position.BLACK);}
				else if(piece == Position.WHITE) {
					positions[row][col].setPiece(Position.WHITE);
				}
				
			}
		}inputStream.close();
	}

}