package com.github.almightysatan.language;

import com.github.almightysatan.language.impl.LanguageManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;

public interface LanguageManager { // TODO rename me

    @NotNull Language load(@NotNull String identifier, @NotNull LanguageParser fallbackParser, @Nullable LanguageParser parser) throws IOException;

    void reload() throws IOException;

    @NotNull Collection<Language> getLanguages();

    @NotNull Language getDefaultLanguage();

    static LanguageManager create(@NotNull String defaultLanguageIdentifier) {
        return new LanguageManagerImpl(defaultLanguageIdentifier);
    }
}
