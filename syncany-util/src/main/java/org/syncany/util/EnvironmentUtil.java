/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2013 Philipp C. Heckel <philipp.heckel@gmail.com> 
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
package org.syncany.util;

import java.io.File;

public class EnvironmentUtil {
	private static final String osName;
	public enum OperatingSystem { WINDOWS, UNIX_LIKE };

	private static OperatingSystem operatingSystem;
	
	static {
		operatingSystem = (File.separatorChar == '\\') ? OperatingSystem.WINDOWS : OperatingSystem.UNIX_LIKE;
		osName = System.getProperty("os.name").toLowerCase();
	}		

	public static void setOperatingSystem(OperatingSystem aOperatingSystem) {
		operatingSystem = aOperatingSystem;
	}
	
	public static OperatingSystem getOperatingSystem() {
		return operatingSystem;
	}
	
	public static boolean isUnixLikeOperatingSystem() {
		return operatingSystem == OperatingSystem.UNIX_LIKE;
	}

	public static boolean isWindows() {
		return operatingSystem == OperatingSystem.WINDOWS;
	}	
	
	public static boolean isMacOS(){
		return osName.startsWith("mac os");
	}

	public static boolean isLinux() {
		return osName.startsWith("linux");
	}
}
