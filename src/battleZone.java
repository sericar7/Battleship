import java.util.Random;

public class battleZone {
    // "battleZone" is conceptualized as a table.  A table index (1,3) indicates row#, column #.
    // Contrast w/ Cartesian coordinates, which switch the indices (3,1) to indicate the "same" location.

    public int gridWidth;                  // The number of columns.  Should be the same for both battleZones, so let it be public.
    public int gridHeight;                 // The number of rows.   Should be the same for both battleZones, so let it be public.
    gridCell[][] gridCellArray;            // 2-dimensional array; the first index is row#, the second index is column#

    public battleZone(int rows, int columns) { // height and width of the battle zone

        this.gridHeight = rows;
        this.gridWidth = columns;
        this.gridCellArray = new gridCell[this.gridHeight][this.gridWidth];  // Allocate space for the array of gridCell(s)

        for (int i = 0; i <= this.gridHeight - 1; i++) {                       // Assign value to the gridCell elements
            for (int j = 0; j <= this.gridWidth - 1; j++) {
                this.gridCellArray[i][j] = new gridCell();                  // When each gridCell is constructed, initial default values are assigned
            }
        }
    }

    public int arrayIndex (int gridIndex) {                                 // NOTE: Grid indexes start at 1, the underlying array indexes start at 0. !!!
        return gridIndex -1;
    }
    public int gridIndex (int arrayIndex) {                                 // NOTE: Grid indexes start at 1, the underlying array indexes start at 0. !!!
        return arrayIndex +1;
    }



    public void displayAllFriendlyCells(String displayMargin, String gridHeaderMsg) {     // displayMargin contains a string that differentiates among players
        // 5/12: Remove passed arg "owner".  By creating two distinct classes (EnemyFleetIntel vs. battleZone) we don't need to identify "owner" in a single class.
        System.out.println(displayMargin);
        System.out.println(displayMargin + gridHeaderMsg);
        System.out.println(displayMargin);
        System.out.println(displayMargin + "        COLUMN");
        System.out.print(displayMargin + "        ");
        for (int j=1;j<=gridWidth;j++) {                                // Print column headers
            System.out.print(j + "          ");
        }
        System.out.println();
        System.out.println(displayMargin + "ROW");

        for (int i = 0; i <= this.arrayIndex(gridHeight); i++) {
            int rowHeader = i+1;
            if (rowHeader < 10) {
                System.out.print(displayMargin + " " + rowHeader + "      ");                               // Print row header for single-digit row #
            } else {
                System.out.print(displayMargin + rowHeader + "      ");                               // Print row header for 2-digit row #
            }
            for (int j = 0; j <= this.arrayIndex(gridWidth); j++) {
                // this.EnemyGridCellArray[i][j] = 0;
                int row = i + 1;
                int column = j + 1;
                char status = '-';  //


                if (this.gridCellArray[i][j].shipName == "") {
                    if (this.gridCellArray[i][j].attacked) {
                        System.out.print("- (miss)   ");                        // No ship in this cell; opponent attacked the cell
                    } else {
                        System.out.print("-          ");                        // No ship in this cell; opponent did not attack the cell
                    }
                } else {                                                        // Ship in this cell
                    if (this.gridCellArray[i][j].attacked) {
                        if (this.gridCellArray[i][j].sunk) {
                            System.out.print(this.gridCellArray[i][j].shipName + " (sunk)   ");
                        }  else {
                            System.out.print(this.gridCellArray[i][j].shipName + " (hit)    ");
                        }

                    } else {
                        System.out.print(this.gridCellArray[i][j].shipName + "          ");    // Ship in this cell; opponent did not attack the cell
                    }
                }
            }
            System.out.println();
        }
        System.out.println(displayMargin);
    }


    public void addShipSection(int x, int y, String name) {     // Deprecated after creating a more robust method of placing ships
        this.gridCellArray[x - 1][y - 1].hasShip = true;
        this.gridCellArray[x - 1][y - 1].shipName = name;
    }





