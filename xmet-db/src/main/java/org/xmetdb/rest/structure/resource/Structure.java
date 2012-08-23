package org.xmetdb.rest.structure.resource;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.idea.opentox.cli.AbstractClient;
import net.idea.opentox.cli.IIdentifiableResource;
import net.toxbank.client.resource.Protocol;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;

public class Structure implements IIdentifiableResource<URL>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6291149605331598909L;
	protected int idchemical;
	protected int idstructure;
	protected Hashtable<String, String> properties; 
	public enum _titles { //from OpenTox.owl
		Compound,
		CASRN {
			@Override
			public String getTitle() {
				return "CASRN";
			}
		},
		EINECS,
		IUPACName {
			@Override
			public String getTitle() {
				return "IUPAC name";
			}
		},
		ChemicalName {
			@Override
			public String getTitle() {
				return "Chemical Name";
			}
		},
		SMILES,
		InChI_std {
			@Override
			public String getTitle() {
				return "Standard InChI";
			}
		},
		InChIKey_std {
			@Override
			public String getTitle() {
				return "Standard InChI key";
			}
		},
		REACHRegistrationDate {
			@Override
			public String getTitle() {
				return "REACH registration date";
			}
		};
		public String getTitle() {
			return name();
		}
		@Override
		public String toString() {
			return getTitle();
		}
	}	
	public Structure(URL resourceURL) {
		setResourceIdentifier(resourceURL);
	}

	public Structure() {
		this(null);
	}

	
	public Hashtable<String, String> getProperties() {
		if (properties==null) properties = new Hashtable<String, String>();
		return properties;
	}
	public void setProperties(Hashtable<String, String> properties) {
		this.properties = properties;
	}
	public int getIdchemical() {
		return idchemical;
	}
	public void setIdchemical(int idchemical) {
		this.idchemical = idchemical;
	}

	public int getIdstructure() {
		return idstructure;
	}
	public void setIdstructure(int idstructure) {
		this.idstructure = idstructure;
	}
	String name;
	String cas;
	String einecs;
	String SMILES;
	String InChI;
	String InChIKey;
	
	List<Protocol> protocols;
	
	public List<Protocol> getProtocols() {
		if (protocols==null) protocols=new ArrayList<Protocol>();
		return protocols;
	}
	public void setProtocols(List<Protocol> protocols) {
		this.protocols = protocols;
	}
	protected String similarity = null;
	public String getSimilarity() {
		return similarity;
	}
	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCas() {
		return cas;
	}
	public void setCas(String cas) {
		if (cas==null) this.cas = null;
		else {
			int index = cas.indexOf("|");
			this.cas = index>0?cas.substring(0,index):cas;
		}
	}
	public String getEinecs() {
		return einecs;
	}
	public void setEinecs(String einecs) {
		this.einecs = einecs;
	}
	public String getSMILES() {
		return SMILES;
	}
	public void setSMILES(String sMILES) {
		SMILES = sMILES;
	}
	public String getInChI() {
		return InChI;
	}
	public void setInChI(String inChI) {
		InChI = inChI;
	}
	
	public String getInChIKey() {
		return InChIKey;
	}
	public void setInChIKey(String inChIKey) {
		InChIKey = inChIKey;
	}
	
	public Object[] parseURI(Reference baseReference)  {
		if (getResourceIdentifier().toString().indexOf("conformer")>0)
			return OpenTox.URI.conformer.getIds(getResourceIdentifier().toString(),baseReference);
		else //smth wrong with parsing compound uris
			return OpenTox.URI.conformer.getIds(getResourceIdentifier().toString()+"/conformer/0",baseReference);

	}
	
	private URL resourceURL;
	private String title;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public void setResourceIdentifier(URL resourceURL) {
		this.resourceURL = resourceURL;
	}

	@Override
	public URL getResourceIdentifier() {
		return resourceURL;
	}
	
	public static List<Structure> retrieveStructures(String queryService, String ref)  throws Exception{
		Reference queryURI = new Reference(queryService);
		HttpClient httpcli = new DefaultHttpClient();
		AbstractClient<Structure, String> cli = new AbstractClient<Structure, String>(httpcli);
		List<Structure> records = new ArrayList<Structure>();
		try {
			List<URL> urls = cli.listURI(new URL(ref)); //TODO custom client to return List<Structure>
		
			for (URL url: urls) {
				Structure struc = new Structure(url);
				try {
					Object[] ids = struc.parseURI(queryURI);
					if (ids[0]!=null) struc.setIdchemical((Integer) ids[0]);
					if (ids[1]!=null) struc.setIdstructure((Integer) ids[1]);
				} catch (Exception x) {
				}
				records.add(struc);
			}
			return records;
		} catch (Exception x) {
			throw new ResourceException(HttpStatus.SC_BAD_GATEWAY, "Failed to retrieve chemical structures", String.format(
					"Error when contacting chemical structure service at %s",
					ref),
					"http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html", x);
		} finally {
			try {httpcli.getConnectionManager().shutdown(); } catch (Exception x) {}
		}

	}
}
