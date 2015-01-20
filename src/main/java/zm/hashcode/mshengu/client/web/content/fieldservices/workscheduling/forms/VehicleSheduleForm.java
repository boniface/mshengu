/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.forms;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import zm.hashcode.mshengu.app.util.UIComboBoxHelper;
import zm.hashcode.mshengu.app.util.UIComponentHelper;
import zm.hashcode.mshengu.app.util.validation.UIValidatorHelper;
import zm.hashcode.mshengu.client.web.content.fieldservices.workscheduling.models.VehicleScheduleBean;

/**
 *
 * @author Ferox
 */
public class VehicleSheduleForm extends FormLayout {

    private UIComponentHelper UIComponent = new UIComponentHelper();
    private UIComboBoxHelper UIComboBox = new UIComboBoxHelper();
    private final VehicleScheduleBean bean;
    public final BeanItem<VehicleScheduleBean> item;
    public final FieldGroup binder;
    // Define Buttons
    public Button save = new Button("Save");
    public Button edit = new Button("Edit");
    public Button cancel = new Button("Cancel");
    public Button update = new Button("Update");
    public Button delete = new Button("Delete");
    public ComboBox vehicleNumber;
    private Label privateSites;
    private Label privateFrequency;
    private Label privateUnits;
    private Label privateServices;
    private Label contractSites;
    private Label contractFrequency;
    private Label contractUnits;
    private Label contractServices;
    private Label otherSites;
    private Label otherFrequency;
    private Label otherUnits;
    private Label otherServices;
    private Label totalSites;
    private Label totalFrequency;
    private Label totalUnits;
    private Label totalServices;
    private Label lblNumberPlate;
    public Label lblDriver;
    public Label errorMessage;

    public VehicleSheduleForm() {
        bean = new VehicleScheduleBean();
        item = new BeanItem<>(bean);
        binder = new FieldGroup(item);
        HorizontalLayout buttons = getButtons();
        buttons.setSizeFull();
        // Determines which properties are shown
        update.setVisible(false);
        delete.setVisible(false);
        privateSites = createNewLabel("Total Sites (Private) : ");
        privateFrequency = createNewLabel("Total Frequency (Private) : ");
        privateUnits = createNewLabel("Total Units (Private) : ");
        privateServices = createNewLabel("Total Services (Private) : ");
        contractSites = createNewLabel("Total Sites (Contract) : ");
        contractFrequency = createNewLabel("Total Frequency (Contract) : ");
        contractUnits = createNewLabel("Total Units (Contract) : ");
        contractServices = createNewLabel("Total Services (Contract) : ");
        otherSites = createNewLabel("Total Sites (Other) : ");
        otherFrequency = createNewLabel("Total Frequency (Other) : ");
        otherUnits = createNewLabel("Total Units (Other) : ");
        otherServices = createNewLabel("Total Services (Other) : ");
        totalFrequency = createNewLabel("Total Frequency (Global) : ");
        totalSites = createNewLabel("Total Sites (Global) : ");
        totalUnits = createNewLabel("Total Units (Global) : ");
        totalServices = createNewLabel("Total Services (Global) : ");
        lblNumberPlate = createNewLabel("Number Plate : ");
        lblDriver = createNewLabel("Driver : ");



        vehicleNumber = UIComboBox.getServiceAndUtilityVehicles("Vehicle Number ", "vehicleNumber", VehicleScheduleBean.class, binder);
        vehicleNumber = UIValidatorHelper.setRequiredComboBox(vehicleNumber, "Driver");
        // UIComponent


        errorMessage = UIComponent.getErrorLabel();
        
        GridLayout grid = new GridLayout(4, 10);
        grid.setSizeFull();

        grid.addComponent(errorMessage, 1, 0, 2, 0);
        
        grid.addComponent(vehicleNumber, 0, 1);
        grid.addComponent(lblNumberPlate, 1, 1);
        grid.addComponent(lblDriver, 2, 1);

        grid.addComponent(privateSites, 0, 3);
        grid.addComponent(privateFrequency, 1, 3);
        grid.addComponent(privateUnits, 2, 3);
        grid.addComponent(privateServices, 3, 3);

        grid.addComponent(contractSites, 0, 4);
        grid.addComponent(contractFrequency, 1, 4);
        grid.addComponent(contractUnits, 2, 4);
        grid.addComponent(contractServices, 3, 4);

        grid.addComponent(otherSites, 0, 5);
        grid.addComponent(otherFrequency, 1, 5);
        grid.addComponent(otherUnits, 2, 5);
        grid.addComponent(otherServices, 3, 5);


        grid.addComponent(totalSites, 0, 6);
        grid.addComponent(totalFrequency, 1, 6);
        grid.addComponent(totalUnits, 2, 6);
        grid.addComponent(totalServices, 3, 6);

        grid.addComponent(new Label("<hr/>", ContentMode.HTML), 0, 2, 2, 2);
        grid.addComponent(new Label("<hr/>", ContentMode.HTML), 0, 7, 2, 7);
//                  grid.addComponent(new Label("<hr/>", ContentMode.HTML), 0, 6, 2, 6);

        addComponent(grid);

    }

