/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.views.AcceptedQuotesTab;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.views.DeclinedQuotesTab;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.views.QuoteRequestsTab;

/**
 *
 * @author Luckbliss
 */
public class QuoteRequestsMenu extends VerticalLayout {

    private final MshenguMain main;
    private final TabSheet tab;
    private final QuoteRequestsTab quoteRequestsTab;
    private final AcceptedQuotesTab acceptedQuotesTab;
    private final DeclinedQuotesTab declinedQuotesTab;

    public QuoteRequestsMenu(MshenguMain app, String selectedTab) {
        main = app;

        quoteRequestsTab = new QuoteRequestsTab(main);
        acceptedQuotesTab = new AcceptedQuotesTab(main);
        declinedQuotesTab = new DeclinedQuotesTab(main);

        tab = new TabSheet();
        tab.setHeight("100%");
        tab.setWidth("100%");

        tab.addTab(quoteRequestsTab, "Quote Requested", null);
        tab.addTab(acceptedQuotesTab, "Accepted Quotes", null);
        tab.addTab(declinedQuotesTab, "Declined Quotes", null);

        if (selectedTab.equals("LANDING")) {
            tab.setSelectedTab(quoteRequestsTab);
        } else if (selectedTab.equals("ACCEPTED_QUOTES")) {
            tab.setSelectedTab(acceptedQuotesTab);
        } else if (selectedTab.equals("DECLINED_QUOTES")) {
            tab.setSelectedTab(declinedQuotesTab);
        }

        addComponent(tab);
    }
}
