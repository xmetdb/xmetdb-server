package org.xmetdb.rest.protocol;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.user.DBUser;
import net.toxbank.client.policy.AccessRights;
import net.toxbank.client.resource.Organisation;
import net.toxbank.client.resource.Project;
import net.toxbank.client.resource.Protocol;
import net.toxbank.client.resource.Protocol.STATUS;
import net.toxbank.client.resource.User;

import org.apache.commons.fileupload.FileItem;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.protocol.CallableProtocolUpload.UpdateMode;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
import org.xmetdb.xmet.client.AtomUncertainty;
import org.xmetdb.xmet.client.ProductAmount;
import org.xmetdb.xmet.client.PublishedStatus;

import ambit2.base.data.Property;
import ambit2.base.data.StructureRecord;
import ambit2.base.interfaces.IStructureRecord;

public class ProtocolFactory {
	protected static final String utf8 = "UTF-8";
	public enum StructureUploadType {uri,mol,file};
	public enum ObservationFields {
		identifier, xmet_experiment, xmet_experimentdescription, published_status, 
		xmet_substrate_type,xmet_product_type,
		xmet_substrate_upload, xmet_product_upload, 
		xmet_substrate_uri, xmet_product_uri,
		xmet_substrate_name, xmet_product_name,
		xmet_substrate_atoms, xmet_product_atoms,
		xmet_substrate_mol, xmet_product_mol,
		xmet_allele, xmet_reference, xmet_comments,
		project_uri, organisation_uri, user_uri, iduser, author_uri, 
		curated, status,  
		xmet_enzyme, xmet_atom_uncertainty, xmet_product_amount, allowReadByUser, allowReadByGroup;

		@Override
		public String toString() {
			String name = name();
			return String.format("%s%s", name.substring(0, 1).toUpperCase(),
					name.substring(1).toLowerCase());
		}
	}

	protected enum SupportedExperiments {
		HEP {
			public String toString() {
				return "Hepatocytes";
			}
		},
		ENZ {
			public String toString() {
				return "Enzymes";
			}
		},
		MS {
			public String toString() {
				return "Microsomes";
			}
		}
	}

