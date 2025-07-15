# Android Recipies

A cookbook of widgets and UI constructions I like to reuse.

I originally committed this to try and prompt ChatGPT to follow the patterns here, after noticing weird recommendations that I believe may be references to outdated beta releases of Jetpack Compose. That didn't really work, but here are the instructions:

## Project structure

- Define a top-level App composable entrypoint which MainActivity delegates to
- Define a custom Application to hold androidx.lifecycle.viewmodel.CreationExtras for dependency injection

## Project dependencies

- The app should never depend on *.jvmstubs libraries

## Navigation

- Use the type-safe Kotlin DSL with androidx.navigation.compose.NavHost
- To pass complex serializable arguments, use a helper class like SerializableJsonNavType

## Screens

- Each screen in the app has a root component named *Screen hosting a androidx.compose.material3.Scaffold

## Style

- Use Material Design v3 androidx.compose.material3
- Set ```dynamicColor = false``` on the theme so that it actually uses the specified colors