    public boolean placeShip(indexPair gridIndex, // Place a ship of passed length, with nose at (gridIndex).
                             int length,          // Length of a ship
                             int direction,       // Try to "build" the ship in the passed direction: 0=north; 1=east; 2=south; 3=west
                             String name,         // Each ship name should be unique, but uniqueness is not managed in this method
                             boolean verbose) {   // Verbose when human places ships; silent when computer places ships.


        //System.out.println("Placing ship ...");

        int gridRowIndex = gridIndex.getRowIndex();
        int gridColumnIndex = gridIndex.getColumnIndex();

        boolean placeWholeShipSuccess = false;                              // Was placement of the whole ship successful?  (Were all of the required cells empty?)

        boolean placeShipSectionSuccess = true;                             // Was placement of a single section of the ship successful?
        int arrayRowIndex = gridRowIndex - 1;                               // convert gridIndex (1...) to arrayIndex (0...)
        int arrayColumnIndex = gridColumnIndex - 1;
        int direction_num = direction;
        String error_string = "";
        // System.out.println("direction_num="+direction_num);

        if (direction_num == 0) {                                           // "Build" the ship up the grid
            if (arrayRowIndex - (length - 1) < 0) {                                           // If the ship won't fit in the battle zone ...
                placeShipSectionSuccess = false;                            // Change the loop flag so that we'll exit
                error_string = "Ship placement extends beyond the battle zone.";
            }
            int i = arrayRowIndex;
            for (i = arrayRowIndex; (placeShipSectionSuccess) & (i > arrayRowIndex - length); i--) {    // While we have success placing ship sections, moving up (north) the battle grid for the specified length of the ship ...
                //System.out.println("i="+i+"y="+y);
                if (this.gridCellArray[i][arrayColumnIndex].shipName != "") {              // If a cell is NOT empty ...
                    placeShipSectionSuccess = false;                        // Change the loop flag so that we'll exit
                    int err_index = i + 1;
                    error_string = "Another ship already occupies (" + err_index + "," + gridColumnIndex + ").";
                }
            }

            if (placeShipSectionSuccess == true) {                          // If all required cells are empty: we can place the ship as requested.
                for (i = arrayRowIndex; i > arrayRowIndex - length; i--) {
                    this.gridCellArray[i][arrayColumnIndex].shipName = name;
                    this.gridCellArray[i][arrayColumnIndex].hasShip = true;
                    placeWholeShipSuccess = true;
                }
            } else {
                if (verbose) {
                    System.out.println("      ! Could not place ship north (up) from (" + gridRowIndex + "," + gridColumnIndex + ") for " + length + " cells: " + error_string);
                }
            }
            if (placeWholeShipSuccess == true) {
                if (verbose) {
                    System.out.println("      OK.  Placed ship north (up) from (" + gridRowIndex + "," + gridColumnIndex + ") for " + length + " cells.");
                }
            }
        }

        if (direction_num == 1) {                                           // "Build" the ship *eastward* (right) the grid
            if (arrayColumnIndex + (length) > gridWidth) {                                  // If the ship won't fit in the battle zone ...
                placeShipSectionSuccess = false;                            // Change the loop flag so that we'll exit
                error_string = "Ship placement extends beyond the battle zone.";
            }
            int i = arrayColumnIndex;
            for (i = arrayColumnIndex; (placeShipSectionSuccess) & (i < arrayColumnIndex + length); i++) {    // While we have success placing ship sections, moving down (south) the battle grid for the specified length of the ship ...
                //System.out.println("x="+x+"i="+i);
                if (this.gridCellArray[arrayRowIndex][i].shipName != "") {              // If a cell is NOT empty ...
                    placeShipSectionSuccess = false;                        // Change the loop flag so that we'll exit
                    int err_index = i + 1;
                    error_string = "Another ship already occupies (" + gridRowIndex + "," + err_index + ").";
                }
            }

            if (placeShipSectionSuccess == true) {                          // If all required cells are empty: we can place the ship as requested.
                for (i = arrayColumnIndex; i < arrayColumnIndex + length; i++) {
                    this.gridCellArray[arrayRowIndex][i].shipName = name;
                    this.gridCellArray[arrayRowIndex][i].hasShip = true;
                    placeWholeShipSuccess = true;
                }
            } else {
                if (verbose) {
                    System.out.println("      ! Could not place ship east (right) from (" + gridRowIndex + "," + gridColumnIndex + ") for " + length + " cells: " + error_string);
                }
            }
            if (placeWholeShipSuccess == true) {
                if (verbose) {
                    System.out.println("      OK.  Placed ship east (right) from (" + gridRowIndex + "," + gridColumnIndex + ") for " + length + " cells.");
                }

            }
        }

        if (direction_num == 2) {                                           // "Build" the ship *down* the grid
            if (arrayRowIndex + (length) > gridHeight) {                                  // If the ship won't fit in the battle zone ...
                placeShipSectionSuccess = false;                            // Change the loop flag so that we'll exit
                error_string = "Ship placement extends beyond the battle zone.";
            }
            int i = arrayRowIndex;
            for (i = arrayRowIndex; (placeShipSectionSuccess) & (i < arrayRowIndex + length); i++) {    // While we have success placing ship sections, moving down (south) the battle grid for the specified length of the ship ...
                //System.out.println("i="+i+"y="+y);
                if (this.gridCellArray[i][arrayColumnIndex].shipName != "") {              // If a cell is NOT empty ...
                    placeShipSectionSuccess = false;                        // Change the loop flag so that we'll exit
                    int err_index = i + 1;
                    error_string = "Another ship already occupies (" + err_index + "," + gridColumnIndex + ").";
                }
            }

            if (placeShipSectionSuccess == true) {                          // If all required cells are empty: we can place the ship as requested.
                for (i = arrayRowIndex; i < arrayRowIndex + length; i++) {
                    this.gridCellArray[i][arrayColumnIndex].shipName = name;
                    this.gridCellArray[i][arrayColumnIndex].hasShip = true;
                    placeWholeShipSuccess = true;
                }
            } else {
                if (verbose) {
                    System.out.println("      ! Could not place ship south (down) from (" + gridRowIndex + "," + gridColumnIndex + ") for " + length + " cells: " + error_string);
                }
            }
            if (placeWholeShipSuccess == true) {
                if (verbose) {
                    System.out.println("      OK.  Placed ship south (down) from (" + gridRowIndex + "," + gridColumnIndex + ") for " + length + " cells.");
                }
            }

        }

        if (direction_num == 3) {                                           // "Build" the ship *down* the grid
            if (arrayColumnIndex - (length - 1) < 0) {                                  // If the ship won't fit in the battle zone ...
                placeShipSectionSuccess = false;                            // Change the loop flag so that we'll exit
                //System.out.println("x="+x+"i="+i);
                error_string = "Ship placement extends beyond the battle zone.";
            }
            int i = arrayColumnIndex;
            for (i = arrayColumnIndex; (placeShipSectionSuccess) & (i > arrayColumnIndex - length); i--) {    // While we have success placing ship sections, moving down (south) the battle grid for the specified length of the ship ...
                //System.out.println("x="+x+"i="+i);
                if (this.gridCellArray[arrayRowIndex][i].shipName != "") {              // If a cell is NOT empty ...
                    placeShipSectionSuccess = false;                        // Change the loop flag so that we'll exit
                    int err_index = i + 1;
                    error_string = "Another ship already occupies (" + arrayRowIndex + "," + err_index + ").";
                }
            }

            if (placeShipSectionSuccess == true) {                          // If all required cells are empty: we can place the ship as requested.
                for (i = arrayColumnIndex; i > arrayColumnIndex - length; i--) {
                    this.gridCellArray[arrayRowIndex][i].shipName = name;
                    this.gridCellArray[arrayRowIndex][i].hasShip = true;
                    placeWholeShipSuccess = true;
                }
            } else {
                if (verbose) {
                    System.out.println("      ! Could not place ship west (left) from (" + gridRowIndex + "," + gridColumnIndex + ") for " + length + " cells: " + error_string);
                }
            }
            if (placeWholeShipSuccess == true) {
                if (verbose) {
                    System.out.println("      OK.  Placed ship west (left) from (" + gridRowIndex + "," + gridColumnIndex + ") for " + length + " cells.");
                    System.out.println();
                }

            }
        }


        //System.out.println("attack " );
        return placeWholeShipSuccess;
    }

