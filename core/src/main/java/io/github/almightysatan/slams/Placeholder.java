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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Represents a placeholder. Extends {@link PlaceholderResolver} as a placeholder can resolve itself.
 *
 * @see Placeholder#constant(String, String)
 */
public interface Placeholder extends PlaceholderResolver {

    /**
     * The key of this placeholder. This should always return the same value. A key should not be null or empty.
     *
     * @return the key
     */
    @NotNull String key();

    /**
     * Evaluates the value of the placeholder using the given context and arguments.
     *
     * @param context   the context
     * @param arguments the arguments
     * @return the value of this placeholder
     */
    @NotNull String value(@Nullable Context context, @NotNull List<@NotNull String> arguments);

    @Override
    default @Nullable Placeholder resolve(@NotNull String key) {
        return key.equals(this.key()) ? this : null;
    }

    /**
     * Returns a new {@link Placeholder}.
     *
     * @param key           the placeholder's key
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
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

    /**
     * Returns a new {@link Placeholder}. The {@link Context} is ignored when evaluating its value.
     *
     * @param key           the placeholder's key
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder withArgs(@NotNull String key, @NotNull ContextIndependentValueFunction valueFunction) {
        return of(key, valueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. Arguments are ignored when evaluating its value.
     *
     * @param key           the placeholder's key
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder withContext(@NotNull String key, @NotNull ArgumentIndependentValueFunction valueFunction) {
        return of(key, valueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. Arguments and {@link Context} are ignored when evaluating its value.
     *
     * @param key           the placeholder's key
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder variable(@NotNull String key, @NotNull ArgumentAndContextIndependentValueFunction valueFunction) {
        Objects.requireNonNull(valueFunction);
        return of(key, valueFunction);
    }

    /**
     * Returns a new {@link Placeholder} with a constant value.
     *
     * @param key   the placeholder's key
     * @param value the value
     * @return a new placeholder
     */
    static @NotNull Placeholder constant(@NotNull String key, @NotNull String value) {
        Objects.requireNonNull(value);
        return of(key, (context, arguments) -> value);
    }

