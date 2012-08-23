package org.xmetdb.rest.structure.resource;

import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;
import org.xmetdb.xmet.client.Resources;

public class StructureRouter extends MyRouter {

	public StructureRouter(Context context) {
		super(context);
		attachDefault(StructureResource.class);
		attach(String.format("/{%s}",MoleculeResource.chemicalKey), MoleculeResource.class);
		attach(String.format("/{%s}%s",MoleculeResource.chemicalKey,Resources.structure), MoleculeResource.class);
		attach(String.format("/{%s}%s/{%s}",MoleculeResource.chemicalKey,Resources.structure,MoleculeResource.structureKey), MoleculeResource.class);
	
	}
	

}
