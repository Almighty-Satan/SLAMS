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

package io.github.almightysatan.slams.bukkit;

import io.github.almightysatan.slams.Component;
import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Contains bukkit-specific placeholders
 * 
 * @see BukkitPlaceholders#addBuiltIn(PlaceholderResolver.Builder) 
 */
public interface BukkitPlaceholders {

    /**
     * Adds the built-in Bukkit placeholders to the given {@link PlaceholderResolver.Builder}.
     * These placeholders include, but are not limited to
     * <ul>
     *     <li>click_open_url</li>
     *     <li>click_run_command</li>
     *     <li>click_suggest_command</li>
     *     <li>click_change_page</li>
     *     <li>hover_show_text</li>
     *     <li>hover_show_achievement</li>
     *     <li>hover_show_item</li>
     *     <li>hover_show_entity</li>
     * </ul>
     *
     * @param builder a {@link PlaceholderResolver.Builder}
     */
    static void addBuiltIn(@NotNull PlaceholderResolver.Builder builder) {
        abstract class EventPlaceholder<A> implements Placeholder {
            private final String key;
            protected final A action;

            public EventPlaceholder(@NotNull String key, @NotNull A action) {
                this.key = key;
                this.action = action;
            }

            @Override
            public @NotNull String key() {
                return key;
            }

            @Override
            public boolean constexpr() {
                return true;
            }

            @SuppressWarnings("unchecked")
            @Override
            public @NotNull <T> Component<T> value(@NotNull Object @NotNull [] contexts, @Unmodifiable @NotNull List<@NotNull Argument<T>> arguments, Component.@NotNull ValueFactory<T> factory) {
                if (arguments.size() < 2)
                    return factory.component(Placeholder.INVALID_ARGUMENTS);
                Argument<T> argument = arguments.get(1);
                T content = argument.value();
                if (!(content instanceof TextComponent[]))
                    return argument;

                // Copy because TextComponents might be immutable
                TextComponent[] components = ((TextComponent[]) content);
                TextComponent[] copy = new TextComponent[components.length];
                for (int i = 0; i < copy.length; i++)
                    copy[i] = new TextComponent(components[i]);

                this.setEvent(copy, arguments.get(0));
                return Component.of((T) copy, argument.stringValue(), argument.rawValue());
            }

            protected abstract <T> void setEvent(@NotNull TextComponent[] content, @NotNull Argument<T> value);
        }

        class ClickEventPlaceholder extends EventPlaceholder<ClickEvent.Action> {
            public ClickEventPlaceholder(@NotNull String key, @NotNull ClickEvent.Action action) {
                super(key, action);
            }

            @Override
            protected <T> void setEvent(@NotNull TextComponent[] content, @NotNull Argument<T> value) {
                String argument = value.stringValue();
                if (!argument.isEmpty())
                    for (TextComponent component : content)
                        component.setClickEvent(new ClickEvent(this.action, argument));
            }
        }

        class HoverEventPlaceholder extends EventPlaceholder<HoverEvent.Action> {
            public HoverEventPlaceholder(@NotNull String key, @NotNull HoverEvent.Action action) {
                super(key, action);
            }

            @Override
            protected <T> void setEvent(@NotNull TextComponent[] content, @NotNull Argument<T> value) {
                T argument = value.value();
                if (argument instanceof TextComponent[] && ((TextComponent[]) argument).length > 0)
                    for (TextComponent component : content)
                        component.setHoverEvent(new HoverEvent(this.action, (TextComponent[]) argument));
            }
        }

        builder.add(new ClickEventPlaceholder("click_open_url", ClickEvent.Action.OPEN_URL),
                new ClickEventPlaceholder("click_run_command", ClickEvent.Action.RUN_COMMAND),
                new ClickEventPlaceholder("click_suggest_command", ClickEvent.Action.SUGGEST_COMMAND),
                new ClickEventPlaceholder("click_change_page", ClickEvent.Action.CHANGE_PAGE),
                new HoverEventPlaceholder("hover_show_text", HoverEvent.Action.SHOW_TEXT),
                new HoverEventPlaceholder("hover_show_achievement", HoverEvent.Action.SHOW_ACHIEVEMENT),
                new HoverEventPlaceholder("hover_show_item", HoverEvent.Action.SHOW_ITEM),
                new HoverEventPlaceholder("hover_show_entity", HoverEvent.Action.SHOW_ENTITY));
    }

    /**
     * Returns a new {@link PlaceholderResolver} that resolves the built-in Bukkit placeholders.
     * These placeholders include, but are not limited to
     * <ul>
     *     <li>click_open_url</li>
     *     <li>click_run_command</li>
     *     <li>click_suggest_command</li>
     *     <li>click_change_page</li>
     *     <li>hover_show_text</li>
     *     <li>hover_show_achievement</li>
     *     <li>hover_show_item</li>
     *     <li>hover_show_entity</li>
     * </ul>
     *
     * @return a new {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver create() {
        PlaceholderResolver.Builder builder = PlaceholderResolver.builder();
        addBuiltIn(builder);
        return builder.build();
    }
}
