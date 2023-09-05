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
import io.github.almightysatan.slams.LanguageEntry;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface MMLanguageEntry<T> extends LanguageEntry {

    @NotNull T value(@Nullable Context context, @NotNull ContextTagResolver<Context> tagResolver);

    default @NotNull T value(@Nullable Context context) {
        return this.value(context, ContextTagResolver.empty());
    }

    default @NotNull T value(@Nullable Context context, @NotNull TagResolver... tagResolvers) {
        Objects.requireNonNull(tagResolvers);
        return this.value(context, ContextTagResolver.of(TagResolver.resolver(tagResolvers)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default @NotNull T value(@Nullable Context context, @NotNull ContextTagResolver<?>... tagResolvers) {
        Objects.requireNonNull(tagResolvers);
        return this.value(context, (ContextTagResolver<Context>) ContextTagResolver.of((ContextTagResolver[]) tagResolvers));
    }
}
