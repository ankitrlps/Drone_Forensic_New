package dji;

import java.util.List;

import javafx.fxml.FXML;

public class DJIMainController {

	@FXML private HeaderController 	headerController;
	@FXML private OverviewTabController overViewTabController;
	@FXML private MapTabController mapTabController;
	@FXML private FlightControlController flightControlController;
	
	@FXML private void initialize() {
		headerController.injectHeader(this);
	}
	
	public void getMap(String kmlPath) {
		mapTabController.openMaps(kmlPath);
	}
	
	public void getListForOverview(List<DjiParameters> getList){
		overViewTabController.displayOverview(getList);
	}
	
	public void getListForFlightController(List<DjiParameters> list){
		flightControlController.showFlightControl(list);
	}
	
}