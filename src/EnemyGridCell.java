public class EnemyGridCell {
    boolean attacked;           // Has opponent attacked this cell?
    boolean hasShip;            // Does a (section of) ship inhabit this cell?
    boolean sinkShipPoint;      // Did an attack on this grid location result in a sink?  This attrib is unique to this class.
    boolean shipSunk;           // A line of adjacent ship sections have been hit and one end section recorded "hit and sink".
    int searchDirection;        // When a ship section is hit, record the direction in which the search for adjacent sections proceeds. 0 = north 1 = east, 2 = south, 3 = west, -1 = TBD
    int confirmedShipLength;    // Reserved, not yet used

    public EnemyGridCell() {

        this.attacked = false;
        this.hasShip = false;   // Used by EnemyFleetIntel class
        this.sinkShipPoint = false; // => true if this is the section where "sink" is reported.
        this.shipSunk = false;      // When a segment is reported sunk, this attrib becomes true for all connected hit but unsunk segments that align with the search direction.
        this.searchDirection = -1;
        this.confirmedShipLength = 0;
    }


}