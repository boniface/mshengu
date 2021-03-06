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
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Colin
 */
@Document
public class AnnualDataFleetMaintenanceMileage implements Serializable, Comparable<AnnualDataFleetMaintenanceMileage> {

    @Id
    private String id;
    private String truckId;
    private String driverPersonId;
    private Date transactionMonth;
    private Integer monthlyMileage;

    private AnnualDataFleetMaintenanceMileage() {
    }

    public AnnualDataFleetMaintenanceMileage(Builder builder) {
        this.id = builder.id;
        this.truckId = builder.truckId;
        this.driverPersonId = builder.driverPersonId;
        this.transactionMonth = builder.transactionMonth;
        this.monthlyMileage = builder.monthlyMileage;
    }

    public static class Builder {

        private String id;
        private String truckId;
        private String driverPersonId;
        private Date transactionMonth;
        private Integer monthlyMileage;

        public Builder(Date value) {
            this.transactionMonth = value;
        }

        public Builder id(String value) {
            this.id = value;
            return this;
        }

        public Builder monthlyMileage(Integer value) {
            this.monthlyMileage = value;
            return this;
        }

        public Builder truckId(String value) {
            this.truckId = value;
            return this;
        }

        public Builder driverPersonId(String value) {
            this.driverPersonId = value;
            return this;
        }

        public AnnualDataFleetMaintenanceMileage build() {
            return new AnnualDataFleetMaintenanceMileage(this);
        }
    }

    @Override
    public int compareTo(AnnualDataFleetMaintenanceMileage o) {
        return this.transactionMonth.compareTo(o.transactionMonth);
    }
    public static Comparator<AnnualDataFleetMaintenanceMileage> DescOrderDateAscOrderTruck = new Comparator<AnnualDataFleetMaintenanceMileage>() {
        @Override
        public int compare(AnnualDataFleetMaintenanceMileage annualDataFleetMaintenanceMileage1, AnnualDataFleetMaintenanceMileage annualDataFleetMaintenanceMileage2) {

            //descending order by Date
            int compareOne = annualDataFleetMaintenanceMileage2.getTransactionMonth().compareTo(annualDataFleetMaintenanceMileage1.getTransactionMonth());
            // Ascending Order by Truck
            int compareTwo = annualDataFleetMaintenanceMileage1.getTruckId().compareTo(annualDataFleetMaintenanceMileage2.getTruckId());

            return ((compareOne == 0) ? compareTwo : compareOne);
        }
    };
    public static Comparator<AnnualDataFleetMaintenanceMileage> AscOrderTruckAscOrderDateComparator = new Comparator<AnnualDataFleetMaintenanceMileage>() {
        @Override
        public int compare(AnnualDataFleetMaintenanceMileage annualDataFleetMaintenanceMileage1, AnnualDataFleetMaintenanceMileage annualDataFleetMaintenanceMileage2) {

            // Ascending Order by Truck
            int compareOne = annualDataFleetMaintenanceMileage1.getTruckId().compareTo(annualDataFleetMaintenanceMileage2.getTruckId());
            // Ascending order by Date
            int compareTwo = annualDataFleetMaintenanceMileage1.getTransactionMonth().compareTo(annualDataFleetMaintenanceMileage2.getTransactionMonth());


            return ((compareOne == 0) ? compareTwo : compareOne);
        }
    };

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.getId());
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
        final AnnualDataFleetMaintenanceMileage other = (AnnualDataFleetMaintenanceMileage) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the truckId
     */
    public String getTruckId() {
        return truckId;
    }

    /**
     * @return the driverPersonId
     */
    public String getDriverPersonId() {
        return driverPersonId;
    }

    /**
     * @return the transactionMonth
     */
    public Date getTransactionMonth() {
        return transactionMonth;
    }

    /**
     * @return the monthlyMileage
     */
    public Integer getMonthlyMileage() {
        return monthlyMileage;
    }
}
