/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.services.fieldservices.Impl;

import com.google.gwt.thirdparty.guava.common.collect.ImmutableList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import zm.hashcode.mshengu.domain.products.LogEventsEnum;
import zm.hashcode.mshengu.domain.products.LogSiteEvents;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.repository.products.LogSiteEventsRepository;
import zm.hashcode.mshengu.services.fieldservices.LogSiteEventService;

/**
 *
 * @author boniface
 */
@Service
public class LogSiteEventServiceImpl implements LogSiteEventService {

    @Autowired
    private LogSiteEventsRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Override
    public List<LogSiteEvents> findAll() {
        return ImmutableList.copyOf(repository.findAll(sortByServiceDate()));
    }
    
    

//    @Override
//    public Site findBySiteId(String SiteId) {
//        return repository.findBySiteId(SiteId);
//    }
    
//    @Override
//     public Site findBySiteName(String siteName){
//         return repository.findBySiteName(siteName);
//     }
    
    private Sort sortByServiceDate() {
        return new Sort(
                new Sort.Order(Sort.Direction.DESC, "date"));
    }

    @Override
    public boolean areLogsEven(String siteId) {
        Query query = new Query(Criteria.where("siteId").is(siteId));
        long count = mongoTemplate.count(query, "logSiteEvents");
        if (count % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String firstUnitLogEvent(LogSiteEvents logSiteEvents) {

        if (areLogsEven(logSiteEvents.getSiteId())) {
            if (LogEventsEnum.START.name().equalsIgnoreCase(logSiteEvents.getAction())) {
                repository.save(logSiteEvents);
                return LogEventsEnum.ACCEPT.name();
            }
            return LogEventsEnum.REJECT.name();
        } else {
            return LogEventsEnum.REJECT.name();
        }
    }

    @Override
    public String lastUnitLogEvent(LogSiteEvents logSiteEvents) {
        if (!areLogsEven(logSiteEvents.getSiteId())) {
            if (LogEventsEnum.FINISH.name().equalsIgnoreCase(logSiteEvents.getAction())) {
                repository.save(logSiteEvents);
                return LogEventsEnum.ACCEPT.name();
            }
            return LogEventsEnum.REJECT.name();
        } else {
            return LogEventsEnum.REJECT.name();
        }
    }

    @Override
    public String getLatestUnitIDWithStartAction(String truckId, String siteId) {
        Query query = new Query(Criteria
                .where("truckId").is(truckId)
                .andOperator(
                Criteria.where("action").is(LogEventsEnum.START.name()),
                Criteria.where("siteId").is(siteId)));
        query.with(new Sort(Sort.Direction.DESC, "date"));
        List<LogSiteEvents> events = mongoTemplate.find(query, LogSiteEvents.class);
        LogSiteEvents event = events.get(0);
        String latestUnitId = getUinitId(event);
        return latestUnitId;
    }

    private String getUinitId(LogSiteEvents event) {
        if (event != null) {
            return event.getUnitId();
        }
        return null;
    }
}
