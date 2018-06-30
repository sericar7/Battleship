import java.awt.*;
import java.util.Scanner;
import java.util.*;
import java.io.*;



public class Main {
    // public static final ship[] Player1_fleet = ;

    public static void main(String[] args) {

        int gridSizeMin = 5;                                            // Min grid size (rows or columns)  NOTE: Must be LTE gridSizeDefault
        int gridSizeMax = 12;                                           // Max grid size (rows or columns)
        int gridSizeDefault = 5;                                        // Default grid size (rows and columns).  Computer player's grid size.  Cannot be changed during game setup or game play.  RESTRICTION:  Must be in range of gridSizeMin and gridSizeMax
        int gridSizeRows = 5;                                           // Human player's grid size (rows).  Can be modified by human player during game setup.  RESTRICTION:  Must be in range of gridSizeMin and gridSizeMax
        int gridSizeColumns = 5;                                        // Human player's grid size (columns).  Can be modified by human player during game setup.  RESTRICTION:  Must be in range of gridSizeMin and gridSizeMax
        int round = 0;
        boolean gameOver = false;
        String displayMarginPlayer1 = "";                               // Displayed on left side of output when human player playes. Passed on displayAllFriendlyCells
        String displayMarginPlayer2 = "***   ";                         // Displayed on left side of output when computer player playes. Passed on displayAllFriendlyCells
        ship[] Player1_fleet;                                           // The ships that Player1 controls.  Player1 fleet is public b/c it is used by Player2 (the computer) to create a separate list for tracking ships
        ship[] Player2_fleet;                                           // The ships that Player2 controls. In the current implementation, Player1 and Player2 have the same number and size ships.
        int playerInputRow = 0;                                         // The row # that the human player wants to attack.  Updated every time the human plays.
        int playerInputColumn = 0;                                      // The column # that the human player wants to attack. Updated every time the human plays.
        indexPair attackGridIndex = new indexPair (0,0);   // Variable for the grid to attack as determined by evaluateIntel
        boolean [] attackResult = new boolean [3];                      // Instantiate and allocate a result for the attack method (test a cell for existence of an opponent's ship)


        Player1_fleet = makeFleet();        // Make the fleet early so that we can easily get and print the fleet size in the intro text.
        Player2_fleet = makeFleet();

        System.out.println();
        System.out.println("BATTLESHIP!");
        System.out.println();
        System.out.println("You play against the computer.");
        System.out.println();


        System.out.println("Each player has a fleet of " + Player1_fleet.length + " ships.  Ship sizes are matched.");
        System.out.println("Your opponent's fleet is in a battle zone that is " + gridSizeDefault + " rows by " + gridSizeDefault + " columns.");
        System.out.println();

        // ==========  Set up the battle zone for Player2 ==============

        battleZone Player2_BattleZone = new battleZone(gridSizeDefault,gridSizeDefault);       // Instantiate and allocate battle zone for computer's ships.  Modify size by changing the number of rows and columns.

        System.out.println("Randomly placing your opponent's ships in the opponent's battle zone of " + gridSizeDefault + " rows by " + gridSizeDefault + " columns ...");
        Player2_BattleZone.placeAllShipsRandomly(Player2_fleet);
        System.out.println();

        // ==========  Get game parameters for Player1  =================

        System.out.println("The default game is that both players have the same size battle zone.");
        //System.out.println("However, you can modify the size of the battle zone that contains your fleet.");
        if (getYesNoInput("Do you want to change the size of the battle zone that contains your fleet? (y/n):")) {
            System.out.println("OK.  Choose your battle zone size, each dimension at least " + gridSizeMin + " but not more than " + gridSizeMax + ".");
            System.out.println();
            gridSizeRows = getGridSizeInput("Type the height of the battle zone for your fleet: ", gridSizeMin, gridSizeMax);
            System.out.println();
            gridSizeColumns = getGridSizeInput("Type the width of the battle zone for your fleet: ", gridSizeMin, gridSizeMax);
            System.out.println();
            if (gridSizeMin*gridSizeMax < gridSizeDefault*gridSizeDefault) {
                System.out.println("Giving yourself a handicap ... you must think you're pretty good.  We'll see!");
            }
        }
        System.out.println();
        promptToContinue("Enter to continue ...");
        System.out.println();


        // ==========  Set up the battle zone for Player1 ==============

        battleZone Player1_BattleZone = new battleZone(gridSizeRows,gridSizeColumns);          // Instantiate and allocate battle zone for human's ships.  Modify size by changing the number of rows and columns.
        EnemyFleetIntel Player1_EnemyFleetIntel = new EnemyFleetIntel(gridSizeDefault,gridSizeDefault);       // Instantiate and allocate enemy fleet intel class for Player1 (human).  Must be same size as the opponent's battle zone.

        // ==========  Set up enemy ships list for both players ==============
        // The list is used by the computer algorithm (Player2).  Although not exposed to the human player (Player1), the list is referenced in ancillary methods so must be defined for the human player as well.

        Player1_EnemyFleetIntel.makeEnemyShipsList(Player2_fleet);     // Create a list that tracks the sink status of ships in Player2's fleet.  The list is not exposed to Player1, but is referenced (required by) methods that are shared between Player1 and Player2.
            // We can make the enemy fleet intel for Player2 only after Player1 has set the size of her battle zone
        EnemyFleetIntel Player2_EnemyFleetIntel = new EnemyFleetIntel(gridSizeRows,gridSizeColumns);          // Instantiate and allocate enemy fleet intel class for Player2 (computer).  Must be same size as the opponent's battle zone.
        Player2_EnemyFleetIntel.makeEnemyShipsList(Player1_fleet);     // Create a list that tracks the sink status of ships in Player1's fleet.  The computer (Player2) uses the list to determine where to attack.  Create the list afer the Player1 fleet is initialized.



        System.out.println("The computer can randomly place your ships in your zone, or you can place each ship manually.");
        System.out.println("The computer player cannot see your ship placements, but during the game will deduce their locations from hits and misses.");
        if (getYesNoInput("Do you want to place your ships manually? (y/n):")) {
            System.out.println();
            System.out.println("OK. For each ship, indicate an ORIGIN and a DIRECTION.");

            for (int i=0; i < Player1_fleet.length; i++) {
                System.out.println();
                boolean success = false;
                boolean verbose = true;     // True for human player--let her know why her choice doesn't work
                while (!success) {
                    System.out.println("Place ship " + Player1_fleet[i].shipName + ", length=" + Player1_fleet[i].shipLength + " in a grid that is " + gridSizeDefault + " by " + gridSizeDefault + ":");
                    playerInputRow = getRowIntInput("    Origin row: ", gridSizeRows);
                    playerInputColumn = getColumnIntInput("    Origin column: ", gridSizeColumns);
                    // System.out.println("Player1_BattleZone.gridCellArray[playerInputRow][playerInputColumn].hasShip = " + Player1_BattleZone.gridCellArray[playerInputRow-1][playerInputColumn-1].hasShip);
                    if (Player1_BattleZone.gridCellArray[playerInputRow-1][playerInputColumn-1].hasShip == true) {
                        System.out.println("Another ship already occupies (" + playerInputRow + "," + playerInputColumn + ").");
                    } else {
                        int playerInputDirection = getDirectionIntInput("    Direction: 0=North, 1=East, 2=South, 3=West: ");
                        indexPair shipOrigin = new indexPair(playerInputRow, playerInputColumn);
                        success = Player1_BattleZone.placeShip(shipOrigin, Player1_fleet[i].shipLength, playerInputDirection, Player1_fleet[i].shipName, verbose);
                    }
                }
                Player1_BattleZone.displayAllFriendlyCells(displayMarginPlayer1, "View of your fleet after placing ship " + Player1_fleet[i].shipName );
            }

        } else {                        // Computer places human's ships automatically (and randomly)
            System.out.println();
            System.out.println("Randomly placing your fleet in a battle zone of " + gridSizeRows + " rows by " + gridSizeColumns + " columns...");
            Player1_BattleZone.placeAllShipsRandomly(Player1_fleet);
        }

        System.out.println();
        promptToContinue("Enter to continue ...");
        System.out.println();

        Player1_BattleZone.displayAllFriendlyCells(displayMarginPlayer1, "View of your fleet.  Letters are ship IDs.");

        System.out.println();
        promptToContinue("Enter to continue ...");
        System.out.println();

        //indexPair testIndexPair = new indexPair(2,2);
        //indexPair newIndexPair = new indexPair(0,0);
        //newIndexPair = testIndexPair.adjacent(0);

        System.out.println();
        System.out.println("* * * * * * * * SETUP IS COMPLETE * * * * * * * * *");
        System.out.println();
        System.out.println("* * * * * * * *    LET'S PLAY     * * * * * * * *");
        System.out.println();

        System.out.println();
        promptToContinue("Enter to continue ...");
        System.out.println();

        // Play


        while (!gameOver) {

            round++;

            //Player1's turn

            System.out.println(displayMarginPlayer1);
            System.out.println(displayMarginPlayer1);
            System.out.println(displayMarginPlayer1 + "************ YOUR TURN ... ************");
            System.out.println(displayMarginPlayer1);

            Player1_EnemyFleetIntel.displayAllEnemyCells(displayMarginPlayer1, "What you know of your opponent's fleet:");

            playerInputRow = getRowIntInput("Type the row to attack: ", gridSizeDefault);
            //System.out.println(playerInputRow);
            playerInputColumn = getColumnIntInput("Type the column to attack: ", gridSizeDefault);
            System.out.println();
            attackGridIndex.putRowIndex(playerInputRow);            // Assign the row to an indexPair class instance
            attackGridIndex.putColumnIndex(playerInputColumn);      // Assign the column to an indexPair class instance
            //System.out.println(playerInputColumn);
            //indexPair[0] = playerInputRow;
            //indexPair[1] = playerInputColumn;
            //gridPoint = playerInputRow,playerInputColumn;
            //if (Player1_EnemyFleetIntel.wasPreviouslyAttacked(playerInputRow, playerInputRow)) {          // Don't tell player -- she must keep track of what she's attacked ....
                //System.out.println("! You already attacked " + playerInputRow + "," + playerInputColumn + "...");
            //}
            attackResult = Player2_BattleZone.attack(attackGridIndex, displayMarginPlayer1);
            gameOver = attackResult[2];
            //System.out.println(String.valueOf(attackResult[0]) + String.valueOf(attackResult[1]) + String.valueOf(attackResult[2]));
            Player1_EnemyFleetIntel.updateIntel(attackGridIndex,attackResult);
            Player1_EnemyFleetIntel.displayAllEnemyCells(displayMarginPlayer1, "Your opponent's fleet after the attack:");
            if (gameOver) {
                System.out.println();
                System.out.println();
                System.out.println("***********************************************");
                System.out.println("*                                             *");
                System.out.println("*              G A M E   O V E R              *");
                System.out.println("*              Y O U     W O N !              *");
                System.out.println("*                                             *");
                System.out.println("***********************************************");
                System.out.println();
            } else {

                promptToContinue("Enter to continue ...");


                // ************************** Player 2's turn  ********************************************************************

                System.out.println();
                System.out.println(displayMarginPlayer2);
                System.out.println(displayMarginPlayer2 + "************ OPPONENT'S TURN ... ***********");
                System.out.println(displayMarginPlayer2);
                System.out.println(displayMarginPlayer2);
                //System.out.println(displayMarginPlayer2 + "What I know about your fleet:");
                Player2_EnemyFleetIntel.displayAllEnemyCells(displayMarginPlayer2, "What I know about your fleet:");
                System.out.println(displayMarginPlayer2);
                attackGridIndex = Player2_EnemyFleetIntel.evaluateIntelEnhanced(displayMarginPlayer2);
                //System.out.println(displayMarginPlayer2 + "Intel Eval:" + attackGridIndex.getRowIndex());

                attackResult = Player1_BattleZone.attack(attackGridIndex, displayMarginPlayer2);


                gameOver = attackResult[2];

                if (!gameOver) {
                    //attackResult = Player1_BattleZone.attack(round, 3, displayMarginPlayer2);
                    //Player2_EnemyFleetIntel.updateIntel(attackGridIndex.getRowIndex(),attackGridIndex.getColumnIndex(),attackResult);
                    Player2_EnemyFleetIntel.updateIntel(attackGridIndex, attackResult);                               // Only Player2 (the computer) uses the updateIntel method.  Player1 uses his natural wits to figure where to attack.
                    if (attackResult[1]) {          // If ship was sunk ...
                        int sunkShipLength = Player2_EnemyFleetIntel.markShipSegmentsSunk(attackGridIndex, displayMarginPlayer2);          // markShipSegmentsSunk method is only for Player2 (the computer).
                        Player2_EnemyFleetIntel.markEnemyShipSunk(sunkShipLength);                                                         //
                    }

                    promptToContinue("Enter to continue ...");

                    Player1_BattleZone.displayAllFriendlyCells("", "Your fleet after the attack:");
                    System.out.println();
                    promptToContinue("Enter to continue ...");
                } else {
                    System.out.println();
                    System.out.println();
                    System.out.println("***********************************************");
                    System.out.println("*                                             *");
                    System.out.println("*              G A M E   O V E R              *");
                    System.out.println("*     Y O U R   O P P O N E N T    W O N !    *");
                    System.out.println("*                                             *");
                    System.out.println("***********************************************");
                    System.out.println();

                }

            }




        }
    }

