package org.xmetdb.rest.links;

import java.io.Serializable;
import java.net.URLEncoder;


public class ExternalIdentifier implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4745993419307294953L;
	/**
	 * 
	 */
	protected String systemDesignator;
	protected String systemIdentifier;
	protected String xmetid;
	
	public String getXmetid() {
		return xmetid;
	}
	public void setXmetid(String xmetid) {
		this.xmetid = xmetid;
	}
	public enum ExternalIdentifierFields {
		xmetid {
			@Override
			public String getStringValue(ExternalIdentifier eid,String quotes) {
				return eid.getXmetid();
			}
			@Override
			public Object getValue(ExternalIdentifier eid) {
				return eid.getXmetid();
			}	
		},
		type {

			@Override
			public String getStringValue(ExternalIdentifier eid,String quotes) {
				return eid.getSystemDesignator();
			}
			@Override
			public Object getValue(ExternalIdentifier eid) {
				return eid.getSystemDesignator();
			}	
		},
		value {
			@Override
			public String getStringValue(ExternalIdentifier eid,String quotes) {
				return eid.getSystemIdentifier();
			}			
			@Override
			public Object getValue(ExternalIdentifier eid) {
				return eid.getSystemIdentifier();
			}	
		};
		public abstract String getStringValue(ExternalIdentifier eid,String quotes);
		public abstract Object getValue(ExternalIdentifier eid);
	}
	public ExternalIdentifier(String xmetid,String systemDesignator,String systemIdentifier) {
		this(systemDesignator,systemIdentifier);
		setXmetid(xmetid);
	}
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
	public String toURI() {
		return String.format("/%s/link/%s/%s", URLEncoder.encode(getXmetid()),URLEncoder.encode(getSystemDesignator()),URLEncoder.encode(getSystemIdentifier()));
	}
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("{\n");
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape(ExternalIdentifierFields.xmetid.name())));
		b.append(":\t");
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape(xmetid)));
		b.append(",\n");		
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape(ExternalIdentifierFields.type.name())));
		b.append(":\t");
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape(systemDesignator)));
		b.append(",\n");
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape(ExternalIdentifierFields.value.name())));
		b.append(":\t");
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape(systemIdentifier)));
		b.append(",\n");		
		b.append(JSONUtils.jsonQuote(JSONUtils.jsonEscape("id")));
		b.append(":\t");
		b.append(JSONUtils.jsonQuote(toURI()));
		b.append("\n\t}");
		return b.toString();
	}
}
