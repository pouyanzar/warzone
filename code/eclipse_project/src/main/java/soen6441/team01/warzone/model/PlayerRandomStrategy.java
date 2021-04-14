package soen6441.team01.warzone.model;

import java.io.Serializable;
import java.util.ArrayList;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;
import soen6441.team01.warzone.model.entities.CardType;

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
	private ArrayList<Card> d_cards;

	/**
	 * constructor
	 * 
	 * @param p_player    the cloned player to which to apply this order strategy
	 * @param p_msg_model the message model used to send messages to the view
	 */
	public PlayerRandomStrategy(IPlayerModel p_player, IAppMsg p_msg_model) {
		d_player = p_player;
		d_msg_model = p_msg_model;
		d_cards = new ArrayList<Card>();
		if (d_player != null) {
			d_cards.addAll(d_player.getCards());
		}
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

		if (l_order == null) {
			l_order = doCards();
		}

		if (l_order == null && d_random_attack > 0) {
			l_order = doRandomAttack();
			d_random_attack--;
		}

		if (l_order == null && d_random_move > 0) {
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
	 * play a card if one is available.<br>
	 * 
	 * @return the order or null if no card played
	 * @throws Exception unexpected error
	 */
	private IOrder doCards() throws Exception {
		IOrder l_order = null;

		if (l_order == null) {
			l_order = tryBomb();
		}

		if (l_order == null) {
			l_order = tryBlockade();
		}

		if (l_order == null) {
			l_order = tryAirlift();
		}

		if (l_order == null) {
			l_order = tryNegotiate();
		}

		if (l_order != null) {
			d_cards = new ArrayList<Card>();
		}

		return l_order;
	}

	/**
	 * use the negotiate card if possible
	 * 
	 * @return negotiate order or null if cannot bomb
	 * @throws Exception unexpected error
	 */
	public IOrder tryNegotiate() throws Exception {
		IOrder l_order = null;

		if (!hasCard(CardType.diplomacy)) {
			return l_order;
		}

		if (d_player.getPlayerModelFactory() == null) {
			return l_order;
		}
		IGamePlayModel l_play = d_player.getPlayerModelFactory().getGamePlayModel();
		if (l_play == null) {
			return l_order;
		}

		ArrayList<IPlayerModel> l_players = l_play.getPlayers();
		if (l_players == null) {
			return l_order;
		}

		l_players.remove(d_player);
		if (l_players.size() > 0) {
			IPlayerModel l_player = l_players.get(Utl.randomInt(l_players.size() - 1));
			l_order = new OrderDiplomacy(d_player, l_player);
		}

		return l_order;
	}

	/**
	 * use the airlift card if possible
	 * 
	 * @return airlift order or null if cannot bomb
	 * @throws Exception unexpected error
	 */
	public IOrder tryAirlift() throws Exception {
		IOrder l_order = null;

		if (!hasCard(CardType.airlift)) {
			return l_order;
		}

		ICountryModel l_from = null;
		ICountryModel l_to = null;

		// get all my countries with armies > 0
		ArrayList<ICountryModel> l_countries = PlayerStrategyUtl.countriesWithArmies(d_player.getPlayerCountries(), 0,
				true);
		if (l_countries.size() > 0) {
			l_from = l_countries.get(Utl.randomInt(l_countries.size() - 1));
			l_countries.remove(l_from);
		}

		if (l_from != null) {
			// get all my countries except the one choosen l_from
			l_countries.addAll(d_player.getPlayerCountries());
			l_countries.remove(l_from);
			if (l_countries.size() > 0) {
				l_to = l_countries.get(Utl.randomInt(l_countries.size() - 1));
				l_order = new OrderAirlift(l_from.getName(), l_to.getName(), l_from.getArmies(), d_player);
			}
		}

		return l_order;
	}

	/**
	 * use the blockade card if possible
	 * 
	 * @return blockade order or null if cannot bomb
	 * @throws Exception unexpected error
	 */
	public IOrder tryBlockade() throws Exception {
		IOrder l_order = null;

		if (!hasCard(CardType.blockade)) {
			return l_order;
		}

		ArrayList<ICountryModel> l_countries = d_player.getPlayerCountries();

		if (l_countries.size() > 0) {
			ICountryModel l_country = l_countries.get(Utl.randomInt(l_countries.size() - 1));
			l_order = new OrderBlockade(d_player, l_country);
		}

		return l_order;
	}

	/**
	 * use the bomb card if possible
	 * 
	 * @return bomb order or null if cannot bomb
	 * @throws Exception unexpected error
	 */
	public IOrder tryBomb() throws Exception {
		IOrder l_order = null;

		if (!hasCard(CardType.bomb)) {
			return l_order;
		}

		ArrayList<ICountryModel> l_countries = PlayerStrategyUtl.findBombTargets(d_player.getPlayerCountries());

		if (l_countries.size() > 0) {
			ICountryModel l_country = l_countries.get(Utl.randomInt(l_countries.size() - 1));
			l_order = new OrderBomb(d_player, l_country);
		}

		return l_order;
	}

	/**
	 * move to random neighboring countries:<br>
	 * 1) find a country I own that has armies <br>
	 * 2) find a a neighbor country owned by me or an opponent army that has 0
	 * armies on it - if none goto 1<br>
	 * 3) create order
	 * 
	 * @return the order or null if this order type is not possible
	 * @throws Exception an unexpected error
	 */
	public IOrder doRandomMove() throws Exception {
		IOrder l_order = null;

		// locate all of my countries that have armies
		ArrayList<ICountryModel> l_my_countries_with_armies = PlayerStrategyUtl
				.countriesWithArmies(d_player.getPlayerCountries(), 0, true);
		if (l_my_countries_with_armies.size() < 1) {
			return null;
		}

		// randomly choose one of my countries to move to a random neighbor
		int l_country_from_idx = 0;
		int l_country_to_idx = 0;
		ICountryModel l_my_country = null;
		ICountryModel l_opponent_country = null;
		ArrayList<ICountryModel> l_opponent_neighbors;

		while (l_order == null && l_my_countries_with_armies.size() > 0) {
			l_country_from_idx = Utl.randomInt(l_my_countries_with_armies.size() - 1);
			l_my_country = l_my_countries_with_armies.get(l_country_from_idx);

			// get neighbor an opponent with no armies or a that I own - this will force a
			// move
			l_opponent_neighbors = PlayerStrategyUtl.countryOwnerLike(false, l_my_country, l_my_country.getNeighbors());
			l_opponent_neighbors = PlayerStrategyUtl.countriesWithArmies(l_opponent_neighbors, 1, false);
			l_opponent_neighbors
					.addAll(PlayerStrategyUtl.countryOwnerLike(true, l_my_country, l_my_country.getNeighbors()));

			if (l_opponent_neighbors.size() < 1) {
				l_my_countries_with_armies.remove(l_country_from_idx);
			} else {
				// randomly choose a neighbor to move to
				l_country_to_idx = Utl.randomInt(l_opponent_neighbors.size() - 1);
				l_opponent_country = l_opponent_neighbors.get(l_country_to_idx);
				l_order = new OrderAdvance(d_player, l_my_country, l_opponent_country, l_my_country.getArmies());
			}
		}

		return l_order;
	}

	/**
	 * attacks random neighboring countries, if possible:<br>
	 * 1) find a country that has armies <br>
	 * 2) find a a neighbor opponent that has armies on it - if none goto 1<br>
	 * 3) create order
	 * 
	 * @return the order or null if this order type is not possible
	 * @throws Exception an unexpected error
	 */
	public IOrder doRandomAttack() throws Exception {
		IOrder l_order = null;

		// locate all of my countries that have armies
		ArrayList<ICountryModel> l_my_countries_with_armies = PlayerStrategyUtl
				.countriesWithArmies(d_player.getPlayerCountries(), 0, true);
		if (l_my_countries_with_armies.size() < 1) {
			return null;
		}

		// randomly choose one of my countries to attack a random opposing neighbor with
		// armies
		int l_country_from_idx = 0;
		int l_country_to_idx = 0;
		ICountryModel l_my_country = null;
		ICountryModel l_opponent_country = null;
		ArrayList<ICountryModel> l_opponent_neighbors;

		while (l_order == null && l_my_countries_with_armies.size() > 0) {
			l_country_from_idx = Utl.randomInt(l_my_countries_with_armies.size() - 1);
			l_my_country = l_my_countries_with_armies.get(l_country_from_idx);

			// get opponent neighbor with armies - this will force an attack
			l_opponent_neighbors = PlayerStrategyUtl.countryOwnerLike(false, l_my_country, l_my_country.getNeighbors());
			l_opponent_neighbors = PlayerStrategyUtl.countriesWithArmies(l_opponent_neighbors, 0, true);
			if (l_opponent_neighbors.size() < 1) {
				l_my_countries_with_armies.remove(l_country_from_idx);
			} else {
				// randomly choose an opponent with armies to attack
				l_country_to_idx = Utl.randomInt(l_opponent_neighbors.size() - 1);
				l_opponent_country = l_opponent_neighbors.get(l_country_to_idx);
				l_order = new OrderAdvance(d_player, l_my_country, l_opponent_country, l_my_country.getArmies());
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
	public IOrder doRandomDeploy() throws Exception {
		IOrder l_order = null;

		if (d_player.getReinforcements() < 1) {
			return l_order;
		}

		// reset counters to allow for attacks and moves
		d_random_attack = 1;
		d_random_move = 1;
		d_cards = new ArrayList<Card>();
		d_cards.addAll(d_player.getCards());

		// find a random country that belongs to me and deploy all reinforcements
		ArrayList<ICountryModel> l_player_countries = d_player.getPlayerCountries();
		if (l_player_countries.size() < 1) {
			return null;
		}
		int l_country_idx = Utl.randomInt(l_player_countries.size() - 1);
		ICountryModel l_country = l_player_countries.get(l_country_idx);
		if (l_country != null) {
			l_order = new OrderDeploy(l_country.getName(), d_player.getReinforcements(), d_player);
		}

		return l_order;
	}

	/**
	 * Checks if the card exist inside the current player's card list
	 * 
	 * @param p_card desired The card to be checked if it exists in player's card
	 *               list
	 * @return true if the card exist and false otherwise
	 */
	public boolean hasCard(CardType p_card) {
		for (Card l_card : d_cards) {
			if (l_card.getCardType() == p_card) {
				return true;
			}
		}
		return false;
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