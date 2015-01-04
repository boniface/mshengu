/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables;

import com.vaadin.ui.Table;
import java.util.List;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceContractLifeCycle;

/**
 *
 * @author boniface
 */
public class WorkSchedulingTable extends Table {

    private final MshenguMain main;

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
                            totalWeeklyServices(contractLifeCycle.getNumberOfUnits(), contractLifeCycle.getFrequency()),
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
    public void clearTable(){
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
}
