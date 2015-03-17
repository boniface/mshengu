/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.services.external.impl;

import com.google.common.collect.Collections2;
import com.google.gwt.thirdparty.guava.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zm.hashcode.mshengu.app.util.predicates.incomingrfq.IncomingRFQActivationStatusPredicate;
import zm.hashcode.mshengu.app.util.predicates.incomingrfq.IncomingRFQStatusPredicate;
import zm.hashcode.mshengu.domain.external.IncomingRFQ;
import zm.hashcode.mshengu.repository.external.IncomingRFQRepository;
import zm.hashcode.mshengu.services.external.IncomingRFQService;

/**
 *
 * @author Ferox
 */
@Service
public class IncomingRFQServiceImpl implements IncomingRFQService {

    @Autowired
    private IncomingRFQRepository repository;

    @Override
    public List<IncomingRFQ> findAll() {
        return ImmutableList.copyOf(repository.findAll(sortByDate()));
    }
    
    
    @Override
    public List<IncomingRFQ>  findByAcceptedStatus(Boolean acceptedStatus) {
        return ImmutableList.copyOf(repository.findByAcceptedStatusOrderByDateOfActionDesc(acceptedStatus));
    }

    @Override
    public void persist(IncomingRFQ incomingRFQ) {

        repository.save(incomingRFQ);

    }

    @Override
    public void merge(IncomingRFQ incomingRFQ) {
        if (incomingRFQ.getId() != null) {
            repository.save(incomingRFQ);
        }
    }

    @Override
    public IncomingRFQ findById(String id) {
        try {
            return repository.findOne(id);
        } catch (IllegalArgumentException iaEx) {
            return null;
        }
    }

    @Override
    public void delete(IncomingRFQ incomingRFQ) {
        repository.delete(incomingRFQ);
    }

    private Sort sortByDate() {
        return new Sort(
                new Sort.Order(Sort.Direction.DESC, "dateOfAction"));
    }

    @Override
    public IncomingRFQ findByRefNumber(String refNumber) {
        return repository.findByRefNumber(refNumber);
    }

    @Override
    public List<IncomingRFQ> findAllClosed() {
        List<IncomingRFQ> incomingRFQList = (List<IncomingRFQ>) repository.findAll(sortByDate());
        Collection<IncomingRFQ> incomingRFQsFilteredList = Collections2.filter(incomingRFQList, new IncomingRFQActivationStatusPredicate(true));
        return ImmutableList.copyOf(incomingRFQsFilteredList);
//        return ImmutableList.copyOf();
    }

    @Override
    public List<IncomingRFQ> findAllOpen() {
        List<IncomingRFQ> incomingRFQList = (List<IncomingRFQ>) repository.findAll(sortByDate());
        Collection<IncomingRFQ> incomingRFQsFilteredList = Collections2.filter(incomingRFQList, new IncomingRFQActivationStatusPredicate(false));
        return ImmutableList.copyOf(incomingRFQsFilteredList);
//        return ImmutableList.copyOf();
    }

    @Override
    public List<IncomingRFQ> findByStatus(String statusName) {
        List<IncomingRFQ> incomingRFQList = (List<IncomingRFQ>) repository.findAll(sortByDate());
        Collection<IncomingRFQ> incomingRFQsFilteredList = Collections2.filter(incomingRFQList, new IncomingRFQStatusPredicate(statusName));
        return ImmutableList.copyOf(incomingRFQsFilteredList);
//        return ImmutableList.copyOf();
    }
}
