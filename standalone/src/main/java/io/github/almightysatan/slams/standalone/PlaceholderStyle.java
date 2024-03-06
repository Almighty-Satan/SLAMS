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

package io.github.almightysatan.slams.standalone;

import org.jetbrains.annotations.NotNull;

/**
 * The Style of Placeholders.
 */
public interface PlaceholderStyle {

    /**
     * Placeholders look like this: {@code <key:arg1:arg2>}
     */
    PlaceholderStyle ANGLE_BRACKETS = of('<', '>', ':');

    /**
     * Placeholders look like this: {@code {key:arg1:arg2}}
     */
    PlaceholderStyle CURLY_BRACKETS = of('{', '}', ':');

    /**
     * Placeholders look like this: {@code (key:arg1:arg2)}
     */
    PlaceholderStyle PARENTHESES = of('(', ')', ':');

    /**
     * Placeholders look like this: {@code %key:arg1:arg2%}
     *
     * @deprecated This style is incompatible with advanced placeholder features like conditionals
     */
    @Deprecated
    PlaceholderStyle PERCENT = of('%', '%', ':');

    /**
     * Returns the first char of a placeholder.
     *
     * @return the first char of a placeholder
     */
    char head();

    /**
     * Returns the last char of a placeholder.
     *
     * @return the last char of a placeholder
     */
    char tail();

    /**
     * Returns the char used to separate arguments
     *
     * @return the char used to separate arguments
     */
    char separator();

    /**
     * Creates a new {@link PlaceholderStyle}.
     *
     * @param head the first char of a placeholder
     * @param tail the last char of a placeholder
     * @param separator the char used to separate arguments
     * @return a new {@link PlaceholderStyle}
     */
    static @NotNull PlaceholderStyle of(char head, char tail, char separator) {
        return new PlaceholderStyle() {
            @Override
            public char head() {
                return head;
            }

            @Override
            public char tail() {
                return tail;
            }

            @Override
            public char separator() {
                return separator;
            }
        };
    }
}
