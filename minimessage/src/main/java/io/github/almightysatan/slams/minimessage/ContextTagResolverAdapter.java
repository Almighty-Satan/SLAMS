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
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class ContextTagResolverAdapter implements TagResolver {

    private final Context context;
    private final TagResolver tagResolver;

    ContextTagResolverAdapter(@Nullable Context context, @NotNull TagResolver tagResolver) {
        this.context = context;
        this.tagResolver = Objects.requireNonNull(tagResolver);
    }

    @Override
    public @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx) throws ParsingException {
        if (tagResolver instanceof ContextTagResolver)
            return ((ContextTagResolver) tagResolver).resolve(name, arguments, ctx, this.context);
        else
            return tagResolver.resolve(name, arguments, ctx);
    }

    @Override
    public boolean has(@NotNull String name) {
        return this.tagResolver.has(name);
    }
}
