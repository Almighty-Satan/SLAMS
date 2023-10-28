/*
 * SLAMS - Simple Language And Message System
 * Copyright (C) 2023 Almighty-Satan, LeStegii
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
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A LanguageParser loads translations for a language. It should be reusable, meaning {@link #load(Values)} might be
 * called more than once.
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
         * Returns the value mapped to a given path or throws an exception supplied by the given {@link Supplier} if the
         * value does not exist or the path is invalid.
         *
         * @param key               the path
         * @param throwableSupplier the supplier
         * @param <T>               the exception
         * @return the value
         * @throws T if the value does not exist or the path is invalid
         */
        default <T extends Throwable> @NotNull Object getOrThrow(@NotNull String key, @NotNull Supplier<? extends T> throwableSupplier) throws T {
            Object value = this.get(key);
            if (value == null)
                throw throwableSupplier.get();
            return value;
        }

        /**
         * Returns an optional that contains the value mapped to the given path, if it exists.
         *
         * @param key the path
         * @return an optional containing the value
         */
        default @NotNull Optional<Object> getOptional(@NotNull String key) {
            return Optional.ofNullable(this.get(key));
        }

        /**
         * Maps a given value to its given path. May be called multiple times for the same path.
         *
         * @param key   the path
         * @param value the value
         * @throws IllegalArgumentException if the path is invalid (is not an element of {@link Values#paths})
         */
        void put(@NotNull String key, @NotNull Object value) throws IllegalArgumentException;

        /**
         * Maps a given value to the given path if there is no value present for the path. May be called multiple times
         * for the same path.
         *
         * @param key   the path
         * @param value the value
         * @throws IllegalArgumentException if the path is invalid (is not an element of {@link Values#paths})
         */
        default void putIfAbsent(@NotNull String key, @NotNull Object value) {
            if (this.get(key) == null)
                this.put(key, value);
        }

        /**
         * Maps a given value to the given path if there is already a value present for the path. May be called multiple
         * times for the same path.
         *
         * @param key   the path
         * @param value the value
         * @throws IllegalArgumentException if the path is invalid (is not an element of {@link Values#paths})
         */
        default void putIfPresent(@NotNull String key, @NotNull Object value) {
            if (this.get(key) != null)
                this.put(key, value);
        }

        /**
         * If there is no value present for the given path, the {@link Supplier} is evaluated and its returned value
         * mapped to the path. May be called multiple times for the same path.
         *
         * @param key           the path
         * @param valueSupplier the supplier
         * @throws IllegalArgumentException if the path is invalid (is not an element of {@link Values#paths})
         */
        default void putIfAbsent(@NotNull String key, @NotNull Supplier<Object> valueSupplier) {
            if (this.get(key) == null)
                this.put(key, valueSupplier.get());
        }

        /**
         * If a value for the given path already exists, the {@link Supplier} is evaluated and its returned value mapped
         * to the path. May be called multiple times for the same path.
         *
         * @param key           the path
         * @param valueSupplier the supplier
         * @throws IllegalArgumentException if the path is invalid (is not an element of {@link Values#paths})
         */
        default void putIfPresent(@NotNull String key, @NotNull Supplier<Object> valueSupplier) {
            if (this.get(key) != null)
                this.put(key, valueSupplier.get());
        }
    }
}
