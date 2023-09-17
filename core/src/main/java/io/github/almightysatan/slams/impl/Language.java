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

import io.github.almightysatan.slams.LanguageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An internal class that represents a Language.
 */
public class Language {

    private final SlamsInternal languageManager;
    private final String identifier;
    private final LanguageParser[] parsers;
    private Map<String, Object> entries;

    public Language(@NotNull SlamsInternal languageManager, @NotNull String identifier, @NotNull LanguageParser @NotNull ... parsers) throws IOException {
        this.languageManager = languageManager;
        this.identifier = identifier;
        this.parsers = parsers;

        this.load();
    }

    protected void load() throws IOException {
        Map<String, Object> entries = new HashMap<>();
        Set<String> paths = this.languageManager.paths();
        LanguageParser.Values values = new LanguageParser.Values() {
            @Override
            public @NotNull @Unmodifiable Set<@NotNull String> paths() {
                return paths;
            }

            @Override
            public @Nullable Object get(@NotNull String key) {
                return entries.get(key);
            }

            @Override
            public void put(@NotNull String key, @NotNull Object value) throws IllegalArgumentException {
                Objects.requireNonNull(value);
                if (!paths.contains(key))
                    throw new IllegalArgumentException("Unknown path: " + key);
                entries.put(key, value);
            }
        };
        for (LanguageParser parser : this.parsers)
            parser.load(values);
        this.entries = entries;
    }

    public @NotNull String identifier() {
        return this.identifier;
    }

    public @Nullable Object value(@NotNull String path) {
        return this.entries.get(path);
    }
}
