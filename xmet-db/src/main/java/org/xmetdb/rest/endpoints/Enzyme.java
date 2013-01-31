package org.xmetdb.rest.endpoints;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

public class Enzyme implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5987449761837302780L;
	public enum EnzymeFields {
		id {
			@Override
			public String leftDelimiter() { return "";}
			@Override
			public String rightDelimiter() { return "";}
			@Override
			public String getStringValue(Enzyme enzyme,String quotes) {
				return Integer.toString(enzyme.getId());
			}
			@Override
			public Object getValue(Enzyme enzyme) {
				return enzyme.getId();
			}	
		},
		code {
			@Override
			public String getStringValue(Enzyme enzyme,String quotes) {
				return enzyme.getCode();
			}			
			@Override
			public Object getValue(Enzyme enzyme) {
				return enzyme.getCode();
			}	
		},
		name {
			@Override
			public String getStringValue(Enzyme enzyme,String quotes) {
				return enzyme.getName();
			}		
			@Override
			public Object getValue(Enzyme enzyme) {
				return enzyme.getName();
			}	
		},
		uniprot {
			@Override
			public String getStringValue(Enzyme enzyme,String quotes) {
				return enzyme.getUniprot_id();
			}		
			@Override
			public Object getValue(Enzyme enzyme) {
				return enzyme.getUniprot_id();
			}			
		},
		alleles {
			@Override
			public String leftDelimiter() { return "[";}
			@Override
			public String rightDelimiter() { return "]";}
			@Override
			public String getStringValue(Enzyme enzyme,String quotes) {
				if (enzyme.getAlleles()==null) return null; 
				StringBuilder alleles = new StringBuilder();
				String delimiter = "";
				for (String allele: enzyme.alleles) {
					alleles.append(delimiter);
					alleles.append(quotes);
					alleles.append(allele);
					alleles.append(quotes);
					delimiter = ",";
				}
				return alleles.toString();
			}		
			@Override
			public Object getValue(Enzyme enzyme) {
				return enzyme.getAlleles();
			}
		};
		public String leftDelimiter() { return "\"";}
		public String rightDelimiter() { return "\"";}
		public abstract String getStringValue(Enzyme enzyme,String quotes);
		public abstract Object getValue(Enzyme enzyme);
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
	protected String uniprot_id = null;


	public String getUniprot_id() {
		return uniprot_id;
	}

	public void setUniprot_id(String uniprot_id) {
		this.uniprot_id = uniprot_id;
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
	
	public void toJSON(Writer writer) throws IOException {
		writer.write(String.format("\n{"));
		for (EnzymeFields field : EnzymeFields.values()) {
			if (!EnzymeFields.id.equals(field))
				writer.write(",");
			writer.write("\"");
			writer.write(field.name());
			writer.write("\":");
			String value = field.getStringValue(this,"\"");
			if (value==null) writer.write(value);
			else {
				writer.write(field.leftDelimiter());
				writer.write(value);
				writer.write(field.rightDelimiter());
			}
		}
		writer.write(String.format("}"));
	}
}
