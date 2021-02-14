package soen6441.team01.warzone.model;

/**
 * The class continent to create instance object of continents on the map
 * 
 * @author pouyan
 *
 */
public class Continent {
	private String continentID;
	private int continentvalue;

	public Continent(String continentID, int continentvalue) {
		this.continentID = continentID;
		this.continentvalue = continentvalue;
	}

	public String getContinentID() {
		return continentID;
	}

	public int getContinetnvalue() {
		return continentvalue;
	}

	public void setContinentID(String continentID) {
		this.continentID = continentID;
	}

	public void setContinentvalue(int continentvalue) {
		this.continentvalue = continentvalue;
	}
}
