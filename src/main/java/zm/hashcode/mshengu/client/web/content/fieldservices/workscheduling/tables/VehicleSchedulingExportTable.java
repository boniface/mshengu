/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables;

import com.vaadin.ui.Table;
import java.util.Date;
import java.util.List;
import zm.hashcode.mshengu.app.facade.customer.CustomerFacade;
import zm.hashcode.mshengu.app.util.DateTimeFormatWeeklyHelper;
import zm.hashcode.mshengu.app.util.UITableIconHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.customer.Contract;
import zm.hashcode.mshengu.domain.customer.Customer;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceContractLifeCycle;
import zm.hashcode.mshengu.domain.ui.location.Location;

/**
 *
 * @author boniface
 */
public class VehicleSchedulingExportTable extends Table {

    private final MshenguMain main;
    private UITableIconHelper uITableIconHelper = new UITableIconHelper();
    private int privateSites;
    private int privateFrequency;
    private int privateUnits;
    private int privateServices;
    private int contractSites;
    private int contractFrequency;
    private int contractUnits;
    private int contractServices;
    private int otherSites;
    private int otherFrequency;
    private int otherUnits;
    private int otherServices;
    private int totalSites;
    private int totalFrequency;
    private int totalUnits;
    private int totalServices;

    private final DateTimeFormatWeeklyHelper dtfwh = new DateTimeFormatWeeklyHelper();
    private String fileName = "";
    private StringBuilder reportHeader;

    public VehicleSchedulingExportTable(MshenguMain main) {
        this.main = main;
        setSizeFull();

        addContainerProperty("Customer Name", String.class, null);
        addContainerProperty("Site Name", String.class, null);
        addContainerProperty("Region", String.class, null);
        addContainerProperty("Suburb", String.class, null);
        addContainerProperty("Street Address", String.class, null);
        addContainerProperty("Frequency", Integer.class, null);
        addContainerProperty("NO of Units", Integer.class, null);
        addContainerProperty("Weekly Services", Integer.class, null);
        addContainerProperty("Mon", String.class, null);
        addContainerProperty("Tue", String.class, null);
        addContainerProperty("Wed", String.class, null);
        addContainerProperty("Thu", String.class, null);
        addContainerProperty("Fri", String.class, null);
        addContainerProperty("Sat", String.class, null);
        addContainerProperty("Sun", String.class, null);
        //addContainerProperty("Contact Number", String.class, null);

        // Add Data Columns
    }

