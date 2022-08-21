package com.bendertales.mc.talesservercommon.repository;

public class NoopFileContentChecker implements FileContentChecker<Object> {
	@Override
	public boolean check(Object o) {
		return true;
	}
}
