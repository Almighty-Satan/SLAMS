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

import io.github.almightysatan.slams.*;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a MiniMessage {@link Message}.
 *
 * @param <T> the type of this message's value
 */
public interface AdventureGenericMessage<T> extends Message<T> {

    @Override
    @NotNull
    AdventureTranslation<T> translate(@Nullable Context context) throws MissingTranslationException, UnknownLanguageException;

    /**
     * Replaces placeholders and returns the resulting value. Uses the given {@link Context Contexts} language.
     *
     * @param context     the context
     * @param tagResolver a {@link TagResolver}
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value(@Nullable Context context, @NotNull TagResolver tagResolver) throws MissingTranslationException, UnknownLanguageException {
        return this.translate(context).value(context, tagResolver);
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the given {@link Context Contexts} language.
     *
     * @param context      the context
     * @param tagResolvers an array of {@link TagResolver TagResolvers}
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value(@Nullable Context context, @NotNull TagResolver @NotNull ... tagResolvers) throws MissingTranslationException, UnknownLanguageException {
        Objects.requireNonNull(tagResolvers);
        return this.value(context, ContextTagResolver.of(tagResolvers));
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the default language.
     *
     * @param tagResolver a {@link TagResolver}
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value(@NotNull TagResolver tagResolver) throws MissingTranslationException, UnknownLanguageException {
        return this.value(null, tagResolver);
    }

    /**
     * Replaces placeholders and returns the resulting value. Uses the default language.
     *
     * @param tagResolvers an array of {@link TagResolver TagResolvers}
     * @return the value
     * @throws UnknownLanguageException    if the language can not be found
     * @throws MissingTranslationException if the language has no translation for this message
     */
    default @NotNull T value(@NotNull TagResolver @NotNull ... tagResolvers) throws MissingTranslationException, UnknownLanguageException {
        Objects.requireNonNull(tagResolvers);
        return this.value(ContextTagResolver.of(tagResolvers));
    }

    @Override
    default @NotNull T value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) throws MissingTranslationException, UnknownLanguageException {
        return this.value(context, ContextTagResolver.of(placeholderResolver));
    }
}
