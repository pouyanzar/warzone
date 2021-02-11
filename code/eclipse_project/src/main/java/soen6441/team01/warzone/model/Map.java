package soen6441.team01.warzone.model;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
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
	public void editMap(String map_filename) throws IOException
	{
		//show_current_directory();
		//walk(".");
		
		List<String> list = Files.readAllLines(new File(map_filename).toPath(), Charset.defaultCharset() );
		return;
	}
	
	// to delete
	private void show_current_directory()
	{
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
	}

	// to delete
    public void walk( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );
                System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {
                System.out.println( "File:" + f.getAbsoluteFile() );
            }
        }
    }
    
	
}
