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

import io.github.almightysatan.slams.*;
import io.github.almightysatan.slams.bukkit.impl.BukkitTypes;
import io.github.almightysatan.slams.impl.MessageImpl;
import io.github.almightysatan.slams.standalone.PlaceholderStyle;
import io.github.almightysatan.slams.standalone.StandaloneSlams;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Message} in {@link BaseComponent} format.
 */
public interface BukkitMessage extends Message<BaseComponent[]> {

    default void send(@NotNull Player target, @Nullable String language, @NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        BaseComponent[] value = this.value(language, placeholderResolver, contexts);
        target.spigot().sendMessage(value);
    }

    default void send(@NotNull Player target, @Nullable String language, @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        this.send(target, language, PlaceholderResolver.empty(), contexts);
    }
    
    default void send(@NotNull Player target, @NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        this.send(target, null, placeholderResolver, contexts);
    }

    default void send(@NotNull Player target, @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        this.send(target, PlaceholderResolver.empty(), contexts);
    }

    default void send(@NotNull CommandSender target, @Nullable String language, @NotNull PlaceholderResolver placeholderResolver,
            @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        if (target instanceof Player) {
            this.send((Player) target, language, placeholderResolver, contexts);
            return;
        }

        String value = this.stringValue(language, placeholderResolver, contexts);
        target.sendMessage(value);
    }

    default void send(@NotNull CommandSender target, @NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        this.send(target, null, placeholderResolver, contexts);
    }

    default void send(@NotNull CommandSender target, @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        this.send(target, PlaceholderResolver.empty(), contexts);
    }

    @Override
    default @NotNull BaseComponent @NotNull [] value(@Nullable String language, @NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        return this.translate(language).value(placeholderResolver, contexts);
    }

    default @NotNull String stringValue(@Nullable String language, @NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        return ((BukkitTranslation) this.translate(language)).stringValue(placeholderResolver, contexts);
    }

    default @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        return this.stringValue(null, placeholderResolver, contexts);
    }

    default @NotNull String stringValue(@NotNull Object @NotNull ... contexts) throws MissingTranslationException, UnknownLanguageException {
        return this.stringValue(null, PlaceholderResolver.empty(), contexts);
    }

    /**
     * Creates a new {@link BukkitMessage} with the given path, {@link PlaceholderStyle}, {@link Slams} and
     * {@link PlaceholderResolver}.
     *
     * @param path                the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams               the language manager (slams instance) to use
     * @param placeholderResolver the tag resolver
     * @return a new {@link BukkitMessage}
     */
    static @NotNull BukkitMessage of(@NotNull String path, @NotNull StandaloneSlams slams, @NotNull PlaceholderResolver placeholderResolver) {
        class BukkitMessageImpl extends MessageImpl<BaseComponent[]> implements BukkitMessage {

            protected BukkitMessageImpl() {
                super(path, slams);
            }

            @Override
            protected @NotNull Translation<BaseComponent[]> toMessageValue(@NotNull Object value) {
                return BukkitTypes.messageValue(slams, placeholderResolver, value);
            }
        }
        return new BukkitMessageImpl();
    }

    /**
     * Creates a new {@link BukkitMessage} with the given path, {@link PlaceholderStyle}, {@link StandaloneSlams}
     * and {@link PlaceholderResolver}. Uses {@link StandaloneSlams#style()}.
     *
     * @param path                the case-sensitive dotted path of this message. For example 'path.to.example.message'
     * @param slams               the language manager (slams instance) to use
     * @return a new {@link BukkitMessage}
     */
    static @NotNull BukkitMessage of(@NotNull String path, @NotNull StandaloneSlams slams) {
        return of(path, slams, PlaceholderResolver.empty());
    }
}
