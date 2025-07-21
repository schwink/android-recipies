# Android Recipies

A cookbook of common patterns and UI constructions.

## ViewModel patterns

### Debounce updating a field over the network

![view_model_debounce_input](https://github.com/user-attachments/assets/a0c37238-1219-4251-80ca-9ac03907ca21)

## Layout and animation

Patterns for moving stuff around the screen dynamically in the layout or draw phases without requiring recomposition.

### Simulate changing padding on a single component on scroll

![chrome_offset_padding](https://github.com/user-attachments/assets/dfc47adf-645f-4e58-9ed2-d349c1044cff)

### Rearrange a header to be more compact on scroll

![chrome_rearrange_layout](https://github.com/user-attachments/assets/7d8decbf-866d-47ff-8285-e62ad88a6062)

### Fade in a row

![chrome_fade_in_row](https://github.com/user-attachments/assets/d64a458b-04b5-45a0-afd1-f7cd901a53db)

## Other notes

### Configuring the software keyboard on Android emulator

Some of the default Android emulators are configured to use a stylus input. This appears as a rounded bar on the left edge of the screen when a TextField is selected.

This prevents the soft keyboard appearing on the bottom of the screen.

The stylus needs to be disabled in the guest Android OS's settings, by clicking on the stylus bar ... -> Menu.

If you see a bar on the left edge of the screen when a TextField is selected, it means that Android is trying to use a stylus input.

### System bar background color in API 34 vs API 35

To maintain consistent behavior across versions, we need to manually specify the system bar background and content colors.

[See good writeup here.](https://www.droidcon.com/2025/02/04/the-elephant-in-the-room-for-android-devs-jetpack-compose-and-edge-to-edge-on-android-15/)
