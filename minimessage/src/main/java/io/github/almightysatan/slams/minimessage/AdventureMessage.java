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

public interface AdventureMessage extends AdventureGenericMessage<Component> {

    static @NotNull AdventureMessage of(@NotNull String path, @NotNull Slams slams, @NotNull TagResolver tagResolver) {
        class AdventureMessageImpl extends MessageImpl<Component, String, TagResolver> implements AdventureMessage {

            protected AdventureMessageImpl(@NotNull String path, @NotNull Slams languageManager, @NotNull TagResolver tagResolver) {
                super(path, languageManager, tagResolver);
            }

            @Override
            protected @NotNull String checkType(@Nullable Object value) throws InvalidTypeException {
                if (!(value instanceof String))
                    throw new InvalidTypeException();
                return (String) value;
            }

            @Override
            public @NotNull Component value(@Nullable Context context, @NotNull TagResolver tagResolver) {
                String value = this.rawValue(context);
                return MiniMessage.miniMessage().deserialize(value, new ContextTagResolverAdapter(context, ContextTagResolver.of(tagResolver, this.placeholderResolver())));
            }
        }

        return new AdventureMessageImpl(path, slams, tagResolver);
    }

    static @NotNull AdventureMessage of(@NotNull String path, @NotNull Slams slams) {
        return of(path, slams, TagResolver.empty());
    }
}
