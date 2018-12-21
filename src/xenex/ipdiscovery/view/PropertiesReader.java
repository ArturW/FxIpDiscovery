/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.view;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class PropertiesReader {

    private static final Logger LOG = Logger.getLogger(PropertiesReader.class.getName());
    //private static final String PATH = "/resources/config.properties";
    private PropertiesReader() {
        //Properties prop = PropertiesReader.read(PATH);
        
    }
    
    public static Properties read(final String path) {
        //private static final String path = "/resources/settings.properties";
        final Properties properties = new Properties();
        try {            
            properties.load(PropertiesReader.class.getResourceAsStream(path));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } 
        return properties;        
    }
        
}
