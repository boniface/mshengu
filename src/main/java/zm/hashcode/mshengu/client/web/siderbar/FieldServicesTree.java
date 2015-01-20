/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.siderbar;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Tree;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.CustomerMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.contactus.ContactUSMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.incidents.IncidentMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.QuoteRequestsMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.servicerequest.ServiceRequestMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.servicesperformed.ServicePerformedMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.site.ServiceSchedulingMenu;
//import zm.hashcode.mshengu.client.web.content.fieldservices.sitedatabase.SiteDatabaseMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.WorkSchedulingMenu;

/**
 *
 * @author boniface
 */
public class FieldServicesTree extends Tree implements ItemClickEvent.ItemClickListener {

    private final MshenguMain main;
    public static final Object CUSTOMER_SETUP = "Customer Activation";
    public static final Object SERVICE_SCHEDULE = "Service Schedule";
    public static final Object SITE_DATABASE = "Site Database";
    public static final Object WORK_SCHEDULE = "Work Scheduling";
    public static final Object SERVICES_PERFORMED = "Services Performed";
    public static final Object SERVICES_REQUEST = "Service Request Management";
    public static final Object INCIDENTS = "Incident Management";
    public static final Object CONTACT_US = "Customer Contact Management";
    private static final String LANDING_TAB = "LANDING";
    private static final String QUOTE_REQUESTS = "Quote Management";

    public FieldServicesTree(MshenguMain main) {
        this.main = main;
        addItem(CUSTOMER_SETUP);
        addItem(SERVICE_SCHEDULE);
        addItem(SITE_DATABASE);
        addItem(SERVICES_PERFORMED);
        addItem(WORK_SCHEDULE);
        addItem(CONTACT_US);
        addItem(INCIDENTS);
        addItem(SERVICES_REQUEST);
        addItem(QUOTE_REQUESTS);

        //Add Listeners
        addItemClickListener((ItemClickEvent.ItemClickListener) this);
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        Object itemId = event.getItemId();
        if (itemId != null) {
            if (CUSTOMER_SETUP.equals(itemId)) {
                manageCustomeriew();
            }
            if (SERVICE_SCHEDULE.equals(itemId)) {
                manageSiteView();
            }
//            if(SITE_DATABASE.equals(itemId)){
//                manageSiteDatabaseView();
//            }
            if (SERVICES_PERFORMED.equals(itemId)) {
                manageServicesPerformedView();
            }
            if (WORK_SCHEDULE.equals(itemId)) {
                manageWorkScheduleView();
            }
            if (INCIDENTS.equals(itemId)) {
                manageIncidentsView();
            }

            if (CONTACT_US.equals(itemId)) {
                manageExternalContactView();
            }
            if (SERVICES_REQUEST.equals(itemId)) {
                manageServiceRequestView();
            }
            if (QUOTE_REQUESTS.equals(itemId)) {
               manageQuoteRequestsView();
            }
            //
        }
    }

    private void manageCustomeriew() {
        main.content.setSecondComponent(new CustomerMenu(main, LANDING_TAB));
    }
    private void manageQuoteRequestsView() {
        main.content.setSecondComponent(new QuoteRequestsMenu(main, LANDING_TAB));
    }

    private void manageSiteView() {
        main.content.setSecondComponent(new ServiceSchedulingMenu(main, LANDING_TAB));
    }

    private void manageWorkScheduleView() {
        main.content.setSecondComponent(new WorkSchedulingMenu(main, LANDING_TAB));

    }
//
//    private void manageIncidentsView() {
//        main.content.setSecondComponent(new IncidentsMenu(main, LANDING_TAB));
//
//    }

    private void manageServicesPerformedView() {
        main.content.setSecondComponent(new ServicePerformedMenu(main, LANDING_TAB));
    }
//                            manageIncidentsView

    private void manageIncidentsView() {
        main.content.setSecondComponent(new IncidentMenu(main, LANDING_TAB));
    }

    private void manageExternalContactView() {
        main.content.setSecondComponent(new ContactUSMenu(main, LANDING_TAB));
    }
//                            manageIncidentsView

    
    private void manageServiceRequestView(){
        main.content.setSecondComponent(new ServiceRequestMenu(main, LANDING_TAB));
    }
    
//    private void manageSiteDatabaseView(){
//        main.content.setSecondComponent(new SiteDatabaseMenu(main, LANDING_TAB));
//    }
}