    public boolean randomlyPlaceShip(int length, String name) {

        indexPair gridIndex = new indexPair(0,0);
        boolean verbose = false;                            // Display #tries to place a ship?
        int maxTries = 500;                                 // Maximum attempts to place a ship.  Let's not loop forever ...
        int tries = 0;                                      // For testing; how many tries did it take to place a ship?

        Random random_int = new Random();
        boolean success = false;                            // Successfully placed a ship?
        //System.out.println("Randomly placing ship " + name);
        for (int i = 1; success == false & i < maxTries; i++) {
            // Generate a random origin for the ship
            gridIndex.putRowIndex(random_int.nextInt(gridHeight) + 1);          // int randomGridRowIndex = random_int.nextInt(gridHeight) + 1;        // Generate random int between 1 and gridHeight
            gridIndex.putColumnIndex(random_int.nextInt(gridWidth) + 1);      // int randomGridColumnIndex = random_int.nextInt(gridWidth) + 1;      // Generate random int between 1 and gridWidth
            //System.out.println("random row index: " + randomRowGridIndex);
            //System.out.println("random col index: " + randomColumnGridIndex);
            //int[] gridCellIndex = new int[]{randomRowGridIndex, randomColumnGridIndex};

            int randomDirection = random_int.nextInt(4);                // Generate a random direction {0,1,2,3] ([N,E,S,W]) of placement from the origin
            //System.out.println("random direction: " + randomDirection);

            success = this.placeShip(   gridIndex,                  // Origin of ship (column) (build *from* this cell in the direction specified
                                        // randomGridColumnIndex,      // Origin of ship (column) (build *from* this cell in the direction specified
                                        length,                     // Length of ship
                                        randomDirection,            // Direction to build: [0,1,2,3] ({N,E,S,W})
                                        name,                       // Name of ship.  Should be unique.  Uniqueness is not managed in "randomlyPlaceShip" or in "placeShip".
                                        false);                     // Unless troubleshooting, the called method should probably generate no messages about placement.  Could be revised.

            tries = i;
            // System.out.println(i + " tries.");
        }
        if (success) {
            System.out.print("Randomly placed ship " + name + ", length = " + length + " cells. ");
            if (verbose) {
                System.out.println(+ tries + " tries."  );
            } else {
                System.out.println();
            }
        } else {
            System.out.println("! Could not place ship " + name + ", length = " + length + " cells.");
        }
        return success;
    }



