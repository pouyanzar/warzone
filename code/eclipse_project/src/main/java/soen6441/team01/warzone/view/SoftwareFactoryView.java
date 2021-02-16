package soen6441.team01.warzone.view;


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
	 * Constructor with viewss defined. Views passed as null will result in the
	 * default view being passed back.
	 * 
	 */
	public SoftwareFactoryView(IMapEditorView p_mapeditor_view) {
		d_mapeditor_view = p_mapeditor_view;
	}

	/**
	 * 
	 * @return an IMapModel object
	 */
	public IMapEditorView getMapEditorView() {
		if (d_mapeditor_view == null)
			d_mapeditor_view = new MapEditorConsoleView();
		return d_mapeditor_view;
	}
}
