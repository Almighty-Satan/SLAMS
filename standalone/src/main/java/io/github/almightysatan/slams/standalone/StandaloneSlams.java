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

package io.github.almightysatan.slams.standalone;

import io.github.almightysatan.slams.LanguageParser;
import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.impl.Language;
import io.github.almightysatan.slams.impl.MessageImpl;
import io.github.almightysatan.slams.impl.SlamsInternal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * An extension of the {@link Slams} interface that contains additional methods for the standalone format.
 * 
 * @see #of(Slams, PlaceholderStyle, boolean, boolean)
 */
public interface StandaloneSlams extends Slams {

    /**
     * Returns the {@link PlaceholderStyle}.
     *
     * @return the {@link PlaceholderStyle}
     */
    @NotNull PlaceholderStyle style();

    /**
     * Whether constant components (i.e. components/placeholders that do not depend on any contexts) should be evaluated
     * when the message is loaded to avoid having to re-evaluate the value every time the message value is computed.
     * {@code true} to enable. This should be treated as a suggestion, not a requirement.
     * Some optimizations may still be performed, even if this setting is disabled.
     *
     * @return {@code true} to enable optimization
     */
    boolean enableConstexprEval();

    /**
     * Whether eligible components of the message should be inlined when loading. {@code true} to enable.
     * This should be treated as a suggestion, not a requirement.
     * Some optimizations may still be performed, even if this setting is disabled.
     *
     * @return {@code true} to enable optimization
     */
    boolean enableInline();

    /**
     * Creates a new {@link StandaloneSlams} from the given {@link Slams} and {@link PlaceholderStyle}.
     *
     * @param slams the Slams instance
     * @param style the PlaceholderStyle
     * @param enableConstexprEval {@code true} to enable optimization
     * @param enableInline {@code true} to enable optimization
     * @return a new {@link StandaloneSlams} instance
     */
    static @NotNull StandaloneSlams of(@NotNull Slams slams, @NotNull PlaceholderStyle style,
            boolean enableConstexprEval, boolean enableInline) {
        Objects.requireNonNull(slams);
        Objects.requireNonNull(style);
        class StandaloneSlamsImpl implements SlamsInternal, StandaloneSlams {
            @Override
            public @NotNull PlaceholderStyle style() {
                return style;
            }

            @Override
            public boolean enableConstexprEval() {
                return enableConstexprEval;
            }

            @Override
            public boolean enableInline() {
                return enableInline;
            }

            @Override
            public void register(@NotNull MessageImpl<?> entry) {
                ((SlamsInternal) slams).register(entry);
            }

            @Override
            public @NotNull @Unmodifiable Set<@NotNull String> paths() {
                return ((SlamsInternal) slams).paths();
            }

            @Override
            public void load(@NotNull String identifier, @NotNull LanguageParser @NotNull ... parsers) throws IOException {
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

    /**
     * Creates a new {@link StandaloneSlams} from the given {@link Slams} and {@link PlaceholderStyle}.
     *
     * @param slams the Slams instance
     * @param style the PlaceholderStyle
     * @return a new {@link StandaloneSlams} instance
     */
    static @NotNull StandaloneSlams of(@NotNull Slams slams, @NotNull PlaceholderStyle style) {
        return of(slams, style, true, true);
    }

    /**
     * Creates a new {@link StandaloneSlams} instance.
     *
     * @param defaultLanguageIdentifier the identifier of the default language
     * @param style the PlaceholderStyle
     * @param enableConstexprEval {@code true} to enable optimization
     * @param enableInline {@code true} to enable optimization
     * @return a new {@link StandaloneSlams} instance
     */
    static @NotNull StandaloneSlams of(@NotNull String defaultLanguageIdentifier, @NotNull PlaceholderStyle style, boolean enableConstexprEval, boolean enableInline) {
        return of(Slams.of(defaultLanguageIdentifier), style, enableConstexprEval, enableInline);
    }

    /**
     * Creates a new {@link StandaloneSlams} from the given default language identifier and {@link PlaceholderStyle}.
     *
     * @param defaultLanguageIdentifier the identifier of the default language
     * @param style the PlaceholderStyle
     * @return a new {@link StandaloneSlams} instance
     */
    static @NotNull StandaloneSlams of(@NotNull String defaultLanguageIdentifier, @NotNull PlaceholderStyle style) {
        return of(Slams.of(defaultLanguageIdentifier), style);
    }

    /**
     * Creates a new {@link StandaloneSlams} from the given default language identifier.
     *
     * @param defaultLanguageIdentifier the identifier of the default language
     * @return a new {@link StandaloneSlams} instance
     */
    static @NotNull StandaloneSlams of(@NotNull String defaultLanguageIdentifier) {
        return of(defaultLanguageIdentifier, PlaceholderStyle.ANGLE_BRACKETS);
    }
}
