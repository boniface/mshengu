/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.views;

import com.vaadin.ui.VerticalLayout;
import zm.hashcode.mshengu.app.facade.external.IncomingRFQFacade;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.forms.SendResonseToQuoteRequestPDFForm;
import zm.hashcode.mshengu.domain.external.IncomingRFQ;

/**
 *
 * @author given
 */
public class QuoteRequestsFollowUpTab extends VerticalLayout {

    private final MshenguMain main;
    private final SendResonseToQuoteRequestPDFForm form;

    public QuoteRequestsFollowUpTab(MshenguMain app, final String id, String responseTotal, String responseComment) {
        
        this.main = app;
        IncomingRFQ rFQToSend = getQuoteToSend(id);
        form = new SendResonseToQuoteRequestPDFForm(main, rFQToSend, responseTotal, responseComment);
        setSizeFull(); 
        //addComponent(form);
        addComponent(form);
    }

    private IncomingRFQ getQuoteToSend(String id) {
        final IncomingRFQ rFQ = IncomingRFQFacade.getIncomingRFQService().findById(id);
        return rFQ;
    }
 

}
