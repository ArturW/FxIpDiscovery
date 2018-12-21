/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.ipdiscovery.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 * @author user
 */
@Deprecated
public class IPDiscovery implements Callable<Set<Device>> {  

    private static final Logger LOG = Logger.getLogger(IPDiscovery.class.getName());            
    
    private final int port;
    private final long timeout;
    private IPDiscovery (int port, long timeout) {
        this.port = port;
        this.timeout = timeout;
    }
    
    public static IPDiscovery newInstance(int port, long timeout) {
        return new IPDiscovery(port, timeout);
    }
    
    @Override
    public Set<Device> call() throws Exception {
        final byte[] request = createRequest();
        Set<Device> result  = sendRequest(request, this.port, this.timeout).get(10, TimeUnit.SECONDS);
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private byte[] createRequest() {
                
        int[] intData = new int [] {0x70, 0x63, 0x00, 0x06, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        byte[] byteData = new byte[intData.length];
        
        for (int i = 0; i < byteData.length; i++) {
            byteData[i] = (byte)intData[i];
        }
        return byteData;
    }
    
    /*
    private ByteBuffer createDataBuffer(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return buffer;
    }
    */
    
    private Future<Set<Device>> startServer (int port, long timeout) {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable task = () -> {
            LOG.log(Level.FINE, "Server started for {0} sec at port {1}", new Object[]{timeout, port});
            final Set<Device> results = new HashSet<>();
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
                    Device device = parseBytes(received);
                                                                
                    if (device != null && !results.contains(device)) {
                        results.add(device);
                        LOG.log(Level.FINE, "Found: {0}", device);
                    }
                    receivedBuffer.clear();
                }
            } catch (IOException ex) {
                Logger.getLogger(IPDiscovery.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                executor.shutdown();
                LOG.log(Level.FINE, "Server stopped");                
            }
            
            return filterResults(results);
        };
        
        Future<Set<Device>> future = executor.submit(task);           
        return future;
    }
    
    private Future<Set<Device>> sendRequest(final byte[] requestData, int port, long timeout) throws SocketException, IOException {
        //AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();        
        Future<Set<Device>> result = startServer(port, timeout);
                
        InetSocketAddress address = new InetSocketAddress("255.255.255.255", port);
        DatagramChannel channel = DatagramChannel.open()
                .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                .bind(new InetSocketAddress(port))
                .setOption(StandardSocketOptions.SO_BROADCAST, true);
                        
        ByteBuffer buffer = ByteBuffer.wrap(requestData);                                        
        channel.send(buffer, address);     
        
        return result;        
    }       
    
    private String getMacAddress (int data[]) {
        final StringBuilder string = new StringBuilder();
                        
        for (int i = 2, count = 1; i < 8; i++) {
            string.append(Integer.toHexString(data[i]));
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
    
    private Device parseBytes(byte[] data) {        
        final int[] rx = convertToInt(data);        
        final String mac = this.getMacAddress(rx);
        final String MICROHARD = "0:f:92";
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
        final String MICROHARD = "0:f:92";
        return set.stream()
                .filter(each -> each.getMacAddress().startsWith(MICROHARD))
                .collect(Collectors.toSet());        
    }       
    
    private Set<Object> getHeaders(Properties prop) {
        Set<Object> headers = new LinkedHashSet<>(prop.values());
        
        return headers;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException  {
        System.out.println("IPDiscovery");
        
        final ExecutorService es = Executors.newFixedThreadPool(3);
        final int[] ports = new int[]{20077, 20087, 20097};
        long timeout = 3;
        final Callable task1 = IPDiscovery.newInstance(ports[0], timeout);
        final Callable task2 = IPDiscovery.newInstance(ports[1], timeout);
        final Callable task3 = IPDiscovery.newInstance(ports[2], timeout);
        
        final Future<Set<Device>> future1 = es.submit(task1);
        final Future<Set<Device>> future2 = es.submit(task2);
        final Future<Set<Device>> future3 = es.submit(task3);
                                
                        
        final Set<Device> devices = new HashSet<>();
        devices.addAll(future1.get(10, TimeUnit.SECONDS));
        devices.addAll(future2.get(10, TimeUnit.SECONDS));
        devices.addAll(future3.get(10, TimeUnit.SECONDS));
                
        es.shutdown();
        
        devices.stream()
            .forEach(each -> System.out.println(each));
                        
    }    
}
