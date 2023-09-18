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

import java.util.List;
import java.util.Objects;

/**
 * Resolves {@link Placeholder Placeholders}.
 */
@FunctionalInterface
public interface PlaceholderResolver {

    /**
     * Searches for a {@link Placeholder} with the given key. SLAMS may cache the resulting {@link Placeholder} if
     * possible.
     *
     * @param key the key
     * @return the placeholder or {@code null}
     */
    @Nullable Placeholder resolve(@NotNull String key);

    /**
     * Returns a {@link PlaceholderResolver} that always returns null (does not resolve any
     * {@link Placeholder Placeholders}).
     *
     * @return a {@link PlaceholderResolver} that always returns null
     */
    static @NotNull PlaceholderResolver empty() {
        return key -> null;
    }

    /**
     * Returns a {@link PlaceholderResolver} that only resolves a single {@link Placeholder}.
     *
     * @param placeholder the {@link Placeholder}
     * @return a new {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver of(@NotNull Placeholder placeholder) {
        Objects.requireNonNull(placeholder);
        return key -> key.equals(placeholder.key()) ? placeholder : null;
    }

    /**
     * Returns a {@link PlaceholderResolver} that can resolve all the given {@link Placeholder Placeholders}.
     *
     * @param placeholders an array of {@link Placeholder Placeholders}
     * @return a new {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver of(@NotNull Placeholder @NotNull ... placeholders) {
        if (placeholders.length == 0)
            return empty();
        return key -> {
            for (Placeholder placeholder : placeholders) {
                if (key.equals(placeholder.key()))
                    return placeholder;
            }
            return null;
        };
    }

    /**
     * Returns a {@link PlaceholderResolver} that will use the given {@link PlaceholderResolver PlaceholderResolvers} to
     * find a {@link Placeholder}. It can therefore resolve any {@link Placeholder} resolved by at least one of the
     * given {@link PlaceholderResolver PlaceholderResolvers}.
     *
     * @param placeholderResolvers an array of {@link PlaceholderResolver PlaceholderResolvers}
     * @return a new {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver of(@NotNull PlaceholderResolver @NotNull ... placeholderResolvers) {
        if (placeholderResolvers.length == 0)
            return empty();
        return key -> {
            for (PlaceholderResolver placeholderResolver : placeholderResolvers) {
                Placeholder placeholder = placeholderResolver.resolve(key);
                if (placeholder != null)
                    return placeholder;
            }
            return null;
        };
    }

    /**
     * Returns a {@link PlaceholderResolver} that will use the given {@link PlaceholderResolver PlaceholderResolvers} to
     * find a {@link Placeholder}. It can therefore resolve any {@link Placeholder} resolved by at least one of the
     * given {@link PlaceholderResolver PlaceholderResolvers}.
     *
     * @param placeholderResolvers a list of {@link PlaceholderResolver PlaceholderResolvers}
     * @return a new {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver of(@NotNull List<@NotNull PlaceholderResolver> placeholderResolvers) {
        return of(placeholderResolvers.toArray(new PlaceholderResolver[0]));
    }

    // TODO add builder
}
