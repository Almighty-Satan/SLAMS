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

import io.github.almightysatan.slams.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.IntFunction;

@ApiStatus.Internal
public class Types {

    static final Map<Class<?>, Function<?, ?>> SIMPLE_TYPES = new HashMap<>();

    public static String checkString(@Nullable Object input) throws InvalidTypeException {
        if (!(input instanceof String))
            throw new InvalidTypeException();
        return (String) input;
    }

    public static <T, U extends Translation<T>> Translation<?>[] checkArray(@Nullable Object input,
            @NotNull Function<Object, U> callback) throws InvalidTypeException {
        if (input instanceof Object[])
            return Arrays.stream((Object[]) input).map(callback).toArray(Translation[]::new);
        else if (input instanceof Collection)
            return ((Collection<?>) input).stream().map(callback).toArray(Translation[]::new);
        else
            throw new InvalidTypeException();
    }

    public static <K, T, U extends Translation<T>> Map<K, U> checkMap(@Nullable Object input,
            Class<K> keyClass, @NotNull Function<Object, U> callback) throws InvalidTypeException {
        if (!(input instanceof Map))
            throw new InvalidTypeException();
        @SuppressWarnings("unchecked")
        Function<Object, K> keyFunction = (Function<Object, K>) SIMPLE_TYPES.get(keyClass);
        if (keyFunction == null)
            throw new IllegalArgumentException("Unknown key type: " + keyClass);
        Map<K, U> values = new HashMap<>();
        for (Entry<?, ?> entry : ((Map<?, ?>) input).entrySet())
            values.put(keyFunction.apply(entry.getKey()), callback.apply(entry.getValue()));
        return values;
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends Translation<T>> TranslationArray<T, U> messageArrayValue(@Nullable Object input, @NotNull IntFunction<T[]> arrayFun, @NotNull Function<Object, U> callback) throws InvalidTypeException {
        Translation<T>[] values = (Translation<T>[]) Types.checkArray(input, callback);
        return new TranslationArray<T, U>() {
            @SuppressWarnings("unchecked")
            @Override
            public @NotNull U get(int index) {
                return (U) values[index];
            }

            @Override
            public int size() {
                return values.length;
            }

            @Override
            public @NotNull T @NotNull [] value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull ... contexts) {
                return Arrays.stream(values).map(translation -> translation.value(placeholderResolver, contexts)).toArray(arrayFun);
            }
        };
    }

    public static <K, T, U extends Translation<T>> TranslationMap<K, T, U> messageMapValue(@Nullable Object input, Class<K> keyClass, @NotNull Function<Object, U> callback) throws InvalidTypeException {
        Map<K, U> values = Types.checkMap(input, keyClass, callback);
        return new TranslationMap<K, T, U>() {
            @Override
            public @Nullable U get(K key) {
                return values.get(key);
            }

            @Override
            public int size() {
                return values.size();
            }

            @Override
            public @NotNull Map<K, T> value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull ... contexts) {
                Map<K, T> map = new HashMap<>();
                for (Map.Entry<K, U> value : values.entrySet())
                    map.put(value.getKey(), value.getValue().value(placeholderResolver, contexts));
                return Collections.unmodifiableMap(map);
            }
        };
    }

    static {
        SIMPLE_TYPES.put(Boolean.class, value -> {
            if (value instanceof Boolean)
                return value;
            if (value instanceof String)
                return Boolean.valueOf((String) value);
            throw new InvalidTypeException();
        });
        SIMPLE_TYPES.put(Double.class, value -> {
            if (value instanceof Double)
                return value;
            if (value instanceof Float)
                return (double) (float) value;
            if (value instanceof Integer)
                return (double) (int) value;
            if (value instanceof Long)
                return (double) (long) value;
            if (value instanceof String)
                try {
                    return Double.valueOf((String) value);
                } catch (NumberFormatException e) {
                    throw new InvalidTypeException();
                }
            if (value instanceof BigDecimal)
                return ((BigDecimal) value).doubleValue();
            throw new InvalidTypeException();
        });
        SIMPLE_TYPES.put(Float.class, value -> {
            if (value instanceof Float)
                return value;
            if (value instanceof Double)
                return (float) (double) value;
            if (value instanceof Integer)
                return (float) (int) value;
            if (value instanceof Long)
                return (float) (long) value;
            if (value instanceof String)
                try {
                    return Float.valueOf((String) value);
                } catch (NumberFormatException e) {
                    throw new InvalidTypeException();
                }
            if (value instanceof BigDecimal)
                return ((BigDecimal) value).floatValue();
            throw new InvalidTypeException();
        });
        SIMPLE_TYPES.put(Integer.class, value -> {
            if (value instanceof Integer)
                return value;
            if (value instanceof Long)
                return (int) (long) value;
            if (value instanceof String)
                try {
                    return Integer.valueOf((String) value);
                } catch (NumberFormatException e) {
                    throw new InvalidTypeException();
                }
            if (value instanceof BigInteger)
                return ((BigInteger) value).intValue();
            throw new InvalidTypeException();
        });
        SIMPLE_TYPES.put(Long.class, value -> {
            if (value instanceof Long)
                return value;
            if (value instanceof Integer)
                return (long) (int) value;
            if (value instanceof String)
                try {
                    return Long.valueOf((String) value);
                } catch (NumberFormatException e) {
                    throw new InvalidTypeException();
                }
            if (value instanceof BigInteger)
                return ((BigInteger) value).longValue();
            throw new InvalidTypeException();
        });
        SIMPLE_TYPES.put(String.class, value -> {
            if (value instanceof String)
                return value;
            throw new InvalidTypeException();
        });
    }
}
