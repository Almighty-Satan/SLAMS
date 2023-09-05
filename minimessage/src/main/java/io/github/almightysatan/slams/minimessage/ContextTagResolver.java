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

package io.github.almightysatan.slams.minimessage;

import io.github.almightysatan.slams.Context;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

@FunctionalInterface
public interface ContextTagResolver<T extends Context> {

    @NotNull TagResolver resolve(@Nullable T context);

    static @NotNull ContextTagResolver<Context> empty() {
        return context -> TagResolver.empty();
    }

    static @NotNull ContextTagResolver<Context> parsed(@NotNull String key, @NotNull String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return context -> Placeholder.parsed(key, value);
    }

    static @NotNull ContextTagResolver<Context> unparsed(@NotNull String key, @NotNull String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return context -> Placeholder.unparsed(key, value);
    }

    static @NotNull ContextTagResolver<Context> of(@NotNull TagResolver tagResolver) {
        Objects.requireNonNull(tagResolver);
        return context -> tagResolver;
    }

    static @NotNull ContextTagResolver<Context> of(@NotNull TagResolver... tagResolvers) {
        Objects.requireNonNull(tagResolvers);
        if (tagResolvers.length == 0)
            return empty();
        return context -> TagResolver.resolver(tagResolvers);
    }

    @SuppressWarnings("unchecked")
    static <T extends Context> @NotNull ContextTagResolver<Context> of(@NotNull Class<T> type, @NotNull ContextTagResolver<T> contextTagResolver) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(contextTagResolver);
        return context -> context != null && type.isAssignableFrom(context.getClass()) ? contextTagResolver.resolve((T) context) : TagResolver.empty();
    }

    @SafeVarargs
    static @NotNull ContextTagResolver<Context> of(@NotNull ContextTagResolver<Context>... contextTagResolvers) {
        Objects.requireNonNull(contextTagResolvers);
        if (contextTagResolvers.length == 0)
            return empty();
        return context -> TagResolver.resolver(Arrays.stream(contextTagResolvers).map(resolver -> resolver.resolve(context)).toArray(TagResolver[]::new));
    }
}
