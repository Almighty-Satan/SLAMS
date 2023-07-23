package io.github.almightysatan.language.impl;

import io.github.almightysatan.language.Language;
import io.github.almightysatan.language.LanguageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LanguageImpl implements Language {

    private final LanguageManagerImpl languageManager;
    private final String identifier;
    private final LanguageParser fallbackParser;
    private final LanguageParser parser;
    private Map<String, Object> entries;

    public LanguageImpl(@NotNull LanguageManagerImpl languageManager, @NotNull String identifier, @NotNull LanguageParser fallbackParser, @Nullable LanguageParser parser) throws IOException {
        this.languageManager = languageManager;
        this.identifier = identifier;
        this.fallbackParser = fallbackParser;
        this.parser = parser;

        this.load();
    }

    protected void load() throws IOException {
        Set<String> paths = this.languageManager.paths();
        Map<String, Object> entries = new HashMap<>(this.fallbackParser.load(paths));
        if (this.parser != null)
            entries.putAll(this.parser.load(paths));
        this.entries = entries;
    }

    @Override
    public void reload() throws IOException {
        this.load();
    }

    @Override
    public @NotNull String identifier() {
        return this.identifier;
    }

    public @NotNull LanguageParser fallbackParser() {
        return this.fallbackParser;
    }

    public @Nullable LanguageParser parser() {
        return this.parser;
    }

    public @Nullable Object value(@NotNull String path) {
        return this.entries.get(path);
    }
}
