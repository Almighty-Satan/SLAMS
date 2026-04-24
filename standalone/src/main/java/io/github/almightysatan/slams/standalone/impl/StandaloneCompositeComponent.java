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

package io.github.almightysatan.slams.standalone.impl;

import io.github.almightysatan.slams.Component;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.standalone.StandaloneSlams;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ApiStatus.Internal
public class StandaloneCompositeComponent extends CompositeComponent<String> {

    public StandaloneCompositeComponent(@NotNull StandaloneSlams slams, @NotNull String raw, @NotNull PlaceholderResolver placeholderResolver) {
        super(slams, raw, placeholderResolver);
    }

    @Override
    public @NotNull String value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
        StringBuilder stringBuilder = new StringBuilder();
        this.value(placeholderResolver, contexts, stringBuilder::append);
        return stringBuilder.toString();
    }

    @Override
    protected @NotNull CompositeComponent composite(@NotNull StandaloneSlams slams, @NotNull String raw, @NotNull PlaceholderResolver placeholderResolver) {
        return new StandaloneCompositeComponent(slams, raw, placeholderResolver);
    }

    @Override
    protected @NotNull Component.ValueFactory<String> factory() {
        return Component.STRING_FACTORY;
    }

    @Override
    protected @NotNull String merge(@NotNull List<String> values) {
        return String.join("", values);
    }
}
