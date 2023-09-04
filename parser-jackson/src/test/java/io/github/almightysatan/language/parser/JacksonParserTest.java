package io.github.almightysatan.language.parser;

import io.github.almightysatan.language.LanguageParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
