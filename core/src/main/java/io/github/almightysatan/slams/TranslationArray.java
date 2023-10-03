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

import org.jetbrains.annotations.NotNull;

/**
 * Represents an array of {@link Translation Translations}.
 *
 * @param <T> the type of this translation
 * @param <U> the {@link Translation Translations} in this array
 */
public interface TranslationArray<T, U extends Translation<T>> extends Translation<T[]> {

    /**
     * Returns the {@link Translation} at the specified index of the array.
     *
     * @param index the index
     * @return the translation at the given index
     * @throws ArrayIndexOutOfBoundsException if the index is either negative or greater than or equal to the size of
     *                                        the array
     */
    @NotNull U get(int index);

    /**
     * Returns the size of the array.
     *
     * @return the size of the array
     */
    int size();
}
