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

package io.github.almightysatan.slams.parser;

import io.github.almightysatan.slams.LanguageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.*;

public class ParserTest {

    public static void testParser(@NotNull LanguageParser parser) throws IOException {
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("hello", "world");
        expectedResult.put("0.1.2", "3");
        expectedResult.put("abc", Arrays.asList("a", "b", "c"));
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("0", "1");
        innerMap.put("2", "3");
        expectedResult.put("def", innerMap);
        expectedResult.put("ghi", "jkl");

        Map<String, Object> result = new HashMap<>();
        // Let's pretend these values were loaded by another parser
        result.put("hello", "This value should be ignored");
        result.put("ghi", "jkl");

        LanguageParser.Values values = new LanguageParser.Values() {
            @Override
            public @NotNull @Unmodifiable Set<@NotNull String> paths() {
                return Collections.unmodifiableSet(expectedResult.keySet());
            }

            @Override
            public @Nullable Object get(@NotNull String key) {
                return result.get(key);
            }

            @Override
            public void put(@NotNull String key, @NotNull Object value) {
                if (!expectedResult.containsKey(key))
                    Assertions.fail();
                result.put(key, value);
            }
        };

        parser.load(values);
        Assertions.assertEquals(expectedResult, result);

        expectedResult.put("xxx", "yyy");

        parser.load(values);
        Assertions.assertEquals(expectedResult, result);
    }
}
