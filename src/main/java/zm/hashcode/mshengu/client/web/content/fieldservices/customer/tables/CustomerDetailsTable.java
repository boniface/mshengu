/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.tables;

import com.vaadin.ui.Table;
import java.util.List;
import zm.hashcode.mshengu.app.facade.customer.CustomerFacade;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.customer.Customer;

/**
 *
 * @author Ferox
 */
public class CustomerDetailsTable extends Table {

    private final MshenguMain main;

    public CustomerDetailsTable(MshenguMain main) {
        this.main = main;
        setSizeFull();


        addContainerProperty("Customer Name", String.class, null);
        addContainerProperty("Contact Person Name", String.class, null);
        addContainerProperty("Main Number", String.class, null);
        addContainerProperty("Email Address", String.class, null);
        //addContainerProperty("Contact Number", String.class, null);

        // Add Data Columns
        List<Customer> customers = CustomerFacade.getCustomerService().findAll();

        for (Customer customer : customers) {
            String cPersonName = "N/A";
            String cPersonMainNumber = "N/A";
            String cPersonEmailAddress = "N/A";
            if (customer.getContactPerson() != null) {
                cPersonName = customer.getContactPersonName();
                cPersonMainNumber = customer.getContactPersonMainNumber();
                cPersonEmailAddress = customer.getContactPersonEmail();
            }
            addItem(new Object[]{customer.getName(),
                cPersonName,
                cPersonMainNumber,
                cPersonEmailAddress,}, customer.getId());
        }
        // Allow selecting items from the table.
        setNullSelectionAllowed(false);

        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);




    }
}
