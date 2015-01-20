/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer;

import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.views.CustomerContractsTab;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.views.CustomerDetailsTab;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.views.NewCustomerTab;

/**
 *
 * @author Ferox
 */
public class CustomerMenu extends VerticalLayout {

    private MshenguMain main;
    private TabSheet tab;
    private CustomerContractsTab customerContractsTab;
    private NewCustomerTab newCustomerTab;

    public CustomerMenu(MshenguMain app, String selectedTab) {
        main = app;

        Label heading = new Label("CREATE A NEW CUSTOMER");
        heading.setSizeUndefined();
        heading.addStyleName("h4");
        VerticalLayout customerDetailsTab = new VerticalLayout();
        customerDetailsTab.setMargin(true);
        customerDetailsTab.addComponent(new CustomerDetailsTab(main));

        customerContractsTab = new CustomerContractsTab(main);
        newCustomerTab = new NewCustomerTab(app);
//        customerContractsTab.setMargin(true);
//        customerContractsTab.addComponent(new CustomerContractsTab(main));

        tab = new TabSheet();
        tab.setHeight("100%");
        tab.setWidth("100%");
        tab.addTab(customerDetailsTab, "Customer Details", null);
        tab.addTab(customerContractsTab, "Hiring Terms", null);
        tab.addTab(newCustomerTab, "New Customer Form", null);

        if (selectedTab.equals("LANDING")) {
            tab.setSelectedTab(customerDetailsTab);
        } else if (selectedTab.equals("CONTRACTS")) {
            tab.setSelectedTab(customerContractsTab);
        } else if (selectedTab.equals("New_Customer")) {
            tab.setSelectedTab(newCustomerTab);
        }
        addComponent(heading);
        addComponent(tab);
        setMargin(true);

    }

    public CustomerMenu(MshenguMain app, String selectedTab, String selectedCustomerId) {
        CustomerMenu customerMenu = new CustomerMenu(app, selectedTab);
        customerMenu.getCustomerContractsTab().setSelectedCustomer(selectedCustomerId);
    }

    /**
     * @return the customerContractsTab
     */
    public CustomerContractsTab getCustomerContractsTab() {
        return customerContractsTab;
    }

}
