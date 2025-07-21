Cropped from emulator screen recordings with ffmpeg

```
ffmpeg -ss 00:00:06 -i debounced_input.webm -vf "crop=in_w:in_h/3:0:62,fps=10,scale=480:-1:flags=lanczos,split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse" -loop 0 view_model_debounce_input.gif

ffmpeg -i rearrange_layout.webm -vf "crop=in_w:in_h*0.6:0:62,fps=10,scale=480:-1:flags=lanczos,split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse" -loop 0 chrome_rearrange_layout.gif
```