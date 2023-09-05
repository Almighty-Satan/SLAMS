package io.github.almightysatan.slams;

import io.github.almightysatan.slams.impl.LanguageManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;

public interface LanguageManager { // TODO rename me

    @NotNull Language load(@NotNull String identifier, @NotNull LanguageParser... parsers) throws IOException;

    void reload() throws IOException;

    @NotNull Collection<Language> getLanguages();

    @NotNull Language getDefaultLanguage();

    static LanguageManager create(@NotNull String defaultLanguageIdentifier) {
        return new LanguageManagerImpl(defaultLanguageIdentifier);
    }
}
