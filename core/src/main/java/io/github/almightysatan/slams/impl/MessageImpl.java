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

public abstract class MessageImpl<T, R, P> implements Message<T> {

    private final String path;
    private final SlamsInternal languageManager;
    private final P implementationPlaceholderResolver;
    private final IdentityHashMap<Language, R> cache = new IdentityHashMap<>();

    protected MessageImpl(@NotNull String path, @NotNull Slams slams, @NotNull P implementationPlaceholderResolver) {
        this.path = Objects.requireNonNull(path);
        this.languageManager = (SlamsInternal) Objects.requireNonNull(slams);
        this.implementationPlaceholderResolver = Objects.requireNonNull(implementationPlaceholderResolver);
        this.languageManager.register(this);
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
