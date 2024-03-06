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

package io.github.almightysatan.slams.standalone;

import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class CompositeComponentTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testBasicPercent() {
        Assertions.assertEquals("Hello World", new CompositeComponent(PlaceholderStyle.PERCENT, "Hello World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello %test:abc% World", new CompositeComponent(PlaceholderStyle.PERCENT, "Hello %test:abc% World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello %World%", new CompositeComponent(PlaceholderStyle.PERCENT, "Hello %World%", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("%Hello% World", new CompositeComponent(PlaceholderStyle.PERCENT, "%Hello% World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("%Hello World%", new CompositeComponent(PlaceholderStyle.PERCENT, "%Hello World%", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello%World", new CompositeComponent(PlaceholderStyle.PERCENT, "Hello%World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("%%", new CompositeComponent(PlaceholderStyle.PERCENT, "%%", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("%%%", new CompositeComponent(PlaceholderStyle.PERCENT, "%%%", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("", new CompositeComponent(PlaceholderStyle.PERCENT, "", PlaceholderResolver.empty()).value());
    }

    @Test
    public void testBasicAngleBrackets() {
        Assertions.assertEquals("Hello World", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "Hello World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello <test:abc> World", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "Hello <test:abc> World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello <World>", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "Hello <World>", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<Hello> World", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "<Hello> World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<Hello World>", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "<Hello World>", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello<World", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "Hello<World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello>World", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "Hello>World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<>", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "<>", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "<", PlaceholderResolver.empty()).value());
        Assertions.assertEquals(">", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, ">", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<<<", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "<<<", PlaceholderResolver.empty()).value());
        Assertions.assertEquals(">>>", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, ">>>", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("", new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "", PlaceholderResolver.empty()).value());
    }

    @Test
    public void testBasicCurlyBrackets() {
        Assertions.assertEquals("Hello World", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "Hello World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello {test:abc} World", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "Hello {test:abc} World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello {World}", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "Hello {World}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{Hello} World", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "{Hello} World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{Hello World}", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "{Hello World}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello{World", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "Hello{World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello}World", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "Hello}World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{}", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "{}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "{", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("}", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{{{", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "{{{", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("}}}", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "}}}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("", new CompositeComponent(PlaceholderStyle.CURLY_BRACKETS, "", PlaceholderResolver.empty()).value());
    }

    @Test
    public void testBasicCurlyParentheses() {
        Assertions.assertEquals("Hello World", new CompositeComponent(PlaceholderStyle.PARENTHESES, "Hello World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello (test:abc) World", new CompositeComponent(PlaceholderStyle.PARENTHESES, "Hello (test:abc) World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello (World)", new CompositeComponent(PlaceholderStyle.PARENTHESES, "Hello (World)", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("(Hello) World", new CompositeComponent(PlaceholderStyle.PARENTHESES, "(Hello) World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("(Hello World)", new CompositeComponent(PlaceholderStyle.PARENTHESES, "(Hello World)", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello(World", new CompositeComponent(PlaceholderStyle.PARENTHESES, "Hello(World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello)World", new CompositeComponent(PlaceholderStyle.PARENTHESES, "Hello)World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("()", new CompositeComponent(PlaceholderStyle.PARENTHESES, "()", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("(", new CompositeComponent(PlaceholderStyle.PARENTHESES, "(", PlaceholderResolver.empty()).value());
        Assertions.assertEquals(")", new CompositeComponent(PlaceholderStyle.PARENTHESES, ")", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("(((", new CompositeComponent(PlaceholderStyle.PARENTHESES, "(((", PlaceholderResolver.empty()).value());
        Assertions.assertEquals(")))", new CompositeComponent(PlaceholderStyle.PARENTHESES, ")))", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("", new CompositeComponent(PlaceholderStyle.PARENTHESES, "", PlaceholderResolver.empty()).value());
    }

    @Test
    public void testPlaceholders() {
        PlaceholderResolver placeholderResolver = PlaceholderResolver.of(
                Placeholder.constant("abc", "def"),
                Placeholder.constant("a<b", "c"),
                Placeholder.constant("a>b", "d"),
                Placeholder.withArgs("arg", args -> args.get(0)),
                Placeholder.conditional("if", () -> true),
                Placeholder.conditional("ifn", () -> false));
        Function<String, String> eval = input -> new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, input, PlaceholderResolver.empty()).value(null, placeholderResolver);

        Assertions.assertEquals("", eval.apply(""));
        Assertions.assertEquals("def", eval.apply("<abc>"));
        Assertions.assertEquals("Hello def", eval.apply("Hello <abc>"));
        Assertions.assertEquals("Hello <>", eval.apply("Hello <>"));
        Assertions.assertEquals("Hello <> <>", eval.apply("Hello <> <>"));
        Assertions.assertEquals("Hello <abc", eval.apply("Hello <abc"));
        Assertions.assertEquals("def Hello", eval.apply("<abc> Hello"));
        Assertions.assertEquals("def Hello def", eval.apply("<abc> Hello <abc>"));
        Assertions.assertEquals("xx def Hello def xx", eval.apply("xx <abc> Hello <abc> xx"));

        // arguments
        Assertions.assertEquals("Hello World", eval.apply("Hello <arg:World>"));
        Assertions.assertEquals("Hello World", eval.apply("<arg:Hello> World"));
        Assertions.assertEquals("Hello World", eval.apply("Hello <arg:World:Earth>"));
        Assertions.assertEquals("Hello World", eval.apply("<arg:Hello:Bye::> World"));

        // escape
        Assertions.assertEquals("Hello <abc>", eval.apply("Hello \\<abc>"));
        Assertions.assertEquals("Hello <abc>", eval.apply("Hello <abc\\>"));
        Assertions.assertEquals("Hello \\ def", eval.apply("Hello \\\\ <abc>"));
        Assertions.assertEquals("Hello def", eval.apply("Hello <abc>\\"));
        Assertions.assertEquals("Hello def\\", eval.apply("Hello <abc>\\\\"));
        Assertions.assertEquals("Hello \\def", eval.apply("Hello \\\\<abc>"));
        Assertions.assertEquals("Hello \\<abc>", eval.apply("Hello \\\\\\<abc>"));
        Assertions.assertEquals("Hello < def", eval.apply("Hello \\< <abc>"));
        Assertions.assertEquals("Hello <a<b> ", eval.apply("Hello <a<b> "));
        Assertions.assertEquals("Hello c ", eval.apply("Hello <a\\<b> "));
        Assertions.assertEquals("Hello d ", eval.apply("Hello <a\\>b> "));

        // missing placeholder
        Assertions.assertEquals("Hello <test> World", eval.apply("Hello <test> World"));
        Assertions.assertEquals("Hello <test:abc:def> World", eval.apply("Hello <test:abc:def> World"));

        // conditional
        Assertions.assertEquals("Hello World", eval.apply("Hello <if:World:Earth>"));
        Assertions.assertEquals("Hello Earth", eval.apply("Hello <ifn:World:Earth>"));
        Assertions.assertEquals("Hello def", eval.apply("Hello <if:<abc>:fail>"));
    }

    @Test
    public void testCaching() {
        boolean[] resolved = new boolean[]{false};
        PlaceholderResolver placeholderResolver = key -> {
            if (resolved[0])
                Assertions.fail();
            resolved[0] = true;
            return Placeholder.constant("hello", "world");
        };

        CompositeComponent component = new CompositeComponent(PlaceholderStyle.ANGLE_BRACKETS, "<hello>", placeholderResolver);

        Assertions.assertEquals("world", component.value(null, placeholderResolver));
        Assertions.assertEquals("world", component.value(null, placeholderResolver));
        Assertions.assertEquals("world", component.value(null, placeholderResolver));
    }
}
