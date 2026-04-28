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
import io.github.almightysatan.slams.standalone.impl.StandaloneCompositeComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

public class CompositeComponentTest {

    private static Stream<Arguments> optimizationArguments() {
        return Stream.of(Arguments.of(false, false), Arguments.of(true, false),
                Arguments.of(false, true), Arguments.of(true, true));
    }

    @ParameterizedTest
    @MethodSource("optimizationArguments")
    public void testBasicPercent(boolean enableConstexprEval, boolean enableInline) {
        StandaloneSlams slams = StandaloneSlams.of("en", PlaceholderStyle.PERCENT, enableConstexprEval, enableInline);
        
        Assertions.assertEquals("Hello World", new StandaloneCompositeComponent(slams, "Hello World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello %test:abc% World", new StandaloneCompositeComponent(slams, "Hello %test:abc% World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello %World%", new StandaloneCompositeComponent(slams, "Hello %World%", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("%Hello% World", new StandaloneCompositeComponent(slams, "%Hello% World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("%Hello World%", new StandaloneCompositeComponent(slams, "%Hello World%", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello%World", new StandaloneCompositeComponent(slams, "Hello%World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("%%", new StandaloneCompositeComponent(slams, "%%", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("%%%", new StandaloneCompositeComponent(slams, "%%%", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("", new StandaloneCompositeComponent(slams, "", PlaceholderResolver.empty()).value());
    }

    @ParameterizedTest
    @MethodSource("optimizationArguments")
    public void testBasicAngleBrackets(boolean enableConstexprEval, boolean enableInline) {
        StandaloneSlams slams = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, enableConstexprEval, enableInline);
        
        Assertions.assertEquals("Hello World", new StandaloneCompositeComponent(slams, "Hello World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello <test:abc> World", new StandaloneCompositeComponent(slams, "Hello <test:abc> World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello <World>", new StandaloneCompositeComponent(slams, "Hello <World>", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<Hello> World", new StandaloneCompositeComponent(slams, "<Hello> World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<Hello World>", new StandaloneCompositeComponent(slams, "<Hello World>", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello<World", new StandaloneCompositeComponent(slams, "Hello<World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello>World", new StandaloneCompositeComponent(slams, "Hello>World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<>", new StandaloneCompositeComponent(slams, "<>", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<", new StandaloneCompositeComponent(slams, "<", PlaceholderResolver.empty()).value());
        Assertions.assertEquals(">", new StandaloneCompositeComponent(slams, ">", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("<<<", new StandaloneCompositeComponent(slams, "<<<", PlaceholderResolver.empty()).value());
        Assertions.assertEquals(">>>", new StandaloneCompositeComponent(slams, ">>>", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("", new StandaloneCompositeComponent(slams, "", PlaceholderResolver.empty()).value());
    }

    @ParameterizedTest
    @MethodSource("optimizationArguments")
    public void testBasicCurlyBrackets(boolean enableConstexprEval, boolean enableInline) {
        StandaloneSlams slams = StandaloneSlams.of("en", PlaceholderStyle.CURLY_BRACKETS, enableConstexprEval, enableInline);
        
        Assertions.assertEquals("Hello World", new StandaloneCompositeComponent(slams, "Hello World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello {test:abc} World", new StandaloneCompositeComponent(slams, "Hello {test:abc} World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello {World}", new StandaloneCompositeComponent(slams, "Hello {World}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{Hello} World", new StandaloneCompositeComponent(slams, "{Hello} World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{Hello World}", new StandaloneCompositeComponent(slams, "{Hello World}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello{World", new StandaloneCompositeComponent(slams, "Hello{World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello}World", new StandaloneCompositeComponent(slams, "Hello}World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{}", new StandaloneCompositeComponent(slams, "{}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{", new StandaloneCompositeComponent(slams, "{", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("}", new StandaloneCompositeComponent(slams, "}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("{{{", new StandaloneCompositeComponent(slams, "{{{", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("}}}", new StandaloneCompositeComponent(slams, "}}}", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("", new StandaloneCompositeComponent(slams, "", PlaceholderResolver.empty()).value());
    }

    @ParameterizedTest
    @MethodSource("optimizationArguments")
    public void testBasicCurlyParentheses(boolean enableConstexprEval, boolean enableInline) {
        StandaloneSlams slams = StandaloneSlams.of("en", PlaceholderStyle.PARENTHESES, enableConstexprEval, enableInline);
        
        Assertions.assertEquals("Hello World", new StandaloneCompositeComponent(slams, "Hello World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello (test:abc) World", new StandaloneCompositeComponent(slams, "Hello (test:abc) World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello (World)", new StandaloneCompositeComponent(slams, "Hello (World)", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("(Hello) World", new StandaloneCompositeComponent(slams, "(Hello) World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("(Hello World)", new StandaloneCompositeComponent(slams, "(Hello World)", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello(World", new StandaloneCompositeComponent(slams, "Hello(World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("Hello)World", new StandaloneCompositeComponent(slams, "Hello)World", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("()", new StandaloneCompositeComponent(slams, "()", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("(", new StandaloneCompositeComponent(slams, "(", PlaceholderResolver.empty()).value());
        Assertions.assertEquals(")", new StandaloneCompositeComponent(slams, ")", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("(((", new StandaloneCompositeComponent(slams, "(((", PlaceholderResolver.empty()).value());
        Assertions.assertEquals(")))", new StandaloneCompositeComponent(slams, ")))", PlaceholderResolver.empty()).value());
        Assertions.assertEquals("", new StandaloneCompositeComponent(slams, "", PlaceholderResolver.empty()).value());
    }
    
    private void assertPlaceholders(Function<String, String> eval) {
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
        Assertions.assertEquals("Hello def", eval.apply("Hello <if:<abc>:<fail>>"));

        // built-in
        Assertions.assertEquals("Hello World", eval.apply("Hello <if_eq:abc:abc:World:Earth>"));
        Assertions.assertEquals("Hello World", eval.apply("Hello <if_num_eq:150:150:World:Earth>"));
        Assertions.assertEquals("Hello Earth", eval.apply("Hello <if_num_eq:100:150:World:Earth>"));
        Assertions.assertEquals("Hello 3", eval.apply("Hello <add:1:2>"));
        Assertions.assertEquals("Hello -1", eval.apply("Hello <sub:1:2>"));
        Assertions.assertEquals("Hello 2", eval.apply("Hello <mul:1:2>"));
        Assertions.assertEquals("Hello 0.5", eval.apply("Hello <div:1:2>"));
    }

    @ParameterizedTest
    @MethodSource("optimizationArguments")
    public void testPlaceholdersGlobal(boolean enableConstexprEval, boolean enableInline) {
        StandaloneSlams slams = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, enableConstexprEval, enableInline);
        PlaceholderResolver placeholderResolver = PlaceholderResolver.builder()
                .constant("abc", "def")
                .constant("a<b", "c")
                .constant("a>b", "d")
                .withArgs("arg", args -> args.get(0))
                .conditional("if", () -> true)
                .conditional("ifn", () -> false)
                .variable("fail", () -> {
                    Assertions.fail();
                    return "fail";
                })
                .builtIn().build();
        Function<String, String> eval = input -> new StandaloneCompositeComponent(slams, input, placeholderResolver).value(PlaceholderResolver.empty(), new Object[0]);
        this.assertPlaceholders(eval);
    }

    @ParameterizedTest
    @MethodSource("optimizationArguments")
    public void testPlaceholdersLocal(boolean enableConstexprEval, boolean enableInline) {
        StandaloneSlams slams = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, enableConstexprEval, enableInline);
        PlaceholderResolver placeholderResolver = PlaceholderResolver.builder()
                .constant("abc", "def")
                .constant("a<b", "c")
                .constant("a>b", "d")
                .withArgs("arg", args -> args.get(0))
                .conditional("if", () -> true)
                .conditional("ifn", () -> false)
                .variable("fail", () -> {
                    Assertions.fail();
                    return "fail";
                })
                .builtIn().build();
        Function<String, String> eval = input -> new StandaloneCompositeComponent(slams, input, PlaceholderResolver.empty()).value(placeholderResolver, new Object[0]);
        this.assertPlaceholders(eval);
    }

    @Test
    public void testCaching() {
        StandaloneSlams slams = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS);
        
        boolean[] resolved = new boolean[]{false};
        PlaceholderResolver placeholderResolver = key -> {
            if (resolved[0])
                Assertions.fail();
            resolved[0] = true;
            return Placeholder.constant("hello", "world");
        };

        StandaloneCompositeComponent component = new StandaloneCompositeComponent(slams, "<hello>", placeholderResolver);

        Assertions.assertEquals("world", component.value(placeholderResolver, new Object[0]));
        Assertions.assertEquals("world", component.value(placeholderResolver, new Object[0]));
        Assertions.assertEquals("world", component.value(placeholderResolver, new Object[0]));
    }

    @Test
    public void testInline() {
        StandaloneSlams slams0 = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, false, false);
        StandaloneCompositeComponent component0 = new StandaloneCompositeComponent(slams0, "<hello><hello>", Placeholder.constant("hello", "world"));
        Assertions.assertEquals("worldworld", component0.value(PlaceholderResolver.empty(), new Object[0]));
        Assertions.assertEquals(2, component0.size());

        StandaloneSlams slams1 = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, true, false);
        StandaloneCompositeComponent component1 = new StandaloneCompositeComponent(slams1, "<hello><hello>", Placeholder.constant("hello", "world"));
        Assertions.assertEquals("worldworld", component1.value(PlaceholderResolver.empty(), new Object[0]));
        Assertions.assertEquals(2, component1.size());

        StandaloneSlams slams2 = StandaloneSlams.of("en", PlaceholderStyle.ANGLE_BRACKETS, true, true);
        StandaloneCompositeComponent component2 = new StandaloneCompositeComponent(slams2, "<hello><hello>", Placeholder.constant("hello", "world"));
        Assertions.assertEquals("worldworld", component2.value(PlaceholderResolver.empty(), new Object[0]));
        Assertions.assertEquals(1, component2.size());
    }
}
