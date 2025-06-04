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

import io.github.almightysatan.jaskl.*;
import io.github.almightysatan.slams.LanguageParser;
import io.github.almightysatan.slams.MissingTranslationException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link LanguageParser} that parses messages from different formats using JASKL.
 */
public class JasklParser implements LanguageParser {

    private static final Type<Optional<?>> TYPE = new Type<Optional<?>>() {
        @Override
        public @NotNull Optional<?> toEntryType(@NotNull Object value) throws InvalidTypeException, ValidationException {
            if (value instanceof Optional)
                return (Optional<?>) value;
            return Optional.of(value);
        }

        @Override
        public @NotNull Object toWritable(@NotNull Optional<?> value, @NotNull Function<@NotNull Object, @NotNull Object> keyPreprocessor) throws InvalidTypeException {
            return value.get();
        }
    };

    private final Config config;
    private final boolean enableWrite;
    private final Map<String, ConfigEntry<Optional<?>>> entries = new HashMap<>();

    protected JasklParser(@NotNull Config config, boolean enableWrite) {
        this.config = Objects.requireNonNull(config);
        this.enableWrite = enableWrite;
    }

    @Override
    public void load(@NotNull Values values) throws IOException {
        try {
            for (String path : values.paths()) {
                ConfigEntry<Optional<?>> entry = this.entries.get(path);
                if (entry == null)
                    entries.put(path, ConfigEntry.of(this.config, path, Optional.empty(), TYPE));
                else
                    entry.setValue(Optional.empty());
            }

            this.config.load();

            for (String path : values.paths()) {
                ConfigEntry<Optional<?>> entry = this.entries.get(path);
                Optional<?> value = entry.getValue();
                value.ifPresent(val -> values.put(path, val));
                if (this.enableWrite && !value.isPresent()) {
                    Object currentValue = values.get(path);
                    if (currentValue != null)
                        entry.setValue(Optional.of(currentValue));
                    else
                        throw new MissingTranslationException(path);
                }
            }

            if (this.enableWrite) {
                this.config.write();
                this.config.prune();
            }
        } catch (Throwable t) {
            throw new IOException(t);
        } finally {
            this.config.close();
        }
    }

    /**
     * Creates a new {@link LanguageParser} that uses a JASKL {@link Config} to load messages.
     *
     * @param config the {@link Config}
     * @return a new {@link LanguageParser}
     */
    public static @NotNull LanguageParser createReadParser(@NotNull Config config) {
        return new JasklParser(config, false);
    }

    /**
     * Creates a new {@link LanguageParser} that uses a JASKL {@link Config} to load messages. This parser updates the
     * config to add missing messages and remove unused values. The parser does not change the value of existing
     * messages in the config.
     *
     * @param config the {@link Config}
     * @return a new {@link LanguageParser}
     */
    public static @NotNull LanguageParser createReadWriteParser(@NotNull Config config) {
        return new JasklParser(config, true);
    }

    /**
     * Creates a new {@link LanguageParser} that uses a JASKL {@link Config} to load messages.
     *
     * @param config the {@link Config}
     * @return a new {@link LanguageParser}
     * @deprecated Use {@link JasklParser#createReadParser} instead
     */
    @Deprecated
    public static @NotNull LanguageParser createParser(@NotNull Config config) {
        return createReadParser(config);
    }
}
