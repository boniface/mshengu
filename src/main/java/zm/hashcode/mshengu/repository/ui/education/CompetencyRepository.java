/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.repository.ui.education;

import java.util.List;
import java.util.Set;
import org.springframework.data.repository.PagingAndSortingRepository;
import zm.hashcode.mshengu.domain.ui.education.Competency;

/**
 *
 * @author lucky
 */
public interface CompetencyRepository extends PagingAndSortingRepository<Competency, String>{
    
}
