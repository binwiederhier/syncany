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
package org.syncany.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.syncany.database.ChunkEntry.ChunkChecksum;
import org.syncany.database.FileContent;
import org.syncany.database.FileContent.FileChecksum;
import org.syncany.database.VectorClock;

/**
 * @author pheckel
 *
 */
public class FileContentSqlDao extends AbstractSqlDao {
	protected static final Logger logger = Logger.getLogger(FileContentSqlDao.class.getSimpleName());
	
	public FileContentSqlDao(Connection connection) {
		super(connection);
	}

	public void writeFileContents(Connection connection, Collection<FileContent> fileContents) throws SQLException {
		for (FileContent fileContent : fileContents) {
			PreparedStatement preparedStatement = getStatement(connection, "/sql/insert.writeFileContents.sql");

			preparedStatement.setString(1, fileContent.getChecksum().toString());
			preparedStatement.setLong(2, fileContent.getSize());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();		
			writeFileContentChunkRefs(connection, fileContent);			
		}
	}

	private void writeFileContentChunkRefs(Connection connection, FileContent fileContent) throws SQLException {
		PreparedStatement preparedStatement = getStatement(connection, "/sql/insert.writeFileContentChunkRefs.sql");
		int order = 0;
		
		for (ChunkChecksum chunkChecksum : fileContent.getChunks()) {
			
			preparedStatement.setString(1, fileContent.getChecksum().toString());
			preparedStatement.setString(2, chunkChecksum.toString());
			preparedStatement.setInt(3, order);

			preparedStatement.addBatch();
			
			order++;				
		}
		
		preparedStatement.executeBatch();
		preparedStatement.close();
	}

	public Map<FileChecksum, FileContent> getFileContents(VectorClock vectorClock) {
		try (PreparedStatement preparedStatement = getStatement("/sql/filecontent.select.master.getFileContentsWithChunkChecksumsForDatabaseVersion.sql")) {
			
			preparedStatement.setString(1, vectorClock.toString());
			preparedStatement.setString(2, vectorClock.toString());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return createFileContents(resultSet);
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<FileChecksum, FileContent> createFileContents(ResultSet resultSet) throws SQLException {
		Map<FileChecksum, FileContent> fileContents = new HashMap<FileChecksum, FileContent>();;		
		FileChecksum currentFileChecksum = null;
		
		while (resultSet.next()) {		
			FileChecksum fileChecksum = FileChecksum.parseFileChecksum(resultSet.getString("checksum"));
			FileContent fileContent = null;
			
			if (currentFileChecksum != null && currentFileChecksum.equals(fileChecksum)) {
				fileContent = fileContents.get(fileChecksum);	
			}
			else {
				fileContent = new FileContent();
				
				fileContent.setChecksum(fileChecksum);
				fileContent.setSize(resultSet.getLong("size"));
			}
			
			ChunkChecksum chunkChecksum = ChunkChecksum.parseChunkChecksum(resultSet.getString("chunk_checksum"));
			fileContent.addChunk(chunkChecksum);

			fileContents.put(fileChecksum, fileContent); 
			currentFileChecksum = fileChecksum;
		}
		
		return fileContents;
	}

	public FileContent getFileContent(FileChecksum fileChecksum, boolean includeChunkChecksums) {
		if (fileChecksum == null) {
			return null;
		}
		else if (includeChunkChecksums) {
			return getFileContentWithChunkChecksums(fileChecksum);			
		}
		else {
			return getFileContentWithoutChunkChecksums(fileChecksum);			
		}
	}

	private FileContent getFileContentWithoutChunkChecksums(FileChecksum fileChecksum) {
		try (PreparedStatement preparedStatement = getStatement("/sql/select.getFileContentByChecksumWithoutChunkChecksums.sql")) {
			preparedStatement.setString(1, fileChecksum.toString());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					FileContent fileContent = new FileContent();
	
					fileContent.setChecksum(FileChecksum.parseFileChecksum(resultSet.getString("checksum")));
					fileContent.setSize(resultSet.getLong("size"));
	
					return fileContent;
				}
			}

			return null;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private FileContent getFileContentWithChunkChecksums(FileChecksum fileChecksum) {
		try (PreparedStatement preparedStatement = getStatement("/sql/select.getFileContentByChecksumWithChunkChecksums.sql")) {
			preparedStatement.setString(1, fileChecksum.toString());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				FileContent fileContent = null;
				
				while (resultSet.next()) {
					if (fileContent == null) {
						fileContent = new FileContent();
						
						fileContent.setChecksum(FileChecksum.parseFileChecksum(resultSet.getString("checksum")));
						fileContent.setSize(resultSet.getLong("size"));
					}
					
					// Add chunk references
					ChunkChecksum chunkChecksum = ChunkChecksum.parseChunkChecksum(resultSet.getString("chunk_checksum"));
					fileContent.addChunk(chunkChecksum);
				}
	
				return fileContent;
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
