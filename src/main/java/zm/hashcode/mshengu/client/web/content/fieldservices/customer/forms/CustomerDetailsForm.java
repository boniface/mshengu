/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.forms;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import zm.hashcode.mshengu.app.util.UIComponentHelper;
import zm.hashcode.mshengu.app.util.validation.MobileNumberValidator;
import zm.hashcode.mshengu.app.util.validation.UIValidatorHelper;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.models.CustomerDetailsBean;

/**
 * I
 *
 * @author Ferox
 */
public class CustomerDetailsForm extends FormLayout {

    private final UIComponentHelper UIComponent = new UIComponentHelper(); 
    private final CustomerDetailsBean bean;
    public final BeanItem<CustomerDetailsBean> item;
    public final FieldGroup binder;
    // Define Buttons
    public Button save = new Button("Activate Customer");
    public Button edit = new Button("Edit");
    public Button cancel = new Button("Cancel");
    public Button update = new Button("Update");
    public Button delete = new Button("Delete");
    public Label errorMessage;

    public CustomerDetailsForm() {
        bean = new CustomerDetailsBean();
        item = new BeanItem<>(bean);
        binder = new FieldGroup(item);
        HorizontalLayout buttons = getButtons();
        buttons.setSizeFull();
        // Determines which properties are shown
        update.setVisible(false);
        delete.setVisible(false);

        // UIComponent
        TextField name = UIComponent.getTextField("Customer Name", "name", CustomerDetailsBean.class, binder);
        name = UIValidatorHelper.setRequiredTextField(name, "Customer Name");
        
        TextField firstName = UIComponent.getTextField("Contact Person Name :", "firstName", CustomerDetailsBean.class, binder);
        firstName = UIValidatorHelper.setRequiredTextField(firstName, "Contact Person Name");
        
        TextField lastName = UIComponent.getTextField("Contact Person Surname :", "lastName", CustomerDetailsBean.class, binder);
        lastName = UIValidatorHelper.setRequiredTextField(lastName, "Contact Person Surname");        
        
        TextField mainNumber = UIComponent.getTextField("Office Number :", "mainNumber", CustomerDetailsBean.class, binder);
        mainNumber.addValidator(UIValidatorHelper.phoneNumberValidator());
        mainNumber = UIValidatorHelper.setRequiredTextField(mainNumber, "Office Number");

        TextField mobileNumber = UIComponent.getTextField("Mobile Number :", "otherNumber", CustomerDetailsBean.class, binder);
        mobileNumber = UIValidatorHelper.setRequiredTextField(mobileNumber, "Mobile Number");
        mobileNumber.addValidator(UIValidatorHelper.mobileNumberValidator());
        
        
        TextField emailAddress = UIComponent.getTextField("Contact Person Email :", "emailAddress", CustomerDetailsBean.class, binder);
        emailAddress.addValidator(UIValidatorHelper.emailValidator());        
        
        TextArea address = UIComponent.getTextArea("Office Adress :", "address", CustomerDetailsBean.class, binder);
        //address = UIValidatorHelper.setRequiredTextArea(address, "Office Adress");
        
        TextField position = UIComponent.getTextField("Contact Person Position :", "position", CustomerDetailsBean.class, binder);
        
        errorMessage = UIComponent.getErrorLabel();
        
        GridLayout grid = new GridLayout(4, 10);
        grid.setSizeFull();

        grid.addComponent(errorMessage, 1, 0, 2, 0);
        
        grid.addComponent(name, 0, 1);
        grid.addComponent(firstName, 1, 1);
        grid.addComponent(lastName, 2, 1);

        grid.addComponent(position, 0, 2);
        grid.addComponent(mainNumber, 1, 2);
        grid.addComponent(mobileNumber, 2, 2);

        grid.addComponent(emailAddress, 0, 3);
        grid.addComponent(address, 1, 3, 2, 3);
        //grid.addComponent(position, 2, 2);

        grid.addComponent(new Label("<hr/>", ContentMode.HTML), 0, 5, 2, 5);
        grid.addComponent(buttons, 0, 6, 2, 6);
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