    // ==========  Methods to get user input ==============

    public static Scanner sc = new Scanner(System.in);


    public static int getGridSizeInput(String message, int gridSizeMin, int gridSizeMax) {
        System.out.print(message);

        while (true) {
            String line = sc.nextLine();
            try {
                int n = Integer.parseInt(line);
                if (n >= gridSizeMin & n <= gridSizeMax) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // ok to ignore: let it fall through to print error message and try next line
            }
            System.out.print("The grid dimension must be between " + gridSizeMin + " and " + gridSizeMax + ".\n" + message);
        }
    }


    public static int getRowIntInput(String message, int rows) {
        System.out.print(message);

        while (true) {
            String line = sc.nextLine();
            try {
                int n = Integer.parseInt(line);
                if (n > 0 & n <= rows) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // ok to ignore: let it fall through to print error message and try next line
            }
            System.out.print("The row must be from 1 to " + rows + ".\n" + message);
        }
    }

    public static ship[] makeFleet() {                         // This fleet matches the original Battleship game fleet.
        ship ship1 = new ship("A",3);
        ship ship2 = new ship("B",4);
        //ship ship3 = new ship("C",3);
        //ship ship4 = new ship("D",3);
        //ship ship5 = new ship("E",2);
        ship[] fleet = new ship[] {ship1,ship2};
        //ship[] fleet = new ship[] {ship1,ship2,ship3,ship4,ship5};
        return fleet;
    }





