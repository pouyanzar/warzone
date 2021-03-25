package soen6441.team01.warzone.model.contracts;

import java.util.ArrayList;

import soen6441.team01.warzone.model.Card;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Interface for Player class
 * 
 * @author John
 *
 */
public interface IPlayerModel {

	String getName();

	void setName(String p_name) throws Exception;

	int getReinforcements();

	void setReinforcements(int p_number_of_army) throws Exception;

	ArrayList<ICountryModel> getPlayerCountries();

	void addPlayerCountry(ICountryModel p_country) throws Exception;

	void removePlayerCountry(ICountryModel p_country) throws Exception;

	ArrayList<IOrder> getOrders();

	void issue_order() throws Exception;

	IOrder next_order();

	IPlayerModel deepClonePlayer(ModelFactory p_factory_model) throws Exception;

	void copyOrders(IPlayerModel p_cloned_player) throws Exception;

	void addCard(Card l_card);

	ArrayList<Card> getCards();

	boolean isDoneTurn();

	void setDoneTurn(boolean p_done_turn);

	boolean hasCard(CardType p_card);
	
	void removeCard(CardType p_card);
	
	ModelFactory getPlayerModelFactory();
	
	void addDiplomacy(IPlayerModel p_friendly_player);
	
	void clearAllDiplomacy();
	
	boolean isDiplomatic(IPlayerModel p_other_player);
}
