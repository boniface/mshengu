/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.siderbar;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Tree;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.CustomerMenu;

/**
 *
 * @author boniface
 */
public class CustomersTree extends Tree implements ItemClickEvent.ItemClickListener {

    private final MshenguMain main;
    public static final Object CUSTOMER_DETAILS = "Customer Management";
    private static final String LANDING_TAB = "LANDING";

    public CustomersTree(MshenguMain main) {
        this.main = main;
        addItem(CUSTOMER_DETAILS);

        //Add Listeners
        addItemClickListener((ItemClickEvent.ItemClickListener) this);
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        Object itemId = event.getItemId();
        if (itemId != null) {
            if (CUSTOMER_DETAILS.equals(itemId)) {
                manageCustomersView();
            }
        }
    }

    private void manageCustomersView() {
        main.content.setSecondComponent(new CustomerMenu(main, LANDING_TAB));

    }
}
