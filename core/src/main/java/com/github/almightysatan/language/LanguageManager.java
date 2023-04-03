package com.github.almightysatan.language;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface LanguageManager { // TODO rename me

    @NotNull Language load(@NotNull String identifier, @NotNull LanguageParser fallbackParser, @Nullable LanguageParser parser);

    void reload();

    @NotNull Collection<Language> getLanguages();

    @NotNull Language getDefaultLanguage();
}
