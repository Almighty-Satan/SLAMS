# SLAMS - Simple Language And Message System

### Standalone Example
// TODO

### Mini-Message Example
```java
LanguageManager langManager = LanguageManager.create("English"); // Set English as the default language
langManager.load("English", JacksonParser.createJsonParser("messages.json")); // Register language and load messages from JSON file

MMStringEntry test0 = MMStringEntry.of("test0", langManager, null); // Just a simple message
MMStringEntry test1 = MMStringEntry.of("test1", langManager, ctx -> Placeholder.unparsed("hello", "world")); // Message with placeholder, "hello" will be replaced with "world"

player.sendMessage(test0.value(null)); // Send the message to a player. No context is provided and therefore the default language will be used. See Context#getLanguage
player.sendMessage(test1.value(null, Placeholder.unparsed("123", "456"))); // Send another message but add an additional placeholder
```

### Building
To build the project, open the terminal and type `./gradlew build`. All jars will be located at `<module>/build/libs/<module>-<version>.jar`.

### Gradle
```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.almighty-satan.slams:slams-<module>:<version>")
}
```
