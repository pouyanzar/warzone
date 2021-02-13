package soen6441.team01.warzone.model;

/**
 * Defines the Map model interface used by the controller to invoke Map command
 * and updates
 *
 */
public interface IMapModel {
	void editMap(String map_filename) throws Exception;

	void loadMap(String map_filename) throws Exception;
}
