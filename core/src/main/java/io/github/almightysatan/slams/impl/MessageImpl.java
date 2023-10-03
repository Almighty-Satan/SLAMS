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

package io.github.almightysatan.slams.impl;

import io.github.almightysatan.slams.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Objects;

/**
 * An abstract implementation of the {@link Message} interface.
 *
 * @param <T> the type of this message's value
 */
public abstract class MessageImpl<T> implements Message<T> {

    private final String path;
    private final SlamsInternal languageManager;
    private final IdentityHashMap<Language, Translation<T>> cache = new IdentityHashMap<>();

    protected MessageImpl(@NotNull String path, @NotNull Slams slams) {
        this.path = this.checkPath(path);
        this.languageManager = (SlamsInternal) Objects.requireNonNull(slams);
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

            if ((c >= 0x30 && c <= 0x39) || (c >= 0x41 && c <= 0x5A) || (c >= 0x61 && c <= 0x7A) || c == 0x5F || c == 0x2D)
                continue;

            throw new IllegalArgumentException("Path contains invalid characters!");
        }
        return path;
    }

    @Override
    public @NotNull String path() {
        return this.path;
    }

    @Override
    public @NotNull Translation<T> translate(@Nullable Context context) {
        Language language = this.languageManager.language(context);
        Translation<T> value = this.cache.get(language);
        if (value == null) {
            Object rawValue = language.value(this.path);
            if (rawValue == null)
                throw new MissingTranslationException(language.identifier(), this.path);
            value = this.toMessageValue(rawValue);
            cache.put(language, value);
        }
        return value;
    }

    protected abstract @NotNull Translation<T> toMessageValue(@NotNull Object value);

    protected void clearCache() {
        this.cache.clear();
    }
}
