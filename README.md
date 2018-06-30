# Battleship
Battleship game that runs in a command window.  
Java implementation.

Summary:
Battleship game for command line, implemented in Java.
1 person plays against the computer.
The computer shows how it assesses targets by "thinking out loud".
The grid is more easily viewed on larger viewports.


To run the Java program:
1. Download out\artifacts\Battleship_jar\Battleship.jar.
2. In a command prompt window, navigate to the folder that contains the JAR file.
3. In the command prompt window, enter the following command:
      java -jar Battleship.jar

The game runs only if your version of Java is compatible with mine.

	  
Defaults:
5X5 grid (A smaller grid makes for quicker games)


Options:
- During setup, player can opt to increase or decrease her grid size from the default size. A larger grid size gives the person an advantage over the computer.
- During setup, player can manually place ships or have them placed randomly.
- Very minor code changes in the Main.java file allow easy mods of the following attributes:
  - Default grid size			 			(modify constant gridSizeDefault)
  - Min and max grid sizes 					(modify constants gridSizeMin and gridSizeMax) gridSizeDefault
  - Number and size of ships in the fleet 	(modify ship names and sizes in the "makeFleet" method)


Note: In the traditional (commercial) version of the game:
- Each player's fleet includes 5 ships of 3 sizes: 5 cells, 4 cells, 3 cells (2#), 2 cells.
- The battle zone (grid) is 10X10.
