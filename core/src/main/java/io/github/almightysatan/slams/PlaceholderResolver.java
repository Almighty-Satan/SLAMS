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

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Resolves {@link Placeholder Placeholders}.
 */
@FunctionalInterface
public interface PlaceholderResolver {

    PlaceholderResolver EMPTY = key -> null;

    /**
     * Searches for a {@link Placeholder} with the given key. SLAMS may cache the resulting {@link Placeholder} if
     * possible.
     *
     * @param key the key
     * @return the placeholder or {@code null}
     */
    @Nullable Placeholder resolve(@NotNull String key);

    /**
     * Returns a {@link PlaceholderResolver} that always returns null (does not resolve any
     * {@link Placeholder Placeholders}).
     *
     * @return a {@link PlaceholderResolver} that always returns null
     */
    static @NotNull PlaceholderResolver empty() {
        return EMPTY;
    }

    /**
     * Returns a {@link PlaceholderResolver} that only resolves a single {@link Placeholder}.
     *
     * @param placeholder the {@link Placeholder}
     * @return a new {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver of(@NotNull Placeholder placeholder) {
        Objects.requireNonNull(placeholder);
        return key -> key.equals(placeholder.key()) ? placeholder : null;
    }

    /**
     * Returns a {@link PlaceholderResolver} that can resolve all the given {@link Placeholder Placeholders}.
     *
     * @param placeholders an array of {@link Placeholder Placeholders}
     * @return a new {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver of(@NotNull Placeholder @NotNull ... placeholders) {
        if (placeholders.length == 0)
            return empty();
        return builder().add(placeholders).build();
    }

    /**
     * Returns a {@link PlaceholderResolver} that will use the given {@link PlaceholderResolver PlaceholderResolvers} to
     * find a {@link Placeholder}. It can therefore resolve any {@link Placeholder} resolved by at least one of the
     * given {@link PlaceholderResolver PlaceholderResolvers}.
     *
     * @param placeholderResolvers an array of {@link PlaceholderResolver PlaceholderResolvers}
     * @return a new {@link PlaceholderResolver}
     */
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

    /**
     * Returns a {@link PlaceholderResolver} that will use the given {@link PlaceholderResolver PlaceholderResolvers} to
     * find a {@link Placeholder}. It can therefore resolve any {@link Placeholder} resolved by at least one of the
     * given {@link PlaceholderResolver PlaceholderResolvers}.
     *
     * @param placeholderResolvers a list of {@link PlaceholderResolver PlaceholderResolvers}
     * @return a new {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver of(@NotNull List<@NotNull PlaceholderResolver> placeholderResolvers) {
        return of(placeholderResolvers.toArray(new PlaceholderResolver[0]));
    }

    /**
     * Returns a new {@link Builder Builder}.
     *
     * @return a new {@link Builder Builder}
     */
    static @NotNull Builder builder() {
        return new Builder() {
            private final Map<String, Placeholder> placeholderMap = new HashMap<>();

            @Override
            public @NotNull PlaceholderResolver build() {
                return this.placeholderMap::get;
            }

            @Override
            public @NotNull Builder add(@NotNull Placeholder placeholder) {
                String key = Objects.requireNonNull(placeholder.key());
                this.placeholderMap.put(key, placeholder);
                return this;
            }
        };
    }

    /**
     * Used to build a {@link PlaceholderResolver}.
     */
    interface Builder {

        @NotNull PlaceholderResolver build();

        /**
         * Adds a new {@link Placeholder}.
         *
         * @param placeholder the {@link Placeholder}
         * @return this {@link Builder}
         */
        @NotNull Builder add(@NotNull Placeholder placeholder);

        /**
         * Adds a new {@link Placeholder}.
         *
         * @param key           the placeholder's key
         * @param valueFunction a function that evaluates this placeholder's value
         * @return this {@link Builder}
         */
        default @NotNull Builder add(@NotNull String key, @NotNull Placeholder.ValueFunction valueFunction) {
            return this.add(Placeholder.of(key, valueFunction));
        }

