<div style="text-align: center;">
    <p align="center">
      <img alt="Datalog IntelliJ" width="128" height="128" src="src/main/resources/icon.svg">
    </p>
</div>
<br>

# Datalog Plugin for the IntelliJ Platform


## Features

### Syntax Highlighting
The plugin features extensive syntax highlighting. In addition renders identifiers differently based on the *semantic information* inferred from your code. For instance, variables in relations will use a italic font while instances of components will use an underscore effect.

[![Syntax Highlighting](https://i.gyazo.com/9eb00d311e6ff2696254796443a24202.png)](https://gyazo.com/9eb00d311e6ff2696254796443a24202)

As with all common language plugins for JetBrains IDEs, the syntax highlighting is fully customizable and can even be disabled entirely if you like to keep it plain.

[![Highlighting Options](https://i.gyazo.com/b1de7ba2792baaffb6cda99a468ed336.png)](https://gyazo.com/b1de7ba2792baaffb6cda99a468ed336)

### Reference Resolution
The plugin keeps track of all identifiers and their corresponding declaration-sites, which enables a lot of advanced code editing goodies.

Want to jump to an identifier's definition? Just click it while pressing CTRL!
As you can see in the following example, this also works in the reverse direction: CTRL-click a definition to see a list of its usages.

[![Reference Resolution](https://i.gyazo.com/24627cf6cf224be5b55a970cd12cd4bc.gif)](https://gyazo.com/24627cf6cf224be5b55a970cd12cd4bc)

In addition, the plugin also supports most of the common reference-resolution related features found in Jetbrains IDEs which can be triggered by various keyboard shortcuts.

### Refactoring
Not happy with your identifiers' names? Press SHIFT-F6 and rename it in no time!

[![Refactoring 1](https://i.gyazo.com/bbe65b48193d7022b344a66dfe5981fd.gif)](https://gyazo.com/bbe65b48193d7022b344a66dfe5981fd)

Of course refactoring also works over multiple files, as well as for relations that within components.

[![Refactoring 2](https://i.gyazo.com/ef03411c9f4563d3a1273ccc0b167b6b.gif)](https://gyazo.com/ef03411c9f4563d3a1273ccc0b167b6b)

### Quick Definition and Documentation
Use the IDEs quick-definition keyboard shortcuts to view an identifiers definition, without jumping to a different code section.

[![Quick-Definition](https://i.gyazo.com/7f508f5feba63e3f18751db4ac17e848.gif)](https://gyazo.com/7f508f5feba63e3f18751db4ac17e848)

Annotate your code with JavaDoc-style comments to provide documentation for relations, components and even preprocessor definitions.

[![Quick-Documentation 1](https://i.gyazo.com/89ee4ba3071a7f6de8b2b0a6de1c8563.png)](https://gyazo.com/89ee4ba3071a7f6de8b2b0a6de1c8563)

The documentation for an identifier can be queried by the IDEs quick-documentation keyboard shortcut.

[![Quick-Documentation 2](https://i.gyazo.com/b6eb6c868d3cc66f95c862c6e2baef81.gif)](https://gyazo.com/b6eb6c868d3cc66f95c862c6e2baef81)

### Code Completion and Parameter Hints
One of the features I love most about IDEs from JetBrains is their context-sensitive auto-completion. While typing, the IDE will show you a list of suggestions that might complete your current input. Wherever possible, it will only suggest completions that make sense semantically.

[![Code Completion](https://i.gyazo.com/dfd8d12380e0e9c52ecf3315ed53bd65.gif)](https://gyazo.com/dfd8d12380e0e9c52ecf3315ed53bd65)

When filling in the parameters of a function or relation and unsure, press CTRL-P to see the signature of the relation.

[![Parameter Hints](https://i.gyazo.com/ecab9bd107398c574a2d832c88098bec.gif)](https://gyazo.com/ecab9bd107398c574a2d832c88098bec)

### Code Analysis
In order to find bugs in the code before it is passed to Soufflé, the plugin will highlight potential errors.

For instance, undefined identifiers will be highlighted.

[![Missing Declaration](https://i.gyazo.com/f8660db4e2fafde96d09a1b8e0f88f93.png)](https://gyazo.com/f8660db4e2fafde96d09a1b8e0f88f93)

Missing parameters will be reported...

[![Missing Parameter](https://i.gyazo.com/8f633ef58c46b18cad93fef81de88a37.png)](https://gyazo.com/8f633ef58c46b18cad93fef81de88a37)

...as will be superfluous parameters.

[![Superfluous Parameter](https://i.gyazo.com/a29d9d9dacd99d00f5c84f090d928102.png)](https://gyazo.com/a29d9d9dacd99d00f5c84f090d928102)

Furthermore, the IDE will issue a warning when variables are used only once in a relation rule.

[![Unused Variable](https://i.gyazo.com/ea25eaf0cc3f0a5e404411e109a2bf44.png)](https://gyazo.com/ea25eaf0cc3f0a5e404411e109a2bf44)

### Automatic Formatting
Admittedly, this feature is not quite mature yet, but the plugin also supports automatic code formatting.
For instance, new lines will be correctly indented when defining rules for a relation.

[![Formatting 1](https://i.gyazo.com/fd38bcd2988f2f27f4f873dd9481a207.gif)](https://gyazo.com/fd38bcd2988f2f27f4f873dd9481a207)

Code formatting can also be triggered manually for a whole file.

[![Formatting 2](https://i.gyazo.com/be9b8b87dec06f56691bbe99df54a6f9.gif)](https://gyazo.com/be9b8b87dec06f56691bbe99df54a6f9)


## How to Get It
The plugin supports all JetBrains IDEs (i.e. IntelliJ, PyCharm, WebStorm, etc.) and can be obtained via their built-in plugin manager.
Just search for `Datalog Language Support` in the marketplace section of the plugin dialog.

The plugin is listed on the [JetBrains plugin repo](https://plugins.jetbrains.com/plugin/13056-datalog-language-support).