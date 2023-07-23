package io.github.almightysatan.language.minimessage;

import com.github.almightysatan.language.Context;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

@FunctionalInterface
public interface ContextTagResolver<T extends Context> {

    @NotNull TagResolver resolve(@Nullable T context);

    static @NotNull ContextTagResolver<Context> empty() {
        return context -> TagResolver.empty();
    }

    static @NotNull ContextTagResolver<Context> parsed(@NotNull String key, @NotNull String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return context -> Placeholder.parsed(key, value);
    }

    static @NotNull ContextTagResolver<Context> unparsed(@NotNull String key, @NotNull String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return context -> Placeholder.unparsed(key, value);
    }

    static @NotNull ContextTagResolver<Context> of(@NotNull TagResolver tagResolver) {
        Objects.requireNonNull(tagResolver);
        return context -> tagResolver;
    }

    static @NotNull ContextTagResolver<Context> of(@NotNull TagResolver... tagResolvers) {
        Objects.requireNonNull(tagResolvers);
        if (tagResolvers.length == 0)
            return empty();
        return context -> TagResolver.resolver(tagResolvers);
    }

    @SuppressWarnings("unchecked")
    static <T extends Context> @NotNull ContextTagResolver<Context> of(@NotNull Class<T> type, @NotNull ContextTagResolver<T> contextTagResolver) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(contextTagResolver);
        return context -> context != null && type.isAssignableFrom(context.getClass()) ? contextTagResolver.resolve((T) context) : TagResolver.empty();
    }

    @SafeVarargs
    static @NotNull ContextTagResolver<Context> of(@NotNull ContextTagResolver<Context>... contextTagResolvers) {
        Objects.requireNonNull(contextTagResolvers);
        if (contextTagResolvers.length == 0)
            return empty();
        return context -> TagResolver.resolver(Arrays.stream(contextTagResolvers).map(resolver -> resolver.resolve(context)).toArray(TagResolver[]::new));
    }
}
