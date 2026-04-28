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

package io.github.almightysatan.slams.bukkit.impl;

import io.github.almightysatan.slams.Component;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.bukkit.BukkitTranslation;
import io.github.almightysatan.slams.standalone.StandaloneSlams;
import io.github.almightysatan.slams.standalone.impl.CompositeComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApiStatus.Internal
public class BukkitCompositeComponent extends CompositeComponent<TextComponent[]> implements BukkitTranslation {

    private static ValueFactory<TextComponent[]> FACTORY = BukkitCompositeComponent::parse;

    protected BukkitCompositeComponent(@NotNull StandaloneSlams slams, @NotNull String raw, @NotNull PlaceholderResolver placeholderResolver) {
        super(slams, raw, placeholderResolver);
    }
    
    protected BukkitCompositeComponent(@NotNull Component<TextComponent[]>[] components) {
        super(components);
    }

    @Override
    public @NotNull TextComponent[] value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
        List<TextComponent> components = new ArrayList<>();
        this.value(placeholderResolver, contexts, value -> {
            for (TextComponent component : value) {
                TextComponent copy = new TextComponent(component);
                if (components.isEmpty()) {
                    components.add(copy);
                    continue;
                }

                TextComponent prev = components.get(components.size() - 1);
                if (prev.getColorRaw() != null && copy.getColorRaw() == null)
                    copy.setColor(prev.getColorRaw());
                if (prev.isBoldRaw() != null && copy.isBoldRaw() == null)
                    copy.setBold(prev.isBoldRaw());
                if (prev.isItalicRaw() != null && copy.isItalicRaw() == null)
                    copy.setItalic(prev.isItalicRaw());
                if (prev.isUnderlinedRaw() != null && copy.isUnderlinedRaw() == null)
                    copy.setUnderlined(prev.isUnderlinedRaw());
                if (prev.isStrikethroughRaw() != null && copy.isStrikethroughRaw() == null)
                    copy.setStrikethrough(prev.isStrikethroughRaw());
                if (prev.isObfuscatedRaw() != null && copy.isObfuscatedRaw() == null)
                    copy.setObfuscated(prev.isObfuscatedRaw());
                components.add(copy);
            }
        });
        return components.toArray(new TextComponent[0]);
    }

    @Override
    protected @NotNull CompositeComponent<TextComponent[]> composite(@NotNull Component<TextComponent[]>[] components) {
        return new BukkitCompositeComponent(components);
    }

    @Override
    protected @NotNull Component.ValueFactory<TextComponent[]> factory() {
        return FACTORY;
    }

    @Override
    protected @NotNull TextComponent[] merge(@NotNull List<TextComponent[]> values) {
        return values.stream().flatMap(Arrays::stream).toArray(TextComponent[]::new);
    }

    private static void setColor(@NotNull TextComponent component, @NotNull ChatColor color) {
        switch (color) {
            case BOLD:
                component.setBold(true);
                break;
            case ITALIC:
                component.setItalic(true);
                break;
            case UNDERLINE:
                component.setUnderlined(true);
                break;
            case STRIKETHROUGH:
                component.setStrikethrough(true);
                break;
            case MAGIC:
                component.setObfuscated(true);
                break;
            case RESET:
                component.setBold(false);
                component.setItalic(false);
                component.setUnderlined(false);
                component.setStrikethrough(false);
                component.setObfuscated(false);
                component.setColor(ChatColor.WHITE);
                break;
            default:
                component.setColor(color);
                break;
        }
    }

    public static @NotNull TextComponent @NotNull [] parse(@NotNull String text) {
        List<TextComponent> components = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        TextComponent component = new TextComponent();
        boolean paragraph = false;
        for (char c : text.toCharArray()) {
            if (c == '§') {
                paragraph = true;
                continue;
            }

            if (paragraph) {
                paragraph = false;
                ChatColor color = ChatColor.getByChar(Character.toLowerCase(c));
                if (color == null)
                    continue;

                if (builder.length() > 0) {
                    component.setText(builder.toString());
                    components.add(component);
                    builder.setLength(0);
                    component = new TextComponent();
                }
                setColor(component, color);
                continue;
            }
            builder.append(c);
        }

        component.setText(builder.toString());
        components.add(component);

        return components.toArray(new TextComponent[0]);
    }
}
