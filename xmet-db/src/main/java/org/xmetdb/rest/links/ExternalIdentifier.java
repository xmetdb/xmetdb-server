package org.xmetdb.rest.links;

import java.io.Serializable;


public class ExternalIdentifier implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4745993419307294953L;
	protected String systemDesignator;
	protected String systemIdentifier;
	
	public ExternalIdentifier(String systemDesignator,String systemIdentifier) {
		setSystemDesignator(systemDesignator);
		setSystemIdentifier(systemIdentifier);
	}
	public ExternalIdentifier() {
		this(null,null);
	}
	public String getSystemDesignator() {
		return systemDesignator;
	}
	public void setSystemDesignator(String systemDesignator) {
		if (systemDesignator!=null && systemDesignator.length()>64)
			this.systemDesignator = systemDesignator.substring(0,63);
		else	
			this.systemDesignator = systemDesignator;
	}
	public String getSystemIdentifier() {
		return systemIdentifier;
	}
	public void setSystemIdentifier(String systemIdentifier) {
		if (systemIdentifier!=null && systemIdentifier.length()>64)
			this.systemIdentifier = systemIdentifier.substring(0,63);
		else	
			this.systemIdentifier = systemIdentifier;		
	}
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("{\n");

		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape("type")));
		b.append(":\t");
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape(systemDesignator)));
		b.append(",\n");
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape("id")));
		b.append(":\t");
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape(systemIdentifier)));
		b.append("\n\t}");
		return b.toString();
	}
}
