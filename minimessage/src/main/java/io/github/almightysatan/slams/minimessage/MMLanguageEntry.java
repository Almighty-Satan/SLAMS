package io.github.almightysatan.slams.minimessage;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.LanguageEntry;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface MMLanguageEntry<T> extends LanguageEntry {

    @NotNull T value(@Nullable Context context, @NotNull ContextTagResolver<Context> tagResolver);

    default @NotNull T value(@Nullable Context context) {
        return this.value(context, ContextTagResolver.empty());
    }

    default @NotNull T value(@Nullable Context context, @NotNull TagResolver... tagResolvers) {
        Objects.requireNonNull(tagResolvers);
        return this.value(context, ContextTagResolver.of(TagResolver.resolver(tagResolvers)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default @NotNull T value(@Nullable Context context, @NotNull ContextTagResolver<?>... tagResolvers) {
        Objects.requireNonNull(tagResolvers);
        return this.value(context, (ContextTagResolver<Context>) ContextTagResolver.of((ContextTagResolver[]) tagResolvers));
    }
}
