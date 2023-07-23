package io.github.almightysatan.language.minimessage;

import io.github.almightysatan.language.Context;
import io.github.almightysatan.language.InvalidTypeException;
import io.github.almightysatan.language.LanguageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface MMStringArrayEntry extends MMLanguageEntry<Component[]> {

    static @NotNull MMStringArrayEntry of(@NotNull String path, @NotNull LanguageManager languageManager, @Nullable ContextTagResolver<Context> tagResolver) {
        class MMStringArrayEntryImpl extends AbstractMMLanguageEntry<String[]> implements MMStringArrayEntry {

            protected MMStringArrayEntryImpl(@NotNull String path, @NotNull LanguageManager languageManager, @Nullable ContextTagResolver<Context> tagResolver) {
                super(path, languageManager, tagResolver);
            }

            @Override
            protected String[] checkType(@Nullable Object value) throws InvalidTypeException {
                if (value instanceof String[])
                    return (String[]) value;
                if (value instanceof List) {
                    return ((List<?>) value).stream().map(element -> {
                        if (element instanceof String)
                            return (String) element;
                        throw new InvalidTypeException();
                    }).toArray(String[]::new);
                }
                throw new InvalidTypeException();
            }

            @Override
            public @NotNull Component @NotNull [] value(@Nullable Context context, @NotNull ContextTagResolver<Context> tagResolver) {
                Object value = this.getRawValue(context);

                TagResolver localTagResolver = tagResolver.resolve(context);
                return Arrays.stream((String[]) value).map(s -> {
                    if (this.tagResolver == null)
                        return MiniMessage.miniMessage().deserialize(s, localTagResolver);
                    else
                        return MiniMessage.miniMessage().deserialize(s, localTagResolver, this.tagResolver.resolve(context));
                }).toArray(Component[]::new);
            }
        }

        return new MMStringArrayEntryImpl(path, languageManager, tagResolver);
    }
}
