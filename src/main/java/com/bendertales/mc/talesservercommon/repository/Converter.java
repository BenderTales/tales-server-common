package com.bendertales.mc.talesservercommon.repository;

@FunctionalInterface
public interface Converter<FILE_CONTENT, CONFIG> {

	CONFIG convert(FILE_CONTENT fileContent);

}
