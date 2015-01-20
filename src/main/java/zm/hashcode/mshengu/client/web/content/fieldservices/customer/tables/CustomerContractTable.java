/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.tables;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import java.util.Set;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.app.util.UITableIconHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.domain.customer.Contract;

/**
 *
 * @author Ferox
 */
public class CustomerContractTable extends Table {

    private final MshenguMain main;
    
    private DateTimeFormatHelper formatHelper = new DateTimeFormatHelper();
    private UITableIconHelper tableIconHelper =  new UITableIconHelper();
    public CustomerContractTable(MshenguMain main) {
        this.main = main;
        setSizeFull();
        addContainerProperty("StartDate", String.class, null);
        addContainerProperty("End Date", String.class, null);
        addContainerProperty("Updated on", String.class, null);
        addContainerProperty("Number of Units", Integer.class, null);
        addContainerProperty("Type", String.class, null);
        addContainerProperty("Active", Embedded.class, null);

 





    }
    
    
    public void loadCustomerContracts(Set<Contract> contracts){
                // Add Data Columns
        removeAllItems();
//        List<Contract> contracts = ContractFacade.getContractService().findAll();
        boolean active = true;
        for (Contract contract : contracts) {
            addItem(new Object[]{formatHelper.getYearMonthDay(contract.getStartDate()),
                        formatHelper.getYearMonthDay(contract.getEndDate()),
                        formatHelper.getYearMonthDay(contract.getDateofUpdate()),
                        contract.getNumberOfUnits(),
                         contract.getContractTypeName(),
                         tableIconHelper.getCheckOrBlank(active)
            }, contract.getId());
            active = false;
        }
        // Allow selecting items from the table.
        setNullSelectionAllowed(false);

        setSelectable(true);
        // Send changes in selection immediately to server.
        setImmediate(true);
    }
}
