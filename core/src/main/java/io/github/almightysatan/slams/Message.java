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

import java.util.Objects;

/**
 * Represents a message. The value of a message is not necessarily a {@link String}. It could be a multidimensional
 * array of Strings, some sort of Map, a MiniMessage Component or something completely different.
 *
 * @param <T> the type of this message's value
 */
public interface Message<T> {

    /**
     * The case-sensitive dotted path of this message. For example 'path.to.example.message'.
     *
     * @return the path of this message
     */
    @NotNull String path();

    /**
     * Returns a {@link Translation} of this message in the given {@link Context Context's} language.
     *
     * @param context the context
     * @return a translation of this message
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    @NotNull Translation<T> translate(@Nullable Context context) throws MissingTranslationException, UnknownLanguageException;

    /**
     * Replaces placeholders and returns the resulting value. Uses the given {@link Context Context's} language.
     *
     * @param context             the context
     * @param placeholderResolver a {@link PlaceholderResolver} with additional {@link Placeholder Placeholders}
     * @return the value
     */
    @NotNull T value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) throws MissingTranslationException, UnknownLanguageException;

    /**
     * Replaces placeholders and returns the resulting value. Uses the given {@link Context Context's} language.
     *
     * @param context              the context
     * @param placeholderResolvers an array of {@link PlaceholderResolver} with additional
     *                             {@link Placeholder Placeholders}
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value(@Nullable Context context, @NotNull PlaceholderResolver @NotNull ... placeholderResolvers) throws MissingTranslationException, UnknownLanguageException {
        Objects.requireNonNull(placeholderResolvers);
        return this.value(context, PlaceholderResolver.of(placeholderResolvers));
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the default language.
     *
     * @param placeholderResolver a {@link PlaceholderResolver} with additional {@link Placeholder Placeholders}
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value(@NotNull PlaceholderResolver placeholderResolver) throws MissingTranslationException, UnknownLanguageException {
        Objects.requireNonNull(placeholderResolver);
        return this.value(null, placeholderResolver);
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the default language.
     *
     * @param placeholderResolvers an array of {@link PlaceholderResolver} with additional
     *                             {@link Placeholder Placeholders}
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value(@NotNull PlaceholderResolver @NotNull ... placeholderResolvers) throws MissingTranslationException, UnknownLanguageException {
        Objects.requireNonNull(placeholderResolvers);
        return this.value(PlaceholderResolver.of(placeholderResolvers));
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the given {@link Context Context's} language.
     *
     * @param context the context
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value(@Nullable Context context) throws MissingTranslationException, UnknownLanguageException {
        return this.value(context, PlaceholderResolver.empty());
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the default language.
     *
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value() throws MissingTranslationException, UnknownLanguageException {
        return this.value((Context) null);
    }
}
