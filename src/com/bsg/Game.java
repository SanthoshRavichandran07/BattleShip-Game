package com.bsg;

import java.util.*;

public class Game {

	static Scanner sc = new Scanner(System.in);
	static char[][] warBase1;
	static char[][] warBase2;
	static char[][] shipBase1;
	static char[][] shipBase2;
	static int baseSize;
	static int player1Score = 0;
	static int player2Score = 0;
	static Random rand = new Random();

	// Create bases
	public static void createBase(int n) {
		baseSize = n;
		warBase1 = new char[baseSize][baseSize];
		warBase2 = new char[baseSize][baseSize];
		shipBase1 = new char[baseSize][baseSize];
		shipBase2 = new char[baseSize][baseSize];

		for (int i = 0; i < baseSize; i++) {
			for (int j = 0; j < baseSize; j++) {
				warBase1[i][j] = '~';
				warBase2[i][j] = '~';
				shipBase1[i][j] = '~';
				shipBase2[i][j] = '~';
			}
		}
	}

	// Print war bases
	public void printBase() {
		System.out.println("   Player 1 \t\t   Player 2");
		int rowIdentifier = 1;
		int columnIdentifier = 1;
		for (int i = 0; i < baseSize; i++) {
			if (i == 0) {
				System.out.print("  ");
				for (int x = 0; x < baseSize; x++) {
					System.out.print(columnIdentifier++ + " ");
				}
				columnIdentifier = 1;
				System.out.print("\t\t");
				System.out.print("  ");
				for (int x = 0; x < baseSize; x++) {
					System.out.print(columnIdentifier++ + " ");
				}
				System.out.println();
			}
			System.out.print(rowIdentifier + " ");
			for (int j = 0; j < baseSize; j++) {
				System.out.print(warBase1[i][j] + " ");
			}
			System.out.print("\t\t");
			System.out.print(rowIdentifier++ + " ");
			for (int j = 0; j < baseSize; j++) {
				System.out.print(warBase2[i][j] + " ");
			}
			System.out.println();
		}
	}

	// Place ships randomly (3-cell ships)
	public void placeShipsRandomly(char[][] base, int numShips) {
		int placed = 0;
		while (placed < numShips) {
			int r = rand.nextInt(baseSize);
			int c = rand.nextInt(baseSize);
			boolean horizontal = rand.nextBoolean();

			if (horizontal) {
				if (c <= baseSize - 3 && base[r][c] == '~' && base[r][c + 1] == '~' && base[r][c + 2] == '~') {
					base[r][c] = 'S';
					base[r][c + 1] = 'S';
					base[r][c + 2] = 'S';
					placed++;
				}
			} else {
				if (r <= baseSize - 3 && base[r][c] == '~' && base[r + 1][c] == '~' && base[r + 2][c] == '~') {
					base[r][c] = 'S';
					base[r + 1][c] = 'S';
					base[r + 2][c] = 'S';
					placed++;
				}
			}
		}
	}

	// Check for 3 consecutive hits and mark destroyed ship
	public boolean checkConsecutiveHits(char[][] shipBase, char[][] warBase) {

		// Check rows
		for (int i = 0; i < baseSize; i++) {
			for (int j = 0; j <= baseSize - 3; j++) {
				if (shipBase[i][j] == 'X' && shipBase[i][j + 1] == 'X' && shipBase[i][j + 2] == 'X') {
					shipBase[i][j] = 'D';
					shipBase[i][j + 1] = 'D';
					shipBase[i][j + 2] = 'D';
					warBase[i][j] = 'D';
					warBase[i][j + 1] = 'D';
					warBase[i][j + 2] = 'D';
					return true;
				}
			}
		}

		// Check columns
		for (int j = 0; j < baseSize; j++) {
			for (int i = 0; i <= baseSize - 3; i++) {
				if (shipBase[i][j] == 'X' && shipBase[i + 1][j] == 'X' && shipBase[i + 2][j] == 'X') {
					shipBase[i][j] = 'D';
					shipBase[i + 1][j] = 'D';
					shipBase[i + 2][j] = 'D';
					warBase[i][j] = 'D';
					warBase[i + 1][j] = 'D';
					warBase[i + 2][j] = 'D';
					return true;
				}
			}
		}

		return false;
	}

	// Player attack
	public boolean playerAttack(char[][] shipBase, char[][] warBase, String playerName, boolean isPlayer1) {
		System.out.print(playerName + " enter row and column (or 0 to exit): ");
		int row = sc.nextInt();
		if (row == 0)
			System.exit(0);
		int col = sc.nextInt();
		if (col == 0)
			System.exit(0);
		row--;
		col--;

		if (row < 0 || row >= baseSize || col < 0 || col >= baseSize) {
			System.err.println("Invalid move!");
			printBase();
			return true;
		}

		if (shipBase[row][col] == 'S') {
			shipBase[row][col] = 'X';
			warBase[row][col] = 'X';
			System.out.println("Hit! " + playerName + " plays again.");

			if (checkConsecutiveHits(shipBase, warBase)) {
				if (isPlayer1)
					player1Score++;
				else
					player2Score++;
				System.out.println(playerName + " destroyed a ship!");
			}

			printBase();

			if (player1Score >= 3) {
				System.out.println("Player 1 wins by destroying 3 ships!");
				System.exit(0);
			}
			if (player2Score >= 3) {
				System.out.println("Player 2 wins by destroying 3 ships!");
				System.exit(0);
			}

			return true;
		} else if (shipBase[row][col] == '~') {
			shipBase[row][col] = 'O';
			warBase[row][col] = 'O';
			System.out.println("Miss! Turn passes.");
			printBase();
			return false;
		} else {
			System.err.println("Already attacked here!");
			printBase();
			return true;
		}
	}

	// Computer attack
	public boolean computerAttack(char[][] shipBase, char[][] warBase) {
		int row, col;
		do {
			row = rand.nextInt(baseSize);
			col = rand.nextInt(baseSize);
		} while (shipBase[row][col] == 'X' || shipBase[row][col] == 'O');

		System.out.println("Computer attacks at (" + (row + 1) + "," + (col + 1) + ")");

		if (shipBase[row][col] == 'S') {
			shipBase[row][col] = 'X';
			warBase[row][col] = 'X';
			System.out.println("Computer hit a ship! Computer plays again.");

			if (checkConsecutiveHits(shipBase, warBase)) {
				player2Score++;
				System.out.println("Computer destroyed a ship!");
			}

			printBase();

			if (player2Score >= 3) {
				System.out.println("Computer wins by destroying 3 ships!");
				System.exit(0);
			}
			return true;
		} else if (shipBase[row][col] == '~') {
			shipBase[row][col] = 'O';
			warBase[row][col] = 'O';
			System.out.println("Computer missed! Turn passes.");
			printBase();
			return false;
		}
		return false;
	}

	// Start game
	public void startGame(boolean vsComputer) {
		boolean player1Turn = true;
		while (true) {
			if (player1Turn) {
				System.out.println("Player 1's turn:");
				player1Turn = playerAttack(shipBase2, warBase2, "Player 1", true);
			} else {
				if (vsComputer) {
					System.out.println("Computer's turn:");
					player1Turn = !computerAttack(shipBase1, warBase1);
				} else {
					System.out.println("Player 2's turn:");
					player1Turn = !playerAttack(shipBase1, warBase1, "Player 2", false);
				}
			}
		}
	}
}
