/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 *
 * @author user
 */
public class Menu extends ContextMenu {
    
    MenuItem copyItem = new MenuItem("Copy");
    MenuItem upgradeItem = new MenuItem("FTP Upgrade...");
    
    Menu() {
        SeparatorMenuItem separator = new SeparatorMenuItem();
        this.getItems().addAll(copyItem, separator, upgradeItem);
    }
    
    
}
