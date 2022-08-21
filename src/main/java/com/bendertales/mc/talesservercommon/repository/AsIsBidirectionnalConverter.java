package com.bendertales.mc.talesservercommon.repository;

public class AsIsBidirectionnalConverter<T> implements BidirectionnalConverter<T, T> {

	@Override
	public T convert(T original) {
		return original;
	}

	@Override
	public T unconvert(T converted) {
		return converted;
	}

}
