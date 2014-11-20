/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.site.tables;

import com.vaadin.ui.Table;
import java.util.Set;
import zm.hashcode.mshengu.app.facade.products.SiteFacade;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceContractLifeCycle;

/**
 *
 * @author boniface
 */
public class SiteTableOriginal extends Table {

    private final MshenguMain main;

    public SiteTableOriginal(MshenguMain main) {
        this.main = main;
        setSizeFull();

  
        addContainerProperty("Site Name", String.class, null);
        addContainerProperty("Site Location", String.class, null);
        addContainerProperty("Street Address", String.class, null);
        addContainerProperty("Number of Units", String.class, null);
        addContainerProperty("Frequency", Integer.class, null);
        addContainerProperty("Monday", String.class, null);
        addContainerProperty("Tuesday", String.class, null);
        addContainerProperty("Wednesday", String.class, null);
        addContainerProperty("Thursday", String.class, null);
        addContainerProperty("Friday", String.class, null);
        addContainerProperty("Saturday", String.class, null);
        addContainerProperty("Sunday", String.class, null);
        //addContainerProperty("Contact Number", String.class, null);

        // Add Data Columns

    }

    public void loadCustomerSites(Set<Site> customerSites) {
        setNullSelectionAllowed(true);
        setSelectable(false);
        setImmediate(false);


        for (Site site : customerSites) {
            SiteServiceContractLifeCycle contractLifeCycle = site.getLastSiteServiceContractLifeCycle();
            String noOfUnits = getNoOfUnits(contractLifeCycle.getNumberOfUnits(), contractLifeCycle.getExpectedNumberOfUnits());
            addItem(new Object[]{site.getName(),
                        site.getLocation().getName(),
                        site.getAddress().getStreetAddress(),
                        noOfUnits,
                        contractLifeCycle.getFrequency(), //  customer.getContactPerson().get(),
                        getViewOutput(contractLifeCycle.isMonday()),
                        getViewOutput(contractLifeCycle.isTuesday()),
                        getViewOutput(contractLifeCycle.isWednesday()),
                        getViewOutput(contractLifeCycle.isThursday()),
                        getViewOutput(contractLifeCycle.isFriday()),
                        getViewOutput(contractLifeCycle.isSaturday()),
                        getViewOutput(contractLifeCycle.isSunday()),}, site.getId());
        }


        // Allow selecting items from the table.
        setNullSelectionAllowed(false);
        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);

    }

    private String getViewOutput(boolean weekday) {
        if (weekday) {
            return "Y";
        } else {
            return "N";
        }
    }

    private String getNoOfUnits(int currentUnits, int totalUnits) {
        String msg = currentUnits + "/" + totalUnits;
        return msg;
    }
}
