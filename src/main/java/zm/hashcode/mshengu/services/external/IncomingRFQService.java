/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.services.external;

import java.util.List;
import zm.hashcode.mshengu.domain.external.IncomingRFQ;

/**
 *
 * @author Ferox
 */
public interface IncomingRFQService {

    public List<IncomingRFQ> findAll();

    public void persist(IncomingRFQ incident);

    public void merge(IncomingRFQ incident);

    public IncomingRFQ findById(String id);

    public void delete(IncomingRFQ incident);
    
    
    public List<IncomingRFQ> findAllClosed();
    
    public List<IncomingRFQ> findAllOpen();
        
    public List<IncomingRFQ> findByStatus(String statusName);
    
    public IncomingRFQ findByRefNumber(String refNumber);
    
    public List<IncomingRFQ> findByAcceptedStatus(Boolean acceptedStatus);
    
}
