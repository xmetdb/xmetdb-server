package org.xmetdb.rest.protocol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.toxbank.client.resource.Protocol;
import net.toxbank.client.resource.Template;

import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.xmet.client.AtomUncertainty;
import org.xmetdb.xmet.client.ProductAmount;
import org.xmetdb.xmet.client.PublishedStatus;

public class DBProtocol extends Protocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6632168193661223228L;
	protected int ID;
	protected int year;
	public static final String QMRFNUMBER = "qmrf_number";
	protected Enzyme endpoint;
	protected PublishedStatus publishedStatus = PublishedStatus.draft;
	protected AtomUncertainty atomUncertainty = AtomUncertainty.Uncertain;
	public AtomUncertainty getAtomUncertainty() {
		return atomUncertainty;
	}

	public void setAtomUncertainty(AtomUncertainty atomUncertainty) {
		this.atomUncertainty = atomUncertainty;
	}

	public ProductAmount getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(ProductAmount productAmount) {
		this.productAmount = productAmount;
	}

	protected ProductAmount productAmount = ProductAmount.Unknown;

	public Enzyme getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Enzyme endpoint) {
		this.endpoint = endpoint;
	}

	protected List<DBAttachment> attachments;
//	public static String prefix = "QMRF";
	
	public List<DBAttachment> getAttachments() {
		if (attachments==null) attachments = new ArrayList<DBAttachment>();
		return attachments;
	}

	public DBProtocol() {
		
	}
	
	/*
	public DBProtocol(int id, int version, int year) {
		setID(id);
		setVersion(version);
		setYear(year);
	}
	*/
	public DBProtocol(String identifier) {
		super();
		setIdentifier(identifier);
	}
	
	@Override
	public void setIdentifier(String identifier) {
		super.setIdentifier(identifier==null?null:identifier.length()>36?identifier.substring(0,35):identifier.trim());
	}
	
	public boolean isValidIdentifier() {
		return getIdentifier()!=null && !"".equals(getIdentifier());
	}
	

	public String getVisibleIdentifier() {
		return getIdentifier();
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public String toString() {
		return getResourceURL()==null?getTitle()==null?"Document":getTitle():
			String.format("<a href='%s'>%s</a>",getResourceURL(),getTitle()==null?getResourceURL():getTitle());
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public void setSubmissionDate(Long submissionDate) {
		super.setSubmissionDate(submissionDate);
		Date date = new Date(submissionDate);
		SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
		setYear(Integer.parseInt(simpleDateformat.format(date)));
	}
	
	public static String generateIdentifier() {
		return UUID.randomUUID().toString(); 
		/*
		Base32 b32 = new Base32();
		try {
			String id = new String(b32.encode(UUID.randomUUID().toString().getBytes()));
			System.out.println(id  + " " + id.length());
			return id;
		} catch (Exception x) {
			x.printStackTrace();
			return UUID.randomUUID().toString(); 
		}
		*/
	}

	@Override
	public Boolean isPublished() {
		return PublishedStatus.published==publishedStatus;
	}
	@Override
	public void setPublished(Boolean isPublished) {
		publishedStatus = isPublished?PublishedStatus.published:PublishedStatus.draft;
	}
	
	public PublishedStatus getPublishedStatus() {
		return publishedStatus;
	}

	public void setPublishedStatus(PublishedStatus publishedStatus) {
		this.publishedStatus = publishedStatus;
	}
	
	public void setReference(String reference) {
		if (getDataTemplate()==null) setDataTemplate(new Template());
		getDataTemplate().setTitle(reference);
	}
	public String getReference() {
		if (getDataTemplate()==null) return null;
		else return getDataTemplate().getTitle();
	}
}
