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
import io.github.almightysatan.slams.impl.MessageImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a {@link Message} in Standalone format. The value of this message is an array of Strings.
 */
public interface StandaloneMessageArray extends Message<String[]> {

    /**
     * Creates a new {@link StandaloneMessageArray} with the given path, {@link PlaceholderStyle}, {@link Slams} and
     * {@link PlaceholderResolver}.
     *
     * @param path                the path of the entry
     * @param slams               the language manager (slams instance) to use
     * @param style               the {@link PlaceholderStyle}
     * @param placeholderResolver the tag resolver
     * @return a new {@link StandaloneMessageArray}
     */
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

    /**
     * Creates a new {@link StandaloneMessageArray} with the given path, {@link Slams} and {@link PlaceholderResolver}.
     * Uses {@link PlaceholderStyle#ANGLE_BRACKETS}.
     *
     * @param path                the path of the entry
     * @param slams               the language manager (slams instance) to use
     * @param placeholderResolver the tag resolver
     * @return a new {@link StandaloneMessageArray}
     */
    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull Slams slams, @NotNull PlaceholderResolver placeholderResolver) {
        return of(path, slams, PlaceholderStyle.ANGLE_BRACKETS, placeholderResolver);
    }

    /**
     * Creates a new {@link StandaloneMessageArray} with the given path. Uses {@link PlaceholderStyle#ANGLE_BRACKETS}.
     *
     * @param path                the path of the entry
     * @param slams               the language manager (slams instance) to use
     * @return a new {@link StandaloneMessageArray}
     */
    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull Slams slams) {
        return of(path, slams, PlaceholderStyle.ANGLE_BRACKETS, PlaceholderResolver.empty());
    }

    /**
     * Creates a new {@link StandaloneMessageArray} with the given path, {@link PlaceholderStyle}, {@link StandaloneSlams}
     * and {@link PlaceholderResolver}.  Uses {@link StandaloneSlams#style()}.
     *
     * @param path                the path of the entry
     * @param slams               the language manager (slams instance) to use
     * @param placeholderResolver the tag resolver
     * @return a new {@link StandaloneMessageArray}
     */
    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull StandaloneSlams slams, @NotNull PlaceholderResolver placeholderResolver) {
        return of(path, slams, slams.style(), placeholderResolver);
    }

    /**
     * Creates a new {@link StandaloneMessageArray} with the given path, {@link PlaceholderStyle}, {@link StandaloneSlams}
     * and {@link PlaceholderResolver}. Uses {@link StandaloneSlams#style()}.
     *
     * @param path                the path of the entry
     * @param slams               the language manager (slams instance) to use
     * @return a new {@link StandaloneMessageArray}
     */
    static @NotNull StandaloneMessageArray of(@NotNull String path, @NotNull StandaloneSlams slams) {
        return of(path, slams, slams.style(), PlaceholderResolver.empty());
    }
}
