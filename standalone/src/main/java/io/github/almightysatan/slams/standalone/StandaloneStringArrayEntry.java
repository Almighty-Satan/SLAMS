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
import io.github.almightysatan.slams.LanguageManager;
import io.github.almightysatan.slams.PlaceholderResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface StandaloneStringArrayEntry extends StandaloneLanguageEntry<String[]> {

    static @NotNull StandaloneStringArrayEntry of(@NotNull String path, @NotNull LanguageManager languageManager, @NotNull PlaceholderResolver placeholderResolver) {
        class StandaloneStringArrayEntryImpl extends AbstractStandaloneLanguageEntry<String[], Component[]> implements StandaloneStringArrayEntry {

            protected StandaloneStringArrayEntryImpl(@NotNull String path, @NotNull LanguageManager languageManager, @NotNull PlaceholderResolver placeholderResolver) {
                super(path, languageManager, placeholderResolver);
            }

            @Override
            protected @NotNull Component @NotNull [] checkType(@Nullable Object value) throws InvalidTypeException {
                if (value instanceof String[])
                    return Arrays.stream((String[]) value).map(CompositeComponent::new).toArray(Component[]::new);
                if (value instanceof List) {
                    return ((List<?>) value).stream().map(element -> {
                        if (element instanceof String)
                            return new CompositeComponent((String) element);
                        throw new InvalidTypeException();
                    }).toArray(Component[]::new);
                }
                throw new InvalidTypeException();
            }

            @Override
            public @NotNull String @NotNull [] value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) {
                Component[] components = this.rawValue(context);
                return Arrays.stream(components).map(component -> {
                    if (this.placeholderResolver == null)
                        return component.value(context, placeholderResolver);
                    else
                        return component.value(context, PlaceholderResolver.of(this.placeholderResolver, placeholderResolver));
                }).toArray(String[]::new);
            }
        }

        return new StandaloneStringArrayEntryImpl(path, languageManager, placeholderResolver);
    }

    static @NotNull StandaloneStringArrayEntry of(@NotNull String path, @NotNull LanguageManager languageManager) {
        return of(path, languageManager, PlaceholderResolver.empty());
    }
}
