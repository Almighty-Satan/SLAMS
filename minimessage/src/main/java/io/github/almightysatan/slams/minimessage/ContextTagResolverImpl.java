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
import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class ContextTagResolverImpl {

    static @NotNull ContextTagResolver ofPlaceholderResolver(@NotNull PlaceholderResolver placeholderResolver, boolean eval) {
        Objects.requireNonNull(placeholderResolver);
        return new ContextTagResolver() {

            @Override
            public @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException {
                Placeholder placeholder = placeholderResolver.resolve(name);
                if (placeholder == null)
                    return null;
                String value = placeholder.value(context, this.argumentQueueToList(arguments));
                return Tag.selfClosingInserting(eval ? ctx.deserialize(value) : Component.text(value));
            }

            @Override
            public boolean has(@NotNull String name) {
                return placeholderResolver.resolve(name) != null;
            }

            private List<String> argumentQueueToList(ArgumentQueue argumentQueue) {
                if (!argumentQueue.hasNext())
                    return Collections.emptyList();

                List<String> argumentList = new ArrayList<>();
                while (argumentQueue.hasNext())
                    argumentList.add(argumentQueue.pop().value());
                return Collections.unmodifiableList(argumentList);
            }
        };
    }
}
