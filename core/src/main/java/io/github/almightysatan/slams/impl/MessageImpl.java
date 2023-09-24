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

package io.github.almightysatan.slams.impl;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.InvalidTypeException;
import io.github.almightysatan.slams.Message;
import io.github.almightysatan.slams.Slams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Objects;

/**
 * An abstract implementation of the {@link Message} interface.
 *
 * @param <T> the type of this message's value
 * @param <R> the type of this message's raw value (before placeholders are replaced)
 * @param <P> the format-dependent placeholder resolver type (does not have to extend
 *            {@link io.github.almightysatan.slams.PlaceholderResolver})
 */
public abstract class MessageImpl<T, R, P> implements Message<T> {

    private final String path;
    private final SlamsInternal languageManager;
    private final P implementationPlaceholderResolver;
    private final IdentityHashMap<Language, R> cache = new IdentityHashMap<>();

    protected MessageImpl(@NotNull String path, @NotNull Slams slams, @NotNull P implementationPlaceholderResolver) {
        this.path = this.checkPath(path);
        this.languageManager = (SlamsInternal) Objects.requireNonNull(slams);
        this.implementationPlaceholderResolver = Objects.requireNonNull(implementationPlaceholderResolver);
        this.languageManager.register(this);
    }

    private String checkPath(@NotNull String path) {
        if (path.isEmpty())
            throw new IllegalArgumentException("Path should not be empty!");

        boolean prevDot = false;
        char[] chars = path.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == 0x2E) {
                if (i == 0 || i == chars.length - 1)
                    throw new IllegalArgumentException("Path cannot start or end with a dot!");
                if (prevDot)
                    throw new IllegalArgumentException("Path cannot contain two or more dots directly following each other!");
                prevDot = true;
                continue;
            }
            prevDot = false;

            if ((c >= 0x30 && c <= 0x39) || (c >= 0x41 && c <= 0x5A) || (c >= 0x61 && c <= 0x7A))
                continue;

            throw new IllegalArgumentException("Path contains invalid characters!");
        }
        return path;
    }

    @Override
    public @NotNull String path() {
        return this.path;
    }

    protected abstract @NotNull R checkType(@Nullable Object value) throws InvalidTypeException;

    protected @NotNull R rawValue(@Nullable Context context) {
        Language language = this.resolveLanguage(context);
        R value = this.cache.get(language);
        if (value == null) {
            value = this.checkType(language.value(this.path));
            cache.put(language, value);
        }
        return value;
    }

    protected void clearCache() {
        this.cache.clear();
    }

    protected @NotNull SlamsInternal languageManager() {
        return this.languageManager;
    }

    protected @NotNull Language resolveLanguage(@Nullable Context context) {
        if (context != null) {
            String languageIdentifier = context.language();
            if (languageIdentifier != null) {
                Language language = this.languageManager.language(languageIdentifier);
                if (language != null)
                    return language;
            }
        }
        return this.languageManager().defaultLanguage();
    }

    protected @NotNull P placeholderResolver() {
        return this.implementationPlaceholderResolver;
    }
}
