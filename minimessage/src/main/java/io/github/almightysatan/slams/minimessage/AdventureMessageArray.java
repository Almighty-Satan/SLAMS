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
import io.github.almightysatan.slams.MessageValue;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.impl.MessageImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an array of messages parsed in MiniMessage format.
 * They can be used to get a {@link Component} array.
 */
public interface AdventureMessageArray extends AdventureGenericMessage<Component[]> {

    @Override
    @NotNull AdventureMessageArrayValue<Component, AdventureMessageValue<Component>> get(@Nullable Context context);

    /**
     * Creates a new {@link AdventureMessageArray} with the given path, {@link Slams} and {@link TagResolver}.
     *
     * @param path        the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams       the language manager (slams instance) to use
     * @param tagResolver the tag resolver
     * @return a new {@link AdventureMessageArray}
     */
    static @NotNull AdventureMessageArray of(@NotNull String path, @NotNull Slams slams, @NotNull TagResolver tagResolver) {
        Objects.requireNonNull(tagResolver);
        class AdventureMessageArrayImpl extends MessageImpl<Component[]> implements AdventureMessageArray {

            protected AdventureMessageArrayImpl() {
                super(path, slams);
            }

            @Override
            protected @NotNull MessageValue<Component[]> toMessageValue(Object value) {
                return AdventureTypes.messageArrayValue(value, Component[]::new, element -> AdventureTypes.messageValue(tagResolver, element));
            }

            @Override
            public @NotNull AdventureMessageArrayValue<Component, AdventureMessageValue<Component>> get(@Nullable Context context) {
                return (AdventureMessageArrayValue<Component, AdventureMessageValue<Component>>) super.get(context);
            }
        }
        return new AdventureMessageArrayImpl();
    }

    /**
     * Creates a new {@link AdventureMessageArray} with the given path.
     *
     * @param path        the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams       the language manager (slams instance) to use
     * @return a new {@link AdventureMessageArray}
     */
    static @NotNull AdventureMessageArray of(@NotNull String path, @NotNull Slams slams) {
        return of(path, slams, TagResolver.empty());
    }
}
