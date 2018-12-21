/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import javafx.concurrent.Task;

/**
 *
 * @author user
 */
public class DiscoveryClientTask extends Task{
    
    final int port;
    public DiscoveryClientTask(int port) {
        this.port = port;                
    }
        
    @Override
    protected Void call() throws Exception {
        final byte[] request = createRequest();
        sendRequest(request, this.port);
        return null;
    }
    
    private void sendRequest(final byte[] requestData, int port) throws SocketException, IOException {                        
        InetSocketAddress address = new InetSocketAddress("255.255.255.255", port);
        DatagramChannel channel = DatagramChannel.open()
                .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                .bind(new InetSocketAddress(port))
                .setOption(StandardSocketOptions.SO_BROADCAST, true);
                        
        ByteBuffer buffer = ByteBuffer.wrap(requestData);                                        
        channel.send(buffer, address);                     
    }   
    
    private byte[] createRequest() {
                
        int[] intData = new int [] {0x70, 0x63, 0x00, 0x06, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        byte[] byteData = new byte[intData.length];
        
        for (int i = 0; i < byteData.length; i++) {
            byteData[i] = (byte)intData[i];
        }
        return byteData;
    }
    
}
