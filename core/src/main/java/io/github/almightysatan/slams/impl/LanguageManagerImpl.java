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

import io.github.almightysatan.slams.Language;
import io.github.almightysatan.slams.LanguageManager;
import io.github.almightysatan.slams.LanguageParser;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull Language load(@NotNull String identifier, @NotNull LanguageParser @NotNull ... parsers) throws IOException {
        if (identifier.isEmpty())
            throw new IllegalArgumentException("Empty language identifier");
        if (this.languages.containsKey(identifier))
            throw new IllegalArgumentException("Duplicate language identifier");

        Language language = new LanguageImpl(this, identifier, parsers);
        this.languages.put(identifier, language);
        return language;
    }

    @Override
    public void reload() throws IOException {
        for (Language language : this.languages.values())
            language.reload();
    }

    @Override
    public @NotNull Collection<Language> languages() {
        return Collections.unmodifiableCollection(this.languages.values());
    }

    @Override
    public @NotNull Language defaultLanguage() {
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
