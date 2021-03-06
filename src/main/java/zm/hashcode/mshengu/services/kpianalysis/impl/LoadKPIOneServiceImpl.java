/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.services.kpianalysis.impl;

import com.google.common.collect.Collections2;
import com.google.gwt.thirdparty.guava.common.collect.ImmutableList;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zm.hashcode.mshengu.app.facade.customer.CustomerFacade;
import zm.hashcode.mshengu.app.util.predicates.customer.ContractTypePredicate;
import zm.hashcode.mshengu.domain.customer.Customer;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceLog;
import zm.hashcode.mshengu.repository.customer.CustomerRepository;
import zm.hashcode.mshengu.services.kpianalysis.LoadKPIOneService;

/**
 *
 * @author Luckbliss
 */
//Field Services (Contract)
//@Service
public class LoadKPIOneServiceImpl implements LoadKPIOneService {

    private List<Customer> customers;

    public LoadKPIOneServiceImpl() {
        customers = initialise("Contract");
    }

    private List<Customer> initialise(String contractTypeName) {
        return CustomerFacade.getCustomerService().findByContractType(contractTypeName);
    }

    @Override
    public double getNoServicesPerformed(String month, int year) {
        double number = 0;
        for (Customer customer : customers) {
            Set<Site> sites = customer.getSites();
            for (Site site : sites) {
                Set<SiteServiceLog> logs = site.getSiteServiceLog();
                for (SiteServiceLog log : logs) {
                    String logmonth = new SimpleDateFormat("MMMM").format(log.getServiceDate());
                    int logyear = Integer.parseInt(new SimpleDateFormat("YYYY").format(log.getServiceDate()));
                    if (logmonth.equals(month) && logyear == year) {
                        number += log.getNumberOfUnitsServiced();
                    }
                }
            }
        }
        return number;
    }

    @Override
    public double getNoServicesNotCompleted(String month, int year) {
        double number = 0;
        for (Customer customer : customers) {
            Set<Site> sites = customer.getSites();
            for (Site site : sites) {
                Set<SiteServiceLog> logs = site.getSiteServiceLog();
                for (SiteServiceLog log : logs) {
                    String logmonth = new SimpleDateFormat("MMMM").format(log.getServiceDate());
                    int logyear = Integer.parseInt(new SimpleDateFormat("YYYY").format(log.getServiceDate()));
                    if (logmonth.equals(month) && logyear == year) {
                        number += log.getNumberOfUnitsNotServiced();
                    }
                }
            }
        }
        return number;
    }

    @Override
    public double getUncompletedPercentage(String month, int year) {
        double performed = getNoServicesPerformed(month, year);
        double notperformed = getNoServicesNotCompleted(month, year);
        return notperformed / (notperformed + performed) * 100;
    }

    @Override
    public double getUnitDeployment(String month, int year) {
        double number = 0;
//        for (Customer customer : customers) {
//            Set<Site> sites = customer.getSites();
//            for (Site site : sites) {                
//                site.getNumberOfTotalUnits();
//                Set<SiteServiceLog> logs = site.getSiteServiceLog();
//                for (SiteServiceLog log : logs) {
//                    String logmonth = new SimpleDateFormat("MMMM").format(log.getServiceDate());
//                    int logyear = Integer.parseInt(new SimpleDateFormat("YYYY").format(log.getServiceDate()));
//                    if (logmonth.equals(month) && logyear == year) {
//                        number += log.getNumberOfUnitsNotServiced();
//                    }
//                }
//            }
//        }
        return number;
    }

    @Override
    public double getCompletedPercentage(String month, int year) {
        double performed = getNoServicesPerformed(month, year);
        double notperformed = getNoServicesNotCompleted(month, year);
        return performed / (notperformed + performed) * 100;
    }
}
