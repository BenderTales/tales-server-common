package com.bendertales.mc.talesservercommon.repository;

public class AsIsConverter<T> implements Converter<T, T> {

	@Override
	public T convert(T fileContent) {
		return fileContent;
	}

}
