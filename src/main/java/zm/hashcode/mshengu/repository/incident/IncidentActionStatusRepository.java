/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.repository.incident;

import org.springframework.data.repository.PagingAndSortingRepository;
import zm.hashcode.mshengu.domain.incident.IncidentActionStatus;

/**
 *
 * @author Ferox
 */
public interface IncidentActionStatusRepository extends PagingAndSortingRepository<IncidentActionStatus, String> {


}
