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

import io.github.almightysatan.slams.LanguageManager;
import io.github.almightysatan.slams.LanguageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class LanguageManagerImpl implements LanguageManager {

    private final String defaultLanguageIdentifier;
    private LanguageImpl defaultLanguage;
    private final Map<String, LanguageImpl> languages;

    public LanguageManagerImpl(@NotNull String defaultLanguageIdentifier) {
        this.defaultLanguageIdentifier = defaultLanguageIdentifier;
        this.languages = new HashMap<>();
    }

    @Override
    public void load(@NotNull String identifier, @NotNull LanguageParser @NotNull ... parsers) throws IOException {
        if (identifier.isEmpty())
            throw new IllegalArgumentException("Empty language identifier");
        if (this.languages.containsKey(identifier))
            throw new IllegalArgumentException("Duplicate language identifier");

        LanguageImpl language = new LanguageImpl(this, identifier, parsers);
        this.languages.put(identifier, language);
    }

    @Override
    public void reload() throws IOException {
        for (LanguageImpl language : this.languages.values())
            language.load();
    }

    @Override
    public @NotNull Collection<@NotNull String> languages() {
        return Collections.unmodifiableCollection(this.languages.keySet());
    }

    public @Nullable LanguageImpl language(@NotNull String identifier) {
        return this.languages.get(identifier);
    }

    @Override
    public @NotNull String defaultLanguageIdentifier() {
        return this.defaultLanguageIdentifier;
    }

    public @NotNull LanguageImpl defaultLanguage() {
        LanguageImpl defaultLanguage = this.defaultLanguage;

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
