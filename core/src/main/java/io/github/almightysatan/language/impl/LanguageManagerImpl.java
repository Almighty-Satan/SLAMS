package io.github.almightysatan.language.impl;

import io.github.almightysatan.language.Language;
import io.github.almightysatan.language.LanguageManager;
import io.github.almightysatan.language.LanguageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class LanguageManagerImpl implements LanguageManager {

    private final String defaultLanguageIdentifier;
    private Language defaultLanguage;
    private final Map<String, Language> languages;

    public LanguageManagerImpl(@NotNull String defaultLanguageIdentifier) {
        this.defaultLanguageIdentifier = defaultLanguageIdentifier;
        this.languages = new HashMap<>();
    }

    @Override
    public @NotNull Language load(@NotNull String identifier, @NotNull LanguageParser... parsers) throws IOException {
        Language language = new LanguageImpl(this, identifier, parsers);
        this.languages.put(identifier, language);
        return language;
    }

    @Override
    public void reload() throws IOException{
        for (Language language : this.languages.values())
            language.reload();
    }

    @Override
    public @NotNull Collection<Language> getLanguages() {
        return Collections.unmodifiableCollection(this.languages.values());
    }

    @Override
    public @NotNull Language getDefaultLanguage() {
        Language defaultLanguage = this.defaultLanguage;

        if (defaultLanguage == null) {
            defaultLanguage = this.languages.get(this.defaultLanguageIdentifier);
            if (defaultLanguage == null)
                throw new RuntimeException("Unknown default language: " + this.defaultLanguageIdentifier);
            this.defaultLanguage = defaultLanguage;
        }

        return defaultLanguage;
    }

    public @NotNull Set<String> paths() {
        return new HashSet<>(this.languages.keySet());
    }
}
