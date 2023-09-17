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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.Set;

/**
 * Loads messages
 */
public interface LanguageParser {

    /**
     * Loads messages and calls {@link Values#put} to map a value to its path. {@link Values#put} should only be called
     * for paths that are an element of {@link Values#paths}. Values loaded by previous parsers can be accessed via
     * {@link Values#get}. Paths are case-sensitive.
     *
     * @param values the {@link Values} object used to map values
     * @throws IOException if an error occurs while loading messages
     */
    void load(@NotNull Values values) throws IOException;

    interface Values {
        /**
         * Returns a {@link Set} containing all valid paths.
         *
         * @return a {@link Set} containing all valid paths
         */
        @NotNull @Unmodifiable Set<@NotNull String> paths();

        /**
         * Returns the value mapped to a given path or {@code null} if the value does not exist or the path is invalid.
         *
         * @param key the path
         * @return the value
         */
        @Nullable Object get(@NotNull String key);

        /**
         * Maps a given value to its given path. May be called multiple times for the same path.
         *
         * @param key the path
         * @param value the value
         * @throws IllegalArgumentException if the path is invalid (is not an element of {@link Values#paths})
         */
        void put(@NotNull String key, @NotNull Object value) throws IllegalArgumentException;
    }
}
