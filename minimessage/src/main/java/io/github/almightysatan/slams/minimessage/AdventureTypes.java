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

package io.github.almightysatan.slams.minimessage;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.InvalidTypeException;
import io.github.almightysatan.slams.Translation;
import io.github.almightysatan.slams.impl.Types;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

class AdventureTypes {

    static AdventureTranslation<Component> messageValue(TagResolver tagResolver, Object value) {
        String string = Types.checkString(value);
        return (context, tagResolver0) -> MiniMessage.miniMessage().deserialize(string, new ContextTagResolverAdapter(context, ContextTagResolver.of(tagResolver, tagResolver0)));
    }

    static <T, U extends AdventureTranslation<T>> AdventureTranslationArray<T, U> messageArrayValue(@Nullable Object input, @NotNull IntFunction<T[]> arrayFun, @NotNull Function<Object, U> callback) throws InvalidTypeException {
        Translation<?>[] values = Types.checkArray(input, callback);
        return new AdventureTranslationArray<T, U>() {
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
            public T @NotNull [] value(@Nullable Context context, @NotNull TagResolver tagResolver) {
                return Arrays.stream(values).map(Translation::value).toArray(arrayFun);
            }
        };
    }

    static <K, T, U extends AdventureTranslation<T>> AdventureTranslationMap<K, T, U> messageMapValue(@Nullable Object input, Class<K> keyClass, @NotNull Function<Object, U> callback) {
        Map<K, U> values = Types.checkMap(input, keyClass, callback);
        return new AdventureTranslationMap<K, T, U>() {
            @Override
            public @Nullable U get(K key) {
                return values.get(key);
            }

            @Override
            public int size() {
                return values.size();
            }

            @Override
            public @NotNull Map<K, T> value(@Nullable Context context, @NotNull TagResolver tagResolver) {
                Map<K, T> map = new HashMap<>();
                for(Map.Entry<K, U> value : values.entrySet())
                    map.put(value.getKey(), value.getValue().value(context, tagResolver));
                return Collections.unmodifiableMap(map);
            }
        };
    }
}
