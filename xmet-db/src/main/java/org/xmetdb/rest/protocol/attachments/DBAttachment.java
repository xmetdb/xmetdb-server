package org.xmetdb.rest.protocol.attachments;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import net.toxbank.client.resource.Document;

import org.xmetdb.rest.protocol.DBProtocol;


public class DBAttachment extends Document {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1141527775736093041L;
	
	public enum attachment_type {
		substrate {
			@Override
			public String toString() {
				return "Substrate";
			}
			@Override
			public String getDescription() {
				return "SDF, MOL, CSV, XLS formats";
			}
			@Override
			public String getXmetName() {
				return "substrate";
			}
		},
		product {
			@Override
			public String toString() {
				return "Product";
			}
			@Override
			public String getDescription() {
				return "SDF, MOL, CSV, XLS formats";
			}	
			@Override
			public String getXmetName() {
				return "product";
			}
		};
		public String getDescription() { return toString();}
		public int maxFiles() { return 3;}
		public String acceptFormats() { return "sdf|mol|csv|xls"; };
		public String getXmetName() {return name();};
		
	}
	
	protected int idquerydatabase= -1;
	protected DBProtocol protocol = null;
	public DBProtocol getProtocol() {
		return protocol;
	}
	public void setProtocol(DBProtocol protocol) {
		this.protocol = protocol;
	}
	public int getIdquerydatabase() {
		return idquerydatabase;
	}
	public void setIdquerydatabase(int idquerydatabase) {
		this.idquerydatabase = idquerydatabase;
	}
	protected attachment_type type;
	protected String description;
	protected String format;
	protected String[] atomNumbers;
	
	public String[] getAtomNumbers() {
		return atomNumbers;
	}
	public void setAtomNumbers(String[] atomNumbers) {
		this.atomNumbers = atomNumbers;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public boolean isImported() {
		return imported;
	}
	public void setImported(boolean imported) {
		this.imported = imported;
	}
	protected String originalFileName;
	protected boolean imported;
	protected int ID;
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	public DBAttachment(int id) {
		this.ID = id;
	}
	public DBAttachment() {
		this(null);
	}
	public DBAttachment(URL resourceURL) {
		super(resourceURL);
	}
	public DBAttachment(URL resourceURL, String mediaType) {
		super(resourceURL);
		setMediaType(mediaType);
	}
	
	public attachment_type getType() {
		return type;
	}
	public void setType(attachment_type type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return String.format("%s %s",getType().getDescription(),getTitle()==null?"":getTitle());
	}
	
	@Override
	public void setResourceURL(URL resourceURL) {
		// TODO Auto-generated method stub
		super.setResourceURL(resourceURL);
	}
	public static DBAttachment file2attachment(String dir,File file, String description, String originalFileName, attachment_type type) {
		DBAttachment attachment = new DBAttachment();
		int extindex = file.getName().lastIndexOf(".");
		if (extindex>0) {
			attachment.setFormat(file.getName().substring(extindex+1));
			attachment.setTitle(file.getName().substring(0,extindex));
		} else {
			attachment.setTitle(file.getName());
			attachment.setFormat("");
		}
		attachment.setDescription(description);
		attachment.setType(type);
		attachment.setOriginalFileName(originalFileName);
		attachment.setImported(false);
		try {
			attachment.setResourceURL(attachment.getPath(dir));
		} catch (Exception x) { 
			x.printStackTrace();
		}
		return attachment;
	}

	public static URL getPath(String dir,String name,String type,String format) throws MalformedURLException {
		File file = new File(String.format("/%s/%s/%s.%s",dir,type,name.replace(" ","%20"),format));
		return file.toURL();
	}
	public URL getPath(String dir) throws MalformedURLException {
		return getPath(dir,getTitle(),getType().name(),getFormat());
	}

}
