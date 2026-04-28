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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents some part of a message, might be a constant, a placeholder, ...
 *
 * @param <T> the type of the result
 */
public interface Component<T> {

    static final ValueFactory<String> STRING_FACTORY = input -> input;

    /**
     * Evaluates the value of this {@link Component}
     *
     * @param placeholderResolver the local {@link PlaceholderResolver}
     * @param contexts            the contexts
     * @return a value of type {@link T}
     */
    @NotNull T value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts);

    @ApiStatus.Internal
    default void value(@NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull [] contexts, @NotNull Consumer<T> consumer) {
        consumer.accept(this.value(placeholderResolver, contexts));
    }

    /**
     * Evaluates the value of this {@link Component} as a string
     *
     * @param placeholderResolver the local {@link PlaceholderResolver}
     * @param contexts            the contexts
     * @return the value as a string
     */
    @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull [] contexts);

    @ApiStatus.Internal
    default void stringValue(@NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull [] contexts, @NotNull Consumer<String> consumer) {
        consumer.accept(this.stringValue(placeholderResolver, contexts));
    }

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
     * Evaluates the value of this {@link Component} without converting it to another type.
     * Can be used to pass values between placeholders without converting them.
     *
     * @param placeholderResolver the local {@link PlaceholderResolver}
     * @param contexts            the contexts
     * @return the value without converting it to another type
     */
    default @Nullable Object rawValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
        return null;
    }

    /**
     * Returns {@code true} if this {@link Component} always returns the same value and does not depend on any context.
     *
     * @return {@code true} if this {@link Component} always returns the same value and does not depend on any context
     */
    boolean constexpr();

    /**
     * Creates a new {@link Component} from constant values
     *
     * @param value       the value
     * @param stringValue the value as a string
     * @param rawValue    the original value before being converted
     * @param <T>         the component's type
     * @return a new {@link Component}
     */
    static <T> @NotNull Component<T> of(@NotNull T value, @NotNull String stringValue, @Nullable Object rawValue) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(stringValue);
        return new Component<T>() {
            @Override
            public @NotNull T value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return value;
            }

            @Override
            public @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return stringValue;
            }

            @Override
            public @Nullable Object rawValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return rawValue;
            }

            @Override
            public boolean constexpr() {
                return true;
            }
        };
    }

    /**
     * Creates a new {@link Component} from constant values
     *
     * @param value       the value
     * @param stringValue the value as a string
     * @param <T>         the component's type
     * @return a new {@link Component}
     */
    static <T> @NotNull Component<T> of(@NotNull T value, @NotNull String stringValue) {
        return of(value, stringValue, null);
    }

    /**
     * Creates a new {@link Component} from a constant string value
     *
     * @param value the value
     * @return a new {@link Component} of type string
     */
    static @NotNull Component<String> ofString(@NotNull String value) {
        return of(value, value, null);
    }

    @FunctionalInterface
    static interface ValueFactory<T> {

        /**
         * Converts a string to a value of type {@link T}
         *
         * @param input the string
         * @return a value of type {@link T}
         */
        @NotNull T value(@NotNull String input);

        default @NotNull Component<T> component(@NotNull String input, @Nullable Object raw) {
            T value = this.value(input);
            return of(value, input, raw);
        }

        default @NotNull Component<T> component(@NotNull String input) {
            return this.component(input, null);
        }
        
        default @NotNull Placeholder.ProcessedPlaceholder<T> processedPlaceholder(@NotNull String input, @Nullable Object raw) {
            Component<T> component = this.component(input, raw);
            return (contexts, arguments, factory) -> component;
        }

        default @NotNull Placeholder.ProcessedPlaceholder<T> processedPlaceholder(@NotNull String input) {
            return processedPlaceholder(input, null);
        }
    }
}