    public static int getColumnIntInput(String message, int columns) {
        System.out.print(message);

        while (true) {
            String line = sc.nextLine();
            try {
                int n = Integer.parseInt(line);
                if (n > 0 & n <= columns) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // ok to ignore: let it fall through to print error message and try next line
            }
            System.out.print("The column must be from 1 to " + columns + ".\n" + message);
        }
    }

    public static int getDirectionIntInput(String message) {
        System.out.print(message);

        while (true) {
            String line = sc.nextLine();
            try {
                int n = Integer.parseInt(line);
                if (n >= 0 & n <= 3) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // ok to ignore: let it fall through to print error message and try next line
            }
            System.out.println();
            System.out.print("    Type 0, 1, 2, or 3. \n" + message);
        }
    }

    public static boolean getYesNoInput(String YNmessage) {
        System.out.print(YNmessage);
        String answer;
        boolean goodAnswer = false;
        boolean yn = false;

        while (goodAnswer == false) {

            String line = sc.nextLine();

            if (line.isEmpty()) {
                System.out.println("Please answer y/n:");
            } else {
                answer = line.trim().substring(0,1).toLowerCase();
                if (answer.equals("y")) {
                    yn = true;
                    goodAnswer = true;

                } else {
                    if (answer.equals("n")) {
                        yn = false;
                        goodAnswer = true;
                    } else {
                        System.out.println("Sorry, I didn't catch that. Please answer y/n:");
                    }
                }
            }

        }

        return yn;
    }

    public static void promptToContinue(String message) {
        System.out.print(message);
        String line = sc.nextLine();
    }



}

