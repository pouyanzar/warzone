package soen6441.team01.warzone.model;

import java.io.Serializable;
import java.util.ArrayList;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;

/**
 * Supports the cheater player strategy. <br>
 * A computer based player that does not require user interaction to make
 * decisions.
 *
 */
public class PlayerCheaterStrategy implements IPlayerStrategy, Serializable {
	private static final long serialVersionUID = 1L;
	// the map is available from within the player object
	private IPlayerModel d_player;
	private IAppMsg d_msg_model;

	/**
	 * constructor
	 * 
	 * @param p_player    the true map based player (i.e. not a clone)
	 * @param p_msg_model the message model used to send messages to the view
	 */
	public PlayerCheaterStrategy(IPlayerModel p_player, IAppMsg p_msg_model) {
		d_player = p_player;
		d_msg_model = p_msg_model;
	}

	/**
	 * cheaters don't create orders, they manipulate the map directly.
	 * 
	 * @return the next order, null = end turn (always null)
	 * @throws Exception an unexpected error
	 */
	public IOrder createOrder() throws Exception {

		ArrayList<ICountryModel> l_neighbors = new ArrayList<>();

		// find and conquer neighbor countries
		ArrayList<ICountryModel> l_player_countries = new ArrayList<>();
		for (ICountryModel l_country : d_player.getPlayerCountries())
			l_player_countries.add(l_country);
		for (ICountryModel l_country : l_player_countries) {
			l_neighbors.addAll(l_country.getNeighbors());
			for (ICountryModel l_neighbor : l_neighbors)
				d_player.addPlayerCountry(l_neighbor);
		}

		// makes double the armies in countries have enemy neighbors
		for (ICountryModel l_country : d_player.getPlayerCountries()) {
			if (l_country.getNeighbors().size() > 0)
				for (ICountryModel l_country_1 : l_country.getNeighbors())
					if (!d_player.getPlayerCountries().contains(l_country_1))
						l_country.setArmies(l_country.getArmies() * 2);
		}

		String l_msg_header = "Gameplay - computer player " + d_player.getName() + " [cheater] issuing order> ";
		d_msg_model.setMessage(MsgType.Informational, l_msg_header + "end turn");
		return null;
	}

	/**
	 * do not create a deep clone of the current strategy<br>
	 * required because we want the player to manipulate the original map directly
	 * 
	 * @return a this strategy object
	 */
	public IPlayerStrategy cloneStrategy(IPlayerModel p_player) throws Exception {
		return this;
	}

	/**
	 * @return the name of this strategy
	 */
	@Override
	public String toString() {
		return "cheater";
	}
}
