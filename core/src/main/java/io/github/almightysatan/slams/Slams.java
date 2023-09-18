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

package io.github.almightysatan.slams;

import io.github.almightysatan.slams.impl.SlamsImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;

/**
 * The main SLAMS object.
 */
public interface Slams {

    /**
     * Registers a new language with the given identifier and loads its messages using the given
     * {@link LanguageParser LanguageParsers}. If more than one {@link LanguageParser} is supplied they will be run in
     * sequential order and can read or overwrite values loaded by previous parsers.
     *
     * @param identifier the new languages identifier
     * @param parsers the {@link LanguageParser LanguageParsers} that should be used to load messages
     * @throws IOException if a parser throws an exception
     */
    void load(@NotNull String identifier, @NotNull LanguageParser @NotNull ... parsers) throws IOException;

    /**
     * Reloads all languages.
     *
     * @throws IOException if a parser throws an exception
     */
    void reload() throws IOException;

    /**
     * Returns a {@link Collection} containing the identifiers of all registered languages.
     *
     * @return a {@link Collection} containing the identifiers of all registered languages
     */
    @NotNull Collection<@NotNull String> languages();

    /**
     * Returns the identifier of the default language.
     *
     * @return the identifier of the default language
     */
    @NotNull String defaultLanguageIdentifier();

    /**
     * Creates a new {@link Slams} instance.
     *
     * @param defaultLanguageIdentifier the identifier of the default language
     * @return a new {@link Slams} instance
     */
    static Slams create(@NotNull String defaultLanguageIdentifier) {
        return new SlamsImpl(defaultLanguageIdentifier);
    }
}
