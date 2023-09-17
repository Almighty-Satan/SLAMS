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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.almightysatan.slams.LanguageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A {@link LanguageParser} that parses message from different formats (e.g. JSON) using Jackson.
 */
public class JacksonParser implements LanguageParser {

    private final ObjectMapper mapper;
    private final DataSource dataSource;

    /**
     * Creates a new {@link JacksonParser} from the given {@link ObjectMapper} and {@link DataSource}.
     *
     * @param objectMapper the {@link ObjectMapper}
     * @param dataSource the {@link DataSource}
     */
    protected JacksonParser(@NotNull ObjectMapper objectMapper, @NotNull DataSource dataSource) {
        this.mapper = Objects.requireNonNull(objectMapper);
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public void load(@NotNull Values values) throws IOException {
        JsonNode root = this.dataSource.readTree(this.mapper);

        for (String path : values.paths()) {
            JsonNode node = resolveNode(root, path);
            if (node != null)
                values.put(path, this.mapper.treeToValue(node, Object.class));
        }
    }

    /**
     * Finds the {@link JsonNode} with the given path.
     *
     * @param root the root node
     * @param path the path
     * @return the node or {@code null}
     */
    protected @Nullable JsonNode resolveNode(@NotNull JsonNode root, @NotNull String path) {
        String[] parts = path.split("\\.");
        JsonNode node = root;
        for (String part : parts) {
            if (node == null)
                return null;
            node = node.get(part);
        }
        return node;
    }

    @FunctionalInterface
    protected interface DataSource {
        @Nullable JsonNode readTree(@NotNull ObjectMapper mapper) throws IOException;
    }

    /**
     * Creates a new {@link LanguageParser} that will use the given {@link ObjectMapper} to read messages from a given
     * {@link File}.
     *
     * @param objectMapper the {@link ObjectMapper}
     * @param file {@link File}
     * @return a new {@link LanguageParser}
     */
    public static @NotNull LanguageParser createParser(@NotNull ObjectMapper objectMapper, @NotNull File file) {
        Objects.requireNonNull(objectMapper);
        Objects.requireNonNull(file);
        return new JacksonParser(objectMapper, mapper -> {
            if (!file.exists())
                throw new FileNotFoundException(String.format("Couldn't load language file %s", file));
            return mapper.readTree(file);
        });
    }

    /**
     * Creates a new {@link LanguageParser} that will use the given {@link ObjectMapper} to read messages from a given
     * {@link InputStream}.
     *
     * @param objectMapper the {@link ObjectMapper}
     * @param inputStream {@link InputStream}
     * @return a new {@link LanguageParser}
     */
    public static @NotNull LanguageParser createParser(@NotNull ObjectMapper objectMapper, @NotNull InputStream inputStream) {
        Objects.requireNonNull(objectMapper);
        Objects.requireNonNull(inputStream);
        return new JacksonParser(objectMapper, mapper -> mapper.readTree(inputStream));
    }

    /**
     * Creates a new {@link LanguageParser} that will use Jackson's {@link JsonMapper} to read messages from a given
     * {@link File}.
     *
     * @param file {@link File}
     * @return a new {@link LanguageParser}
     */
    public static @NotNull LanguageParser createJsonParser(@NotNull File file) {
        return createParser(new JsonMapper(), file);
    }

    /**
     * Creates a new {@link LanguageParser} that will use Jackson's {@link JsonMapper} to read messages from a given
     * {@link InputStream}.
     *
     * @param inputStream {@link InputStream}
     * @return a new {@link LanguageParser}
     */
    public static @NotNull LanguageParser createJsonParser(@NotNull InputStream inputStream) {
        return createParser(new JsonMapper(), inputStream);
    }
}
