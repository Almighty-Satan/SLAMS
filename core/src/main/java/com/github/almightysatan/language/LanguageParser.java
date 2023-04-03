package com.github.almightysatan.language;

import java.io.IOException;
import java.util.Map;

public interface LanguageParser { // TODO rename me

    Map<String, Object> load() throws IOException;
}
