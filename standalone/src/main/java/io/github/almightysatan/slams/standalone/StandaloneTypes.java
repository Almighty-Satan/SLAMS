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

package io.github.almightysatan.slams.standalone;

import io.github.almightysatan.slams.*;
import io.github.almightysatan.slams.impl.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

class StandaloneTypes {

    static MessageValue<String> messageValue(PlaceholderStyle style, PlaceholderResolver placeholderResolver, Object input) {
        Component component = new CompositeComponent(style, Types.checkString(input), placeholderResolver);
        return component::value;
    }

    static <T, U extends MessageValue<T>> MessageArrayValue<T, U> messageArrayValue(@Nullable Object input, @NotNull IntFunction<T[]> arrayFun, @NotNull Function<Object, U> callback) throws InvalidTypeException {
        MessageValue<?>[] values = Types.checkArray(input, callback);
        return new MessageArrayValue<T, U>() {
            @SuppressWarnings("unchecked")
            @Override
            public U get(int index) {
                return (U) values[index];
            }

            @Override
            public int size() {
                return values.length;
            }

            @Override
            public T @NotNull [] value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) {
                return Arrays.stream(values).map(MessageValue::value).toArray(arrayFun);
            }
        };
    }

    static <K, T, U extends MessageValue<T>> MessageValue<Map<K, T>> messageMapValue(@Nullable Object input, Class<K> keyClass, @NotNull Function<Object, U> callback) {
        Map<K, U> values = Types.checkMap(input, keyClass, callback);
        return new MessageValue<Map<K, T>>() {
            @Override
            public @NotNull Map<K, T> value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) {
                Map<K, T> map = new HashMap<>();
                for(Map.Entry<K, U> value : values.entrySet())
                    map.put(value.getKey(), value.getValue().value(context, placeholderResolver));
                return Collections.unmodifiableMap(map);
            }
        };
    }
}
