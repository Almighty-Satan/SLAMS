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
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An extension of MiniMessage's {@link TagResolver} which supports context-dependent Tags.
 */
public interface ContextTagResolver extends TagResolver {

    /**
     * A {@link ContextTagResolver} that does not resolve any tags.
     */
    ContextTagResolver EMPTY = new ContextTagResolver() {
        @Override
        public @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException {
            return null;
        }

        @Override
        public boolean has(@NotNull String name) {
            return false;
        }
    };

    /**
     * Gets a tag from this {@link ContextTagResolver} based on the given {@link Context}. The context may be null.
     *
     * @param name      the name of the tag
     * @param arguments the arguments
     * @param ctx       the MiniMessage {@link net.kyori.adventure.text.minimessage.Context}
     * @param context   the Slams {@link Context}
     * @return a tag
     * @throws ParsingException if the arguments provided are invalid
     */
    @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException;

    @Override
    default @Nullable Tag resolve(@TagPattern @NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx) throws ParsingException {
        return this.resolve(name, arguments, ctx, null);
    }

    @Override
    boolean has(@NotNull String name);

    /**
     * Returns a {@link ContextTagResolver} that does not resolve any tags.
     *
     * @return a {@link ContextTagResolver}
     */
    static @NotNull ContextTagResolver empty() {
        return EMPTY;
    }

    /**
     * Returns a {@link ContextTagResolver} that will use the given {@link TagResolver TagResolvers} to find a
     * {@link Tag}. It can therefore resolve any {@link Tag} resolved by at least one of the given
     * {@link TagResolver TagResolvers}.
     *
     * @param tagResolvers an array of {@link TagResolver TagResolvers}
     * @return a new {@link ContextTagResolver}
     */
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

    /**
     * Returns a {@link ContextTagResolver} that will use the given {@link ContextTagResolver ContextTagResolvers} to
     * find a {@link Tag}. It can therefore resolve any {@link Tag} resolved by at least one of the given
     * {@link ContextTagResolver ContextTagResolvers}.
     *
     * @param tagResolvers an array of {@link ContextTagResolver ContextTagResolvers}
     * @return a new {@link ContextTagResolver}
     */
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


    /**
     * Returns a {@link ContextTagResolver} that will use the given {@link TagResolver TagResolvers} to find a
     * {@link Tag}. It can therefore resolve any {@link Tag} resolved by at least one of the given
     * {@link TagResolver TagResolvers}.
     *
     * @param tagResolvers a {@link List} of {@link TagResolver TagResolvers}
     * @return a new {@link ContextTagResolver}
     */
    @Deprecated
    static @NotNull ContextTagResolver of(@NotNull List<@NotNull TagResolver> tagResolvers) {
        return of(tagResolvers.toArray(new TagResolver[0]));
    }

    /**
     * Creates a new {@link ContextTagResolver} from the given {@link PlaceholderResolver}.
     *
     * @param placeholderResolver the {@link PlaceholderResolver}
     * @return a new {@link ContextTagResolver}
     */
    static @NotNull ContextTagResolver of(@NotNull PlaceholderResolver placeholderResolver) {
        return ContextTagResolverImpl.ofPlaceholderResolver(placeholderResolver, false);
    }

    /**
     * Creates a new {@link ContextTagResolver} from the given {@link PlaceholderResolver}. All tags returned by this
     * {@link ContextTagResolver} will be pre-process tags and their value will therefore be parsed by MiniMessage. Do
     * not use this method for placeholders that return user input!
     *
     * @param placeholderResolver the {@link PlaceholderResolver}
     * @return a new {@link ContextTagResolver}
     */
    static @NotNull ContextTagResolver ofUnsafe(@NotNull PlaceholderResolver placeholderResolver) {
        return ContextTagResolverImpl.ofPlaceholderResolver(placeholderResolver, true);
    }

