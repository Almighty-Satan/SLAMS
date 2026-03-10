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

package io.github.almightysatan.slams.bukkit.impl;

import io.github.almightysatan.slams.InvalidTypeException;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.Translation;
import io.github.almightysatan.slams.bukkit.BukkitTranslation;
import io.github.almightysatan.slams.impl.Types;
import io.github.almightysatan.slams.standalone.StandaloneSlams;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public interface BukkitTypes {

    static @NotNull BukkitTranslation messageValue(@NotNull StandaloneSlams slams, @NotNull PlaceholderResolver placeholderResolver, @NotNull Object input) throws InvalidTypeException {
        return new BukkitCompositeComponent(slams, Types.checkString(input), placeholderResolver);
    }
}
