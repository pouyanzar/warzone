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
	public static boolean IsEmpty(String p_string) {
		if (p_string == null)
			return true;
		if (p_string.trim().equals(""))
			return true;
		return false;
	}
	
	/**
	 * Parse and extract the 1st word from the specified sentence. 
	 * 
	 * @param p_sentence the string containing words (ie separated by space(s)
	 * @return String[0] = 1st word, String[1] = rest of sentence
	 */
	public static String[] GetFirstWord(String p_sentence) {
		String[] l_reply = new String[2];
		if (p_sentence == null) {
			l_reply[0] = "";
			l_reply[1] = "";
			return l_reply;
		}

		p_sentence = p_sentence.trim();
		int l_idx = p_sentence.indexOf(' ');
		if (l_idx < 1 && p_sentence.length() > 1) {
			l_reply[0] = p_sentence;
			l_reply[1] = "";
			return l_reply;
		}

		try {
			l_reply[0] = p_sentence.substring(0, l_idx);
		} catch (Exception ex) {
			l_reply[0] = "";
			l_reply[1] = "";
			return l_reply;
		}

		try {
			l_idx++;
			l_reply[1] = p_sentence.substring(l_idx, p_sentence.length()).trim();
		} catch (Exception ex) {
			l_reply[1] = "";
			return l_reply;
		}
		return l_reply;
	}
}
