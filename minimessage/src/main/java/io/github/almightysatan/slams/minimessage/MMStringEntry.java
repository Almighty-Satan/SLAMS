package io.github.almightysatan.slams.minimessage;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.InvalidTypeException;
import io.github.almightysatan.slams.LanguageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MMStringEntry extends MMLanguageEntry<Component> {

    static @NotNull MMStringEntry of(@NotNull String path, @NotNull LanguageManager languageManager, @Nullable ContextTagResolver<Context> tagResolver) {
        class MMStringEntryImpl extends AbstractMMLanguageEntry<String> implements MMStringEntry {

            protected MMStringEntryImpl(@NotNull String path, @NotNull LanguageManager languageManager, @Nullable ContextTagResolver<Context> tagResolver) {
                super(path, languageManager, tagResolver);
            }

            @Override
            protected String checkType(@Nullable Object value) throws InvalidTypeException {
                if (!(value instanceof String))
                    throw new InvalidTypeException();
                return (String) value;
            }

            @Override
            public @NotNull Component value(@Nullable Context context, @NotNull ContextTagResolver<Context> tagResolver) {
                String value = this.getRawValue(context);

                TagResolver localTagResolver = tagResolver.resolve(context);
                if (this.tagResolver == null)
                    return MiniMessage.miniMessage().deserialize(value, localTagResolver);
                else
                    return MiniMessage.miniMessage().deserialize(value, localTagResolver, this.tagResolver.resolve(context));
            }
        }

        return new MMStringEntryImpl(path, languageManager, tagResolver);
    }
}
