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

package io.github.almightysatan.slams.minimessage;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.InvalidTypeException;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.impl.MessageImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an array of messages parsed in MiniMessage format.
 * They can be used to get a {@link Component} array.
 */
public interface AdventureMessageArray extends AdventureGenericMessage<Component[]> {

    /**
     * Creates a new {@link MMStringArrayEntry} with the given path, {@link LanguageManager} and {@link ContextTagResolver}.
     *
     * @param path            the path of the entry
     * @param languageManager the language manager
     * @param tagResolver     the tag resolver
     * @return the created entry
     */
    static @NotNull AdventureMessageArray of(@NotNull String path, @NotNull Slams slams, @NotNull TagResolver tagResolver) {
        class AdventureMessageArrayImpl extends MessageImpl<Component[], String[], TagResolver> implements AdventureMessageArray {

            protected AdventureMessageArrayImpl(@NotNull String path, @NotNull Slams languageManager, @NotNull TagResolver tagResolver) {
                super(path, languageManager, tagResolver);
            }

            @Override
            protected String @NotNull [] checkType(@Nullable Object value) throws InvalidTypeException {
                if (value instanceof String[])
                    return (String[]) value;
                if (value instanceof List) {
                    return ((List<?>) value).stream().map(element -> {
                        if (element instanceof String)
                            return (String) element;
                        throw new InvalidTypeException();
                    }).toArray(String[]::new);
                }
                throw new InvalidTypeException();
            }

            /**
             * Returns the value of this entry as a {@link Component} array.
             * @param context       the context
             * @param tagResolver   the tag resolver
             * @return the value of this entry as a {@link Component} array
             */
            @Override
            public @NotNull Component @NotNull [] value(@Nullable Context context, @NotNull TagResolver tagResolver) {
                Object value = this.rawValue(context);
                TagResolver adapter = new ContextTagResolverAdapter(context, ContextTagResolver.of(tagResolver, this.placeholderResolver()));
                return Arrays.stream((String[]) value).map(s -> MiniMessage.miniMessage().deserialize(s, adapter)).toArray(Component[]::new);
            }
        }

        return new AdventureMessageArrayImpl(path, slams, tagResolver);
    }

    static @NotNull AdventureMessageArray of(@NotNull String path, @NotNull Slams slams) {
        return of(path, slams, TagResolver.empty());
    }
}
