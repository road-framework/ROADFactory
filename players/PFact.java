
public class PFact {
	private String name;
	private String source;
	private PFactIdentifier identifier;
	private PFactAttributes attributes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setIdentifier(PFactIdentifier identifier) {
		this.identifier = identifier;
	}

	public PFactIdentifier getIdentifier() {
		return identifier;
	}

	public void setAttributes(PFactAttributes attributes) {
		this.attributes = attributes;
	}

	public PFactAttributes getAttributes() {
		return attributes;
	}

}
