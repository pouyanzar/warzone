package soen6441.team01.warzone.controller;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the GameEngine class
 *
 */
public class GameEngineTest {

	/**
	 * Run multiple tests on getCommandAndParams with 0 word commands 
	 */
	@Test
	public void test_getCommandAndParams_1() {
		GameEngine l_game_engine = new GameEngine();
		String[] l_reply = l_game_engine.getCommandAndParams(null);
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = l_game_engine.getCommandAndParams("");
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = l_game_engine.getCommandAndParams(" ");
		assertEquals("", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Run multiple tests on getCommandAndParams with 1 word commands
	 */
	@Test
	public void test_getCommandAndParams_2() {
		GameEngine l_game_engine = new GameEngine();
		String[] l_reply = l_game_engine.getCommandAndParams("help");
		assertEquals("help", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = l_game_engine.getCommandAndParams("exit ");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = l_game_engine.getCommandAndParams(" exit");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
		l_reply = l_game_engine.getCommandAndParams(" exit ");
		assertEquals("exit", l_reply[0]);
		assertEquals("", l_reply[1]);
	}

	/**
	 * Run multiple tests on getCommandAndParams with more than 1 word commands
	 */
	@Test
	public void test_getCommandAndParams_3() {
		GameEngine l_game_engine = new GameEngine();
		String[] l_reply = l_game_engine.getCommandAndParams("help me");
		assertEquals("help", l_reply[0]);
		assertEquals("me", l_reply[1]);
		l_reply = l_game_engine.getCommandAndParams("editcountry -add Canada");
		assertEquals("editcountry", l_reply[0]);
		assertEquals("-add Canada", l_reply[1]);
		l_reply = l_game_engine.getCommandAndParams(" editcountry  -add  Canada");
		assertEquals("editcountry", l_reply[0]);
		assertEquals("-add  Canada", l_reply[1]);
		l_reply = l_game_engine.getCommandAndParams("editcountry  -add   Canada ");
		assertEquals("editcountry", l_reply[0]);
		assertEquals("-add   Canada", l_reply[1]);
	}
}
