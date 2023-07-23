package io.github.almightysatan.language;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Language {

    void reload() throws IOException;

    @NotNull String identifier();
}
