/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.domain.external;

import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import zm.hashcode.mshengu.domain.incident.UserAction;

/**
 *
 * @author Ferox
 */
@Document
public class IncomingRFQ implements Serializable, Comparable<IncomingRFQ> {

    @Id
    private String id;
    private Date dateOfAction;
    private String refNumber;
    private String companyName;
    private String contactPersonFirstname;
    private String contactPersonLastname;
    private String telephoneNumber;
    private String contactNumber;
    private String email;
    private String billingAddress;
    private String deliveryAddress;
    private String vatNumber;
    private String eventName;
    private Date eventDate;
    private String eventType;
    private String toiletsRequired1;
    private String toiletsRequired2;
    private String toiletsRequired3;
    private String toiletsRequired4;
    private int quantityRequired1;
    private int quantityRequired2;
    private int quantityRequired3;
    private int quantityRequired4;
    private int numberOfJanitors;
    private int numberOfToiletRolls;
    private Date deliveryDate;
    private Date collectionDate;
    private int daysRental;
    private String comment;
    private boolean closed;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
    private String faxNumber;
    private String status;
    private Date quoteAcceptenceDate;
    private Boolean acceptedStatus;
    @DBRef(lazy = true)
    private MailNotifications mailNotifications;
    @DBRef(lazy = true)
    private Set<UserAction> userAction = new HashSet<>();

    public IncomingRFQ(Builder builder) {
        this.id = builder.id;
        this.dateOfAction = builder.dateOfAction;
        this.refNumber = builder.refNumber;
        this.companyName = builder.companyName;
        this.contactPersonFirstname = builder.contactPersonFirstname;
        this.contactPersonLastname = builder.contactPersonLastname;
        this.telephoneNumber = builder.telephoneNumber;
        this.contactNumber = builder.contactNumber;
        this.email = builder.email;
        this.billingAddress = builder.billingAddress;
        this.deliveryAddress = builder.deliveryAddress;
        this.vatNumber = builder.vatNumber;
        this.eventName = builder.eventName;
        this.eventDate = builder.eventDate;
        this.eventType = builder.eventType;
        this.faxNumber = builder.faxNumber;

        this.toiletsRequired1 = builder.toiletsRequired1;
        this.toiletsRequired2 = builder.toiletsRequired2;
        this.toiletsRequired3 = builder.toiletsRequired3;
        this.toiletsRequired4 = builder.toiletsRequired4;
        this.quantityRequired1 = builder.quantityRequired1;
        this.quantityRequired2 = builder.quantityRequired2;
        this.quantityRequired3 = builder.quantityRequired3;
        this.quantityRequired4 = builder.quantityRequired4;
        this.numberOfJanitors = builder.numberOfJanitors;
        this.numberOfToiletRolls = builder.numberOfToiletRolls;
        this.deliveryDate = builder.deliveryDate;
        this.collectionDate = builder.collectionDate;
        this.daysRental = builder.daysRental;
        this.comment = builder.comment;
        this.closed = builder.closed;
        this.monday = builder.monday;
        this.tuesday = builder.tuesday;
        this.wednesday = builder.wednesday;
        this.thursday = builder.thursday;
        this.friday = builder.friday;
        this.saturday = builder.saturday;
        this.sunday = builder.sunday;
        this.mailNotifications = builder.mailNotifications;
        this.userAction = builder.userAction;
        this.status = builder.status;
        this.acceptedStatus = builder.acceptedStatus;
        this.quoteAcceptenceDate = builder.quoteAcceptenceDate;
    }

    private IncomingRFQ() {
    }

    @Override
    public int compareTo(IncomingRFQ o) {
        return -1 * getDateOfAction().compareTo(o.getDateOfAction());
    }

    public Boolean getAcceptedStatus() {
        return acceptedStatus;
    }

    public void setAcceptedStatus(Boolean acceptedStatus) {
        this.acceptedStatus = acceptedStatus;
    }

    public Date getQuoteAcceptenceDate() {
        return quoteAcceptenceDate;
    }

