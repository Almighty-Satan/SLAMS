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

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.InvalidTypeException;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.impl.MessageImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface StandaloneMessageArray extends StandaloneGenericMessage<String[]> {

    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull Slams slams, @NotNull PlaceholderStyle style, @NotNull PlaceholderResolver placeholderResolver) {
        Objects.requireNonNull(style);
        class StandaloneMessageArrayImpl extends MessageImpl<String[], Component[], PlaceholderResolver> implements StandaloneMessageArray {

            protected StandaloneMessageArrayImpl(@NotNull String path, @NotNull Slams languageManager, @NotNull PlaceholderResolver placeholderResolver) {
                super(path, languageManager, placeholderResolver);
            }

            @Override
            protected @NotNull Component @NotNull [] checkType(@Nullable Object value) throws InvalidTypeException {
                if (value instanceof String[])
                    return Arrays.stream((String[]) value).map(element -> new CompositeComponent(style, element, this.placeholderResolver())).toArray(Component[]::new);
                if (value instanceof List) {
                    return ((List<?>) value).stream().map(element -> {
                        if (element instanceof String)
                            return new CompositeComponent(style, (String) element, this.placeholderResolver());
                        throw new InvalidTypeException();
                    }).toArray(Component[]::new);
                }
                throw new InvalidTypeException();
            }

            @Override
            public @NotNull String @NotNull [] value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) {
                Component[] components = this.rawValue(context);
                return Arrays.stream(components).map(component ->
                        component.value(context, PlaceholderResolver.of(placeholderResolver, this.placeholderResolver()))
                ).toArray(String[]::new);
            }
        }

        return new StandaloneMessageArrayImpl(path, slams, placeholderResolver);
    }

    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull Slams slams, @NotNull PlaceholderResolver placeholderResolver) {
        return of(path, slams, PlaceholderStyle.ANGLE_BRACKETS, placeholderResolver);
    }

    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull Slams slams) {
        return of(path, slams, PlaceholderStyle.ANGLE_BRACKETS, PlaceholderResolver.empty());
    }

    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull StandaloneSlams languageManager, @NotNull PlaceholderResolver placeholderResolver) {
        return of(path, languageManager, languageManager.style(), placeholderResolver);
    }

    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull StandaloneSlams languageManager) {
        return of(path, languageManager, languageManager.style(), PlaceholderResolver.empty());
    }
}
