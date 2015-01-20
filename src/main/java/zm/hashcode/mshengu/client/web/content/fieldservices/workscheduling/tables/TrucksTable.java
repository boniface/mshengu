/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import java.util.Date;
import java.util.List;
import zm.hashcode.mshengu.app.facade.fleet.TruckFacade;
import zm.hashcode.mshengu.app.util.DateTimeFormatWeeklyHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.fleet.Truck;

/**
 *
 * @author geek
 */
public class TrucksTable extends Table {

    private final MshenguMain main;

             private final DateTimeFormatWeeklyHelper dtfwh = new DateTimeFormatWeeklyHelper();
    private String fileName = "";
    private    StringBuilder reportHeader;
   
    public TrucksTable(MshenguMain main) {
        this.main = main;
        setSizeFull();
        addContainerProperty("Vehicle Number", String.class, null);
        addContainerProperty("Number Plate", String.class, null);
        addContainerProperty("Brand", String.class, null);
        addContainerProperty("Model", String.class, null);
        addContainerProperty("Status", String.class, null);
        addContainerProperty("Details ", Button.class, null);

        List<Truck> trucks = TruckFacade.getTruckService().findAllServiceAndUtilityVehicles();
        for (Truck truck : trucks) {
            Button showDetails = new Button("Details");
            showDetails.setData(truck.getId());
            showDetails.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    // Get the item identifier from the user-defined data.
                    String itemId = (String) event.getButton().getData();
                    Truck truck = TruckFacade.getTruckService().findById(itemId);
                }
            });

            showDetails.setStyleName(Reindeer.BUTTON_LINK);
            addItem(new Object[]{
                truck.getVehicleNumber(),
                truck.getNumberPlate(),
                truck.getBrand(),
                truck.getModel(),
                getStatus(truck),
                showDetails
            }, truck.getId());
        }
        // Allow selecting items from the table.
        setNullSelectionAllowed(false);
//
        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);
    }

    private String getStatus(Truck truck) {
        if (truck.getRoutes().size() < 0) {
            return "NOT ASSIGNED";
        }
        return "ASSIGNED TO " + truck.getRoutes().size() + " Sites";
    }
    
     public void setReportHeader() {
        dtfwh.setDate(new Date());        
        reportHeader = new StringBuilder();
        reportHeader.append("Mshengu Vehicle/Driver Allocation Sheet").append('\n')
                    .append("Print date  : ").append("\t").append(dtfwh.getTodayForTbaleFormat());
        
        fileName = "Mshengu_Vehicle_Allocation_" + dtfwh.getTodayForTbaleFormat() +".xls";
    }
    
    
     public String getFileName(){
        if(fileName.equals("")){
            return fileName = "Mshengu_Vehicle_Allocation_" + dtfwh.getTodayForTbaleFormat() +".xls";
        }else{
            return fileName;
        }
    }
     
     public String getReportHeader(){
         return reportHeader.toString();
     }
}
