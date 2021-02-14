package soen6441.team01.warzone.model;

/**
 * The class Country to create instance object of countries on the map
 * 
 * @author pouyan
 *
 */
public class Country implements ICountryModel, ICountryModelView {

	private String countryID;

	public Country(String countryID) {
		this.countryID = countryID;
	}

	public String getCountryID() {
		return countryID;
	}

	public void setCountryID(String countryID) {
		this.countryID = countryID;
	}
}