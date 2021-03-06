/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.repository.fleet.Impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.domain.fleet.OperatingCost;
import zm.hashcode.mshengu.domain.fleet.Truck;
import zm.hashcode.mshengu.repository.fleet.OperatingCostRepositoryCustom;

/**
 *
 * @author Colin
 */
public class OperatingCostRepositoryImpl implements OperatingCostRepositoryCustom {

    final DateTimeFormatHelper dateTimeFormatHelper = new DateTimeFormatHelper();
    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public List<OperatingCost> findOperatingCostBetweenTwoDates(Date from, Date to) {
        Date fromDate = dateTimeFormatHelper.resetTimeAndMonthStart(from);
        Date toDate = dateTimeFormatHelper.resetTimeAndMonthEnd(to);
        Query operatingCostListQuery = new Query();
        operatingCostListQuery.addCriteria(
                Criteria.where("transactionDate").exists(true)
                .andOperator(Criteria.where("transactionDate").gte(fromDate),
                Criteria.where("transactionDate").lte(toDate)));

        /*
         List<OperatingCost> operatingCostList = mongoOperation.find(operatingCostListQuery, OperatingCost.class);

         System.out.println("OperatingCostRepository  - GENERAL QUERY Start= " + from + " | To= " + to);
         if (operatingCostList.isEmpty()) {
         System.out.println("OperatingCostRepository  - GENERAL QUERY - NO MATCHING RECORDS FOUND");
         }

         for (OperatingCost operatingCost : operatingCostList) {
         System.out.println("OperatingCostRepository - GENERAL QUERY -  Date= " + operatingCost.getTransactionDate() + " | Mileage= " + operatingCost.getSpeedometer() + " | Truck= " + operatingCost.getTruckId());
         }
         System.out.println("--==--");
         */
        return mongoOperation.find(operatingCostListQuery, OperatingCost.class);
    }

    @Override
    public List<OperatingCost> getOperatingCostByTruckByMonth(Truck truck, Date month) {
        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        return getOperatingCosts(truck, from, to);
    }

    @Override
    public List<OperatingCost> getOperatingCostByTruckBetweenTwoDates(Truck truck, Date from, Date to) {
        Date fromDate = dateTimeFormatHelper.resetTimeAndMonthStart(from);
        Date toDate = dateTimeFormatHelper.resetTimeAndMonthEnd(to);
        return getOperatingCosts(truck, fromDate, toDate);
    }

    private List<OperatingCost> getOperatingCosts(Truck truck, Date from, Date to) {
        Query truckOperatingCostListQuery = new Query();
        truckOperatingCostListQuery.addCriteria(
                Criteria.where("truckId").is(truck.getId())
                .andOperator(Criteria.where("transactionDate").gte(from),
                Criteria.where("transactionDate").lte(to)));

        /*
         List<OperatingCost> operatingCostList = mongoOperation.find(truckOperatingCostListQuery, OperatingCost.class);

         System.out.println("OperatingCostRepository  - TRUCK QUERY Start= " + from + " | To= " + to + "  FOR TRUCK: " + truck.getId());
         if (operatingCostList.isEmpty()) {
         System.out.println("OperatingCostRepository  - TRUCK QUERY - NO MATCHING RECORDS FOUND");
         }

         for (OperatingCost operatingCost : operatingCostList) {
         System.out.println("OperatingCostRepository - TRUCK QUERY -  Date= " + operatingCost.getTransactionDate() + " | Mileage= " + operatingCost.getSpeedometer() + " | Truck= " + operatingCost.getTruckId());
         }
         System.out.println("--==--");
         */

        return mongoOperation.find(truckOperatingCostListQuery, OperatingCost.class);
    }

    private Date resetToMonthEndLastSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // Set time fields to last hour:minute:second:millisecond
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