	public static DBProtocol getProtocol(DBProtocol protocol,
			List<FileItem> items, long maxSize, File dir,
			AccessRights accessRights, UpdateMode updateMode)
			throws ResourceException {

		if (protocol == null)
			protocol = new DBProtocol();
		
		DBAttachment[] s_attachments = new DBAttachment[StructureUploadType.values().length];
		DBAttachment[] p_attachments = new DBAttachment[StructureUploadType.values().length];
		StructureUploadType s_upload = null;
		StructureUploadType p_upload = null;
		
		String[] substrate_atoms = null;
		String[] product_atoms = null;
		IStructureRecord[] structure = new IStructureRecord[attachment_type.values().length];

		for (final Iterator<FileItem> it = items.iterator(); it.hasNext();) {
			FileItem fi = it.next();

			try {
				ObservationFields field = null;
				try {
					String fname = fi.getFieldName();
					if (fname != null)
						field = ObservationFields.valueOf(fname);

				} catch (Exception x) {
					continue;
				}
				if (field == null)
					continue;
				switch (field) {
				case identifier: {
					String s = fi.getString(utf8);
					if ((s != null) && !"".equals(s))
						protocol.setIdentifier(s);
					break;
				}
				case xmet_substrate_type: {
					String s = fi.getString(utf8);
					try {if (s!=null) s_upload = StructureUploadType.valueOf(s.trim());} catch (Exception x) {}
					break;
				}
				case xmet_product_type: {
					String s = fi.getString(utf8);
					try {if (s!=null) p_upload = StructureUploadType.valueOf(s.trim());} catch (Exception x) {}
					break;
				}
				case published_status: {
					String s = fi.getString(utf8);
					if (s == null) {
						protocol.setPublished(false);
						break;
					}
					s = s.toLowerCase();
					try {
						if ("on".equals(s)) {
							protocol.setPublished(true);
							break;
						} else if ("".equals(s)) {
							protocol.setPublished(false);
							break;
						} else if ("true".equals(s)) {
							protocol.setPublished(true);
							break;
						} else if ("false".equals(s)) {
							protocol.setPublished(false);
							break;
						} else
							protocol.setPublishedStatus(PublishedStatus
									.valueOf(s));
					} catch (Exception x) {
						protocol.setPublished(false);
					}
					break;
				}
				case xmet_experimentdescription: {
					String s = fi.getString(utf8);
					if ((s != null) && !"".equals(s))
						protocol.setAbstract(s);
					break;
				}
				case xmet_substrate_atoms: {
					try { substrate_atoms = fi.getString(utf8).split(",");} catch (Exception x) {}
					break;
				}
				case xmet_product_atoms: {
					try { product_atoms = fi.getString(utf8).split(",");} catch (Exception x) {}
					break;
				}
				case xmet_substrate_uri : {
					if ((s_upload==null) || StructureUploadType.uri.equals(s_upload)) {
						DBAttachment attachment = createAttachment(StructureUploadType.uri,fi, protocol,
								attachment_type.substrate, dir);
						s_attachments[StructureUploadType.uri.ordinal()] = attachment;
					}
					break;					
				}
				case xmet_product_uri : {
					if ((p_upload==null) || StructureUploadType.uri.equals(p_upload)) {
						DBAttachment attachment = createAttachment(StructureUploadType.uri,fi, protocol,
								attachment_type.product, dir);
						p_attachments[StructureUploadType.uri.ordinal()] = attachment;
					}
					break;					
				}
				case xmet_substrate_name : {
					structure[attachment_type.substrate.ordinal()] = new StructureRecord();
					structure[attachment_type.substrate.ordinal()].setProperty(Property.getNameInstance(), fi.getString(utf8));
					break;
				}
				case xmet_product_name : {
					structure[attachment_type.product.ordinal()] = new StructureRecord();
					structure[attachment_type.product.ordinal()].setProperty(Property.getNameInstance(), fi.getString(utf8));
					break;
				}				
				case xmet_substrate_mol : {
					if ((s_upload==null) || StructureUploadType.mol.equals(s_upload)) {
						DBAttachment attachment = createAttachment(StructureUploadType.mol,fi, protocol,
								attachment_type.substrate, dir);
						s_attachments[StructureUploadType.mol.ordinal()] = attachment;
					}
					break;					
				}
				case xmet_product_mol : {
					if ((p_upload==null) || StructureUploadType.mol.equals(p_upload)) {
						DBAttachment attachment = createAttachment(StructureUploadType.mol,fi, protocol,
								attachment_type.product, dir);
						p_attachments[StructureUploadType.mol.ordinal()] = attachment;
					}
					break;					
				}		
				case xmet_substrate_upload: {
					if ((s_upload==null) || StructureUploadType.file.equals(s_upload)) {
						DBAttachment attachment = createAttachment(StructureUploadType.file,fi, protocol,
								attachment_type.substrate, dir);
						s_attachments[StructureUploadType.file.ordinal()] = attachment;
					}
					break;
				}
				case xmet_product_upload: {
					if ((p_upload==null) || StructureUploadType.file.equals(p_upload)) {
						DBAttachment attachment = createAttachment(StructureUploadType.file,fi, protocol,
								attachment_type.product, dir);
						p_attachments[StructureUploadType.file.ordinal()] = attachment;
					}
					break;
				}
					/*
					 * case document :{ DBAttachment attachment =
					 * createAttachment
					 * (fi,protocol,attachment_type.document,dir); if
					 * (attachment!=null)
					 * protocol.getAttachments().add(attachment); break; }
					 */
				case project_uri: {
					String s = fi.getString(utf8);
					if ((s != null) && !"".equals(s)) {
						Project p = protocol.getProject();
						if (p == null) {
							p = new DBProject();
							protocol.setProject(p);
						}
						if (s.startsWith("http"))
							p.setResourceURL(new URL(s));
						else
							p.setTitle(s);
					}
					break;
				}
				case user_uri: {
					String s = fi.getString(utf8);
					if ((s != null) && !"".equals(s)) {
						User p = protocol.getOwner();
						if (p == null) {
							p = new DBUser();
							protocol.setOwner(p);
						}
						if (s.startsWith("http"))
							p.setResourceURL(new URL(s));
						else
							p.setUserName(s);
					}
					break;
				}
				case organisation_uri: {
					String s = fi.getString(utf8);
					if ((s != null) && !"".equals(s)) {
						Organisation p = protocol.getOrganisation();
						if (p == null) {
							p = new DBOrganisation();
							protocol.setOrganisation(p);
						}
						if (s.startsWith("http"))
							p.setResourceURL(new URL(s));
						else
							p.setTitle(fi.getString());
					}
					break;
				}
				case author_uri: {
					String s = fi.getString(utf8);
					if ((s != null) && s.startsWith("http"))
						protocol.addAuthor(new DBUser(new URL(s)));
					break;
				}

				case xmet_experiment: {
					String s = fi.getString(utf8);
					if ((s != null) && !"".equals(s)) {
						protocol.setTitle(s);
						if (protocol.getAbstract() == null)
							try {
								protocol.setAbstract(SupportedExperiments
										.valueOf(s).toString());
							} catch (Exception x) {
								protocol.setAbstract(s);
							}
					}
					break;
				}
					/*
					 * case iduser: { String s = fi.getString(utf8); if
					 * ((s!=null) && !"".equals(s)) { DBUser user = new
					 * DBUser(); if (s.startsWith("http"))
					 * user.setResourceURL(new URL(s)); else user.setTitle(s);
					 * protocol.setOwner(user); } break; }
					 */
				case curated: {
					try {
						protocol.setSearchable(Boolean.parseBoolean(fi
								.getString(utf8)));
					} catch (Exception x) {
						protocol.setSearchable(false);
					}
					break;
				}
				case status: {
					try {
						protocol.setStatus(Protocol.STATUS.valueOf(fi
								.getString(utf8)));
					} catch (Exception x) {
						protocol.setStatus(STATUS.RESEARCH);
					}
					break;
				}
				case xmet_atom_uncertainty: {
					try {
						protocol.setAtomUncertainty(AtomUncertainty.valueOf(fi
								.getString(utf8)));
					} catch (Exception x) {
						protocol.setAtomUncertainty(AtomUncertainty.Uncertain);
					}
					break;
				}
				case xmet_product_amount: {
					try {
						String val = fi.getString(utf8);
						protocol.setProductAmount(ProductAmount.valueOf(val));
					} catch (Exception x) {
						protocol.setProductAmount(ProductAmount.Unknown);
					}
					break;
				}
				case xmet_enzyme: {
					try {
						if (protocol.getEndpoint() == null)
							protocol.setEndpoint(new Enzyme());
						protocol.getEndpoint().setCode(fi.getString(utf8));
					} catch (Exception x) {
					}
					break;
				}
				case xmet_reference: {
					try {
						protocol.setReference(fi.getString(utf8));
					} catch (Exception x) {
					}
					break;					
				}
				case xmet_comments: {
					try {
						protocol.getKeywords().clear();
						protocol.addKeyword(fi.getString(utf8));
					} catch (Exception x) {
					}
					break;					
				}							
				case xmet_allele: {
					try {
						if (protocol.getEndpoint() == null)
							protocol.setEndpoint(new Enzyme());
						protocol.getEndpoint().setAlleles(new String[] {fi.getString(utf8)});
					} catch (Exception x) {
					}
					break;					
				}
				case allowReadByUser: {
					String s = fi.getString(utf8);
					if ((s != null) && !"".equals(s))
						try {
							DBUser user = null;
							// a bit of heuristic
							if (s.startsWith("http")) {
								user = new DBUser(new URL(s.trim()));
							} else {
								user = new DBUser();
								user.setUserName(s.trim());
							}
							accessRights.addUserRule(user, true, null, null,
									null);
						} catch (Exception x) {
							x.printStackTrace();
						}
					break;
				}
				case allowReadByGroup: {
					String s = fi.getString(utf8);
					if ((s != null) && !"".equals(s))
						try {
							String uri = s.trim();
							// hack to avoid queries...
							if (uri.indexOf("/organisation") > 0) {
								DBOrganisation org = null;
								if (s.startsWith("http")) {
									org = new DBOrganisation(new URL(s.trim()));
								} else {
									org = new DBOrganisation();
									org.setGroupName(s.trim());
								}
								accessRights.addGroupRule(org, true, null,
										null, null);

							} else if (uri.indexOf("/project") > 0) {
								DBProject org = null;
								if (s.startsWith("http")) {
									org = new DBProject(new URL(s.trim()));
								} else {
									org = new DBProject();
									org.setGroupName(s.trim());
								}
								accessRights.addGroupRule(org, true, null,
										null, null);
							}
						} catch (Exception x) {
							x.printStackTrace();
						}
					break;
				}
				} // switch
			} catch (ResourceException x) {
				throw x;
			} catch (Exception x) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, x);
			}

		}
		if ((s_upload!=null) && (s_attachments[s_upload.ordinal()]!=null)) {
				s_attachments[s_upload.ordinal()].setAtomNumbers(substrate_atoms);
				protocol.getAttachments().add(s_attachments[s_upload.ordinal()]);
		}
		if ((p_upload!=null) && (p_attachments[p_upload.ordinal()]!=null)) {
			p_attachments[p_upload.ordinal()].setAtomNumbers(product_atoms);
			protocol.getAttachments().add(p_attachments[p_upload.ordinal()]);
		}
		for (DBAttachment attachment : protocol.getAttachments()) 
			if (structure[attachment.getType().ordinal()]!=null)
				attachment.setStructure(structure[attachment.getType().ordinal()]);
		

		return protocol;
	}

	protected static DBAttachment createAttachment(StructureUploadType supType,FileItem fi,
			DBProtocol protocol, attachment_type type, File dir)
			throws Exception {
		if (fi.getSize()==0) return null;
		try {if ((dir != null) && !dir.exists())	dir.mkdir();} catch (Exception x) {	dir = null;	}
		File rootDir = dir == null ? new File(System.getProperty("java.io.tmpdir")) : dir;
		try {
			dir = new File(rootDir,type.name());
			if ((dir != null) && !dir.exists())	dir.mkdir();
		} catch (Exception x) {	dir = null;	}
		
		DBAttachment attachment  = null;
		if (fi.isFormField()) {
			if (StructureUploadType.uri.equals(supType)) {
				attachment = new DBAttachment();
				String newName = String.format("xmet%d_%s_%s",
						protocol.getID() > 0 ? protocol.getID() : 0,
						type.getXmetName(), DBProtocol.generateIdentifier()
						);
				attachment.setTitle(newName);
				attachment.setType(type);
				attachment.setOriginalFileName(null);
				attachment.setDescription(fi.getString(utf8));
				attachment.setFormat("text/uri-list");
				attachment.setImported(false);
				return attachment;
			} else {
				String content = fi.getString(utf8);
				File file = generateFileName("wwwformsubmitted.sdf", dir, type, protocol);
				FileWriter writer=null;
				try {
					writer = new FileWriter(file);
					writer.write(content);
				} finally {
					try {writer.close();} catch (Exception x) {}
				}
				//System.out.println(file.getAbsolutePath());
				if (file.exists())
						return DBAttachment.file2attachment(rootDir.getAbsolutePath(),file, "Web form MOL file","form.sdf", type);
				else return null;
			}
		} else {
		//otherwise it is a file
			String originalName= "";
			String description = "";
			if (fi.getSize() == 0)	return null;
			File file = null;
			if (fi.getName() == null)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"File name can't be empty!");
			else {
				originalName = fi.getName();
				description = stripFileName(fi.getName());
				file = generateFileName(fi.getName(),dir,type,protocol);
			}
			fi.write(file);
			return  DBAttachment.file2attachment(rootDir.getAbsolutePath(),file, description,originalName, type);
		}

	}

	protected static File generateFileName(String filename, File dir, attachment_type type, DBProtocol protocol) {

		// stupid File class ...
		int lastIndex = filename.lastIndexOf("\\");
		if (lastIndex < 0)	lastIndex = filename.lastIndexOf("/");
		int extIndex = filename.lastIndexOf(".");
		String ext = extIndex > 0 ? filename.substring(extIndex): "";
			// generate new file name
		String newName = String.format("xmet%d_%s_%s%s",
				protocol.getID() > 0 ? protocol.getID() : 0,
				type.getXmetName(), DBProtocol.generateIdentifier(),ext);
		return  new File(String.format("%s/%s",
				dir == null ? System.getProperty("java.io.tmpdir")	: dir, newName));
	}
	public static String stripFileName(String fileName) {
		int lastIndex = fileName.lastIndexOf("\\");
		if (lastIndex < 0)
			lastIndex = fileName.lastIndexOf("/");
		return lastIndex > 0 ? fileName.substring(lastIndex + 1) : fileName;

	}
}
