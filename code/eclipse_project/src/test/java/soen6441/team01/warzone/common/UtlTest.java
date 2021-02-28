package soen6441.team01.warzone.common;

import static org.junit.Assert.*;

import org.junit.Test;

import soen6441.team01.warzone.model.GameEngine;

public class UtlTest {

	/**
	 * Simple test to see if the current working directory can be listed to the
	 * console. Also useful during the build (locally and remotely) to see what is
	 * the current working dorectory.
	 */
	@Test
	public void test_printCurrentDirectory() {
		Utl.printCurrentDirectory();
		assertTrue(true);
	}

	/**
	 * test the IsValidMapName method
	 */
	@Test
	public void test_IsValidMapName() {
		assertTrue(Utl.isValidMapName("Canada"));
		assertTrue(Utl.isValidMapName("North-America"));
		assertTrue(Utl.isValidMapName("North_America_1"));
		assertTrue(Utl.isValidMapName("Planet1"));
		assertFalse(Utl.isValidMapName(null));
		assertFalse(Utl.isValidMapName(""));
		assertFalse(Utl.isValidMapName(" PlantX"));
		assertFalse(Utl.isValidMapName("PlantX "));
		assertFalse(Utl.isValidMapName(" Plant X"));
		assertFalse(Utl.isValidMapName("!PlantX"));
		assertFalse(Utl.isValidMapName("123"));
		assertFalse(Utl.isValidMapName("12_3"));
	}

	/**
	 * Run multiple tests on getFirstWord with 0 words
	 */
	@Test
	public void test_getFirstWord_1() {
		String[] l_reply = Utl.getFirstWord(null);
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.getFirstWord("");
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.getFirstWord(" ");
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Run multiple tests on getFirstWord with 1 word
	 */
	@Test
	public void test_getFirstWord_2() {
		String[] l_reply = Utl.getFirstWord("help");
		assertEquals("help", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.getFirstWord("exit ");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.getFirstWord(" exit");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.getFirstWord(" exit ");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Run multiple tests on getFirstWord with more than 1 word
	 */
	@Test
	public void test_getFirstWord_3() {
		String[] l_reply = Utl.getFirstWord("help me");
		assertEquals("help", l_reply[0]);
		assertEquals("me", l_reply[1]);
		l_reply = Utl.getFirstWord("editcountry -add Canada");
		assertEquals("editcountry", l_reply[0]);
		assertEquals("-add Canada", l_reply[1]);
		l_reply = Utl.getFirstWord(" editcountry  -add  Canada");
		assertEquals("editcountry", l_reply[0]);
		assertEquals("-add  Canada", l_reply[1]);
		l_reply = Utl.getFirstWord("editcountry  -add   Canada ");
		assertEquals("editcountry", l_reply[0]);
		assertEquals("-add   Canada", l_reply[1]);
	}

	/**
	 * Run multiple tests on getFirstWord with more than 1 word and getting
	 * subsequent words from sentence.
	 */
	@Test
	public void test_getFirstWord_wierd_calls() {
		String[] l_reply;
		l_reply = Utl.getFirstWord(null);
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.getFirstWord("");
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.getFirstWord("1");
		assertEquals("1", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Run multiple tests on getFirstWord with more than 1 word and getting
	 * subsequent words from sentence.
	 */
	@Test
	public void test_getFirstWord_many_words_multiple_calls() {
		String[] l_reply = Utl.getFirstWord("editcontinent -add 1 North-America -remove 1   -add  99   Europe");
		assertEquals("editcontinent", l_reply[0]);
		assertEquals("-add 1 North-America -remove 1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("-add", l_reply[0]);
		assertEquals("1 North-America -remove 1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("1", l_reply[0]);
		assertEquals("North-America -remove 1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("North-America", l_reply[0]);
		assertEquals("-remove 1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("-remove", l_reply[0]);
		assertEquals("1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("1", l_reply[0]);
		assertEquals("-add  99   Europe", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("-add", l_reply[0]);
		assertEquals("99   Europe", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("99", l_reply[0]);
		assertEquals("Europe", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("Europe", l_reply[0]);
		assertEquals("", l_reply[1]);

		l_reply = Utl.getFirstWord(l_reply[1]);
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Test ConvertToInteger with multiple values
	 */
	@Test
	public void test_ConvertToInteger_with_multiple_value() {
		assertTrue(Utl.convertToInteger("1")==1);
		assertTrue(Utl.convertToInteger(" 1")==1);
		assertTrue(Utl.convertToInteger("1 ")==1);
		assertTrue(Utl.convertToInteger(" 1 ")==1);
		assertTrue(Utl.convertToInteger("1234567")==1234567);
		assertTrue(Utl.convertToInteger("-1234567")==-1234567);
		assertTrue(Utl.convertToInteger(null)==Integer.MAX_VALUE);
		assertTrue(Utl.convertToInteger("")==Integer.MAX_VALUE);
		assertTrue(Utl.convertToInteger("-add")==Integer.MAX_VALUE);
		assertTrue(Utl.convertToInteger("-1 add")==Integer.MAX_VALUE);
		assertTrue(Utl.convertToInteger("John")==Integer.MAX_VALUE);
	}
	
	/**
	 * Test nthIndexOf with multiple values
	 */
	@Test
	public void test_nthIndexOf_1() {
		assertTrue(Utl.nthIndexOf("0123456789", "x", 1)==-1);
		assertTrue(Utl.nthIndexOf("123[567[90", "x", 1)==-1);
		assertTrue(Utl.nthIndexOf("123[567[90", "[", 1)==3);
		assertTrue(Utl.nthIndexOf(" xa[ y [z ", "[", 1)==3);
		assertTrue(Utl.nthIndexOf(" xa[ y [z ", "[", 2)==7);
	}
	
	/**
	 * Test shiftSubstring with multiple values
	 */
	@Test
	public void test_shiftSubstring_1() {
		assertTrue(Utl.shiftSubstring("0123456789", 4, 4, ' ').equals("0123456789"));
		assertTrue(Utl.shiftSubstring("0123456789", 4, 3, ' ').equals("0123456789"));
		assertTrue(Utl.shiftSubstring("0123456789", 3, 4, ' ').equals("012 3456789"));
		assertTrue(Utl.shiftSubstring("0123456789", 3, 7, ' ').equals("012    3456789"));
		assertTrue(Utl.shiftSubstring("0123456789", 3, 7, '-').equals("012----3456789"));
	}}
