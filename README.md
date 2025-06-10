# Android reference app

A simple app showcasing latest versions of APIs and verified best practices.

Intended use is to prompt LLMs to imitate this sample code base.

# Project structure

- Define a top-level App composable entrypoint which MainActivity delegates to
- Define a custom Application to hold androidx.lifecycle.viewmodel.CreationExtras for dependency injection

# Project dependencies

- Never add *.jvmstubs dependencies

# Navigation

- Use the type-safe Kotlin DSL with androidx.navigation.compose.NavHost
- In the App composable, rememberNavController() and define the NavHost
- Use backStackEntry.toRoute<T> to get destinations
- To pass complex serializable arguments, use a helper class like SerializableJsonNavType

# Screens

- Each screen in the app has a root component hosting a androidx.compose.material3.Scaffold
- Screens use ViewModels and delegate their asynchronous suspend work to viewModelScope

# Style

- Use Material Design v3 androidx.compose.material3
- In the Activity, wrap the App composable in the theme to apply it everywhere
- Set Theme dynamicColor = false so that it follows the specified colors