    public void setQuoteAcceptenceDate(Date quoteAcceptenceDate) {
        this.quoteAcceptenceDate = quoteAcceptenceDate;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the dateOfAction
     */
    public Date getDateOfAction() {
        return dateOfAction;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @return the contactPerson
     */
    public String getContactPersonFirstname() {
        return contactPersonFirstname;
    }

    public String getContactPersonLastname() {
        return contactPersonLastname;
    }

    public String getStatus() {
        return status;
    }

    /**
     * @return the telephoneNumber
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * @return the contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the billingAddress
     */
    public String getBillingAddress() {
        return billingAddress;
    }

    /**
     * @return the deliveryAddress
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * @return the vatNumber
     */
    public String getVatNumber() {
        return vatNumber;
    }

    /**
     * @return the eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @return the eventDate
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * @return the eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * @return the toiletsRequired1
     */
    public String getToiletsRequired1() {
        return toiletsRequired1;
    }

    /**
     * @return the toiletsRequired2
     */
    public String getToiletsRequired2() {
        return toiletsRequired2;
    }

    /**
     * @return the toiletsRequired3
     */
    public String getToiletsRequired3() {
        return toiletsRequired3;
    }

    /**
     * @return the toiletsRequired4
     */
    public String getToiletsRequired4() {
        return toiletsRequired4;
    }

    /**
     * @return the quantityRequired1
     */
    public int getQuantityRequired1() {
        return quantityRequired1;
    }

    /**
     * @return the quantityRequired2
     */
    public int getQuantityRequired2() {
        return quantityRequired2;
    }

    /**
     * @return the quantityRequired3
     */
    public int getQuantityRequired3() {
        return quantityRequired3;
    }

    /**
     * @return the quantityRequired4
     */
    public int getQuantityRequired4() {
        return quantityRequired4;
    }

    /**
     * @return the numberOfJanitors
     */
    public int getNumberOfJanitors() {
        return numberOfJanitors;
    }

    /**
     * @return the numberOfToiletRolls
     */
    public int getNumberOfToiletRolls() {
        return numberOfToiletRolls;
    }

    /**
     * @return the deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @return the collectionDate
     */
    public Date getCollectionDate() {
        return collectionDate;
    }

    /**
     * @return the daysRental
     */
    public int getDaysRental() {
        return daysRental;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @return the monday
     */
    public boolean isMonday() {
        return monday;
    }

    /**
     * @return the tuesday
     */
    public boolean isTuesday() {
        return tuesday;
    }

    /**
     * @return the wednesday
     */
    public boolean isWednesday() {
        return wednesday;
    }

    /**
     * @return the thursday
     */
    public boolean isThursday() {
        return thursday;
    }

    /**
     * @return the friday
     */
    public boolean isFriday() {
        return friday;
    }

    /**
     * @return the saturday
     */
    public boolean isSaturday() {
        return saturday;
    }

    /**
     * @return the sunday
     */
    public boolean isSunday() {
        return sunday;
    }

    /**
     * @return the mailNotifications
     */
    public MailNotifications getMailNotifications() {
        return mailNotifications;
    }

    /**
     * @return the refNumber
     */
    public String getRefNumber() {
        return refNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * @return the userAction
     */
    private List<UserAction> sortUserActions() {
        List<UserAction> userActionList = new ArrayList<>();
        userActionList.addAll(userAction);
        Collections.sort(userActionList);
        return userActionList;
    }

    public Set<UserAction> getUserAction() {
        if (userAction != null) {
            return ImmutableSet.copyOf(sortUserActions());
        } else {
            return ImmutableSet.copyOf(userAction);
        }
    }

    public UserAction getLastUserAction() {
        if (userAction.size() > 0) {
            return sortUserActions().get(0);
        } else {
            return null;
        }
    }

    public static class Builder {

        private String id;
        private final Date dateOfAction;
        private String refNumber;
        private String companyName;
        private String contactPersonFirstname;
        private String contactPersonLastname;
        private String telephoneNumber;
        private String contactNumber;
        private String email;
        private String billingAddress;
        private String deliveryAddress;
        private String vatNumber;
        private String eventName;
        private Date eventDate;
        private String eventType;
        private String toiletsRequired;
        private String quantityRequired;
        private String toiletsRequired1;
        private String toiletsRequired2;
        private String toiletsRequired3;
        private String toiletsRequired4;
        private int quantityRequired1;
        private int quantityRequired2;
        private int quantityRequired3;
        private int quantityRequired4;
        private int numberOfJanitors;
        private int numberOfToiletRolls;
        private Date deliveryDate;
        private Date collectionDate;
        private int daysRental;
        private String comment;
        private boolean closed;
        private boolean monday;
        private boolean tuesday;
        private boolean wednesday;
        private boolean thursday;
        private boolean friday;
        private boolean saturday;
        private boolean sunday;
        private String faxNumber;
        private MailNotifications mailNotifications;
        private Set<UserAction> userAction = new HashSet<>();
        private String status;
        private Boolean acceptedStatus;
        private Date quoteAcceptenceDate;

        public Builder incomingRFQ(IncomingRFQ incomingRFQ) {
            this.id = incomingRFQ.getId();
//        this.dateOfAction = incomingRFQ.dateOfAction;
            this.companyName = incomingRFQ.getCompanyName();
            this.contactPersonFirstname = incomingRFQ.getContactPersonFirstname();
            this.contactPersonLastname = incomingRFQ.getContactPersonLastname();
            this.telephoneNumber = incomingRFQ.getTelephoneNumber();
            this.contactNumber = incomingRFQ.getContactNumber();
            this.email = incomingRFQ.getEmail();
            this.billingAddress = incomingRFQ.getBillingAddress();
            this.deliveryAddress = incomingRFQ.getDeliveryAddress();
            this.vatNumber = incomingRFQ.getVatNumber();
            this.eventName = incomingRFQ.getEventName();
            this.eventDate = incomingRFQ.getEventDate();
            this.eventType = incomingRFQ.getEventType();
            this.faxNumber = incomingRFQ.getFaxNumber();

            this.toiletsRequired1 = incomingRFQ.getToiletsRequired1();
            this.toiletsRequired2 = incomingRFQ.getToiletsRequired2();
            this.toiletsRequired3 = incomingRFQ.getToiletsRequired3();
            this.toiletsRequired4 = incomingRFQ.getToiletsRequired4();
            this.quantityRequired1 = incomingRFQ.getQuantityRequired1();
            this.quantityRequired2 = incomingRFQ.getQuantityRequired2();
            this.quantityRequired3 = incomingRFQ.getQuantityRequired3();
            this.quantityRequired4 = incomingRFQ.getQuantityRequired4();
            this.numberOfJanitors = incomingRFQ.getNumberOfJanitors();
            this.numberOfToiletRolls = incomingRFQ.getNumberOfToiletRolls();
            this.deliveryDate = incomingRFQ.getDeliveryDate();
            this.collectionDate = incomingRFQ.getCollectionDate();
            this.daysRental = incomingRFQ.getDaysRental();
            this.comment = incomingRFQ.getComment();
            this.closed = incomingRFQ.isClosed();
            this.monday = incomingRFQ.isMonday();
            this.tuesday = incomingRFQ.isTuesday();
            this.wednesday = incomingRFQ.isWednesday();
            this.thursday = incomingRFQ.isThursday();
            this.friday = incomingRFQ.isFriday();
            this.saturday = incomingRFQ.isSaturday();
            this.sunday = incomingRFQ.isSunday();
            this.mailNotifications = incomingRFQ.getMailNotifications();
            this.userAction = incomingRFQ.getUserAction();
            this.refNumber = incomingRFQ.getRefNumber();
            this.status = incomingRFQ.getStatus();
            this.acceptedStatus = incomingRFQ.getAcceptedStatus();
            this.quoteAcceptenceDate = incomingRFQ.getQuoteAcceptenceDate();
            return this;
        }

        public Builder(Date value) {
            this.dateOfAction = value;
        }

        public Builder acceptedStatus(Boolean acceptedStatus) {
            this.acceptedStatus = acceptedStatus;
            return this;
        }

        public Builder quoteAcceptenceDate(Date quoteAcceptenceDate) {
            this.quoteAcceptenceDate = quoteAcceptenceDate;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder id(String value) {
            this.id = value;
            return this;
        }

        public Builder contactNumber(String value) {
            this.contactNumber = value;
            return this;
        }

        public Builder faxNumber(String value) {
            this.faxNumber = value;
            return this;
        }

        public Builder refNumber(String value) {
            this.refNumber = value;
            return this;
        }

        public Builder companyName(String value) {
            this.companyName = value;
            return this;
        }

        public Builder contactPersonFirstname(String value) {
            this.contactPersonFirstname = value;
            return this;
        }

        public Builder contactPersonLastname(String value) {
            this.contactPersonLastname = value;
            return this;
        }

        public Builder telephoneNumber(String value) {
            this.telephoneNumber = value;
            return this;
        }

        public Builder email(String value) {
            this.email = value;
            return this;
        }

        public Builder billingAddress(String value) {
            this.billingAddress = value;
            return this;
        }

        public Builder deliveryAddress(String value) {
            this.deliveryAddress = value;
            return this;
        }

        public Builder vatNumber(String value) {
            this.vatNumber = value;
            return this;
        }

        public Builder eventName(String value) {
            this.eventName = value;
            return this;
        }

        public Builder eventDate(Date value) {
            this.eventDate = value;
            return this;
        }

        public Builder eventType(String value) {
            this.eventType = value;
            return this;
        }

        public Builder toiletsRequired1(String value) {
            this.toiletsRequired1 = value;
            return this;
        }

        public Builder toiletsRequired2(String value) {
            this.toiletsRequired2 = value;
            return this;
        }

        public Builder toiletsRequired3(String value) {
            this.toiletsRequired3 = value;
            return this;
        }

        public Builder toiletsRequired4(String value) {
            this.toiletsRequired4 = value;
            return this;
        }

        public Builder quantityRequired1(int value) {
            this.quantityRequired1 = value;
            return this;
        }

        public Builder quantityRequired2(int value) {
            this.quantityRequired2 = value;
            return this;
        }

        public Builder quantityRequired3(int value) {
            this.quantityRequired3 = value;
            return this;
        }

        public Builder quantityRequired4(int value) {
            this.quantityRequired4 = value;
            return this;
        }

        public Builder numberOfJanitors(int value) {
            this.numberOfJanitors = value;
            return this;
        }

        public Builder numberOfToiletRolls(int value) {
            this.numberOfToiletRolls = value;
            return this;
        }

        public Builder deliveryDate(Date value) {
            this.deliveryDate = value;
            return this;
        }

        public Builder collectionDate(Date value) {
            this.collectionDate = value;
            return this;
        }

        public Builder daysRental(int value) {
            this.daysRental = value;
            return this;
        }

        public Builder comment(String value) {
            this.comment = value;
            return this;
        }

        public Builder closed(boolean value) {
            this.closed = value;
            return this;
        }

        public Builder monday(boolean value) {
            this.monday = value;
            return this;
        }

        public Builder tuesday(boolean value) {
            this.tuesday = value;
            return this;
        }

        public Builder wednesday(boolean value) {
            this.wednesday = value;
            return this;
        }

        public Builder thursday(boolean value) {
            this.thursday = value;
            return this;
        }

        public Builder friday(boolean value) {
            this.friday = value;
            return this;
        }

        public Builder saturday(boolean value) {
            this.saturday = value;
            return this;
        }

        public Builder sunday(boolean value) {
            this.sunday = value;
            return this;
        }

        public Builder mailNotifications(MailNotifications value) {
            this.mailNotifications = value;
            return this;
        }

        public Builder userAction(Set<UserAction> value) {
            this.userAction = value;
            return this;
        }

        public IncomingRFQ build() {
            return new IncomingRFQ(this);
        }
    }

    private boolean isNullObject(Object object) {
        if (object == null) {
            return true;
        } else {
            return false;
        }
    }

    public String getLastUserActionStatusId() {
        if (!isNullObject(getLastUserAction())) {
            UserAction lastUserActionStatus = getLastUserAction();
            return lastUserActionStatus.getId();
        } else {
            return null;
        }
    }

    public String getLastUserActionStatusName() {
        if (!isNullObject(getLastUserAction())) {
            UserAction lastUserActionStatus = getLastUserAction();
            return lastUserActionStatus.getUserActionStatusName();
        } else {
            return null;
        }
    }

    public String getMailNotificationsId() {
        if (!isNullObject(mailNotifications)) {
            return mailNotifications.getId();
        } else {
            return null;
        }
    }

    public String getMailNotificationsName() {
        if (!isNullObject(mailNotifications)) {
            return mailNotifications.getName();
        } else {
            return null;
        }
    }
}
