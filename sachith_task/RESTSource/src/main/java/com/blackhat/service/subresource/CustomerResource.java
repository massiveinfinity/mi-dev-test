package com.blackhat.service.subresource;

import static com.blackhat.BlackhatConstants.getSession;
import com.blackhat.entity.Customer;
import com.blackhat.factory.FactoryFacade;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Sachith Dickwella
 */
public class CustomerResource extends FactoryFacade<Customer> {

    private final ServletContext context;
    private final Logger logger = Logger.getLogger(CustomerResource.class);

    public CustomerResource(ServletContext context) {
        this.context = context;
    }

    @PUT
    @Path("/")
    @Produces("text/plain")
    @Consumes("application/json")
    @Override
    public int create(Customer type) {
        Session session = getSession(context);
        Transaction tx = session.beginTransaction();
        int genId = 0;
        try {
            genId = (Integer) session.save(type);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error(Arrays.toString(e.getStackTrace()));
            genId = -1;
        } finally {
            session.close();
        }
        return genId;
    }

    @GET
    @Path("/")
    @Produces({"application/xml; qs=0.9", "application/json"})
    @Override
    public List<Customer> getAll() {
        Session session = getSession(context);
        try {
            Query query = session.getNamedQuery("getAllCustomers");
            return query.list();
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        } finally {
            session.close();
        }
        return null;
    }

    @GET
    @Path("/{id}")
    @Produces({"application/xml; qs=0.9", "application/json"})
    @Override
    public Customer getById(@PathParam(value = "id") int id) {
        Session session = getSession(context);
        try {
            Query query = session.getNamedQuery("getCustomerById");
            query.setInteger("id", id);
            List<Customer> customers = query.list();
            if (customers.size() > 0) {
                return customers.get(1);
            }
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        } finally {
            session.close();
        }
        return null;
    }

    @GET
    @Path("/customer/{name}")
    @Produces({"application/xml; qs=0.9", "application/json"})
    @Override
    public List<Customer> getByName(@PathParam(value = "name") String name) {
        Session session = getSession(context);
        try {
            Query query = session.getNamedQuery("getCustomerByName");
            query.setString("name", name);
            return query.list();
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        } finally {
            session.close();
        }
        return null;
    }

    @POST
    @Path("/update")
    @Produces("text/plain")
    @Consumes("aplication/json")
    @Override
    public int update(Customer type) {
        Session session = getSession(context);
        Transaction tx = session.beginTransaction();
        try {
            session.update(type);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error(Arrays.toString(e.getStackTrace()));
            return -1;
        } finally {
            session.close();
        }
        return 0;
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces("text/plain")
    @Override
    public int delete(@PathParam(value = "id") int id) {
        Session session = getSession(context);
        Transaction tx = session.beginTransaction();
        try {
            /**
             * TODO code here
             */
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error(Arrays.toString(e.getStackTrace()));
            return -1;
        } finally {
            session.close();
        }
        return 0;
    }
}
