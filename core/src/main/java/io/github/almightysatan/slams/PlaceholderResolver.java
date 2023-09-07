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

@FunctionalInterface
public interface PlaceholderResolver {

    @Nullable Placeholder resolve(@NotNull String key);

    static @NotNull PlaceholderResolver empty() {
        return key -> null;
    }

    static @NotNull PlaceholderResolver of(@NotNull Placeholder placeholder) {
        Objects.requireNonNull(placeholder);
        return key -> key.equals(placeholder.key()) ? placeholder : null;
    }

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

    static @NotNull PlaceholderResolver of(@NotNull List<@NotNull PlaceholderResolver> placeholderResolvers) {
        return of(placeholderResolvers.toArray(new PlaceholderResolver[0]));
    }
}
