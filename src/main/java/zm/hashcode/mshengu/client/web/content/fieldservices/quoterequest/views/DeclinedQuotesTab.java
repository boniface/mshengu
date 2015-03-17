/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.views;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Date;
import zm.hashcode.mshengu.app.facade.external.IncomingRFQFacade;
import zm.hashcode.mshengu.app.facade.external.MailNotificationsFacade;
import zm.hashcode.mshengu.app.util.SequenceHelper;
import zm.hashcode.mshengu.app.util.validation.OnSubmitValidationHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.QuoteRequestsMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.forms.QuoteRequestsForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.models.QuoteRequestBean;
import zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.tables.DeclinedQuotesTable;
import zm.hashcode.mshengu.domain.external.IncomingRFQ;
import zm.hashcode.mshengu.domain.external.MailNotifications;
import zm.hashcode.mshengu.domain.ui.util.Sequence;

/**
 *
 * @author Luckbliss
 */
public class DeclinedQuotesTab extends VerticalLayout implements
        Button.ClickListener, Property.ValueChangeListener {

    private final MshenguMain main;
    private final QuoteRequestsForm form;
    private final DeclinedQuotesTable table;
    private final SequenceHelper sequenceHelper = new SequenceHelper();
    private boolean acceptedQuote;
    private Date quoteAcceptenceDate;

    public DeclinedQuotesTab(MshenguMain app) {
        main = app;
        form = new QuoteRequestsForm();
        
        table = new DeclinedQuotesTable(main, this);
        table.setCellStyleGenerator(new Table.CellStyleGenerator() {

            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                if (propertyId == null) {
                    // Styling for row
                    Item item = table.getItem(itemId);
                    String sentStatus = (String) item.getItemProperty("Sent Status").getValue();
                    if (sentStatus.equals("not sent")) {
                        return "highlight-red";
                    } else {
                        return null;
                    }
                } else {
                    // styling for column propertyId
                    return null;
                }

            }
        });
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
        }else if(source == form.accepted){            
            acceptedQuote = true;
            quoteAcceptenceDate = new Date();
            saveEditedForm(form.binder);
        }
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        final Property property = event.getProperty();
        if (property == table) {
            final IncomingRFQ rfg = IncomingRFQFacade.getIncomingRFQService().findById(table.getValue().toString());
            form.binder.setItemDataSource(new BeanItem<>(getBean(rfg)));
            setReadFormProperties();
            if(rfg.getStatus().equals("sent")){
                form.accepted.setVisible(true);
            }else{
                form.accepted.setVisible(false);
            }
        }
    }

    private void saveForm(FieldGroup binder) {
        try {
            binder.commit();
            IncomingRFQFacade.getIncomingRFQService().persist(getEntity(binder));
            getHome();
            Notification.show("Record ADDED!", Notification.Type.TRAY_NOTIFICATION);
        } catch (FieldGroup.CommitException e) {
            Collection<Field<?>> fields = binder.getFields();
            OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
            helper.doValidation();
            Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    private void saveEditedForm(FieldGroup binder) {
        try {
            binder.commit();
            IncomingRFQFacade.getIncomingRFQService().merge(getUpdatedEntity(binder));
            getHome();
            Notification.show("Record UPDATED!", Notification.Type.TRAY_NOTIFICATION);
        } catch (FieldGroup.CommitException e) {
            Collection<Field<?>> fields = binder.getFields();
            OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
            helper.doValidation();
            Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    private void deleteForm(FieldGroup binder) {
        IncomingRFQFacade.getIncomingRFQService().delete(getUpdatedEntity(binder));
        getHome();
    }

    private IncomingRFQ getEntity(FieldGroup binder) {

        final MailNotifications mailNotifications = MailNotificationsFacade.getMailNotificationsService().findByName("REQUEST_TOILET_HIRE_NOTIFCATION");
        final QuoteRequestBean bean = ((BeanItem<QuoteRequestBean>) binder.getItemDataSource()).getBean();
//sequenceHelper
        IncomingRFQ incomingRFQ = new IncomingRFQ.Builder(bean.getActionDate())
                .refNumber(sequenceHelper.getRefNumber(getMailNotficationSequence(mailNotifications)))
                .billingAddress(bean.getBillingAddress())
                .collectionDate(bean.getCollectionDate())
                .comment(bean.getComment())
                .companyName(bean.getCompanyNameNonRequired())
                .contactPersonFirstname(bean.getContactPersonFirstname())
                .contactPersonLastname(bean.getContactPersonLastname())
                .faxNumber(bean.getFaxNumber())
                .contactNumber(bean.getContactNumber())
                .daysRental(bean.getDaysRental())
                .deliveryAddress(bean.getDeliveryAddress())
                .deliveryDate(bean.getDeliveryDate())
                .email(bean.getEmail())
                .eventDate(bean.getEventDate())
                .eventName(bean.getEventName())
                .eventType(bean.getEventType())
                .sunday(bean.isServiceFrequencySun())
                .saturday(bean.isServiceFrequencySat())
                .friday(bean.isServiceFrequencyFri())
                .thursday(bean.isServiceFrequencyThur())
                .wednesday(bean.isServiceFrequencyWed())
                .tuesday(bean.isServiceFrequencyTue())
                .monday(bean.isServiceFrequencyMon())
                .numberOfJanitors(bean.getNumberOfJanitors())
                .numberOfToiletRolls(bean.getNumberOfToiletRolls())
                .quantityRequired1(bean.getQuantityRequired1())
                .quantityRequired2(bean.getQuantityRequired2())
                .quantityRequired3(bean.getQuantityRequired3())
                .vatNumber(bean.getVatRegistrationNumberUnrequired())
                .status("not sent")
                .acceptedStatus(acceptedQuote)
                .quoteAcceptenceDate(quoteAcceptenceDate)
                .telephoneNumber(bean.getTelephoneNumberNonRequired())
                .toiletsRequired1(bean.getToiletsRequired1())
                .toiletsRequired2(bean.getToiletsRequired2())
                .toiletsRequired3(bean.getToiletsRequired3())
                .build();
        return incomingRFQ;
    }

    private IncomingRFQ getUpdatedEntity(FieldGroup binder) {

        final QuoteRequestBean bean = ((BeanItem<QuoteRequestBean>) binder.getItemDataSource()).getBean();

        final IncomingRFQ rFQ = IncomingRFQFacade.getIncomingRFQService().findById(bean.getId());
        final IncomingRFQ incomingRFQ = new IncomingRFQ.Builder(bean.getActionDate())
                .incomingRFQ(rFQ)
                .billingAddress(bean.getBillingAddress())
                .collectionDate(bean.getCollectionDate())
                .comment(bean.getComment())
                .companyName(bean.getCompanyNameNonRequired())
                .contactPersonFirstname(bean.getContactPersonFirstname())
                .contactPersonLastname(bean.getContactPersonLastname())
                .faxNumber(bean.getFaxNumber())
                .contactNumber(bean.getContactNumber())
                .daysRental(bean.getDaysRental())
                .deliveryAddress(bean.getDeliveryAddress())
                .deliveryDate(bean.getDeliveryDate())
                .email(bean.getEmail())
                .eventDate(bean.getEventDate())
                .eventName(bean.getEventName())
                .eventType(bean.getEventType())
                .sunday(bean.isServiceFrequencySun())
                .saturday(bean.isServiceFrequencySat())
                .friday(bean.isServiceFrequencyFri())
                .thursday(bean.isServiceFrequencyThur())
                .wednesday(bean.isServiceFrequencyWed())
                .tuesday(bean.isServiceFrequencyTue())
                .monday(bean.isServiceFrequencyMon())
                .numberOfJanitors(bean.getNumberOfJanitors())
                .numberOfToiletRolls(bean.getNumberOfToiletRolls())
                .quantityRequired1(bean.getQuantityRequired1())
                .quantityRequired2(bean.getQuantityRequired2())
                .quantityRequired3(bean.getQuantityRequired3())
                .vatNumber(bean.getVatRegistrationNumberUnrequired())
                .telephoneNumber(bean.getTelephoneNumberNonRequired())
                .toiletsRequired1(bean.getToiletsRequired1())
                .toiletsRequired2(bean.getToiletsRequired2())
                .toiletsRequired3(bean.getToiletsRequired3())
                .acceptedStatus(acceptedQuote)
                .quoteAcceptenceDate(quoteAcceptenceDate)
                .build();

        return incomingRFQ;
    }

    private void getHome() {
        main.content.setSecondComponent(new QuoteRequestsMenu(main, "LANDING"));
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
        form.accepted.addClickListener((Button.ClickListener) this);
        form.declined.addClickListener((Button.ClickListener) this);
        //Register Table Listerners
        table.addValueChangeListener((Property.ValueChangeListener) this);
    }

    private QuoteRequestBean getBean(IncomingRFQ rfg) {
        QuoteRequestBean bean = new QuoteRequestBean();
        bean.setId(rfg.getId());
        bean.setActionDate(rfg.getDateOfAction());
        bean.setBillingAddress(rfg.getBillingAddress());
        bean.setCollectionDate(rfg.getCollectionDate());
        bean.setComment(rfg.getComment());
        bean.setCompanyNameNonRequired(rfg.getCompanyName());
        bean.setContactNumber(rfg.getContactNumber());
        bean.setContactPersonFirstname(rfg.getContactPersonFirstname());
        bean.setContactPersonLastname(rfg.getContactPersonLastname());
        bean.setDaysRental(rfg.getDaysRental());
        bean.setDeliveryAddress(rfg.getDeliveryAddress());
        bean.setDeliveryDate(rfg.getDeliveryDate());
        bean.setEmail(rfg.getEmail());
        bean.setEventDate(rfg.getEventDate());
        bean.setEventName(rfg.getEventName());
        bean.setEventType(rfg.getEventType());
        bean.setNumberOfJanitors(rfg.getNumberOfJanitors());
        bean.setNumberOfToiletRolls(rfg.getNumberOfToiletRolls());
        bean.setQuantityRequired1(rfg.getQuantityRequired1());
        bean.setQuantityRequired2(rfg.getQuantityRequired2());
        bean.setQuantityRequired3(rfg.getQuantityRequired3());
        bean.setRefNumber(rfg.getRefNumber());
        bean.setServiceFrequencySun(rfg.isSunday());
        bean.setServiceFrequencySat(rfg.isSaturday());
        bean.setServiceFrequencyFri(rfg.isFriday());
        bean.setServiceFrequencyThur(rfg.isThursday());
        bean.setServiceFrequencyWed(rfg.isWednesday());
        bean.setServiceFrequencyTue(rfg.isTuesday());
        bean.setServiceFrequencyMon(rfg.isMonday());
        bean.setTelephoneNumberNonRequired(rfg.getTelephoneNumber());
        bean.setToiletsRequired1(rfg.getToiletsRequired1());
        bean.setToiletsRequired2(rfg.getToiletsRequired2());
        bean.setToiletsRequired3(rfg.getToiletsRequired3());
        bean.setVatRegistrationNumberUnrequired(rfg.getVatNumber());
        bean.setContactNumber(rfg.getContactNumber());
        bean.setFaxNumber(rfg.getFaxNumber());

        return bean;
    }

    private Sequence getMailNotficationSequence(MailNotifications mailNotifications) {
        if (mailNotifications != null) {
            return mailNotifications.getSequence();
        } else {
            return null;
        }
    }
}
