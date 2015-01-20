/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.views;

import com.vaadin.ui.VerticalLayout;
import zm.hashcode.mshengu.app.facade.external.IncomingRFQFacade;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.forms.NewCustomerPDFForm;
import zm.hashcode.mshengu.domain.external.IncomingRFQ;

/**
 *
 * @author given
 */
public class NewCustomerTab extends VerticalLayout {

    private final MshenguMain main;
    private final NewCustomerPDFForm form;

    public NewCustomerTab(MshenguMain app) {
        
        this.main = app;
        form = new NewCustomerPDFForm(main);
        setSizeFull(); 
        //addComponent(form);
        addComponent(form);
    }

    private IncomingRFQ getQuoteToSend(String id) {
        final IncomingRFQ rFQ = IncomingRFQFacade.getIncomingRFQService().findById(id);
        return rFQ;
    }
 

}
