package soen6441.team01.warzone.model.contracts;

import java.util.ArrayList;

public interface IPlayerModel {

	String getName();

	void setName(String p_name) throws Exception;

	int getPlayerArmy();

	void setPlayerArmy(int p_number_of_army) throws Exception;

	ArrayList<ICountryModel> getPlayerCountries();

	void addPlayerCountry(ICountryModel p_country) throws Exception;

	void removePlayerCountry(ICountryModel p_country) throws Exception;

	ArrayList<IContinentModel> getPlayerContinents();

	void addPlayerContinent(IContinentModel p_continent) throws Exception;

	void removePlayerContinent(IContinentModel p_continent) throws Exception;

	ArrayList<IOrderModel> getOrders();

	void issue_order();

	IOrderModel next_order();
}
