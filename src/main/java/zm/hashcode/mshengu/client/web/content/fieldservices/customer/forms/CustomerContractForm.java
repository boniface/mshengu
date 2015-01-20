/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.forms;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import zm.hashcode.mshengu.app.util.UIComboBoxHelper;
import zm.hashcode.mshengu.app.util.UIComponentHelper;
import zm.hashcode.mshengu.app.util.validation.UIValidatorHelper;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.models.CustomerContractBean;

/**
 *
 * @author Ferox
 */
public class CustomerContractForm  extends FormLayout {

    private UIComponentHelper UIComponent = new UIComponentHelper();
    private UIComboBoxHelper UIComboBox = new UIComboBoxHelper();
    private final CustomerContractBean bean;
    public final BeanItem<CustomerContractBean> item;
    public final FieldGroup binder;
    // Define Buttons
    public Button save = new Button("Save");
    public Button edit = new Button("Edit");
    public Button cancel = new Button("Cancel");
    public Button update = new Button("Update");
    public Button delete = new Button("Delete");
    public Label errorMessage;
    
    public CustomerContractForm() {
        bean = new CustomerContractBean();
        item = new BeanItem<>(bean);
        binder = new FieldGroup(item);
        HorizontalLayout buttons = getButtons();
        buttons.setSizeFull();
        // Determines which properties are shown
        update.setVisible(false);
        delete.setVisible(false);

        // UIComponent
        
//        ComboBox customerId = UIComboBox.getCustomerComboBox("Customer", "customerId", ContractBean.class, binder);
        DateField startDate = UIComponent.getDateField("Start Date", "startDate", CustomerContractBean.class, binder);
        startDate = UIValidatorHelper.setRequiredDateField(startDate, "Start Date");
        
        DateField endDate = UIComponent.getDateField("End Date", "endDate", CustomerContractBean.class, binder);
        endDate = UIValidatorHelper.setRequiredDateField(endDate, "End Date");
        DateField dateofAction = UIComponent.getDateField("Date of Action", "dateofAction", CustomerContractBean.class, binder);
        
        TextField numberOfUnits = UIComponent.getTextField("Number of Units:", "numberOfUnits", CustomerContractBean.class, binder);
        numberOfUnits = UIValidatorHelper.setRequiredTextField(numberOfUnits, "Number of Units");
        
        TextField pricePerUnit = UIComponent.getBigDecimalTextField("Price per Unit :", "pricePerUnit", CustomerContractBean.class, binder);
        
        TextField status = UIComponent.getTextField("Status :", "status", CustomerContractBean.class, binder);
        
        ComboBox contractTypeId = UIComboBox.getContractTypeComboBox("Contract Type :", "contractTypeId", CustomerContractBean.class, binder);
        contractTypeId = UIValidatorHelper.setRequiredComboBox(contractTypeId, "Contract Type");
        
        errorMessage = UIComponent.getErrorLabel();

        GridLayout grid = new GridLayout(4, 10);
        grid.setSizeFull();

        
        grid.addComponent(errorMessage, 1, 0, 2, 0);
        
        grid.addComponent(startDate, 0, 2);        
        grid.addComponent(endDate, 1, 2);
        grid.addComponent(numberOfUnits, 2, 2);

        grid.addComponent(pricePerUnit, 0, 3);
//        grid.addComponent(status, 1, 1);
        grid.addComponent(contractTypeId, 1, 3);
        


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
