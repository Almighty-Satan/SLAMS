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

import io.github.almightysatan.slams.Language;
import io.github.almightysatan.slams.LanguageManager;
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
        LanguageManager langManager = LanguageManager.create("0");
        Language lang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "1234");
            return map;
        });

        MMStringEntry entry = MMStringEntry.of("test", langManager, null);
        TextComponent component = (TextComponent) entry.value(null);
        assertEquals("1234", component.content());
    }

    @Test
    public void testDefaultLanguageSelection() throws IOException {
        LanguageManager langManager = LanguageManager.create("1");
        Language otherLang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "123");
            return map;
        });
        Language defaultLang = langManager.load("1", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "456");
            return map;
        });

        MMStringEntry entry = MMStringEntry.of("test", langManager, null);
        TextComponent component = (TextComponent) entry.value(null);
        assertEquals("456", component.content());
    }

    @Test
    public void testContextLanguageSelection() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        Language otherLang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "123");
            return map;
        });
        Language contextLang = langManager.load("1", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "456");
            return map;
        });

        MMStringEntry entry = MMStringEntry.of("test", langManager, null);
        TextComponent component = (TextComponent) entry.value(new TestContext(contextLang, "YXZ"));
        assertEquals("456", component.content());
    }

    @Test
    public void testLocalPlaceholder() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        Language lang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "Hello <xxx>");
            return map;
        });

        MMStringEntry entry = MMStringEntry.of("test", langManager, ContextTagResolver.empty());
        TextComponent component = (TextComponent) entry.value(null, net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("xxx", "World"));
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testPlaceholder() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        Language lang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "Hello <test>");
            return map;
        });

        MMStringEntry entry = MMStringEntry.of("test", langManager, ContextTagResolver.of(Placeholder.of("test", "World")));
        TextComponent component = (TextComponent) entry.value(null);
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testContextPlaceholder() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        Language lang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "Hello <test>");
            return map;
        });

        TestContext context = new TestContext(null, "World");

        MMStringEntry entry = MMStringEntry.of("test", langManager, ContextTagResolver.of(TestContext.class, ctx-> net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("test", ctx.getName())));
        TextComponent component = (TextComponent) entry.value(context);
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testPlaceholderArgument() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        Language lang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "Hello <test:some other argument:World>");
            return map;
        });

        MMStringEntry entry = MMStringEntry.of("test", langManager, ContextTagResolver.of(Placeholder.of("test", (Placeholder.ContextIndependentValueFunction) (arguments) -> arguments.get(1))));
        TextComponent component = (TextComponent) entry.value(null);
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testArray() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        Language lang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", new String[] {"Hello", "<test>"});
            return map;
        });

        MMStringArrayEntry entry = MMStringArrayEntry.of("test", langManager, ctx -> net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("test", "World"));
        Component[] components = entry.value(null);
        assertEquals("Hello", ((TextComponent) components[0]).content());
        assertEquals("World", ((TextComponent) components[1]).content());
    }
}
