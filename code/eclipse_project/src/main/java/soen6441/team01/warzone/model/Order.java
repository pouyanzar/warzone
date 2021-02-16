package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.IOrderModel;
import soen6441.team01.warzone.model.contracts.IOrderModelView;

public class Order implements IOrderModel, IOrderModelView {

	private String d_order;

	public Order(String l_order) throws Exception {

		super();
		setOrder(l_order);
	}

	/**
	 * gets the current order issued by player
	 * 
	 * @return d_order the current order issued by player
	 */
	public String getOrder() {
		return d_order;
	}

	/**
	 * sets the order player decides to issue
	 */
	public void setOrder(String l_order) throws Exception{
		d_order = l_order;
	}

}
