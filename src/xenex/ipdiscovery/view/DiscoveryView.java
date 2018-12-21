/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.view;

import javafx.scene.layout.VBox;

/**
 *
 * @author user
 */
public class DiscoveryView extends VBox {

    ButtonsBar buttonsBar = new ButtonsBar();
    DeviceTable deviceTable = new DeviceTable();
    
    public DiscoveryView() {
        this.getChildren().addAll(buttonsBar, deviceTable);
    }
}
