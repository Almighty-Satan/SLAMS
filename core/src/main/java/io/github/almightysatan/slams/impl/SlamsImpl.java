/*
 * SLAMS - Simple Language And Message System
 * Copyright (C) 2023 Almighty-Satan, UeberallGebannt
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package io.github.almightysatan.slams.impl;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.LanguageParser;
import io.github.almightysatan.slams.UnknownLanguageException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.*;

/**
 * An implementation of the {@link SlamsInternal} interface.
 */
public class SlamsImpl implements SlamsInternal {

    private final Map<String, MessageImpl<?>> entries;
    private final String defaultLanguageIdentifier;
    private Language defaultLanguage;
    private final Map<String, Language> languages;

    public SlamsImpl(@NotNull String defaultLanguageIdentifier) {
        this.entries = new HashMap<>();
        this.defaultLanguageIdentifier = defaultLanguageIdentifier;
        this.languages = new HashMap<>();
    }

    @Override
    public void register(@NotNull MessageImpl<?> entry) {
        if (this.entries.containsKey(entry.path()))
            throw new IllegalArgumentException("Duplicate path: " + entry.path());
        this.entries.put(entry.path(), entry);
    }

    @Override
    public @NotNull @Unmodifiable Set<@NotNull String> paths() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    @Override
    public void load(@NotNull String identifier, @NotNull LanguageParser @NotNull ... parsers) throws IOException {
        if (identifier.isEmpty())
            throw new IllegalArgumentException("Empty language identifier");
        if (this.languages.containsKey(identifier))
            throw new IllegalArgumentException("Duplicate language identifier");

        Language language = new Language(this, identifier, parsers);
        this.languages.put(identifier, language);
    }

    @Override
    public void reload() throws IOException {
        for (Language language : this.languages.values())
            language.load();
        for (MessageImpl<?> entry : this.entries.values())
            entry.clearCache();
    }

    @Override
    public @NotNull Collection<@NotNull String> languages() {
        return Collections.unmodifiableCollection(this.languages.keySet());
    }

    @Override
    public @Nullable Language language(@NotNull String identifier) {
        return this.languages.get(identifier);
    }

    @Override
    public @NotNull String defaultLanguageIdentifier() {
        return this.defaultLanguageIdentifier;
    }

    @Override
    public @NotNull Language defaultLanguage() {
        Language defaultLanguage = this.defaultLanguage;

        if (defaultLanguage == null) {
            defaultLanguage = this.languages.get(this.defaultLanguageIdentifier);
            if (defaultLanguage == null)
                throw new UnknownLanguageException(this.defaultLanguageIdentifier);
            this.defaultLanguage = defaultLanguage;
        }

        return defaultLanguage;
    }

    @Override
    public @NotNull Language language(@Nullable Context context) {
        if (context == null)
            return this.defaultLanguage();

        String languageIdentifier = context.language();
        if (languageIdentifier == null)
            return this.defaultLanguage();

        Language language = this.language(languageIdentifier);
        if (language == null)
            throw new UnknownLanguageException(languageIdentifier);
        return language;
    }
}
