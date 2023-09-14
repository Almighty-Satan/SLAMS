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

package io.github.almightysatan.slams.parser;

import io.github.almightysatan.slams.LanguageParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JacksonParserTest {

    @Test
    public void testJsonParser() throws IOException {
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("hello", "world");
        expectedResult.put("0.1.2", "3");
        expectedResult.put("abc", Arrays.asList("a", "b", "c"));
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("0", "1");
        innerMap.put("2", "3");
        expectedResult.put("def", innerMap);
        expectedResult.put("ghi", "jkl");

        Map<String, Object> emptyValues = new HashMap<>();
        emptyValues.put("hello", "This value should be ignored");
        emptyValues.put("0.1.2", null);
        emptyValues.put("abc", null);
        emptyValues.put("def", null);
        emptyValues.put("ghi", "jkl");

        LanguageParser parser = JacksonParser.createJsonParser(new File("src/test/resources/test.json"));
        Map<String, Object> result = parser.load(Collections.unmodifiableMap(emptyValues));

        Assertions.assertEquals(expectedResult, result);
    }
}
