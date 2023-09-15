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

import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.LanguageParser;
import io.github.almightysatan.slams.impl.SlamsInternal;
import io.github.almightysatan.slams.impl.MessageImpl;
import io.github.almightysatan.slams.impl.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public interface StandaloneSlams extends Slams {

    @NotNull PlaceholderStyle style();

    static @NotNull StandaloneSlams of(@NotNull Slams slams, @NotNull PlaceholderStyle style) {
        Objects.requireNonNull(slams);
        Objects.requireNonNull(style);
        class StandaloneSlamsImpl implements SlamsInternal, StandaloneSlams {
            @Override
            public @NotNull PlaceholderStyle style() {
                return style;
            }

            @Override
            public void register(@NotNull MessageImpl<?, ?, ?> entry) {
                ((SlamsInternal) slams).register(entry);
            }

            @Override
            public @NotNull @Unmodifiable Set<@NotNull String> paths() {
                return ((SlamsInternal) slams).paths();
            }

            @Override
            public void load(@NotNull String identifier, @NotNull LanguageParser... parsers) throws IOException {
                slams.load(identifier, parsers);
            }

            @Override
            public void reload() throws IOException {
                slams.reload();
            }

            @Override
            public @NotNull Collection<@NotNull String> languages() {
                return slams.languages();
            }

            @Override
            public @NotNull String defaultLanguageIdentifier() {
                return slams.defaultLanguageIdentifier();
            }

            @Override
            public @Nullable Language language(@NotNull String identifier) {
                return ((SlamsInternal) slams).language(identifier);
            }

            @Override
            public @NotNull Language defaultLanguage() {
                return ((SlamsInternal) slams).defaultLanguage();
            }
        }
        return new StandaloneSlamsImpl();
    }
}
