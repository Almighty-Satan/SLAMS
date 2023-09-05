package io.github.almightysatan.slams.minimessage;

import io.github.almightysatan.slams.Context;
import io.github.almightysatan.slams.LanguageManager;
import io.github.almightysatan.slams.impl.LanguageEntryImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractMMLanguageEntry<T> extends LanguageEntryImpl<T> {

    protected final ContextTagResolver<Context> tagResolver;

    protected AbstractMMLanguageEntry(@NotNull String path, @NotNull LanguageManager languageManager, @Nullable ContextTagResolver<Context> tagResolver) {
        super(path, languageManager);
        this.tagResolver = tagResolver;
    }
}
