/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.app.util;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Embedded;
import java.io.Serializable;

/**
 *
 * @author Ferox
 */
public class UITableIconHelper<T> implements Serializable {

    /**
     * Valid parameters values 16, 24, 32, 48
     *
     * @param size
     * @return
     */
    public Embedded getCheckIncon(int size) {
        Embedded icon;
        switch (size) {
            case 16:
                icon = new Embedded("", new ThemeResource("Check-icon-16.png"));
                break;
            case 24:
                icon = new Embedded("", new ThemeResource("Check-icon-24.png"));
                break;
            case 32:
                icon = new Embedded("", new ThemeResource("Check-icon-32.png"));
                break;
            case 48:
                icon = new Embedded("", new ThemeResource("Check-icon-48.png"));
                break;
            default:
                icon = new Embedded("", new ThemeResource("Check-icon-32.png"));
        }
        return icon;
    }

    /**
     * Valid parameters values 16, 24, 32, 48
     *
     * @param size
     * @return
     */
    public Embedded getCrossIcon(int size) {
        Embedded icon;
        switch (size) {
            case 16:
                icon = new Embedded("", new ThemeResource("delete-icon-16.png"));
                break;
            case 24:
                icon = new Embedded("", new ThemeResource("delete-icon-24.png"));
                break;
            case 32:
                icon = new Embedded("", new ThemeResource("delete-icon-32.png"));
                break;
            case 48:
                icon = new Embedded("", new ThemeResource("delete-icon-48.png"));
                break;
            default:
                icon = new Embedded("", new ThemeResource("delete-icon-32.png"));
        }
        return icon;
    }

    public Embedded getWarehouseIcon(int size) {
        Embedded icon;
        switch (size) {
            case 20:
                icon = new Embedded("", new ThemeResource("map_pin_black_20.PNG"));
                break;
            case 32:
                icon = new Embedded("", new ThemeResource("map_pin_black_32.PNG"));
                break;
            default:
                icon = new Embedded("", new ThemeResource("map_pin_black_20.PNG"));
        }
        return icon;
    }

    public Embedded getScrappedIcon(int size) {
        Embedded icon;
        switch (size) {
            case 20:
                icon = new Embedded("", new ThemeResource("map_pin_red_20.PNG"));
                break;
            case 32:
                icon = new Embedded("", new ThemeResource("map_pin_red_32.PNG"));
                break;
            default:
                icon = new Embedded("", new ThemeResource("map_pin_red_20.PNG"));
        }
        return icon;
    }

    public Embedded getDeploymentStatusNotFoundIcon(int size) {
        Embedded icon;
        switch (size) {
            case 20:
                icon = new Embedded("", new ThemeResource("map_pin_red_20.PNG"));
                break;
            case 32:
                icon = new Embedded("", new ThemeResource("map_pin_red_32.PNG"));
                break;
            default:
                icon = new Embedded("", new ThemeResource("map_pin_red_20.PNG"));
        }
        return icon;
    }

    public Embedded getDeployedIcon(int size) {
        Embedded icon;
        switch (size) {
            case 20:
                icon = new Embedded("", new ThemeResource("map_pin_green_20.PNG"));
                break;
            case 32:
                icon = new Embedded("", new ThemeResource("map_pin_green_32.PNG"));
                break;
            default:
                icon = new Embedded("", new ThemeResource("map_pin_green_20.PNG"));
        }
        return icon;
    }

    public Embedded getCheckOrCross(boolean flag, int size) {
        if (flag) {
            return getCheckIncon(size);
        } else {
            return getCrossIcon(size);
        }
    }
    
    public Embedded getCheckOrBlank(boolean flag) {
        Embedded icon;
        if (flag) {
            return icon = new Embedded("", new ThemeResource("Check-icon-24.png"));
        } else {
           return icon = new Embedded("", new ThemeResource("blank.png"));
        }
    }

    public Embedded getDeployedStatus(String status, int size) {
        if (status.equalsIgnoreCase("DEPLOYED")) {
            return getDeployedIcon(size);
        } else if (status.equalsIgnoreCase("STOCK")) {
            return getWarehouseIcon(size);
        } else if (status.equalsIgnoreCase("SCRAPPED")) {
            return getScrappedIcon(size);
        } else {
            return null;
        }

    }

    public Embedded getVisitIcon(boolean prev, boolean actualday, int totalUnits) {
           
        Embedded icon;
        if (actualday) {
            return icon = new Embedded("", new ThemeResource("Check-icon-24.png"));
        } else {
            if ((prev) && (totalUnits >= 150)) {
                return icon = new Embedded("", new ThemeResource("ball_yellow.png"));
            } else {
//                return icon = new Embedded("", new ThemeResource("ball_grey.png"));
                
                return icon = new Embedded("", new ThemeResource("blank.png"));
            }
        }
    }
    
    public String getVisitUnicodeIcon(boolean prev, boolean actualday, int totalUnits) {
           
        String icon;
        if (actualday) {
            return icon = "\u2713";
        } else {
            if ((prev) && (totalUnits >= 150)) {
               return icon = "\uF042";
            } else {
//                return icon = new Embedded("", new ThemeResource("ball_grey.png"));
                
                return icon = "";
            }
        }
    }
}
