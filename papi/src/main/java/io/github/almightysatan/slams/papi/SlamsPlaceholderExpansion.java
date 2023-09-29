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

package io.github.almightysatan.slams.papi;

import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Objects;

/**
 * A {@link PlaceholderExpansion} that makes SLAMS placeholders accessible via PlaceholderAPI.
 */
public class SlamsPlaceholderExpansion extends PlaceholderExpansion {

    private final String identifier;
    private final String author;
    private final String version;
    private final PlaceholderResolver placeholderResolver;

    /**
     * Creates a new {@link SlamsPlaceholderExpansion}.
     *
     * @param identifier the identifier of this expansion
     * @param author the author of this expansion
     * @param version the version of this expansion
     * @param placeholderResolver the {@link PlaceholderResolver}
     */
    public SlamsPlaceholderExpansion(@NotNull String identifier, @NotNull String author, @NotNull String version, @NotNull PlaceholderResolver placeholderResolver) {
        this.identifier = Objects.requireNonNull(identifier);
        this.author = Objects.requireNonNull(author);
        this.version = Objects.requireNonNull(version);
        this.placeholderResolver = Objects.requireNonNull(placeholderResolver);
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return this.author;
    }

    @Override
    public @NotNull String getVersion() {
        return this.version;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Placeholder placeholder = this.placeholderResolver.resolve(params);
        if (placeholder == null)
            return null;
        if (player == null)
            return placeholder.value(null, Collections.emptyList());
        if (!player.isOnline())
            return placeholder.value(OfflinePlayerContext.of(player), Collections.emptyList());
        return placeholder.value(PlayerContext.of((Player) player), Collections.emptyList());
    }
}
