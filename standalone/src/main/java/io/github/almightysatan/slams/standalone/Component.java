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

package io.github.almightysatan.slams.standalone;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

interface Component {

    @NotNull String value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver);

    static @NotNull Component simple(@NotNull String value) {
        Objects.requireNonNull(value);
        return (context, placeholderResolver) -> value;
    }

    static @NotNull Component placeholder(@NotNull String raw, @NotNull String key, @NotNull List<@NotNull String> arguments, @NotNull PlaceholderResolver placeholderResolver) {
        if (key.isEmpty())
            return simple(raw);

        List<String> unmodifiableArguments = Collections.unmodifiableList(arguments);
        Placeholder placeholder = placeholderResolver.resolve(key);
        if (placeholder != null)
            return (context, placeholderResolver0) -> placeholder.value(context, unmodifiableArguments);

        return (context, placeholderResolver0) -> {
            Placeholder placeholder0 = placeholderResolver0.resolve(key);
            if (placeholder0 == null)
                return raw;
            else
                return placeholder0.value(context, unmodifiableArguments);
        };
    }

    static @NotNull Component nestedPlaceholder(@NotNull String raw, @NotNull String key, @NotNull List<@NotNull Component> arguments, @NotNull PlaceholderResolver placeholderResolver) {
        if (key.isEmpty())
            return simple(raw);

        Placeholder placeholder = placeholderResolver.resolve(key);
        if (placeholder != null)
            return (context, placeholderResolver0) -> placeholder.value(context, lazyEvalList(context, placeholderResolver0, arguments));

        return (context, placeholderResolver0) -> {
            Placeholder placeholder0 = placeholderResolver0.resolve(key);
            if (placeholder0 == null)
                return raw;
            else
                return placeholder0.value(context, lazyEvalList(context, placeholderResolver0, arguments));
        };
    }

    static @Unmodifiable @NotNull List<@NotNull String> lazyEvalList(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver, @NotNull List<@NotNull Component> arguments) {
        String[] values = new String[arguments.size()];
        return new AbstractList<String>() {

            @Override
            public int size() {
                return arguments.size();
            }

            @Override
            public String get(int index) {
                if (values[index] != null)
                    return values[index];
                return values[index] = arguments.get(index).value(context, placeholderResolver);
            }
        };
    }
}
