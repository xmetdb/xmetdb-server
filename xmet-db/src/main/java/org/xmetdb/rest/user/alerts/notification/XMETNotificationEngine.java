package org.xmetdb.rest.user.alerts.notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.idea.restnet.user.alerts.notification.SimpleNotificationEngine;
import net.toxbank.client.io.rdf.ProtocolIO;
import net.toxbank.client.resource.Protocol;

import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class XMETNotificationEngine extends SimpleNotificationEngine {
	protected ProtocolIO ioClass = new ProtocolIO();
    public XMETNotificationEngine(Reference root) throws IOException {
	    super(root,"config/xmetdb.properties");
	    setNotificationSubject("XMetDB Alert Updates");
	}
    @Override
    protected List<String> retrieve(Reference ref) throws Exception {
  	  ClientResource cr = null;
  	  Representation repr = null;
  	  try {

  		cr = new ClientResource(ref);
  		repr = cr.get(MediaType.APPLICATION_RDF_XML);
  		if (org.restlet.data.Status.SUCCESS_OK.equals(cr.getStatus())) {
  			List<String> urls = new ArrayList<String>();
  			Model model = ModelFactory.createDefaultModel();
  			model.read(repr.getStream(), null, "RDF/XML");
  			List<Protocol> protocols = ioClass.fromJena(model);
  			model.close();
  			model = null;
  			for (Protocol p : protocols) {
  				urls.add(String.format("%s\t%s\n%s\r\n",p.getIdentifier(), p.getTitle(), p.getResourceURL() ));
  			}
  			return urls;
  		}
  		} catch (ResourceException x) {

  			if (x.getStatus().equals(org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND)) {
  				//skip, this is ok
  			} else 
  				log.log(Level.WARNING,String.format("Error reading URL %s\n%s",ref.toString(),cr.getStatus()),x);
  		} catch (Exception x) {	
  			throw x;
  		} finally {
  			try {repr.release();} catch (Exception x) {}
  			try {cr.release();} catch (Exception x) {}
  		}     
  		return null;
    }

}
