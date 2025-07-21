# Android Recipies

A cookbook of common patterns and UI constructions.

# Other Notes / Gotchas

## Configuring the software keyboard on Android emulator

Some of the default Android emulators are configured to use a stylus input. This appears as a rounded bar on the left edge of the screen when a TextField is selected.

This prevents the soft keyboard appearing on the bottom of the screen.

The stylus needs to be disabled in the guest Android OS's settings, by clicking on the stylus bar ... -> Menu.

If you see a bar on the left edge of the screen when a TextField is selected, it means that Android is trying to use a stylus input.

## System bar background color in API 34 vs API 35

To maintain consistent behavior across versions, we need to manually specify the system bar background and content colors.

[See good writeup here.](https://www.droidcon.com/2025/02/04/the-elephant-in-the-room-for-android-devs-jetpack-compose-and-edge-to-edge-on-android-15/)
