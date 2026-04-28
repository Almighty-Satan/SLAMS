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

package io.github.almightysatan.slams.impl;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

@ApiStatus.Internal
public class LazyEvalList<T, U> extends AbstractList<U> {
    
    private final Function<T, U> function;
    private final List<T> raw;
    private final Object[] values;

    public LazyEvalList(@NotNull Function<T, U> function, @NotNull List<@NotNull T> raw) {
        this.function = function;
        this.raw = raw;
        this.values = new Object[raw.size()];
    }

    @SuppressWarnings("unchecked")
    @Override
    public U get(int index) {
        if (this.values[index] != null)
            return (U) this.values[index];
        return (U) (this.values[index] = this.function.apply(this.raw.get(index)));
    }

    @Override
    public int size() {
        return this.values.length;
    }
}
