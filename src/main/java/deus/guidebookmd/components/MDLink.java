package deus.guidebookmd.components;

import org.lwjgl.input.Mouse;

public class MDLink extends MDText {

	protected int pageRef = 0;
	public MDLink(String text, String link) {
		super(text);
		String s = link.trim();
		if (!s.isEmpty()) {
			pageRef = Integer.parseInt(s);
		}
	}

	@Override
	public void render(int x, int y, int mx, int my) {
		super.render(x, y, mx, my);

		if (mx > x && mx < x + width && my > y && my < y + height) {
			if (Mouse.isButtonDown(0)) {
				screen.goTo(pageRef);
			}
		}


	}




}
