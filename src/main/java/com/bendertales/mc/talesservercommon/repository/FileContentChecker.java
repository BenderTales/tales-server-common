package com.bendertales.mc.talesservercommon.repository;

@FunctionalInterface
public interface FileContentChecker<FILE_CONTENT> {

	/**
	 * Checks if the given file content is valid.
	 * If the file content is not valid, it should fix it.
	 * @param fileContent
	 * @return true if the file content was valid, false otherwise.
	 */
	boolean check(FILE_CONTENT fileContent);
}
