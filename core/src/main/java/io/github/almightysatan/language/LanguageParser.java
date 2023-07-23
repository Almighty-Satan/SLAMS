package io.github.almightysatan.language;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface LanguageParser { // TODO rename me

    @NotNull Map<String, Object> load(@NotNull Set<String> paths) throws IOException;
}
