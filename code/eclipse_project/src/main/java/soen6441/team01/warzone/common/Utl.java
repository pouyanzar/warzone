package soen6441.team01.warzone.common;

import java.io.File;
import java.util.ArrayList;

import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.model.entities.UserMessage;

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

	/**
	 * Checks that the p_map_name contains valid characters as defined by the
	 * Country and Continent names, i.e. letters, digits, '_', '-' (e.g. no spaces).
	 * Must contain at least 1 letter.
	 * 
	 * @param p_map_name the name of the map element to check
	 * @return true if the name is valid; otherwise false
	 */
	public static boolean isValidMapName(String p_map_name) {
		if (p_map_name == null || p_map_name == "") {
			return false;
		}
		int l_letter_ctr = 0;
		char[] l_chars = p_map_name.toCharArray();
		for (char l_ch : l_chars) {
			if (Character.isLetter(l_ch)) {
				l_letter_ctr++;
			}
			if (!Character.isLetter(l_ch) && !Character.isDigit(l_ch) && (l_ch == ' ' || l_ch == '!' || l_ch == '\\'
					|| l_ch == '/' || l_ch == '[' || l_ch == ']' || l_ch == ';')) {
				return false;
			}
		}

		// must have at least 1 letter in it
		if (l_letter_ctr < 1) {
			return false;
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

	/**
	 * Parse and extract the 1st word from the specified sentence.
	 * 
	 * @param p_sentence the string containing words (ie separated by space(s)
	 * @return String[0] = 1st word, String[1] = rest of sentence
	 */
	public static String[] getFirstWord(String p_sentence) {
		String[] l_reply = new String[2];
		if (p_sentence == null) {
			l_reply[0] = "";
			l_reply[1] = "";
			return l_reply;
		}

		p_sentence = p_sentence.trim();
		int l_idx = p_sentence.indexOf(' ');
		if (l_idx < 1 && p_sentence.length() > 0) {
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

	/**
	 * converts a string into an integer.
	 * 
	 * @param p_string the string to convert into an integer
	 * @return the integer value of the string; otherwise Integer.MAX_VALUE if the
	 *         string is not an integer
	 */
	public static int convertToInteger(String p_string) {
		int result;
		if (Utl.isEmpty(p_string)) {
			return Integer.MAX_VALUE;
		}
		p_string = p_string.trim();
		try {
			result = Integer.parseInt(p_string);
		} catch (NumberFormatException e) {
			result = Integer.MAX_VALUE;
		}
		return result;
	}

	/**
	 * Display a message to the system console
	 * 
	 * @param p_msg_type the type of message to display as defined by the enum
	 * @param p_message  the message to display to the user
	 */
	public static void consoleMessage(MessageType p_msg_type, String p_message) {
		switch (p_msg_type) {
		case None:
			System.out.println(p_message);
			break;
		case Informational:
			System.out.println("info: " + p_message);
			break;
		case Warning:
			System.out.println("warn: " + p_message);
			break;
		default:
			System.out.println("error: " + p_message);
			break;
		}
	}

	/**
	 * find the position of the nth occurrence of a substring
	 * 
	 * @param p_str    the string to check for substrings
	 * @param p_substr the substring to check for
	 * @param p_occ    the nth occurrence
	 * @return the position (zero-based) of the nth occurance of the specified
	 *         substring. -1 if substring doesn't exist.
	 */
	public static int nthIndexOf(String p_str, String p_substr, int p_occ) {
		int l_pos = -1;
		try {
			l_pos = p_str.indexOf(p_substr);
			while (--p_occ > 0 && l_pos != -1) {
				l_pos = p_str.indexOf(p_substr, l_pos + 1);
			}
		} catch (Exception ex) {
		}
		return l_pos;
	}

	/**
	 * Insert spaces at position l_pos to specified l_to_pos, shifting the rest fo
	 * the string as needed
	 * 
	 * @param l_line   the sting to shift
	 * @param l_pos    the position to start inserting spaces (zero-based)
	 * @param l_to_pos the ending position of the inserted spaces (zero-based)
	 * @return the string shifted as requested
	 */
	public static String shiftSubstring(String l_line, int l_pos, int l_to_pos) {
		StringBuilder l_str = new StringBuilder(l_line);
		try {
			int i = l_to_pos - l_pos;
			for (; i > 0; i--) {
				l_str.insert(l_pos, ' ');
			}
		} catch (Exception ex) {
		}
		return l_str.toString();
	}
	
	/**
	 * Justify the fields on the report
	 * 
	 * @param l_report  the report with the fields justified
	 * @param l_fld     the field identifier
	 * @param l_fld_num the nth field indicated by the identifier
	 * @return the justified report
	 */
	public static ArrayList<String> justifyField(ArrayList<String> l_report, String l_fld, int l_fld_num) {
		ArrayList<String> l_xreport = new ArrayList<String>();

		// figure out the position of the farthest field
		int l_max_fld_pos = 0;
		for (String l_line : l_report) {
			int l_pos = Utl.nthIndexOf(l_line, l_fld, l_fld_num);
			if (l_pos > l_max_fld_pos) {
				l_max_fld_pos = l_pos;
			}
		}

		// justify the field to the farthest position
		for (String l_line : l_report) {
			int l_pos = Utl.nthIndexOf(l_line, l_fld, l_fld_num);
			String l_xline = Utl.shiftSubstring(l_line, l_pos, l_max_fld_pos);
			l_xreport.add(l_xline);
		}

		return l_xreport;
	}
}