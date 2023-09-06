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

package io.github.almightysatan.slams.standalone;

import io.github.almightysatan.slams.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class CompositeComponent implements Component {

    private static final Pattern REGEX = Pattern.compile("(?:((?:\\\\%|[^%])*)(?:%)((?:(?:\\\\%|[^%\\\\])*))(?:%)((?:\\\\%|[^%\\\\])*))|(.+)");

    private final Component[] components;

    CompositeComponent(@NotNull String raw) {
        Objects.requireNonNull(raw);
        this.components = processString(raw);
    }

    @Override
    public @NotNull String value(@Nullable Context context, @NotNull PlaceholderResolver placeholderResolver) {
        return Arrays.stream(components).map(component -> component.value(context, placeholderResolver)).collect(Collectors.joining());
    }

    private static Component[] processString(@NotNull String input) {
        List<Component> components = new ArrayList<>();
        Matcher matcher = REGEX.matcher(input);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = matcher.group(i);
                if (group != null) {
                    if (i == 2)
                        components.add(Component.placeholder(group));
                    else
                        components.add(Component.simple(group));
                }
            }
        }
        return components.toArray(new Component[0]);
    }
}
