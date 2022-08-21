package com.bendertales.mc.talesservercommon.repository;

public interface BidirectionnalConverter <T, U> {

	public U convert(T original);
	public T unconvert(U converted);

}
