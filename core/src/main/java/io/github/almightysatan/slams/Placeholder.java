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

import io.github.almightysatan.slams.impl.LazyEvalList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

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

    public static final String INVALID_ARGUMENTS = "INVALID_ARGUMENTS";
    public static final String INVALID_CONTEXT = "INVALID_CONTEXT";
    public static final String INVALID_COMPARISON = "INVALID_COMPARISON";
    public static final String INVALID_FORMAT = "INVALID_FORMAT";

    /**
     * The key of this placeholder. This should always return the same value. A key should not be null or empty.
     *
     * @return the key
     */
    @NotNull String key();

    /**
     * Returns {@code true} if this placeholder always returns the same value when supplied with the same arguments and
     * does not depend on any context.
     *
     * @return {@code true} if this placeholder always returns the same value when supplied with the same arguments and
     * does not depend on any context
     */
    boolean constexpr();

    /**
     * Evaluates the value of the placeholder using the given contexts and arguments.
     *
     * @param contexts  the contexts provided to the message
     * @param arguments the placeholder's arguments
     * @param factory   a factory to create the result type
     * @param <T>       the result type (e.g. {@link String})
     * @return the value of this placeholder
     */
    <T> @NotNull Component<T> value(@NotNull Object @NotNull [] contexts, @Unmodifiable @NotNull List<@NotNull Argument<T>> arguments,
            @NotNull Component.ValueFactory<T> factory);

    /**
     * A method that can be used to parse constant arguments when a message is loaded initialy to allow for some
     * optimizations. Arguments may be {@code null} if they can not be resolved to constant values. Returning
     * {@code null} from this method indicates that {{@link #value(Object[], List, Component.ValueFactory)}} should be
     * called instead.
     * 
     * Placeholders are not required to implement this method, it is optional.
     * 
     * Note that depending on the SLAMS implementation being used this method may never be called at all.
     *
     * @param arguments the placeholder's arguments, elements can be {@code null} if they are not constant values
     * @param factory   a factory to create the result type
     * @param <T>       the result type (e.g. {@link String})
     * @return a representation of this placeholder where some arguments have been parsed or {@code null}
     */
    default <T> @Nullable ProcessedPlaceholder<T> processArguments(@Unmodifiable @NotNull List<@Nullable Argument<T>> arguments,
            @NotNull Component.ValueFactory<T> factory) {
        return null;
    }

    @Override
    default @Nullable Placeholder resolve(@NotNull String key) {
        return key.equals(this.key()) ? this : null;
    }

    /**
     * Returns a new {@link Placeholder}.
     *
     * @param key           the placeholder's key
     * @param constexpr     {@code true} if this placeholder always returns the same value and does not depend on any context
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder of(@NotNull String key, boolean constexpr, @NotNull ValueFunction valueFunction) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(valueFunction);
        return new Placeholder() {
            @Override
            public @NotNull String key() {
                return key;
            }

            @Override
            public boolean constexpr() {
                return constexpr;
            }

            @Override
            public @NotNull <T> Component<T> value(@NotNull Object @NotNull [] contexts, @Unmodifiable @NotNull List<@NotNull Argument<T>> arguments, Component.@NotNull ValueFactory<T> factory) {
                List<String> list = new LazyEvalList<Argument<T>, String>(Argument::stringValue, arguments);
                Object raw = valueFunction.value(contexts, list);
                return factory.componentFromString(String.valueOf(raw), raw);
            }
        };
    }

    /**
     * Returns a new {@link Placeholder}. The contexts are ignored when evaluating its value.
     *
     * @param key           the placeholder's key
     * @param constexpr     {@code true} if this placeholder always returns the same value and does not depend on any context
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder withArgs(@NotNull String key, boolean constexpr, @NotNull ContextIndependentValueFunction valueFunction) {
        return of(key, constexpr, valueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. The contexts are ignored when evaluating its value.
     *
     * @param key           the placeholder's key
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder withArgs(@NotNull String key, @NotNull ContextIndependentValueFunction valueFunction) {
        return withArgs(key, false, valueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. Arguments are ignored when evaluating its value.
     *
     * @param key           the placeholder's key
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder withContext(@NotNull String key, @NotNull ArgumentIndependentValueFunction valueFunction) {
        return of(key, false, valueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. Arguments and contexts are ignored when evaluating its value.
     *
     * @param key           the placeholder's key
     * @param valueFunction a function that evaluates this placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder variable(@NotNull String key, @NotNull ArgumentAndContextIndependentValueFunction valueFunction) {
        Objects.requireNonNull(valueFunction);
        return of(key, false, valueFunction);
    }

    /**
     * Returns a new {@link Placeholder} with a constant value.
     *
     * @param key   the placeholder's key
     * @param value the value
     * @return a new placeholder
     */
    static @NotNull Placeholder constant(@NotNull String key, @NotNull Object value) {
        Objects.requireNonNull(value);
        return of(key, true, (contexts, args) -> value);
    }

    /**
     * Returns a new {@link Placeholder}. If a context of the given type exists, {@code contextValueFunction} will be
     * used to evaluate the value. Otherwise {@code fallbackValueFunction} will be
     * used.
     *
     * @param key                   the placeholder's key
     * @param type                  class of the context type
     * @param contextValueFunction  a function that evaluates this placeholder's value
     * @param fallbackValueFunction a function that evaluates this placeholder's value
     * @param <T>                   the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ContextValueFunction<T> contextValueFunction, @NotNull ValueFunction fallbackValueFunction) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(contextValueFunction);
        Objects.requireNonNull(fallbackValueFunction);
        return of(key, false, (contexts, arguments) -> {
            for (Object c : contexts)
                if (type.isAssignableFrom(c.getClass()))
                    return contextValueFunction.value((T) c, arguments);
            return fallbackValueFunction.value(contexts, arguments);
        });
    }

    /**
     * Returns a new {@link Placeholder}. If a context of the given type exists, {@code contextValueFunction} will be
     * used to evaluate the value. Otherwise {@code fallbackValue} will be used.
     *
     * @param key                  the placeholder's key
     * @param type                 class of the context type
     * @param contextValueFunction a function that evaluates this placeholder's value
     * @param fallbackValue        the fallback value
     * @param <T>                  the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ContextValueFunction<T> contextValueFunction, @NotNull String fallbackValue) {
        return contextual(key, type, contextValueFunction, (contexts, arguments) -> fallbackValue);
    }

    /**
     * Returns a new {@link Placeholder}. If a context of the given type exists, {@code contextValueFunction} will be
     * used to evaluate the value.
     *
     * @param key                  the placeholder's key
     * @param type                 class of the context type
     * @param contextValueFunction a function that evaluates this placeholder's value
     * @param <T>                  the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ContextValueFunction<T> contextValueFunction) {
        return contextual(key, type, contextValueFunction, INVALID_CONTEXT);
    }

    /**
     * Returns a new {@link Placeholder}. If a context of the given type exists, {@code contextValueFunction} will be
     * used to evaluate the value. Otherwise {@code fallbackValueFunction} will be used. Possible arguments passed to
     * the placeholder are ignored.
     *
     * @param key                   the placeholder's key
     * @param type                  class of the context type
     * @param contextValueFunction  a function that evaluates this placeholder's value
     * @param fallbackValueFunction a function that evaluates this placeholder's value
     * @param <T>                   the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ArgumentIndependentContextValueFunction<T> contextValueFunction, @NotNull ValueFunction fallbackValueFunction) {
        return contextual(key, type, (ContextValueFunction<T>) contextValueFunction, fallbackValueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. If a context of the given type exists, {@code contextValueFunction} will be
     * used to evaluate the value. Otherwise {@code fallbackValue} will be used. Possible arguments passed to the
     * placeholder are ignored.
     *
     * @param key                  the placeholder's key
     * @param type                 class of the context type
     * @param contextValueFunction a function that evaluates this placeholder's value
     * @param fallbackValue        the fallback value
     * @param <T>                  the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ArgumentIndependentContextValueFunction<T> contextValueFunction, @NotNull String fallbackValue) {
        return contextual(key, type, (ContextValueFunction<T>) contextValueFunction, fallbackValue);
    }

    /**
     * Returns a new {@link Placeholder}. If a context of the given type exists, {@code contextValueFunction} will be
     * used to evaluate the value. Possible arguments passed to the placeholder are ignored.
     *
     * @param key                  the placeholder's key
     * @param type                 class of the context type
     * @param contextValueFunction a function that evaluates this placeholder's value
     * @param <T>                  the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull ArgumentIndependentContextValueFunction<T> contextValueFunction) {
        return contextual(key, type, (ContextValueFunction<T>) contextValueFunction);
    }

    /**
     * Returns a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
     * argument as its value, otherwise the second argument is returned. If no context of the given type can be found,
     * {@code fallbackValueFunction} will be used instead.
     * Example format: {@code Hello <hasName:<name>:unknown user>!}
     *
     * @param key                   the placeholder's key
     * @param type                  class of the context type
     * @param predicate             takes in a context if the given type and is used to check whether the first or
     *                              second argument should be returned as this placeholder's value
     * @param fallbackValueFunction a function that evaluates this placeholder's value
     * @param <T>                   the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate, @NotNull ValueFunction fallbackValueFunction) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(type);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(fallbackValueFunction);
        return new Placeholder() {
            @Override
            public @NotNull String key() {
                return key;
            }

            @Override
            public boolean constexpr() {
                return false;
            }

            @Override
            public @NotNull <U> Component<U> value(@NotNull Object @NotNull [] contexts,
                    @Unmodifiable @NotNull List<@NotNull Argument<U>> arguments, Component.@NotNull ValueFactory<U> factory) {
                for (Object context : contexts)
                    if (type.isAssignableFrom(context.getClass())) {
                        if (predicate.test((T) context))
                            return !arguments.isEmpty() ? arguments.get(0) : factory.componentFromString("");
                        return arguments.size() > 1 ? arguments.get(1) : factory.componentFromString("");
                    }
                List<String> list = new LazyEvalList<Argument<U>, String>(Argument::stringValue, arguments);
                Object raw = fallbackValueFunction.value(contexts, list);
                return factory.componentFromString(String.valueOf(raw), raw);
            }
        };
    }

    /**
     * Returns a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
     * argument as its value, otherwise the second argument is returned. If no context of the given type can be found,
     * {@code fallbackValue} will be used instead.
     * Example format: {@code Hello <hasName:<name>:unknown user>!}
     *
     * @param key           the placeholder's key
     * @param type          class of the context type
     * @param predicate     takes in a context if the given type and is used to check whether the first or
     *                      second argument should be returned as this placeholder's value
     * @param fallbackValue the fallback value
     * @param <T>           the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate, @NotNull String fallbackValue) {
        Objects.requireNonNull(fallbackValue);
        return conditional(key, type, predicate, (contexts, arguments) -> fallbackValue);
    }

    /**
     * Returns a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
     * argument as its value, otherwise the second argument is returned.
     * Example format: {@code Hello <hasName:<name>:unknown user>!}
     *
     * @param key       the placeholder's key
     * @param type      class of the context type
     * @param predicate takes in a context if the given type and is used to check whether the first or
     *                  second argument should be returned as this placeholder's value
     * @param <T>       the context type
     * @return a new placeholder
     */
    static <T> @NotNull Placeholder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate) {
        return conditional(key, type, predicate, INVALID_CONTEXT);
    }

    /**
     * Returns a new {@link Placeholder}. If the supplier is {@code true} this placeholder will return the first
     * argument as its value, otherwise the second argument is returned.
     * Example format: {@code Hello <hasName:<name>:unknown user>!}
     *
     * @param key      the placeholder's key
     * @param supplier used to check whether the first or second argument should be returned as this
     *                 placeholder's value
     * @return a new placeholder
     */
    static @NotNull Placeholder conditional(@NotNull String key, @NotNull BooleanSupplier supplier) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(supplier);
        return new Placeholder() {
            @Override
            public @NotNull String key() {
                return key;
            }

            @Override
            public boolean constexpr() {
                return false;
            }

            @Override
            public @NotNull <T> Component<T> value(@NotNull Object @NotNull [] contexts,
                    @Unmodifiable @NotNull List<@NotNull Argument<T>> arguments, Component.@NotNull ValueFactory<T> factory) {
                if (supplier.getAsBoolean())
                    return !arguments.isEmpty() ? arguments.get(0) : factory.componentFromString("");
                return arguments.size() > 1 ? arguments.get(1) : factory.componentFromString("");
            }
        };
    }

    /**
     * Returns a new {@link Placeholder}. This placeholder compares its first two arguments using the given function. If
     * the function evaluates to {@code true}, this placeholder will return the third argument as its value, otherwise
     * the fourth argument is returned. It is assumed, that the given function always returns the same result when
     * provided with the same input.
     * Example format: {@code <if_eq:<time>:1:1 second:<time> seconds>}
     *
     * @param key                the placeholder's key
     * @param comparisonFunction a function that compares the first two arguments of this placeholder
     * @return a new placeholder
     */
    static @NotNull Placeholder comparison(@NotNull String key, @NotNull ComparisonFunction comparisonFunction) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(comparisonFunction);
        return new Placeholder() {
            @Override
            public @NotNull String key() {
                return key;
            }

            @Override
            public boolean constexpr() {
                return true;
            }

            @Override
            public @NotNull <T> Component<T> value(@NotNull Object @NotNull [] contexts,
                    @Unmodifiable @NotNull List<@NotNull Argument<T>> arguments, Component.@NotNull ValueFactory<T> factory) {
                if (arguments.size() < 2)
                    return factory.componentFromString(INVALID_COMPARISON);
                if (comparisonFunction.value(arguments.get(0).stringValue(), arguments.get(1).stringValue()))
                    return arguments.size() > 2 ? arguments.get(2) : factory.componentFromString("");
                return arguments.size() > 3 ? arguments.get(3) : factory.componentFromString("");
            }
        };
    }

    @FunctionalInterface
    interface ProcessedPlaceholder<T> {

        /**
         * Evaluates the value of the placeholder using the given contexts and arguments.
         *
         * @param contexts  the contexts provided to the message
         * @param arguments the placeholder's arguments
         * @param factory   a factory to create the result type
         * @return the value of this placeholder
         */
        @NotNull Component<T> value(@NotNull Object @NotNull [] contexts, @Unmodifiable @NotNull List<@NotNull Argument<T>> arguments,
                @NotNull Component.ValueFactory<T> factory);
    }

    interface Argument<T> extends Component<T> {

        @NotNull T value();

        @Override
        default @NotNull T value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
            return this.value();
        }

        @NotNull String stringValue();

        @Override
        default @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
            return this.stringValue();
        }

        @Nullable Object rawValue();

        @Override
        default @Nullable Object rawValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
            return this.rawValue();
        }
    }

    @FunctionalInterface
    interface ValueFunction {
        @NotNull Object value(@NotNull Object @NotNull [] contexts, @NotNull List<@NotNull String> arguments);
    }

    @FunctionalInterface
    interface ArgumentIndependentValueFunction extends ValueFunction {
        @NotNull Object value(@Nullable Object @NotNull [] contexts);

        @Override
        default @NotNull Object value(@NotNull Object @NotNull [] contexts, @NotNull List<@NotNull String> arguments) {
            return this.value(contexts);
        }
    }

    @FunctionalInterface
    interface ContextIndependentValueFunction extends ValueFunction {
        @NotNull Object value(@NotNull List<@NotNull String> arguments);

        @Override
        default @NotNull Object value(@NotNull Object @NotNull [] contexts, @NotNull List<@NotNull String> arguments) {
            return this.value(arguments);
        }
    }

    @FunctionalInterface
    interface ArgumentAndContextIndependentValueFunction extends ValueFunction {
        @NotNull Object value();

        @Override
        default @NotNull Object value(@NotNull Object @NotNull [] contexts, @NotNull List<@NotNull String> arguments) {
            return this.value();
        }
    }

    @FunctionalInterface
    interface ContextValueFunction<T> {
        @NotNull Object value(@NotNull T context, @NotNull List<@NotNull String> arguments);
    }

    @FunctionalInterface
    interface ArgumentIndependentContextValueFunction<T> extends ContextValueFunction<T> {
        @NotNull Object value(@NotNull T context);

        @Override
        default @NotNull Object value(@NotNull T context, @NotNull List<@NotNull String> arguments) {
            return this.value(context);
        }
    }

    @FunctionalInterface
    interface ComparisonFunction {
        boolean value(@NotNull String argument0, @NotNull String argument1);
    }
}
