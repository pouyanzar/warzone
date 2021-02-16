package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.model.contracts.*;

/**
 * Warzone MVC main controller. Manages the coordination and progression of the different stages
 * of the game. As well as supporting user actions (ie. interactions, genstures).
 */
public class GameEngine {
	private IMapModel d_map;
	private IPlayerModel d_player;
	
	public GameEngine(IMapModel p_map, IPlayerModel p_player) {
		d_map = p_map;
		d_player = p+player;
	}

}
