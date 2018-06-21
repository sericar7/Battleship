
//import java.util.Random;
//import java.util.Scanner;
import java.util.*;

public class EnemyFleetIntel {
    // "EnemyFleetIntel" contains information that a player knows about the opponent's fleet.  Information is the result of attacking opponent's BattleZone.
    // Like the BattleZone class, the grid for EnemyFleetIntel is conceptualized as rows and columns starting at index 1.

    public int gridWidth;                  // The number of columns.  Should be the same for both battleZones, so let it be public.
    public int gridHeight;                 // The number of rows.   Should be the same for both battleZones, so let it be public.
    ArrayList<enemyShip> enemyShipsList = new ArrayList<enemyShip>();
    //public enemyShip[] enemyShipsList;     // A list of enemy ships, derived from the opponent's fleet list.
    EnemyGridCell[][] gridCellArray;       // 2-dimensional array; the first index is row#, the second index is column#.  ! NOTE: Grid indexes start at 1, the underlying array indexes start at 0. !!!
    String displayMarginPlayer2 = "***   ";

    public EnemyFleetIntel(int rows, int columns) { // height and width of the enemy's battle zone

        this.gridHeight = rows;
        this.gridWidth = columns;
        this.gridCellArray = new EnemyGridCell[this.gridHeight][this.gridWidth];    // Allocate space for the array of gridCell(s)

    for (int i = 0; i <= this.arrayIndex(gridHeight); i++) {                  // For each row ... (convert grid index (1,2,3,...) to array index (0,1,2,...))
            for (int j = 0; j <= this.arrayIndex(gridWidth); j++) {           // For each column ... (convert grid index (1,2,3,...) to array index (0,1,2,...))
                this.gridCellArray[i][j] = new EnemyGridCell();                     // When each gridCell is constructed, initial default values are assigned
            }
        }
    }




    public int arrayIndex (int gridIndex) {                                         // NOTE: Grid indexes start at 1, the underlying array indexes start at 0. !!!
        return gridIndex -1;
    }
    public int gridIndex (int arrayIndex) {                                         // NOTE: Grid indexes start at 1, the underlying array indexes start at 0. !!!
        return arrayIndex +1;
    }



