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

package io.github.almightysatan.slams.bukkit;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A context that has a {@link OfflinePlayer}.
 */
public interface OfflinePlayerContext {

    /**
     * Returns the player.
     *
     * @return the player
     */
    @NotNull OfflinePlayer player();

    /**
     * Creates a new {@link OfflinePlayerContext} from the given player.
     *
     * @param offlinePlayer the player
     * @return the context
     */
    static @NotNull OfflinePlayerContext of(@NotNull OfflinePlayer offlinePlayer) {
        Objects.requireNonNull(offlinePlayer);
        return () -> offlinePlayer;
    }
}
