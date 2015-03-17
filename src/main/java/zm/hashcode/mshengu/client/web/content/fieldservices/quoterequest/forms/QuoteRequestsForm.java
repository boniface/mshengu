/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.forms;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import zm.hashcode.mshengu.app.util.UIComboBoxHelper;
import zm.hashcode.mshengu.app.util.UIComponentHelper;
import zm.hashcode.mshengu.app.util.validation.UIValidatorHelper;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.models.QuoteRequestBean;

/**
 *
 * @author Luckbliss
 */
public class QuoteRequestsForm extends FormLayout {

    private UIComponentHelper UIComponent = new UIComponentHelper();
    private UIComboBoxHelper UIComboBox = new UIComboBoxHelper();
    private final QuoteRequestBean bean;
    public final BeanItem<QuoteRequestBean> item;
    public final FieldGroup binder;
    // Define Buttons
    public Button save = new Button("Save");
    public Button edit = new Button("Edit");
    public Button cancel = new Button("Cancel");
    public Button update = new Button("Update");
    public Button delete = new Button("Delete");
    public Button accepted = new Button("Accepted");
    public Button declined = new Button("Declined");
    public Label errorMessage;

    public QuoteRequestsForm() {
        bean = new QuoteRequestBean();
        item = new BeanItem<>(bean);
        binder = new FieldGroup(item);

        update.setVisible(false);
        delete.setVisible(false);
        edit.setVisible(false);
        
        accepted.setStyleName("background-blue");
        declined.setStyleName("background-blue");
        accepted.setVisible(false);
        declined.setVisible(false);
        
        HorizontalLayout buttons = getButtons();
        buttons.setSizeFull();
        
        TextField refNumber = UIComponent.getTextField("Reference Number:", "refNumber", QuoteRequestBean.class, binder);
        refNumber.setReadOnly(true);
        
        DateField actionDate = UIComponent.getDateField("Request Date:", "actionDate", QuoteRequestBean.class, binder);
        actionDate = UIValidatorHelper.setRequiredDateField(actionDate, "Request Date");
        
        TextField companyNameNonRequired = UIComponent.getTextField("Company Name:", "companyNameNonRequired", QuoteRequestBean.class, binder);
        
        TextField vatRegistrationNumberUnrequired = UIComponent.getTextField("VAT Registration Number:", "vatRegistrationNumberUnrequired", QuoteRequestBean.class, binder);
        
        TextField contactPersonFirstname = UIComponent.getTextField("Contact Person Firstname:", "contactPersonFirstname", QuoteRequestBean.class, binder);
        contactPersonFirstname = UIValidatorHelper.setRequiredTextField(contactPersonFirstname, "Contact Person Firstname");
        
        TextField contactPersonLastname = UIComponent.getTextField("Contact Person Lastname:", "contactPersonLastname", QuoteRequestBean.class, binder);
        contactPersonLastname = UIValidatorHelper.setRequiredTextField(contactPersonLastname, "Contact Person Lastname");
        
        TextField telephoneNumberNonRequired = UIComponent.getTextField("Company Tel Number:", "telephoneNumberNonRequired", QuoteRequestBean.class, binder);
        telephoneNumberNonRequired.addValidator(UIValidatorHelper.phoneNumberValidator());
        
        TextField contactNumber = UIComponent.getTextField("Contact Number:", "contactNumber", QuoteRequestBean.class, binder);
        contactNumber = UIValidatorHelper.setRequiredTextField(contactNumber, "Contact Number");
        contactNumber.addValidator(UIValidatorHelper.phoneNumberValidator());
        
        TextField faxNumber = UIComponent.getTextField("Fax Number:", "faxNumber", QuoteRequestBean.class, binder);
        faxNumber.addValidator(UIValidatorHelper.faxNumberValidator());
        
        TextField email = UIComponent.getTextField("Email :", "email", QuoteRequestBean.class, binder);
        email.addValidator(UIValidatorHelper.emailValidator());
        email = UIValidatorHelper.setRequiredTextField(email, "Email");
        
        TextArea billingAddress = UIComponent.getTextArea("Billing Address", "billingAddress", QuoteRequestBean.class, binder);
        billingAddress = UIValidatorHelper.setRequiredTextArea(billingAddress, "Billing Address");         
        
        TextArea deliveryAddress = UIComponent.getTextArea("Delivery Address", "deliveryAddress", QuoteRequestBean.class, binder);
        deliveryAddress = UIValidatorHelper.setRequiredTextArea(deliveryAddress, "Delivery Address");  
        
        ComboBox eventType = UIComboBox.getEventTypeComboBox("Event Type:", "eventType", QuoteRequestBean.class, binder);
        eventType = UIValidatorHelper.setRequiredComboBox(eventType, "Event Type");
        
        TextField eventName = UIComponent.getTextField("Event Name:", "eventName", QuoteRequestBean.class, binder);
        
        DateField eventDate = UIComponent.getDateField("Event Date:", "eventDate", QuoteRequestBean.class, binder);
        eventDate = UIValidatorHelper.setRequiredDateField(eventDate, "Event Date");
        
        ComboBox toiletsRequired1 = UIComboBox.getUnitTypeComboBox("Toilets Required:", "toiletsRequired1", QuoteRequestBean.class, binder);
        toiletsRequired1 = UIValidatorHelper.setRequiredComboBox(toiletsRequired1, "Toilets Required");
        
        TextField quantityRequired1 = UIComponent.getTextField("Quantity Required:", "quantityRequired1", QuoteRequestBean.class, binder);
        quantityRequired1 = UIValidatorHelper.setRequiredTextField(quantityRequired1, "Quantity Required");
        
        ComboBox toiletsRequired2 = UIComboBox.getUnitTypeComboBox("Additional Toilets Required 1:", "toiletsRequired2", QuoteRequestBean.class, binder);               
        TextField quantityRequired2 = UIComponent.getTextField(" Additional Quantity Required 1:", "quantityRequired2", QuoteRequestBean.class, binder);
        ComboBox toiletsRequired3 = UIComboBox.getUnitTypeComboBox("Additional Toilets Required 2:", "toiletsRequired3", QuoteRequestBean.class, binder);
        TextField quantityRequired3 = UIComponent.getTextField("Additional Quantity Required 2:", "quantityRequired3", QuoteRequestBean.class, binder);
        TextField numberOfJanitors = UIComponent.getTextField("Number Of Janitors:", "numberOfJanitors", QuoteRequestBean.class, binder);
        TextField numberOfToiletRolls = UIComponent.getTextField("Number Of Toilet Rolls:", "numberOfToiletRolls", QuoteRequestBean.class, binder);
        
        DateField deliveryDate = UIComponent.getDateField("Delivery Date:", "deliveryDate", QuoteRequestBean.class, binder);
        deliveryDate = UIValidatorHelper.setRequiredDateField(deliveryDate, "Delivery Date");
        
        DateField collectionDate = UIComponent.getDateField("Collection Date:", "collectionDate", QuoteRequestBean.class, binder);
        collectionDate = UIValidatorHelper.setRequiredDateField(collectionDate, "Collection Date");
        
        TextField daysRental = UIComponent.getTextField("Days Rental:", "daysRental", QuoteRequestBean.class, binder);
        TextArea comment = UIComponent.getTextArea("Comment", "comment", QuoteRequestBean.class, binder);
        comment.addValidator(new BeanValidator(QuoteRequestBean.class, "comment"));
        CheckBox serviceFrequencyMon = UIComponent.getCheckBox("Monday", "serviceFrequencyMon", QuoteRequestBean.class, binder);
        CheckBox serviceFrequencyTue = UIComponent.getCheckBox("Tuesday", "serviceFrequencyTue", QuoteRequestBean.class, binder);
        CheckBox serviceFrequencyWed = UIComponent.getCheckBox("Wednesday", "serviceFrequencyWed", QuoteRequestBean.class, binder);
        CheckBox serviceFrequencyThur = UIComponent.getCheckBox("Thursday", "serviceFrequencyThur", QuoteRequestBean.class, binder);
        CheckBox serviceFrequencyFri = UIComponent.getCheckBox("Friday", "serviceFrequencyFri", QuoteRequestBean.class, binder);
        CheckBox serviceFrequencySat = UIComponent.getCheckBox("Saturday", "serviceFrequencySat", QuoteRequestBean.class, binder);
        CheckBox serviceFrequencySun = UIComponent.getCheckBox("Sunday", "serviceFrequencySun", QuoteRequestBean.class, binder);

        errorMessage = UIComponent.getErrorLabel();

        GridLayout grid = new GridLayout(3, 16);
        grid.setSizeFull();
        
        grid.addComponent(errorMessage, 1, 0, 2, 0);

        grid.addComponent(refNumber, 0, 1);
        grid.addComponent(actionDate, 1, 1);
        grid.addComponent(companyNameNonRequired, 2, 1);
        
        grid.addComponent(vatRegistrationNumberUnrequired, 0, 2);
        grid.addComponent(contactPersonFirstname, 1, 2);
        grid.addComponent(contactPersonLastname, 2, 2);
        
        grid.addComponent(telephoneNumberNonRequired, 0, 3);
        grid.addComponent(email, 1, 3);
        grid.addComponent(faxNumber, 2, 3);
        
        grid.addComponent(contactNumber, 0, 4);
        grid.addComponent(billingAddress, 1, 4);
        grid.addComponent(deliveryAddress, 2, 4);

        grid.addComponent(eventDate, 0, 5);
        grid.addComponent(eventType, 1, 5);
        grid.addComponent(eventName, 2, 5);

        grid.addComponent(toiletsRequired1, 0, 6);
        grid.addComponent(quantityRequired1, 1, 6);
        grid.addComponent(numberOfJanitors, 2, 6);

        grid.addComponent(toiletsRequired2, 0, 7);
        grid.addComponent(quantityRequired2, 1, 7);
        grid.addComponent(numberOfToiletRolls, 2, 7);

        grid.addComponent(toiletsRequired3, 0, 8);
        grid.addComponent(quantityRequired3, 1, 8);
        grid.addComponent(comment, 2, 8);

        grid.addComponent(deliveryDate, 0, 9);
        grid.addComponent(collectionDate, 1, 9);
        grid.addComponent(daysRental, 2, 9);

        grid.addComponent(serviceFrequencyMon, 0, 10);
        grid.addComponent(serviceFrequencyTue, 1, 10);
        grid.addComponent(serviceFrequencyWed, 2, 10);

        grid.addComponent(serviceFrequencyThur, 0, 11);
        grid.addComponent(serviceFrequencyFri, 1, 11);
        grid.addComponent(serviceFrequencySat, 2, 11);

        grid.addComponent(serviceFrequencySun, 0, 12);

        grid.addComponent(new Label("<hr/>", ContentMode.HTML), 0, 13, 2, 13);
        grid.addComponent(buttons, 0, 14, 2, 14);

        addComponent(grid);
    }

    private HorizontalLayout getButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        save.setSizeFull();
        edit.setSizeFull();
        cancel.setSizeFull();
        update.setSizeFull();
        delete.setSizeFull();
        accepted.setSizeFull();
        declined.setSizeFull();
        

        buttons.addComponent(save);
        buttons.addComponent(edit);
        buttons.addComponent(cancel);
        buttons.addComponent(update);
        buttons.addComponent(delete);
        buttons.addComponent(accepted);
        buttons.addComponent(declined);
        return buttons;
    }
}
