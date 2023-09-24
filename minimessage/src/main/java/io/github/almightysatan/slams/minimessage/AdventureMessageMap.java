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
import io.github.almightysatan.slams.Message;
import io.github.almightysatan.slams.Translation;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.impl.MessageImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a {@link Message} in MiniMessage format. The value of this message is a {@link Map}. The Map's value is
 * always a {@link Component}, the key can be any of the following types:
 * <ul>
 *     <li>Boolean</li>
 *     <li>Double</li>
 *     <li>Float</li>
 *     <li>Integer</li>
 *     <li>Long</li>
 *     <li>String</li>
 * </ul>
 *
 * @param <K> the type of the Map's key
 */
public interface AdventureMessageMap<K> extends AdventureGenericMessage<Map<K, Component>> {

    /**
     * Creates a new {@link AdventureMessageMap} with the given path, {@link Slams} and {@link TagResolver}.
     * <p>Possible key types:</p>
     * <ul>
     *     <li>Boolean</li>
     *     <li>Double</li>
     *     <li>Float</li>
     *     <li>Integer</li>
     *     <li>Long</li>
     *     <li>String</li>
     * </ul>
     *
     * @param path        the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams       the language manager (slams instance) to use
     * @param keyType     the class of the key's type
     * @param tagResolver the tag resolver
     * @param <K>         the type of the key
     * @return a new {@link AdventureMessageMap}
     */
    static <K> @NotNull AdventureMessageMap<K> of(@NotNull String path, @NotNull Slams slams, Class<K> keyType, @NotNull TagResolver tagResolver) {
        Objects.requireNonNull(tagResolver);
        class AdventureMessageMapImpl extends MessageImpl<Map<K, Component>> implements AdventureMessageMap<K> {

            protected AdventureMessageMapImpl() {
                super(path, slams);
            }

            @Override
            protected @NotNull Translation<Map<K, Component>> toMessageValue(@NotNull Object value) {
                return AdventureTypes.messageMapValue(value, keyType, element -> AdventureTypes.messageValue(tagResolver, element));
            }

            @Override
            public @NotNull AdventureTranslation<Map<K, Component>> translate(@Nullable Context context) {
                return (AdventureTranslation<Map<K, Component>>) super.translate(context);
            }
        }
        return new AdventureMessageMapImpl();
    }

    /**
     * Creates a new {@link AdventureMessageMap} with the given path.
     * <ul>
     *     <li>Boolean</li>
     *     <li>Double</li>
     *     <li>Float</li>
     *     <li>Integer</li>
     *     <li>Long</li>
     *     <li>String</li>
     * </ul>
     *
     * @param path    the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams   the language manager (slams instance) to use
     * @param keyType the class of the key's type
     * @param <K>     the type of the key
     * @return a new {@link AdventureMessageMap}
     */
    static <K> @NotNull AdventureMessageMap<K> of(@NotNull String path, @NotNull Slams slams, @NotNull Class<K> keyType) {
        return of(path, slams, keyType, TagResolver.empty());
    }
}
