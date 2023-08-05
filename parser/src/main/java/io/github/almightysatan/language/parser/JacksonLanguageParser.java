package io.github.almightysatan.language.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.almightysatan.language.LanguageParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class JacksonLanguageParser implements LanguageParser {

    private final File file;
    private final ObjectMapper mapper;

    protected JacksonLanguageParser(@NotNull File file) {
        this.mapper = new ObjectMapper();
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public @NotNull Map<String, Object> load(@NotNull Set<String> paths) throws IOException {

        if (!this.file.exists())
            throw new FileNotFoundException(String.format("Couldn't load language file %s", file));

        ObjectNode root;

        try {
            root = (ObjectNode) this.mapper.readTree(this.file);
        } catch (ClassCastException e) {
            throw new IOException(e);
        }

        final Map<String, Object> entries = new HashMap<>();

        paths.forEach(fullPath -> {
            ObjectNode node = node(root, fullPath);
            if (node != null) entries.put(fullPath, node.asText());
        });

        return entries;

    }

    private ObjectNode node(ObjectNode root, String path) {
        String[] parts = path.split("\\.");
        ObjectNode node = root;
        for (String part : parts) {
            if (node.has(part)) {
                node = (ObjectNode) node.get(part);
            } else {
                return null;
            }
        }
        return node;
    }

}
