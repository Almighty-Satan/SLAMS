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
    private final LanguageParser[] parsers;
    private Map<String, Object> entries;

    public LanguageImpl(@NotNull LanguageManagerImpl languageManager, @NotNull String identifier, @NotNull LanguageParser... parsers) throws IOException {
        this.languageManager = languageManager;
        this.identifier = identifier;
        this.parsers = parsers;

        this.load();
    }

    protected void load() throws IOException {
        Set<String> paths = this.languageManager.paths();
        Map<String, Object> entries = new HashMap<>();
        for (LanguageParser parser : this.parsers)
            entries.putAll(parser.load(paths));
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

    public @Nullable LanguageParser[] parsers() {
        return this.parsers;
    }

    public @Nullable Object value(@NotNull String path) {
        return this.entries.get(path);
    }
}
