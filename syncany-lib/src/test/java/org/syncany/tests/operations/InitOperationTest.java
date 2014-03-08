/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2014 Philipp C. Heckel <philipp.heckel@gmail.com> 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.tests.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.syncany.operations.InitOperation;
import org.syncany.operations.InitOperation.InitOperationOptions;
import org.syncany.operations.InitOperation.InitOperationResult;
import org.syncany.tests.util.TestConfigUtil;
import org.syncany.tests.util.TestFileUtil;

/**
 * This test goes through the creation of a local repo and verifies
 * the existence of files/folders as well as the connect link.
 * @author Pim Otte
 *
 */
public class InitOperationTest {
	
	@Test
	public void testInitOperation() throws Exception {	
		InitOperationOptions operationOptions = TestConfigUtil.createTestInitOperationOptions("A");
		InitOperation op = new InitOperation(operationOptions, null);
		InitOperationResult res = op.execute();
		File repoDir = new File(operationOptions.getConfigTO().getConnectionTO().getSettings().get("path"));
		File localDir = new File(operationOptions.getLocalDir(), ".syncany");
		
		//Test the repository
		assertTrue((new File(repoDir, "databases").exists()));
		assertTrue((new File(repoDir, "syncany").exists()));
		assertTrue((new File(repoDir, "multichunks").exists()));
		assertEquals((new File(repoDir, "master").exists()), TestConfigUtil.getCrypto());
		
		//Test the local folder		
		assertTrue((new File(localDir, "db").exists()));
		assertTrue((new File(localDir, "cache").exists()));
		assertTrue((new File(localDir, "config.xml").exists()));
		assertTrue((new File(localDir, "logs").exists()));
		assertTrue((new File(localDir, "repo").exists()));
		assertEquals((new File(localDir, "master").exists()), TestConfigUtil.getCrypto());
			
		//Test the existance of generated link
		String link = res.getGenLinkResult().getShareLink();
		assertNotNull(link);
		
		TestFileUtil.deleteDirectory(repoDir);
		TestFileUtil.deleteDirectory(localDir);
	}
}
