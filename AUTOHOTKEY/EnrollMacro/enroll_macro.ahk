#`::
Run, http://dsm2015.cafe24.com
Sleep, 4000
MouseClick, WheelDown, , ,8
Sleep, 1500
ImageSearch, foundX1, foundY1, 600, 100, A_ScreenWidth, A_ScreenHeight, *50 img.png
MouseClick, L, foundX1, foundY1
Sleep, 1500
ImageSearch, foundX2, foundY2, 500, 100, A_ScreenWidth, A_ScreenHeight, *50 20.png
MouseClick, L, foundX2, foundY2
Sleep, 5000
ImageSearch, foundX3, foundY3, 500, 100, A_ScreenWidth, A_ScreenHeight, *50 12.png
MouseClick, L, foundX3, foundY3
MouseClick, L, foundX2, foundY2
return