package io.github.almightysatan.language.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.almightysatan.language.LanguageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class JacksonParser implements LanguageParser {

    private final ObjectMapper mapper;
    private final DataSource dataSource;

    protected JacksonParser(@NotNull ObjectMapper objectMapper, @NotNull DataSource dataSource) {
        this.mapper = Objects.requireNonNull(objectMapper);
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public @NotNull Map<String, Object> load(@NotNull Map<String, Object> entries) throws IOException {
        JsonNode root = this.dataSource.readTree(this.mapper);

        final Map<String, Object> newEntries = new HashMap<>();
        for (Entry<String, Object> entry : entries.entrySet()) {
            JsonNode node = resolveNode(root, entry.getKey());
            newEntries.put(entry.getKey(), node != null ? this.mapper.treeToValue(node, Object.class) : entry.getValue());
        }

        return Collections.unmodifiableMap(newEntries);
    }

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
        JsonNode readTree(ObjectMapper mapper) throws IOException;
    }

    public static @NotNull LanguageParser createParser(@NotNull ObjectMapper objectMapper, @NotNull File file) {
        Objects.requireNonNull(objectMapper);
        Objects.requireNonNull(file);
        return new JacksonParser(objectMapper, mapper -> {
            if (!file.exists())
                throw new FileNotFoundException(String.format("Couldn't load language file %s", file));
            return mapper.readTree(file);
        });
    }

    public static @NotNull LanguageParser createParser(@NotNull ObjectMapper objectMapper, @NotNull InputStream inputStream) {
        Objects.requireNonNull(objectMapper);
        Objects.requireNonNull(inputStream);
        return new JacksonParser(objectMapper, mapper -> mapper.readTree(inputStream));
    }

    public static @NotNull LanguageParser createJsonParser(@NotNull File file) {
        return createParser(new JsonMapper(), file);
    }

    public static @NotNull LanguageParser createJsonParser(@NotNull InputStream inputStream) {
        return createParser(new JsonMapper(), inputStream);
    }
}
