/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.models;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
/**
 *
 * @author Ferox
 */
public final class SelectCustomerBean implements Serializable {

    @NotNull
    private String customerId;

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}