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
The style of placeholders can be changed by declaring a `PlaceholderStyle` or using a `StandaloneSlams` object when registering a message.  
By default they look like this `<key:argument0:argument1:argument2>`
and can be escaped by using ``\``: `This does not contain a \<placeholder\>`

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

### Languages
SLAMS supports using multiple languages. Which language is used is determined by the `Context` object
that is passed when calling `Message#value`. SLAMS automatically uses the default language if either the
context is `null` or `Context#language` returns `null`.
```java
Slams slams = Slams.create("English"); // Set English as the default language

StandaloneMessage message = StandaloneMessage.of("example.greeting", slams); // Just a simple message

// Let's load our languages
slams.load("English", JacksonParser.createJsonParser("english_messages.json"));
slams.load("German", JacksonParser.createJsonParser("german_messages.json"));
slams.load("Spanish", JacksonParser.createJsonParser("spanish_messages.json"));

System.out.println(message.value()); // This will use the default language (English)
System.out.println(message.value(null)); // This also uses the default language

Context context = new Context() { // You can extend this interface however you want
    @Override
    public @Nullable String language() {
        return "Spanish";
    }
};

System.out.println(message.value(context)); // This prints a Spanish message
```
`english_messages.json` would look something like this:
```json
{
  "example": {
    "greeting": "Hello"
  }
}
```
We can also use more than one `LanguageParser` when loading a language. They will be run in sequential order and can
read or overwrite values loaded by previous parsers. This could be used provide a user with a config to change messages
and use a pre-configured default value if a message is missing from the user's config.
```java
slams.load("English", JacksonParser.createJsonParser("internal/default_messages.json"), JacksonParser.createJsonParser("config/user_messages.json"));
```
Implementing your own `LanguageParser` is also possible. Have a look at `JacksonParser` as a reference.

### Placeholders
Placeholders can either be passed when registering a message
```java
StandaloneMessage testMessage = StandaloneMessage.of("test", slams, Placeholder.constant("pi", "3"));
```
or when calling `Message#value`
```java
testMessage.value(context, Placeholder.constant("e", "3"));
```
Using multiple placeholders is also possible:
```java
PlaceholderResolver placeholderResolver = PlaceholderResolver.of(Placeholder.constant("hello", "world"), Placeholder.constant("1234", "5678"));
StandaloneMessage.of("test2", slams, placeholderResolver);
```
Though you might want to use a builder for that:
```java
PlaceholderResolver placeholderResolver = PlaceholderResolver.builder().constant("hello", "world").constant("1234", "5678").build();
```
`PlaceholderResolver` and `Placeholder` objects can be re-used as many times as you'd like.

### Context/Argument Dependent Placeholders
Placeholders can have arguments. The following placeholder always replaces itself with its first argument.
For example `<test:Hello World>` would be replaced with `Hello World`. Using arguments like this might seem pretty
useless, but you might want wo use them to pass something like a `SimpleDateFormat`.
```java
Placeholder.withArgs("test", arguments -> arguments.isEmpty() ? "Empty" : arguments.get(0));
```
Let's say we have a `User` that extends from `Context` and `User` has a method `getName`. We can now create a 
placeholder that is replaced with the name of the user.
```java
Placeholder.contextual("name", User.class, User::getName);
```
If the provided context is `null` or not a `User` this placeholder would be replaced with something telling us our
context is invalid. If we want to avoid this we can provide our own fallback value:
```java
Placeholder.contextual("name", User.class, User::getName, "Unknown User");
```

### Conditional Placeholders
Conditional placeholders function as if statements within messages.
```java
Placeholder.conditional("hasName", User.class, User::hasName());
```
An example of a message using the placeholder shown above would be: `Hello <hasName:<name>:unknown user>!`.
Be aware that the Mini-Message implementation does by default not support parsing tags within the arguments of a
placeholder. If you need this functionality consider using `ContextTagResolver#ofUnsafe`.

### Built-In Placeholders
SLAMS provides built-in placeholders that can be enabled by calling `PlaceholderResolver.Builder#builtIn`

