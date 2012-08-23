package org.xmetdb.xmet.rest;

import net.idea.restnet.c.RESTComponent;

import org.restlet.Application;
import org.restlet.Context;

/**
 * This is used as a servlet component instead of the core one, to be able to attach protocols 
 * @author nina
 *
 */
public class XMETRESTComponent extends RESTComponent {
		public XMETRESTComponent() {
			this(null);
		}
		public XMETRESTComponent(Context context,Application[] applications) {
			super(context,applications);
			
		
		}
		public XMETRESTComponent(Context context) {
			this(context,new Application[]{new XMETApplication()});
		}
		
		

}
