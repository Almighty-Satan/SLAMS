package io.github.almightysatan.language.minimessage;

import io.github.almightysatan.language.Language;
import io.github.almightysatan.language.LanguageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        }, null);

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
        }, null);
        Language defaultLang = langManager.load("1", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "456");
            return map;
        }, null);

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
        }, null);
        Language contextLang = langManager.load("1", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "456");
            return map;
        }, null);

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
        }, null);

        MMStringEntry entry = MMStringEntry.of("test", langManager, ContextTagResolver.empty());
        TextComponent component = (TextComponent) entry.value(null, Placeholder.unparsed("xxx", "World"));
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testPlaceholder() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        Language lang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", "Hello <test>");
            return map;
        }, null);

        MMStringEntry entry = MMStringEntry.of("test", langManager, ctx -> Placeholder.unparsed("test", "World"));
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
        }, null);

        TestContext context = new TestContext(null, "World");

        MMStringEntry entry = MMStringEntry.of("test", langManager, ContextTagResolver.of(TestContext.class, ctx-> Placeholder.unparsed("test", ctx.getName())));
        TextComponent component = (TextComponent) entry.value(context);
        assertEquals("Hello World", component.content());
    }

    @Test
    public void testArray() throws IOException {
        LanguageManager langManager = LanguageManager.create("0");
        Language lang = langManager.load("0", paths -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", new String[] {"Hello", "<test>"});
            return map;
        }, null);

        MMStringArrayEntry entry = MMStringArrayEntry.of("test", langManager, ctx -> Placeholder.unparsed("test", "World"));
        Component[] components = entry.value(null);
        assertEquals("Hello", ((TextComponent) components[0]).content());
        assertEquals("World", ((TextComponent) components[1]).content());
    }
}
