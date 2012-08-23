package org.xmetdb.xmet.client;

/**
 * Published status as in the original QMRF documents
 * @author nina
 *
 */
public enum PublishedStatus {
		draft,
		submitted,
		under_review,
		returned_for_revision,
		review_completed,
		published,
		archived,
		deleted;
		@Override
		public String toString() {
			return name().replace("_"," ");
		}
}
