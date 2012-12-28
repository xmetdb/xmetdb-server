package org.xmetdb.rest.protocol;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

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
import org.xmetdb.rest.groups.DBOrganisation;
import org.xmetdb.rest.groups.DBProject;
import org.xmetdb.rest.protocol.CallableProtocolUpload.UpdateMode;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
import org.xmetdb.rest.user.DBUser;
import org.xmetdb.xmet.client.AtomUncertainty;
import org.xmetdb.xmet.client.ProductAmount;
import org.xmetdb.xmet.client.PublishedStatus;

public class ProtocolFactory {
	protected static final String utf8 = "UTF-8";

	public enum ObservationFields {
		identifier, xmet_experiment, xmet_experimentdescription, published_status, 
		xmet_substrate_upload, xmet_product_upload, 
		xmet_substrate_uri, xmet_product_uri,
		project_uri, organisation_uri, user_uri, iduser, author_uri, 
		summarySearchable, status, xmlkeywords, 
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
				case xmet_substrate_uri : {
					DBAttachment attachment = createAttachment(fi, protocol,
							attachment_type.data_training, null);
					if (attachment != null)
						protocol.getAttachments().add(attachment);
					break;					
				}
				case xmet_product_uri : {
					DBAttachment attachment = createAttachment(fi, protocol,
							attachment_type.data_validation, null);
					if (attachment != null)
						protocol.getAttachments().add(attachment);
					break;					
				}
				case xmet_substrate_upload: {
					DBAttachment attachment = createAttachment(fi, protocol,
							attachment_type.data_training, dir);
					if (attachment != null)
						protocol.getAttachments().add(attachment);
					break;
				}
				case xmet_product_upload: {
					DBAttachment attachment = createAttachment(fi, protocol,
							attachment_type.data_validation, dir);
					if (attachment != null)
						protocol.getAttachments().add(attachment);
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
				case summarySearchable: {
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
						protocol.setProductAmount(ProductAmount.valueOf(fi
								.getString(utf8)));
					} catch (Exception x) {
						protocol.setProductAmount(ProductAmount.Unknown);
					}
					break;
				}
				case xmet_enzyme: {
					try {
						if (protocol.getEndpoint() == null)
							protocol.setEndpoint(new Enzyme(null, null));
						protocol.getEndpoint().setCode(fi.getString(utf8));
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
		return protocol;
	}

	protected static DBAttachment createAttachment(FileItem fi,
			DBProtocol protocol, attachment_type type, File dir)
			throws Exception {
		if (fi.getSize()==0) return null;
		if (fi.isFormField()) {
			// TODO URI or smiles specification of substrate & product
			// protocol.setDataTemplate(new Template(new
			// URL(fi.getString(utf8))))
			DBAttachment attachment = new DBAttachment();
			String newName = String.format("xmet%d_%s_%s",
					protocol.getID() > 0 ? protocol.getID() : 0,
					type.getXmetName(), DBProtocol.generateIdentifier()
					);
			attachment.setTitle(newName);
			attachment.setFormat("text/uri-list");
			attachment.setDescription(fi.getString(utf8));
			attachment.setType(type);
			attachment.setOriginalFileName(null);
			attachment.setImported(false);
			return attachment;
		} else {
			String originalName = "";
			String description = "";
			if (fi.getSize() == 0)
				return null;
			File file = null;
			if (fi.getName() == null)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
						"File name can't be empty!");
			else {
				try {
					if ((dir != null) && !dir.exists())
						dir.mkdir();
				} catch (Exception x) {
					dir = null;
				}
				try {
					dir = new File(dir == null ? new File(
							System.getProperty("java.io.tmpdir")) : dir,
							type.name());
					if ((dir != null) && !dir.exists())
						dir.mkdir();
				} catch (Exception x) {
					dir = null;
				}
				// stupid File class ...
				int lastIndex = fi.getName().lastIndexOf("\\");
				if (lastIndex < 0)
					lastIndex = fi.getName().lastIndexOf("/");
				description = stripFileName(fi.getName());
				int extIndex = fi.getName().lastIndexOf(".");
				String ext = extIndex > 0 ? fi.getName().substring(extIndex)
						: "";

				// generate new file name
				originalName = fi.getName();
				String newName = String.format("xmet%d_%s_%s%s",
						protocol.getID() > 0 ? protocol.getID() : 0,
						type.getXmetName(), DBProtocol.generateIdentifier(),
						ext);
				file = new File(String.format("%s/%s",
						dir == null ? System.getProperty("java.io.tmpdir")
								: dir, newName));
			}
			fi.write(file);

			return DBAttachment.file2attachment(file, description,
					originalName, type);

		}
	}


	public static String stripFileName(String fileName) {
		int lastIndex = fileName.lastIndexOf("\\");
		if (lastIndex < 0)
			lastIndex = fileName.lastIndexOf("/");
		return lastIndex > 0 ? fileName.substring(lastIndex + 1) : fileName;

	}
}
