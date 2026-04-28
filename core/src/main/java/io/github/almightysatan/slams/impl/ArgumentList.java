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

package io.github.almightysatan.slams.impl;

import io.github.almightysatan.slams.Component;
import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.AbstractList;
import java.util.List;

@ApiStatus.Internal
public class ArgumentList<T> extends AbstractList<Placeholder.Argument<T>> {

    private final List<Component<T>> components;
    private final PlaceholderResolver placeholderResolver;
    private final Object[] contexts;

    public ArgumentList(@Unmodifiable @NotNull List<@NotNull Component<T>> components, @NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull [] contexts) {
        this.components = components;
        this.placeholderResolver = placeholderResolver;
        this.contexts = contexts;
    }

    @Override
    public Placeholder.Argument<T> get(int index) {
        return toArgument(this.components.get(index), this.placeholderResolver, this.contexts);
    }

    @Override
    public int size() {
        return this.components.size();
    }

    public static <T> @NotNull Placeholder.Argument<T> toArgument(@NotNull Component<T> component, @NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull [] contexts) {
        return new Placeholder.Argument<T>() {
            @Override
            public @NotNull T value() {
                return component.value(placeholderResolver, contexts);
            }

            @Override
            public @NotNull String stringValue() {
                return component.stringValue(placeholderResolver, contexts);
            }

            @Override
            public @Nullable Object rawValue() {
                return component.rawValue(placeholderResolver, contexts);
            }

            @Override
            public boolean constexpr() {
                return component.constexpr();
            }
        };
    }
}
