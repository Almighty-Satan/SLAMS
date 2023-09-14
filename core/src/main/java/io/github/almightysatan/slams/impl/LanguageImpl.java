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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LanguageImpl {

    private final LanguageManagerImpl languageManager;
    private final String identifier;
    private final LanguageParser[] parsers;
    private Map<String, Object> entries;

    public LanguageImpl(@NotNull LanguageManagerImpl languageManager, @NotNull String identifier, @NotNull LanguageParser @NotNull ... parsers) throws IOException {
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

    public @NotNull String identifier() {
        return this.identifier;
    }

    public @Nullable Object value(@NotNull String path) {
        return this.entries.get(path);
    }
}
