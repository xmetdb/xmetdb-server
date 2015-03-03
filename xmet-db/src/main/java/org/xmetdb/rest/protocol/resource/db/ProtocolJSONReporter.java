package org.xmetdb.rest.protocol.resource.db;

import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.idea.modbcum.i.IQueryCondition;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.modbcum.p.MasterDetailsProcessor;
import net.idea.modbcum.r.QueryReporter;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.groups.IDBGroup;
import net.idea.restnet.groups.resource.GroupQueryURIReporter;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.Request;
import org.xmetdb.rest.endpoints.Enzyme;
import org.xmetdb.rest.endpoints.db.ReadEnzymeByObservation;
import org.xmetdb.rest.links.ExternalIdentifier;
import org.xmetdb.rest.links.db.ReadObservationIdentifiers;
import org.xmetdb.rest.protocol.DBProtocol;
import org.xmetdb.rest.protocol.attachments.AttachmentURIReporter;
import org.xmetdb.rest.protocol.attachments.DBAttachment;
import org.xmetdb.rest.protocol.attachments.DBAttachment.attachment_type;
import org.xmetdb.rest.protocol.attachments.db.ReadAttachment;

public class ProtocolJSONReporter extends
		QueryReporter<DBProtocol, IQueryRetrieval<DBProtocol>, Writer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected String queryService;
	protected String comma = null;
	protected QueryURIReporter uriReporter;
	protected AttachmentURIReporter<IQueryRetrieval<DBAttachment>> attachmentReporter;
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MMM dd yyyy");

	public QueryURIReporter getUriReporter() {
		return uriReporter;
	}

	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;

	public ProtocolJSONReporter(Request request, String queryService) {
		super();
		this.queryService = queryService;
		uriReporter = new ProtocolQueryURIReporter<IQueryRetrieval<DBProtocol>>(
				request);
		groupReporter = new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(
				request);
		userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
		attachmentReporter = new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(
				request);
		getProcessors().clear();
		// reads enzymes
		IQueryRetrieval<Enzyme> queryE = new ReadEnzymeByObservation(null);
		MasterDetailsProcessor<DBProtocol, Enzyme, IQueryCondition> enzymeReader = new MasterDetailsProcessor<DBProtocol, Enzyme, IQueryCondition>(
				queryE) {
			@Override
			protected DBProtocol processDetail(DBProtocol target, Enzyme detail)
					throws Exception {
				target.setEndpoint(detail);
				return target;
			}
		};
		getProcessors().add(enzymeReader);

		// Reads external links
		IQueryRetrieval<ExternalIdentifier> queryL = new ReadObservationIdentifiers();
		MasterDetailsProcessor<DBProtocol, ExternalIdentifier, IQueryCondition> linksReader = new MasterDetailsProcessor<DBProtocol, ExternalIdentifier, IQueryCondition>(
				queryL) {
			@Override
			public DBProtocol process(DBProtocol obs) throws Exception {
				obs.setExternalids(null);
				return super.process(obs);
			}

			@Override
			protected DBProtocol processDetail(DBProtocol target,
					ExternalIdentifier detail) throws Exception {
				if (target.getExternalids() == null)
					target.setExternalids(new ArrayList<ExternalIdentifier>());
				target.getExternalids().add(detail);
				return target;
			}
		};
		getProcessors().add(linksReader);
		// Reads attachment links
		IQueryRetrieval<DBAttachment> queryP = new ReadAttachment(null, null);
		MasterDetailsProcessor<DBProtocol, DBAttachment, IQueryCondition> attachmentReader = new MasterDetailsProcessor<DBProtocol, DBAttachment, IQueryCondition>(
				queryP) {
			@Override
			protected DBProtocol processDetail(DBProtocol target,
					DBAttachment detail) throws Exception {

				detail.setResourceURL(new URL(attachmentReporter.getURI(detail)));
				target.getAttachments().add(detail);
				return target;
			}
		};
		getProcessors().add(attachmentReader);

		processors.add(new DefaultAmbitProcessor<DBProtocol, DBProtocol>() {
			@Override
			public DBProtocol process(DBProtocol target) throws AmbitException {
				try {
					processItem(target);
				} catch (AmbitException x) {
					throw x;
				} catch (Exception x) {
					throw new AmbitException(x);
				}
				return target;
			};
		});
	}

	@Override
	public void header(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("{\"observations\": [\n");
		} catch (Exception x) {
		}

	}

	private static String format = "\n{\n\t\"uri\":\"%s\",\n\t\"identifier\": \"%s\",\n\t\"title\": \"%s\",\n\t\"description\": \"%s\",\n\t\"atom_uncertainty\": \"%s\",\n\t\"product_amount\": \"%s\",\n\t\"enzyme\": {\n\t\t\"id\" :%d,\"code\" :%s, \"name\" :%s, \"uniprot\" :%s\n\t},\n\t\"reference\": \"%s\",\n\t\"comments\": \"%s\",\n\t\"updated\": \"%s\",\n\t\"curated\": %s,\n\t\"publishedStatus\": \"%s\",\n\t\"owner\": {\n\t\t\"uri\" :\"%s\",\n\t\t\"username\": \"%s\"\n\t},\n\t\"externalIdentifiers\":[%s]";
	private static String formatAttachments = ",\n\t\"%s\": {\n\t\t\"dataset\": {\"uri\": %s, \"structure\": null}\n\t}";
	private static String emptyAttachments = ",\n\t\"%s\": {\n\t\t\"dataset\": {\"uri\": null, \"structure\": null}\n\t}";

	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {
			if (comma != null)
				getOutput().write(comma);
			String uri = uriReporter.getURI(item);

			if ((item.getProject() != null)
					&& (item.getProject().getResourceURL() == null))
				item.getProject().setResourceURL(
						new URL(groupReporter.getURI((DBProject) item
								.getProject())));
			if ((item.getOrganisation() != null)
					&& (item.getOrganisation().getResourceURL() == null))
				item.getOrganisation().setResourceURL(
						new URL(groupReporter.getURI((DBOrganisation) item
								.getOrganisation())));
			if ((item.getOwner() != null)
					&& (item.getOwner().getResourceURL() == null))
				item.getOwner().setResourceURL(
						new URL(userReporter.getURI((DBUser) item.getOwner())));

			String reference = item.getReference();
			List<String> comments = item.getKeywords();

			StringBuilder eids = new StringBuilder();
			if (item.getExternalids() != null) {
				String d = "";
				for (ExternalIdentifier id : item.getExternalids()) {
					eids.append(d);
					eids.append(id.toString());
					d = ",";
				}
			}

			getOutput().write(
					String.format(
							format,
							uri,
							item.getVisibleIdentifier(),
							item.getTitle(),
							item.getAbstract(),
							item.getAtomUncertainty().name(),
							item.getProductAmount().name(),
							(item.getEndpoint() == null || item.getEndpoint()
									.getID() > 0) ? item.getEndpoint().getID()
									: -1,
							(item.getEndpoint() == null || item.getEndpoint()
									.getCode() == null) ? "null" : String
									.format("\"%s\"", item.getEndpoint()
											.getCode()),
							(item.getEndpoint() == null || item.getEndpoint()
									.getName() == null) ? "null" : String
									.format("\"%s\"", jsonEscape(item
											.getEndpoint().getName())),
							(item.getEndpoint() == null || item.getEndpoint()
									.getUniprot_id() == null) ? "null" : String
									.format("\"%s\"", item.getEndpoint()
											.getUniprot_id()),
							reference == null ? "" : reference,
							comments == null ? "" : comments.size() == 0 ? ""
									: jsonEscape(comments.get(0)), dateFormat
									.format(new Date(item.getTimeModified())),
							item.isSearchable(), item.getPublishedStatus(),
							item.getOwner().getResourceURL(), item.getOwner()
									.getUserName(), eids.toString()

					));
			for (attachment_type at : attachment_type.values()) {
				int count = 0;
				if ((item.getAttachments() != null)
						&& (item.getAttachments().size() > 0))
					for (DBAttachment attachment : item.getAttachments())
						if (at.equals(attachment.getType())) {
							getOutput()
									.write(String
											.format(formatAttachments,
													attachment.getType()
															.toString(),
													attachment
															.getIdquerydatabase() > 0 ? String
															.format("\"%s/dataset/R%d\"",
																	queryService,
																	attachment
																			.getIdquerydatabase())
															: "null"));
							count++;
						}
				// there could be missing attachment, but we need JSON filled in
				if (count == 0)
					getOutput().write(
							String.format(emptyAttachments, at.toString()));
			}
			// \"substrate\":{\"uri\":\"%s\"},\n\"product\":{\"uri\":%s%s%s}
			getOutput().write("\n}");
			comma = ",";
		} catch (Exception x) {
			x.printStackTrace();
		}
		return item;
	}

	@Override
	public void footer(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("\n]\n}");
		} catch (Exception x) {
		}
	}

	public static String jsonEscape(String value) {
		return value.replace("\\", "\\\\").replace("/", "\\/")
				.replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n")
				.replace("\r", "\\r").replace("\t", "\\t")
				.replace("\"", "\\\"");
	}

}
