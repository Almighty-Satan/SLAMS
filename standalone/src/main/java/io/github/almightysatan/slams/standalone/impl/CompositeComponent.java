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
import io.github.almightysatan.slams.Placeholder;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.impl.LazyEvalList;
import io.github.almightysatan.slams.standalone.PlaceholderStyle;
import io.github.almightysatan.slams.standalone.StandaloneSlams;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@ApiStatus.Internal
public abstract class CompositeComponent<T> implements Component<T> {

    protected final Component<T>[] components;
    protected final boolean constexpr;

    protected CompositeComponent(@NotNull StandaloneSlams slams, @NotNull String raw, @NotNull PlaceholderResolver placeholderResolver) {
        Objects.requireNonNull(slams);
        Objects.requireNonNull(raw);
        Objects.requireNonNull(placeholderResolver);
        this.components = this.processString(slams, raw, placeholderResolver);
        this.constexpr = Arrays.stream(this.components).allMatch(Component::constexpr);
    }

    @Override
    public void value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts, @NotNull Consumer<T> consumer) {
        for (Component<T> component : this.components) {
            component.value(placeholderResolver, contexts, value -> consumer.accept(value));
        }
    }

    public @NotNull T value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
        return this.value(placeholderResolver, contexts);
    }

    @TestOnly
    public @NotNull T value() {
        return value(PlaceholderResolver.empty(), new Object[0]);
    }

    @TestOnly
    public int size() {
        return this.components.length;
    }

    @Override
    public @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
        StringBuilder stringBuilder = new StringBuilder();
        this.stringValue(placeholderResolver, contexts, stringBuilder::append);
        return stringBuilder.toString();
    }

    @Override
    public void stringValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts, @NotNull Consumer<String> consumer) {
        for (Component<T> component : this.components)
            component.stringValue(placeholderResolver, contexts, consumer);
    }

    @Override
    public boolean constexpr() {
        return this.constexpr;
    }

    protected @NotNull Component<T> constant(@NotNull T value, @NotNull String stringValue) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(stringValue);
        return new Component<T>() {
            @Override
            public @NotNull T value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return value;
            }

            @Override
            public @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return stringValue;
            }

            @Override
            public boolean constexpr() {
                return true;
            }
        };
    }

    protected @NotNull Component<T> simple(@NotNull String value) {
        Objects.requireNonNull(value);
        T element = this.factory().fromString(value);
        return this.constant(element, value);
    }

    protected final @NotNull Component<T> evalConstExpression(@NotNull Component<T> component, @NotNull StandaloneSlams slams) {
        if (!component.constexpr() || !slams.enableConstexprEval())
            return component;

        PlaceholderResolver placeholderResolver = PlaceholderResolver.empty(); // No local placeholders
        T value = component.value(placeholderResolver, new Object[0]);
        String stringValue = component.stringValue(placeholderResolver, new Object[0]);

        return this.constant(value, stringValue);
    }

    protected <T> @NotNull Component<T> globalPlaceholder(@NotNull Placeholder placeholder,
            @Unmodifiable @NotNull List<@NotNull Component<T>> arguments, @NotNull Component.ValueFactory<T> factory) {
        boolean constexpr = placeholder.constexpr() && arguments.stream().allMatch(Component::constexpr);
        return new Component<T>() {
            @Override
            public @NotNull T value(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return placeholder.value(placeholderResolver, contexts, arguments, factory).value(placeholderResolver, contexts);
            }

            @Override
            public @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return placeholder.value(placeholderResolver, contexts, arguments, factory).stringValue(placeholderResolver, contexts);
            }

            @Override
            public @Nullable Object rawValue(@NotNull PlaceholderResolver placeholderResolver, @NotNull Object @NotNull [] contexts) {
                return placeholder.value(placeholderResolver, contexts, arguments, factory).rawValue(placeholderResolver, contexts);
            }

            @Override
            public boolean constexpr() {
                return constexpr;
            }
        };
    }

    protected @NotNull Component<T> localPlaceholder(@NotNull String raw, @NotNull String key,
            @Unmodifiable @NotNull List<@NotNull Component<T>> arguments) {
        return new Component<T>() {
            @Override
            public @NotNull T value(@NotNull PlaceholderResolver placeholderResolver0, @NotNull Object @NotNull [] contexts) {
                Placeholder placeholder0 = placeholderResolver0.resolve(key);
                if (placeholder0 == null)
                    return CompositeComponent.this.factory().fromString(raw);
                return placeholder0.value(placeholderResolver0, contexts, arguments, CompositeComponent.this.factory()).value(placeholderResolver0, contexts);
            }

            @Override
            public @NotNull String stringValue(@NotNull PlaceholderResolver placeholderResolver0, @NotNull Object @NotNull [] contexts) {
                Placeholder placeholder0 = placeholderResolver0.resolve(key);
                if (placeholder0 == null)
                    return raw;
                return placeholder0.value(placeholderResolver0, contexts, arguments, CompositeComponent.this.factory()).stringValue(placeholderResolver0, contexts);
            }

            @Override
            public @Nullable Object rawValue(@NotNull PlaceholderResolver placeholderResolver0, @NotNull Object @NotNull [] contexts) {
                Placeholder placeholder0 = placeholderResolver0.resolve(key);
                if (placeholder0 == null)
                    return null;
                return placeholder0.value(placeholderResolver0, contexts, arguments, CompositeComponent.this.factory()).rawValue(placeholderResolver0, contexts);
            }

            @Override
            public boolean constexpr() {
                return false; // local placeholders can not be inlined
            }
        };
    }

    protected @NotNull Component<T> placeholder(@NotNull String raw, @NotNull String key,
            @Unmodifiable @NotNull List<@NotNull Component<T>> arguments, @NotNull PlaceholderResolver placeholderResolver,
            @NotNull StandaloneSlams slams) {
        if (key.isEmpty())
            return this.simple(raw);

        Placeholder globalPlaceholder = placeholderResolver.resolve(key);
        if (globalPlaceholder != null)
            return this.evalConstExpression(this.globalPlaceholder(globalPlaceholder, arguments, this.factory()), slams);
        return this.localPlaceholder(raw, key, arguments);
    }

    protected abstract @NotNull CompositeComponent<T> composite(@NotNull StandaloneSlams slams, @NotNull String raw,
            @NotNull PlaceholderResolver placeholderResolver);

    protected abstract @NotNull Component.ValueFactory<T> factory();

    protected abstract @NotNull T merge(@NotNull List<T> values);

    protected void inline(@NotNull StandaloneSlams slams, @NotNull List<Component<T>> components) {
        if (!slams.enableInline() || !slams.enableConstexprEval() || components.size() <= 1)
            return;

        List<Component<T>> original = new ArrayList(components);
        List<T> values = new ArrayList();
        List<String> stringValues = new ArrayList();

        components.clear();
        for (Component<T> component : original) {
            if (!component.constexpr()) {
                if (!values.isEmpty()) {
                    components.add(this.constant(this.merge(values), String.join("", stringValues)));
                    values.clear();
                    stringValues.clear();
                }
                components.add(component);
            } else {
                values.add(component.value(PlaceholderResolver.empty(), new Object[0]));
                stringValues.add(component.stringValue(PlaceholderResolver.empty(), new Object[0]));
            }
        }

        if (!values.isEmpty())
            components.add(this.constant(this.merge(values), String.join("", stringValues)));
    }

    protected @NotNull Component<T> @NotNull [] processString(@NotNull StandaloneSlams slams, @NotNull String input,
            @NotNull PlaceholderResolver placeholderResolver) {
        PlaceholderStyle style = slams.style();
        char headChar = style.head();
        char tailChar = style.tail();
        char separatorChar = style.separator();

        List<Component<T>> components = new ArrayList<>();
        StringBuilder raw = new StringBuilder(input.length());
        List<String> arguments = new ArrayList<>();
        StringBuilder argument = new StringBuilder();
        int scope = 0;
        boolean nested = false;
        boolean escape = false;
        for (char c : input.toCharArray()) {
            if (c == '\\') {
                escape = !escape;
                if (escape)
                    continue;
            }

            if (escape) {
                escape = false;
                raw.append(c);
                if (scope > 0)
                    argument.append(c);
                continue;
            }

            if (c == headChar && (tailChar != headChar || scope == 0)) {
                if (++scope == 1) {
                    if (raw.length() > 0) {
                        components.add(this.simple(raw.toString()));
                        raw.setLength(0);
                    }
                    raw.append(c);
                    continue;
                } else
                    nested = true;
            }

            raw.append(c);

            if (scope > 0) {
                if (c == tailChar && --scope == 0) {
                    arguments.add(argument.toString());

                    String key = arguments.remove(0);
                    if (nested)
                        components.add(this.placeholder(raw.toString(), key, Collections.unmodifiableList(arguments.stream()
                                .map(arg -> this.composite(slams, arg, placeholderResolver))
                                .collect(Collectors.toList())), placeholderResolver, slams));
                    else
                        components.add(this.placeholder(raw.toString(), key, new LazyEvalList<>(this::simple, arguments), placeholderResolver, slams));

                    nested = false;
                    raw.setLength(0);
                    argument.setLength(0);
                    arguments = new ArrayList<>();
                    continue;
                }

                if (scope == 1 && c == separatorChar) {
                    arguments.add(argument.toString());
                    argument.setLength(0);
                } else
                    argument.append(c);
            }
        }

        if (raw.length() > 0)
            components.add(this.simple(raw.toString()));

        this.inline(slams, components);

        return (Component<T>[]) components.toArray(new Component[0]);
    }
}
