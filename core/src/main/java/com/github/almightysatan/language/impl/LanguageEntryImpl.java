package com.github.almightysatan.language.impl;

import com.github.almightysatan.language.LanguageEntry;
import com.github.almightysatan.language.LanguageManager;
import org.jetbrains.annotations.NotNull;

public abstract class LanguageEntryImpl implements LanguageEntry {

    private final String path;
    private final LanguageManagerImpl languageManager;

    protected LanguageEntryImpl(@NotNull String path, @NotNull LanguageManager languageManager) {
        this.path = path;
        this.languageManager = (LanguageManagerImpl) languageManager;
    }

    @Override
    public @NotNull String path() {
        return this.path;
    }

    protected @NotNull LanguageManagerImpl languageManager() {
        return this.languageManager;
    }
}
