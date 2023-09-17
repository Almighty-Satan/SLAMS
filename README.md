![Maven Central](https://img.shields.io/maven-central/v/io.github.almighty-satan.slams/slams-parser-jackson?style=flat-square)
![GitHub](https://img.shields.io/github/license/Almighty-Satan/SLAMS?style=flat-square)
![CI](https://img.shields.io/github/actions/workflow/status/Almighty-Satan/SLAMS/gradle-build.yml?branch=master&style=flat-square)
![Last Commit](https://img.shields.io/github/last-commit/Almighty-Satan/SLAMS?style=flat-square)

# SLAMS - Simple Language And Message System

___

SLAMS is an easy to use message/language system allowing projects to easily support multiple languages with placeholders and message formatting. Messages can be automatically loaded from formats like JSON or supplied by the user via a custom implementation of the `LanguageParser` interface.

SLAMS has been created with support for Minecraft servers in mind and therefore also supports formats like [MiniMessages](https://docs.advntr.dev/minimessage/index.html).

### Standalone Example
```java
Slams slams = Slams.create("English"); // Set English as the default language

StandaloneMessage test0 = StandaloneMessage.of("test0", slams); // Just a simple message
StandaloneMessage test1 = StandaloneMessage.of("test1", slams, Placeholder.constant("hello", "world")); // Message with placeholder, "hello" will be replaced with "world"

slams.load("English", JacksonParser.createJsonParser("messages.json")); // Register language and load messages from JSON file

System.out.println(test0.value()); // Print the message. No context is provided and therefore the default language will be used. See Context#language
System.out.println(test1.value(null, Placeholder.constant("123", "456"))); // Print another message but add an additional placeholder
```

### Mini-Message Example
```java
Slams slams = Slams.create("English"); // Set English as the default language

AdventureMessage test0 = AdventureMessage.of("test0", slams); // Just a simple message
AdventureMessage test1 = AdventureMessage.of("test1", slams, ctx -> net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("hello", "world")); // Message with placeholder, "hello" will be replaced with "world"

slams.load("English", JacksonParser.createJsonParser("messages.json")); // Register language and load messages from JSON file

audience.sendMessage(test0.value()); // Send the message to an Adventure Audience. No context is provided and therefore the default language will be used. See Context#language
audience.sendMessage(test1.value(null, net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed("123", "456"))); // Send another message but add an additional minimessage placeholder
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
