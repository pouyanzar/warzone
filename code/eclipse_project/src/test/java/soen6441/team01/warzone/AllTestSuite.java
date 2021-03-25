package soen6441.team01.warzone;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import soen6441.team01.warzone.common.ObserableTest;
import soen6441.team01.warzone.common.UtlTest;
import soen6441.team01.warzone.controller.GameStartupControllerTest;
import soen6441.team01.warzone.controller.IssueOrderControllerTest;
import soen6441.team01.warzone.controller.MapEditorControllerTest;
import soen6441.team01.warzone.model.CardTest;
import soen6441.team01.warzone.model.ContinentTest;
import soen6441.team01.warzone.model.CountryTest;
import soen6441.team01.warzone.model.GamePlayTest;
import soen6441.team01.warzone.model.MapTest;
import soen6441.team01.warzone.model.OrderAdvanceTest;
import soen6441.team01.warzone.model.OrderAirliftTest;
import soen6441.team01.warzone.model.OrderBlockadeTest;
import soen6441.team01.warzone.model.OrderBombTest;
import soen6441.team01.warzone.model.OrderDeployTest;
import soen6441.team01.warzone.model.OrderDiplomacyTest;
import soen6441.team01.warzone.model.PlayerTest;
import soen6441.team01.warzone.view.GamePlayConsoleTest;
import soen6441.team01.warzone.view.MapEditorConsoleTest;

@RunWith(Suite.class)

@SuiteClasses({ ObserableTest.class, UtlTest.class, GameStartupControllerTest.class, IssueOrderControllerTest.class,
		MapEditorControllerTest.class, CardTest.class, ContinentTest.class, CountryTest.class, GamePlayTest.class,
		MapTest.class, OrderAdvanceTest.class, OrderBombTest.class, OrderDeployTest.class, PlayerTest.class,
		MapEditorConsoleTest.class, OrderBlockadeTest.class, GamePlayConsoleTest.class, OrderAirliftTest.class, 
		OrderDiplomacyTest.class })
/**
 * Test Suite for all the classes
 * 
 * @author pouyan
 *
 */
public class AllTestSuite {

}