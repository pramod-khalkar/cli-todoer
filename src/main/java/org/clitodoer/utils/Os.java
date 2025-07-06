package org.clitodoer.utils;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 **/
@UtilityClass
public class Os {

	public static Path getStoragePath() {
		String os = System.getProperty("os.name").toLowerCase();
		String home = System.getProperty("user.home");
		if (os.contains("mac")) {
			return Paths.get(home, "Library", "Application Support", "todo");
		} else if (os.contains("linux")) {
			String xdg = System.getenv("XDG_DATA_HOME");
			if (xdg != null) return Paths.get(xdg, "todo");
			return Paths.get(home, ".local", "share", "todo");
		} else {
			// fallback for Windows or others
			return Paths.get(home, ".todo");
		}
	}
}
