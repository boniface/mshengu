/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.contactus.tables;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import zm.hashcode.mshengu.app.facade.external.ContactUSFacade;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.app.util.UITableIconHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.external.ContactUS;
import zm.hashcode.mshengu.domain.incident.UserAction;

/**
 *
 * @author Luckbliss
 */
public class ContactUSFollowUpTable extends Table {

    private final MshenguMain main;
    private DateTimeFormatHelper formatHelper = new DateTimeFormatHelper();
    private UITableIconHelper tableIconHelper = new UITableIconHelper();
    private final String id;

    public ContactUSFollowUpTable(MshenguMain app, final String id) {
        this.main = app;
        this.id = id;


        addContainerProperty("Updated On", String.class, null);
        addContainerProperty("Resolved Date", String.class, null);
        addContainerProperty("Quality Assurance Date", String.class, null);
        addContainerProperty("Comment", String.class, null);
        addContainerProperty("Responder", String.class, null);
        addContainerProperty("Status ", String.class, null);
        addContainerProperty("Active ", Embedded.class, null);


        // Allow selecting items from the table.
        setNullSelectionAllowed(false);

        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);
        setSizeFull();
        loadUserActions(id);


    }

    public final void loadUserActions(String contactUSId) {
        // Add Data Columns
        ContactUS contactUS = ContactUSFacade.getContactUSService().findById(contactUSId);
        removeAllItems();

        boolean active = true;
        if (contactUS.getUserAction() != null) {
            for (UserAction userAction : contactUS.getUserAction()) {
                addItem(new Object[]{
                    //                userAction.getStatus(),
                    formatHelper.getDayMonthYear(userAction.getActionDate()),
                    formatHelper.getDayMonthYear(userAction.getResolvedDate()),
                    formatHelper.getDayMonthYear(userAction.getQualityAssuranceDate()),
                    userAction.getComment(),
                    userAction.getStaffName(),
                    userAction.getUserActionStatusName(), //                incident.getComment()
                    tableIconHelper.getCheckOrBlank(active)
                }, userAction.getId());
                active = false;
            }
        }
    }
}
