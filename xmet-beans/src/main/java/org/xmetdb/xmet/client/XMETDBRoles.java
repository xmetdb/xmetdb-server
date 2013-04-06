package org.xmetdb.xmet.client;



public enum XMETDBRoles {
	
	xmetdb_user {
		@Override
		public String toString() {
			return "Author";
		}
		@Override
		public String getHint() {
			return "Registered user and XMetDB documents author";
		}		
	},
	xmetdb_curator {
		//curate existing observations
		@Override
		public String getURI() {
			return Resources.editor;
		}
		@Override
		public String toString() {
			return "Curator";
		}
		@Override
		public String getHint() {
			return "Curating existing obsevations";
		}
	},
	xmetdb_admin {
		//can do sysadmin
		@Override
		public String getURI() {
			return Resources.admin;
		}	
		@Override
		public String toString() {
			return "Admin";
		}		
		@Override
		public String getHint() {
			return "System administration";
		}
	};
	public String getURI() {return null;}
	public String getHint() { return toString();}

}

