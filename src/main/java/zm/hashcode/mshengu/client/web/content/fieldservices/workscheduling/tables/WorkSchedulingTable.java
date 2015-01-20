/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables;

import com.vaadin.ui.Table;
import java.util.Date;
import java.util.List;
import zm.hashcode.mshengu.app.util.DateTimeFormatWeeklyHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceContractLifeCycle;

/**
 *
 * @author boniface
 */
public class WorkSchedulingTable extends Table {
    
    private final MshenguMain main;
    
    private final DateTimeFormatWeeklyHelper dtfwh = new DateTimeFormatWeeklyHelper();
    private String fileName = "";
    private    StringBuilder reportHeader;
    
    public WorkSchedulingTable(MshenguMain main) {
        this.main = main;
        setSizeFull();
        
        addContainerProperty("Site Name", String.class, null);
        addContainerProperty("Site Location", String.class, null);
        addContainerProperty("Street Address", String.class, null);
        addContainerProperty("NO of Units", Integer.class, null);
        addContainerProperty("Frequency", Integer.class, null);
        addContainerProperty("Total Weekly Services", Integer.class, null);
        addContainerProperty("Visit Days", String.class, null);
        //addContainerProperty("Contact Number", String.class, null);

        // Add Data Columns
    }
    
    public void loadVehicleRoutes(List<Site> customerSites) {
        setNullSelectionAllowed(true);
        setSelectable(false);
        setImmediate(false);
        
        removeAllItems();
        for (Site site : customerSites) {
            if (site.getLastSiteServiceContractLifeCycle() != null) {
                SiteServiceContractLifeCycle contractLifeCycle = site.getLastSiteServiceContractLifeCycle();
                addItem(new Object[]{site.getName(),
                    site.getLocation().getName(),
                    site.getAddress().getStreetAddress(),
                    contractLifeCycle.getExpectedNumberOfUnits(),
                    contractLifeCycle.getFrequency(),
                    totalWeeklyServices(contractLifeCycle.getExpectedNumberOfUnits(), contractLifeCycle.getFrequency()),
                    contractLifeCycle.visitDays(),}, site.getId());
            }
        }

        // Allow selecting items from the table.
        setNullSelectionAllowed(false);
        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);
        
    }
    
    private String getNoOfUnits(int currentUnits, int totalUnits) {
        String msg = currentUnits + "/" + totalUnits;
        return msg;
    }
    
    private int totalWeeklyServices(int numberOfUnits, int Frequency) {
        return numberOfUnits * Frequency;
    }

    public void clearTable() {
        setNullSelectionAllowed(true);
        setSelectable(false);
        setImmediate(false);
        
        removeAllItems();
        // Allow selecting items from the table.
        setNullSelectionAllowed(false);
        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);
    }
    
    public void setReportHeader(String vehicleName, String driverName) {
        dtfwh.setDate(new Date());        
        reportHeader = new StringBuilder();
        reportHeader.append("Vehicle : ").append("\t").append(vehicleName).append('\t')
                .append("Driver  : ").append("\t").append(driverName).append('\n')
                .append("Service Schedule").append("\t").append(dtfwh.getMondayForTbaleFormat())
                .append(" / ").append(dtfwh.getSundayForTbaleFormat());
        
        fileName =  vehicleName + "_Schedule_From_" + dtfwh.getMondayForTbaleFormat() + "_To_" + dtfwh.getSundayForTbaleFormat() +".xls";
    }
    
    
     public String getFileName(){
        if(fileName.equals("")){
            return "_" + dtfwh.getMondayForTbaleFormat() + ".xls";
        }else{
            return fileName;
        }
    }
     
     public String getReportHeader(){
         return reportHeader.toString();
     }
}
