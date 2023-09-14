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
import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public interface ContextTagResolver extends TagResolver {

    @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException;

    @Override
    default @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx) throws ParsingException {
        return this.resolve(name, arguments, ctx, null);
    }

    @Override
    boolean has(@NotNull String name);

    // TODO this should return a singleton
    static @NotNull ContextTagResolver empty() {
        return new ContextTagResolver() {
            @Override
            public @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException {
                return null;
            }

            @Override
            public boolean has(@NotNull String name) {
                return false;
            }
        };
    }

    static @NotNull ContextTagResolver of(@NotNull TagResolver @NotNull ... tagResolvers) {
        Objects.requireNonNull(tagResolvers);
        if (tagResolvers.length == 0)
            return empty();
        return new ContextTagResolver() {
            @Override
            public @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException {
                for (TagResolver tagResolver : tagResolvers) {
                    Tag tag;
                    if (tagResolver instanceof ContextTagResolver)
                        tag = ((ContextTagResolver) tagResolver).resolve(name, arguments, ctx, context);
                    else
                        tag = tagResolver.resolve(name, arguments, ctx);

                    if (tag != null)
                        return tag;
                }
                return null;
            }

            @Override
            public boolean has(@NotNull String name) {
                for (TagResolver tagResolver : tagResolvers)
                    if (tagResolver.has(name))
                        return true;
                return false;
            }
        };
    }

    static @NotNull ContextTagResolver of(@NotNull ContextTagResolver @NotNull ... tagResolvers) {
        Objects.requireNonNull(tagResolvers);
        if (tagResolvers.length == 0)
            return empty();
        return new ContextTagResolver() {
            @Override
            public @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException {
                for (ContextTagResolver tagResolver : tagResolvers) {
                    Tag tag = tagResolver.resolve(name, arguments, ctx, context);

                    if (tag != null)
                        return tag;
                }
                return null;
            }

            @Override
            public boolean has(@NotNull String name) {
                for (ContextTagResolver tagResolver : tagResolvers)
                    if (tagResolver.has(name))
                        return true;
                return false;
            }
        };
    }

    static @NotNull ContextTagResolver of(@NotNull List<@NotNull TagResolver> tagResolvers) {
        return of(tagResolvers.toArray(new TagResolver[0]));
    }

    static @NotNull ContextTagResolver of(@NotNull PlaceholderResolver placeholderResolver) {
        Objects.requireNonNull(placeholderResolver);
        return new ContextTagResolver() {

            @Override
            public @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException {
                Placeholder placeholder = placeholderResolver.resolve(name);
                return placeholder == null ? null : Tag.selfClosingInserting(Component.text(placeholder.value(context, this.argumentQueueToList(arguments))));
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

    // TODO add builder
}
