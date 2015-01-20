/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.test.customer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.testng.annotations.Test;
import zm.hashcode.mshengu.app.util.DateTimeFormatWeeklyHelper;
import zm.hashcode.mshengu.domain.customer.Contract;
import zm.hashcode.mshengu.domain.customer.Customer;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceContractLifeCycle;
import zm.hashcode.mshengu.services.customer.ContractService;
import zm.hashcode.mshengu.services.customer.CustomerService;
import zm.hashcode.mshengu.services.people.PersonService;
import zm.hashcode.mshengu.services.products.SiteService;
import zm.hashcode.mshengu.services.products.SiteServiceContractLifeCycleService;
import zm.hashcode.mshengu.test.AppTest;
import static zm.hashcode.mshengu.test.AppTest.ctx;

/**
 *
 * @author boniface
 */
public class CustomerContractTest extends AppTest {

    @Autowired
    private SiteServiceContractLifeCycleService siteServiceContractLifeCycleService;
    @Autowired
    private PersonService personService;
    @Autowired
    private SiteService siteService;
    @Autowired
    CustomerService customerService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ContractService contractService;
    private String roleId;

    private final DateTimeFormatWeeklyHelper dtfwh = new DateTimeFormatWeeklyHelper();
    
    
        @Test
    private void dateTest(){
        
        String vehicleName = "MSV-05 - (CA 263 564)";
        String driver = "James Milanzi";
        dtfwh.setDate(new Date());
        
        StringBuilder reportHeader = new StringBuilder();
        reportHeader.append("Vehicle : ").append("\t").append(vehicleName).append('\n')
                    .append("Driver  : ").append("\t").append(driver).append('\n')
                    .append("Period  : ").append("\t").append(dtfwh.getMonthForTbaleFormat()).append(" ").append(dtfwh.getYearForTbaleFormat())
                    .append("\t").append("(Week ").append(dtfwh.getWeekForTbaleFormat()).append(")").append("\n")
                    .append("From    : ").append("\t").append(dtfwh.getMondayForTbaleFormat()).append("  To  ").append(dtfwh.getSundayForTbaleFormat())
                    .append("\n").append("\n").append("\n");
                            
                    


            System.out.println(reportHeader);
        
    }
//    @Test
    public void customerContracts() {
        customerService = ctx.getBean(CustomerService.class);
        List<Customer> customerList = customerService.findAll();
//       siteList.subList(fromIndex, toIndex)
        if (customerList != null) {
            int count = customerList.size();
            System.out.println("Customer Count = " + count);
            int x = 1;

            for (Customer customer : customerList) {
                System.out.println("\n=============Site no " + x + "/" + count + "==================");
                System.out.println("Customer Name : " + customer.getName());

                Set<Contract> contractList = customer.getContracts();

                if (contractList != null) {
                    for (Contract contract : contractList) {
                        System.out.println("Contract Date DaOf action " + contract.getDateofAction());
                        System.out.println("Contract Start Date " + contract.getStartDate());
                        System.out.println("Contract  Update Date " + contract.getDateofUpdate());
                        System.out.println("Contract  Type " + contract.getContractTypeName());
                    }
                }
                System.out.println("Last Contract Type" + customer.getLastContactTypeName());
                System.out.println("Last Contract Date of Action Date " + customer.getLastContactDateOfAction());
                x++;
            }
        }

    }

//    @Test
    public void updateCustomerContract() {
        contractService = ctx.getBean(ContractService.class);
        mongoTemplate = ctx.getBean(MongoTemplate.class);

        Query query = new Query(Criteria
                .where("dateofAction").is(null));
//                .andOperator(
//                Criteria.where("serviceDate").gte(serviceDateStart),
//                Criteria.where("serviceDate").lte(serviceDateEnd),
//                Criteria.where("statusMessage").is(statusMessage)));
//        query.with(new Sort(Sort.Direction.DESC, "date")); WITHIN
//        long count = mongoTemplate.find(query, Contract.class, "contract");
        List<Contract> contractList = mongoTemplate.find(query, Contract.class, "contract");

        if (contractList != null) {
            for (Contract contract : contractList) {
                System.out.println("Contract  Parent ID  " + contract.getParentId());
                System.out.println("Contract Date Da Of action (before) " + contract.getDateofAction());
                System.out.println("Contract Start Date " + contract.getStartDate());
                System.out.println("Contract  Update Date " + contract.getDateofUpdate());
                System.out.println("Contract  Type " + contract.getContractTypeName());

                Contract newContrac = new Contract.Builder(new Date())
                        .contract(contract)
                        .build();

                contractService.merge(newContrac);
                System.out.println("Contract Date Da Of action (After) " + newContrac.getDateofAction());

            }
        }

    }

//    @Test
    public void checkCustomer() {
        customerService = ctx.getBean(CustomerService.class);
        List<Customer> customerList = customerService.findAll();
//       siteList.subList(fromIndex, toIndex)
        int aggTotalCust = 0;
        int aggtotalSIte = 0;
        int calcTotalCust = 0;
        int calcTotalSite = 0;

        if (customerList != null) {
            int countCust = customerList.size();
            aggTotalCust = countCust;
            System.out.println("Customer Count = " + countCust);
            int xCust = 1;

            for (Customer customer : customerList) {
                calcTotalCust++;
                System.out.println("\n=============Customer no " + xCust + " of " + countCust + "==================");
                System.out.println("Customer Name : " + customer.getName());
//       siteList.subList(fromIndex, toIndex)

                Set<Site> siteList = customer.getSites();

                if (siteList != null) {
                    int countSite = siteList.size();
                    aggtotalSIte += countSite;

                    int xSite = 1;

                    for (Site site : siteList) {
                        calcTotalSite++;
                        System.out.println("Site no " + xSite + "/" + countSite + " ==>  " + site.getName());//  " || has " + site.getLastSiteServiceContractLifeCycle().getNumberOfUnits() + "out of ");
//                        System.out.println("Site Name : " );
//
//         
                        xSite++;
                    }
//                                        System.out.println("Customer Site Count = " + countSite);
                    xCust++;
                    System.out.println("\nCustomer Total Sites " + countSite);
                } else {
                    System.out.println("Site == NULL");
                    System.out.println("Customer Total Sites " + 0);
                }
            }
            System.out.println("\n===============================\nFinal Totals");
            System.out.println("Agg Customer Total " + aggTotalCust);
            System.out.println("Agg Site Total " + aggtotalSIte);
//            System.out.println("Calc Customer Total " + calcTotalCust);
//            System.out.println("Calc Site Total " + calcTotalSite);
//                System.out.println("Contract  Type " + site.getContractTypeName());
        }
    }
//    Contract contract = contractService.findById("5282f26f823063df45045c53");
//    Contract newContrac = new Contract.Builder(new Date())
//            .contract(contract)
//            .build();
//}
//@Test
    public void updateSietContractLifeCycle() {

        customerService = ctx.getBean(CustomerService.class);
        List<Customer> customerList = customerService.findAll();
//       siteList.subList(fromIndex, toIndex)
        if (customerList != null) {
            int count = customerList.size();
            System.out.println("Customer Count = " + count);
            int x = 1;

            for (Customer customer : customerList) {
                System.out.println("\n=============Site no " + x + "/" + count + "==================");

                String contractType = customer.getLastContactTypeName();
                Set<Site> siteList = customer.getSites();
                System.out.println("Customer Name : " + customer.getName());
                System.out.println("Site Count : " + siteList.size());
                System.out.println("Contract Type : " + contractType);
                updateSiteContractLifeCycle(siteList, contractType);

                x++;
            }
        }

    }

    private void updateSiteContractLifeCycle(Set<Site> siteList, String contractType) {
        siteServiceContractLifeCycleService = ctx.getBean(SiteServiceContractLifeCycleService.class);

        for (Site site : siteList) {
            if (site != null) {
                if (site.getLastSiteServiceContractLifeCycle() != null) {
                    SiteServiceContractLifeCycle siteServiceContractLifeCycle = site.getLastSiteServiceContractLifeCycle();
                    SiteServiceContractLifeCycle newSiteServiceContractLifeCycle = new SiteServiceContractLifeCycle.Builder(siteServiceContractLifeCycle.getDateofAction())
                            .siteServiceContractLifeCycle(siteServiceContractLifeCycle)
                            .parentId(site.getName())
                           // .contractType(contractType)
                            .build();
                    siteServiceContractLifeCycleService.merge(newSiteServiceContractLifeCycle);

                }
            }
        }
//                System.out.println("Last Contract Type" + customer.getLastContactTypeName());
//                System.out.println("Last Contract Date of Action Date " + customer.getLastContactDateOfAction());
    }
}
