/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.model;

import java.util.Objects;

/**
 *
 * @author user
 */
public class Device {
    private final String macAddress;
    private final String ipAddress;
    private final String description;
    private final String unitAddress;
    private final String productName;
    private final String firmware;
    private final String mode;
    private final String networkName;
    //private final String radioFirmware;
    
    public static class Builder {
        // Rquired Parameters;
        private final String macAddress;
        private final String ipAddress;
        
        // Optional Parameters
        private String description = "";
        private String unitAddress = "";
        private String productName = "";
        private String firmware = "";        
        private String mode = "";
        private String networkName = "";
        private String radioFirmware = "";
        
        public Builder(String mac, String ipAddress) {
            this.macAddress = mac;
            this.ipAddress = ipAddress;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder unitAddress(String unitAddress) {
            this.unitAddress = unitAddress;
            return this;
        }
        
        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }
        
        public Builder firmware(String firmware) {
            this.firmware = firmware;
            return this;
        }
        
        public Builder radioFirmware(String radioFirmware) {
            this.radioFirmware = radioFirmware;
            return this;
        }
        
        public Builder mode(String mode) {
            this.mode = mode;
            return this;
        }
        
        
        public Builder networkName(String networkName) {
            this.networkName = networkName;
            return this;
        }
        
        public Device build() {
            return new Device(this);
        }
    }

    private Device(Builder builder) {
        this.macAddress = builder.macAddress;
        this.ipAddress = builder.ipAddress;
        this.description = builder.description;
        this.unitAddress = builder.unitAddress;
        this.productName = builder.productName;
        if (builder.radioFirmware.equals(""))
            this.firmware = builder.firmware;
        else
            this.firmware = builder.firmware + "(" + builder.radioFirmware + ")";        
        this.mode = builder.mode;
        this.networkName = builder.networkName;        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.macAddress);
        hash = 43 * hash + Objects.hashCode(this.ipAddress);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Device other = (Device) obj;
        if (!Objects.equals(this.macAddress, other.macAddress)) {
            return false;
        }
        if (!Objects.equals(this.ipAddress, other.ipAddress)) {
            return false;
        }
        return true;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public String getProductName() {
        return productName;
    }

    public String getFirmware() {
        return firmware;
    }

    public String getMode() {
        return mode;
    }

    public String getNetworkName() {
        return networkName;
    }
    
    @Override
    public String toString() {
        return "Device{" + "mac=" + macAddress + ", ipAddress=" + ipAddress + ", description=" + description + '}';
    }

        
}
