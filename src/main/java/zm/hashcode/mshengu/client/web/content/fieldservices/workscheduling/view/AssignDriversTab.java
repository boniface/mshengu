/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.view;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import zm.hashcode.mshengu.app.facade.fleet.TruckFacade;
import zm.hashcode.mshengu.app.facade.people.PersonFacade;
import zm.hashcode.mshengu.app.util.validation.OnSubmitValidationHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.WorkSchedulingMenu;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.forms.AssignDriverForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.forms.VehicleInfoForm;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.models.AssignDriversBean;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.models.VehicleInfoBean;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.tables.AssignedDriversTable;
import zm.hashcode.mshengu.domain.fleet.Truck;
import zm.hashcode.mshengu.domain.people.Person;

/**
 *
 * @author Ferox
 */
public class AssignDriversTab extends VerticalLayout implements
        Button.ClickListener, Property.ValueChangeListener {

    private final MshenguMain main;
    private AssignDriverForm form;
    private VehicleInfoForm vehicleInfoForm;
    private AssignedDriversTable table;
    
    private final Button btnExportDriverAllocation;

    public AssignDriversTab(MshenguMain app) {
        main = app;
        form = new AssignDriverForm();
        vehicleInfoForm = new VehicleInfoForm();

        setSizeFull();
        addComponent(vehicleInfoForm);
        addComponent(form);

        vehicleInfoForm.binder.setReadOnly(true);

        btnExportDriverAllocation = new Button("Export Driver Allocation Sheet");
        btnExportDriverAllocation.setSizeFull();
        btnExportDriverAllocation.setStyleName("default extramargin");
        
        table = new AssignedDriversTable(main);
        addComponent(table);
        addComponent(new Label("<hr/>", ContentMode.HTML));
        addComponent(btnExportDriverAllocation);
        table.addValueChangeListener((Property.ValueChangeListener) this);
        addListeners();
    }

    public AssignedDriversTable getTable() {
        return table;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        final Button source = event.getButton();
        if (source == form.save) {
            saveEditedForm(form.binder);
        } else if (source == form.edit) {
            setEditFormProperties();
        } else if (source == form.cancel) {
            getHome();
        } else if (source == btnExportDriverAllocation) {
            exportDriverAllocaationSheet();
        }
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        final Property property = event.getProperty();
        if (property == table) {
//            final Truck truck = TruckFacade.getTruckService().findById(table.getValue().toString());
//            form.binder.setItemDataSource(new BeanItem<>(getBean(truck)));
//            setReadFormProperties();
        }
        if (property == form.truckId) {
            final Truck truck = TruckFacade.getTruckService().findById(form.truckId.getValue().toString());
            vehicleInfoForm.binder.setItemDataSource(new BeanItem<>(getBean(truck)));
//            setReadFormProperties();
        }
    }

    private void saveEditedForm(FieldGroup binder) {
        try {
            binder.commit();
            TruckFacade.getTruckService().merge(assignDriver(binder));
            getHome();
            Notification.show("Record UPDATED!", Notification.Type.TRAY_NOTIFICATION);
        } catch (FieldGroup.CommitException e) {
            Collection<Field<?>> fields = binder.getFields();
            OnSubmitValidationHelper helper = new OnSubmitValidationHelper(fields, form.errorMessage);
            helper.doValidation();
            Notification.show("Please Correct Red Colored Inputs!", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    private Truck assignDriver(FieldGroup binder) {
        //final  Truck cust = new Truck.Builder(binder.getItemDataSource().getItemProperty("name")).
        final Truck existingTruck;
        final Person driver;

        final AssignDriversBean assignDriversBean = ((BeanItem<AssignDriversBean>) binder.getItemDataSource()).getBean();

        try {
            existingTruck = TruckFacade.getTruckService().findById(assignDriversBean.getTruckId());
            driver = PersonFacade.getPersonService().findById(assignDriversBean.getDriverId());

            final Truck truck = new Truck.Builder(existingTruck.getNumberPlate())
                    .truck(existingTruck)
                    .brand(existingTruck.getBrand())
                    .driver(driver)
                    .id(existingTruck.getId())
                    .build();
            return truck;

        } catch (Exception ex) {
            return null;
        }
    }

    private void getHome() {
        main.content.setSecondComponent(new WorkSchedulingMenu(main, "DRIVERS"));
    }

    private void setEditFormProperties() {
        form.binder.setReadOnly(false);
        form.save.setVisible(false);
        form.edit.setVisible(false);
        form.cancel.setVisible(true);
        form.delete.setVisible(false);
        form.update.setVisible(true);
    }

    private void setReadFormProperties() {
        form.binder.setReadOnly(true);
        //Buttons Behaviour
        form.save.setVisible(false);
        form.edit.setVisible(true);
        form.cancel.setVisible(true);
        form.delete.setVisible(true);
        form.update.setVisible(false);
    }

    private void addListeners() {
        //Register Button Listeners
        form.save.addClickListener((Button.ClickListener) this);
        form.edit.addClickListener((Button.ClickListener) this);
        form.cancel.addClickListener((Button.ClickListener) this);
        form.update.addClickListener((Button.ClickListener) this);
        form.delete.addClickListener((Button.ClickListener) this);
        btnExportDriverAllocation.addClickListener((Button.ClickListener) this);
        //Register Table Listerners        
        form.truckId.addValueChangeListener((Property.ValueChangeListener) this);
    }

    private VehicleInfoBean getBean(Truck truck) {
        VehicleInfoBean bean = new VehicleInfoBean();

        bean.setNumberPlate(truck.getNumberPlate());
        bean.setBrand(truck.getBrand());
        bean.setCategoryId(truck.getCategoryId());
        bean.setEngineNo(truck.getEngineNo());
        bean.setIsActive(truck.isIsActive());
        bean.setVehicleNumber(truck.getVehicleNumber());
        bean.setModel(truck.getModel());
        bean.setId(truck.getId());
        return bean;
    }

    
    public void exportDriverAllocaationSheet(){
        table.setReportHeader();
      ExcelExport excelExport = new ExcelExport(table);
                excelExport.setExportFileName(table.getFileName());
                excelExport.setReportTitle(table.getReportHeader());
                excelExport.setDoubleDataFormat("Text");
                excelExport.setDateDataFormat("dd.MM.yyyy");
                excelExport.setDisplayTotals(false);
                excelExport.export();
  }
}