    public void loadVehicleRoutes(List<Site> vehicleSites) {
        setNullSelectionAllowed(true);
        setSelectable(false);
        setImmediate(false);
        removeAllItems();

        privateSites = 0;
        privateFrequency = 0;
        privateUnits = 0;
        privateServices = 0;
        contractSites = 0;
        contractFrequency = 0;
        contractUnits = 0;
        contractServices = 0;
        otherSites = 0;
        otherFrequency = 0;
        otherUnits = 0;
        otherServices = 0;
        totalSites = 0;
        totalFrequency = 0;
        totalUnits = 0;
        totalServices = 0;

        for (Site site : vehicleSites) {
            String customerName = "";
            String siteName = site.getName();
            String suburbName = site.getLocationName();
            String regionName = getParentLocationName(site.getLocation());
            String address = site.getAddressStreetAddress();
            int units = 0;
            int frequency = 0;
            int services = 0;

            String mondayExp = null;
            String tuesdayExp = null;
            String wednesdayExp = null;
            String thursdayExp = null;
            String fridayExp = null;
            String saturdayExp = null;
            String sundayExp = null;

            SiteServiceContractLifeCycle contractLifeCycle = site.getLastSiteServiceContractLifeCycle();
            if (contractLifeCycle != null) {
                units = contractLifeCycle.getExpectedNumberOfUnits();
                frequency = contractLifeCycle.getFrequency();
                services = contractLifeCycle.getWeeklyServices();
                //TO ADD ON THE INVISIBLE TABLE TO EXPORT
                mondayExp = uITableIconHelper.getVisitUnicodeIcon(contractLifeCycle.isSunday(), contractLifeCycle.isMonday(), contractLifeCycle.getExpectedNumberOfUnits());
                tuesdayExp = uITableIconHelper.getVisitUnicodeIcon(contractLifeCycle.isMonday(), contractLifeCycle.isTuesday(), contractLifeCycle.getExpectedNumberOfUnits());
                wednesdayExp = uITableIconHelper.getVisitUnicodeIcon(contractLifeCycle.isTuesday(), contractLifeCycle.isWednesday(), contractLifeCycle.getExpectedNumberOfUnits());
                thursdayExp = uITableIconHelper.getVisitUnicodeIcon(contractLifeCycle.isWednesday(), contractLifeCycle.isThursday(), contractLifeCycle.getExpectedNumberOfUnits());
                fridayExp = uITableIconHelper.getVisitUnicodeIcon(contractLifeCycle.isThursday(), contractLifeCycle.isFriday(), contractLifeCycle.getExpectedNumberOfUnits());
                saturdayExp = uITableIconHelper.getVisitUnicodeIcon(contractLifeCycle.isFriday(), contractLifeCycle.isSaturday(), contractLifeCycle.getExpectedNumberOfUnits());
                sundayExp = uITableIconHelper.getVisitUnicodeIcon(contractLifeCycle.isSaturday(), contractLifeCycle.isSunday(), contractLifeCycle.getExpectedNumberOfUnits());
            }

            Customer customer = null;
            if (site.getParentId() != null) {
                customer = CustomerFacade.getCustomerService().findById(site.getParentId());
            }
            if (customer != null) {
                customerName = customer.getName();
                Contract contract = customer.getLastContract();// CustomerFacade.getCustomerService().getSitetCurrentContract(site.getParentId());
                if (contract != null) {
                    if (contract.getContractTypeName().equalsIgnoreCase("CONTRACT")) {
                        addToContract(frequency, units, services);

                    } else if (contract.getContractTypeName().equalsIgnoreCase("PRIVATE HIRE")) {
                        addToPrivate(frequency, units, services);
                    } else {
                        addToOther(frequency, units, services);
                    }
                } else {
                    addToOther(frequency, units, services);
                }

            } else {
                addToOther(frequency, units, services);
            }

            addItem(new Object[]{customerName, siteName, suburbName,
                regionName, address, frequency, units, services,
                mondayExp, tuesdayExp, wednesdayExp, thursdayExp, fridayExp, saturdayExp,
                sundayExp,}, site.getId());
        }

        // Allow selecting items from the table.
        setNullSelectionAllowed(false);
        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);

    }

    private void addToContract(int frequency, int units, int services) {
        contractSites += 1;
        contractFrequency += frequency;
        contractUnits += units;
        contractServices += services;
    }

    private void addToPrivate(int frequency, int units, int services) {
        privateSites += 1;
        privateFrequency += frequency;
        privateUnits += units;
        privateServices += services;
    }

    private void addToOther(int frequency, int units, int services) {
        otherSites += 1;
        otherFrequency += frequency;
        otherUnits += units;
        otherServices += services;

    }

    private String getParentLocationName(Location location) {
        if (location != null) {
            return location.getParentLocationName();
        } else {
            return null;
        }

    }

    /**
     * @return the privateSites
     */
    public int getPrivateSites() {
        return privateSites;
    }

    /**
     * @return the privateFrequency
     */
    public int getPrivateFrequency() {
        return privateFrequency;
    }

    /**
     * @return the privateUnits
     */
    public int getPrivateUnits() {
        return privateUnits;
    }

    /**
     * @return the privateServices
     */
    public int getPrivateServices() {
        return privateServices;
    }

    /**
     * @return the contractSites
     */
    public int getContractSites() {
        return contractSites;
    }

    /**
     * @return the contractFrequency
     */
    public int getContractFrequency() {
        return contractFrequency;
    }

    /**
     * @return the contractUnits
     */
    public int getContractUnits() {
        return contractUnits;
    }

    /**
     * @return the contractServices
     */
    public int getContractServices() {
        return contractServices;
    }

    /**
     * @return the otherSites
     */
    public int getOtherSites() {
        return otherSites;
    }

    /**
     * @return the otherFrequency
     */
    public int getOtherFrequency() {
        return otherFrequency;
    }

    /**
     * @return the otherUnits
     */
    public int getOtherUnits() {
        return otherUnits;
    }

    /**
     * @return the otherServices
     */
    public int getOtherServices() {
        return otherServices;
    }

    /**
     * @return the totalSites
     */
    public int getTotalSites() {
        return totalSites + getPrivateSites() + getContractSites() + getOtherSites();
    }

    /**
     * @return the totalFrequency
     */
    public int getTotalFrequency() {
        return totalFrequency = getPrivateFrequency() + getContractFrequency() + getOtherFrequency();
    }

    /**
     * @return the totalUnits
     */
    public int getTotalUnits() {
        return totalUnits = getPrivateUnits() + getContractUnits() + getOtherUnits();
    }

    /**
     * @return the totalServices
     */
    public int getTotalServices() {
        return totalServices = getPrivateServices() + getContractServices() + getOtherServices();
    }

    public void setReportHeader(String vehicleName, String driverName) {
        dtfwh.setDate(new Date());
        reportHeader = new StringBuilder();
        reportHeader.append("Vehicle : ").append("\t").append(vehicleName).append('\t')
                .append("Driver  : ").append("\t").append(driverName).append('\n')
                .append("Service Schedule").append("\t").append(dtfwh.getMondayForTbaleFormat())
                .append(" / ").append(dtfwh.getSundayForTbaleFormat());

        fileName = vehicleName + "_Schedule_From_" + dtfwh.getMondayForTbaleFormat() + "_To_" + dtfwh.getSundayForTbaleFormat() + ".xls";
    }

    public String getFileName() {
        if (fileName.equals("")) {
            return "_" + dtfwh.getMondayForTbaleFormat() + ".xls";
        } else {
            return fileName;
        }
    }

    public String getReportHeader() {
        return reportHeader.toString();
    }

   
}