        /**
         * Adds multiple {@link Placeholder Placeholders}.
         *
         * @param placeholders an array of {@link Placeholder Placeholders}
         * @return this {@link Builder}
         */
        default @NotNull Builder add(@NotNull Placeholder @NotNull ... placeholders) {
            for (Placeholder placeholder : placeholders)
                this.add(placeholder);
            return this;
        }

        /**
         * Adds multiple {@link Placeholder Placeholders}.
         *
         * @param placeholders a {@link Collection} of {@link Placeholder Placeholders}
         * @return this {@link Builder}
         */
        default @NotNull Builder add(@NotNull Collection<@NotNull Placeholder> placeholders) {
            for (Placeholder placeholder : placeholders)
                this.add(placeholder);
            return this;
        }

        /**
         * Adds a new placeholder. The {@link Context} is ignored when evaluating its value.
         *
         * @param key           the placeholder's key
         * @param valueFunction a function that evaluates this placeholder's value
         * @return this {@link Builder}
         */
        default @NotNull Builder withArgs(@NotNull String key, @NotNull Placeholder.ContextIndependentValueFunction valueFunction) {
            return this.add(Placeholder.withArgs(key, valueFunction));
        }

        /**
         * Adds a new placeholder. Arguments are ignored when evaluating its value.
         *
         * @param key           the placeholder's key
         * @param valueFunction a function that evaluates this placeholder's value
         * @return this {@link Builder}
         */
        default @NotNull Builder withContext(@NotNull String key, @NotNull Placeholder.ArgumentIndependentValueFunction valueFunction) {
            return this.add(Placeholder.withContext(key, valueFunction));
        }

        /**
         * Adds a new placeholder. Arguments and {@link Context} are ignored when evaluating its value.
         *
         * @param key           the placeholder's key
         * @param valueFunction a function that evaluates this placeholder's value
         * @return this {@link Builder}
         */
        default @NotNull Builder variable(@NotNull String key, @NotNull Placeholder.ArgumentAndContextIndependentValueFunction valueFunction) {
            return this.add(Placeholder.variable(key, valueFunction));
        }

        /**
         * Adds a new placeholder with a constant value.
         *
         * @param key   the placeholder's key
         * @param value the value
         * @return this {@link Builder}
         */
        default @NotNull Builder constant(@NotNull String key, @NotNull String value) {
            return this.add(Placeholder.constant(key, value));
        }

