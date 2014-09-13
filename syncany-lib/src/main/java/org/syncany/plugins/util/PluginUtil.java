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
package org.syncany.plugins.util;

import org.simpleframework.xml.Element;
import org.syncany.config.to.ConnectionTO;
import org.syncany.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Roth <christian.roth@port17.de>
 * @version 0.0.1
 */

public abstract class PluginUtil {

	/**
	 * For compatibility reasons
	 * Will be removed when we have wizardstyle plugin initialisation
	 *
	 * @deprecated
	 */
	public static Map<String, String> createMapFromTransferSettings(ConnectionTO connectionTO) throws IllegalAccessException {

		final Map<String, String> connection = new HashMap<>();
		for (Field f : ReflectionUtil.getAllFieldsWithAnnotation(connectionTO.getClass(), Element.class)) {
			connection.put(f.getName(), f.get(connectionTO).toString());
		}

		return connection;

	}

}