/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.views;

import com.mongodb.MongoException;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import zm.hashcode.mshengu.app.facade.customer.CustomerFacade;
import zm.hashcode.mshengu.app.facade.people.ContactPersonFacade;
import zm.hashcode.mshengu.app.util.validation.OnSubmitValidationHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.CustomerMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.forms.CustomerDetailsForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.models.CustomerDetailsBean;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.tables.CustomerDetailsTable;
import zm.hashcode.mshengu.domain.customer.Contract;
import zm.hashcode.mshengu.domain.customer.Customer;
import zm.hashcode.mshengu.domain.customer.Invoice;
import zm.hashcode.mshengu.domain.customer.Order;
import zm.hashcode.mshengu.domain.people.ContactPerson;
import zm.hashcode.mshengu.domain.products.Site;

/**
 *
 * @author Ferox
 */
public class CustomerDetailsTab extends VerticalLayout implements
        Button.ClickListener, Property.ValueChangeListener {

    private final MshenguMain main;
    private final CustomerDetailsForm form;
    private final CustomerDetailsTable table;
    String selectedCustomerID;

    public CustomerDetailsTab(MshenguMain app) {
        main = app;
        form = new CustomerDetailsForm();
        table = new CustomerDetailsTable(main);
        setSizeFull();
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
            selectedCustomerID = table.getValue().toString();

            if (selectedCustomerID != null) {
                final Customer customer = CustomerFacade.getCustomerService().findById(table.getValue().toString());
                form.binder.setItemDataSource(new BeanItem<>(getBean(customer)));
            }
        }
    }

    private void saveForm(FieldGroup binder) {
        try {
            binder.commit();
            CustomerFacade.getCustomerService().persist(getEntity(binder));
            getHome();
            Notification.show("Record ADDED!", Notification.Type.TRAY_NOTIFICATION);
            main.content.setSecondComponent(new CustomerMenu(main, "CONTRACTS"));
//            main.content.
        } catch (FieldGroup.CommitException e) {
            Collection<Field<?>> fields = binder.getFields();
            OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
            helper.doValidation();
            Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);
        } catch (MongoException.DuplicateKey | DuplicateKeyException e) {
            Notification.show("Customer Name Already Exists!", Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private void saveEditedForm(FieldGroup binder) {
        try {
            binder.commit();
            CustomerFacade.getCustomerService().merge(getUpdateEntity(binder));
            getHome();
            Notification.show("Record UPDATED!", Notification.Type.TRAY_NOTIFICATION);
            main.content.setSecondComponent(new CustomerMenu(main, "CONTRACTS"));
        } catch (FieldGroup.CommitException e) {
            Collection<Field<?>> fields = binder.getFields();
            OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
            helper.doValidation();
            Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);          
        } catch (MongoException.DuplicateKey | DuplicateKeyException e) {
            Notification.show("Customer Name Already Exists!", Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private void deleteForm(FieldGroup binder) {
//        CustomerFacade.getCustomerService().delete(getEntity(binder));
        Notification.show("Deleting customers is not allowed!", Notification.Type.TRAY_NOTIFICATION);
        getHome();
    }

    private Customer getEntity(FieldGroup binder) {
        //final  Customer cust = new Customer.Builder(binder.getItemDataSource().getItemProperty("name")).

        Set<Invoice> invoices = new HashSet<>();
        Set<Contract> contacts = new HashSet<>();
        Set<Order> orders = new HashSet<>();
        Set<Site> sites = new HashSet<>();

        final CustomerDetailsBean customerBean = ((BeanItem<CustomerDetailsBean>) binder.getItemDataSource()).getBean();

        final ContactPerson contactPerson = new ContactPerson.Builder(customerBean.getFirstName(), customerBean.getLastName())
                .address1(customerBean.getAddress())
                .emailAddress(customerBean.getEmailAddress())
                .mainNumber(customerBean.getMainNumber())
                .otherNumber(customerBean.getOtherNumber())
                .position(customerBean.getPosition())
                //                .id(customerBean.getContactPersonId())
                .build();

        ContactPersonFacade.getContactPersonService().persist(contactPerson);

        final Customer customer = new Customer.Builder(customerBean.getName())
                .id(customerBean.getId())
                .invoices(invoices)
                .orders(orders)
                .sites(sites)
                .contactPerson(contactPerson)
                .contracts(contacts)
                .build();

        return customer;

    }

    private void getHome() {
        main.content.setSecondComponent(new CustomerMenu(main, "LANDING"));
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
    }

    private CustomerDetailsBean getBean(Customer customer) {
        CustomerDetailsBean bean = new CustomerDetailsBean();
        bean.setName(customer.getName());
        bean.setId(customer.getId());
        if (getContactPerson(customer) != null) {
            ContactPerson contactPerson = customer.getContactPerson();
            bean.setContactPersonId(contactPerson.getId());
            bean.setAddress(contactPerson.getAddress1());
            bean.setEmailAddress(contactPerson.getEmailAddress());
            bean.setFirstName(contactPerson.getFirstName());
            bean.setLastName(contactPerson.getLastName());
            bean.setMainNumber(contactPerson.getMainNumber());
            bean.setOtherNumber(contactPerson.getOtherNumber());
            bean.setPosition(contactPerson.getPosition());
        }
        return bean;
    }

    private Customer getUpdateEntity(FieldGroup binder) {
        //final  Customer cust = new Customer.Builder(binder.getItemDataSource().getItemProperty("name")).
        Customer existingCustomer;
        Set<Invoice> invoices = new HashSet<>();
        Set<Contract> contacts = new HashSet<>();
        Set<Order> orders = new HashSet<>();
        Set<Site> sites = new HashSet<>();

        final CustomerDetailsBean customerBean = ((BeanItem<CustomerDetailsBean>) binder.getItemDataSource()).getBean();

        //update 
        existingCustomer = CustomerFacade.getCustomerService().findById(customerBean.getId());
        if (!StringUtils.isEmpty(existingCustomer)) {
            invoices.addAll(existingCustomer.getInvoices());
            contacts.addAll(existingCustomer.getContracts());
            orders.addAll(existingCustomer.getOrders());
            sites.addAll(existingCustomer.getSites());
        }

        final ContactPerson contactPerson = getContactPerson(customerBean);
        final Customer customer = new Customer.Builder(customerBean.getName())
                .id(customerBean.getId())
                .invoices(invoices)
                .orders(orders)
                .sites(sites)
                .contactPerson(contactPerson)
                .contracts(contacts)
                .build();

        return customer;

    }

    private ContactPerson getContactPerson(CustomerDetailsBean customerBean) {
        final ContactPerson contactPerson = new ContactPerson.Builder(customerBean.getFirstName(), customerBean.getLastName())
                .address1(customerBean.getAddress())
                .emailAddress(customerBean.getEmailAddress())
                .mainNumber(customerBean.getMainNumber())
                .otherNumber(customerBean.getOtherNumber())
                .position(customerBean.getPosition())
                .id(customerBean.getContactPersonId())
                .build();

        if (customerBean.getContactPersonId() != null) {
            ContactPersonFacade.getContactPersonService().merge(contactPerson);
            return contactPerson;
        } else {
            if ((!StringUtils.isEmpty(contactPerson.getFirstName())) && (!StringUtils.isEmpty(contactPerson.getFirstName()))) {
                ContactPersonFacade.getContactPersonService().persist(contactPerson);
                return contactPerson;
            } else {
                return null;
            }
        }

    }

    private ContactPerson getContactPerson(Customer customer) {
        if (customer != null) {
            if (customer.getContactPerson() != null) {

                return customer.getContactPerson();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
