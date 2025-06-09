# llm-reference-android

Ideal modern Android patterns, collected as an example for AI coding tools.

# Project structure

- Define a top-level App composable entrypoint which MainActivity delegates to
- Define a custom Application to hold androidx.lifecycle.viewmodel.CreationExtras for dependency injection

# Project dependencies

- Never recommend *.jvmstubs dependencies

# Navigation

- Use the type-safe Kotlin DSL with androidx.navigation.compose.NavHost
- rememberNavController() and define the NavHost in the App composable
- Use backStackEntry.toRoute<T> to get destinations
- To pass complex arguments, use a helper class like SerializableJsonNavType

# Style

- Use Material Design v3 androidx.compose.material3
- In the Activity, wrap the App composable in the theme to apply it everywhere
- Set Theme dynamicColor = false so that it follows the specified colors
- Use https://m3.material.io/theme-builder to easily create a theme from the colors in an uploaded photo

