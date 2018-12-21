/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import xenex.ipdiscovery.view.DiscoveryPresenter;
import xenex.ipdiscovery.view.DiscoveryView;


/**
 *
 * @author user
 */
public class Main extends Application {
    private static final String STYLESHEET_PATH = "resources/styleSheet.css";
    
    private static final Main main = new Main();
    private Stage stage;
    
    public static Main getInstance() {
        return main;
    }

    public Stage getStage() {
        return stage;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        
        DiscoveryView view = new DiscoveryView();
        DiscoveryPresenter presenter = new DiscoveryPresenter(view);
                                        
        StackPane root = new StackPane();        
        root.getChildren().add(view);
        
        Scene scene = new Scene(root, 1000, 500);        
        scene.getStylesheets().add(STYLESHEET_PATH);
        
        primaryStage.setTitle("IP Discovery");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
