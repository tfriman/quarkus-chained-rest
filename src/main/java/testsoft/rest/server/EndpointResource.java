package testsoft.rest.server;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import testsoft.rest.client.RestClientService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/")
public class EndpointResource {

    private static String host;

    static {
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Inject
    @RestClient
    RestClientService restClientService;

    private Long time() {
        return System.currentTimeMillis();
        //return System.nanoTime();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String name() {
        long start = time();
        String response = restClientService.callNext();
        long diff = time() - start;
        return "[" + diff + "] host:" + host + " called\n" + response;
    }


    @GET
    @Path("/timestamp")
    @Produces(MediaType.TEXT_PLAIN)
    public String timestamp() {
        return "[" + time() + "] host:" + host;
    }

}
