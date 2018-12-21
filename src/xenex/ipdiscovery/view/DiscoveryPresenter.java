/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.view;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import xenex.ipdiscovery.Main;
import xenex.ipdiscovery.model.Device;
import xenex.ipdiscovery.model.DiscoveryClientTask;
import xenex.ipdiscovery.model.DiscoveryServerTask;
import xenex.ipdiscovery.model.TFTPService;

/**
 *
 * @author user
 */
public class DiscoveryPresenter {
    
    private static final String PATH = "/resources/config.properties";
    private static final int GET_TIMEOUT = 10;    
    private final Properties prop;    
    
    private final DiscoveryView view;
    private final ObservableList discoveredList = FXCollections.observableArrayList();
    
    Menu menu = new Menu();
    
    public DiscoveryPresenter(DiscoveryView view) {
    
        this.view = view;        
        this.prop =  PropertiesReader.read(PATH);                
        
        attachEvents();
    }
    
    private void attachEvents() {
        view.buttonsBar.settingsButton.setOnAction(value -> {});
        view.buttonsBar.settingsButton.setDisable(true);
        
        view.buttonsBar.ipButton.setOnAction(value -> {
            int port = Integer.parseInt((String)prop.get("portIp"));
            int timeout = Integer.parseInt((String)prop.get("timeout"));
            discoverAction(port, timeout);
        });
        
        view.buttonsBar.vipButton.setOnAction(value -> {
            int port = Integer.parseInt((String)prop.get("portVip"));
            int timeout = Integer.parseInt((String)prop.get("timeout"));
            discoverAction(port, timeout);
            
        });
        
        view.buttonsBar.cellButton.setOnAction(value -> {
            int port = Integer.parseInt((String)prop.get("portCell"));
            int timeout = Integer.parseInt((String)prop.get("timeout"));
            discoverAction(port, timeout);
        });
        
        view.buttonsBar.allButton.setOnAction(value -> {
            int timeout = Integer.parseInt((String)prop.get("timeout"));
            int port = Integer.parseInt((String)prop.get("portIp"));
            discoverAction(port, timeout);
            port = Integer.parseInt((String)prop.get("portVip"));
            discoverAction(port, timeout);
            port = Integer.parseInt((String)prop.get("portCell"));
            discoverAction(port, timeout);
        });
        
        view.buttonsBar.copyButton.setOnAction(value -> {           
            view.deviceTable.getSelectionModel().selectAll();
            ObservableList<Device> selectedList = view.deviceTable.getSelectionModel().getSelectedItems();                        
            System.out.println(selectedList);
            StringBuilder string = new StringBuilder();
            selectedList.stream()
                    .forEach(each -> {
                        String line = String.format("%s\n", each.toString());
                        string.append(line);
                    });
            
            Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();            
            content.putString(string.toString());            
            clipboard.setContent(content);
            view.deviceTable.getSelectionModel().clearSelection();
        });
                
        view.buttonsBar.clearButton.setOnAction(value -> {
            view.deviceTable.getItems().clear();            
            //view.deviceTable.setItems(FXCollections.observableArrayList(new Device.Builder("", "").build()));
        });
                
        view.deviceTable.setOnMouseClicked((MouseEvent event) -> {
            MouseButton button = event.getButton();
            Device device = (Device)view.deviceTable.getSelectionModel().getSelectedItem();
            System.out.println(device);
            if (device == null)
                return;
            
            switch (button) {
                case PRIMARY:                                       
                    String url = "http://" + device.getIpAddress();            
                    System.out.println(url);

                    HostServices services = Main.getInstance().getHostServices();
                    services.showDocument(url);
                    //openPage(url);
                    break;
                case SECONDARY:                                                                                
                    double x = event.getScreenX();
                    double y = event.getScreenY();
                    //ipMenu.show(view, x, y);
                    //ipMenu.setAutoHide(true);                    
                    view.deviceTable.setContextMenu(menu);                    
                    
                    break;
            }
        }); 
                
        menu.copyItem.setOnAction(value -> {                        
            // Not needed for single selected row
            //view.deviceTable.getSelectionModel().selectAll();
            Device selected = (Device)view.deviceTable.getSelectionModel().getSelectedItem();
            System.out.println(selected);
            String line = String.format("%s\n", selected.toString());
            
            Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(line);       
            clipboard.setContent(content);
            view.deviceTable.getSelectionModel().clearSelection();
        });
        
        menu.upgradeItem.setOnAction(value -> {
            Device selected = (Device)view.deviceTable.getSelectionModel().getSelectedItem();
            String url = selected.getIpAddress();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Bin files", "*.bin"),
                    new ExtensionFilter("Img files", "*.img"),
                    new ExtensionFilter("Pkg files", "*.pkg"),
                    new ExtensionFilter("All files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(Main.getInstance().getStage());                        
            if (selectedFile != null) {                
                TFTPService.send(url, selectedFile);
            }
        });
        
    }
    
    private void openPage(String url) {         
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);

        TextField addressBar = new TextField(url);            
        VBox browser = new VBox(addressBar, webView);

        Stage stage = new Stage();
        stage.setTitle(url);
        stage.setScene(new Scene(browser));
        stage.show();
    }
    private void discoverAction(int port, int timeout) {
        final ExecutorService es = Executors.newFixedThreadPool(2);
            
            final Task clientTask = new DiscoveryClientTask(port);
            final Task serverTask = new DiscoveryServerTask(port, timeout);
                                                            
            serverTask.onSucceededProperty().addListener(listener -> {
                });            
                        
            serverTask.setOnSucceeded(v -> {
                ObservableList<Device> result = ((DiscoveryServerTask)serverTask).getDeviceList();                
                result.stream()
                        .filter(each -> !discoveredList.contains(each))
                        .forEach(each -> discoveredList.add(each));
                view.deviceTable.setItems(discoveredList);
            });
                        
            Thread server = new Thread(clientTask);
            //server.setDaemon(true);
            server.start();            
            //sleep(250);            
            Thread client = new Thread(serverTask);
            //client.setDaemon(true);
            client.start();  
    }
    
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(DiscoveryPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
