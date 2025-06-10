# llm-reference-android

An experiment to prompt LLMs to imitate this sample code base with latest versions of APIs and verified modern Android best practices.

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

# Screens

- Each screen in the app has a root component which starts with a androidx.compose.material3.Scaffold
- Screens use ViewModels and delegate their asynchronous suspend work to viewModelScope

# Style

- Use Material Design v3 androidx.compose.material3
- In the Activity, wrap the App composable in the theme to apply it everywhere
- Set Theme dynamicColor = false so that it follows the specified colors
- Use https://m3.material.io/theme-builder to easily create a theme from the colors in an uploaded photo

