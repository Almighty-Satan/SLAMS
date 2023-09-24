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

package io.github.almightysatan.slams.minimessage;

import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageTest {

    @Test
    public void testBasic() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessage entry = AdventureMessage.of("test", langManager);

        langManager.load("0", values -> values.put("test", "1234"));

        TextComponent component = (TextComponent) entry.value(null);
        assertEquals("1234", component.content());
    }

    @Test
    public void testDefaultLanguageSelection() throws IOException {
        Slams langManager = Slams.create("1");
        AdventureMessage entry = AdventureMessage.of("test", langManager);

        langManager.load("0", values -> values.put("test", "123"));
        langManager.load("1", values -> values.put("test", "456"));

        TextComponent component = (TextComponent) entry.value(null);
        assertEquals("456", component.content());
    }

    @Test
    public void testContextLanguageSelection() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessage entry = AdventureMessage.of("test", langManager);

        langManager.load("0", values -> values.put("test", "123"));
        langManager.load("1", values -> values.put("test", "456"));

        TextComponent component = (TextComponent) entry.value(new TestContext("1", "YXZ"));
        assertEquals("456", component.content());
    }

    @Test
    public void testLocalPlaceholder() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessage entry = AdventureMessage.of("test", langManager, ContextTagResolver.empty());

        langManager.load("0", values -> values.put("test", "Hello <xxx>"));

        TextComponent component = (TextComponent) entry.value(null, net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("xxx", "World"));
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testPlaceholder() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessage entry = AdventureMessage.of("test", langManager, ContextTagResolver.of(Placeholder.constant("test", "World")));

        langManager.load("0", values -> values.put("test", "Hello <test>"));

        TextComponent component = (TextComponent) entry.value(null);
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testContextPlaceholder() throws IOException {
        Slams langManager = Slams.create("0");
        TestContext context = new TestContext(null, "World");
        AdventureMessage entry = AdventureMessage.of("test", langManager, ContextTagResolver.of(PlaceholderResolver.builder().contextual("test", TestContext.class, TestContext::getName).build()));

        langManager.load("0", values -> values.put("test", "Hello <test>"));

        TextComponent component = (TextComponent) entry.value(context);
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testPlaceholderArgument() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessage entry = AdventureMessage.of("test", langManager, ContextTagResolver.of(Placeholder.withArgs("test", (arguments) -> arguments.get(1))));

        langManager.load("0", values -> values.put("test", "Hello <test:some other argument:World>"));

        TextComponent component = (TextComponent) entry.value(null);
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testArray() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessageArray entry = AdventureMessageArray.of("test", langManager, net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("test", "World"));

        langManager.load("0", values -> values.put("test", new String[]{"Hello", "<test>"}));

        Component[] components = entry.value(null);
        assertEquals("Hello", ((TextComponent) components[0]).content());
        assertEquals("World", ((TextComponent) components[1]).content());
    }

    @Test
    public void testArray2d() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessageArray2d entry = AdventureMessageArray2d.of("test", langManager, net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("test", "World"));

        langManager.load("0", values -> values.put("test", new String[][]{new String[]{"Hello", "<test>"}}));

        Component[][] components = entry.value(null);
        assertEquals("Hello", ((TextComponent) components[0][0]).content());
        assertEquals("World", ((TextComponent) components[0][1]).content());
    }

    @Test
    public void testMap() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessageMap<Integer> entry = AdventureMessageMap.of("test", langManager, Integer.class, net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("test", "World"));

        Map<Integer, String> map = new HashMap<>();
        map.put(5, "Hello");
        map.put(99, "<test>");
        langManager.load("0", values -> values.put("test", map));

        Map<Integer, Component> components = entry.value();
        assertEquals("Hello", ((TextComponent) components.get(5)).content());
        assertEquals("World", ((TextComponent) components.get(99)).content());
    }

    @Test
    public void testMessageArrayValue() throws IOException {
        Slams langManager = Slams.create("0");
        AdventureMessageArray2d entry = AdventureMessageArray2d.of("test", langManager, net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("test", "World"));

        langManager.load("0", values -> values.put("test", new String[][]{new String[]{"Hello", "<test>"}}));

        assertEquals(1, entry.translate(null).size());
        assertEquals(2, entry.translate(null).get(0).size());
        assertEquals("World", ((TextComponent) entry.translate(null).get(0).get(1).value()).content());
    }
}