    /**
     * Returns a new {@link Builder}.
     *
     * @return a new {@link Builder}
     */
    static @NotNull Builder builder() {
        List<ContextTagResolver> contextTagResolvers = new ArrayList<>();

        return new Builder() {
            @Override
            public @NotNull ContextTagResolver build() {
                return of(contextTagResolvers.toArray(new ContextTagResolver[0]));
            }

            @Override
            public @NotNull Builder add(@NotNull ContextTagResolver contextTagResolver) {
                Objects.requireNonNull(contextTagResolver);
                contextTagResolvers.add(contextTagResolver);
                return this;
            }

            @Override
            public @NotNull Builder add(@NotNull TagResolver tagResolver) {
                Objects.requireNonNull(tagResolver);
                if (tagResolver instanceof ContextTagResolver)
                    return this.add((ContextTagResolver) tagResolver);
                return this.add(new ContextTagResolver() {
                    @Override
                    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, net.kyori.adventure.text.minimessage.@NotNull Context ctx, @Nullable Context context) throws ParsingException {
                        return tagResolver.resolve(name, arguments, ctx);
                    }

                    @Override
                    public boolean has(@NotNull String name) {
                        return tagResolver.has(name);
                    }
                });
            }

            @Override
            public @NotNull Builder add(@NotNull PlaceholderResolver placeholderResolver) {
                Objects.requireNonNull(placeholderResolver);
                return this.add(of(placeholderResolver));
            }

            @Override
            public @NotNull Builder addUnsafe(@NotNull PlaceholderResolver placeholderResolver) {
                Objects.requireNonNull(placeholderResolver);
                return this.add(ofUnsafe(placeholderResolver));
            }
        };
    }

    /**
     * Can be used to build a {@link ContextTagResolver}.
     */
    static interface Builder {

        /**
         * Creates a new {@link ContextTagResolver} from this {@link Builder}.
         *
         * @return a new {@link ContextTagResolver}
         */
        @NotNull ContextTagResolver build();

        /**
         * Adds a {@link ContextTagResolver}.
         *
         * @param contextTagResolver the {@link ContextTagResolver}
         * @return this {@link Builder}
         */
        @NotNull Builder add(@NotNull ContextTagResolver contextTagResolver);

        /**
         * Adds a {@link TagResolver}.
         *
         * @param tagResolver the {@link TagResolver}
         * @return this {@link Builder}
         */
        @NotNull Builder add(@NotNull TagResolver tagResolver);

        /**
         * Adds a {@link PlaceholderResolver}.
         *
         * @param placeholderResolver the {@link PlaceholderResolver}
         * @return this {@link Builder}
         */
        @NotNull Builder add(@NotNull PlaceholderResolver placeholderResolver);

        /**
         * Adds a {@link PlaceholderResolver}. All tags created from the given {@link PlaceholderResolver} will be
         * pre-process tags and their value will therefore be parsed by MiniMessage. Do not use this method for
         * placeholders that return user input!
         *
         * @param placeholderResolver the {@link PlaceholderResolver}
         * @return this {@link Builder}
         */
        @NotNull Builder addUnsafe(@NotNull PlaceholderResolver placeholderResolver);

        /**
         * Adds multiple {@link ContextTagResolver ContextTagResolvers}.
         *
         * @param contextTagResolvers the {@link ContextTagResolver ContextTagResolvers}
         * @return this {@link Builder}
         */
        default @NotNull Builder add(@NotNull ContextTagResolver @NotNull ... contextTagResolvers) {
            for (ContextTagResolver contextTagResolver : contextTagResolvers)
                this.add(contextTagResolver);
            return this;
        }

        /**
         * Adds multiple {@link TagResolver TagResolvers}.
         *
         * @param tagResolvers the {@link TagResolver TagResolvers}
         * @return this {@link Builder}
         */
        default @NotNull Builder add(@NotNull TagResolver @NotNull ... tagResolvers) {
            for (TagResolver tagResolver : tagResolvers)
                this.add(tagResolver);
            return this;
        }

        /**
         * Adds multiple {@link PlaceholderResolver PlaceholderResolvers}.
         *
         * @param placeholderResolvers the {@link PlaceholderResolver PlaceholderResolvers}
         * @return this {@link Builder}
         */
        default @NotNull Builder add(@NotNull PlaceholderResolver @NotNull ... placeholderResolvers) {
            for (PlaceholderResolver placeholderResolver : placeholderResolvers)
                this.add(placeholderResolver);
            return this;
        }

        /**
         * Adds multiple {@link PlaceholderResolver PlaceholderResolvers}. All tags created from the given
         * {@link PlaceholderResolver PlaceholderResolvers} will be pre-process tags and their value will therefore be
         * parsed by MiniMessage. Do not use this method for placeholders that return user input!
         *
         * @param placeholderResolvers the {@link PlaceholderResolver PlaceholderResolvers}
         * @return this {@link Builder}
         */
        default @NotNull Builder addUnsafe(@NotNull PlaceholderResolver @NotNull ... placeholderResolvers) {
            for (PlaceholderResolver placeholderResolver : placeholderResolvers)
                this.addUnsafe(placeholderResolver);
            return this;
        }
    }
}
