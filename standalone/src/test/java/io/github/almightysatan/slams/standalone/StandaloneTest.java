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

import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandaloneTest {

    @Test
    public void testBasic() throws IOException {
        Slams langManager = Slams.create("0");
        StandaloneMessage entry = StandaloneMessage.of("test", langManager);

        langManager.load("0", values -> values.put("test", "1234"));

        String value = entry.value();
        assertEquals("1234", value);
    }

    @Test
    public void testDefaultLanguageSelection() throws IOException {
        Slams langManager = Slams.create("1");
        StandaloneMessage entry = StandaloneMessage.of("test", langManager);

        langManager.load("0", values -> values.put("test", "123"));
        langManager.load("1", values -> values.put("test", "456"));

        String value = entry.value();
        assertEquals("456", value);
    }

    @Test
    public void testContextLanguageSelection() throws IOException {
        Slams langManager = Slams.create("0");
        StandaloneMessage entry = StandaloneMessage.of("test", langManager);

        langManager.load("0", values -> values.put("test", "123"));
        langManager.load("1", values -> values.put("test", "456"));

        String value = entry.value(new TestContext("1", "YXZ"));
        assertEquals("456", value);
    }

    @Test
    public void testLocalPlaceholder() throws IOException {
        StandaloneSlams langManager = StandaloneSlams.of(Slams.create("0"), PlaceholderStyle.PERCENT);
        StandaloneMessage entry = StandaloneMessage.of("test", langManager);

        langManager.load("0", values -> values.put("test", "Hello %xxx%"));

        String value = entry.value(null, Placeholder.constant("xxx", "World"));
        assertEquals("Hello World", value);
    }

    @Test
    public void testPlaceholder() throws IOException {
        Slams langManager = Slams.create("0");
        StandaloneMessage entry = StandaloneMessage.of("test", langManager, PlaceholderResolver.of(Placeholder.constant("test", "World")));

        langManager.load("0", values -> values.put("test", "Hello <test>"));

        String value = entry.value();
        assertEquals("Hello World", value);
    }

    @Test
    public void testContextPlaceholder() throws IOException {
        Slams langManager = Slams.create("0");
        TestContext context = new TestContext(null, "World");
        StandaloneMessage entry = StandaloneMessage.of("test", langManager, Placeholder.contextual("test", TestContext.class, TestContext::getName));

        langManager.load("0", values -> values.put("test", "Hello <test>"));

        String value = entry.value(context);
        assertEquals("Hello World", value);
    }

    @Test
    public void testPlaceholderArgument() throws IOException {
        Slams langManager = Slams.create("0");
        StandaloneMessage entry = StandaloneMessage.of("test", langManager, PlaceholderResolver.of(Placeholder.withArgs("test", (arguments) -> arguments.get(1))));

        langManager.load("0", values -> values.put("test", "Hello <test:some other argument:World>"));

        String value = entry.value();
        assertEquals("Hello World", value);
    }

    @Test
    public void testArray() throws IOException {
        Slams langManager = Slams.create("0");
        StandaloneMessageArray entry = StandaloneMessageArray.of("test", langManager, Placeholder.constant("test", "World"));

        langManager.load("0", values -> values.put("test", new String[]{"Hello", "<test>"}));

        String[] components = entry.value();
        assertEquals("Hello", components[0]);
        assertEquals("World", components[1]);
    }

    @Test
    public void testArray2d() throws IOException {
        Slams langManager = Slams.create("0");
        StandaloneMessageArray2d entry = StandaloneMessageArray2d.of("test", langManager, Placeholder.constant("test", "World"));

        langManager.load("0", values -> values.put("test", new String[][]{new String[]{"Hello", "<test>"}}));

        String[][] components = entry.value();
        assertEquals("Hello", components[0][0]);
        assertEquals("World", components[0][1]);
    }

    @Test
    public void testMap() throws IOException {
        Slams langManager = Slams.create("0");
        StandaloneMessageMap<Integer> entry = StandaloneMessageMap.of("test", langManager, Integer.class, Placeholder.constant("test", "World"));

        Map<Integer, String> map = new HashMap<>();
        map.put(5, "Hello");
        map.put(99, "<test>");
        langManager.load("0", values -> values.put("test", map));

        Map<Integer, String> components = entry.value();
        assertEquals("Hello", components.get(5));
        assertEquals("World", components.get(99));
    }

    @Test
    public void testMessageArrayValue() throws IOException {
        Slams langManager = Slams.create("0");
        StandaloneMessageArray2d entry = StandaloneMessageArray2d.of("test", langManager, Placeholder.constant("test", "World"));

        langManager.load("0", values -> values.put("test", new String[][]{new String[]{"Hello", "<test>"}}));

        assertEquals(1, entry.get(null).size());
        assertEquals(2, entry.get(null).get(0).size());
        assertEquals("World", entry.get(null).get(0).get(1).value());
    }
}
