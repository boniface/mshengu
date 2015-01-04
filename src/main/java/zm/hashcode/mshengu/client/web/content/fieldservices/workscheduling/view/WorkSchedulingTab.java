/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.view;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import org.springframework.util.StringUtils;
import org.vaadin.haijian.PdfExporter;
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
//    private final VehicleInfoForm vehicleInfoForm;
    private final WorkSchedulingTable table;
    private final PdfExporter export;
    private final Button exportPdf;

    public WorkSchedulingTab(MshenguMain app) {
        main = app;
        form = new WorkSchedulingForm();
        table = new WorkSchedulingTable(main);
//        vehicleInfoForm = new VehicleInfoForm();
        setSizeFull();
//        addComponent(vehicleInfoForm);
        exportPdf = new Button("Export PDF");
        export = new PdfExporter(table);
        export.setCaption("Export PDF");
        addComponent(form);
        addComponent(exportPdf);
        addComponent(export);
        export.setVisible(false);
        addComponent(table); 
        
//        form.binder.setReadOnly(true);
        exportPdf.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.binder.commit(); //check for validation before downloading the PDF.
                } catch (FieldGroup.CommitException ex) {
                    Collection<Field<?>> fields = form.binder.getFields();
                    OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
                    helper.doValidation();
                    Notification.show("Please Correct Red Colored Inputs\nThen try again.", Notification.Type.TRAY_NOTIFICATION);                    
                }
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
            if (!StringUtils.isEmpty(truck.getDriver())) {
                form.setDriversName(truck.getDriver());
                form.driverName.removeStyleName("invalid");                 
            }
            if (!StringUtils.isEmpty(truck.getRoutes())) {
                table.loadVehicleRoutes(truck.getRoutes());
                form.truckId.removeStyleName("invalid");
                form.errorMessage.setValue("");
                exportPdf.setVisible(false);
                export.setVisible(true);
            }else{
                table.clearTable();
                export.setVisible(false);
            }
        }
        if (property == form.driverName) {            
            if (form.driverName.getValue().equals("")) {                
                exportPdf.setVisible(true);
                export.setVisible(false);
            }else{
                exportPdf.setVisible(false);
                export.setVisible(true);
            }
        }
    }

    private void getHome() {
        main.content.setSecondComponent(new WorkSchedulingMenu(main, "LANDING"));
    }
}
