package soen6441.team01.warzone;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import soen6441.team01.warzone.common.ObserableTest;
import soen6441.team01.warzone.common.UtlTest;
import soen6441.team01.warzone.controller.*;
import soen6441.team01.warzone.model.*;
import soen6441.team01.warzone.view.*;

@RunWith(Suite.class)

@SuiteClasses({ ObserableTest.class, UtlTest.class, GameStartupControllerTest.class, IssueOrderControllerTest.class,
		MapEditorControllerTest.class, CardTest.class, ContinentTest.class, CountryTest.class, GamePlayTest.class,
		MapTest.class, OrderAdvanceTest.class, OrderBombTest.class, OrderDeployTest.class, PlayerTest.class,
		MapEditorConsoleTest.class, OrderBlockadeTest.class, GamePlayConsoleTest.class, OrderAirliftTest.class, 
		OrderDiplomacyTest.class, MapIoConquestTest.class, MapIoDominationTest.class, GameEngineTest.class,
		PlayerRandomStrategyTest.class })
/**
 * Test Suite for all the classes
 * 
 * @author pouyan
 *
 */
public class AllTestSuite {

}