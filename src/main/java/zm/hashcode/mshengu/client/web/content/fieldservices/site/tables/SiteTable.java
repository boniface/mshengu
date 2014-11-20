/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.site.tables;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import java.util.Set;
import zm.hashcode.mshengu.app.facade.products.SiteFacade;
import zm.hashcode.mshengu.app.util.UITableIconHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceContractLifeCycle;
import zm.hashcode.mshengu.domain.ui.location.Location;

/**
 *
 * @author boniface
 */
public class SiteTable extends Table {

    private final MshenguMain main;
    private int monthlyServices;
    private int weeklyServices;
    private int totalNumberOfSites;
    private int totalNumberOfUnits;
    private UITableIconHelper iconHelper = new UITableIconHelper();

    public SiteTable(MshenguMain main) {
        this.main = main;
        setSizeFull();


        addContainerProperty("Site Name", String.class, null);
        addContainerProperty("Suburb", String.class, null);
        addContainerProperty("Region", String.class, null);
        addContainerProperty("Planed \n NO of Units", Integer.class, null);
        addContainerProperty("Actual \n No of Units", Integer.class, null);
        addContainerProperty("Frequency", Integer.class, null);
        addContainerProperty("Total \n Monthtly Services", Integer.class, null);
        addContainerProperty("Scheduled \n Visit Days", String.class, null);
        addContainerProperty("Is Active", Embedded.class, null);

        // Add Data I

    }

    public void loadCustomerSites(Set<Site> customerSites) {
        setNullSelectionAllowed(true);
        setSelectable(false);
        setImmediate(false);
        monthlyServices = 0;
        weeklyServices = 0;
        totalNumberOfSites = 0;        
        totalNumberOfUnits = 0;

        for (Site site : customerSites) {
            SiteServiceContractLifeCycle contractLifeCycle = site.getLastSiteServiceContractLifeCycle();
            if (contractLifeCycle != null) {
                totalNumberOfUnits += contractLifeCycle.getExpectedNumberOfUnits();
                monthlyServices += contractLifeCycle.getMonthlyServices();
                weeklyServices += contractLifeCycle.getWeeklyServices();
                totalNumberOfSites += 1;
                addItem(new Object[]{site.getName(),
                            site.getLocationName(),
                            getParentLocation(site.getLocation()),
                            contractLifeCycle.getExpectedNumberOfUnits(),
                            contractLifeCycle.getNumberOfUnits(),
                            contractLifeCycle.getFrequency(),
                            contractLifeCycle.getMonthlyServices(),
                            contractLifeCycle.visitDays(),
                            iconHelper.getCheckOrCross(site.isActive(), 24),}, site.getId());
            }

        }


        // Allow selecting items from the table.
        setNullSelectionAllowed(false);
        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);

    }

    private String getParentLocation(Location location) {
        if (location != null) {
            return location.getParentLocationName();
        } else {
            return null;
        }
    }

    /**
     * @return the monthlyServices
     */
    public int getMonthlyServices() {
        return monthlyServices;
    }

    /**
     * @return the weeklyServices
     */
    public int getWeeklyServices() {
        return weeklyServices;
    }

    /**
     * @return the totalNumberOfSites
     */
    public int getTotalNumberOfSites() {
        return totalNumberOfSites;
    }

    /**
     * @return the totalNumberOfUnits
     */
    public int getTotalNumberOfUnits() {
        return totalNumberOfUnits;
    }
}
