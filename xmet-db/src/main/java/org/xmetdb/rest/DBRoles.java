package org.xmetdb.rest;

import java.util.List;

import net.idea.restnet.db.aalocal.DBRole;

import org.restlet.security.Role;
import org.xmetdb.xmet.client.XMETDBRoles;

/**
 * Singleton helper class
 * @author nina
 *
 */
public class DBRoles {
	public static final DBRole adminRole = new DBRole(XMETDBRoles.xmetdb_admin.name(),XMETDBRoles.xmetdb_admin.toString());
	public static final DBRole curatorRole = new DBRole(XMETDBRoles.xmetdb_curator.name(),XMETDBRoles.xmetdb_curator.toString());
	public static final DBRole userRole = new DBRole(XMETDBRoles.xmetdb_user.name(),XMETDBRoles.xmetdb_user.toString());

	private DBRoles() {
		
	}
	public static boolean isAdminOrCurator(List<Role> roles) {
		return (roles==null)?false:(
			 		(roles.indexOf(DBRoles.adminRole)>=0)||(roles.indexOf(DBRoles.curatorRole) >=0)
			 		);
	}	   
	public static boolean isAdmin(List<Role> roles) {
		return (roles==null)?false:(roles.indexOf(DBRoles.adminRole)>=0);
	}
	public static boolean isCurator(List<Role> roles) {
		return (roles==null)?false:(roles.indexOf(DBRoles.curatorRole)>=0);
	}
	public static boolean isUser(List<Role> roles) {
		return (roles==null)?false:(roles.indexOf(DBRoles.userRole)>=0);
	}
}
