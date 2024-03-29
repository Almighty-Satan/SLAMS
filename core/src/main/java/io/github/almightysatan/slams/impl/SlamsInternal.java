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

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.UnknownLanguageException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

/**
 * An extension of the {@link Slams} interface that contains methods that should only be used internally.
 */
public interface SlamsInternal extends Slams {

    void register(@NotNull MessageImpl<?> entry);

    @NotNull @Unmodifiable Set<@NotNull String> paths();

    @Nullable Language language(@NotNull String identifier);

    @NotNull Language defaultLanguage() throws UnknownLanguageException;

    @NotNull Language language(@Nullable Context context) throws UnknownLanguageException;
}
