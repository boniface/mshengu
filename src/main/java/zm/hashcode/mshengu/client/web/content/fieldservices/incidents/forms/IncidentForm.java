/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.incidents.forms;

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
import zm.hashcode.mshengu.client.web.content.fieldservices.incidents.models.IncidentBean;

/**
 *
 * @author Ferox
 */
public class IncidentForm extends FormLayout {

    private UIComponentHelper UIComponent = new UIComponentHelper();
    private UIComboBoxHelper UIComboBox = new UIComboBoxHelper();
    private final IncidentBean bean;
    public final BeanItem<IncidentBean> item;
    public final FieldGroup binder;
    // Define Buttons
    public Button save = new Button("Save");
    public Button edit = new Button("Edit");
    public Button cancel = new Button("Cancel");
    public Button update = new Button("Update");
    public Button delete = new Button("Delete");
    public Label errorMessage;

    public IncidentForm() {
        bean = new IncidentBean();
        item = new BeanItem<>(bean);
        binder = new FieldGroup(item);
        HorizontalLayout buttons = getButtons();
        buttons.setSizeFull();
        // Determines which properties are shown
        update.setVisible(false);
        delete.setVisible(false);

        // UIComponent
//          private String id;
        DateField actionDate = UIComponent.getDateField("Reported On:", "actionDate", IncidentBean.class, binder);

        TextField refNumber = UIComponent.getTextField("Reference Number:", "refNumber", IncidentBean.class, binder);

        TextField customer = UIComponent.getTextField("Client Name:", "customer", IncidentBean.class, binder);
        customer = UIValidatorHelper.setRequiredTextField(customer, "Client Name");

        TextField contactPerson = UIComponent.getTextField("Contact Person:", "contactPerson", IncidentBean.class, binder);
        contactPerson = UIValidatorHelper.setRequiredTextField(contactPerson, "Contact Person");

        TextField contactNumber = UIComponent.getTextField("Contact Number:", "contactNumber", IncidentBean.class, binder);
        contactNumber = UIValidatorHelper.setRequiredTextField(contactNumber, "Contact Number");
        contactNumber.addValidator(UIValidatorHelper.phoneNumberValidator());

        TextField email = UIComponent.getTextField("Email:", "email", IncidentBean.class, binder);
        email.addValidator(UIValidatorHelper.emailValidator());

        TextField site = UIComponent.getTextField("Site Name:", "site", IncidentBean.class, binder);

        TextField suburb = UIComponent.getTextField("Suburb:", "suburb", IncidentBean.class, binder);

        ComboBox toiletType = UIComboBox.getUnitTypeComboBox("Toilet Type:", "toiletType", IncidentBean.class, binder);
        toiletType = UIValidatorHelper.setRequiredComboBox(toiletType, "Toilet Type");

        ComboBox incidentType = UIComboBox.getIncidentTypeComboBox("Incident Type:", "incidentType", IncidentBean.class, binder);
        incidentType = UIValidatorHelper.setRequiredComboBox(incidentType, "Incident Type");

        CheckBox closed = UIComponent.getCheckBox("Closed:", "closed", IncidentBean.class, binder);
        ComboBox serviceProvider = UIComboBox.getSubcontractorsComboBox("Service Provider:", "serviceProvider", IncidentBean.class, binder);

//        ComboBox status = UIComboBox.getIncidentStatusComboBox("Status :", "status", IncidentBean.class, binder);
        ComboBox mailNotifications = UIComboBox.getMailNotificationComboBox("Notification Name:", "mailNotifications", IncidentBean.class, binder);
        mailNotifications = UIValidatorHelper.setRequiredComboBox(mailNotifications, "Notification Name");

        TextArea comment = UIComponent.getTextArea("Remarks:", "comment", IncidentBean.class, binder);
        comment.addValidator(new BeanValidator(IncidentBean.class, "comment"));

        errorMessage = UIComponent.getErrorLabel();

        GridLayout grid = new GridLayout(4, 10);
        grid.setSizeFull();

        grid.addComponent(errorMessage, 1, 0, 2, 0);

        grid.addComponent(refNumber, 0, 1);
        grid.addComponent(customer, 1, 1);
        grid.addComponent(mailNotifications, 2, 1);

        grid.addComponent(contactPerson, 0, 2);
        grid.addComponent(contactNumber, 1, 2);
        grid.addComponent(email, 2, 2);

        grid.addComponent(incidentType, 0, 3);
        grid.addComponent(site, 1, 3);
        grid.addComponent(suburb, 2, 3);

        grid.addComponent(toiletType, 0, 4);
        grid.addComponent(serviceProvider, 1, 4);
        grid.addComponent(closed, 2, 4);
        
        grid.addComponent(comment, 0, 5);


        grid.addComponent(new Label("<hr/>", ContentMode.HTML), 0, 6, 2, 6);
        grid.addComponent(buttons, 0, 7, 2, 7);

        addComponent(grid);

    }

    private HorizontalLayout getButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        save.setSizeFull();
        edit.setSizeFull();
        cancel.setSizeFull();
        update.setSizeFull();
        delete.setSizeFull();

        save.setStyleName("default");
        edit.setStyleName("default");
        cancel.setStyleName("default");
        update.setStyleName("default");
        delete.setStyleName("default");

        buttons.addComponent(save);
        buttons.addComponent(edit);
        buttons.addComponent(cancel);
        buttons.addComponent(update);
        buttons.addComponent(delete);
        return buttons;
    }
}
