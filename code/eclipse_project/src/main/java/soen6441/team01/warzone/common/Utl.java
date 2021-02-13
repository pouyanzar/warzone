package soen6441.team01.warzone.common;

import java.io.File;

/**
 * Utility class holding commonly used general functions
 * 
 * @author John
 *
 */
public class Utl {
	/**
	 * Print the current directory to the console. Useful for debugging a unit test
	 * that references resources.
	 */
	public static void printCurrentDirectory() {
		System.out.println("current directory: " + System.getProperty("user.dir"));
	}

	/**
	 * Print the directory starting at the specified path to the console. Useful for
	 * debugging a unit test that references resources.
	 * 
	 * Code taken from:
	 * https://stackoverflow.com/questions/2056221/recursively-list-files-in-java
	 * 
	 * @param path the path where to list the directory contents from
	 */
	public static void printDirectory(String path) {
		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				printDirectory(f.getAbsolutePath());
				System.out.println("Dir:" + f.getAbsoluteFile());
			} else {
				System.out.println("File:" + f.getAbsoluteFile());
			}
		}
	}
	
	public void no_op() {}
	
}
