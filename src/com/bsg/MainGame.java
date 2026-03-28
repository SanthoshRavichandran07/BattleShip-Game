package com.bsg;

import java.util.Scanner;

public class MainGame {
    public static void main(String[] args) {
        Game game = new Game();
        Scanner sc = new Scanner(System.in);

        System.out.println("\t----- Battle Ship Game -----");
        
        // Step 1: Base setup
        System.out.print("Enter size of the base: ");
        int n = sc.nextInt();
        if(n<3) {
        	System.err.println("Base size must be greater than or equal to 3");
        	System.exit(0);
        }
        Game.createBase(n);

        // Step 2: Choose opponent
        System.out.print("Play with another player or computer? (p/c): ");
        char choice = sc.next().charAt(0);
        boolean vsComputer = (choice == 'c');

        // Step 3: Place ships on hidden ship bases
        // Example: 5 ships each, each ship is 3 cells long
        game.placeShipsRandomly(Game.shipBase1, 5);  // Player 1 ships
        game.placeShipsRandomly(Game.shipBase2, 5);  // Player 2 or Computer ships

        // Step 4: Show initial war bases (empty, only '~')
        game.printBase();

        // Step 5: Start the game loop
        game.startGame(vsComputer);

        sc.close();
    }
}
