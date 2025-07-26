# Guidebook.md
Helps BTA modders create mod guides quickly and easily using Markdown with extra features

**How to Use?**  
Create a MDBookScreen inherited class.
Load config from asset path, include .md files.  
Use as a normal screen, opened from a trigger.

**Formats**  
Use `< & >` chars for color codes:
- 0 = &0Black
- 1 = &1Dark Blue
- 2 = &2Dark Green
- 3 = &3Dark Aqua
- 4 = &4Dark Red
- 5 = &5Dark Purple
- 6 = &6Gold
- 7 = &7Gray
- 8 = &8Dark Gray
- 9 = &9Blue
- a = &aGreen
- b = &bAqua
- c = &cRed
- d = &dLight Purple
- e = &eYellow
- f = &fWhite
- s = &sShadow
- r = Reset

**Syntax**  
Regular &3Markdown syntax adapted for &6BTA Guidebook format:
# H1
## H2
### H3
#### H4
##### H5
###### H6
* Unordered List
- Unordered List
1. Ordered List

**Specials**  
**Images**
- [text](/assets/modid/textures/path/to/img.png)(width,height,type)
- ![slot](modid:item/name)
- ![workbench](empty,empty,empty,empty,empty,empty,empty,empty,empty)

- &6Width & Height in pixels
- &6type = default | icon (no text)

&cMore info on the mod's GitHub wiki!
