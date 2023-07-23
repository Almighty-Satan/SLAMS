package io.github.almightysatan.language.minimessage;

import io.github.almightysatan.language.Context;
import io.github.almightysatan.language.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestContext implements Context {

    private Language language;
    private String name;

    public TestContext(@Nullable Language language, @NotNull String name) {
        this.language = language;
        this.name = name;
    }

    @Override
    public @Nullable Language getLanguage() {
        return this.language;
    }

    public @NotNull String getName() {
        return this.name;
    }
}
