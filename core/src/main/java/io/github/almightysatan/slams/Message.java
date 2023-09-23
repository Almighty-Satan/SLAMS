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

    @NotNull MessageValue<T> get(@Nullable Context context);

    /**
     * Replaces placeholders and returns the resulting value. Uses the given {@link Context Contexts} language.
     *
     * @param context the context
     * @param placeholderResolver a {@link PlaceholderResolver} with additional {@link Placeholder Placeholders}
     * @return the value
     */
    @NotNull T value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver);

    /**
     * Replaces placeholders and returns the resulting value. Uses the given {@link Context Contexts} language.
     *
     * @param context the context
     * @param placeholderResolvers an array of {@link PlaceholderResolver} with additional
     *                             {@link Placeholder Placeholders}
     * @return the value
     */
    default @NotNull T value(@Nullable Context context, @NotNull PlaceholderResolver @NotNull ... placeholderResolvers) {
        Objects.requireNonNull(placeholderResolvers);
        return this.value(context, PlaceholderResolver.of(placeholderResolvers));
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the given {@link Context Contexts} language.
     *
     * @param context the context
     * @return the value
     */
    default @NotNull T value(@Nullable Context context) {
        return this.value(context, PlaceholderResolver.empty());
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the default language.
     *
     * @return the value
     */
    default @NotNull T value() {
        return this.value(null);
    }
}
