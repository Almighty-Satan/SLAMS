package com.github.almightysatan.language;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Language {

    void reload();

    @NotNull String identifier();

    @Nullable <T> T value(@NotNull String path, @NotNull Class<T> type) throws InvalidTypeException;
}
