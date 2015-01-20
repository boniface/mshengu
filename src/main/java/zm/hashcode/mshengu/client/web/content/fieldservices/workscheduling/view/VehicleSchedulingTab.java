/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.view;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import org.springframework.util.StringUtils;
import zm.hashcode.mshengu.app.facade.fleet.TruckFacade;
import zm.hashcode.mshengu.app.util.validation.OnSubmitValidationHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.WorkSchedulingMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.forms.VehicleSheduleForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.models.VehicleScheduleBean;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables.VehicleSchedulingExportTable;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables.VehicleSchedulingTable;
import zm.hashcode.mshengu.domain.fleet.Truck;

/**
 *
 * @author Ferox
 */
public class VehicleSchedulingTab extends VerticalLayout implements Property.ValueChangeListener {

    private final MshenguMain main;
    private VehicleSheduleForm form;
    private final VehicleSchedulingTable table;    
    private final VehicleSchedulingExportTable tableToExport;
    private final Button btnExportSchedule;

    public VehicleSchedulingTab(MshenguMain app) {
        main = app;
        form = new VehicleSheduleForm();
        table = new VehicleSchedulingTable(main);
        tableToExport = new VehicleSchedulingExportTable(main);
        setSizeFull();
//      
        
        btnExportSchedule = new Button("Export Service Schedule");
        btnExportSchedule.setSizeFull();
        btnExportSchedule.setStyleName("default extramargin");
        addComponent(form);
        addComponent(btnExportSchedule);
        addComponent(btnExportSchedule);
        addComponent(new Label("<hr/>", ContentMode.HTML));
  
        addComponent(table);
        addComponent(tableToExport);
        tableToExport.setVisible(false);

        btnExportSchedule.addClickListener((Button.ClickEvent event) -> {
            try {
                form.binder.commit(); //check for validation before downloading the PDF.
                
                
                ExcelExport excelExport = new ExcelExport(tableToExport);
                excelExport.setExportFileName(tableToExport.getFileName());
                excelExport.setReportTitle(tableToExport.getReportHeader());
                excelExport.setDoubleDataFormat("Text");
                excelExport.setDateDataFormat("dd.MM.yyyy");
                excelExport.setDisplayTotals(true);
                excelExport.export();
            } catch (FieldGroup.CommitException ex) {
                Collection<Field<?>> fields = form.binder.getFields();
                OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
                helper.doValidation();
                Notification.show("Please Correct Red Colored Inputs\nThen try again.", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        form.vehicleNumber.addValueChangeListener((Property.ValueChangeListener) this);
    }
    
    public VehicleSchedulingTable getTable() {
        return table;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        final Property property = event.getProperty();
        if (property == form.vehicleNumber) {
            if (form.vehicleNumber.getValue() != null) {
                System.out.println("Vehicle ID" + form.vehicleNumber.getValue().toString());

                final Truck truck = TruckFacade.getTruckService().findById(form.vehicleNumber.getValue().toString());//                
                String truckName = truck.getVehicleNumber() + " - (" + truck.getNumberPlate() + ")";
                
                if (!StringUtils.isEmpty(truck.getRoutes())) {

                    form.setTruckDetails(truck.getNumberPlate(), truck.getDriverName());
                    table.loadVehicleRoutes(truck.getRoutes());
                    tableToExport.loadVehicleRoutes(truck.getRoutes());
                    form.setContractTotals(table.getContractSites(), table.getContractFrequency(), table.getContractUnits(), table.getContractServices());
                    form.setPrivateTotals(table.getPrivateSites(), table.getPrivateFrequency(), table.getPrivateUnits(), table.getPrivateServices());
                    form.setOtherTotals(table.getOtherSites(), table.getOtherFrequency(), table.getOtherUnits(), table.getOtherServices());
                    form.setGlobalTotals(table.getTotalSites(), table.getTotalFrequency(), table.getTotalUnits(), table.getTotalServices());
                    form.vehicleNumber.removeStyleName("invalid");
                    form.errorMessage.setValue("");
                   
                } else {
                    table.removeAllItems();
                    form.setContractTotals(0, 0, 0, 0);
                    form.setPrivateTotals(0, 0, 0, 0);
                    form.setOtherTotals(0, 0, 0, 0);
                    form.setGlobalTotals(0, 0, 0, 0);
                    form.setTruckDetails("", "");
                }
                
            tableToExport.setReportHeader(truckName, form.lblDriver.getValue());
                
            } else {
                System.out.println("Vehicle ID = NULL " + form.vehicleNumber.getValue());
            }
        }

    }

    private void getHome() {
        main.content.setSecondComponent(new WorkSchedulingMenu(main, "VEHICLE_SCHEDULING"));
    }

    private VehicleScheduleBean getBean(Truck truck) {
        VehicleScheduleBean bean = new VehicleScheduleBean();

        bean.setNumberPlate(truck.getNumberPlate());
        bean.setDriverName(truck.getDriverName());
        return bean;
    }
}
