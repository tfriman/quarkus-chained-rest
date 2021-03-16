package testsoft.rest.server;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(EndpointResource.class);

    static {
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @ConfigProperty(name = "target/mp-rest/url")
    String nextUrl;

    @ConfigProperty(name = "deployment.identifier")
    String deploymentIdentifier;

    @Inject
    @RestClient
    RestClientService restClientService;

    private Long time() {
        return System.currentTimeMillis();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String name() {
        LOG.info("name() called, next url {}", nextUrl);
        long start = time();
        String response = restClientService.callNext();
        long diff = time() - start;
        return deploymentIdentifier + " [" + diff + "] host:" + host + " called\n" + response;
    }


    @GET
    @Path("/timestamp")
    @Produces(MediaType.TEXT_PLAIN)
    public String timestamp() {
        LOG.info("timestamp() called");
        return "[" + time() + "] host:" + host + " deployment:" + deploymentIdentifier;
    }

}