    public boolean placeAllShipsRandomly(ship[] fleet) {
        boolean success = true;
        String name = "";
        int length = 0;

        for (int i=0; i < fleet.length & success; i++) {
            name = fleet[i].shipName;
            length = fleet[i].shipLength;
            success = this.randomlyPlaceShip(length, name);
            if (!success) {
                System.out.println("! Could not place all ships in the fleet.");
            }
        }

        return success;         // Will be true only if all ships were placed successfully
    }

    public boolean gridIndexIsValid(int gridRowIndex, int gridColumnIndex) {
        if (gridRowIndex <= this.gridHeight & gridRowIndex > 0 & gridColumnIndex <= this.gridWidth & gridColumnIndex > 0) {
            //System.out.println("True " );
            return true;

        } else {
            //System.out.println("False " );
            return false;
        }
    }

    public boolean[] attack(indexPair gridIndex, String displayMargin) {

        // Attack a grid in opponent's zone.  Results (miss, hit, sink, game-over) are based on the state of the *opponent's* battleZone.  The opponent's cell is changed appropriately (e.g., record that the cell was attacked).
        int gridRowIndex = gridIndex.getRowIndex();
        int gridColumnIndex = gridIndex.getColumnIndex();
        String resultString = "";

        // boolean hitBool = false;                                                        // A ship section is in this cell
        //boolean shipSunk = false;                                                       // Is this the last section of this ship?
        //boolean gameOver = false;                                                       // Are all ships sunk?
        boolean[] attackResult = new boolean[]{false, false, false};                    // hit, sink, game-over

        //if (index[0] > gridHeight || index[0] < 1 || index[1] > gridWidth || index[1] < 1) { // Check that index is in the range
        if (!gridIndexIsValid(gridRowIndex,gridColumnIndex)) { // Check that index is in the range
            resultString = "! Invalid index: " + gridRowIndex + "," + gridColumnIndex + ".  Choose row 1-" + gridHeight + "; column 1-" + gridWidth;
            System.out.println(resultString);
            return attackResult;
        } else {
            //System.out.println("Valid");
            int arrayRowIndex = gridRowIndex - 1;
            int arrayColumnIndex = gridColumnIndex - 1;
            this.gridCellArray[arrayRowIndex][arrayColumnIndex].attacked = true;

            if (this.gridCellArray[arrayRowIndex][arrayColumnIndex].shipName != "") {                // If a ship section is in this cell ...
                resultString = "( ( (  HIT  ) ) )";
                attackResult[0] = true;     //hitBool
                String hitShipName = this.gridCellArray[arrayRowIndex][arrayColumnIndex].shipName;

                boolean foundUnattackedSectionOfThisShip = false;                              // Test whether entire ship is sunk
                for (int i = 0; i < gridHeight && !foundUnattackedSectionOfThisShip; i++) {
                    for (int j = 0; j < gridWidth && !foundUnattackedSectionOfThisShip; j++) {
                        if (hitShipName == this.gridCellArray[i][j].shipName) {             // If another section of this ship is found ...
                            if (!this.gridCellArray[i][j].attacked) {
                                foundUnattackedSectionOfThisShip = true;
                            }
                        }
                    }
                }
                if (foundUnattackedSectionOfThisShip) {                                      // If an unattacked section of this ship is found ...
                    resultString = resultString + "  Not yet sunk...";                         // The ship is not yet sunk
                } else {
                    attackResult[1] = true;   //shipSunk                                     // The ship is sunk
                    resultString = resultString + " ... and ...  ! ! !  SINK  ! ! ! ";
                    boolean foundUnattackedSectionOfAnyShip = false;                           // Test whether all ships are sunk
                    for (int i = 0; i < gridHeight && !foundUnattackedSectionOfAnyShip; i++) {
                        for (int j = 0; j < gridWidth && !foundUnattackedSectionOfAnyShip; j++) {
                            if (this.gridCellArray[i][j].shipName != "") {                  // If a section of any ship is in this cell ...
                                if (!this.gridCellArray[i][j].attacked) {                      // If the section has not yet been attacked ...
                                    foundUnattackedSectionOfAnyShip = true;                    // Set the flag (Not all ships are sunk)
                                }
                            }
                        }
                    }
                    if (foundUnattackedSectionOfAnyShip) {                                        // If an unattacked section of any ship is found ...
                        resultString = resultString + "  But not all ships are sunk...";          // Set the return text: The ship is not yet sunk
                    } else {
                        attackResult[2] = true;     //gameOver                                  // All ships are sunk: Game Over!
                        resultString = resultString + "  All ships are sunk... Game Over!";
                    }
                }
            } else {                            // No ship in this cell
                resultString = "... miss ...";
            }
            System.out.println(displayMargin);
            System.out.println(displayMargin + " ========>  Attack (" + gridRowIndex + "," + gridColumnIndex + ")  =======>    " + resultString);
            System.out.println(displayMargin);

            return attackResult;
        }
    }


}



