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

public interface Placeholder extends PlaceholderResolver {

    @NotNull String key();

    @NotNull String value(@Nullable Context context, @NotNull List<@NotNull String> arguments);

    @Override
    default @Nullable Placeholder resolve(@NotNull String key) {
        return key.equals(this.key()) ? this : null;
    }

    static @NotNull Placeholder of(@NotNull String key, @NotNull ValueFunction valueFunction) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(valueFunction);
        return new Placeholder() {
            @Override
            public @NotNull String key() {
                return key;
            }

            @Override
            public @NotNull String value(@Nullable Context context, @NotNull List<@NotNull String> arguments) {
                return valueFunction.value(context, arguments);
            }
        };
    }

    static @NotNull Placeholder of(@NotNull String key, @NotNull ContextIndependentValueFunction valueFunction) {
        return of(key, (ValueFunction) valueFunction);
    }

    static @NotNull Placeholder of(@NotNull String key, @NotNull ArgumentIndependentValueFunction valueFunction) {
        return of(key, (ValueFunction) valueFunction);
    }

    static @NotNull Placeholder of(@NotNull String key, @NotNull String value) {
        Objects.requireNonNull(value);
        return of(key, (context, arguments) -> value);
    }

    @SuppressWarnings("unchecked")
    static <T extends Context> @NotNull Placeholder of(@NotNull String key, @NotNull Class<T> type, @NotNull ContextSensitiveValueFunction<T> contextValueFunction, ValueFunction fallbackValueFunction) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(contextValueFunction);
        Objects.requireNonNull(fallbackValueFunction);
        return of(key, (context, arguments) -> context != null && type.isAssignableFrom(context.getClass()) ? contextValueFunction.value((T) context, arguments) : fallbackValueFunction.value(context, arguments));
    }

    static <T extends Context> @NotNull Placeholder of(@NotNull String key, @NotNull Class<T> type, @NotNull ContextSensitiveValueFunction<T> contextValueFunction, @NotNull String fallbackValue) {
        return of(key, type, contextValueFunction, (context, arguments) -> fallbackValue);
    }

    static <T extends Context> @NotNull Placeholder of(@NotNull String key, @NotNull Class<T> type, @NotNull ContextSensitiveValueFunction<T> contextValueFunction) {
        return of(key, type, contextValueFunction, "INVALID_CONTEXT");
    }

    static <T extends Context> @NotNull Placeholder of(@NotNull String key, @NotNull Class<T> type, @NotNull ContextSensitiveArgumentIndependentValueFunction<T> contextValueFunction, @NotNull ValueFunction fallbackValueFunction) {
        return of(key, type, (ContextSensitiveValueFunction<T>) contextValueFunction, fallbackValueFunction);
    }

    static <T extends Context> @NotNull Placeholder of(@NotNull String key, @NotNull Class<T> type, @NotNull ContextSensitiveArgumentIndependentValueFunction<T> contextValueFunction, @NotNull String fallbackValue) {
        return of(key, type, (ContextSensitiveValueFunction<T>) contextValueFunction, fallbackValue);
    }

    static <T extends Context> @NotNull Placeholder of(@NotNull String key, @NotNull Class<T> type, @NotNull ContextSensitiveArgumentIndependentValueFunction<T> contextValueFunction) {
        return of(key, type, (ContextSensitiveValueFunction<T>) contextValueFunction);
    }

    @FunctionalInterface
    interface ValueFunction {
        @NotNull String value(@Nullable Context context, @NotNull List<@NotNull String> arguments);
    }

    @FunctionalInterface
    interface ArgumentIndependentValueFunction extends ValueFunction {
        @NotNull String value(@Nullable Context context);

        @Override
        default @NotNull String value(@Nullable Context context, @NotNull List<@NotNull String> arguments) {
            return this.value(context);
        }
    }

    @FunctionalInterface
    interface ContextIndependentValueFunction extends ValueFunction {
        @NotNull String value(@NotNull List<@NotNull String> arguments);

        @Override
        default @NotNull String value(@Nullable Context context, @NotNull List<@NotNull String> arguments) {
            return this.value(arguments);
        }
    }

    @FunctionalInterface
    interface ContextSensitiveValueFunction<T extends Context> {
        @NotNull String value(@NotNull T context, @NotNull List<@NotNull String> arguments);
    }

    @FunctionalInterface
    interface ContextSensitiveArgumentIndependentValueFunction<T extends Context> extends ContextSensitiveValueFunction<T> {
        @NotNull String value(@NotNull T context);

        @Override
        default @NotNull String value(@NotNull T context, @NotNull List<@NotNull String> arguments) {
            return this.value(context);
        }
    }
}