        /**
         * Adds a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
         * {@code contextValueFunction} will be used to evaluate the value. Otherwise {@code fallbackValueFunction} will be
         * used.
         *
         * @param key                   the placeholder's key
         * @param type                  class of the context type
         * @param contextValueFunction  a function that evaluates this placeholder's value
         * @param fallbackValueFunction a function that evaluates this placeholder's value
         * @param <T>                   the context type
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull Placeholder.ContextualValueFunction<T> contextValueFunction, @NotNull Placeholder.ValueFunction fallbackValueFunction) {
            return this.add(Placeholder.contextual(key, type, contextValueFunction, fallbackValueFunction));
        }

        /**
         * Adds a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
         * {@code contextValueFunction} will be used to evaluate the value. Otherwise {@code fallbackValue} will be used.
         *
         * @param key                  the placeholder's key
         * @param type                 class of the context type
         * @param contextValueFunction a function that evaluates this placeholder's value
         * @param fallbackValue        the fallback value
         * @param <T>                  the context type
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull Placeholder.ContextualValueFunction<T> contextValueFunction, @NotNull String fallbackValue) {
            return this.add(Placeholder.contextual(key, type, contextValueFunction, fallbackValue));
        }

        /**
         * Adds a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
         * {@code contextValueFunction} will be used to evaluate the value.
         *
         * @param key                  the placeholder's key
         * @param type                 class of the context type
         * @param contextValueFunction a function that evaluates this placeholder's value
         * @param <T>                  the context type
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull Placeholder.ContextualValueFunction<T> contextValueFunction) {
            return this.add(Placeholder.contextual(key, type, contextValueFunction));
        }

        /**
         * Adds a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
         * {@code contextValueFunction} will be used to evaluate the value. Otherwise {@code fallbackValueFunction} will be
         * used. Possible arguments passed to the placeholder are ignored.
         *
         * @param key                   the placeholder's key
         * @param type                  class of the context type
         * @param contextValueFunction  a function that evaluates this placeholder's value
         * @param fallbackValueFunction a function that evaluates this placeholder's value
         * @param <T>                   the context type
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull Placeholder.ArgumentIndependentContextualValueFunction<T> contextValueFunction, @NotNull Placeholder.ValueFunction fallbackValueFunction) {
            return this.add(Placeholder.contextual(key, type, contextValueFunction, fallbackValueFunction));
        }

        /**
         * Adds a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
         * {@code contextValueFunction} will be used to evaluate the value. Otherwise {@code fallbackValue} will be used.
         * Possible arguments passed to the placeholder are ignored.
         *
         * @param key                  the placeholder's key
         * @param type                 class of the context type
         * @param contextValueFunction a function that evaluates this placeholder's value
         * @param fallbackValue        the fallback value
         * @param <T>                  the context type
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull Placeholder.ArgumentIndependentContextualValueFunction<T> contextValueFunction, @NotNull String fallbackValue) {
            return this.add(Placeholder.contextual(key, type, contextValueFunction, fallbackValue));
        }

        /**
         * Adds a new {@link Placeholder}. If the {@link Context} is not {@code null} and of the given type,
         * {@code contextValueFunction} will be used to evaluate the value. Possible arguments passed to the placeholder are
         * ignored.
         *
         * @param key                  the placeholder's key
         * @param type                 class of the context type
         * @param contextValueFunction a function that evaluates this placeholder's value
         * @param <T>                  the context type
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder contextual(@NotNull String key, @NotNull Class<T> type, @NotNull Placeholder.ArgumentIndependentContextualValueFunction<T> contextValueFunction) {
            return this.add(Placeholder.contextual(key, type, contextValueFunction));
        }

        /**
         * Adds a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
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
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate, @NotNull Placeholder.ValueFunction fallbackValueFunction) {
            return this.add(Placeholder.conditional(key, type, predicate, fallbackValueFunction));
        }

        /**
         * Adds a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
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
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate, @NotNull String fallbackValue) {
            return this.add(Placeholder.conditional(key, type, predicate, fallbackValue));
        }

        /**
         * Adds a new {@link Placeholder}. If the predicate is {@code true} this placeholder will return the first
         * argument as its value, otherwise the second argument is returned.
         * Example format: {@code Hello <hasName:<name>:unknown user>!}
         *
         * @param key                   the placeholder's key
         * @param type                  class of the context type
         * @param predicate             takes in the current {@link Context} and is used to check whether the first or
         *                              second argument should be returned as this placeholder's value
         * @param <T>                   the context type
         * @return this {@link Builder}
         */
        default <T extends Context> @NotNull Builder conditional(@NotNull String key, @NotNull Class<T> type, @NotNull Predicate<@NotNull T> predicate) {
            return this.add(Placeholder.conditional(key, type, predicate));
        }

        /**
         * Adds a new {@link Placeholder}. If the supplier is {@code true} this placeholder will return the first
         * argument as its value, otherwise the second argument is returned.
         * Example format: {@code Hello <hasName:<name>:unknown user>!}
         *
         * @param key                   the placeholder's key
         * @param supplier              used to check whether the first or second argument should be returned as this
         *                              placeholder's value
         * @return this {@link Builder}
         */
        default @NotNull Builder conditional(@NotNull String key, @NotNull BooleanSupplier supplier) {
            return this.add(Placeholder.conditional(key, supplier));
        }
    }
}
