package soen6441.team01.warzone.controller;

/**
 * The main entry for the Warzone game (console version). To run the game from
 * the console command line; 1) build the target jar file (using Maven install)
 * 2) open a command window and goto the target directory 3) paste the following
 * line on the command line <b>java -classpath controller-0.0.1-SNAPSHOT.jar
 * soen6441.team01.warzone.controller.App</b>
 *
 */
public class App {
	/**
	 * main entry point of the application
	 * 
	 * @param args passed in by the operating system
	 */
	public static void main(String[] args) {
		GameEngine l_game_engine = new GameEngine();
		l_game_engine.startGame();
	}
}
