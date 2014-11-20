/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.domain.procurement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import zm.hashcode.mshengu.domain.fleet.Truck;
import zm.hashcode.mshengu.domain.people.Person;
import zm.hashcode.mshengu.domain.serviceprovider.ServiceProvider;
import zm.hashcode.mshengu.domain.ui.util.CostCentreCategoryType;
import zm.hashcode.mshengu.domain.ui.util.CostCentreType;
import zm.hashcode.mshengu.domain.ui.util.ItemCategoryType;

/**
 *
 * @author Luckbliss
 */
@Document
public final class Request implements Serializable, Comparable<Request> {

    @Id
    private String id;
    @DBRef
    private Person person;
    private String orderNumber;
    @DBRef(lazy = true)
    private CostCentreType costCentreType;
    @DBRef(lazy = true)
    private CostCentreCategoryType categoryType;
    @DBRef(lazy = true)
    private Truck truck;
    @DBRef(lazy = true)
    private ItemCategoryType itemCategoryType;
    @DBRef
    private ServiceProvider serviceProvider;
    @DBRef (lazy = true)
    private Set<RequestPurchaseItem> items;
    private boolean approvalStatus;
    private String reasonForDisapproval;
    private String deliveryInstructions;
    private Date deliveryDate;
    private Date misMatchDate;
    private Date paymentDate;
    private BigDecimal total;
    private BigDecimal paymentAmount;
    private String matchStatus;
    private String invoiceNumber;
    private String approver;
    private Date orderDate;
    private boolean emailstatus;
    private String truckId;
    private String serviceProviderSupplierId;

    private Request() {
    }

    private Request(Builder builder) {
        this.id = builder.id;
        this.orderDate = builder.orderDate;
        this.person = builder.person;
        this.serviceProvider = builder.serviceProvider;
        this.items = builder.items;
        this.approvalStatus = builder.approvalStatus;
        this.matchStatus = builder.matchStatus;
        this.reasonForDisapproval = builder.reasonForDisapproval;
        this.deliveryInstructions = builder.deliveryInstructions;
        this.deliveryDate = builder.deliveryDate;
        this.total = builder.total;
        this.paymentAmount = builder.paymentAmount;
        this.invoiceNumber = builder.invoiceNumber;
        this.costCentreType = builder.costCentreType;
        this.categoryType = builder.categoryType;
        this.itemCategoryType = builder.itemCategoryType;
        this.truck = builder.truck;
        this.orderNumber = builder.orderNumber;
        this.misMatchDate = builder.misMatchDate;
        this.paymentDate = builder.paymentDate;
        this.approver = builder.approver;
        this.emailstatus = builder.emailstatus;
        this.truckId = builder.truckId;
        this.serviceProviderSupplierId = builder.serviceProviderSupplierId;
    }