    public void displayAllEnemyCells(String displayMargin, String gridHeadingMsg) {
        // 5/12: Remove passed arg "owner".  By creating two distinct classes (EnemyFleetIntel vs. battleZone) we don't need to identify "owner" in a single class.
        System.out.println(displayMargin);
        System.out.println(displayMargin + gridHeadingMsg);
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
            System.out.print(displayMargin + " " + rowHeader + "      ");                               // Print row header
            for (int j = 0; j <= this.arrayIndex(gridWidth); j++) {
                // this.EnemyGridCellArray[i][j] = 0;
                int row = i + 1;
                int column = j + 1;
                char status = '-';  //

                // The opponent's (limited) view of the cell ...
                if (this.gridCellArray[i][j].attacked) {
                    if (this.gridCellArray[i][j].hasShip) {
                        if (this.gridCellArray[i][j].sinkShipPoint) {
                            System.out.print("SINK!      ");
                        }  else {
                            System.out.print("HIT!       ");
                        }

                    } else {
                        System.out.print("miss       ");
                    }

                } else {
                    System.out.print("?          ");
                }

            }
            System.out.println();
        }
        System.out.println(displayMargin);
    }



    public void makeEnemyShipsList(ship[] enemyFleet) {             // Use the enemy's fleet list to create a list for tracking sunk ships
        //System.out.println(" enemyFleet.length " + enemyFleet.length);
        // enemyShip[] enemyShipsList = new enemyShip[enemyFleet.length];
        //this.enemyShipsList = enemyFleet.length;
        for (int i = 0; i < enemyFleet.length; i++) {
            //System.out.println(i + "add to enemyShipsList");
            enemyShip ship0 = new enemyShip(enemyFleet[i].shipLength);
            // Per the constructor, the shipSunk attrib is set false when created
            //enemyShipsList[i] = ship0;
            enemyShipsList.add(ship0);
            //this.enemyShipsList[i].shipLength = enemyFleet[i].shipLength;
        }
        //for (int i = 0; i < enemyFleet.length; i++) {
            // System.out.println( "  enemyShipsList.size(): " +  enemyShipsList.size());
        //}
    }

    public void markEnemyShipSunk(int sunkShipLength) {        // When an opponent's ship is sunk, mark it in the enemyShipsList.  The size is a guess, and can be incorrect if another ship touches an end of the sunk ship.

        boolean found = false;
        for (int i = 0; i < enemyShipsList.size() & !found; i++) {
            // System.out.println(i);
            if (sunkShipLength == enemyShipsList.get(i).shipLength){
                if (enemyShipsList.get(i).shipSunk) {
                    // nop--mark a ship that is not yet sunk
                } else {
                    found = true;
                    enemyShipsList.get(i).shipSunk = true;
                }
            }
        }
    }

    public int findShortestUnsunkShipLength() {

        int shortestUnsunkShipLength = 99;
        //System.out.println("  enemyShipsList.size()  " + enemyShipsList.size());
        for (int i = 0; i < enemyShipsList.size(); i++) {
            //System.out.println(i);
            //System.out.println("enemyShipsList.get(i).shipSunk: " + enemyShipsList.get(i).shipSunk + " enemyShipsList.get(i).shipLength: " + enemyShipsList.get(i).shipLength);
            if (!enemyShipsList.get(i).shipSunk){                                   // If the ship is not sunk ....
                if (shortestUnsunkShipLength > enemyShipsList.get(i).shipLength) {      // If the length of the ship is less than the current shortest ship ....
                    shortestUnsunkShipLength = enemyShipsList.get(i).shipLength;            // The current ship's length is the new shortest.
                } else {
                    // nop--mark a ship that is not yet sunk
                }
            }
        }
        return shortestUnsunkShipLength;
    }




    public indexPair evaluateIntelEnhanced (String displayMargin) {

        // Look for a ship section that is hit but unsunk. Look for targets adjacent to the hit but unsunk cell. (If no hit but unsunk cell exists, generate a random cell to attack).
        // System.out.println("*** ---- evaluateIntelEnhanced ------");

        indexPair gridIndexPair = new indexPair (0,0);
        //promptToContinue("Enter to continue 1 ...");

        //boolean existsHitNotSunk = false;
        if  (existsHitNotSunkCell()) {     //
            //gridIndexPair = findHitNotSunk ();   // get an indexPair from the called function?
            //indexPair gridIndexPair = new indexPair (3,2);
            //promptToContinue("Enter to continue 2 ...");
            // System.out.println("this.hitNotSunkCell() = " + this.getHitNotSunkCell().getRowIndex() + "," + this.getHitNotSunkCell().getColumnIndex());
            //promptToContinue("Enter to continue 3 ...");
            gridIndexPair = findTargetNearHitNotSunkCell (this.getHitNotSunkCell(), displayMargin);
            //promptToContinue("Enter to continue 4 ...");
            //gridIndexPair = getHitNotSunkCell();
            //System.out.println("hitNotSunk gridIndexPair = " + gridIndexPair.getRowIndex() + "," + gridIndexPair.getColumnIndex());
            //int direction = 0;
            //int length = 0;
            //gridIndexPair.putRowIndex(3);
            //gridIndexPair.putColumnIndex(3);
            //int segLength = this.shipSectionSegmentLength (direction, length, gridIndexPair);
            //System.out.println("length north = " + segLength);

        } else {
            gridIndexPair = evaluateIntelRandom ();
            System.out.println(displayMargin +"OK ... I'll choose a random cell: (" + gridIndexPair.getRowIndex() + "," + gridIndexPair.getColumnIndex() + ")");


        }

        // System.out.println("---- EXIT evaluateIntelEnhanced ------");
        return gridIndexPair;
    }


    public indexPair findTargetNearHitNotSunkCell (indexPair hitNotSunkCell, String displayMargin) {

        indexPair candidateCellIndex = new indexPair (hitNotSunkCell.getRowIndex(),hitNotSunkCell.getColumnIndex());
        indexPair untestedCellIndex = new indexPair (0,0);                      // Store coordinates of an adjacent, untested cell.  If two hitNotSunk sections are found in this orientation, this becomes the target.  If not, keep looking for a better target.
        indexPair targetCellIndex = new indexPair (0,0);                      // When an appropriate candidate is identified and ready to return to calling method, change the name from "candidate..." to "target...".
        boolean foundTarget = false;
        boolean[] searchList = {false,false,false,false};
        boolean searchedAllDirections = false;
        //String directionText = "";                  // Text ("North","East","South","West") that corresponds to the integer representations (0,1,2,3).  Initialize to "North" and must align with the for loop.
        System.out.println(displayMargin);
        System.out.println(displayMargin + "Hmmmm ... (" + hitNotSunkCell.getRowIndex() + "," + hitNotSunkCell.getColumnIndex() + ") is hit but not sunk.  Which adjacent cell shall I attack?");
        System.out.println(displayMargin);

        for (int searchDirection = 0; foundTarget ==false & searchDirection <= 3 & searchedAllDirections == false; searchDirection++) {  // Search direction is 4 compass points (0=N, 1=E, 2=S, 3=W)

            if (searchList[searchDirection] == true) {      // If this direction has already been searched ...
                // No need to search again.
            } else {
                searchList[searchDirection] = true;         // Set the flag that indicates that we searched in this direction.

                boolean foundBoundary = false;              // Becomes true when we find a cell that is: Off map, Empty, Sunk.  Reset to false when trying a new direction.
                boolean foundBothBoundaries = false;        // Becomes true when a second boundary is found in a given orientation (N+S, E+W).  Reset to false when trying a new direction.
                boolean foundUntestedCell = false;          // Becomes true when an untested (not yet attacked) is found.  Reset when changing orientation (when foundBothBoundaries = true).
                boolean multiSectionHitNotSunk = false;     // Becomes true when we find a HitNotSunk section adjacent to another HitNotSunkSection
                int boundaryCount = 0;                      // Increment when a boundary is found.  When 2 boundaries are found in a given orientation, it's time to try another direction.

                candidateCellIndex.putRowIndex(hitNotSunkCell.getRowIndex());            // Start searching from the previously hit (but not yet sunk) cell
                candidateCellIndex.putColumnIndex(hitNotSunkCell.getColumnIndex());

                while (foundTarget==false & foundBothBoundaries == false) {                                 // Search a direction.  If a boundary is found, search the opposite direction.)
                    candidateCellIndex = candidateCellIndex.adjacent(searchDirection);                                             // Determine the index of the next cell in the direction of <searchDirection>

                    System.out.println(displayMargin + "Look " + directionInt2Text(searchDirection) + "..."); // to cell (" + candidateCellIndex.getRowIndex() + "," + candidateCellIndex.getColumnIndex() + "):");
                    if (this.gridIndexIsValid(candidateCellIndex.getRowIndex(), candidateCellIndex.getColumnIndex())) {         // If the next cell is in the BattleZone ...
                        if (!wasPreviouslyAttacked(candidateCellIndex.getRowIndex(), candidateCellIndex.getColumnIndex())) {          // If this cell has not yet been attacked ...
                            if (multiSectionHitNotSunk == true) {                                                                       // And is aligned with hitNotSunk sections,
                                foundTarget = true;                                                                                         // This is a good target!
                                targetCellIndex.putRowIndex(candidateCellIndex.getRowIndex());                                              // Assign this location as the target
                                targetCellIndex.putColumnIndex(candidateCellIndex.getColumnIndex());
                                this.gridCellArray[arrayIndex(targetCellIndex.getRowIndex())][arrayIndex(targetCellIndex.getColumnIndex())].searchDirection = searchDirection;  // Set the direction
                                System.out.println(displayMargin +"(" + targetCellIndex.getRowIndex() + "," + targetCellIndex.getColumnIndex() + ") has not yet been attacked and is aligned with other sections that are hit but not sunk.");
                            }  else {
                                // candidateCellIndex marks a potential target.  Save the coordinates as untestedCellInces.  If we find nothing better, we can use it as the target.  But let's keep looking, if we haven't yet used all directions.
                                untestedCellIndex = new indexPair(candidateCellIndex.getRowIndex(), candidateCellIndex.getColumnIndex());
                                this.gridCellArray[arrayIndex(untestedCellIndex.getRowIndex())][arrayIndex(untestedCellIndex.getColumnIndex())].searchDirection = searchDirection;  // We might not attack this cell, but now is the time to set the direction.  Direction can be changed on a future search if this cell is not attacked on this turn.
                                foundBoundary = true;                // An untested cell is a boundary of the current search effort.
                                foundUntestedCell = true;
                                System.out.println(displayMargin +"(" + untestedCellIndex.getRowIndex() + "," + untestedCellIndex.getColumnIndex() + ") has not yet been attacked.  I'll keep it in mind as a potential target. ");
                            }
                        } else {                        // Found a cell in the BattleZone that has been tested (has been attacked)
                            if (this.gridCellArray[arrayIndex(candidateCellIndex.getRowIndex())][arrayIndex(candidateCellIndex.getColumnIndex())].hasShip == true) {
                                if (this.gridCellArray[arrayIndex(candidateCellIndex.getRowIndex())][arrayIndex(candidateCellIndex.getColumnIndex())].shipSunk == true) {
                                    System.out.println(displayMargin +"(" + candidateCellIndex.getRowIndex() + "," + candidateCellIndex.getColumnIndex() + ") has been attacked and appears to be a section of a ship that is sunk.");
                                    foundBoundary = true;                       // Found a boundary in this direction.
                                }  else {
                                    multiSectionHitNotSunk = true;
                                    System.out.println(displayMargin +"(" + candidateCellIndex.getRowIndex() + "," + candidateCellIndex.getColumnIndex() + ") has been attacked and is not yet sunk.  A ship might be oriented " + orientation(searchDirection) + "!");
                                    // This orientation contains at least two sections of hitNotSunk.  Hint: An untested, adjacent cell in this orientation would be a nice target.  Keep going in this direction.
                                }
                            } else {                                           // Tested, empty cell: boundary
                                foundBoundary = true;
                                System.out.println(displayMargin +"(" + candidateCellIndex.getRowIndex() + "," + candidateCellIndex.getColumnIndex() + ") has been attacked and is empty.  I'll keep looking.");
                            }
                        }
                    } else {                                        // And index that is out of the battle zone ...
                        foundBoundary = true;                          // ... is a boundary.
                        System.out.println(displayMargin +"(" + candidateCellIndex.getRowIndex() + "," + candidateCellIndex.getColumnIndex() + ") and further " + directionInt2Text(searchDirection) + " is out of the battle zone.");
                    }
                    if (foundTarget == true) {
                        // Do nothing in this section: the flag is set to exit this method and return the coordinates of a promising target.
                    } else {
                        if (foundBoundary == true) {
                            if (foundUntestedCell == true & multiSectionHitNotSunk == true) {           // Account for the case where the multi-section HitNotSunk was encountered *after* an untested cell was found in the same orientation.
                                foundTarget = true;                                                                                         // This is a good target!
                                targetCellIndex.putRowIndex(untestedCellIndex.getRowIndex());                                               // Assign this location as the target
                                targetCellIndex.putColumnIndex(untestedCellIndex.getColumnIndex());

                                System.out.println(displayMargin +"(" + targetCellIndex.getRowIndex() + "," + targetCellIndex.getColumnIndex() + ") has not yet been attacked and is aligned with other sections that are hit but not sunk.  Attack!  ");
                            } else {
                                boundaryCount++;
                                if (boundaryCount < 2) {
                                    foundBoundary = false;              // Clear the flag; next time it is set will be the 2nd boundary
                                    candidateCellIndex.putRowIndex(hitNotSunkCell.getRowIndex());            // Reset the search origin to the previously hit (but not yet sunk) cell
                                    candidateCellIndex.putColumnIndex(hitNotSunkCell.getColumnIndex());
                                    switch (searchDirection) {          // Switch the direction of search.
                                        case 0:
                                            searchDirection = 2;
                                            break;
                                        case 1:
                                            searchDirection = 3;
                                            break;
                                        case 2:
                                            searchDirection = 0;
                                            break;
                                        case 3:
                                            searchDirection = 1;
                                            break;
                                        default:
                                            System.out.println("! UNEXPECTED direction VALUE in EnemyFleetIntel.findTargetNearHitNotSunkCell = " + searchDirection);
                                            break;
                                    }
                                    searchList[searchDirection] = true; // Set the flag that indicates that we searched in this direction.
                                    // System.out.println("***   Switch search direction to " + directionText);
                                }  else {                               // boundaryCount = 2 ...
                                    foundBothBoundaries = true;         // ... set the flag that will cause exit from the the loop for this orientation (we checked N *and* S or we checked E *and* W)
                                    foundUntestedCell = false;          // ... clear the flag in prep for the next search orientation
                                }
                            }
                        } else {                                    // Not a boundary (not out-of-BattleZone, not untested cell, not empty cell, not sunk cell.  Must be another hitNotSunk cell.
                            // Keep going in this direction
                        }
                    }
                }
            }  // else ... (searchList[searchDirection] == false) {      // This direction has not been searched ...
            if (searchList[0]==true & searchList[1]==true & searchList[2]==true & searchList[3]==true) {        // Check whether we've searched all directions
                searchedAllDirections = true;                                                                   // If so, set the flag that will exit the loop that checks all directions
            }
        }                   // Search through 4 compass points (0=N, 1=E, 2=S, 3=W)
        if (!foundTarget==true) {
            if (untestedCellIndex.getRowIndex() != 0) {                 // If the index is *not* 0, we found an adjacent, untested cell.  That is a suitable target.
                System.out.println(displayMargin +" OK.  " + "(" + untestedCellIndex.getRowIndex() + "," + untestedCellIndex.getColumnIndex() + ") is an adjacent, untested cell.  Good enough.");
                targetCellIndex.putRowIndex(untestedCellIndex.getRowIndex());        // Assign the last discovered adjacent, untested cell as the target cell.
                targetCellIndex.putColumnIndex(untestedCellIndex.getColumnIndex());
            } else {                // Couldn't find any adjacent, untested cells!!  (This is an abnormal condition)
                targetCellIndex = evaluateIntelRandom ();
                System.out.println(displayMargin +"Hmmm ... I couldn't find any adjacent, untested cells.  (Very strange!)  I'll randomly pick a cell: (" + targetCellIndex.getRowIndex() + "," + targetCellIndex.getColumnIndex() + ")");

            }
        }
        // System.out.println("***   searchDirection of attacked cell: " + this.gridCellArray[arrayIndex(targetCellIndex.getRowIndex())][arrayIndex(targetCellIndex.getColumnIndex())].searchDirection);
        return targetCellIndex;
    }




    public indexPair evaluateIntelRandom () {

        // Return a random grid location that has not already been attacked.  Use this method of finding a target if there are no hit but unsunk cells.
        // System.out.println("---- evaluateIntelRandom ------");


        boolean newTarget = false;

        Random random_int = new Random();
        int randomGridRowIndex=0;
        int randomGridColumnIndex=0;
        indexPair randomCell = new indexPair (0,0);
        int shortestUnsunkShipLength = findShortestUnsunkShipLength();
        //System.out.println("  Shortest unsunk shiplength = " + shortestUnsunkShipLength);

        while (newTarget==false) {
            randomGridRowIndex = random_int.nextInt(gridHeight) + 1;        // Generate random int between 1 and gridHeight
            randomGridColumnIndex = random_int.nextInt(gridWidth) + 1;      // Generate random int between 1 and gridWidth
            randomCell.putRowIndex(randomGridRowIndex);
            randomCell.putColumnIndex(randomGridColumnIndex);

            //System.out.println("Random grid index: " + randomGridRowIndex + "  " + randomGridColumnIndex);
            if (!wasPreviouslyAttacked(randomGridRowIndex,randomGridColumnIndex)) {
                boolean foundSpaceForAShip = false;
                for (int direction = 0; direction <= 3 & !foundSpaceForAShip; direction++) {
                    if (existsAdjacentUntestedCells (direction, shortestUnsunkShipLength, randomCell)) {
                        foundSpaceForAShip = true;
                    }
                }
                if (foundSpaceForAShip = true) {
                    newTarget=true;
                }
            }
        }
        // System.out.println("Random grid index: " + randomGridRowIndex + "," + randomGridColumnIndex);
        //indexPair gridIndexPair = new indexPair (randomGridRowIndex,randomGridColumnIndex);

        // System.out.println("---- EXIT evaluateIntelRandom ------");

        return randomCell;
    }


    public void updateIntel (indexPair gridIndexPair, boolean[] attackResult) { // int gridRowIndex, int gridColumnIndex

        // Record attack results in the player's intel map.
        // System.out.println(" ---- updateIntel ------");
        int gridRowIndex = gridIndexPair.getRowIndex();
        int gridColumnIndex = gridIndexPair.getColumnIndex();
        int direction;
        // System.out.println("*** Attack results: " + attackResult[0] + attackResult[1] + attackResult[2]);
        if (this.gridIndexIsValid(gridRowIndex, gridColumnIndex)) {                 // battleZone_A.gridIndexIsValid(gridIndex)             // Test for index validity (The "attack" method also checks for valid range, so the test here is redundant.)
            if (wasPreviouslyAttacked(gridRowIndex, gridColumnIndex)) {             //      (this.gridCellArray[gridIndex[0]-1][gridIndex[1]-1].attacked == true) {
                System.out.println("   (BTW, you already attacked this cell.)");
            } else {
                // Update the EnemyFleetIntel data
                int arrayRowIndex = gridRowIndex -1 ;
                int arrayColumnIndex = gridColumnIndex -1 ;
                this.gridCellArray[arrayRowIndex][arrayColumnIndex].attacked = true;
                this.gridCellArray[arrayRowIndex][arrayColumnIndex].hasShip = attackResult[0];
                this.gridCellArray[arrayRowIndex][arrayColumnIndex].sinkShipPoint = attackResult[1];

                // this.gridCellArray[arrayRowIndex][arrayColumnIndex].shipSunk = attackResult[1];    Make this update in the markShipSegmentsSunk method.
                // this.gridCellArray[arrayRowIndex][arrayColumnIndex].searchDirection was set when cells adjacent to a hit cell were tested.

                // int orientation;            // The suspected orientation of the ship that contains this cell. A ship spans at least two cells.  0 = north-south, 1 = east-west, -1 = TBD
                // int confirmedShipLength;    // Reserved, not yet used
            }
            // System.out.println("---- EXIT updateIntel ------");
        } else {
            System.out.println("! Invalid index: " + gridRowIndex + "," + gridColumnIndex + ".  Choose row 1-"+ gridHeight + "; column 1-" + gridWidth);
        }
    }

    public int markShipSegmentsSunk (indexPair sinkPoint, String displayMargin) {

        // Called from Main, and only by the computer player. (The human player must figure out which ship sections are sunk)
        // Return the (guessed) length of the sunk ship.

        // When a hit sinks a ship, mark the other sections of the ship as SUNK.
        // We'll guess that all connected, hit but unsunk sections that align with the search direction are part of the same ship.
        // That guess could be wrong if two ships touch.  In that case we could erroneously mark a section (but only 1 section) of another ship SUNK.  Eventually we'll find the other sections of that ship.

        int direction = this.gridCellArray[arrayIndex(sinkPoint.getRowIndex())][arrayIndex(sinkPoint.getColumnIndex())].searchDirection;
        int sunkShipLength = 0;
        // System.out.println("***   markShipSegmentsSunk: searchDirection of sunk cell: " + direction);
        int gridRow = sinkPoint.getRowIndex();
        int gridColumn = sinkPoint.getColumnIndex();
        // System.out.println("***   markShipSegmentsSunk: origin: (" + gridRow + "," + gridColumn + ")");
        boolean foundBoundary = false;

        switch(direction)
        {
            case 0 :        // The last ship section was attacked (and sunk) when searching North.  Mark "sunk" on all connected, hit but unsunk sections to the south.
                System.out.println(displayMargin +" I'll guess that all connected ship segments from (" + gridRow + "," + gridColumn + ") southward are part of 1 ship, which is now sunk.");
                System.out.println(displayMargin);
                for (int i = gridRow; foundBoundary == false; i++) {
                    if (this.gridIndexIsValid(i,gridColumn)) {
                        // System.out.println("***  (" + i + "," + gridColumn + ")" + this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].attacked + this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].hasShip + this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].shipSunk);
                        if (this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].attacked & this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].hasShip & !this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].shipSunk) { // If the section is hit but not sunk ...
                            this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].shipSunk = true;
                            // System.out.println("***    Mark (" + i + "," + gridColumn + ") as SUNK.");
                            sunkShipLength++;
                        } else {
                            foundBoundary = true;   // Anything else is a boundary: untested, empty, sunk
                        }
                    } else {
                        foundBoundary = true;       // Off the grid is also a boundary
                    }
                }
                break;
            case 1 :        // The last ship section was attacked (and sunk) when searching East.  Mark "sunk" on all connected, hit but unsunk sections to the west.
                System.out.println(displayMargin + "I'll guess that all connected ship segments from (" + gridRow + "," + gridColumn + ") westward are part of 1 ship, which is now sunk.");
                for (int j = gridColumn; foundBoundary == false; j--) {
                    if (this.gridIndexIsValid(gridRow,j)) {
                        // System.out.println("***  (" + gridRow + "," + j + ")" + this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].attacked + this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].hasShip + this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].shipSunk);
                        if (this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].attacked & this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].hasShip & !this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].shipSunk) { // If the section is hit but not sunk ...
                            this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].shipSunk = true;
                            // System.out.println("***    Mark (" + gridRow + "," + j + ") as SUNK.");
                            sunkShipLength++;
                        } else {
                            foundBoundary = true;   // Anything else is a boundary: untested, empty, sunk
                        }
                    } else {
                        foundBoundary = true;       // Off the grid is also a boundary
                    }
                }
                break;
            case 2 :        // The last ship section was attacked (and sunk) when searching South.  Mark "sunk" on all connected, hit but unsunk sections to the north.
                System.out.println(displayMargin + "I'll guess that all connected ship segments from (" + gridRow + "," + gridColumn + ") northward are part of 1 ship, which is now sunk.");
                for (int i = gridRow; foundBoundary == false; i--) {
                    if (this.gridIndexIsValid(i,gridColumn)) {
                        // System.out.println("***  (" + i + "," + gridColumn + ")" + this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].attacked + this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].hasShip + this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].shipSunk);
                        if (this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].attacked & this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].hasShip & !this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].shipSunk) { // If the section is hit but not sunk ...
                            this.gridCellArray[arrayIndex(i)][arrayIndex(gridColumn)].shipSunk = true;
                            // System.out.println("***    Mark (" + i + "," + gridColumn + ") as SUNK.");
                            sunkShipLength++;
                        } else {
                            foundBoundary = true;   // Anything else is a boundary: untested, empty, sunk
                        }
                    } else {
                        foundBoundary = true;       // Off the grid is also a boundary
                    }
                }
                break;
            case 3 :        // The last ship section was attacked (and sunk) when searching West.  Mark "sunk" on all connected, hit but unsunk sections to the east.
                System.out.println(displayMargin + "I'll guess that all connected ship segments from (" + gridRow + "," + gridColumn + ") eastward are part of 1 ship, which is now sunk.");
                for (int j = gridColumn; foundBoundary == false; j++) {
                    if (this.gridIndexIsValid(gridRow,j)) {
                        // System.out.println("***  (" + gridRow + "," + j + ")" + this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].attacked + this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].hasShip + this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].shipSunk);
                        if (this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].attacked & this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].hasShip & !this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].shipSunk) { // If the section is hit but not sunk ...
                            this.gridCellArray[arrayIndex(gridRow)][arrayIndex(j)].shipSunk = true;
                            // System.out.println("***    Mark (" + gridRow + "," + j + ") as SUNK.");
                            sunkShipLength++;
                        } else {
                            foundBoundary = true;   // Anything else is a boundary: untested, empty, sunk
                        }
                    } else {
                        foundBoundary = true;       // Off the grid is also a boundary
                    }
                }
                break;
            default :
                System.out.println("! Invalid direction in markShipSegmentsSunk: " + direction);
        }
        return sunkShipLength;
    }






    public boolean existsHitNotSunkCell () {

        indexPair hitNotSunkGridIndexPair;
        hitNotSunkGridIndexPair = new indexPair (0,0);
        boolean found = false;

        for (int i = 0; i <= this.arrayIndex(gridHeight) & found==false; i++) {
            for (int j = 0; j <= this.arrayIndex(gridWidth) & found==false; j++) {
                if (this.gridCellArray[i][j].hasShip & !this.gridCellArray[i][j].shipSunk) {
                    hitNotSunkGridIndexPair.putRowIndex(i+1);           // Convert array index to grid index by adding 1
                    hitNotSunkGridIndexPair.putColumnIndex(j+1);
                    found = true;
                    // System.out.println("Found hitNotSunk cell: " + hitNotSunkGridIndexPair.getRowIndex() + "," + hitNotSunkGridIndexPair.getColumnIndex());
                } else {
                    // nop
                }
            }

        }

        return  found;
    }


    private indexPair getHitNotSunkCell () {

        indexPair hitNotSunkGridIndexPair;
        hitNotSunkGridIndexPair = new indexPair (0,0);
        boolean found = false;

        for (int i = 0; i <= this.arrayIndex(gridHeight) & found==false; i++) {
            for (int j = 0; j <= this.arrayIndex(gridWidth) & found==false; j++) {
                if (this.gridCellArray[i][j].hasShip & !this.gridCellArray[i][j].shipSunk) {
                    hitNotSunkGridIndexPair.putRowIndex(i+1);           // Convert array index to grid index by adding 1
                    hitNotSunkGridIndexPair.putColumnIndex(j+1);
                    found = true;
                    //System.out.println("Found hitNotSunk cell: " + hitNotSunkGridIndexPair.getRowIndex() + "," + hitNotSunkGridIndexPair.getColumnIndex());
                } else {
                    // nop
                }
            }
        }
        if (found){
            //nop
        }
        return  hitNotSunkGridIndexPair;
    }





    public String directionInt2Text (int searchDirection) {
        String text = "";
        switch (searchDirection) {
            case 0:
                text = "North";
                break;
            case 1:
                text = "East";
                break;
            case 2:
                text = "South";
                break;
            case 3:
                text = "West";
                break;
            default:
                System.out.println("! UNEXPECTED direction VALUE in directionText = " + searchDirection);
                break;
        }
        return text;
    }


    public String orientation (int searchDirection) {
        String text = "";
        switch (searchDirection) {
            case 0:
            case 2:
                text = "North-South";
                break;
            case 1:
            case 3:
                text = "East-West";
                break;
            default:
                System.out.println("! UNEXPECTED direction VALUE in directionText = " + searchDirection);
                break;
        }
        return text;
    }


    public boolean gridIndexIsValid(int gridRow, int gridColumn) {
        //System.out.println("gridIndexIsValid: this.gridHeight = " + this.gridHeight + " this.gridWidth = " + this.gridWidth );
        if (gridRow <= this.gridHeight & gridRow > 0 & gridColumn <= this.gridWidth & gridColumn > 0) {
            //System.out.println("True " );
            return true;
        } else {
            //System.out.println("False " );
            return false;
        }
    }

    public boolean wasPreviouslyAttacked(int gridRow, int gridColumn) {
        int arrayRowIndex =  this.arrayIndex(gridRow);              // Convert grid index (1,2,3,...) to array index (0,1,2,...)
        int arrayColumnIndex =  this.arrayIndex(gridColumn);              // Convert grid index (1,2,3,...) to array index (0,1,2,...)


        //System.out.println("Determine if wasPreviouslyAttacked: " + gridRow + "," + gridColumn);

        if (this.gridCellArray[arrayRowIndex][arrayColumnIndex].attacked) {
            //System.out.println("True " );
            return true;
        } else {
            //System.out.println("False " );
            return false;
        }
    }


    public static Scanner sc = new Scanner(System.in);

    public static void promptToContinue(String message) {
        System.out.print(message);
        String line = sc.nextLine();
    }


    public int shipSectionSegmentLength (int direction, int length, indexPair gridIndexPair) {

        // How many adjacent hit but not sunk cells are there?
        // Recursive calls; start by passing length = 0 (?)

        //System.out.println("---- shipSectionSegmentLength ------");

        //System.out.println("length1 = " + length);
        //System.out.println("direction = " + direction);

        indexPair newGridIndexPair = new indexPair (gridIndexPair.getRowIndex(), gridIndexPair.getColumnIndex());       // Initialize the new index as the "old" index
        int i = arrayIndex(gridIndexPair.getRowIndex());
        int j = arrayIndex(gridIndexPair.getColumnIndex());
        //System.out.println("i = " + i);
        //System.out.println("j = " + j);
        if (i>=0 & j>=0) {
            //System.out.println("hasShip = " + this.gridCellArray[i][j].hasShip);
            if (this.gridCellArray[i][j].hasShip) {
                int segLength = length + 1;
                //indexPair newGridIndexPair = new indexPair (gridIndexPair.getRowIndex() - 1, gridIndexPair.getColumnIndex());

                //System.out.println("length2 = " + segLength);
                switch(direction)
                {
                    case 0 :        // direction = north
                        newGridIndexPair.putRowIndex(gridIndexPair.getRowIndex() -1) ;         // Decrease the grid row index by 1 (move north)
                        //System.out.println("direction = north");
                        break;
                    case 1 :        // direction = east
                        newGridIndexPair.putColumnIndex(gridIndexPair.getColumnIndex() +1) ;   // Increase the grid column index by 1 (move east)
                        //System.out.println("direction = east");
                        break;
                    case 2 :        // direction = south
                        newGridIndexPair.putRowIndex(gridIndexPair.getRowIndex() +1) ;         // Increase the grid row index by 1 (move north)
                        //System.out.println("direction = south");
                        break;
                    case 3 :        // direction = west
                        newGridIndexPair.putColumnIndex(gridIndexPair.getColumnIndex() -1) ;   // Decrease the grid column index by 1 (move west)
                        //System.out.println("direction = west");
                        break;
                    default :
                        System.out.println("! Invalid direction");
                }
                return shipSectionSegmentLength (direction, segLength, newGridIndexPair);
            } else {
                System.out.println("No ship: (" + gridIndexPair.getRowIndex() + "," + gridIndexPair.getColumnIndex() + ")" );
            }
            //System.out.println("---- EXIT shipSectionSegmentLength ------");
            return length;
        }  else {               // Out of bounds
            //System.out.println("---- EXIT shipSectionSegmentLength ------");
            return length;
        }


    }


    public boolean existsAdjacentUntestedCells (int direction, int length, indexPair gridIndexPair) {

        // Are there enough adjacent untested cells to fit a ship of a passed length?

        //System.out.println("---- existsAdjacentUntestedCells ------");

        //System.out.println("length1 = " + length);
        //System.out.println("direction = " + direction);

        //indexPair newGridIndexPair = new indexPair (gridIndexPair.getRowIndex(), gridIndexPair.getColumnIndex());       // Initialize the new index as the "old" index
        int i = arrayIndex(gridIndexPair.getRowIndex());
        int j = arrayIndex(gridIndexPair.getColumnIndex());
        boolean thereIsRoom = false;
        //System.out.println("i = " + i);
        //System.out.println("j = " + j);
        //System.out.println("length = " + length);
        if (i>=0 & j>=0) {
            //System.out.println("attacked = " + this.gridCellArray[i][j].attacked);
            boolean foundBoundary = false;
            switch(direction)
            {
                case 0 :        // direction = north
                    //System.out.println("direction = north");
                    for (int k = 1; k < length & foundBoundary == false; k++) {          // While the index has been incremented less than the length and no boundary found ....
                        //System.out.println("k top = " + k);
                        if (i-k < 0) {                                                      // If off the grid, that's a boundary
                            foundBoundary = true;
                        } else {
                            if (this.gridCellArray[i-k][j].attacked) {                          // If the cell was tested (attacked), that's a boundary
                                foundBoundary = true;
                            } else {
                                // nop.  Keep going.
                            }
                        }
                        //System.out.println("k bottom = " + k);
                    }
                    //if (foundBoundary = false) {                                                          // If we incremented the index by length and found no boundary ...
                      //  return true;                                                                        // There are <length-1> untested adjacent spaces
                    //}

                    break;
                case 1 :        // direction = east
                    //System.out.println("direction = east");
                    for (int k = 1; (k < length) & (foundBoundary == false); k++) {          // While the index has been incremented less than the length and no boundary found ....
                        //System.out.println("k = " + k);
                        if (j+k >= this.gridWidth) {                                        // If off the grid, that's a boundary
                            foundBoundary = true;
                            //System.out.println("fb = true; j+k > gridWidth");
                        } else {
                            if (this.gridCellArray[i][j+k].attacked) {                          // If the cell was tested (attacked), that's a boundary
                                foundBoundary = true;
                                //System.out.println("fb = true; attacked");
                            } else {
                                // nop.  Keep going.
                                //System.out.println("nop");
                            }
                        }
                    }
                    //if (foundBoundary = false) {                                                          // If we incremented the index by length and found no boundary ...
                        //return true;                                                                        // There are <length-1> untested adjacent spaces
                    //}
                    break;
                case 2 :        // direction = south
                    //System.out.println("direction = south");
                    for (int k = 1; (k < length) & (foundBoundary == false); k++) {          // While the index has been incremented less than the length and no boundary found ....
                        //System.out.println("k = " + k);
                        if (i+k >= this.gridHeight) {                                                      // If off the grid, that's a boundary
                            foundBoundary = true;
                        } else {
                            if (this.gridCellArray[i+k][j].attacked) {                          // If the cell was tested (attacked), that's a boundary.  The calling method checks before calling this method, so this condition shouldn't come up, but let's be safe ... .
                                foundBoundary = true;
                            } else {
                                // nop.  Keep going.
                            }
                        }
                    }
                    //if (foundBoundary = false) {                                                          // If we incremented the index by length and found no boundary ...
                        //return true;                                                                        // There are <length-1> untested adjacent spaces
                    //}
                    break;
                case 3 :        // direction = west
                    //System.out.println("direction = west");
                    for (int k = 1; (k < length) & (foundBoundary == false); k++) {          // While the index has been incremented less than the length and no boundary found ....
                        //System.out.println("k = " + k);
                        if (j-k < 0) {                                        // If off the grid, that's a boundary
                            foundBoundary = true;
                        } else {
                            if (this.gridCellArray[i][j-k].attacked) {                          // If the cell was tested (attacked), that's a boundary
                                foundBoundary = true;
                            } else {
                                // nop.  Keep going.
                            }
                        }
                    }
                    ///if (foundBoundary = false) {                                                          // If we incremented the index by length and found no boundary ...
                        //return true;                                                                        // There are <length-1> untested adjacent spaces
                    //}
                    break;
                default :
                    System.out.println("! Invalid direction");
            }
            if (foundBoundary == false) {
                thereIsRoom = true;
                //System.out.println("thereIsRoom = true");
            }
        }  else {               // Out of bounds
            System.out.println("! Invalid direction");
        }
        //System.out.println("thereIsRoom = " + thereIsRoom);
        return thereIsRoom;
    }



}




