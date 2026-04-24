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

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@ApiStatus.Internal
class ContextTagResolverAdapter implements TagResolver {

    private final TagResolver tagResolver;
    private final Object[] contexts;

    ContextTagResolverAdapter(@NotNull TagResolver tagResolver, @NotNull Object @NotNull [] contexts) {
        this.contexts = contexts;
        this.tagResolver = Objects.requireNonNull(tagResolver);
    }

    @Override
    public @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if (tagResolver instanceof ContextTagResolver)
            return ((ContextTagResolver) tagResolver).resolve(name, arguments, ctx, this.contexts);
        else
            return tagResolver.resolve(name, arguments, ctx);
    }

    @Override
    public boolean has(@NotNull String name) {
        return this.tagResolver.has(name);
    }
}
