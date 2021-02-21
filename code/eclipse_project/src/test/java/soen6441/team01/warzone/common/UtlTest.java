package soen6441.team01.warzone.common;

import static org.junit.Assert.*;

import org.junit.Test;

import soen6441.team01.warzone.controller.GameEngine;

public class UtlTest {

	/**
	 * Simple test to see if the current working directory can be listed to the
	 * console. Also useful during the build (locally and remotely) to see what is
	 * the current working dorectory.
	 */
	@Test
	public void test_printCurrentDirectory() {
		Utl.PrintCurrentDirectory();
		assertTrue(true);
	}

	/**
	 * test the IsValidMapName method
	 */
	@Test
	public void test_IsValidMapName() {
		assertTrue(Utl.IsValidMapName("Canada"));
		assertTrue(Utl.IsValidMapName("North-America"));
		assertTrue(Utl.IsValidMapName("North_America"));
		assertTrue(Utl.IsValidMapName("Planet1"));
		assertFalse(Utl.IsValidMapName(null));
		assertFalse(Utl.IsValidMapName(""));
		assertFalse(Utl.IsValidMapName(" PlantX"));
		assertFalse(Utl.IsValidMapName("PlantX "));
		assertFalse(Utl.IsValidMapName(" Plant X"));
		assertFalse(Utl.IsValidMapName("*PlantX"));
	}

	/**
	 * Run multiple tests on getFirstWord with 0 words
	 */
	@Test
	public void test_getFirstWord_1() {
		String[] l_reply = Utl.GetFirstWord(null);
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.GetFirstWord("");
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.GetFirstWord(" ");
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Run multiple tests on getFirstWord with 1 word
	 */
	@Test
	public void test_getFirstWord_2() {
		String[] l_reply = Utl.GetFirstWord("help");
		assertEquals("help", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.GetFirstWord("exit ");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.GetFirstWord(" exit");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.GetFirstWord(" exit ");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Run multiple tests on getFirstWord with more than 1 word
	 */
	@Test
	public void test_getFirstWord_3() {
		String[] l_reply = Utl.GetFirstWord("help me");
		assertEquals("help", l_reply[0]);
		assertEquals("me", l_reply[1]);
		l_reply = Utl.GetFirstWord("editcountry -add Canada");
		assertEquals("editcountry", l_reply[0]);
		assertEquals("-add Canada", l_reply[1]);
		l_reply = Utl.GetFirstWord(" editcountry  -add  Canada");
		assertEquals("editcountry", l_reply[0]);
		assertEquals("-add  Canada", l_reply[1]);
		l_reply = Utl.GetFirstWord("editcountry  -add   Canada ");
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
		l_reply = Utl.GetFirstWord(null);
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.GetFirstWord("");
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = Utl.GetFirstWord("1");
		assertEquals("1", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Run multiple tests on getFirstWord with more than 1 word and getting
	 * subsequent words from sentence.
	 */
	@Test
	public void test_getFirstWord_many_words_multiple_calls() {
		String[] l_reply = Utl.GetFirstWord("editcontinent -add 1 North-America -remove 1   -add  99   Europe");
		assertEquals("editcontinent", l_reply[0]);
		assertEquals("-add 1 North-America -remove 1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("-add", l_reply[0]);
		assertEquals("1 North-America -remove 1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("1", l_reply[0]);
		assertEquals("North-America -remove 1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("North-America", l_reply[0]);
		assertEquals("-remove 1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("-remove", l_reply[0]);
		assertEquals("1   -add  99   Europe", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("1", l_reply[0]);
		assertEquals("-add  99   Europe", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("-add", l_reply[0]);
		assertEquals("99   Europe", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("99", l_reply[0]);
		assertEquals("Europe", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("Europe", l_reply[0]);
		assertEquals("", l_reply[1]);

		l_reply = Utl.GetFirstWord(l_reply[1]);
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Test ConvertToInteger with multiple values
	 */
	@Test
	public void test_ConvertToInteger_with_multiple_value() {
		assertTrue(Utl.ConvertToInteger("1")==1);
		assertTrue(Utl.ConvertToInteger(" 1")==1);
		assertTrue(Utl.ConvertToInteger("1 ")==1);
		assertTrue(Utl.ConvertToInteger(" 1 ")==1);
		assertTrue(Utl.ConvertToInteger("1234567")==1234567);
		assertTrue(Utl.ConvertToInteger("-1234567")==-1234567);
		assertTrue(Utl.ConvertToInteger(null)==Integer.MAX_VALUE);
		assertTrue(Utl.ConvertToInteger("")==Integer.MAX_VALUE);
		assertTrue(Utl.ConvertToInteger("-add")==Integer.MAX_VALUE);
		assertTrue(Utl.ConvertToInteger("-1 add")==Integer.MAX_VALUE);
		assertTrue(Utl.ConvertToInteger("John")==Integer.MAX_VALUE);
	}
	
	
}
