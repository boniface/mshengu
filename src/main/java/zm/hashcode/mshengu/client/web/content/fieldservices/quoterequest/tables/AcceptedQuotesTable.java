/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.tables;

import com.vaadin.ui.Table;
import java.util.List;
import zm.hashcode.mshengu.app.facade.external.IncomingRFQFacade;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.app.util.UITableIconHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.views.AcceptedQuotesTab;
import zm.hashcode.mshengu.domain.external.IncomingRFQ;

/**
 *
 * @author Luckbliss
 */
public class AcceptedQuotesTable extends Table {

    private final MshenguMain main;
    private DateTimeFormatHelper formatHelper = new DateTimeFormatHelper();
    private UITableIconHelper iconHelper = new UITableIconHelper();
    private AcceptedQuotesTab tab;

    public AcceptedQuotesTable(MshenguMain app, final AcceptedQuotesTab quoteRequestsTab) {
        this.main = app;
        this.tab = quoteRequestsTab;

        addContainerProperty("Reference No", String.class, null);
        addContainerProperty("Request Date", String.class, null);
        addContainerProperty("Delivery Date", String.class, null);
        addContainerProperty("Event Date", String.class, null);
        addContainerProperty("Customer", String.class, null);
        addContainerProperty("Event Name", String.class, null);
        addContainerProperty("Contact No", String.class, null);
        addContainerProperty("Sent Status", String.class, null);
        addContainerProperty("Acceptence Day", String.class, null);

        // Allow selecting items from the table.
        setNullSelectionAllowed(false);

        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);
        setSizeFull();

        loadQuoteRequests();

    }

    public final void loadQuoteRequests() {
        List<IncomingRFQ> incomingRFQs = IncomingRFQFacade.getIncomingRFQService().findByAcceptedStatus(Boolean.TRUE);

        if (incomingRFQs != null) {
            for (IncomingRFQ incomingRFQ : incomingRFQs) {

                if (incomingRFQ != null && incomingRFQ.getAcceptedStatus()) {
                   
                    addItem(new Object[]{
                        incomingRFQ.getRefNumber(),
                        formatHelper.getDayMonthYear(incomingRFQ.getDateOfAction()),
                        formatHelper.getDayMonthYear(incomingRFQ.getDeliveryDate()),
                        formatHelper.getDayMonthYear(incomingRFQ.getEventDate()),
                        incomingRFQ.getCompanyName(),
                        incomingRFQ.getEventName(),
                        incomingRFQ.getContactNumber(),
                        incomingRFQ.getStatus(),
                        formatHelper.getDayMonthYear(incomingRFQ.getQuoteAcceptenceDate())}, incomingRFQ.getId());
                }
            }
        }
    }
}
