package net.neferett.linaris.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class LibraryLoader {
	
	public static void addJarsInDirectory(File dir) {
		System.out.println("[LibraryLoader] Adding jars in " + dir);
		if (!dir.isDirectory()) {
			System.out.println("[LibraryLoader] Directory does not exist, exiting");
			return;
		}
		for (File f : dir.listFiles()) {
			if (f.isFile() && f.getName().endsWith(".jar")) {
				System.out.println(f);
				addJarToClassPath(f);
			}
		}
	}
	
	public static void addJarToClassPath(File jar) {
		if (!jar.isFile() && !jar.getName().endsWith(".jar"))
			return;
		System.out.print("[LibraryLoader] Attempting to add " + jar + " ... ");
		try {
			String path = "file://" + jar.getAbsolutePath();
			URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<?> sysclass = URLClassLoader.class;
			Class<?>[] parameters = new Class<?>[]{URL.class};
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new URL(path));
			System.out.println("Success");
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("Fail");
			return;
		}
	}
	
	//directory of the jar
	public static File getJarLocation() {
		File f = new File(LibraryLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		return f.getParentFile();
	}
	
}