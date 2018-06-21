# Battleship
Battleship game that runs in a command window.  Java implementation.

Summary:
Battleship game for command line, implemented in Java.
1 person plays against the computer.
The computer shows how it assesses targets by "thinking out loud".
The grid is designed for larger viewports.


To run the Java program:
1. Download and unzip the archive file (battleship.zip) from the \out\production\Battleship\ folder.
2. In a command prompt window, navigate to the folder that contains the unzipped files.
3. In the command prompt window, enter the following command:
      java Main


	  
Defaults:
5X5 grid (A smaller grid makes for quicker games)


Options:
During setup, player can opt to increase or decrease her grid size from the default size. A larger grid size gives the person an advantage over the computer.
During setup, player can manually place ships or have them placed randomly.


Minor code changes allow easy mods of the following attributes:
- Default grid size
- Min and max grid sizes
- Number and size of ships in the fleet 


Note: In the traditional (commercial) version of the game:
- Each player's fleet includes 5 ships of 3 sizes: 5 cells, 4 cells, 3 cells (2#), 2 cells.
- The battle zone (grid) is 10X10.
