package io.github.almightysatan.slams.impl;

import io.github.almightysatan.slams.Language;
import io.github.almightysatan.slams.LanguageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> entries = new HashMap<>();
        this.languageManager.paths().forEach(path -> entries.put(path, null));
        for (LanguageParser parser : this.parsers)
            entries.putAll(parser.load(Collections.unmodifiableMap(entries)));
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
