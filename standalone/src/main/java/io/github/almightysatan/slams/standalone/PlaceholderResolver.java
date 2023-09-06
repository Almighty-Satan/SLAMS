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

package io.github.almightysatan.slams.standalone;

import io.github.almightysatan.slams.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface PlaceholderResolver {

    @Nullable Placeholder<? extends Context> resolve(@Nullable Context context, @NotNull String key);

    static @NotNull PlaceholderResolver empty() {
        return (context, key) -> null;
    }

    static @NotNull PlaceholderResolver of(@NotNull String key, @NotNull Placeholder<Context> placeholder) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(placeholder);
        return (context, _key) -> key.equals(_key) ? placeholder : null;
    }

    static @NotNull PlaceholderResolver of(@NotNull String key, @NotNull String placeholder) {
        return of(key, context -> placeholder);
    }

    static @NotNull PlaceholderResolver of(Map<String, Placeholder<Context>> placeholders) {
        Objects.requireNonNull(placeholders);
        return (context, key) -> placeholders.get(key);
    }

    static @NotNull PlaceholderResolver of(@NotNull Function<String, Placeholder<Context>> resolve) {
        Objects.requireNonNull(resolve);
        return (context, key) -> resolve.apply(key);
    }

    static <T extends Context> @NotNull PlaceholderResolver of(@NotNull String key, @NotNull Class<T> type, Placeholder<T> placeholder) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(type);
        Objects.requireNonNull(placeholder);
        return (context, _key) -> context != null && type.isAssignableFrom(context.getClass()) && key.equals(_key) ? placeholder : null;
    }

    static @NotNull PlaceholderResolver of(@NotNull PlaceholderResolver @NotNull ... placeholderResolvers) {
        Objects.requireNonNull(placeholderResolvers);
        if (placeholderResolvers.length == 0)
            return empty();
        return (context, key) -> {
            for (PlaceholderResolver placeholderResolver : placeholderResolvers) {
                Placeholder<? extends Context> component = placeholderResolver.resolve(context, key);
                if (component != null)
                    return component;
            }
            return null;
        };
    }
}
