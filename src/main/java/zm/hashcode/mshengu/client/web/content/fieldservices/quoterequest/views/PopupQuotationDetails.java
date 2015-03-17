/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.views;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import zm.hashcode.mshengu.client.web.MshenguMain;

/**
 *
 * @author given
 */
// Create a dynamically updating content for the popup
public class PopupQuotationDetails implements PopupView.Content, Button.ClickListener {

    private final TextField total;
    private VerticalLayout root;
    private final MshenguMain main;
    private final QuoteRequestsTab tab;
    private Button followUpButton;
    private TextArea commentTextArea;
    private String requestId;

    public PopupQuotationDetails(QuoteRequestsTab tab, MshenguMain main, String requestId) {
        this.requestId = requestId;
        this.main = main;
        this.tab = tab;
        root = new VerticalLayout();

        root.setSizeUndefined();
        root.setSpacing(true);
        root.setMargin(true);

        total = new TextField();
        total.setReadOnly(false);
        total.setWidth("300px");
        total.setNullRepresentation("");
        total.setImmediate(true);
        total.setStyleName("blackcolor");
        total.focus();
        
        commentTextArea = new TextArea();
        commentTextArea.setReadOnly(false);
        commentTextArea.setWidth("300px");
        commentTextArea.setNullRepresentation("");
        commentTextArea.setImmediate(true);
        commentTextArea.setStyleName("blackcolor");

        followUpButton = new Button("Continue");
        followUpButton.setStyleName(Reindeer.BUTTON_DEFAULT);
        followUpButton.setData(this.requestId);
        followUpButton.addClickListener((Button.ClickListener) this);

        root.addComponent(new Label("Please enter the Quotation's Total Price:"));
        root.addComponent(total);
        root.addComponent(new Label("<br/>", ContentMode.HTML));
        root.addComponent(new Label("Comments"));
        root.addComponent(commentTextArea);
        root.addComponent(new Label("<br/>", ContentMode.HTML));        
        root.addComponent(followUpButton);
        root.addComponent(new Label("<br/>", ContentMode.HTML));
        root.addComponent(new Label("Total including VAT."));
        

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        final Button source = event.getButton();
        if (source == followUpButton) {
            try {
                String responseTotal = total.getValue();
                float testValidity = Float.parseFloat(responseTotal); //check if numbers or not. Only numbers are allowed.

                String responseComment = commentTextArea.getValue();
                
                String quoteRequestId = (String) event.getButton().getData();
                QuoteRequestsFollowUpTab newTab = new QuoteRequestsFollowUpTab(this.main, quoteRequestId, responseTotal, responseComment);
                this.tab.removeAllComponents();
                this.tab.addComponent(newTab);

            } catch (NumberFormatException e) {
                Notification.show("Total Price must be Numbers only and is required to continue!\nClick here to correct.", Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public String getMinimizedValueAsHTML() {
        return "Respond";
    }

    @Override
    public Component getPopupComponent() {
        return root;
    }

};
