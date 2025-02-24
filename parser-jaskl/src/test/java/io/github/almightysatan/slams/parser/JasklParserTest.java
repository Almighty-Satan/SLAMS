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

import io.github.almightysatan.jaskl.hocon.HoconConfig;
import io.github.almightysatan.jaskl.json.JsonConfig;
import io.github.almightysatan.jaskl.yaml.YamlConfig;
import io.github.almightysatan.slams.LanguageParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class JasklParserTest {

    @Test
    public void testJasklYamlParser() throws IOException {
        ParserTest.testRead(JasklParser.createReadParser(YamlConfig.of(new File("src/test/resources/test.yaml"))));
    }

    @Test
    public void testJasklHoconParser() throws IOException {
        ParserTest.testRead(JasklParser.createReadParser(HoconConfig.of(new File("src/test/resources/test.json"))));
    }

    @Test
    public void testJasklJsonParser() throws IOException {
        ParserTest.testRead(JasklParser.createReadParser(JsonConfig.of(new File("src/test/resources/test.json"))));
    }

    @Test
    public void testJasklYamlParserWrite() throws IOException {
        File file = new File("build/test/test.yaml");
        file.getParentFile().mkdirs();
        Files.copy(new File("src/test/resources/test.yaml").toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        LanguageParser parser = JasklParser.createReadWriteParser(YamlConfig.of(file));
        ParserTest.testWrite(parser);
    }

    @Test
    public void testJasklHoconParserWrite() throws IOException {
        File file = new File("build/test/test.hocon");
        file.getParentFile().mkdirs();
        Files.copy(new File("src/test/resources/test.json").toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        LanguageParser parser = JasklParser.createReadWriteParser(HoconConfig.of(file));
        ParserTest.testWrite(parser);
    }

    @Test
    public void testJasklJsonParserWrite() throws IOException {
        File file = new File("build/test/test.json");
        file.getParentFile().mkdirs();
        Files.copy(new File("src/test/resources/test.json").toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        LanguageParser parser = JasklParser.createReadWriteParser(JsonConfig.of(file));
        ParserTest.testWrite(parser);
    }
}
