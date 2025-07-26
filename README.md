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

# How to use?
### Markdown guidebook
```java
public class MyBook extends MDBookScreen {

	public IntroBook() {
		String path = "/assets/modid/markdown/bookname/";
		config = BookConfig.fromJsonResource(getClass(), path + "config.json");
		String[] pages = {
			"index.md", // Page 0
			"intro.md", // Page 1
			"page2.md",
			"page3.md",
			"page4.md",
			"page5.md",
			"page6.md" // Last page
		};
		for (String page : pages) {
			loadMarkdownPages(path + page); // Load markdown
		}
	}
}
```

### Display markdown
_Check superclass for more info._
```java
public class MyMDScreen extends MDScreen {
    public MyMDScreen() {
        currentPage = MarkdownCompiler.compile("/assets/modid/markdown/path/to/your/file.md", getClass());
    }
}
```

### Showcase
Download the zip with the files in the release page
<img width="1464" height="879" alt="image" src="https://github.com/user-attachments/assets/d87392c0-9c10-44e0-878c-a8926bffca7f" />
https://github.com/user-attachments/assets/d2f9fe57-74f7-433e-8f00-0848fc137571

