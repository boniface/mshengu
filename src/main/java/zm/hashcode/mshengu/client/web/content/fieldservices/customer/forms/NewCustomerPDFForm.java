/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.forms;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import zm.hashcode.mshengu.app.util.SendEmailHelper;
import zm.hashcode.mshengu.app.util.UIComponentHelper;
import zm.hashcode.mshengu.app.util.validation.OnSubmitValidationHelper;
import zm.hashcode.mshengu.app.util.validation.UIValidatorHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.CustomerMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.models.NewCustomerBean;
import zm.hashcode.mshengu.client.web.content.fieldservices.customer.models.NewCustomerControl;

/**
 *
 * @author Luckbliss
 */
public class NewCustomerPDFForm extends FormLayout {

    public Button back = new Button("Cancel");
    public Button email = new Button("E-Mail Form");
    private MshenguMain main;
    private static Embedded embedded = null;
    private static StreamResource streamResource = null;
    private static ByteArrayInputStream byteArrInputStream = null;
    private NewCustomerControl control = new NewCustomerControl();
    private SendEmailHelper emailHelper = new SendEmailHelper();
    public UIComponentHelper UIComponent = new UIComponentHelper();
    public final NewCustomerBean bean = new NewCustomerBean();
    public final BeanItem<NewCustomerBean> item = new BeanItem<>(bean);
    public final FieldGroup binder = new FieldGroup(item);
    private TextField sendemail = new TextField();
    public Label errorMessage;

    public NewCustomerPDFForm(final MshenguMain main) {

        sendemail = UIComponent.getTextField("Email Address:", "email", NewCustomerBean.class, binder);
        sendemail.addValidator(UIValidatorHelper.emailValidator());
        sendemail = UIValidatorHelper.setRequiredTextField(sendemail, "Email Address");

        this.main = main;
        setSizeFull();
        streamResource = new StreamResource(createStreamResource(), "New Customer.pdf");
        embedded = new Embedded();
        embedded.setType(Embedded.TYPE_BROWSER);
        embedded.setHeight("500");
        embedded.setWidth("1000");
        embedded.setMimeType("application/pdf");
        embedded.setSource(streamResource);
//        embedded.setImmediate(true);

        GridLayout layout = new GridLayout(3, 4);
        errorMessage = UIComponent.getErrorLabel();

        layout.addComponent(errorMessage, 0, 0, 1, 0);
        addComponent(new Label("<br>", ContentMode.HTML));
        layout.addComponent(sendemail);
        addComponent(embedded);
        addComponent(new Label("<br>", ContentMode.HTML));
        addComponent(layout);
        addComponent(new Label("<br>", ContentMode.HTML));

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();
        back.setSizeFull();
        email.setSizeFull();
        buttons.addComponent(back);
        buttons.addComponent(email);

        addComponent(buttons);

        back.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getHome();
            }
        });

        email.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    binder.commit();
                    NewCustomerBean bean = ((BeanItem<NewCustomerBean>) binder.getItemDataSource()).getBean();
                    DataSource sendsource = new ByteArrayDataSource(control.processPDF().toByteArray(), "application/pdf");
                    emailHelper.sendToNewCustomer(sendsource, bean.getEmail(), "Mshengu - New Customer Form", "Mshengu - New Customer Form");
                    
                    Notification.show("Email sent to " + bean.getEmail(), Notification.Type.TRAY_NOTIFICATION);
                    getHome();
                } catch (FieldGroup.CommitException ex) {
                    Collection<Field<?>> fields = binder.getFields();
                    OnSubmitValidationHelper validationHelper = new OnSubmitValidationHelper(fields, errorMessage);
                    validationHelper.doValidation();
                    Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);
                }
            }
        });
    }

    private void getHome() {
        main.content.setSecondComponent(new CustomerMenu(main, "LANDING"));
    }

    private StreamResource.StreamSource createStreamResource() {

        StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                byte byteArray[] = control.processPDF().toByteArray();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(control.processPDF(request).toByteArray());
                byteArrInputStream = byteArrayInputStream;
                return byteArrayInputStream;
//                InputStream fis = new ByteArrayInputStream(os.toByteArray());
            }
        };
        return streamSource;
    }
}
