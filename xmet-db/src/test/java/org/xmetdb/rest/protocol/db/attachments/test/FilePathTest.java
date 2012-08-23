package org.xmetdb.rest.protocol.db.attachments.test;

import junit.framework.Assert;

import org.junit.Test;
import org.xmetdb.rest.protocol.ProtocolFactory;

public class FilePathTest {
	@Test
	public void testWinFile() {
		Assert.assertEquals("test.pdf",ProtocolFactory.stripFileName("C:\\Documents and Settings\\tester\\Desktop\\test.pdf"));
	}
	
	@Test
	public void testUxFile() {
		Assert.assertEquals("test.pdf",ProtocolFactory.stripFileName("/srv/xmet-docs/document/test.pdf"));
	}
	
}
