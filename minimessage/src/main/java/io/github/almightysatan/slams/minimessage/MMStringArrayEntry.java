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
import io.github.almightysatan.slams.LanguageManager;
import io.github.almightysatan.slams.impl.LanguageEntryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface MMStringArrayEntry extends MMLanguageEntry<Component[]> {

    static @NotNull MMStringArrayEntry of(@NotNull String path, @NotNull LanguageManager languageManager, @NotNull TagResolver tagResolver) {
        class MMStringArrayEntryImpl extends LanguageEntryImpl<Component[], String[], TagResolver> implements MMStringArrayEntry {

            protected MMStringArrayEntryImpl(@NotNull String path, @NotNull LanguageManager languageManager, @NotNull TagResolver tagResolver) {
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

            @Override
            public @NotNull Component @NotNull [] value(@Nullable Context context, @NotNull TagResolver tagResolver) {
                Object value = this.rawValue(context);
                TagResolver adapter = new ContextTagResolverAdapter(context, ContextTagResolver.of(tagResolver, this.placeholderResolver()));
                return Arrays.stream((String[]) value).map(s -> MiniMessage.miniMessage().deserialize(s, adapter)).toArray(Component[]::new);
            }
        }

        return new MMStringArrayEntryImpl(path, languageManager, tagResolver);
    }

    static @NotNull MMStringArrayEntry of(@NotNull String path, @NotNull LanguageManager languageManager) {
        return of(path, languageManager, TagResolver.empty());
    }
}
