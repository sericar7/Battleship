public class enemyShip {


    // The computer player cannot see the human player's ship class, but creates their own class to track which of their opponent's ships are sunk.
    // When the computer's attack yields a "sink", the computer uses available info to guess the length of the sunk ship and modify the shipSunk attrib in the computer player's enemyShip instance.
    // It is a guess because an unsunk ship that touches the sunk ship could be (erroneously) counted in the length of the sunk ship.
    //
    // This class is not used to determine if a single ship is sunk or to determine if all ships are sunk.  (That is done by examining all cells in the battle zone: see the method boolean foundUnattackedSectionOfThisShip in the battleZone class.)
    //


    // String shipName;               // Name of ship is not important for the purpose of tracking which ship sizes have been sunk and which have not been sunk.
    int shipLength;                   // Length of ship. In the Battleship game, from 2 - 5.  This attrib is used by opponent (in opponent's instance) when tracking which ships are sunk.
    boolean shipSunk;                 // This attrib is used only in the opponent's instance.

    public enemyShip(int length) { // height and width of the enemy's battle zone

        // this.shipName = name;
        this.shipLength = length;
        this.shipSunk = false;

    }
}
