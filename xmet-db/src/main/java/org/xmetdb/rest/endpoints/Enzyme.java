package org.xmetdb.rest.endpoints;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

public class Enzyme implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5987449761837302780L;
	public enum EnzymeFields {
		id,
		code,
		name,
		uri,
		alleles;

	}

	protected String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	protected String code;
	protected String parentCode;
	protected String[] alleles = null;
	protected URI uri = null;
	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		if (uri==null) this.uri = null;
		else if (uri.toString().startsWith("http"))
			this.uri = uri;
		else try {
			this.uri = new URI(String.format("http://www.uniprot.org/uniprot/%s", uri.toString()));
		} catch (URISyntaxException x) {
			this.uri = uri;
		}
	}

	public String[] getAlleles() {
		return alleles;
	}

	public void setAlleles(String[] alleles) {
		this.alleles = alleles;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	protected int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Enzyme() {
		super();
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String toString() {
		return String.format("%s.%s",getCode(),getAlleles()==null?"":getAlleles().length==0?"":getAlleles()[0]);
	}
}