    private HorizontalLayout getButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        save.setSizeFull();
        edit.setSizeFull();
        cancel.setSizeFull();
        update.setSizeFull();
        delete.setSizeFull();
        
        save.setStyleName("default");
        edit.setStyleName("default");
        cancel.setStyleName("default");
        update.setStyleName("default");
        delete.setStyleName("default");
        

        buttons.addComponent(save);
        buttons.addComponent(edit);
        buttons.addComponent(cancel);
        buttons.addComponent(update);
        buttons.addComponent(delete);
        return buttons;
    }

//    public void loadSuburbs(String parentId) {
//        List<Location> locations = LocationFacade.getLocationService().findAll();
//        locationId.removeAllItems();
//        Collection<Location> childLocations = Collections2.filter(locations, new ChildLocationIdPredicate(parentId));
//        for (Location location : childLocations) {
//            locationId.addItem(location.getId());
//            locationId.setItemCaption(location.getId(), location.getName());
//        }
//    }
    public void setPrivateTotals(int sites, int frequency, int units, int services) {
        privateSites.setValue("Total Sites (Private) : " + sites);
        privateFrequency.setValue("Total Frequency (Private) : " + frequency);
        privateUnits.setValue("Total Units (Private) : " + units);
        privateServices.setValue("Total Services (Private) : " + services);
    }

    public void setContractTotals(int sites, int frequency, int units, int services) {
        contractSites.setValue("Total Sites (Contract) : " + sites);
        contractFrequency.setValue("Total Frequency (Contract) : " + frequency);
        contractUnits.setValue("Total Units (Contract) : " + units);
        contractServices.setValue("Total Services (Contract) : " + services);
    }

    public void setOtherTotals(int sites, int frequency, int units, int services) {
        otherSites.setValue("Total Sites (Other) : " + sites);
        otherFrequency.setValue("Total Frequency (Other) : " + frequency);
        otherUnits.setValue("Total Units (Other) : " + units);
        otherServices.setValue("Total Services (Other) : " + services);
    }

    public void setGlobalTotals(int sites, int frequency, int units, int services) {

        totalSites.setValue("Total Sites (Global) : " + sites);
        totalFrequency.setValue("Total Frequency (Global) : " + frequency);
        totalUnits.setValue("Total Units (Global) : " + units);
        totalServices.setValue("Total Services (Global) : " + services);
    }

    public void setTruckDetails(String numberPlate, String driverName) {
        lblNumberPlate.setValue("Number Plate : " + numberPlate);
        lblDriver.setValue("Driver : " + driverName);
    }

    private Label createNewLabel(String value) {
        Label label = new Label(value);
        label.setSizeUndefined();
        label.addStyleName("h4");
        return label;
    }
}
