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

import io.github.almightysatan.slams.Message;
import io.github.almightysatan.slams.MessageValue;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.impl.MessageImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a {@link Message} in Standalone format. The value of this message is a {@link Map}. The Map's value is
 * always a String, the key can be any of the following types:
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
public interface StandaloneMessageMap<K> extends StandaloneGenericMessage<Map<K, String>> {

    /**
     * Creates a new {@link StandaloneMessageMap} with the given path, {@link PlaceholderStyle}, {@link Slams} and
     * {@link PlaceholderResolver}.
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
     * @param path                the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams               the language manager (slams instance) to use
     * @param keyType             the class of the key's type
     * @param style               the {@link PlaceholderStyle}
     * @param placeholderResolver the tag resolver
     * @param <K>                 the type of the key
     * @return a new {@link StandaloneMessageMap}
     */
    static <K> @NotNull StandaloneMessageMap<K> of(@NotNull String path, @NotNull Slams slams, @NotNull Class<K> keyType, @NotNull PlaceholderStyle style, @NotNull PlaceholderResolver placeholderResolver) {
        Objects.requireNonNull(style);
        class StandaloneMessageMapImpl extends MessageImpl<Map<K, String>> implements StandaloneMessageMap<K> {

            protected StandaloneMessageMapImpl() {
                super(path, slams);
            }

            @Override
            protected @NotNull MessageValue<Map<K, String>> toMessageValue(Object value) {
                return StandaloneTypes.messageMapValue(value, keyType, element -> StandaloneTypes.messageValue(style, placeholderResolver, element));
            }
        }

        return new StandaloneMessageMapImpl();
    }

    /**
     * Creates a new {@link StandaloneMessageMap} with the given path, {@link Slams} and {@link PlaceholderResolver}.
     * Uses {@link PlaceholderStyle#ANGLE_BRACKETS}.
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
     * @param path                the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams               the language manager (slams instance) to use
     * @param keyType             the class of the key's type
     * @param placeholderResolver the tag resolver
     * @param <K>                 the type of the key
     * @return a new {@link StandaloneMessageMap}
     */
    static <K> @NotNull StandaloneMessageMap<K> of(@NotNull String path, @NotNull Slams slams, @NotNull Class<K> keyType, @NotNull PlaceholderResolver placeholderResolver) {
        return of(path, slams, keyType, PlaceholderStyle.ANGLE_BRACKETS, placeholderResolver);
    }

    /**
     * Creates a new {@link StandaloneMessageMap} with the given path. Uses {@link PlaceholderStyle#ANGLE_BRACKETS}.
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
     * @param path    the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams   the language manager (slams instance) to use
     * @param keyType the class of the key's type
     * @param <K>     the type of the key
     * @return a new {@link StandaloneMessageMap}
     */
    static <K> @NotNull StandaloneMessageMap<K> of(@NotNull String path, @NotNull Slams slams, @NotNull Class<K> keyType) {
        return of(path, slams, keyType, PlaceholderStyle.ANGLE_BRACKETS, PlaceholderResolver.empty());
    }

    /**
     * Creates a new {@link StandaloneMessageMap} with the given path, {@link PlaceholderStyle}, {@link StandaloneSlams}
     * and {@link PlaceholderResolver}.  Uses {@link StandaloneSlams#style()}.
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
     * @param path                the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams               the language manager (slams instance) to use
     * @param keyType             the class of the key's type
     * @param placeholderResolver the tag resolver
     * @param <K>                 the type of the key
     * @return a new {@link StandaloneMessageMap}
     */
    static <K> @NotNull StandaloneMessageMap<K> of(@NotNull String path, @NotNull StandaloneSlams slams, @NotNull Class<K> keyType, @NotNull PlaceholderResolver placeholderResolver) {
        return of(path, slams, keyType, slams.style(), placeholderResolver);
    }

    /**
     * Creates a new {@link StandaloneMessageMap} with the given path, {@link PlaceholderStyle}, {@link StandaloneSlams}
     * and {@link PlaceholderResolver}. Uses {@link StandaloneSlams#style()}.
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
     * @param path    the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams   the language manager (slams instance) to use
     * @param keyType the class of the key's type
     * @param <K>     the type of the key
     * @return a new {@link StandaloneMessageMap}
     */
    static <K> @NotNull StandaloneMessageMap<K> of(@NotNull String path, @NotNull StandaloneSlams slams, @NotNull Class<K> keyType) {
        return of(path, slams, keyType, slams.style(), PlaceholderResolver.empty());
    }
}
