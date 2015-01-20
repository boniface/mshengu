/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import zm.hashcode.mshengu.app.facade.fleet.TruckFacade;
import zm.hashcode.mshengu.app.facade.products.SiteFacade;
import zm.hashcode.mshengu.app.util.DateTimeFormatWeeklyHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.WorkSchedulingMenu;
import zm.hashcode.mshengu.domain.fleet.Truck;
import zm.hashcode.mshengu.domain.products.Site;

/**
 *
 * @author boniface
 */
public class AssignedSitesTable extends Table {

    private final MshenguMain main;
 
    public AssignedSitesTable(final MshenguMain main, String trackId) {
        this.main = main;

        setSizeFull();
        addContainerProperty("Site", String.class, null);
        addContainerProperty("Address", String.class, null);
        addContainerProperty("Remove Route", Button.class, null);

        final Truck truck = TruckFacade.getTruckService().findById(trackId);

        List<Site> routes = truck.getRoutes();
        for (Site site : routes) {

            Button removeSiteButton = new Button("Remove Route");
            removeSiteButton.setData(site.getId());
            removeSiteButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    // Get the item identifier from the user-defined data.
                    String itemId = (String) event.getButton().getData();
                    Site site = SiteFacade.getSiteService().findById(itemId);
                    List<Site> sites = new ArrayList<>();
                    sites.addAll(truck.getRoutes());
                    sites.remove(site);

                    Truck updatedTruck = new Truck.Builder(truck.getNumberPlate())
                            .truck(truck)
                            .routes(sites)
                            .build();
                    TruckFacade.getTruckService().merge(updatedTruck);
                    getHome();
                }
            });
            removeSiteButton.setStyleName(Reindeer.BUTTON_LINK);
            addItem(new Object[]{
                site.getName(),
                site.getAddressStreetAddress(),
                removeSiteButton
            }, site.getId());
        }
        // Allow selecting items from the table.
        setNullSelectionAllowed(false);
//
        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);
    }


    private void getHome() {
        main.content.setSecondComponent(new WorkSchedulingMenu(main, "ROUTES"));
    }
    
}