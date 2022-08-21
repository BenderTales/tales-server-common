package com.bendertales.mc.talesservercommon.repository.data;

import java.nio.file.Path;

import com.bendertales.mc.talesservercommon.repository.Converter;


public interface DataPathProvider<T> extends Converter<T, Path> {
}
