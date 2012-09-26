package org.xmetdb.xmet.rest;

import java.util.UUID;

import net.idea.modbcum.i.config.Preferences;
import net.idea.restnet.aa.cookie.CookieAuthenticator;
import net.idea.restnet.aa.local.UserLoginPOSTResource;
import net.idea.restnet.aa.local.UserLogoutPOSTResource;
import net.idea.restnet.aa.resource.AdminRouter;
import net.idea.restnet.c.ChemicalMediaType;
import net.idea.restnet.c.routers.MyRouter;
import net.idea.restnet.db.aalocal.DBRole;
import net.idea.restnet.db.aalocal.DBVerifier;
import net.idea.restnet.db.aalocal.DbEnroller;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.security.Authorizer;
import org.restlet.security.RoleAuthorizer;
import org.restlet.security.SecretVerifier;
import org.restlet.security.User;
import org.restlet.service.TunnelService;
import org.xmetdb.rest.FreeMarkerApplicaton;
import org.xmetdb.rest.endpoints.EndpointsResource;
import org.xmetdb.rest.groups.OrganisationRouter;
import org.xmetdb.rest.groups.ProjectRouter;
import org.xmetdb.rest.protocol.ProtocolRouter;
import org.xmetdb.rest.protocol.facet.ProtocolsByEndpointResource;
import org.xmetdb.rest.protocol.resource.db.UnpublishedProtocolsResource;
import org.xmetdb.rest.structure.resource.DatasetResource;
import org.xmetdb.rest.structure.resource.StructureRouter;
import org.xmetdb.rest.user.UserRouter;
import org.xmetdb.rest.user.alerts.resource.AlertRouter;
import org.xmetdb.rest.user.resource.MyAccountResource;
import org.xmetdb.xmet.aa.XMETLoginFormResource;
import org.xmetdb.xmet.aa.XMETLoginPOSTResource;
import org.xmetdb.xmet.aa.XMETLogoutPOSTResource;
import org.xmetdb.xmet.aa.UserAuthorizer;
import org.xmetdb.xmet.client.XMETDBRoles;
import org.xmetdb.xmet.client.Resources;
import org.xmetdb.xmet.task.XMETAdminRouter;
import org.xmetdb.xmet.task.XMETEditorRouter;
import org.xmetdb.xmet.task.XMETTaskRouter;

/**
 * XMETDB web services / web application
 * 
 * @author nina
 * 
 */
public class XMETApplication extends FreeMarkerApplicaton<String> {
	/** The Freemarker's configuration. */
 

	public XMETApplication() {
		super();

		setName("xmetdb");
		setDescription("Xenobiotics metabolism database");
		setOwner("xmetdb.org");
		setAuthor("Developed by Ideaconsult Ltd. (2012) on behalf of xmetdb.org");
		setConfigFile("config/xmetdb.properties");

		setStatusService(new XMETStatusService());
		setTunnelService(new TunnelService(true, true) {
			@Override
			public Filter createInboundFilter(Context context) {
				return new XMETTunnelFilter(context);
			}
		});
		getTunnelService().setUserAgentTunnel(true);
		getTunnelService().setExtensionsTunnel(false);
		getTunnelService().setMethodTunnel(true);

		Preferences.setProperty(Preferences.MAXRECORDS, "0");

		getMetadataService().setEnabled(true);
		getMetadataService().addExtension("sdf",
				ChemicalMediaType.CHEMICAL_MDLSDF, true);
		getMetadataService().addExtension("mol",
				ChemicalMediaType.CHEMICAL_MDLMOL, true);
		getMetadataService().addExtension("inchi",
				ChemicalMediaType.CHEMICAL_INCHI, true);
		getMetadataService().addExtension("cml",
				ChemicalMediaType.CHEMICAL_CML, true);
		getMetadataService().addExtension("smiles",
				ChemicalMediaType.CHEMICAL_SMILES, true);

		if (isInsecure())
			insecureConfig();

	}

