/*
 * SLAMS - Simple Language And Message System
 * Copyright (C) 2023 Almighty-Satan, LeStegii
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
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.Translation;
import io.github.almightysatan.slams.impl.MessageImpl;
import io.github.almightysatan.slams.standalone.impl.StandaloneTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Message} in Standalone format. The value of this message is a String.
 */
public interface StandaloneMessage extends StandaloneGenericMessage<String> {

    /**
     * Creates a new {@link StandaloneMessage} with the given path, {@link PlaceholderStyle}, {@link Slams} and
     * {@link PlaceholderResolver}.
     *
     * @param path                the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams               the language manager (slams instance) to use
     * @param placeholderResolver the tag resolver
     * @return a new {@link StandaloneMessage}
     */
    static @NotNull StandaloneMessage of(@NotNull String path, @NotNull StandaloneSlams slams, @NotNull PlaceholderResolver placeholderResolver) {
        class StandaloneMessageImpl extends MessageImpl<String> implements StandaloneMessage {

            protected StandaloneMessageImpl() {
                super(path, slams);
            }

            @Override
            protected @NotNull Translation<String> toMessageValue(@NotNull Object value) {
                return StandaloneTypes.messageValue(slams, placeholderResolver, value);
            }
        }
        return new StandaloneMessageImpl();
    }

    /**
     * Creates a new {@link StandaloneMessage} with the given path, {@link PlaceholderStyle}, {@link StandaloneSlams}
     * and {@link PlaceholderResolver}. Uses {@link StandaloneSlams#style()}.
     *
     * @param path                the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams               the language manager (slams instance) to use
     * @return a new {@link StandaloneMessage}
     */
    static @NotNull StandaloneMessage of(@NotNull String path, @NotNull StandaloneSlams slams) {
        return of(path, slams, PlaceholderResolver.empty());
    }
}
