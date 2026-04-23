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

import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.standalone.PlaceholderStyle;
import io.github.almightysatan.slams.standalone.StandaloneSlams;
import net.md_5.bungee.api.chat.BaseComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BukkitCompositeComponentTest {

    @Test
    public void testInline() {
        StandaloneSlams slams0 = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, false, false);
        BukkitCompositeComponent component0 = new BukkitCompositeComponent(slams0, "<hello><hello>", Placeholder.constant("hello", "world"));
        Assertions.assertEquals("worldworld", BaseComponent.toPlainText(component0.value(PlaceholderResolver.empty(), new Object[0])));
        Assertions.assertEquals(2, component0.size());

        StandaloneSlams slams1 = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, true, false);
        BukkitCompositeComponent component1 = new BukkitCompositeComponent(slams1, "<hello><hello>", Placeholder.constant("hello", "world"));
        Assertions.assertEquals("worldworld", BaseComponent.toPlainText(component1.value(PlaceholderResolver.empty(), new Object[0])));
        Assertions.assertEquals(2, component1.size());

        StandaloneSlams slams2 = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, true, true);
        BukkitCompositeComponent component2 = new BukkitCompositeComponent(slams2, "<hello><hello>", Placeholder.constant("hello", "world"));
        Assertions.assertEquals("worldworld", BaseComponent.toPlainText(component2.value(PlaceholderResolver.empty(), new Object[0])));
        Assertions.assertEquals(1, component2.size());
    }
}
