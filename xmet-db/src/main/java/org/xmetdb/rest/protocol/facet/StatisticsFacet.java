package org.xmetdb.rest.protocol.facet;

import net.idea.modbcum.q.facet.AbstractFacet;

public class StatisticsFacet extends AbstractFacet<String> {
	protected String subcategory ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1482170881876629883L;

	public StatisticsFacet(String subcategory) {
		super(subcategory);
		this.subcategory = subcategory;
	}
	public String getSubcategory() {
		return subcategory;
	}
	@Override
	public String getSubCategoryURL(String... params) {
		return subcategory;
	}
	@Override
	public String getSubcategoryTitle() {
		return subcategory;
	}
}