    @Override
    public int compareTo(Request o) {
        return getOrderNumber().compareToIgnoreCase(o.getOrderNumber());
    }
    public static Comparator<Request> AscOrderTruckAscOrderDeliveryDateComparator = new Comparator<Request>() {
        @Override
        public int compare(Request request1, Request request2) {
            // Ascending Order by Truck
            int compareOne = request1.getTruckId().compareTo(request2.getTruckId());
            // Ascending order by Delivery Date
            int compareTwo = request1.getDeliveryDate().compareTo(request2.getDeliveryDate());

            return ((compareOne == 0) ? compareTwo : compareOne);
        }
    };

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Request other = (Request) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public static class Builder {

        private String id;
        private final Person person;
        private ServiceProvider serviceProvider;
        private Set<RequestPurchaseItem> items;
        private boolean approvalStatus;
        private String reasonForDisapproval;
        private String deliveryInstructions;
        private Date deliveryDate;
        private Date paymentDate;
        private BigDecimal total;
        private BigDecimal paymentAmount;
        private String matchStatus;
        private String invoiceNumber;
        private CostCentreType costCentreType;
        private CostCentreCategoryType categoryType;
        private ItemCategoryType itemCategoryType;
        private Truck truck;
        private String orderNumber;
        private Date misMatchDate;
        private String approver;
        private Date orderDate;
        private boolean emailstatus;
        private String truckId;
        private String serviceProviderSupplierId;

        public Builder(Person value) {
            this.person = value;
        }

        public Builder request(Request request) {
            this.id = request.getId();
            this.serviceProvider = request.getServiceProvider();
            this.items = request.getRequestPurchaseItems();
            this.orderNumber = request.getOrderNumber();
            this.approvalStatus = request.isApprovalStatus();
            this.matchStatus = request.getStatus();
            this.reasonForDisapproval = request.getReasonForDisapproval();
            this.deliveryInstructions = request.getDeliveryInstructions();
            this.deliveryDate = request.getDeliveryDate();
            this.total = request.getTotal();
            this.paymentAmount = request.getPaymentAmount();
            this.invoiceNumber = request.getInvoiceNumber();
            this.costCentreType = request.getCostCentreType();
            this.categoryType = request.getCategoryType();
            this.itemCategoryType = request.getItemCategoryType();
            this.misMatchDate = request.getMisMatchDate();
            this.orderDate = request.getOrderDate();
            this.paymentDate = request.getPaymentDate();
            this.approver = request.getApprover();
            this.emailstatus = request.isEmailstatus();
            this.truck = request.getTruck();
            this.truckId = request.getTruckId();
            this.serviceProviderSupplierId = request.getServiceProviderSupplierId();
            return this;
        }

        public Builder id(String value) {
            this.id = value;
            return this;
        }

        public Builder paymentDate(Date value) {
            this.paymentDate = value;
            return this;
        }

        public Builder paymentAmount(BigDecimal value) {
            this.paymentAmount = value;
            return this;
        }

        public Builder emailstatus(boolean value) {
            this.emailstatus = value;
            return this;
        }

        public Builder orderDate(Date value) {
            this.orderDate = value;
            return this;
        }

        public Builder approver(String value) {
            this.approver = value;
            return this;
        }

        public Builder misMatchDate(Date value) {
            this.misMatchDate = value;
            return this;
        }

        public Builder truck(Truck value) {
            this.truck = value;
            return this;
        }

        public Builder costCentreType(CostCentreType value) {
            this.costCentreType = value;
            return this;
        }

        public Builder categoryType(CostCentreCategoryType value) {
            this.categoryType = value;
            return this;
        }

        public Builder itemCategoryType(ItemCategoryType value) {
            this.itemCategoryType = value;
            return this;
        }

        public Builder invoiceNumber(String value) {
            this.invoiceNumber = value;
            return this;
        }

        public Builder total(BigDecimal value) {
            this.total = value;
            return this;
        }

        public Builder deliveryInstructions(String value) {
            this.deliveryInstructions = value;
            return this;
        }

        public Builder deliveryDate(Date value) {
            this.deliveryDate = value;
            return this;
        }

        public Builder reasonForDisapproval(String value) {
            this.reasonForDisapproval = value;
            return this;
        }

        public Builder approvalStatus(boolean value) {
            this.approvalStatus = value;
            return this;
        }

        public Builder matchStatus(String value) {
            this.matchStatus = value;
            return this;
        }

        public Builder items(Set<RequestPurchaseItem> value) {
            this.items = value;
            return this;
        }

        public Builder serviceProvider(ServiceProvider value) {
            this.serviceProvider = value;
            return this;
        }

        public Builder orderNumber(String value) {
            this.orderNumber = value;
            return this;
        }

        public Builder truckId(String value) {
            this.truckId = value;
            return this;
        }

        public Builder serviceProviderSupplierId(String value) {
            this.serviceProviderSupplierId = value;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public boolean isEmailstatus() {
        return emailstatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getApprover() {
        return approver;
    }

    public Date getMisMatchDate() {
        return misMatchDate;
    }

    public CostCentreType getCostCentreType() {
        return costCentreType;
    }

    public CostCentreCategoryType getCategoryType() {
        return categoryType;
    }

    public ItemCategoryType getItemCategoryType() {
        return itemCategoryType;
    }

    public String getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Truck getTruck() {
        return truck;
    }

    public Person getPerson() {
        return person;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public Set<RequestPurchaseItem> getRequestPurchaseItems() {
        return items;
    }

    public boolean isApprovalStatus() {
        return approvalStatus;
    }

    public String getReasonForDisapproval() {
        return reasonForDisapproval;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return matchStatus;
    }

    private boolean isNullObject(Object object) {
        if (object == null) {
            return true;
        } else {
            return false;
        }
    }

    public String getPersonId() {
        if (!isNullObject(person)) {
            return person.getId();
        } else {
            return null;
        }
    }

    public String getPersonName() {
        if (!isNullObject(person)) {
            return person.getFirstname() + " " + person.getLastname();
        } else {
            return null;
        }
    }

    public String getServiceProviderId() {
        if (!isNullObject(serviceProvider)) {
            return serviceProvider.getId();
        } else {
            return null;
        }
    }

    public String getServiceProviderName() {
        if (!isNullObject(serviceProvider)) {
            return serviceProvider.getName();
        } else {
            return null;
        }
    }

    public String getServiceProviderEmail() {
        if (!isNullObject(serviceProvider)) {
            return serviceProvider.getContactPersonEmail();
        } else {
            return null;
        }
    }

    /**
     * @return the truckId
     */
    public String getTruckId() {
        return truckId;
    }

    /**
     * @return the serviceProviderSupplierId
     */
    public String getServiceProviderSupplierId() {
        return serviceProviderSupplierId;
    }
}
