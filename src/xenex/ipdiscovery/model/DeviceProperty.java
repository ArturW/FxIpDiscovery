/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author user
 */
public class DeviceProperty {

    private final ReadOnlyStringWrapper macAddress = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper ipAddress = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper description = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper unitAddress = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper productName = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper firmware = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper mode = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper networkName = new ReadOnlyStringWrapper();      
    
    private String getMacAddress() {
        return macAddress.get();
    }

    private ReadOnlyStringProperty macAddressProperty() {
        return macAddress.getReadOnlyProperty();
    }
    
    private String getIpAddress() {
        return ipAddress.get();
    }

    private ReadOnlyStringProperty ipAddressProperty() {
        return ipAddress.getReadOnlyProperty();
    }
    
    private String getDescription() {
        return description.get();
    }

    private ReadOnlyStringProperty descriptionProperty() {
        return description.getReadOnlyProperty();
    }
       
    private String getUnitAddress() {
        return unitAddress.get();
    }

    private ReadOnlyStringProperty unitAddressProperty() {
        return unitAddress.getReadOnlyProperty();
    }
    
    private String getProductName() {
        return productName.get();
    }

    private ReadOnlyStringProperty productNameProperty() {
        return productName.getReadOnlyProperty();
    }
    
    private String getFirmware() {
        return firmware.get();
    }

    private ReadOnlyStringProperty firmwareProperty() {
        return firmware.getReadOnlyProperty();
    }
    
    private String getMode() {
        return mode.get();
    }

    private ReadOnlyStringProperty modeProperty() {
        return mode.getReadOnlyProperty();
    }
    
    private String getNetworkName() {
        return networkName.get();
    }

    private ReadOnlyStringProperty networkNameProperty() {
        return networkName.getReadOnlyProperty();
    }
}
