/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.views;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.dussan.vaadin.dcharts.DCharts;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.app.util.flagImages.FlagImage;
import zm.hashcode.mshengu.app.util.panel.PanelEfficiency;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.charts.FuelExecutiveDashboardChartUI;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.charts.actualcharts.executivedashboard.DieselPriceRandPerLitreLineChart;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.charts.actualcharts.executivedashboard.TotalFleetFuelSpendBarChart;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.forms.FuelExecutiveDashboardForm;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.model.executivedashboard.FuelSpendMonthlyCostBean;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.util.AnnualFuelSpendLayout;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.util.EfficiencylLayout;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.util.FleetFuelUtil;
import zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetfuel.util.FuelSpendLayout;
import zm.hashcode.mshengu.domain.fleet.OperatingCost;
import zm.hashcode.mshengu.domain.fleet.Truck;

/**
 *
 * @author Colin
 */
public class ExecutiveDashboardTab extends VerticalLayout implements
        Button.ClickListener, Property.ValueChangeListener {

    private final MshenguMain main;
    private final FuelExecutiveDashboardForm form;
    private final FuelExecutiveDashboardChartUI chart;
    private final FleetFuelUtil fleetFuelUtil = new FleetFuelUtil();
    private final DateTimeFormatHelper dateTimeFormatHelper = new DateTimeFormatHelper();
    private final FlagImage flagImage = new FlagImage();
    public static Date startDate = null;
    public static Date endDate = null;
    public static List<OperatingCost> operatingCostTwentyFiveMonthsList = new ArrayList<>();
    public static List<OperatingCost> dateRangeOperatingCostList = new ArrayList<>();
    public static List<FuelSpendMonthlyCostBean> fuelSpendMonthlyCostBeanList = new ArrayList<>();
    public static BigDecimal grandTotalFuelSpend = BigDecimal.ZERO;
    //
    public static BigDecimal fuelSpendService = BigDecimal.ZERO; // PENDING
    public static BigDecimal fuelSpendUnit = BigDecimal.ZERO;// PENDING
    //
    public static BigDecimal annualTotalFuelSpend = BigDecimal.ZERO;
    public static BigDecimal serviceTotalFuelSpend = BigDecimal.ZERO;
    public static BigDecimal utilityTotalFuelSpend = BigDecimal.ZERO;
    public static BigDecimal operationalTotalFuelSpend = BigDecimal.ZERO;
    public static BigDecimal nonOperationalTotalFuelSpend = BigDecimal.ZERO;
    //
    public static BigDecimal serviceFuelSpendPercentage = BigDecimal.ZERO;
    public static BigDecimal utilityFuelSpendPercentage = BigDecimal.ZERO;
    public static BigDecimal operationalFuelSpendPercentage = BigDecimal.ZERO;
    public static BigDecimal nonOperationalFuelSpendPercentage = BigDecimal.ZERO;
    //
    public static Integer annualMileageSumAllMsvTrucks = new Integer("0");
    public static String chartPeriod = null;
    public static Integer chartPeriodCount = null;
    //
    private static BigDecimal oneMonthEfficiency = BigDecimal.ZERO;
    private static BigDecimal threeMonthEfficiency = BigDecimal.ZERO;
    private static BigDecimal twelveMonthEfficiency = BigDecimal.ZERO;
    private static Embedded oneMonthEfficiencyFlag = new Embedded();
    private static Embedded threeMonthEfficiencyFlag = new Embedded();
    private static Embedded twelveMonthEfficiencyFlag = new Embedded();
//    private static Embedded fuelSpendUnitFlag = new Embedded();// PENDING
//    private static Embedded fuelSpendServiceFlag = new Embedded();// PENDING

    public static BigDecimal fuelSpendPerUnit = BigDecimal.ZERO;
    public static BigDecimal fuelSpendPerService = BigDecimal.ZERO;

//    private static boolean isThreeMonthOutOfRange = false;
//    private static boolean isTwelveMonthOutOfRange = false;
//    //
    private static List<OperatingCost> operatingCostThreeMonthsList = new ArrayList<>();
    private static List<OperatingCost> operatingCostTwelveMonthsList = new ArrayList<>();

    public ExecutiveDashboardTab(MshenguMain app) {
        main = app;
        form = new FuelExecutiveDashboardForm();
        chart = new FuelExecutiveDashboardChartUI(main);
        setSizeFull();
        addComponent(form);
        addComponent(chart);
        addListeners(); // for Dashboard chart sizing
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        final Button source = event.getButton();
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        final Property property = event.getProperty();
        if (property == form.startDate) {
            try {
                startDate = fleetFuelUtil.resetMonthToFirstDay(form.startDate.getValue()); // reset the choosen start date to 1st day
            } catch (java.lang.NullPointerException ex) {
                Notification.show("Error. Enter a Valid Date for Start Date.", Notification.Type.ERROR_MESSAGE);
            }
            try {
                endDate = fleetFuelUtil.resetMonthToLastDay(form.endDate.getValue());
            } catch (java.lang.NullPointerException ex) {
            }
            if (endDate != null) {
                if (endDate.before(fleetFuelUtil.resetMonthToFirstDay(new Date()))) {
                    int monthCount = fleetFuelUtil.countMonthsInRange(startDate, endDate);
                    if (monthCount > 0 && monthCount <= 12) {
                        getDataAndPerformCharts();
                    } else {
                        Notification.show("Please Specify Date Range in Approprate Order and at most 12 months.", Notification.Type.TRAY_NOTIFICATION);
                    }
                } else {
                    Notification.show("Error. Select any month before Current Month as End Date.", Notification.Type.TRAY_NOTIFICATION);
                }
            }
        } else if (property == form.endDate) {
            try {
                endDate = fleetFuelUtil.resetMonthToLastDay(form.endDate.getValue());
            } catch (java.lang.NullPointerException ex) {
                Notification.show("Error. Enter a Valid Date for End Date.", Notification.Type.ERROR_MESSAGE);
            }
            try {
                startDate = fleetFuelUtil.resetMonthToFirstDay(form.startDate.getValue());
            } catch (java.lang.NullPointerException ex) {
            }
            if (startDate != null) {
                if (endDate.before(fleetFuelUtil.resetMonthToFirstDay(new Date()))) {
                    int monthCount = fleetFuelUtil.countMonthsInRange(startDate, endDate);
                    if (monthCount > 0 && monthCount <= 12) {
                        getDataAndPerformCharts();
                    } else {
                        Notification.show("Please Specify Date Range in Approprate Order and at most 12 months.", Notification.Type.TRAY_NOTIFICATION);
                    }
                } else {
                    Notification.show("Error. Select any month before Current Month as End Date.", Notification.Type.TRAY_NOTIFICATION);
                }
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getDataAndPerformCharts() {
        dateRangeOperatingCostList.clear();
        operatingCostTwentyFiveMonthsList.clear();

        // Determine date range for other calculations on this Tab, extra 3 month for Previous month mileage
        // or get StartMileage for Car
        fleetFuelUtil.determineDateRange(endDate, 25);
        fleetFuelUtil.getTrucks();

        operatingCostTwentyFiveMonthsList.addAll(fleetFuelUtil.getOperatingCostByTruckBetweenTwoDates(FleetFuelUtil.startDate, FleetFuelUtil.endDate));
        Collections.sort(operatingCostTwentyFiveMonthsList, OperatingCost.DescOrderDateAscOrderTruckIdComparator);
        // Extract date Range for Charts only
        dateRangeOperatingCostList.addAll(fleetFuelUtil.getOperatingCostsForDateRange(startDate, endDate, operatingCostTwentyFiveMonthsList));
        if (!dateRangeOperatingCostList.isEmpty()) {
            buildFuelSpendMonthlyCostBeanList(); // used for both Bar n Line Charts

            oneMonthEfficiency = getOneMonthlyFleetEfficiency();
            oneMonthEfficiencyFlag = flagImage.determineFuelUsageFlag(oneMonthEfficiency);
//
            Date nuEndDate = fleetFuelUtil.resetMonthToFirstDay(endDate); // first day of endDate month so startDate will be first day of month
            Date startDatee = fleetFuelUtil.determineStartDate(nuEndDate, 3);
            nuEndDate = fleetFuelUtil.resetMonthToLastDay(endDate); // reset Endate to last day of month
            operatingCostThreeMonthsList = getOperatingCostsforMonthsRange(operatingCostTwentyFiveMonthsList, nuEndDate, startDatee);
            //
            threeMonthEfficiency = getThreeMonthlyFleetEfficiency(startDatee, nuEndDate);
            threeMonthEfficiencyFlag = flagImage.determineFuelUsageFlag(threeMonthEfficiency);
            //
            nuEndDate = fleetFuelUtil.resetMonthToFirstDay(endDate); // first day of endDate month so startDate will be first day of month
            startDatee = fleetFuelUtil.determineStartDate(nuEndDate, 12);
            nuEndDate = fleetFuelUtil.resetMonthToLastDay(endDate); // reset Endate to last day of month
            operatingCostTwelveMonthsList = getOperatingCostsforMonthsRange(operatingCostTwentyFiveMonthsList, nuEndDate, startDatee);
            //
            twelveMonthEfficiency = getTwelveMonthlyFleetEfficiency(startDatee, nuEndDate);
            twelveMonthEfficiencyFlag = flagImage.determineFuelUsageFlag(twelveMonthEfficiency);
            // buildFuelSpendData(operatingCostTwelveMonthsList); // PENDING
            buildAnnualFuelSpendData(); //
            //
            displayCharts();
        } else {
            Notification.show("No Daily input Found for Specified Date Range!", Notification.Type.TRAY_NOTIFICATION);
        }

    }

    public void buildFuelSpendMonthlyCostBeanList() {
        BigDecimal randPerLitre = BigDecimal.ZERO;
        // SORT THE LIST BY DATE ASC
        Collections.sort(dateRangeOperatingCostList);// ,OperatingCost.AscOrderDateAscOrderTruckIdComparator
        // LOOP AND SUBTOTAL
        fuelSpendMonthlyCostBeanList.clear();
        BigDecimal monthFuelCostTotal = BigDecimal.ZERO;
        Date transactionDate = fleetFuelUtil.resetMonthToFirstDay(dateRangeOperatingCostList.get(0).getTransactionDate());
        int counter = 0;
        for (OperatingCost operatingCost : dateRangeOperatingCostList) {
            if (fleetFuelUtil.resetMonthToFirstDay(operatingCost.getTransactionDate()).equals(transactionDate)) {
                monthFuelCostTotal = monthFuelCostTotal.add(operatingCost.getFuelCost());
                // Find the RandPerLitre for the LAST DAY OF Month using Service MSV trucks ONLY
                for (Truck truck : FleetFuelUtil.serviceTrucks) {
                    if (truck.getId().equals(operatingCost.getTruckId())
                            && FleetFuelUtil.truncate(truck.getVehicleNumber(), 3).equalsIgnoreCase("MSV")
                            && (operatingCost.getRandPerLitre().compareTo(BigDecimal.ZERO) > 0)) {
                        randPerLitre = operatingCost.getRandPerLitre();
                        break;
                    }
                }
                // Test for Last item in List and SubTotal
                if (dateRangeOperatingCostList.indexOf(operatingCost) == dateRangeOperatingCostList.size() - 1) {
                    counter++;
                    performSubTotal(monthFuelCostTotal, dateRangeOperatingCostList, operatingCost, randPerLitre, counter);
                }

            } else {
                // Subtotal
                counter++;
                performSubTotal(monthFuelCostTotal, dateRangeOperatingCostList, operatingCost, randPerLitre, counter);
                //Reset
                monthFuelCostTotal = operatingCost.getFuelCost();
                transactionDate = fleetFuelUtil.resetMonthToFirstDay(operatingCost.getTransactionDate());
                randPerLitre = BigDecimal.ZERO;
            }
        }
    }

    private void performSubTotal(BigDecimal monthTotal, List<OperatingCost> dateRangeOperatingCostList, OperatingCost operatingCost, BigDecimal randPerLitre, int counter) {
        // Subtotal
        monthTotal = monthTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
        grandTotalFuelSpend = grandTotalFuelSpend.add(monthTotal);
        // Build FuelSpendMonthlyCostBean and add to ArrayList
        int currentIndex = dateRangeOperatingCostList.indexOf(operatingCost);
        fuelSpendMonthlyCostBeanList.add(buildFuelSpendMonthlyCostBeanObject(counter, monthTotal, dateRangeOperatingCostList.get(currentIndex - 1), randPerLitre));
    }

    private FuelSpendMonthlyCostBean buildFuelSpendMonthlyCostBeanObject(int counter, BigDecimal monthTotal, OperatingCost operatingCost, BigDecimal randPerLitre) {
        FuelSpendMonthlyCostBean fuelSpendMonthlyCostBean = new FuelSpendMonthlyCostBean();
        fuelSpendMonthlyCostBean.setMonthlyAmountSpend(monthTotal);
        fuelSpendMonthlyCostBean.setId(counter + "");
        fuelSpendMonthlyCostBean.setMonth(dateTimeFormatHelper.getMonthYearMonthAsMediumString(operatingCost.getTransactionDate().toString()));
        fuelSpendMonthlyCostBean.setTransactionMonth(operatingCost.getTransactionDate());
        fuelSpendMonthlyCostBean.setMonthRandPerLiter(randPerLitre);

        return fuelSpendMonthlyCostBean;
    }

    public BigDecimal getOneMonthlyFleetEfficiency() { //, Date endDate
        Integer monthMileageTotal = new Integer("0");
        BigDecimal monthFuelCostTotal = BigDecimal.ZERO;
        List<OperatingCost> truckMonthOperatingCostList = new ArrayList<>();
//        Collections.sort(operatingCostTwentyFiveMonthsList, OperatingCost.DescOrderDateAscOrderTruckIdComparator);
        Date endDatee = fleetFuelUtil.resetMonthToFirstDay(operatingCostTwentyFiveMonthsList.get(0).getTransactionDate());
//        int counter = 0; // counter for Number of Truck with operating Cost data for the period specified

        for (Truck truck : FleetFuelUtil.msvTrucks) { //  Not using allTrucks b/c Ops and Non-Ops Trucks dont have mileage entered
            truckMonthOperatingCostList.clear();
            for (OperatingCost operatingCost : operatingCostTwentyFiveMonthsList) {
                if (!(operatingCost.getSpeedometer() <= 0 && operatingCost.getFuelCost().compareTo(BigDecimal.ZERO) == 0
                        && operatingCost.getFuelLitres().compareTo(Double.parseDouble("0.0")) == 0)) {
                    if (operatingCost.getTruckId().equals(truck.getId())
                            && endDatee.compareTo(fleetFuelUtil.resetMonthToFirstDay(operatingCost.getTransactionDate())) == 0) {
                        truckMonthOperatingCostList.add(operatingCost);
                    }
                }
                // if Date changes, then it is no longer End Date as we sorted in Desc Order
                if (endDatee.compareTo(fleetFuelUtil.resetMonthToFirstDay(operatingCost.getTransactionDate())) != 0) {
                    break;
                }
            }
            // getMTDAct for Truck
            if (truckMonthOperatingCostList.size() > 0) {
                monthFuelCostTotal = monthFuelCostTotal.add(fleetFuelUtil.sumOfFuelCostCalculation(truckMonthOperatingCostList));
                monthMileageTotal += fleetFuelUtil.doMileageCalculation(truckMonthOperatingCostList, truck);
            }
        }
        // get MTDAct or 1M Efficiency for msv Trucks
        return fleetFuelUtil.performMtdActAverageCalc(monthFuelCostTotal, monthMileageTotal);
    }

    public BigDecimal getThreeMonthlyFleetEfficiency(Date startDatee, Date endDatee) {
        List<OperatingCost> selectedThreeMonthsOperatingCostList = new ArrayList<>();
        Integer allTrucksTotalMileageSum = new Integer("0");
        BigDecimal totalFuelCostAllTrucks = BigDecimal.ZERO;
        List<OperatingCost> truckCurrentMonthOperatingCostList = new ArrayList<>();
        Collections.sort(operatingCostThreeMonthsList, OperatingCost.DescOrderDateAscOrderTruckIdComparator); // MAY NOT be NEEDED. Comment this line and see if if affects OUTPUT
        endDatee = fleetFuelUtil.resetMonthToFirstDay(endDate); // For Loop ACCURACY sakes
        for (Truck truck : FleetFuelUtil.msvTrucks) {
            truckCurrentMonthOperatingCostList.clear();
            Calendar calendar = Calendar.getInstance();
            for (calendar.setTime(endDatee); calendar.getTime().after(startDatee) || calendar.getTime().compareTo(startDatee) == 0; calendar.add(Calendar.MONTH, -1)) {
                truckCurrentMonthOperatingCostList.addAll(getOneMonthlyOperatingCostForTruck(operatingCostThreeMonthsList, calendar.getTime(), truck));
                if (!truckCurrentMonthOperatingCostList.isEmpty()) {
//                    Integer mileageSUM = fleetFuelUtil.calculateMonthMileageTotal(truckCurrentMonthOperatingCostList, truck);
//                    truck3MonthMileageSum += mileageSUM;
                    allTrucksTotalMileageSum += fleetFuelUtil.calculateMonthMileageTotal(truckCurrentMonthOperatingCostList, truck);
                    selectedThreeMonthsOperatingCostList.addAll(truckCurrentMonthOperatingCostList);
                    truckCurrentMonthOperatingCostList.clear();
                }
            }
            // SUM the FuelCostS for truck for these 3 months and increment the totalFuelCostAllTrucks value
            BigDecimal truckThreeMonthFuelTotal = fleetFuelUtil.sumOfFuelCostCalculation(selectedThreeMonthsOperatingCostList);
            totalFuelCostAllTrucks = totalFuelCostAllTrucks.add(truckThreeMonthFuelTotal);
            selectedThreeMonthsOperatingCostList.clear();
        }
        // get MTDAct or 3M Efficiency for msv Trucks
        return fleetFuelUtil.performMtdActAverageCalc(totalFuelCostAllTrucks, allTrucksTotalMileageSum);
    }

    public BigDecimal getTwelveMonthlyFleetEfficiency(Date startDatee, Date endDatee) {
        annualMileageSumAllMsvTrucks = new Integer("0");
        BigDecimal totalFuelCostAllTrucks = BigDecimal.ZERO;
        List<OperatingCost> selectedTwelveMonthsOperatingCostList = new ArrayList<>();
//        if (!isTwelveMonthOutOfRange) {
        List<OperatingCost> truckCurrentMonthOperatingCostList = new ArrayList<>();
        Collections.sort(operatingCostTwelveMonthsList, OperatingCost.DescOrderDateAscOrderTruckIdComparator);
        endDatee = fleetFuelUtil.resetMonthToFirstDay(endDate); // For Loop ACCURACY sakes
        for (Truck truck : FleetFuelUtil.msvTrucks) {
            truckCurrentMonthOperatingCostList.clear();
//            Integer truck12MonthMileageSum = new Integer("0");
            Calendar calendar = Calendar.getInstance();
            for (calendar.setTime(endDatee); calendar.getTime().after(startDatee) || calendar.getTime().compareTo(startDatee) == 0; calendar.add(Calendar.MONTH, -1)) {
                truckCurrentMonthOperatingCostList.addAll(getOneMonthlyOperatingCostForTruck(operatingCostTwelveMonthsList, calendar.getTime(), truck));

                if (!truckCurrentMonthOperatingCostList.isEmpty()) {
//                    Integer mileageSUM = fleetFuelUtil.calculateMonthMileageTotal(truckCurrentMonthOperatingCostList, truck);
//                    truck12MonthMileageSum += mileageSUM;
                    annualMileageSumAllMsvTrucks += fleetFuelUtil.calculateMonthMileageTotal(truckCurrentMonthOperatingCostList, truck);
                    selectedTwelveMonthsOperatingCostList.addAll(truckCurrentMonthOperatingCostList);
                    truckCurrentMonthOperatingCostList.clear();
                }
            }
////            //========== DELETE ==============
////            System.out.println("SUM OF 12 Months Mileage SUM for " + truck.getVehicleNumber() + " = " + truck12MonthMileageSum);
////            //========== DELETE ==============

            // SUM the FuelCostS for truck for these 3 months and increment the totalFuelCostAllTrucks value
            BigDecimal truckTwelveMonthFuelTotal = fleetFuelUtil.sumOfFuelCostCalculation(selectedTwelveMonthsOperatingCostList);
            totalFuelCostAllTrucks = totalFuelCostAllTrucks.add(truckTwelveMonthFuelTotal);
            selectedTwelveMonthsOperatingCostList.clear();

////            //========== DELETE ==============
////            System.out.println("SUM OF 12 Months FUEL COST for " + truck.getVehicleNumber() + " = " + truckTwelveMonthFuelTotal);
////            //========== DELETE ==============
        }

        // get MTDAct or 12M Efficiency for msv Trucks
        return fleetFuelUtil.performMtdActAverageCalc(totalFuelCostAllTrucks, annualMileageSumAllMsvTrucks);
    }

    public List<OperatingCost> getOneMonthlyOperatingCostForTruck(List<OperatingCost> operatingCostList, Date date, Truck truck) {
//        boolean found = false;
        List<OperatingCost> truckMonthOperatingCostList = new ArrayList<>();
        truckMonthOperatingCostList.clear();
        for (OperatingCost operatingCost : operatingCostList) {
            if (operatingCost.getTruckId().equals(truck.getId())
                    && date.compareTo(fleetFuelUtil.resetMonthToFirstDay(operatingCost.getTransactionDate())) == 0) {
                truckMonthOperatingCostList.add(operatingCost);
//                found = true;
            }
            // if Date changes, then it is no longer End Date as we sorted in Desc Order
            if (/*found &&*/fleetFuelUtil.resetMonthToFirstDay(operatingCost.getTransactionDate()).before(date)) {
                break;
            }
        }

        return truckMonthOperatingCostList;
    }

    public List<OperatingCost> getOperatingCostsforMonthsRange(List<OperatingCost> operatingCostTwentyFiveMonthsList, Date endDate, Date startDate) {
//        Collections.sort(operatingCostTwentyFiveMonthsList, OperatingCost.DescOrderDateAscOrderTruckIdComparator); // has been sorted already
        List<OperatingCost> monthsOperatingCostList = new ArrayList<>();
        for (OperatingCost operatingCost : operatingCostTwentyFiveMonthsList) {
            // Omit ZERO OBJECTS
            if (!(operatingCost.getSpeedometer() == 0 && operatingCost.getFuelCost().compareTo(BigDecimal.ZERO) == 0 && operatingCost.getFuelLitres().compareTo(Double.parseDouble("0.0")) == 0 && operatingCost.getSlipNo().equals("0000") && operatingCost.getRandPerLitre().compareTo(BigDecimal.ZERO) == 0)) {
                if (operatingCost.getTransactionDate().compareTo(startDate) == 0
                        || (operatingCost.getTransactionDate().after(startDate) && operatingCost.getTransactionDate().before(endDate))
                        || operatingCost.getTransactionDate().compareTo(endDate) == 0) {
                    monthsOperatingCostList.add(operatingCost);
                }
            }
            // if Date is before startDate, then looping stops as we sorted operatingCostTwentyFiveMonthsList in Desc Order
            if (operatingCost.getTransactionDate().before(startDate)) {
                break;
            }
        }

        return monthsOperatingCostList;
    }

    public void buildAnnualFuelSpendData() {
        List<OperatingCost> serviceTruckOperatingCostList = new ArrayList<>();
        List<OperatingCost> operationalTruckOperatingCostList = new ArrayList<>();
        List<OperatingCost> nonOperationalTruckOperatingCostList = new ArrayList<>();
        List<OperatingCost> utilityTruckOperatingCostList = new ArrayList<>();

        for (OperatingCost operatingCost : operatingCostTwelveMonthsList) {
            Truck truck = fleetFuelUtil.findTruckFromAllTruckListById(operatingCost.getTruckId());
            if (truck != null) {
                if (FleetFuelUtil.truncate(truck.getVehicleNumber(), 3).equalsIgnoreCase("MMV")) {
                    nonOperationalTruckOperatingCostList.add(operatingCost);
                } else if (FleetFuelUtil.truncate(truck.getVehicleNumber(), 3).equalsIgnoreCase("MOV")) {
                    operationalTruckOperatingCostList.add(operatingCost);
                } else if (FleetFuelUtil.truncate(truck.getVehicleNumber(), 3).equalsIgnoreCase("MSV")) {
                    serviceTruckOperatingCostList.add(operatingCost);
                } else if (FleetFuelUtil.truncate(truck.getVehicleNumber(), 3).equalsIgnoreCase("MUV")) {
                    utilityTruckOperatingCostList.add(operatingCost);
                }
            }
        }
        serviceTotalFuelSpend = fleetFuelUtil.sumOfFuelCostCalculation(serviceTruckOperatingCostList);
        utilityTotalFuelSpend = fleetFuelUtil.sumOfFuelCostCalculation(utilityTruckOperatingCostList);
        operationalTotalFuelSpend = fleetFuelUtil.sumOfFuelCostCalculation(operationalTruckOperatingCostList);
        nonOperationalTotalFuelSpend = fleetFuelUtil.sumOfFuelCostCalculation(nonOperationalTruckOperatingCostList);
        //
        annualTotalFuelSpend = annualTotalFuelSpend.add(serviceTotalFuelSpend);
        annualTotalFuelSpend = annualTotalFuelSpend.add(utilityTotalFuelSpend);
        annualTotalFuelSpend = annualTotalFuelSpend.add(operationalTotalFuelSpend);
        annualTotalFuelSpend = annualTotalFuelSpend.add(nonOperationalTotalFuelSpend);
        //
        serviceFuelSpendPercentage = fleetFuelUtil.performFuelSpendPercentage(annualTotalFuelSpend, serviceTotalFuelSpend);
        utilityFuelSpendPercentage = fleetFuelUtil.performFuelSpendPercentage(annualTotalFuelSpend, utilityTotalFuelSpend);
        operationalFuelSpendPercentage = fleetFuelUtil.performFuelSpendPercentage(annualTotalFuelSpend, operationalTotalFuelSpend);
        nonOperationalFuelSpendPercentage = fleetFuelUtil.performFuelSpendPercentage(annualTotalFuelSpend, nonOperationalTotalFuelSpend);
    }

    public void displayCharts() {
        chartPeriod = fuelSpendMonthlyCostBeanList.get(0).getMonth() + " - " + fuelSpendMonthlyCostBeanList.get(fuelSpendMonthlyCostBeanList.size() - 1).getMonth();
        chartPeriodCount = fuelSpendMonthlyCostBeanList.size();

        // Assuming it is sorted by Date in Asc Order
        DCharts dTotalFleetFuelSpendChart = createTotalFleetFuelSpendChart();
        DCharts dTotalDieselPriceRandPerLitreChart = createDieselPriceRandPerLitreChart();

        // Create a Panel
        Panel totalTotalFleetFuelSpendPanel = new Panel("Total Fleet Fuel Spend: " + chartPeriod); //
        totalTotalFleetFuelSpendPanel.setWidth("100%");
        totalTotalFleetFuelSpendPanel.setHeight("100%");
        totalTotalFleetFuelSpendPanel.setStyleName("bubble");
        totalTotalFleetFuelSpendPanel.setSizeUndefined(); // Shrink to fit content

        HorizontalLayout totalTotalFleetFuelSpendLayout = new HorizontalLayout();
        totalTotalFleetFuelSpendLayout.setMargin(true); //
        totalTotalFleetFuelSpendLayout.setSizeUndefined(); // Shrink to fit content
        totalTotalFleetFuelSpendLayout.addComponent(dTotalFleetFuelSpendChart);
        // Add item to the Panel
        totalTotalFleetFuelSpendPanel.setContent(totalTotalFleetFuelSpendLayout);
        // ========

        // Create a Panel
        Panel dieselPriceRandPerLitrePanel = new Panel("Diesel Price (R/Ltr): " + chartPeriod); //
        dieselPriceRandPerLitrePanel.setWidth("100%");
        dieselPriceRandPerLitrePanel.setHeight("100%");
        dieselPriceRandPerLitrePanel.setStyleName("bubble");
        dieselPriceRandPerLitrePanel.setSizeUndefined(); // Shrink to fit content

        HorizontalLayout dieselPriceRandPerLitreLayout = new HorizontalLayout();
        dieselPriceRandPerLitreLayout.setMargin(true); //
        dieselPriceRandPerLitreLayout.setSizeUndefined(); // Shrink to fit content
        dieselPriceRandPerLitreLayout.addComponent(dTotalDieselPriceRandPerLitreChart);
        // Add item to the Panel
        dieselPriceRandPerLitrePanel.setContent(dieselPriceRandPerLitreLayout);
        // ========

        PanelEfficiency oneMonthEfficiencyPanel = new PanelEfficiency("Service Fleet 1M Efficiency");
        PanelEfficiency threeMonthEfficiencyPanel = new PanelEfficiency("Service Fleet 3M Efficiency");
        PanelEfficiency twelveMonthEfficiencyPanel = new PanelEfficiency("Service Fleet 12M Efficiency");
        PanelEfficiency fuelSpendPanel = new PanelEfficiency("Fuel Spend");
        PanelEfficiency annualFuelSpendPanel = new PanelEfficiency("Annual Fuel Spend: " + chartPeriod);
        //
        EfficiencylLayout efficiencylLayout = new EfficiencylLayout();
        oneMonthEfficiencyPanel.setContent(efficiencylLayout.getEfficiencyLayout(oneMonthEfficiency, oneMonthEfficiencyFlag));
        efficiencylLayout = new EfficiencylLayout();
        threeMonthEfficiencyPanel.setContent(efficiencylLayout.getEfficiencyLayout(threeMonthEfficiency, threeMonthEfficiencyFlag));
        efficiencylLayout = new EfficiencylLayout();
        twelveMonthEfficiencyPanel.setContent(efficiencylLayout.getEfficiencyLayout(twelveMonthEfficiency, twelveMonthEfficiencyFlag));
        //
        final FuelSpendLayout fuelSpendLayout = new FuelSpendLayout();
        // fuelSpendPanel.setContent(fuelSpendLayout.getFuelSpendLayout(fuelSpendPerUnit, fuelSpendPerService, fuelSpendUnitFlag, fuelSpendServiceFlag));
        //
        final AnnualFuelSpendLayout annualFuelSpendLayout = new AnnualFuelSpendLayout();
        annualFuelSpendPanel.setContent(annualFuelSpendLayout.getFuelSpendLayout(annualTotalFuelSpend, serviceTotalFuelSpend, utilityTotalFuelSpend, operationalTotalFuelSpend, nonOperationalTotalFuelSpend, serviceFuelSpendPercentage, utilityFuelSpendPercentage, operationalFuelSpendPercentage, nonOperationalFuelSpendPercentage));
        //
        chart.chartVerticalLayout.removeAllComponents();
        chart.chartVerticalLayout.addComponent(totalTotalFleetFuelSpendPanel);
        chart.chartVerticalLayout.addComponent(dieselPriceRandPerLitrePanel);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(oneMonthEfficiencyPanel);
        horizontalLayout.addComponent(threeMonthEfficiencyPanel);
        horizontalLayout.addComponent(twelveMonthEfficiencyPanel);
//        horizontalLayout.addComponent(fuelSpendPanel);
        horizontalLayout.addComponent(annualFuelSpendPanel);
        chart.chartVerticalLayout.addComponent(horizontalLayout);
        // house cleaning
        grandTotalFuelSpend = BigDecimal.ZERO;
        annualTotalFuelSpend = BigDecimal.ZERO;
        startDate = null;
        endDate = null;
        annualMileageSumAllMsvTrucks = new Integer("0");
        operatingCostTwelveMonthsList.clear();
        operatingCostThreeMonthsList.clear();
    }

    public DCharts createTotalFleetFuelSpendChart() {
        TotalFleetFuelSpendBarChart totalFleetFuelSpendChart = new TotalFleetFuelSpendBarChart();
        return totalFleetFuelSpendChart.createChart(fuelSpendMonthlyCostBeanList, grandTotalFuelSpend);
    }

    public DCharts createDieselPriceRandPerLitreChart() {
        DieselPriceRandPerLitreLineChart dieselPriceRandPerLitreChart = new DieselPriceRandPerLitreLineChart();
        return dieselPriceRandPerLitreChart.createChart(fuelSpendMonthlyCostBeanList);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////    public void clearZeroObjects() {
////        // Clear objects with zeros for  mileage, fuelCost, fuelLitres, .slipNo("0000") in operatingCostTwentyFiveMonthsList and dateRangeOperatingCostList
////        for (OperatingCost operatingCost : operatingCostTwentyFiveMonthsList) {
////            if (!(operatingCost.getSpeedometer() == 0 && operatingCost.getFuelCost().compareTo(BigDecimal.ZERO) == 0 && operatingCost.getFuelLitres().compareTo(Double.parseDouble("0.0")) == 0 && operatingCost.getSlipNo().equals("0000") && operatingCost.getRandPerLitre().compareTo(BigDecimal.ZERO) == 0)) {
////                int index = operatingCostTwentyFiveMonthsList.indexOf(operatingCost);
//////                operatingCostTwentyFiveMonthsList.remove(operatingCost);
////                operatingCostTwentyFiveMonthsList.remove(index);
////            }
////        }
////
//////        for (OperatingCost operatingCost : dateRangeOperatingCostList) {
//////            if (!(operatingCost.getSpeedometer() == 0 && operatingCost.getFuelCost().compareTo(BigDecimal.ZERO) == 0 && operatingCost.getFuelLitres().compareTo(Double.parseDouble("0.0")) == 0 && operatingCost.getSlipNo().equals("0000") && operatingCost.getRandPerLitre().compareTo(BigDecimal.ZERO) == 0)) {
//////                int index = dateRangeOperatingCostList.indexOf(operatingCost);
////////                dateRangeOperatingCostList.remove(operatingCost);
//////                dateRangeOperatingCostList.remove(index);
//////            }
//////        }
////    }

    private void addListeners() {
//        //Register Button Listeners
//        form.save.addClickListener((Button.ClickListener) this);
//        form.edit.addClickListener((Button.ClickListener) this);
//        form.cancel.addClickListener((Button.ClickListener) this);
//        form.update.addClickListener((Button.ClickListener) this);
//        form.delete.addClickListener((Button.ClickListener) this);
        //
        form.startDate.addValueChangeListener((Property.ValueChangeListener) this);
        form.endDate.addValueChangeListener((Property.ValueChangeListener) this);
    }
}
