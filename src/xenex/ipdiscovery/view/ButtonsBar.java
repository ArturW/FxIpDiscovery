/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.view;

import java.util.Properties;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;

/**
 *
 * @author user
 */
public class ButtonsBar extends ButtonBar {
    
    //ButtonBar buttonBar = new ButtonBar();
    Button settingsButton = new Button("Settings");            
    Button ipButton = new Button("IP");
    Button vipButton = new Button("VIP");
    Button cellButton = new Button("Cell");
    Button allButton = new Button("All");
    Button copyButton = new Button("Copy All");
    Button clearButton = new Button("Clear");
        
    public ButtonsBar() {                                
        layoutButtons();
    }
    
    private void layoutButtons() {
        this.getStyleClass().add("buttonBar");
       
        ipButton.getStyleClass().add("buttons");        
        //ControlView.setHgrow(ipButton, Priority.ALWAYS);
                
        ButtonBar.setButtonData(settingsButton, ButtonData.LEFT);
        ButtonBar.setButtonData(ipButton, ButtonData.LEFT);
        ButtonBar.setButtonData(vipButton, ButtonData.LEFT);
        ButtonBar.setButtonData(cellButton, ButtonData.LEFT);
        ButtonBar.setButtonData(allButton, ButtonData.LEFT);
        ButtonBar.setButtonData(copyButton, ButtonData.RIGHT);
        ButtonBar.setButtonData(clearButton, ButtonData.RIGHT);
                
        this.getButtons().addAll(settingsButton, ipButton, vipButton, cellButton, allButton,
               copyButton, clearButton);        
               
    }
 
}
