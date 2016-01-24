package com.blackhat.service;

import com.blackhat.service.subresource.ArtworkResource;
import com.blackhat.service.subresource.CustomerResource;
import com.blackhat.service.subresource.DeliveryAddressResource;
import com.blackhat.service.subresource.JobOrderResource;
import com.blackhat.service.subresource.PantoneColorResource;
import com.blackhat.service.subresource.PartResource;
import com.blackhat.service.subresource.SampleJobResource;
import com.blackhat.service.subresource.ScreenResource;
import com.blackhat.service.subresource.UserResource;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 *
 * @author Sachith Dickwella
 */
@Path("/resource")
public class Resource {

    @Context
    private ServletContext context;

    @Path("/users")
    public UserResource getUsers() {
        return new UserResource(context);
    }

    @Path("/customers")
    public CustomerResource getCustomers() {
        return new CustomerResource(context);
    }

    @Path("/joborders")
    public JobOrderResource getJobOrders() {
        return new JobOrderResource(context);
    }

    @Path("/samplejobs")
    public SampleJobResource getSampleJobs() {
        return new SampleJobResource(context);
    }
    
    @Path("/pantonecolors")
    public PantoneColorResource getPantoneColors() {
        return new PantoneColorResource(context);
    }
    
    @Path("/parts")
    public PartResource getParts() {
        return new PartResource(context);
    }
    
    @Path("/screens")
    public ScreenResource getScreens() {
        return new ScreenResource(context);
    }
    
    @Path("/artworks")
    public ArtworkResource getArtworks() {
        return new ArtworkResource(context);
    }
    
    @Path("/deliveryaddresses")
    public DeliveryAddressResource getDeliveryAddresses() {
        return new DeliveryAddressResource(context);
    }
    
    @Path("/samples")
    public SampleJobResource getSamples() {
        return new SampleJobResource(context);
    }
}
