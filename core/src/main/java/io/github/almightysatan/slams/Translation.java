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

/**
 * Represents a translation of a message in a specific language.
 *
 * @param <T> the type of this translation
 */
public interface Translation<T> {

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @param context the context
     * @param placeholderResolver a {@link PlaceholderResolver} with additional {@link Placeholder Placeholders}
     * @return the value
     */
    @NotNull T value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver);

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @param context the context
     * @param placeholderResolvers an array of {@link PlaceholderResolver PlaceholderResolvers}
     * @return the value
     */
    default @NotNull T value(@Nullable Context context, @NotNull PlaceholderResolver @NotNull ... placeholderResolvers) {
        return this.value(context, PlaceholderResolver.of(placeholderResolvers));
    }

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @param placeholderResolver a {@link PlaceholderResolver} with additional {@link Placeholder Placeholders}
     * @return the value
     */
    default @NotNull T value(@NotNull PlaceholderResolver placeholderResolver) {
        return this.value(null, placeholderResolver);
    }

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @param placeholderResolvers an array of {@link PlaceholderResolver PlaceholderResolvers}
     * @return the value
     */
    default @NotNull T value(@NotNull PlaceholderResolver @NotNull ... placeholderResolvers) {
        return this.value(PlaceholderResolver.of(placeholderResolvers));
    }

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @param context the context
     * @return the value
     */
    default @NotNull T value(@Nullable Context context) {
        return this.value(context, PlaceholderResolver.empty());
    }

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @return the value
     */
    default @NotNull T value() {
        return this.value(null, PlaceholderResolver.empty());
    }
}
