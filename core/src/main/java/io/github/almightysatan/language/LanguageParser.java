package io.github.almightysatan.language;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public interface LanguageParser { // TODO rename me

    @NotNull Map<String, Object> load(@NotNull Map<String, Object> entries) throws IOException;
}
