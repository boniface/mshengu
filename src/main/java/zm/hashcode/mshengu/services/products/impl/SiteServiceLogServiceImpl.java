/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.services.products.impl;

import com.google.gwt.thirdparty.guava.common.collect.ImmutableList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import zm.hashcode.mshengu.app.util.DateTimeFormatWeeklyHelper;
import zm.hashcode.mshengu.domain.products.SiteServiceLog;
import zm.hashcode.mshengu.domain.products.SiteServiceLogStatusEnum;
import zm.hashcode.mshengu.domain.products.UnitServiceLog;
import zm.hashcode.mshengu.repository.products.SiteServiceLogRepository;
import zm.hashcode.mshengu.services.products.SiteServiceLogService;

/**
 *
 * @author Luckbliss
 */
@Service
public class SiteServiceLogServiceImpl implements SiteServiceLogService {

    @Autowired
    private SiteServiceLogRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;
    private DateTimeFormatWeeklyHelper dtfwh = new DateTimeFormatWeeklyHelper();

    @Override
    public List<SiteServiceLog> findAll() {
        return ImmutableList.copyOf(repository.findAll(sortByServiceDate()));
    }

    @Override
    public void persist(SiteServiceLog siteServiceLog) {

        repository.save(siteServiceLog);

    }

    @Override
    public void merge(SiteServiceLog siteServiceLog) {
        if (siteServiceLog.getId() != null) {
            repository.save(siteServiceLog);
        }
    }

    @Override
    public SiteServiceLog findById(String id) {
        try {
            return repository.findOne(id);
        } catch (IllegalArgumentException iaEx) {
            return null;
        }
    }

    @Override
    public void delete(SiteServiceLog siteServiceLog) {
        repository.delete(siteServiceLog);
    }

    private Sort sortByServiceDate() {
        return new Sort(
                new Sort.Order(Sort.Direction.DESC, "serviceDate"));
    }

    /*
     * the query in this method lacks a criteria, it should filter distinct unit id's cos a unit should not be serviced more than once in a day.
     */
    /**
     *
     * @param siteName
     * @param statusMessage WITHIN or User Away From UNIT
     * @param serviceDateStart
     * @param serviceDateEnd
     * @return long count
     */
    @Override
    public long getTotalUnitsServiced(String siteName, String statusMessage, Date serviceDateStart, Date serviceDateEnd) {
        Query query = new Query(Criteria
                .where("siteName").is(siteName)
                .andOperator(
                        Criteria.where("serviceDate").gte(serviceDateStart),
                        Criteria.where("serviceDate").lte(serviceDateEnd),
                        Criteria.where("statusMessage").is(statusMessage)));
//        query.with(new Sort(Sort.Direction.DESC, "date")); WITHIN
        long count = mongoTemplate.count(query, "unitServiceLog");
        return count;

    }

    @Override
    public List<SiteServiceLog> getOutdatedOpenLogs(Date date) {
        dtfwh.setDate(date);
        Query query = new Query(Criteria
                .where("status").is(SiteServiceLogStatusEnum.OPEN.name())
                .andOperator(
                        //                Criteria.where("serviceDate").gte(serviceDateStart),
                        Criteria.where("serviceDate").lte(dtfwh.getMondayDateFull())));
//                Criteria.where("statusMessage").is("WITHIN")));
//        query.with(new Sort(Sort.Direction.DESC, "date"));
        List<SiteServiceLog> siteServiceLog = mongoTemplate.find(query, SiteServiceLog.class, "siteServiceLog");
        return siteServiceLog;

    }

    @Override
    public List<SiteServiceLog> getSiteServiceLogsException(String siteName, Date startDate, Date endDate, String serviceStatus) {
//        dtfwh.setDate(date);
        Query query = new Query(Criteria
                .where("parentId").is(siteName)
                .andOperator(
                        Criteria.where("serviceDate").gte(startDate),
                        Criteria.where("serviceDate").lte(endDate),
                        Criteria.where("serviceStatus").is(serviceStatus)));
//                Criteria.where("statusMessage").is("WITHIN")));
        query.with(new Sort(Sort.Direction.DESC, "serviceDate"));
        List<SiteServiceLog> siteServiceLog = mongoTemplate.find(query, SiteServiceLog.class, "siteServiceLog");
        return siteServiceLog;

    }

    @Override
    public List<SiteServiceLog> getAllSiteServiceLogs(String siteName, Date startDate, Date endDate) {
        dtfwh.setDate(endDate);
        Query query = new Query(Criteria
                .where("parentId").is(siteName)
                .andOperator(
                        Criteria.where("serviceDate").gte(startDate),
                        Criteria.where("serviceDate").lt(dtfwh.getTomorrowsDate_No_HTMSM())));
//                Criteria.where("serviceStatus").is(serviceStatus)));
//                Criteria.where("statusMessage").is("WITHIN")));
        query.with(new Sort(Sort.Direction.DESC, "serviceDate"));
        List<SiteServiceLog> siteServiceLog = mongoTemplate.find(query, SiteServiceLog.class, "siteServiceLog");
        return siteServiceLog;

    }

    @Override
    public String getServiceByTruckId(String siteName, String statusMessage, Date startDate, Date endDate) {
        dtfwh.setDate(endDate);
        Query query = new Query(Criteria
                .where("parentId").is(siteName)
                .andOperator(
                        Criteria.where("serviceDate").gte(startDate),
                        Criteria.where("serviceDate").lte(endDate),
                        Criteria.where("statusMessage").is(statusMessage)));
//         query.limit(1);
        UnitServiceLog siteUnitServiceLog = mongoTemplate.findOne(query, UnitServiceLog.class, "unitServiceLog");
        String truckId = "N/A";
        if (siteUnitServiceLog != null) {
            truckId = siteUnitServiceLog.getServicedBy();
        }
        return truckId;

    }

    @Override
    public List<SiteServiceLog> getAllServiceLogsException(Date startDate, Date endDate, String serviceStatus) {
//        dtfwh.setDate(date);
        Query query = new Query(Criteria
                .where("serviceStatus").is(serviceStatus)
                .andOperator(
                        Criteria.where("serviceDate").gte(startDate),
                        Criteria.where("serviceDate").lte(endDate)));
//                Criteria.where("serviceStatus").is(serviceStatus)));
//                Criteria.where("statusMessage").is("WITHIN")));
//        query.with(new Sort(Sort.Direction.DESC, "date"));
        List<SiteServiceLog> siteServiceLog = mongoTemplate.find(query, SiteServiceLog.class, "siteServiceLog");
        return siteServiceLog;
    }

    @Override
    public List<SiteServiceLog> getAllServiceLogs(Date startDate, Date endDate) {
//        dtfwh.setDate(date);
        Query query = new Query(Criteria
                .where("serviceDate").gte(startDate)
                .andOperator(
                        //                Criteria.where("serviceDate").gte(startDate),
                        Criteria.where("serviceDate").lte(endDate)));
//                Criteria.where("serviceStatus").is(serviceStatus)));
//                Criteria.where("statusMessage").is("WITHIN")));
//        query.with(new Sort(Sort.Direction.DESC, "date"));
        List<SiteServiceLog> siteServiceLog = mongoTemplate.find(query, SiteServiceLog.class, "siteServiceLog");
        return siteServiceLog;
    }
}
