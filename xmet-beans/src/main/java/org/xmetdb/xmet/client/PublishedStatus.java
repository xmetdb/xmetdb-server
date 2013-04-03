package org.xmetdb.xmet.client;

/**
 * Published status as in the original QMRF documents
 * @author nina
 *
 */
public enum PublishedStatus {
		draft,
		published,
		archived,
		deleted;
		@Override
		public String toString() {
			return name().replace("_"," ");
		}
}
