package soen6441.team01.warzone.model;

import java.io.Serializable;
import java.util.ArrayList;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;

/**
 * Supports the random player strategy. <br>
 * A computer based player that does not require user interaction to make
 * decisions.
 *
 */
public class PlayerRandomStrategy implements IPlayerStrategy, Serializable {
	private static final long serialVersionUID = 1L;
	// the map is available from within the player object
	private IPlayerModel d_player;
	private IAppMsg d_msg_model;
	private int d_random_attack = 1;
	private int d_random_move = 1;

	/**
	 * constructor
	 * 
	 * @param p_player    the cloned player to which to apply this order strategy
	 * @param p_msg_model the message model used to send messages to the view
	 */
	public PlayerRandomStrategy(IPlayerModel p_player, IAppMsg p_msg_model) {
		d_player = p_player;
		d_msg_model = p_msg_model;
	}

	/**
	 * create the order. <br>
	 * note that the order needs to be executed otherwise any changes to the
	 * players' state is not affected. e.g. if we don't do this then if a deploy
	 * order is given the number of reinforcements will not be subtracted from the
	 * player. However at this stage the player is a clone with a completely cloned
	 * map, so it's ok to execute the order as it won't affect the master (ie
	 * common) map, until the order is executed on the real player and the real map,
	 * which is done by the system automatically later on.
	 * 
	 * @return the next order, null = end turn
	 * @throws Exception an unexpected error
	 */
	public IOrder createOrder() throws Exception {
		String l_msg_header = "Gameplay - computer player " + d_player.getName() + " [random] issuing order> ";

		IOrder l_order = doRandomDeploy();

		if (d_random_attack > 0 && l_order == null) {
			l_order = doRandomAttack();
			d_random_attack--;
		}
		if (d_random_move > 0 && l_order == null) {
			l_order = doRandomMove();
			d_random_move--;
		}

		if (l_order != null) {
			d_msg_model.setMessage(MsgType.Informational, l_msg_header + l_order.toString());
			l_order.execute(); // see method comment above
		} else {
			d_msg_model.setMessage(MsgType.Informational, l_msg_header + "end turn");
		}

		return l_order;
	}

	/**
	 * moves armies randomly between its countries, if possible:<br>
	 * 1) find a country that has armies - if none then don't do it 2) find a a
	 * neighbor that is either your own country or an opponent that has 0 armies on
	 * it - if none goto 1 3) create order
	 * 
	 * @return the order or null if this order type is not possible
	 * @throws Exception an unexpected error
	 */
	private IOrder doRandomMove() throws Exception {
		IOrder l_order = null;
		if (d_player.getReinforcements() < 1) {
			return l_order;
		}
		/*
		 * 1) find a country that has armies - if none then don't do it 
		 * 2) find a neighbor that is either your own country or an opponent that has 0 armies on
		 * it - if none goto 1
		 */
		ArrayList<ICountryModel> l_player_countries = d_player.getPlayerCountries();
		int SumArmies = 0;
		for (ICountryModel d_c : l_player_countries)
			SumArmies += d_c.getArmies();
		if (SumArmies > 0) {
			int l_country_from_idx = 0;
			ICountryModel l_from_country = null;
			do {
				l_country_from_idx = Utl.randomInt(l_player_countries.size() - 1);
				l_from_country = l_player_countries.get(l_country_from_idx);
			} while (l_from_country.getArmies() <= 0);
			SumArmies = 0;
			for (ICountryModel d_c : l_from_country.getNeighbors())
				SumArmies += d_c.getArmies();
			
			int l_country_to_idx = Utl.randomInt(l_player_countries.size() - 1);
			ICountryModel l_to_country = l_player_countries.get(l_country_to_idx);
			if (l_country_from_idx != l_country_to_idx) {
				if ((l_from_country != null) && (l_to_country != null)) {
					int l_numarmies = Utl.randomInt(l_from_country.getArmies() - 1);
					l_order = new OrderAdvance(d_player, l_from_country, l_to_country, l_numarmies);
				}
			}
		}
		return l_order;
	}

	/**
	 * attacks random neighboring countries, if possible:<br>
	 * 1) find a country that has armies - if none then don't do it 2) find a a
	 * neighbor opponent that has armies on it - if none goto 1 3) create order
	 * 
	 * @return the order or null if this order type is not possible
	 * @throws Exception an unexpected error
	 */
	private IOrder doRandomAttack() throws Exception {
		IOrder l_order = null;
		if (d_player.getReinforcements() < 1) {
			return l_order;
		}
		ArrayList<ICountryModel> l_player_countries = d_player.getPlayerCountries();
		int SumArmies = 0;
		for (ICountryModel d_c : l_player_countries)
			SumArmies += d_c.getArmies();
		if (SumArmies > 0) {
			int l_country_from_idx = 0;
			ICountryModel l_from_country = null;
			do {
				l_country_from_idx = Utl.randomInt(l_player_countries.size() - 1);
				l_from_country = l_player_countries.get(l_country_from_idx);
			} while (l_from_country.getArmies() <= 0);

			SumArmies = 0;
			for (ICountryModel d_c : l_from_country.getNeighbors())
				SumArmies += d_c.getArmies();
			if (SumArmies > 0) {
				int l_country_to_idx = 0;
				ICountryModel l_to_countries = null;
				do {
					l_country_to_idx = Utl.randomInt(l_from_country.getNeighbors().size() - 1);
					l_to_countries = l_from_country.getNeighbors().get(l_country_to_idx);
				} while (l_to_countries.getArmies() <= 0);
				if ((l_from_country != null) && (l_to_countries != null)) {
					int l_numarmies = Utl.randomInt(l_from_country.getArmies() - 1);
					l_order = new OrderAdvance(d_player, l_from_country, l_to_countries, l_numarmies);
				}
			}
		}
		return l_order;
	}

	/**
	 * do a Deploy to one of the player's countries, if possible<br>
	 * 
	 * @return the order or null if a deploy is not allowed
	 * @throws Exception an unexpected error
	 */
	private IOrder doRandomDeploy() throws Exception {
		IOrder l_order = null;

		if (d_player.getReinforcements() < 1) {
			return l_order;
		}

		ArrayList<ICountryModel> l_player_countries = d_player.getPlayerCountries();
		int l_country_idx = Utl.randomInt(l_player_countries.size() - 1);
		ICountryModel l_country = l_player_countries.get(l_country_idx);
		if (l_country != null) {
			l_order = new OrderDeploy(l_country.getName(), d_player.getReinforcements(), d_player);
		}
		return l_order;
	}

	/**
	 * create a deep clone of the current strategy<br>
	 * required because we want the player to analyze based on recent changes to the
	 * map made by recent orders
	 * 
	 * @return a new strategy object cloned from this object
	 */
	public IPlayerStrategy cloneStrategy(IPlayerModel p_player) throws Exception {
		IPlayerStrategy l_player_strategy = new PlayerRandomStrategy(p_player,
				p_player.getPlayerModelFactory().getUserMessageModel());
		return l_player_strategy;
	}

	/**
	 * @return the name of this strategy
	 */
	@Override
	public String toString() {
		return "random";
	}
}