    /**
     * Returns a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
     * {@code contextValueFunction} will be used to evaluate the value. Otherwise {@code fallbackValueFunction} will be
     * used.
     *
     * @param key                   the placeholder's key
     * @param type                  class of the context type
     * @param contextValueFunction  a function that evaluates this placeholder's value
     * @param fallbackValueFunction a function that evaluates this placeholder's value
     * @param <T>                   the context type
     * @return a new placeholder
     */
    @SuppressWarnings("unchecked")
    static <T extends Context> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ContextualValueFunction<T> contextValueFunction, @NotNull ValueFunction fallbackValueFunction) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(contextValueFunction);
        Objects.requireNonNull(fallbackValueFunction);
        return of(key, (context, arguments) -> context != null && type.isAssignableFrom(context.getClass()) ? contextValueFunction.value((T) context, arguments) : fallbackValueFunction.value(context, arguments));
    }

    /**
     * Returns a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
     * {@code contextValueFunction} will be used to evaluate the value. Otherwise {@code fallbackValue} will be used.
     *
     * @param key                  the placeholder's key
     * @param type                 class of the context type
     * @param contextValueFunction a function that evaluates this placeholder's value
     * @param fallbackValue        the fallback value
     * @param <T>                  the context type
     * @return a new placeholder
     */
    static <T extends Context> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ContextualValueFunction<T> contextValueFunction, @NotNull String fallbackValue) {
        return contextual(key, type, contextValueFunction, (context, arguments) -> fallbackValue);
    }

    /**
     * Returns a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
     * {@code contextValueFunction} will be used to evaluate the value.
     *
     * @param key                  the placeholder's key
     * @param type                 class of the context type
     * @param contextValueFunction a function that evaluates this placeholder's value
     * @param <T>                  the context type
     * @return a new placeholder
     */
    static <T extends Context> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ContextualValueFunction<T> contextValueFunction) {
        return contextual(key, type, contextValueFunction, "INVALID_CONTEXT");
    }

    /**
     * Returns a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
     * {@code contextValueFunction} will be used to evaluate the value. Otherwise {@code fallbackValueFunction} will be
     * used. Possible arguments passed to the placeholder are ignored.
     *
     * @param key                   the placeholder's key
     * @param type                  class of the context type
     * @param contextValueFunction  a function that evaluates this placeholder's value
     * @param fallbackValueFunction a function that evaluates this placeholder's value
     * @param <T>                   the context type
     * @return a new placeholder
     */
    static <T extends Context> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ArgumentIndependentContextualValueFunction<T> contextValueFunction, @NotNull ValueFunction fallbackValueFunction) {
        return contextual(key, type, (ContextualValueFunction<T>) contextValueFunction, fallbackValueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
     * {@code contextValueFunction} will be used to evaluate the value. Otherwise {@code fallbackValue} will be used.
     * Possible arguments passed to the placeholder are ignored.
     *
     * @param key                  the placeholder's key
     * @param type                 class of the context type
     * @param contextValueFunction a function that evaluates this placeholder's value
     * @param fallbackValue        the fallback value
     * @param <T>                  the context type
     * @return a new placeholder
     */
    static <T extends Context> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ArgumentIndependentContextualValueFunction<T> contextValueFunction, @NotNull String fallbackValue) {
        return contextual(key, type, (ContextualValueFunction<T>) contextValueFunction, fallbackValue);
    }

    /**
     * Returns a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
     * {@code contextValueFunction} will be used to evaluate the value. Possible arguments passed to the placeholder are
     * ignored.
     *
     * @param key                  the placeholder's key
     * @param type                 class of the context type
     * @param contextValueFunction a function that evaluates this placeholder's value
     * @param <T>                  the context type
     * @return a new placeholder
     */
    static <T extends Context> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ArgumentIndependentContextualValueFunction<T> contextValueFunction) {
        return contextual(key, type, (ContextualValueFunction<T>) contextValueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
     * argument as its value, otherwise the second argument is returned. If the {@link Context} is {@code null} or not
     * of the given type, {@code fallbackValueFunction} will be used instead.
     * Example format: {@code Hello <hasName:<name>:unknown user>!}
     *
     * @param key                   the placeholder's key
     * @param type                  class of the context type
     * @param predicate             takes in the current {@link Context} and is used to check whether the first or
     *                              second argument should be returned as this placeholder's value
     * @param fallbackValueFunction a function that evaluates this placeholder's value
     * @param <T>                   the context type
     * @return a new placeholder
     */
    static <T extends Context> @NotNull Placeholder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate, @NotNull ValueFunction fallbackValueFunction) {
        Objects.requireNonNull(predicate);
        return contextual(key, type, (context, arguments) -> {
            if (predicate.test(context))
                return !arguments.isEmpty() ? arguments.get(0) : "";
            else
                return arguments.size() > 1 ? arguments.get(1) : "";
        }, fallbackValueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
     * argument as its value, otherwise the second argument is returned. If the {@link Context} is {@code null} or not
     * of the given type, {@code fallbackValue} will be used instead.
     * Example format: {@code Hello <hasName:<name>:unknown user>!}
     *
     * @param key                   the placeholder's key
     * @param type                  class of the context type
     * @param predicate             takes in the current {@link Context} and is used to check whether the first or
     *                              second argument should be returned as this placeholder's value
     * @param fallbackValue         the fallback value
     * @param <T>                   the context type
     * @return a new placeholder
     */
    static <T extends Context> @NotNull Placeholder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate, @NotNull String fallbackValue) {
        Objects.requireNonNull(fallbackValue);
        return conditional(key, type, predicate, (context, arguments) -> fallbackValue);
    }

    /**
     * Returns a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
     * argument as its value, otherwise the second argument is returned.
     * Example format: {@code Hello <hasName:<name>:unknown user>!}
     *
     * @param key                   the placeholder's key
     * @param type                  class of the context type
     * @param predicate             takes in the current {@link Context} and is used to check whether the first or
     *                              second argument should be returned as this placeholder's value
     * @param <T>                   the context type
     * @return a new placeholder
     */
    static <T extends Context> @NotNull Placeholder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate) {
        return conditional(key, type, predicate, "INVALID_CONTEXT");
    }

    /**
     * Returns a new {@link Placeholder}. If the supplier is {@code true} this placeholder will return the first
     * argument as its value, otherwise the second argument is returned.
     * Example format: {@code Hello <hasName:<name>:unknown user>!}
     *
     * @param key                   the placeholder's key
     * @param supplier              used to check whether the first or second argument should be returned as this
     *                              placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder conditional(@NotNull String key, @NotNull BooleanSupplier supplier) {
        Objects.requireNonNull(supplier);
        return of(key, (context, arguments) -> {
            if (supplier.getAsBoolean())
                return !arguments.isEmpty() ? arguments.get(0) : "";
            else
                return arguments.size() > 1 ? arguments.get(1) : "";
        });
    }

    /**
     * Returns a new {@link Placeholder}. This placeholder compares its first two arguments using the given function. If
     * the function evaluates to {@code true}, this placeholder will return the third argument as its value, otherwise
     * the fourth argument is returned.
     * Example format: {@code <if_eq:<time>:1:1 second:<time> seconds>}
     *
     * @param key                   the placeholder's key
     * @param comparisonFunction    a function that compares the first two arguments of this placeholder
     * @return a new placeholder
     */
    static @NotNull Placeholder comparison(@NotNull String key, @NotNull ComparisonFunction comparisonFunction) {
        Objects.requireNonNull(comparisonFunction);
        return of(key, (context, arguments) -> {
            if (arguments.size() < 2)
                return "INVALID_COMPARISON";
            if (comparisonFunction.value(arguments.get(0), arguments.get(1)))
                return arguments.size() > 2 ? arguments.get(2) : "";
            else
                return arguments.size() > 3 ? arguments.get(3) : "";
        });
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
    interface ArgumentAndContextIndependentValueFunction extends ValueFunction {
        @NotNull String value();

        @Override
        default @NotNull String value(@Nullable Context context, @NotNull List<@NotNull String> arguments) {
            return this.value();
        }
    }

    @FunctionalInterface
    interface ContextualValueFunction<T extends Context> {
        @NotNull String value(@NotNull T context, @NotNull List<@NotNull String> arguments);
    }

    @FunctionalInterface
    interface ArgumentIndependentContextualValueFunction<T extends Context> extends ContextualValueFunction<T> {
        @NotNull String value(@NotNull T context);

        @Override
        default @NotNull String value(@NotNull T context, @NotNull List<@NotNull String> arguments) {
            return this.value(context);
        }
    }

    @FunctionalInterface
    interface ComparisonFunction {
        boolean value(@NotNull String argument0, @NotNull String argument1);
    }
}
