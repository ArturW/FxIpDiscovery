/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Instant;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 *
 * @author user
 */
public class DiscoveryServerTask extends Task {

    private static final Logger LOG = Logger.getLogger(DiscoveryServerTask.class.getName());
    private static final String MICROHARD = "00:0F:92";
    private final ObservableList<Device> deviceList = FXCollections.observableArrayList();
    private final int port;
    private final int timeout;
    
    public DiscoveryServerTask(int port, int timeout) {
        this.port = port;
        this.timeout = timeout;
    }

    public ObservableList<Device> getDeviceList() {
        return deviceList;
    }
    
    @Override
    protected Void call() {
        LOG.log(Level.INFO, "Server started for {0} sec at port {1}", new Object[]{timeout, port});
        //final Set<Device> results = new HashSet<>();
        try {                
            DatagramChannel ch = DatagramChannel.open()
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .bind(new InetSocketAddress(port));
            ch.configureBlocking(false);

            ByteBuffer receivedBuffer = ByteBuffer.allocate(64);

            Instant stop = Instant.now().plusSeconds(timeout);
            while(Instant.now().isBefore(stop)) {
                SocketAddress adr = ch.receive(receivedBuffer);
                receivedBuffer.flip();
                byte[] received = receivedBuffer.array();                
                /* Parse received restuls */
                Device device = parseBytes(received);                
                if (device != null && !deviceList.contains(device)) {
                    deviceList.add(device);
                    LOG.log(Level.INFO, "Found new: {0}", device);
                    //Platform.runLater(() -> deviceList.add(device));
                }
                receivedBuffer.clear();
            }
        } catch (IOException ex) {
            Logger.getLogger(IPDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            LOG.log(Level.INFO, "Server stopped");                
        }    
        return null;
    }
    
        
    private String getMacAddress (int data[]) {
        final StringBuilder string = new StringBuilder();
                        
        for (int i = 2, count = 1; i < 8; i++) {
            //string.append(Integer.toHexString(data[i]));
            String hex = (Integer.toHexString(data[i]));
            if (hex.length() == 1)
                hex = "" + 0 + hex;
            string.append(hex.toUpperCase());
            
            if (count < 6) {
                string.append(':');
                ++count;
            }
        }
        return new String(string);   
    }
    
    private String getIPAddress (int data[]) {
        final StringBuilder string = new StringBuilder();
        
        for (int i = 9, count = 1; i < 13; i++) {
            string.append(data[i]);
            if (count < 4) {
                string.append('.');
                ++count;
            }
        }
        return new String(string);   
    }
        
    private int[] convertToInt(byte[] data) {                                
        return IntStream.range(0, data.length)
                .map(i -> data[i] & 0xFF)
                .toArray();                
    }
   
    private int getEndIndex(String data, int start) {
        int end = data.indexOf("\0", start);
        return end;
    }
    
    private String getItem (String data, int start, int end) {
        //StringBuilder string = new StringBuilder();
        //int[] intData = convertToInt(byteData);
        String str = new String(data.substring(start, end));
        return str;
    }
    
    //IPx20 481 IPx20LC v2.2.60 Remote IPx20             -> [IPx20, 481, IPx20LC, v2.2.60, Remote, IPx20] / 6
    //VIP2  VIP2-5800 v2.2.0-r2018 ap wlan0   VIP2 defaul-> [VIP2, , VIP2-5800, v2.2.0-r2018, ap, wlan0, , , VIP2, defaul] / 10
    private Device parseBytes(byte[] data) {        
        final int[] rx = convertToInt(data);        
        final String mac = this.getMacAddress(rx);       
        if (!mac.startsWith(MICROHARD))
             return null; //new Device.Builder("", "").build();   
        
        final String ip = this.getIPAddress(rx);                
        
        final String strData = new String(data);        
        final String[] items = strData.substring(13).split("\0");        
        //System.out.println(strData.substring(13) + "-> " + Arrays.toString(items) + " / " + items.length);
        final String description = items[0];
        final String unitAddress = items[1];
        final String productName = items[2];
        final String firmware = items[3];
        final String mode = items[4];
        final String networkName = items[5];
        //final String radioFirmware = items[6];
                
        final Device device = new Device.Builder(mac, ip)
                .description(description)
                .unitAddress(unitAddress)
                .productName(productName)
                .firmware(firmware)
                .mode(mode)
                .networkName(networkName)
                //.radioFirmware(radioFirmware)
                .build();
        return device;
    }   
        
    private Set<Device> filterResults(Set<Device> set) {        
        return set.stream()
                .filter(each -> each.getMacAddress().startsWith(MICROHARD))
                .collect(Collectors.toSet());        
    }       
}