	@Override
	public Restlet createInboundRoot() {

		Router router = new MyRouter(this.getContext());
		// here we check if the cookie contains auth token, if not just consider
		// the user notlogged in
		Filter auth = createCookieAuthenticator(true);
		Router setCookieUserRouter = new MyRouter(getContext());
		auth.setNext(setCookieUserRouter);

		setCookieUserRouter
				.attach(Resources.login, XMETLoginFormResource.class);
		
		AlertRouter alertRouter = new AlertRouter(getContext());
		
		MyRouter myAccountRouter = new MyRouter(getContext());
		myAccountRouter.attachDefault(MyAccountResource.class);
		myAccountRouter.attach(Resources.alert,alertRouter);
		setCookieUserRouter
				.attach(Resources.myaccount, myAccountRouter);
		

		/** XMETDB observations & SOMs **/
		ProtocolRouter protocols = new ProtocolRouter(getContext());
		OrganisationRouter org_router = new OrganisationRouter(getContext());
		ProjectRouter projectRouter = new ProjectRouter(getContext());
		Restlet protocolRouter;
		

		
		protocolRouter = protocols; // createProtectedResource(protocols,"protocol",new
									// ProtocolAuthorizer());
		setCookieUserRouter.attach(Resources.protocol, protocolRouter);
		setCookieUserRouter.attach(Resources.project, projectRouter);
		setCookieUserRouter.attach(Resources.organisation, org_router);
		setCookieUserRouter.attach(Resources.user, createUserRouter(new UserRouter(getContext(),
				protocols, org_router, projectRouter, alertRouter)));
	

		setCookieUserRouter.attach(Resources.endpoint,
				ProtocolsByEndpointResource.class);
		setCookieUserRouter.attach(Resources.chemical, new StructureRouter(getContext()));

		setCookieUserRouter.attach(String.format("%s/{%s}",Resources.dataset,DatasetResource.datasetKey), DatasetResource.class);
		setCookieUserRouter.attach(Resources.admin, createAdminRouter());
		setCookieUserRouter.attach(Resources.editor, createEditorRouter());
		setCookieUserRouter.attach(Resources.unpublished, createUnpublishedRouter());
		setCookieUserRouter.attach(Resources.task, new XMETTaskRouter(
				getContext()));

		setCookieUserRouter.attach("", XMETWelcomeResource.class);
		setCookieUserRouter.attach("/", XMETWelcomeResource.class);
		
		
		Router endpointsRouter = new MyRouter(getContext());
		endpointsRouter.attachDefault(EndpointsResource.class);
		endpointsRouter.attach(EndpointsResource.resourceID,EndpointsResource.class);
		endpointsRouter.attach(EndpointsResource.resourceKey,EndpointsResource.class);
		endpointsRouter.attach(EndpointsResource.resourceTree, EndpointsResource.class);
		setCookieUserRouter.attach(EndpointsResource.resource, endpointsRouter);
		
		router.attach(auth);
		/**
		 * Images, styles, favicons, applets
		 */
		attachStaticResources(router);

		Router protectedRouter = new MyRouter(getContext());
		protectedRouter.attach("/roles", XMETLoginFormResource.class);
		protectedRouter.attach(
				String.format("/%s", UserLoginPOSTResource.resource),
				XMETLoginPOSTResource.class);
		protectedRouter.attach(
				String.format("/%s", UserLogoutPOSTResource.resource),
				XMETLogoutPOSTResource.class);

		auth = createCookieAuthenticator(false);
		auth.setNext(protectedRouter);
		router.attach("/protected", auth);


		router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
		router.setRoutingMode(Router.MODE_BEST_MATCH);
		/*
		 * StringWriter w = new StringWriter();
		 * XMETApplication.printRoutes(router, ">", w);
		 * System.out.println(w.toString());
		 */
		
        initFreeMarkerConfiguration();
		
        //"clap://class/templates"));
        
		return router;
	}


	/*
	 * protected Restlet createLocalAAVerifiedResource(Class clazz) {
	 * 
	 * Filter userAuthn = new
	 * ChallengeAuthenticatorDBLocal(getContext(),true,"conf/xmetdb.pref"
	 * ,"xmet_users"); userAuthn.setNext(clazz); return userAuthn; }
	 */
	protected Filter createCookieAuthenticator(boolean optional) {
		String secret = getProperty(Resources.Config.secret.name());
		CookieAuthenticator cookieAuth = new CookieAuthenticator(getContext(),
				"xmet_users", (secret==null?UUID.randomUUID().toString():secret).getBytes());
		cookieAuth.setCookieName("xmetdb");
		long sessionLength = 1000*60*45L; //45 min in milliseconds
		try { sessionLength = Long.parseLong(getProperty(Resources.Config.sessiontimeout.name())); } catch (Exception x) {}
		if (sessionLength<600000) sessionLength =  600000; //10 min in case the config is broken
		cookieAuth.setSessionLength(sessionLength);
		String config = "conf/xmetdb.pref";
		if (!optional) {
			// cookieAuth.setCookieName("subjectId");
			cookieAuth.setLoginFormPath("/login");
			cookieAuth.setLoginPath("/signin");
			cookieAuth.setLogoutPath("/signout");


			cookieAuth.setVerifier(new DBVerifier(getContext(), config,
					"xmet_users"));
			cookieAuth.setEnroler(new DbEnroller(getContext(), config,
					"xmet_users"));
			return cookieAuth;
		} else {
			cookieAuth.setVerifier(new SecretVerifier() {

				@Override
				public int verify(Request request, Response response) {
					int result = RESULT_VALID;

					if (request.getChallengeResponse() != null) {
						String identifier = getIdentifier(request, response);
						char[] secret = getSecret(request, response);
						if (verify(identifier, secret)) {
							request.getClientInfo().setUser(
									new User(identifier));
						}
					}
					return result;
				}

				@Override
				public boolean verify(String identifier, char[] secret) {
					return true;
				}

			});
			cookieAuth.setEnroler(new DbEnroller(getContext(), config,
					"xmet_users"));
		}
		return cookieAuth;
	}



