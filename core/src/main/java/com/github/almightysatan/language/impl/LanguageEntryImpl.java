package com.github.almightysatan.language.impl;

import com.github.almightysatan.language.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class LanguageEntryImpl<T> implements LanguageEntry {

    private final String path;
    private final LanguageManagerImpl languageManager;

    protected LanguageEntryImpl(@NotNull String path, @NotNull LanguageManager languageManager) {
        this.path = Objects.requireNonNull(path);
        this.languageManager = (LanguageManagerImpl) Objects.requireNonNull(languageManager);
    }

    @Override
    public @NotNull String path() {
        return this.path;
    }

    protected abstract T checkType(@Nullable Object value) throws InvalidTypeException;

    protected T getRawValue(Context context) {
        return this.checkType(this.getLanguage(context).value(this.path));
    }

    protected @NotNull LanguageManagerImpl languageManager() {
        return this.languageManager;
    }

    protected @NotNull LanguageImpl getLanguage(@Nullable Context context) {
        if (context != null) {
            Language language = context.getLanguage();
            if (language != null)
                return (LanguageImpl) language;
        }
        return (LanguageImpl) this.languageManager().getDefaultLanguage();
    }
}
