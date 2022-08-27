package com.bendertales.mc.talesservercommon.repository.data;

public interface IDataRepository <KEY, DATA> {

	DATA load(KEY key);
	void save(KEY key, DATA data);
}
