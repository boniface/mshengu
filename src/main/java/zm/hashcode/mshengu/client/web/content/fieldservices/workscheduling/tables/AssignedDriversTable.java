/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables;

import com.vaadin.ui.Table;
import java.util.Date;
import java.util.List;
import zm.hashcode.mshengu.app.facade.fleet.TruckFacade;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.app.util.DateTimeFormatWeeklyHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.fleet.Truck;

/**
 *
 * @author Ferox
 */
public class AssignedDriversTable extends Table {

    private final MshenguMain main;
    private DateTimeFormatHelper formatHelper = new DateTimeFormatHelper();

      private final DateTimeFormatWeeklyHelper dtfwh = new DateTimeFormatWeeklyHelper();
    private String fileName = "";
    private    StringBuilder reportHeader;
    public AssignedDriversTable(MshenguMain main) {
        this.main = main;
        setSizeFull();

                addContainerProperty("Vehicle Number", String.class, null);
        addContainerProperty("Brand", String.class, null);
        addContainerProperty("Model", String.class, null);
        addContainerProperty("Number Plate", String.class, null);
        addContainerProperty("Expire Date", String.class, null);
        addContainerProperty("Driver", String.class, null);
        addContainerProperty("In Service?", Boolean.class, null);
        //addContainerProperty("Contact Number", String.class, null);

        // Add Data Columns
        List<Truck> truckList = TruckFacade.getTruckService().findAll();
        for (Truck truck : truckList) {
            addItem(new Object[]{
                truck.getVehicleNumber(),
                truck.getBrand(),
                truck.getModel(),
                truck.getNumberPlate(),
                formatHelper.getYearMonthDay(truck.getDateOfExpire()),
                truck.getDriverName(),
                truck.isIsActive(),}, truck.getId());
        }
        // Allow selecting items from the table.
        setNullSelectionAllowed(false);

        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);




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
