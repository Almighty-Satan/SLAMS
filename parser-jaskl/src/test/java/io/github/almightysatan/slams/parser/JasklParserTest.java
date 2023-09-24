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

import io.github.almightysatan.jaskl.hocon.HoconConfig;
import io.github.almightysatan.jaskl.json.JsonConfig;
import io.github.almightysatan.jaskl.yaml.YamlConfig;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class JasklParserTest {

    @Test
    public void testJasklYamlParser() throws IOException {
        ParserTest.testParser(JasklParser.createParser(YamlConfig.of(new File("src/test/resources/test.yaml"))));
    }

    @Test
    public void testJasklHoconParser() throws IOException {
        ParserTest.testParser(JasklParser.createParser(HoconConfig.of(new File("src/test/resources/test.json"))));
    }

    @Test
    public void testJasklJsonParser() throws IOException {
        ParserTest.testParser(JasklParser.createParser(JsonConfig.of(new File("src/test/resources/test.json"))));
    }
}
