/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;

/**
 *
 * @author user
 */
public class TFTPService {
    
    private TFTPService() {
        
    }
    
    public static void send(String hostname, File file) {
        final int transferMode = TFTP.BINARY_MODE;        
        String fileName = file.getName();
                
        //fileName = file.getAbsolutePath();
        //System.out.println(fileName);
        //fileName = file.getPath();
        //System.out.println(fileName);
        
        TFTPClient tftp = new TFTPClient();
        tftp.setDefaultTimeout(60000);
        
        try {
            tftp.open();
        } catch (SocketException ex) {
            Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, "Error: could not open local UDP socket.", ex);
            Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        }
        
        FileInputStream input = null;
        // Try to open local file for reading
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, "Error: could not open local file for reading.", ex);
            Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        }
        
        // Try to send local file via TFTP
        // eg tftp -i 192.168.1.39 put IPn4G-v1_1_0-r1084-16.bin
        
            try
            {
                System.out.println("Sending..." + fileName + " to " + hostname + " in " + transferMode + " mode");                
                tftp.sendFile(fileName, transferMode, input, hostname);
                System.out.println("Done");
            }
            catch (UnknownHostException ex)
            {
                Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, "Error: could not resolve hostname.", ex);
                Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } catch (IOException ex) {
                Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, "Error: I/O exception occurred while receiving file.", ex);
                Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } finally {
                tftp.close();                
                try {
                    //if (input != null)
                        input.close();
                    
                } catch (IOException ex){
                    Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, "Error: error closing file.", ex);
                    Logger.getLogger(TFTPService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }

    }
}
