public class gridCell {
    boolean attacked;   // Has opponent attacked this cell?
    boolean sunk;       // Has the opponent sunk the ship that inhabits this cell?  This attribute can be seen only by the ship owner.
    boolean hasShip;    // Does a (section of) ship inhabit this cell?
    String shipName;    // A ship can span several cells.  All sections of a ship have the same name

    public gridCell() {

        this.attacked = false;
        this.sunk = false;
        this.hasShip = false;   // Used by EnemyFleetIntel class
        this.shipName = "";
    }


}
