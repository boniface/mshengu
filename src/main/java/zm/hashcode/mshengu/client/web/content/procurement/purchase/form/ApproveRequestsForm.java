/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.procurement.purchase.form;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import java.math.BigDecimal;
import java.util.Date;
import zm.hashcode.mshengu.app.facade.procurement.RequestFacade;
import zm.hashcode.mshengu.app.facade.ui.util.SequenceFacade;
import zm.hashcode.mshengu.app.security.GetUserCredentials;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.app.util.SequenceHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.procurement.purchase.PurchaseMenu;
import zm.hashcode.mshengu.client.web.content.procurement.purchase.table.DisplayItemsTable;
import zm.hashcode.mshengu.client.web.content.procurement.purchase.views.ApproveRequestsTab;
import zm.hashcode.mshengu.domain.people.EmployeeDetail;
import zm.hashcode.mshengu.domain.people.Person;
import zm.hashcode.mshengu.domain.procurement.Request;
import zm.hashcode.mshengu.domain.ui.util.CostCentreType;
import zm.hashcode.mshengu.domain.ui.util.Sequence;

/**
 *
 * @author Luckbliss
 */
public class ApproveRequestsForm extends FormLayout implements
        Button.ClickListener {

    private final Button approve = new Button("Approve");
    private final Button disapprove = new Button("Disapprove");
    private final Button back = new Button("Back");
    private final Button delete = new Button("Delete");
    private final ApproveRequestsTab tab;
    private final String requestId;
    private final SequenceHelper helper = new SequenceHelper();
    private MshenguMain main;

    public ApproveRequestsForm(String itemId, ApproveRequestsTab tab, MshenguMain main) {
        requestId = itemId;
        this.tab = tab;
        this.main = main;
        Request request = RequestFacade.getRequestService().findById(itemId);

        GridLayout layout = new GridLayout(3, 15);
        layout.setSizeFull();
        Label requesterInfo = new Label("Requester Information");
        requesterInfo.addStyleName("h4");
        Label person = new Label("Person Requesting: " + request.getPersonName());

        Label account = new Label("Cost Centre: " + returnCostCentre(request.getCostCentreType()));

        layout.addComponent(requesterInfo, 0, 0);
        layout.addComponent(new Label("<br>", ContentMode.HTML), 0, 1);
        layout.addComponent(person, 0, 2);
        layout.addComponent(account, 1, 2);
        layout.addComponent(new Label("<br>", ContentMode.HTML), 0, 3);

        Label vendorInfo = new Label("Vendor Information");
        vendorInfo.addStyleName("h4");
        Label vendor = new Label("Vendor: " + request.getServiceProvider().getName());
        Label address = new Label("Address: " + request.getServiceProvider().getContactAddress1());
        Label phoneNumber = new Label("Phone Number: " + request.getServiceProvider().getContactPersonMainNumber());
        Label deliveryInstructions = new Label("Delivery Instructions: " + request.getDeliveryInstructions());

        layout.addComponent(vendorInfo, 0, 4);
        layout.addComponent(new Label("<br>", ContentMode.HTML), 0, 5);
        layout.addComponent(vendor, 0, 6);
        layout.addComponent(address, 1, 6);
        layout.addComponent(phoneNumber, 2, 6);
        deliveryInstructions.setSizeFull();
        layout.addComponent(new Label("<br>", ContentMode.HTML), 0, 7);
        layout.addComponent(deliveryInstructions, 0, 8, 1, 8);
        layout.addComponent(new Label("<br>", ContentMode.HTML), 0, 9);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();
        approve.setSizeFull();
        buttons.addComponent(approve);
        disapprove.setSizeFull();
        buttons.addComponent(disapprove);
        delete.setSizeFull();
        buttons.addComponent(delete);
        back.setSizeFull();
        buttons.addComponent(back);

        layout.addComponent(buttons, 0, 10, 2, 10);
        layout.addComponent(new Label("<br>", ContentMode.HTML), 0, 11);

        DisplayItemsTable table = new DisplayItemsTable(request.getRequestPurchaseItems(), request.getTotal());
        table.setSizeFull();

        layout.addComponent(table, 0, 12, 2, 12);
        addListeners();
        addComponent(layout);
    }

    private String returnCostCentre(CostCentreType costCentre) {
        if (costCentre != null) {
            return costCentre.getName();
        }
        return null;
    }

    private String getDateofBirth(Date dateofbirth) {
        if (dateofbirth != null) {
            return new DateTimeFormatHelper().getYearMonthDay(dateofbirth);
        }
        return null;
    }

    private void addListeners() {
        //Register Button Listeners
        approve.addClickListener((Button.ClickListener) this);
        disapprove.addClickListener((Button.ClickListener) this);
        back.addClickListener((Button.ClickListener) this);
        delete.addClickListener((Button.ClickListener) this);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        final Button source = event.getButton();
        Request request = RequestFacade.getRequestService().findById(requestId);
        Person user = new GetUserCredentials().getLoggedInPerson();
        
        if (request != null && user != null) {
            if (source == approve) {
                if (getDetail(request.getPerson()) != null) {
                    if (!getEmail(getDetail(request.getPerson())).equals(user.getUsername())) {
                        if (request.getTotal().compareTo(new BigDecimal("3000.00")) <= 0) {
                            if (user.getUsername().equals("sydney@mshengutoilethire.co.za") || user.getUsername().equals("haroldmanus@iafrica.com") || user.getUsername().equals("hiltonjc@iafrica.com")) {
                                approve(request, user.getFirstname() + " " + user.getLastname());
                            } else {
                                Notification.show("User not allowed to approve!", Notification.Type.TRAY_NOTIFICATION);
                            }
                        } else if (request.getTotal().compareTo(new BigDecimal("3000.00")) > 0) {
                            if (("sydney@mshengutoilethire.co.za".equals("haroldmanus@iafrica.com") || user.getUsername().equals("hiltonjc@iafrica.com"))) {
                                approve(request, user.getFirstname() + " " + user.getLastname());
                            } else {
                                Notification.show("User not allowed to approve more than R3000!", Notification.Type.TRAY_NOTIFICATION);
                            }
                        }
                    } else {
                        Notification.show("You cannot approve your own request!", Notification.Type.TRAY_NOTIFICATION);
                    }
                } else {
                    Notification.show("This request was does not have the information of the person who created it hence cannot be approved!", Notification.Type.TRAY_NOTIFICATION);
                }
            } else if (source == disapprove) {
                if (getDetail(request.getPerson()) != null || getEmail(getDetail(request.getPerson())).equals("")) {
                    if (!getEmail(getDetail(request.getPerson())).equals(user.getUsername())) {
                        if (request.getTotal().compareTo(new BigDecimal("3000.00")) <= 0) {
                            if (user.getUsername().equals("sydney@mshengutoilethire.co.za") || user.getUsername().equals("haroldmanus@iafrica.com") || user.getUsername().equals("hiltonjc@iafrica.com")) {
                                disapprove();
                            } else {
                                Notification.show("User not allowed to disapprove!", Notification.Type.TRAY_NOTIFICATION);
                            }
                        } else if (request.getTotal().compareTo(new BigDecimal("3000.00")) > 0) {
                            if ((user.getUsername().equals("haroldmanus@iafrica.com") || user.getUsername().equals("hiltonjc@iafrica.com"))) {
                                disapprove();
                            } else {
                                Notification.show("User not allowed to disapprove  more than R3000!", Notification.Type.TRAY_NOTIFICATION);
                            }
                        }
                    } else {
                        Notification.show("You cannot disapprove your own request!", Notification.Type.TRAY_NOTIFICATION);
                    }
                } else {
                    Notification.show("This request was does not have the information of the person who created it hence cannot be disapproved!", Notification.Type.TRAY_NOTIFICATION);
                }

            } else if (source == back) {
                main.content.setSecondComponent(new PurchaseMenu(main, "APPROVE_REQUESTS"));
            } else if (source == delete) {
                if (getDetail(request.getPerson()) != null) {
                    if (!getEmail(getDetail(request.getPerson())).equals(user.getUsername())) {
                        if (request.getTotal().compareTo(new BigDecimal("3000.00")) <= 0) {
                            if (user.getUsername().equals("sydney@mshengutoilethire.co.za") || user.getUsername().equals("haroldmanus@iafrica.com") || user.getUsername().equals("hiltonjc@iafrica.com")) {
                                RequestFacade.getRequestService().delete(request);
                                main.content.setSecondComponent(new PurchaseMenu(main, "APPROVE_REQUESTS"));
                            } else {
                                Notification.show("User not allowed to disapprove!", Notification.Type.TRAY_NOTIFICATION);
                            }
                        } else if (request.getTotal().compareTo(new BigDecimal("3000.00")) > 0) {
                            if ((user.getUsername().equals("haroldmanus@iafrica.com") || user.getUsername().equals("hiltonjc@iafrica.com"))) {
                                disapprove();
                            } else {
                                Notification.show("User not allowed to disapprove  more than R3000!", Notification.Type.TRAY_NOTIFICATION);
                            }
                        }
                    } else {
                        Notification.show("You cannot disapprove your own request!", Notification.Type.TRAY_NOTIFICATION);
                    }
                } else {
                    Notification.show("This request was does not have the information of the person who created it hence cannot be deleted!", Notification.Type.TRAY_NOTIFICATION);
                }
            } else {
                Notification.show("Request does not exist!", Notification.Type.TRAY_NOTIFICATION);
            }
        }
    }

    private void approve(Request request, String user) {
        Sequence sequence = SequenceFacade.getSequenceListService().findByName("PURCHASE_REQUEST");
        Request newRequest = new Request.Builder(request.getPerson())
                .request(request)
                .approver(user)
                .approvalStatus(true)
                .orderNumber(helper.getRefNumber(sequence))
                .build();
        RequestFacade.getRequestService().merge(newRequest);
        main.content.setSecondComponent(new PurchaseMenu(main, "APPROVE_REQUESTS"));
    }

    private void disapprove() {
        tab.removeAllComponents();
        DisapproveRequestsForm form = new DisapproveRequestsForm(RequestFacade.getRequestService().findById(requestId), tab.main);
        tab.addComponent(form);
    }

    private EmployeeDetail getDetail(Person person) {
        if (person != null) {
            return person.getEmployeeDetails();
        }
        return null;
    }

    private String getEmail(EmployeeDetail detail) {
        if (detail != null) {
            if(detail.getEmail() == null){
                return "";
            }
            return detail.getEmail();
        }
        return "";
    }
}
