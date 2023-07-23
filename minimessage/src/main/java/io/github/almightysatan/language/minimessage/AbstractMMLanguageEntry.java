package io.github.almightysatan.language.minimessage;

import com.github.almightysatan.language.Context;
import com.github.almightysatan.language.LanguageManager;
import com.github.almightysatan.language.impl.LanguageEntryImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractMMLanguageEntry<T> extends LanguageEntryImpl<T> {

    protected final ContextTagResolver<Context> tagResolver;

    protected AbstractMMLanguageEntry(@NotNull String path, @NotNull LanguageManager languageManager, @Nullable ContextTagResolver<Context> tagResolver) {
        super(path, languageManager);
        this.tagResolver = tagResolver;
    }
}
