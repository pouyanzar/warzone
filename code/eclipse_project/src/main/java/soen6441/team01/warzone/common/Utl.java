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
	public static void PrintCurrentDirectory() {
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
	public static void PrintDirectory(String path) {
		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				PrintDirectory(f.getAbsolutePath());
				System.out.println("Dir:" + f.getAbsoluteFile());
			} else {
				System.out.println("File:" + f.getAbsoluteFile());
			}
		}
	}

	/**
	 * Checks that the p_map_name contains valid characters as defined by the
	 * Country and Continent names, i.e. letters, digits, '_', '-' (e.g. no spaces)
	 * 
	 * @param p_map_name the name of the map element to check
	 * @return true if the name is valid; otherwise false
	 */
	public static boolean IsValidMapName(String p_map_name) {
		if (p_map_name == null || p_map_name == "") {
			return false;
		}
		char[] l_chars = p_map_name.toCharArray();
		for (char l_ch : l_chars) {
			if (!Character.isLetter(l_ch) && !Character.isDigit(l_ch) && l_ch != '_' && l_ch != '-') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if a given string is null, empty or spaces
	 * 
	 * @param p_string string to check
	 * @return true = string is null, empty or spaces; otherwise false
	 */
	public static boolean isEmpty(String p_string) {
		if (p_string == null)
			return true;
		if (p_string.trim().equals(""))
			return true;
		return false;
	}
}
