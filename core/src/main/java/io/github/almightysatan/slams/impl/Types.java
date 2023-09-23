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

package io.github.almightysatan.slams.impl;

import io.github.almightysatan.slams.InvalidTypeException;
import io.github.almightysatan.slams.MessageValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class Types {

    static final Map<Class<?>, Function<?, ?>> SIMPLE_TYPES = new HashMap<>();

    public static String checkString(@Nullable Object input) throws InvalidTypeException {
        if (!(input instanceof String))
            throw new InvalidTypeException();
        return (String) input;
    }

    public static <T, U extends MessageValue<T>> MessageValue<?>[] checkArray(@Nullable Object input, @NotNull Function<Object, U> callback) throws InvalidTypeException {
        if (input instanceof Object[])
            return Arrays.stream((Object[]) input).map(callback).toArray(MessageValue[]::new);
        else if (input instanceof Collection)
            return ((Collection<?>) input).stream().map(callback).toArray(MessageValue[]::new);
        else
            throw new InvalidTypeException();
    }

    public static <K, T, U extends MessageValue<T>> Map<K, U> checkMap(@Nullable Object input, Class<K> keyClass, @NotNull Function<Object, U> callback) {
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
