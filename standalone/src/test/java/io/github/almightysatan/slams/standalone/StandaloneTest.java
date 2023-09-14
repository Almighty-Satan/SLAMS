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

import io.github.almightysatan.slams.LanguageManager;
import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandaloneTest {

    @Test
    public void testBasic() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        StandaloneStringEntry entry = StandaloneStringEntry.of("test", langManager);

        langManager.load("0", values -> values.put("test", "1234"));

        String value = entry.value();
        assertEquals("1234", value);
    }

    @Test
    public void testDefaultLanguageSelection() throws IOException {
        LanguageManager langManager = LanguageManager.create("1");
        StandaloneStringEntry entry = StandaloneStringEntry.of("test", langManager);

        langManager.load("0", values -> values.put("test", "123"));
        langManager.load("1", values -> values.put("test", "456"));

        String value = entry.value();
        assertEquals("456", value);
    }

    @Test
    public void testContextLanguageSelection() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        StandaloneStringEntry entry = StandaloneStringEntry.of("test", langManager);

        langManager.load("0", values -> values.put("test", "123"));
        langManager.load("1", values -> values.put("test", "456"));

        String value = entry.value(new TestContext("1", "YXZ"));
        assertEquals("456", value);
    }

    @Test
    public void testLocalPlaceholder() throws IOException {
        StandaloneLanguageManager langManager = StandaloneLanguageManager.of(LanguageManager.create("0"), PlaceholderStyle.PERCENT);
        StandaloneStringEntry entry = StandaloneStringEntry.of("test", langManager);

        langManager.load("0", values -> values.put("test", "Hello %xxx%"));

        String value = entry.value(null, Placeholder.constant("xxx", "World"));
        assertEquals("Hello World", value);
    }

    @Test
    public void testPlaceholder() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        StandaloneStringEntry entry = StandaloneStringEntry.of("test", langManager, PlaceholderResolver.of(Placeholder.constant("test", "World")));

        langManager.load("0", values -> values.put("test", "Hello <test>"));

        String value = entry.value();
        assertEquals("Hello World", value);
    }

    @Test
    public void testContextPlaceholder() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        TestContext context = new TestContext(null, "World");
        StandaloneStringEntry entry = StandaloneStringEntry.of("test", langManager, Placeholder.contextual("test", TestContext.class, TestContext::getName));

        langManager.load("0", values -> values.put("test", "Hello <test>"));

        String value = entry.value(context);
        assertEquals("Hello World", value);
    }

    @Test
    public void testPlaceholderArgument() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        StandaloneStringEntry entry = StandaloneStringEntry.of("test", langManager, PlaceholderResolver.of(Placeholder.withArgs("test", (arguments) -> arguments.get(1))));

        langManager.load("0", values -> values.put("test", "Hello <test:some other argument:World>"));

        String value = entry.value();
        assertEquals("Hello World", value);
    }

    @Test
    public void testArray() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        StandaloneStringArrayEntry entry = StandaloneStringArrayEntry.of("test", langManager, Placeholder.constant("test", "World"));

        langManager.load("0", values -> values.put("test", new String[]{"Hello", "<test>"}));

        String[] components = entry.value();
        assertEquals("Hello", components[0]);
        assertEquals("World", components[1]);
    }
}
