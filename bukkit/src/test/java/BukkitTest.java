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

import io.github.almightysatan.slams.*;
import io.github.almightysatan.slams.bukkit.BukkitMessage;
import io.github.almightysatan.slams.bukkit.BukkitPlaceholders;
import io.github.almightysatan.slams.bukkit.impl.BukkitCompositeComponent;
import io.github.almightysatan.slams.standalone.PlaceholderStyle;
import io.github.almightysatan.slams.standalone.StandaloneSlams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BukkitTest {

    @Test
    public void testBasic() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        BukkitMessage entry = BukkitMessage.of("test", slams);

        slams.load("0", values -> values.put("test", "1234"));

        BaseComponent[] value = entry.value();
        assertEquals(1, value.length);
        assertEquals("1234", ((TextComponent) value[0]).getText());
        assertMessageEquals("1234", value);
    }

    @Test
    public void testDefaultLanguageSelection() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("1");
        BukkitMessage entry = BukkitMessage.of("test", slams);

        slams.load("0", values -> values.put("test", "123"));
        slams.load("1", values -> values.put("test", "456"));

        BaseComponent[] value = entry.value();
        assertMessageEquals("456", value);
    }

    @Test
    public void testContextLanguageSelection() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        BukkitMessage entry = BukkitMessage.of("test", slams);

        slams.load("0", values -> values.put("test", "123"));
        slams.load("1", values -> values.put("test", "456"));

        BaseComponent[] value = entry.value("1", new TestContext("YXZ"));
        assertMessageEquals("456", value);
    }

    @Test
    public void testLocalPlaceholder() throws IOException {
        @SuppressWarnings("deprecation")
        StandaloneSlams slams = StandaloneSlams.of(Slams.of("0"), PlaceholderStyle.PERCENT);
        BukkitMessage entry = BukkitMessage.of("test", slams);

        slams.load("0", values -> values.put("test", "Hello %xxx%"));

        BaseComponent[] value = entry.value(Placeholder.constant("xxx", "World"));
        assertEquals("Hello World", BaseComponent.toPlainText(value));
    }

    @Test
    public void testPlaceholder() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        BukkitMessage entry = BukkitMessage.of("test", slams, PlaceholderResolver.of(Placeholder.constant("test", "World")));

        slams.load("0", values -> values.put("test", "Hello <test>"));

        BaseComponent[] value = entry.value();
        assertEquals("Hello World", BaseComponent.toPlainText(value));
    }

    @Test
    public void testContextPlaceholder() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        TestContext context = new TestContext("World");
        PlaceholderResolver placeholder = PlaceholderResolver.builder()
                .contextual("test", TestContext.class, TestContext::getName)
                .namespace("abc-", TestContext.class, ctx -> new TestContext2(ctx.getName()), builder -> {
                    builder.contextual("test", TestContext2.class, TestContext2::getName);
                }).build();
        BukkitMessage entry = BukkitMessage.of("test", slams, placeholder);
        BukkitMessage entry2 = BukkitMessage.of("test2", slams, placeholder);

        slams.load("0", values -> {
            values.put("test", "Hello <test>");
            values.put("test2", "Hello <abc-test>");
        });

        BaseComponent[] value = entry.value(context);
        assertEquals("Hello World", BaseComponent.toPlainText(value));
        BaseComponent[] value2 = entry.value(context);
        assertEquals("Hello World", BaseComponent.toPlainText(value));
    }

    @Test
    public void testPlaceholderArgument() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        BukkitMessage entry = BukkitMessage.of("test", slams, PlaceholderResolver.of(Placeholder.withArgs("test", (arguments) -> arguments.get(1))));

        slams.load("0", values -> values.put("test", "Hello <test:some other argument:World>"));

        BaseComponent[] value = entry.value();
        assertEquals("Hello World", BaseComponent.toPlainText(value));
    }

    private void testColor(String expected, String input) throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        BukkitMessage entry = BukkitMessage.of("test", slams, PlaceholderResolver.builtInPlaceholders());

        slams.load("0", values -> {
            values.put("test", input);
        });

        BaseComponent[] value = entry.value();
        assertEquals(expected, BaseComponent.toLegacyText(value));
    }

    @Test
    public void testColors() throws IOException {
        testColor("§fHello §3World!", "Hello §3World!");
        testColor("§fHello§3 §3World§3!", "Hello§3 <if_eq:0:0:World:ERROR>!");
        testColor("§1Hello §3§3World§3!", "§1Hello §3<if_eq:0:0:World:ERROR>!");
        testColor("§fHello§3 §3Wor§3ld§3!", "Hello§3 <if_eq:0:0:Wor<if_eq:0:0:ld:ERROR>:ERROR>!");
        testColor("§fHello§3 §3Wor§1ld§1!", "Hello§3 <if_eq:0:0:Wor§1ld:ERROR>!");
        testColor("§1Hello §fWorld!", "§1Hello §rWorld!");
        testColor("§1§lHello §fWorld!", "§1§lHello §rWorld!");
    }

    @Test
    public void testParse() throws IOException {
        assertEquals(1, BukkitCompositeComponent.parse("").length);
        
        BaseComponent[] value = BukkitCompositeComponent.parse("§1§lHello World");
        assertEquals(1, value.length);
        assertEquals("Hello World", ((TextComponent) value[0]).getText());
        assertEquals(ChatColor.DARK_BLUE, ((TextComponent) value[0]).getColor());
        assertTrue(((TextComponent) value[0]).isBold());
    }

    @Test
    public void testRepeat() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        BukkitMessage entry = BukkitMessage.of("test", slams);

        slams.load("0", values -> {
            values.put("test", "Hello <test>!");
        });

        BaseComponent[] value = entry.value(Placeholder.constant("test", "§1World"));
        assertEquals("§fHello §1World§1!", BaseComponent.toLegacyText(value));

        BaseComponent[] value2 = entry.value(Placeholder.constant("test", "World"));
        assertEquals("§fHello §fWorld§f!", BaseComponent.toLegacyText(value2));
    }

    @Test
    public void testBuiltInPlaceholders() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        BukkitMessage entry = BukkitMessage.of("test", slams, BukkitPlaceholders.create());

        slams.load("0", values -> values.put("test", "Hello <click_run_command:test:Wo<hover_show_text:test2:rld>>!"));

        BaseComponent[] value = entry.value();
        assertEquals("Hello World!", BaseComponent.toPlainText(value));
        assertEquals(4, value.length);
        assertNull(((TextComponent) value[0]).getClickEvent());
        assertNull(((TextComponent) value[0]).getHoverEvent());
        assertNotNull(((TextComponent) value[1]).getClickEvent());
        assertEquals("test", ((TextComponent) value[1]).getClickEvent().getValue());
        assertNull(((TextComponent) value[1]).getHoverEvent());
        assertNotNull(((TextComponent) value[2]).getClickEvent());
        assertEquals("test", ((TextComponent) value[2]).getClickEvent().getValue());
        assertNotNull(((TextComponent) value[2]).getHoverEvent());
        assertEquals("test2", BaseComponent.toPlainText(((TextComponent) value[2]).getHoverEvent().getValue()));
        assertNull(((TextComponent) value[3]).getClickEvent());
        assertNull(((TextComponent) value[3]).getHoverEvent());
    }

    @Test
    public void testBuiltInPlaceholdersNoInline() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0", PlaceholderStyle.ANGLE_BRACKETS, false, false);
        BukkitMessage entry = BukkitMessage.of("test", slams, BukkitPlaceholders.create());

        slams.load("0", values -> values.put("test", "Hello <click_run_command:test:Wo<hover_show_text:test2:rld>>!"));

        BaseComponent[] value = entry.value();
        assertEquals("Hello World!", BaseComponent.toPlainText(value));
        assertEquals(4, value.length);
        assertNull(((TextComponent) value[0]).getClickEvent());
        assertNull(((TextComponent) value[0]).getHoverEvent());
        assertNotNull(((TextComponent) value[1]).getClickEvent());
        assertEquals("test", ((TextComponent) value[1]).getClickEvent().getValue());
        assertNull(((TextComponent) value[1]).getHoverEvent());
        assertNotNull(((TextComponent) value[2]).getClickEvent());
        assertEquals("test", ((TextComponent) value[2]).getClickEvent().getValue());
        assertNotNull(((TextComponent) value[2]).getHoverEvent());
        assertEquals("test2", BaseComponent.toPlainText(((TextComponent) value[2]).getHoverEvent().getValue()));
        assertNull(((TextComponent) value[3]).getClickEvent());
        assertNull(((TextComponent) value[3]).getHoverEvent());
    }

    @Test
    public void testStringValue() throws IOException {
        StandaloneSlams slams = StandaloneSlams.of("0");
        BukkitMessage entry0 = BukkitMessage.of("test0", slams);
        BukkitMessage entry1 = BukkitMessage.of("test1", slams, Placeholder.constant("test", "§1World"));
        BukkitMessage entry2 = BukkitMessage.of("test2", slams, PlaceholderResolver.of(Placeholder.constant("test", "§1World"), BukkitPlaceholders.create()));
        BukkitMessage entry3 = BukkitMessage.of("test3", slams, Placeholder.constant("test", "World"));
        

        slams.load("0", values -> {
            values.put("test0", "Hello <test>!");
            values.put("test1", "Hello <test>!");
            values.put("test2", "Hello <click_run_command:test:<hover_show_text:test2:<test>>>!");
            values.put("test3", "Hello §1<test>!");
        });

        assertEquals("Hello §1World!", entry0.stringValue(Placeholder.constant("test", "§1World")));
        assertEquals("Hello §1World!", entry1.stringValue());
        assertEquals("Hello §1World!", entry2.stringValue());
        assertEquals("Hello §1World!", entry3.stringValue());
    }
    
    private void assertMessageEquals(BaseComponent[] expected, BaseComponent[] actual) {
        // Compares the result of toString as TextComponent does not implement an equals method
        assertArrayEquals(Arrays.stream(expected).map(BaseComponent::toString).toArray(String[]::new), Arrays.stream(actual).map(BaseComponent::toString).toArray(String[]::new));
    }

    private void assertMessageEquals(String legacyText, BaseComponent[] actual) {
        this.assertMessageEquals(TextComponent.fromLegacyText(legacyText), actual);
    }
}
