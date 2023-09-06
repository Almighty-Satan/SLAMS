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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StandaloneStringEntry extends StandaloneLanguageEntry<String> {

    static @NotNull StandaloneStringEntry of(@NotNull String path, @NotNull LanguageManager languageManager, @Nullable PlaceholderResolver placeholderResolver) {
        class StandaloneStringEntryImpl extends AbstractStandaloneLanguageEntry<String, Component> implements StandaloneStringEntry {

            protected StandaloneStringEntryImpl(@NotNull String path, @NotNull LanguageManager languageManager, @Nullable PlaceholderResolver placeholderResolver) {
                super(path, languageManager, placeholderResolver);
            }

            @Override
            protected @NotNull Component checkType(@Nullable Object value) throws InvalidTypeException {
                if (!(value instanceof String))
                    throw new InvalidTypeException();
                return new CompositeComponent((String) value);
            }

            @Override
            public @NotNull String value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) {
                Component component = this.rawValue(context);

                if (this.placeholderResolver == null)
                    return component.value(context, placeholderResolver);
                else
                    return component.value(context, PlaceholderResolver.of(this.placeholderResolver, placeholderResolver));
            }
        }

        return new StandaloneStringEntryImpl(path, languageManager, placeholderResolver);
    }
}
