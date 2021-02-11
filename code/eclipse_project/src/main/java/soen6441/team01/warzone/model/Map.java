package soen6441.team01.warzone.model;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * Manages Warzone Maps 
 *
 */
public class Map implements IMapModel, IMapModelView
{
	/**
	 * Loads a map from an existing “domination” map file, or creates a new map from scratch if the file does not exist.
	 *  
	 * @param map_filename the filename of the "domination" map file
	 * @throws IOException 
	 */
	public void editMap(String map_filename) throws Exception
	{
		List<String> list = Files.readAllLines(new File(map_filename).toPath(), Charset.defaultCharset() );
		// todo: parse the map file... (another method?)
		return;
	}
}
