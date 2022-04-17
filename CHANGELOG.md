# Changelog 

## 2.0.0

### Syntax and Language Support
* Add support for base type syntax (#4).
* Add support `float` and `unsigned` data types.
* Fix parsing for aggregations; add `mean` aggregator.
* Improve support for parameters of `.input` and `.oudput` directives.

### Editor
 * Fix syntax highlighting of `#include` directives.
 * Fix bug in reference resolution logic (#3).
 * Fix missing jump-to-relation line markers for fact statements.
 * Improve jump-to-base / jump-to-sub-components line markers for components.
 * Add quick-fix for replacing unused variables with wildcards in statements.

### Performance and Stability
 * Replace usages of deprecated APIs (#6).
 * Fix performance issues for line marker generation.
 * Use latest IntelliJ Plugin SDK.

## 1.1.0
 * Fix annotations for relation arity mistakes.
 * Extend documentation parsing to all declaration types (e.g. preprocessor definitions, types, etc.).
 * Include comment declarations in code completion.
 * Fix rule lookup via gutter-icon.
 * Add support for relation qualifiers.
 
## 1.0.0
 * Initial release.