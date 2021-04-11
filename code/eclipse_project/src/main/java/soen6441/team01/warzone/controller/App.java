package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.model.GameEngine;

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
	 * @throws Exception unexpected error
	 */
	public static void main(String[] args) throws Exception {
		GameEngine l_game_engine = new GameEngine();
		while (l_game_engine != null) {
			l_game_engine = l_game_engine.startGame();
			if (l_game_engine != null) {
				// if l_game_engine is not null that means a new game was loaded.
				// the next phase in the game engine will be the issue order phase for a single
				// game. this is fine, but certain context hasn't been setup yet, therefore
				// we'll invoke initGamePlay() in SingleGameController to initialize itself in
				// order for the game play phases to work correctly.
				l_game_engine.getControllerFactory().getSingleGameController().initGamePlay();
			}
		}
	}
}
