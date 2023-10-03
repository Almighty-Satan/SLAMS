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

package io.github.almightysatan.slams;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents a map of {@link Translation Translations}.
 *
 * @param <T> the type of this translation
 * @param <U> the {@link Translation Translations} in this map
 */
public interface TranslationMap<K, T, U extends Translation<T>> extends Translation<Map<K, T>> {

    /**
     * Returns the {@link Translation} for the specified key
     *
     * @param key the key
     * @return the translation for the given key or null if it does not exist
     */
    @Nullable U get(K key);

    /**
     * Returns the size of the map.
     *
     * @return the size of the map
     */
    int size();
}
