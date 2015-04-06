/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2015 Philipp C. Heckel <philipp.heckel@gmail.com> 
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
package org.syncany.operations.up;

import java.util.Queue;

import org.syncany.database.DatabaseVersion;
import org.syncany.util.FilteredQueueAdderConsumer;

/**
 * @author Jesse Donkervliet
 *
 */
public class DatabaseVersionConsumer extends FilteredQueueAdderConsumer<DatabaseVersion> {

	/**
	 * @param queue
	 */
	public DatabaseVersionConsumer(Queue<DatabaseVersion> queue) {
		super(queue);
	}

	@Override
	public boolean isValid(DatabaseVersion databaseVersion) {
		if (databaseVersion == null) {
			// end-of-stream poison object.
			return true;
		}
		return databaseVersion.getFileHistories().size() > 0;
	}
}