	/**
	 * Resource /bookmark
	 * 
	 * @return
	 * 
	 *         protected Restlet createBookmarksRouter() { BookmarksRouter
	 *         bookmarkRouter = new BookmarksRouter(getContext());
	 * 
	 *         Filter bookmarkAuth = new
	 *         OpenSSOAuthenticator(getContext(),false,"opentox.org"); Filter
	 *         bookmarkAuthz = new BookmarksAuthorizer();
	 *         bookmarkAuth.setNext(bookmarkAuthz);
	 *         bookmarkAuthz.setNext(bookmarkRouter); return bookmarkAuth; }
	 */
	/**
	 * Resource /admin
	 * 
	 * @return
	 */
	protected Restlet createAdminRouter() {
		Authorizer authz = new SimpleRoleAndMethodAuthorizer(new DBRole(
				XMETDBRoles.xmetdb_manager.name(), XMETDBRoles.xmetdb_manager.toString()));
		authz.setNext(new XMETAdminRouter(getContext()));
		return authz;
	}

	/**
	 * Resource /editor
	 * 
	 * @return
	 */
	protected Restlet createEditorRouter() {
		Authorizer authz = new SimpleRoleAndMethodAuthorizer(new DBRole(
				XMETDBRoles.xmetdb_user.name(), XMETDBRoles.xmetdb_user.toString()));
		authz.setNext(new XMETEditorRouter(getContext()));
		
		return authz;
	}
	protected Restlet createUnpublishedRouter() {
		Authorizer authz = new SimpleRoleAndMethodAuthorizer(
				new DBRole(XMETDBRoles.xmetdb_editor.name(), XMETDBRoles.xmetdb_editor.toString()),
				new DBRole(XMETDBRoles.xmetdb_manager.name(), XMETDBRoles.xmetdb_manager.toString())
				);
		authz.setNext(new AdminRouter(getContext()) {
			@Override
			protected void init() {
				attachDefault(UnpublishedProtocolsResource.class);
			}
		});
		
		return authz;
	}
	/**
	 * Use {@link UserAuthorizer} unless explicitly xmet_protected is explicitly set to false.
	 * @param userRouter
	 * @return
	 */
	protected Restlet createUserRouter(UserRouter userRouter) {
		String aa = getProperty(Resources.Config.xmet_protected.name());
		if (aa==null) aa = getContext().getParameters().getFirstValue(Resources.Config.xmet_protected.name());
		boolean aaenabled = true;
		try {
			if (aa!=null)
				aaenabled = Boolean.parseBoolean(aa);
		} catch (Exception x) {aaenabled = true;}
		if (aaenabled) {
			Authorizer authz = new UserAuthorizer();
			authz.setNext(userRouter);
			return authz;
		} else return userRouter;
	}	
	/**
	 * Images, styles, icons Works if packaged as war only!
	 * 
	 * @return
	 */
	protected void attachStaticResources(Router router) {
		/*
		 * router.attach("/images",new Directory(getContext(),
		 * LocalReference.createFileReference("/webapps/images")));
		 */

		Directory metaDir = new Directory(getContext(), "war:///META-INF");
		Directory imgDir = new Directory(getContext(), "war:///images");
		Directory jmolDir = new Directory(getContext(), "war:///jmol");
		Directory jmeDir = new Directory(getContext(), "war:///jme");
		Directory styleDir = new Directory(getContext(), "war:///style");
		Directory scriptsDir = new Directory(getContext(), "war:///scripts");
		Directory jquery = new Directory(getContext(), "war:///jquery");

		router.attach("/meta/", metaDir);
		router.attach("/images/", imgDir);
		router.attach("/jmol/", jmolDir);
		router.attach("/jme/", jmeDir);
		router.attach("/jquery/", jquery);
		router.attach("/style/", styleDir);
		router.attach("/scripts/", scriptsDir);

	}


	
	/**
	 * Standalone, for testing mainly
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Create a component
		Component component = new XMETRESTComponent();
		final Server server = component.getServers().add(Protocol.HTTP, 8080);
		component.start();

		System.out.println("Server started on port " + server.getPort());
		System.out.println("Press key to stop server");
		System.in.read();
		System.out.println("Stopping server");
		component.stop();
		System.out.println("Server stopped");
	}

}

/**
 * GET allowed always; POST/PUT/DELETE depends on roles
 * 
 * @author nina
 * 
 */
class SimpleRoleAndMethodAuthorizer extends RoleAuthorizer {
	public SimpleRoleAndMethodAuthorizer(DBRole... roles) {
		super();
		for (DBRole role : roles)
			getAuthorizedRoles().add(role);
	}

	@Override
	public boolean authorize(Request request, Response response) {
		if ((request.getClientInfo() == null)
				|| (request.getClientInfo().getUser() == null)
				|| (request.getClientInfo().getUser().getIdentifier() == null))
			return false;
		// if (Method.GET.equals(request.getMethod()))
		// return true;
		return super.authorize(request, response);
	}

}
