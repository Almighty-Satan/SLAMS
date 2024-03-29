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

package io.github.almightysatan.slams.minimessage;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.Translation;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a translation of a message in a specific language in MiniMessage format.
 *
 * @param <T> the type of this translation
 */
public interface AdventureTranslation<T> extends Translation<T> {

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @param context the context
     * @param tagResolver a {@link TagResolver}
     * @return the value
     */
    @NotNull T value(@Nullable Context context, @NotNull TagResolver tagResolver);

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @param context the context
     * @param tagResolvers an array of {@link TagResolver TagResolvers}
     * @return the value
     */
    default @NotNull T value(@Nullable Context context, @NotNull TagResolver @NotNull ... tagResolvers) {
        return this.value(context, ContextTagResolver.of(tagResolvers));
    }

    /**
     * Replaces placeholders and returns the resulting value.
     *
     * @param tagResolvers an array of {@link TagResolver TagResolvers}
     * @return the value
     */
    default @NotNull T value(@NotNull TagResolver @NotNull ... tagResolvers) {
        return this.value(null, tagResolvers);
    }

    @Override
    default @NotNull T value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) {
        return this.value(context, ContextTagResolver.of(placeholderResolver));
    }
}