| Placeholder | Description                                                                                                                                  | Example                                                               |
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------|
| `if_eq`     | Returns the third argument if the first argument is equal to the second argument. Otherwise the third argument is returned.                  | `<if_eq:<something>:x:X:not X>`                                       |
| `if_ne`     | Returns the third argument if the first argument is **not** equal to the second argument. Otherwise the third argument is returned.          | `<if_ne:<something>:x:not X:X>`                                       |
| `if_num_eq` | Returns the third argument if the first argument is a number equal to the second argument. Otherwise the third argument is returned.         | `<if_num_eq:<number>:1.5:1.5:not 1.5>`                                |
| `if_num_ne` | Returns the third argument if the first argument is a number **not** equal to the second argument. Otherwise the third argument is returned. | `<if_num_ne:<number>:1.5:not 1.5:1.5>`                                |
| `if_num_lt` | Returns the third argument if the first argument is less than the second argument. Otherwise the third argument is returned.                 | `<if_num_lt:<number>:1.5:less than 1.5:not less than 1.5>`            |
| `if_num_gt` | Returns the third argument if the first argument is greater than the second argument. Otherwise the third argument is returned.              | `<if_num_gt:<number>:1.5:greater than 1.5:not greater than 1.5>`      |
| `if_num_le` | Returns the third argument if the first argument is less than or equal to the second argument. Otherwise the third argument is returned.     | `<if_num_le:<number>:1.5:less than or equal to 1.5:greater than 1.5>` |
| `if_num_ge` | Returns the third argument if the first argument is greater than or equal to the second argument. Otherwise the third argument is returned.  | `<if_num_ge:<number>:1.5:greater than or equal to 1.5:less than 1.5>` |
| `add`       | Adds the first and second argument.                                                                                                          | `<add:1:5>`                                                           |
| `sub`       | Substracts the second argument from the first.                                                                                               | `<sub:5:1>`                                                           |
| `mul`       | Multiplies the first and second argument.                                                                                                    | `<mul:2:2>`                                                           |
| `div`       | Divides the first argument by the second.                                                                                                    | `<div:4:2>`                                                           |
| `sdf`       | Formats the current date with the given [SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html).       | `<sdf:yyyy-MM-dd>`                                                    |

### PlaceholderAPI
[PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) is a [Spigot plugin](https://www.spigotmc.org/resources/placeholderapi.6245/)
that allows a plugin to access placeholders from other plugins. SLAMS placeholders can be made available via
PlaceholderAPI by using the `papi` module.
```java
PlaceholderResolver placeholders = ... // Your placeholders
new SlamsPlaceholderExpansion(identifier, author, version, placeholders).register();
```
To use PlaceholderAPI placeholders in SLAMS messages you just have to add a `PapiPlaceholderResolver` to your
PlaceholderResolver.
```java
PlaceholderResolver placeholderResolver = PlaceholderResolver.of(Placeholder.constant("something", "else"), PapiPlaceholderResolver.create());
```
If you are using MiniMessage or the standalone implementation with the default placeholder style, a PlaceholderAPI placeholder would
look like this:
```
<papi:expansion_name:placeholder_name>
```

### Modules

| Module         | Description                                                                                                                                                                    |
|----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| core           | Contains common code. Can not be used on it's own.                                                                                                                             |
| standalone     | A standalone implementation that does not rely on external dependencies.                                                                                                       |
| minimessage    | An implementation that uses the [MiniMessage](https://docs.advntr.dev/minimessage/index.html) format.                                                                          |
| parser-jaskl   | A [JASKL](https://github.com/Almighty-Satan/JASKL) based parser to load messages. Supports formats like YAML, HOCON, JSON and MongoDB.                                         |
| parser-jackson | A [Jackson](https://github.com/FasterXML/jackson) based parser to load message files. It supports formats like JSON and TOML.                                                  |
| papi           | Allows [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) placeholders to be used in SLAMS messages and makes SLAMS placeholders available via PlaceholderAPI. |

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
