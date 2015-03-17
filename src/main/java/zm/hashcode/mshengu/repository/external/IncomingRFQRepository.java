/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.repository.external;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import zm.hashcode.mshengu.domain.external.IncomingRFQ;

/**
 *
 * @author Ferox
 */
public interface IncomingRFQRepository extends PagingAndSortingRepository<IncomingRFQ, String> {
   public IncomingRFQ findByRefNumber(String refNumber);
   public List<IncomingRFQ> findByAcceptedStatusOrderByDateOfActionDesc(Boolean acceptedStatus);
}
