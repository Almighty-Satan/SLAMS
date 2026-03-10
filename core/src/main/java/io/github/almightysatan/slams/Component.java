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

import io.github.almightysatan.slams.impl.InternalComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents some part of a message, might be a constant, a placeholder, ...
 *
 * @param <T> the type of the result
 */
public interface Component<T> {

    /**
     * Evaluates the value of this {@link Component}
     *
     * @param placeholderResolver the local {@link PlaceholderResolver}
     * @param contexts            the contexts
     * @return a value of type {@link T}
     */
    @NotNull T value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts);

    /**
     * Evaluates the value of this {@link Component} as a string
     *
     * @param placeholderResolver the local {@link PlaceholderResolver}
     * @param contexts            the contexts
     * @return the value as a string
     */
    @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull [] contexts);

    /**
     * Creates a new {@link Component} of type string with the same value as {@link #stringValue(PlaceholderResolver, Object[])}
     *
     * @param placeholderResolver the local {@link PlaceholderResolver}
     * @param contexts            the contexts
     * @return a new {@link Component} of type string
     */
    default @NotNull Component<String> stringComponent(@NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull [] contexts) {
        return ofString(this.stringValue(placeholderResolver, contexts));
    }

    /**
     * Returns {@code true} if this {@link Component} always returns the same value and does not depend on any context.
     *
     * @return {@code true} if this {@link Component} always returns the same value and does not depend on any context
     */
    boolean constexpr();

    /**
     * Creates a new {@link Component} from a constant string value
     *
     * @param value the value
     * @return a new {@link Component} of type string
     */
    static @NotNull Component<String> ofString(@NotNull String value) {
        Objects.requireNonNull(value);
        return new InternalComponent<String>() {
            @Override
            public @NotNull String value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return value;
            }

            @Override
            public @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return value;
            }

            @Override
            public boolean constexpr() {
                return true;
            }
        };
    }

    @FunctionalInterface
    static interface ValueFactory<T> {

        /**
         * Converts a string to a value of type {@link T}
         *
         * @param input the string
         * @return a value of type {@link T}
         */
        @NotNull T fromString(@NotNull String input);
    }
}
