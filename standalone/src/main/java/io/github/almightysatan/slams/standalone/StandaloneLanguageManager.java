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

package io.github.almightysatan.slams.standalone;

import io.github.almightysatan.slams.Language;
import io.github.almightysatan.slams.LanguageManager;
import io.github.almightysatan.slams.LanguageParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public interface StandaloneLanguageManager extends LanguageManager {

    @NotNull PlaceholderStyle style();

    static @NotNull StandaloneLanguageManager of(@NotNull LanguageManager languageManager, @NotNull PlaceholderStyle style) {
        Objects.requireNonNull(languageManager);
        Objects.requireNonNull(style);
        return new StandaloneLanguageManager() {
            @Override
            public @NotNull PlaceholderStyle style() {
                return style;
            }

            @Override
            public @NotNull Language load(@NotNull String identifier, @NotNull LanguageParser... parsers) throws IOException {
                return languageManager.load(identifier, parsers);
            }

            @Override
            public void reload() throws IOException {
                languageManager.reload();
            }

            @Override
            public @NotNull Collection<Language> languages() {
                return languageManager.languages();
            }

            @Override
            public @NotNull Language defaultLanguage() {
                return languageManager.defaultLanguage();
            }
        };
    }
}
