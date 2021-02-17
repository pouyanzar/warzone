package soen6441.team01.warzone.view;

import soen6441.team01.warzone.controller.IInteractionDrivenController;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * views.
 */
public class SoftwareFactoryView {
	private IMapEditorView d_mapeditor_view = null;

	/**
	 * Constructor with no views defined. Software factory will return default
	 * views.
	 */
	public SoftwareFactoryView() {  
	}

	/**
	 * Constructor with views defined. Views passed as null will result in the
	 * default view being passed back.
	 * 
	 * @param p_mapeditor_view the map editor view to use
	 */
	public SoftwareFactoryView(IMapEditorView p_mapeditor_view) {
		d_mapeditor_view = p_mapeditor_view;
	}

	/**
	 * 
	 * @param p_controller the controller to pass into the view
	 * @return an IMapEditorView object
	 */
	public IMapEditorView getMapEditorView(IInteractionDrivenController p_controller) {
		if (d_mapeditor_view == null)
			d_mapeditor_view = new MapEditorConsoleView(p_controller);
		return d_mapeditor_view;
	} 
}
