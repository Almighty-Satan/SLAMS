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

import io.github.almightysatan.slams.Context;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A {@link Context} that has a {@link Player}.
 */
public interface PlayerContext extends OfflinePlayerContext {

    @Override
    @NotNull Player player();

    /**
     * Creates a new {@link PlayerContext} from the given player.
     *
     * @param player the player
     * @return the context
     */
    static @NotNull PlayerContext of(@NotNull Player player) {
        Objects.requireNonNull(player);
        return new PlayerContext() {
            @Override
            public @NotNull Player player() {
                return player;
            }

            @Override
            public @Nullable String language() {
                return null;
            }
        };
    }
}
