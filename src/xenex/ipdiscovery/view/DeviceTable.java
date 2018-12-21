/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.view;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author user
 * @param <Device>
 */
public class DeviceTable<Device> extends TableView {
        
    private static final boolean COLUMN_STYLE = false;
    
    public DeviceTable() {
        this.getStyleClass().add("table");
        //this.getSelectionModel().setCellSelectionEnabled(true);
        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        setUpColumns();                
    }

    private void setUpColumns() {
        TableColumn<Device, String> macCol = new TableColumn<>("Mac Address");        
        macCol.setCellValueFactory(new PropertyValueFactory<>("macAddress"));
        
        TableColumn<Device, String> ipCol = new TableColumn<>("IP Address");
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
                
        ipCol.setCellFactory(column -> {
            return new TableCell<Device, String>() {
                @Override
                protected void updateItem(String item, boolean b) {
                    super.updateItem(item, b);                    
                    //this.setTextFill(Color.BLUE);
                    this.setStyle("-fx-font-weight: bold");                   
                    this.setText(item);
                }             
            };        
        });
        
        TableColumn<Device, String> descriptionCol = new TableColumn<>("Description");        
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        TableColumn<Device, String> unitAddressCol = new TableColumn<>("Unit Address");
        unitAddressCol.setCellValueFactory(new PropertyValueFactory<>("unitAddress"));
        
        TableColumn<Device, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        
        TableColumn<Device, String> firmwareCol = new TableColumn<>("Firmware");
        firmwareCol.setCellValueFactory(new PropertyValueFactory<>("firmware"));
        
        TableColumn<Device, String> modeCol = new TableColumn<>("Mode");        
        modeCol.setCellValueFactory(new PropertyValueFactory<>("mode"));
        
        TableColumn<Device, String> networkNameCol = new TableColumn<>("Network Name");
        networkNameCol.setCellValueFactory(new PropertyValueFactory<>("networkName"));
                        
        this.getColumns().addAll(macCol, ipCol,
                descriptionCol, unitAddressCol, productNameCol, firmwareCol,
                modeCol, networkNameCol);
        
        if (COLUMN_STYLE) {
            macCol.getStyleClass().add("table-column-cell");
            ipCol.getStyleClass().add("table-column-cell");
            descriptionCol.getStyleClass().add("table-column-cell");
            unitAddressCol.getStyleClass().add("table-column-cell");
            productNameCol.getStyleClass().add("table-column-cell");
            firmwareCol.getStyleClass().add("table-column-cell");
            modeCol.getStyleClass().add("table-column-cell");
            networkNameCol.getStyleClass().add("table-column-cell");
            
        }
    }
    
    
}
