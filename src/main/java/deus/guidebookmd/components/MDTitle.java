package deus.guidebookmd.components;

public class MDTitle extends MDText {

	public MDTitle(String text, int level) {
		super(text);
		this.height = 20 + level;
	}

}
