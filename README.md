![Maven Central](https://img.shields.io/maven-central/v/io.github.almighty-satan.slams/slams-parser-jackson?style=flat-square)
![GitHub](https://img.shields.io/github/license/Almighty-Satan/SLAMS?style=flat-square)
![CI](https://img.shields.io/github/actions/workflow/status/Almighty-Satan/SLAMS/gradle-build.yml?branch=master&style=flat-square)
![Last Commit](https://img.shields.io/github/last-commit/Almighty-Satan/SLAMS?style=flat-square)

# SLAMS - Simple Language And Message System

___

SLAMS is an easy to use message/language system allowing projects to easily support multiple languages with placeholders and messages formats saved in different file formats like JSON.

SLAMS has been created with support for Minecraft servers in mind and therefore also supports formats like [MiniMessages](https://docs.advntr.dev/minimessage/index.html).

### Standalone Example
// TODO

### Mini-Message Example
```java
LanguageManager langManager = LanguageManager.create("English"); // Set English as the default language
langManager.load("English", JacksonParser.createJsonParser("messages.json")); // Register language and load messages from JSON file

MMStringEntry test0 = MMStringEntry.of("test0", langManager, null); // Just a simple message
MMStringEntry test1 = MMStringEntry.of("test1", langManager, ctx -> Placeholder.unparsed("hello", "world")); // Message with placeholder, "hello" will be replaced with "world"

player.sendMessage(test0.value(null)); // Send the message to a player. No context is provided and therefore the default language will be used. See Context#language
player.sendMessage(test1.value(null, Placeholder.unparsed("123", "456"))); // Send another message but add an additional placeholder
```

[MiniMessages](https://docs.advntr.dev/minimessage/index.html) is a user friendly message format for Minecraft servers integrated into [Adventure](https://github.com/KyoriPowered/adventure). Every server core [implementing the Adventure-API](https://docs.advntr.dev/platform/native.html) (e.g. [Paper](https://papermc.io/)) should be supported out of the box while some others (e.g. [Craftbukkit or Spigot](https://docs.advntr.dev/platform/bukkit.html)) may require additional adapters.

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
