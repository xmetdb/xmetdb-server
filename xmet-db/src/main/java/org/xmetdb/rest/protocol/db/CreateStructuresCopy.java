package org.xmetdb.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;

import org.xmetdb.rest.protocol.DBProtocol;

/**
 * Copies substrate and product structures from one observation to another
 * @author nina
 *
 */
public class CreateStructuresCopy  extends AbstractUpdate<String,DBProtocol> {
	protected int newpropertyid = -1;
	public int getNewpropertyid() {
		return newpropertyid;
	}

	public void setNewpropertyid(int newpropertyid) {
		this.newpropertyid = newpropertyid;
	}
	protected String[] create_sql = {
			"insert into `ambit2-xmetdb`.query SELECT null,idsessions,?,content,idtemplate,now() FROM `ambit2-xmetdb`.query where name = ?",
			"insert into `ambit2-xmetdb`.query_results SELECT qnew.idquery,r.idchemical,r.idstructure,r.selected,r.metric,r.text\n"+ 
			"FROM `ambit2-xmetdb`.query qnew join `ambit2-xmetdb`.query qold join `ambit2-xmetdb`.query_results r where\n"+ 
			"qnew.name = ? and qold.name = ? and qold.idsessions = qnew.idsessions and qold.idquery=r.idquery\n",
			//and SOMs via properties
			"insert ignore into `ambit2-xmetdb`.properties\n"+
			"select null,idreference,?,units,?,islocal,ptype from `ambit2-xmetdb`.properties where name=?"

	};

	public CreateStructuresCopy(String oldID,DBProtocol ref) {
		super(ref);
		setGroup(oldID);
	}

	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (!getObject().isValidIdentifier()) throw new AmbitException("No observation ID");
		if (getGroup()==null) throw new AmbitException("No Owner!");
		List<QueryParam> params1 = new ArrayList<QueryParam>();
		switch (index) {
		case 0: {
			params1.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
			params1.add(new QueryParam<String>(String.class, getGroup()));
			break;
		}
		case 1: {
			params1.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
			params1.add(new QueryParam<String>(String.class, getGroup()));
			break;
		}
		case 2: {
			params1.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
			params1.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
			params1.add(new QueryParam<String>(String.class, getGroup()));
			break;
		}		
		}

		return params1;
	}

	public String[] getSQL() throws AmbitException {
		return create_sql;
	}
	public void setID(int index, int id) {
		if (index==2) newpropertyid = id;
	}
	@Override
	public boolean returnKeys(int index) {
		return index==2;
	}

}