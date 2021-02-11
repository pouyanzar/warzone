package soen6441.team01.warzone.model;

import java.io.IOException;

/**
 * Defines the Map model interface used by the controller to invoke Map command
 * and updates
 *
 */
public interface IMapModel
{
	void editMap(String map_filename) throws Exception;
}
