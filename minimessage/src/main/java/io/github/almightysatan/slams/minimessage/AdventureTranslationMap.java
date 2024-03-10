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

import io.github.almightysatan.slams.TranslationMap;

import java.util.Map;

/**
 * Represents a map of {@link AdventureTranslation AdventureTranslations}.
 *
 * @param <K> the type of the key
 * @param <T> the type of this translation
 * @param <U> the {@link AdventureTranslation AdventureTranslations} in this map
 */
public interface AdventureTranslationMap<K, T, U extends AdventureTranslation<T>> extends TranslationMap<K, T, U>, AdventureTranslation<Map<K, T>> {
}
