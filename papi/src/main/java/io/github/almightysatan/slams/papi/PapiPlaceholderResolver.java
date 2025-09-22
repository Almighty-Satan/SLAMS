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

package io.github.almightysatan.slams.papi;

import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Contains methods to create a placeholder named "papi" that can be used to access PlaceholderAPI placeholders.
 */
public interface PapiPlaceholderResolver {

    /**
     * Returns a {@link Placeholder} named "papi".
     *
     * @return a {@link Placeholder}
     */
    static @NotNull Placeholder create() {
        return Placeholder.of("papi", (context, arguments) -> {
            if (arguments.size() != 2)
                return "INVALID_PAPI_FORMAT";
            String identifier = arguments.get(0).toLowerCase(Locale.ROOT);
            PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().getExpansion(identifier);
            if (expansion == null)
                return "UNKNOWN_PAPI_EXPANSION";

            String value = expansion.onRequest(context instanceof OfflinePlayerContext ? ((OfflinePlayerContext) context).player() : null, arguments.get(1));
            if (value == null)
                return "UNKNOWN_PAPI_PLACEHOLDER";
            return value;
        });
    }

    /**
     * Returns a {@link PlaceholderResolver} that resolves a placeholder named "papi" if PlaceholderAPI is available.
     * Otherwise an empty {@link PlaceholderResolver} is returned.
     *
     * @return a {@link PlaceholderResolver}
     */
    static @NotNull PlaceholderResolver createIfAvailable() {
        try {
            PlaceholderAPIPlugin.getInstance();
            return create();
        } catch (NoClassDefFoundError e) {
            return PlaceholderResolver.empty();
        }
    }

    /**
     * Adds a {@link Placeholder} named "papi" to the given {@link PlaceholderResolver.Builder} if PlaceholderAPI is
     * available.
     * 
     * @param builder a {@link PlaceholderResolver.Builder}
     */
    static void addIfAvailable(@NotNull PlaceholderResolver.Builder builder) {
        try {
            PlaceholderAPIPlugin.getInstance();
            builder.add(create());
        } catch (NoClassDefFoundError e) {
            // nop
        }
    }
}
