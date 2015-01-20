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
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.forms.WorkSchedulingForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables.WorkSchedulingTable;
import zm.hashcode.mshengu.domain.fleet.Truck;

/**
 *
 * @author Ferox
 */
public class WorkSchedulingTab extends VerticalLayout implements Property.ValueChangeListener {

    private final MshenguMain main;
    private final WorkSchedulingForm form;
    private final WorkSchedulingTable table;
    private final Button btnExportSchedule;

    public WorkSchedulingTab(MshenguMain app) {
        main = app;
        form = new WorkSchedulingForm();
        table = new WorkSchedulingTable(main);
        setSizeFull();
        
        btnExportSchedule = new Button("Export Service Schedule");
        btnExportSchedule.setSizeFull();
        btnExportSchedule.setStyleName("default extramargin");
        addComponent(form);
        addComponent(btnExportSchedule);
        addComponent(new Label("<hr/>", ContentMode.HTML));
        addComponent(table); 
        
//        form.binder.setReadOnly(true);
        btnExportSchedule.addClickListener((Button.ClickEvent event) -> {
            try {
                form.binder.commit(); //check for validation before downloading the PDF.
                
                ExcelExport excelExport = new ExcelExport(table);                
                excelExport.setExportFileName(table.getFileName());
                excelExport.setReportTitle(table.getReportHeader());
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
        form.truckId.addValueChangeListener((Property.ValueChangeListener) this);
        form.driverName.addValueChangeListener((Property.ValueChangeListener) this);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        final Property property = event.getProperty();
        if (property == form.truckId) {
            final Truck truck = TruckFacade.getTruckService().findById(form.truckId.getValue().toString());
            String truckName = truck.getVehicleNumber() + " - (" + truck.getNumberPlate() + ")";
            if (!StringUtils.isEmpty(truck.getDriver())) {
                form.setDriversName(truck.getDriver());
                form.driverName.removeStyleName("invalid");                 
            }
            if (!StringUtils.isEmpty(truck.getRoutes())) {
                table.loadVehicleRoutes(truck.getRoutes());
                form.truckId.removeStyleName("invalid");
                form.errorMessage.setValue("");
//                exportPdf.setVisible(false);
//                export.setVisible(true);
            }else{
                table.clearTable();
//                export.setVisible(false);
            }
            table.setReportHeader(truckName, form.driverName.getValue());
        }
//        if (property == form.driverName) {            
//            if (form.driverName.getValue().equals("")) {                
//                exportPdf.setVisible(true);
////                export.setVisible(false);
//            }else{
//                exportPdf.setVisible(false);
////                export.setVisible(true);
//            }
//        }
    }

    private void getHome() {
        main.content.setSecondComponent(new WorkSchedulingMenu(main, "LANDING"));
    }
}
