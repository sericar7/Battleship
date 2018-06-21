public class ship {

    // Assume: Both players have the same type of fleet: same number of ships, same number of ships of each length.
    //
    // This class is used to populate a battle zone: the ship name is added <length> adjacent cells in the battle zone.  The ship owner can see the ship name in their battle zone.
    // After populating the battle zone, the ship owner does not use this class.
    //
    // The computer player cannot see the human player's ship instance, but creates their own instance to track which of their opponent's ships are sunk.  See the enemyShip class.
    // This class is not used to determine if a single ship is sunk or to determine if all ships are sunk.  (That is done by examining all cells in the battle zone: see the method boolean foundUnattackedSectionOfThisShip in the battleZone class.)
    //


    String shipName;                  // Name of ship.  Keep it small (1 char?) to fit in CLI grid.  This attrib is used only in the ship owner's instance and only when populating the battle zone.
    int shipLength;                   // Length of ship. In the Battleship game, from 2 - 5.  This attrib is used by ship owner when populating the battle zone and by opponent (in opponent's instance) when tracking which ships are sunk.
    // boolean shipSunk;                 // This attrib is used only in the opponent's instance.

    public ship(String name, int length) { // height and width of the enemy's battle zone

        this.shipName = name;
        this.shipLength = length;
        // this.shipSunk = false;

    }
}
