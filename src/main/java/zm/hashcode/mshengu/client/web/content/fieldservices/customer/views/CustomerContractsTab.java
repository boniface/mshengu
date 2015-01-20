/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.views;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import zm.hashcode.mshengu.app.facade.customer.ContractFacade;
import zm.hashcode.mshengu.app.facade.customer.ContractTypeFacade;
import zm.hashcode.mshengu.app.facade.customer.CustomerFacade;
import zm.hashcode.mshengu.app.util.UIComboBoxHelper;
import zm.hashcode.mshengu.app.util.validation.OnSubmitValidationHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.CustomerMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.forms.CustomerContractForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.forms.SelectCustomerForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.models.CustomerContractBean;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.models.SelectCustomerBean;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.tables.CustomerContractTable;
import zm.hashcode.mshengu.domain.customer.Contract;
import zm.hashcode.mshengu.domain.customer.ContractType;
import zm.hashcode.mshengu.domain.customer.Customer;

/**
 *
 * @author Ferox
 */
public class CustomerContractsTab extends VerticalLayout implements
        Button.ClickListener, Property.ValueChangeListener {

    private final MshenguMain main;
    private final CustomerContractForm form;
    private final CustomerContractTable table;
    private final SelectCustomerForm selectCustomerform;
    private String selectedCustomerId;
    private UIComboBoxHelper UIComboBox = new UIComboBoxHelper();

    public CustomerContractsTab(MshenguMain app) {
        main = app;
        selectCustomerform = new SelectCustomerForm();


        form = new CustomerContractForm();
        table = new CustomerContractTable(main);

        setSizeFull();
//    ComboBox customerId = UIComboBox.getCustomerComboBox("Customer", "customerId", CustomerContractBean.class, binder);

        addComponent(selectCustomerform);

        addComponent(form);

        addComponent(table);

        addListeners();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        final Button source = event.getButton();
        if (source == form.save) {
            saveForm(form.binder);
        } else if (source == form.edit) {
            setEditFormProperties();
        } else if (source == form.cancel) {
            getHome();
        } else if (source == form.update) {
            saveEditedForm(form.binder);
        } else if (source == form.delete) {
            deleteForm(form.binder);
        }
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        final Property property = event.getProperty();
        if (property == table) {
            final Contract contract = ContractFacade.getContractService().findById(table.getValue().toString());
            form.binder.setItemDataSource(new BeanItem<>(getBean(contract)));
            setReadFormProperties();
        } else if (property == selectCustomerform.comboBoxSelectCustomer) {
            if (selectCustomerform.comboBoxSelectCustomer.getValue() != null) {
                String customerId = selectCustomerform.comboBoxSelectCustomer.getValue().toString();

                Customer customer = CustomerFacade.getCustomerService().findById(customerId);
                if (customer != null) {
                    selectCustomerform.comboBoxSelectCustomer.removeValueChangeListener((Property.ValueChangeListener) this);
                    table.loadCustomerContracts(customer.getContracts());
                    selectCustomerform.comboBoxSelectCustomer.addValueChangeListener((Property.ValueChangeListener) this);
                }
            }
        }
    }

    public void setSelectedCustomer(String selectedCustomerId) {
        if (selectedCustomerId != null) {
            selectCustomerform.comboBoxSelectCustomer.setValue(selectedCustomerId);
        }
    }

    private boolean checkIfCustomerWasSelected(FieldGroup binder) {
        boolean isCustomerSelected = false;
        try {
            selectCustomerform.binder.commit();
            final SelectCustomerBean selectCustomerBean = ((BeanItem<SelectCustomerBean>) selectCustomerform.binder.getItemDataSource()).getBean();
            selectedCustomerId = selectCustomerBean.getCustomerId();
            isCustomerSelected = true;
        } catch (FieldGroup.CommitException e) {
            Collection<Field<?>> fields = binder.getFields();
            OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
            helper.doValidation();
            Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);            
        } finally {
            return isCustomerSelected;
        }
    }

    private void saveForm(FieldGroup binder) {
        if (checkIfCustomerWasSelected(binder)) {
            saveContract(binder);            
        }
    }

    private void saveEditedForm(FieldGroup binder) {
        try {
            if (checkIfCustomerWasSelected(binder)) {
                binder.commit();
                Contract contract = getUpdateEntity(binder);
                ContractFacade.getContractService().merge(contract);
                saveContract(binder);
                Notification.show("Record UPDATED!", Notification.Type.TRAY_NOTIFICATION);
            }
        } catch (FieldGroup.CommitException e) {
            Collection<Field<?>> fields = binder.getFields();
            OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
            helper.doValidation();
            Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);            
        }
    }

    private void saveContract(FieldGroup binder) {
        try {
            binder.commit();
            Contract contract = getEntity(binder, selectedCustomerId);
            ContractFacade.getContractService().persist(contract);
            updateCustomerContracts(contract);
            getHome();
            Notification.show("Record ADDED!", Notification.Type.TRAY_NOTIFICATION);
        } catch (FieldGroup.CommitException e) {
            Collection<Field<?>> fields = binder.getFields();
            OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
            helper.doValidation();
            Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);           
        }

    }

    private void deleteForm(FieldGroup binder) {
//        Contract contract = getEntity(binder);
//        Customer cust = CustomerFacade.getCustomerService().findById(contract.getParentId());
//        ContractFacade.getContractService().persist(contract);
//        updateCustomer(contract);
//        
//        final SelectCustomerBean selectCustomerBean = ((BeanItem<SelectCustomerBean>) selectCustomerform.binder.getItemDataSource()).getBean();
//        ContractFacade.getContractService().delete(getEntity(binder));
        Notification.show("Not Allowed to delete contrats!", Notification.Type.TRAY_NOTIFICATION);
        getHome();
    }

    private Contract getEntity(FieldGroup binder, String selectedCustomerId) {
        final CustomerContractBean contractBean = ((BeanItem<CustomerContractBean>) binder.getItemDataSource()).getBean();
        ContractType contractType = ContractTypeFacade.getContractTypeService().findById(contractBean.getContractTypeId());
//        Status status = StatusFacade.getStatusService().findById(contractBean.getStatus());

        final Contract contract = new Contract.Builder(new Date())
                .startDate(contractBean.getStartDate())
                .endDate(contractBean.getEndDate())
                .numberOfUnits(contractBean.getNumberOfUnits())
                .pricePerUnit(contractBean.getPricePerUnit())
//                .status(contractBean.getStatus())
                .contractType(contractType)
                //                .id(contractBean.getId())
                .parentId(selectedCustomerId)
                .build();

        return contract;

    }

    private Contract getUpdateEntity(FieldGroup binder) {
        final CustomerContractBean contractBean = ((BeanItem<CustomerContractBean>) binder.getItemDataSource()).getBean();
        final Contract activeContract = ContractFacade.getContractService().findById(contractBean.getId());

        final Contract contract = new Contract.Builder(contractBean.getDateofAction())
                .contract(activeContract)
                .dateofUpdate(new Date())
                .build();

        return contract;

    }

    private void getHome() {
        main.content.setSecondComponent(new CustomerMenu(main, "CONTRACTS"));
    }

    private void setEditFormProperties() {
        form.binder.setReadOnly(false);
        form.save.setVisible(false);
        form.edit.setVisible(false);
        form.cancel.setVisible(true);
        form.delete.setVisible(false);
        form.update.setVisible(true);
    }

    private void setReadFormProperties() {
        form.binder.setReadOnly(true);
        //Buttons Behaviour
        form.save.setVisible(false);
        form.edit.setVisible(true);
        form.cancel.setVisible(true);
        form.delete.setVisible(true);
        form.update.setVisible(false);
    }

    private void addListeners() {
        //Register Button Listeners
        form.save.addClickListener((Button.ClickListener) this);
        form.edit.addClickListener((Button.ClickListener) this);
        form.cancel.addClickListener((Button.ClickListener) this);
        form.update.addClickListener((Button.ClickListener) this);
        form.delete.addClickListener((Button.ClickListener) this);
        //Register Table Listerners
        table.addValueChangeListener((Property.ValueChangeListener) this);
        selectCustomerform.comboBoxSelectCustomer.addValueChangeListener((Property.ValueChangeListener) this);
    }

    private void updateCustomerContracts(Contract contract) {

        try {
            Set<Contract> contacts = new HashSet<>();

            Customer originalCustomer = CustomerFacade.getCustomerService().findById(selectedCustomerId);
            contacts.addAll(originalCustomer.getContracts());
            contacts.add(contract);


            Customer newCustomer = new Customer.Builder(originalCustomer.getName())
                    .customer(originalCustomer)
                    .contracts(contacts)
                    .build();

            CustomerFacade.getCustomerService().merge(newCustomer);
        } catch (Exception e) {
            Notification.show("Error assosiating contract with customer!", Notification.Type.TRAY_NOTIFICATION);
            getHome();
        }
    }

    private CustomerContractBean getBean(Contract contract) {
        CustomerContractBean bean = new CustomerContractBean();
        bean.setStartDate(contract.getStartDate());
        bean.setEndDate(contract.getEndDate());
        bean.setDateofAction(contract.getDateofAction());
        bean.setNumberOfUnits(contract.getNumberOfUnits());
        bean.setPricePerUnit(contract.getPricePerUnit());
//        bean.setStatus(contract.getStatus());
        bean.setContractTypeId(contract.getContractTypeId());
        bean.setId(contract.getId());
        bean.setParentId(contract.getParentId());
        return bean;
    }
}