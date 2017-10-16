package net.neferett.linaris.disguises.utilities;

import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.entity.Entity;

/**
 * User: Austin Date: 4/22/13 Time: 11:47 PM (c) lazertester
 */
// Code for this taken and slightly modified from
// https://github.com/ddopson/java-class-enumerator
public class ClassGetter {

	public static ArrayList<Class<?>> getClassesForPackage(String pkgname) {
		final ArrayList<Class<?>> classes = new ArrayList<>();
		// String relPath = pkgname.replace('.', '/');

		// Get a File object for the package
		final CodeSource src = Entity.class.getProtectionDomain().getCodeSource();
		if (src != null) {
			final URL resource = src.getLocation();
			resource.getPath();
			processJarfile(resource, pkgname, classes);
		}
		return classes;
	}

	private static Class<?> loadClass(String className) {
		try {
			return Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
		} catch (final NoClassDefFoundError e) {
			return null;
		}
	}

	@SuppressWarnings("resource")
	private static void processJarfile(URL resource, String pkgname, ArrayList<Class<?>> classes) {
		try {
			final String relPath = pkgname.replace('.', '/');
			final String resPath = URLDecoder.decode(resource.getPath(), "UTF-8");
			final String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
			final JarFile jarFile = new JarFile(jarPath);
			final Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				final JarEntry entry = entries.nextElement();
				final String entryName = entry.getName();
				String className = null;
				if (entryName.endsWith(".class") && entryName.startsWith(relPath)
						&& entryName.length() > (relPath.length() + "/".length()))
					className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
				if (className != null) {
					final Class<?> c = loadClass(className);
					if (c != null)
						classes.add(c);
				}
			}
		} catch (final Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
}
