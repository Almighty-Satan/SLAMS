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

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.PlaceholderResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class CompositeComponent implements Component {

    private final Component[] components;

    CompositeComponent(@NotNull PlaceholderStyle style, @NotNull String raw, @NotNull PlaceholderResolver placeholderResolver) {
        this.components = processString(style, raw, placeholderResolver);
    }

    @Override
    public @NotNull String value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) {
        return Arrays.stream(components).map(component -> component.value(context, placeholderResolver)).collect(Collectors.joining());
    }

    @TestOnly
    @NotNull String value() {
        return value(null, PlaceholderResolver.empty());
    }

    static @NotNull Component @NotNull [] processString(@NotNull PlaceholderStyle style, @NotNull String input, @NotNull PlaceholderResolver placeholderResolver) {
        char headChar = style.head();
        char tailChar = style.tail();
        char separatorChar = style.separator();

        List<Component> components = new ArrayList<>();
        StringBuilder raw = new StringBuilder();
        List<Component> arguments = new ArrayList<>();
        StringBuilder argument = new StringBuilder();
        int scope = 0;
        boolean escape = false;
        for (char c : input.toCharArray()) {
            if (c == '\\') {
                escape = !escape;
                if (escape)
                    continue;
            }

            if (escape) {
                escape = false;
                raw.append(c);
                if (scope > 0)
                    argument.append(c);
                continue;
            }

            if (c == headChar && (tailChar != headChar || scope == 0) && ++scope == 1) {
                if (raw.length() > 0) {
                    components.add(Component.simple(raw.toString()));
                    raw.setLength(0);
                }
                raw.append(c);
                continue;
            }

            raw.append(c);

            if (scope > 0) {
                if (c == tailChar && --scope == 0) {
                    arguments.add(new CompositeComponent(style, argument.toString(), placeholderResolver));
                    components.add(Component.placeholder(raw.toString(), arguments, placeholderResolver));
                    raw.setLength(0);
                    argument.setLength(0);
                    arguments = new ArrayList<>();
                    continue;
                }

                if (scope == 1 && c == separatorChar) {
                    arguments.add(new CompositeComponent(style, argument.toString(), placeholderResolver));
                    argument.setLength(0);
                } else
                    argument.append(c);
            }
        }

        if (raw.length() > 0)
            components.add(Component.simple(raw.toString()));

        return components.toArray(new Component[0]);
    }
}
