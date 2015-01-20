/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.site;

import com.vaadin.data.Property;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.servicesperformed.views.SiteSiteUnitTab;
import zm.hashcode.mshengu.client.web.content.fieldservices.site.forms.CustomerSiteForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.site.views.AllSiteDetailsTab;
import zm.hashcode.mshengu.client.web.content.fieldservices.site.views.CustomerSiteDetailsTab;
//import zm.hashcode.mshengu.client.web.content.fieldservices.site.views.NotActiveSiteDetailsTab;
import zm.hashcode.mshengu.client.web.content.fieldservices.site.views.SiteContactPersonDetailsTab;

/**
 *
 * @author Ferox
 */
public class ServiceSchedulingMenu extends VerticalLayout implements Property.ValueChangeListener {

    private final MshenguMain main;
    private final TabSheet tab;
//    private TabSheet tab;
    private final CustomerSiteForm selectCustomerSite;
    private final CustomerSiteDetailsTab customerSiteDetailsTab;
    private final AllSiteDetailsTab allSiteDetailsTab;
    private final SiteContactPersonDetailsTab contactPersonDetailsTab;
    private final SiteSiteUnitTab siteSiteUnitTab;
//    private final NotActiveSiteDetailsTab notActiveSiteDetailsTab;
    private String selectedCustomerId;
    private String selectedSiteId;

    public ServiceSchedulingMenu(MshenguMain app, String selectedTab) {
        main = app;

        selectCustomerSite = new CustomerSiteForm();
        customerSiteDetailsTab = new CustomerSiteDetailsTab(main);
        contactPersonDetailsTab = new SiteContactPersonDetailsTab(main);
        siteSiteUnitTab = new SiteSiteUnitTab(main, "LANDING");
        allSiteDetailsTab = new AllSiteDetailsTab(main);
//        notActiveSiteDetailsTab = new NotActiveSiteDetailsTab(main);

        tab = new TabSheet();
        tab.setHeight("100%");
        tab.setWidth("100%");
        tab.addTab(customerSiteDetailsTab, "Site Details", null);
        tab.addTab(contactPersonDetailsTab, "Site Contact Person", null);
        tab.addTab(allSiteDetailsTab, "Sites Database");
        
        
        if (selectedCustomerId != null) {
            if (selectedTab.equals("LANDING")) {
                tab.setSelectedTab(customerSiteDetailsTab);
            } else if (selectedTab.equals("CONTACT_PERSON")) {
                tab.setSelectedTab(contactPersonDetailsTab);
            } else if (selectedTab.equals("ALL_SITES")) {
                tab.setSelectedTab(allSiteDetailsTab);
            } 
        } else {

            Notification.show("Please select a customer!", Notification.Type.TRAY_NOTIFICATION);
        }
        addComponent(selectCustomerSite);
        addComponent(tab);

        selectCustomerSite.comboBoxSelectContractType.addValueChangeListener((Property.ValueChangeListener) this);
        selectCustomerSite.comboBoxSelectCustomer.addValueChangeListener((Property.ValueChangeListener) this);
        selectCustomerSite.comboBoxSelectSite.addValueChangeListener((Property.ValueChangeListener) this);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        final Property property = event.getProperty();
        if (property == selectCustomerSite.comboBoxSelectContractType) {
            if (selectCustomerSite.comboBoxSelectContractType.getValue().toString() != null) {
                String id = selectCustomerSite.comboBoxSelectContractType.getValue().toString();
                String caption = selectCustomerSite.comboBoxSelectContractType.getItemCaption(id);
                selectCustomerSite.comboBoxSelectCustomer.removeValueChangeListener((Property.ValueChangeListener) this);
                selectCustomerSite.loadCustomersByContract(caption);
                selectCustomerSite.comboBoxSelectCustomer.addValueChangeListener((Property.ValueChangeListener) this);
            }
        } else if (property == selectCustomerSite.comboBoxSelectCustomer) {
            if (selectCustomerSite.comboBoxSelectCustomer.getValue().toString() != null) {
                setSelectedCustomerId(selectCustomerSite.comboBoxSelectCustomer.getValue().toString());
                selectCustomerSite.comboBoxSelectSite.removeValueChangeListener((Property.ValueChangeListener) this);
                selectCustomerSite.loadCustomerSites(getSelectedCustomerId());
                selectCustomerSite.comboBoxSelectSite.addValueChangeListener((Property.ValueChangeListener) this);
            }
        } else if (property == selectCustomerSite.comboBoxSelectSite) {
            if (selectCustomerSite.comboBoxSelectSite.getValue().toString() != null) {
                setSelectedSiteId(selectCustomerSite.comboBoxSelectSite.getValue().toString());
//                setSelectedSiteInTabs(getSelectedCustomerId());
//                selectCustomerSite.loadCustomerSites(getSelectedCustomerId());

            }
        }
    }

    /**
     * @return the selectedCustomerId
     */
    public String getSelectedCustomerId() {
        return selectedCustomerId;
    }

    /**
     * @param selectedCustomerId the selectedCustomerId to set
     */
    public void setSelectedCustomerId(String selectedCustomerId) {
        this.selectedCustomerId = selectedCustomerId;
        setSelectedCustomerInTabs(getSelectedCustomerId());
    }

    public void setSelectedCustomerInTabs(String selectedCustomerId) {
        this.customerSiteDetailsTab.setParentId(selectedCustomerId);
//        this.contactPersonDetailsTab.setParentId(selectedCustomerId);
    }

    public void addListener() {
        selectCustomerSite.comboBoxSelectSite.addValueChangeListener((Property.ValueChangeListener) this);
    }

    /**
     * @return the selectedSiteId
     */
    public String getSelectedSiteId() {
        return selectedSiteId;
    }

    /**
     * @param selectedSiteId the selectedSiteId to set
     */
    public void setSelectedSiteId(String selectedSiteId) {
        this.selectedSiteId = selectedSiteId;
        setSelectedSiteInTabs(selectedSiteId);
    }

    public void setSelectedSiteInTabs(String selectedSiteId) {
        this.contactPersonDetailsTab.setParentId(selectedSiteId);
        this.siteSiteUnitTab.loadSiteUnits(selectedSiteId);
    }
}