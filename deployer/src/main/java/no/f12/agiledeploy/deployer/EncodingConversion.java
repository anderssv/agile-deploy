package no.f12.agiledeploy.deployer;

public class EncodingConversion {

	private String path;
	private String sourceEncoding;
	private String targetEncoding;

	public EncodingConversion(String path, String sourceEncoding, String targetEncoding) {
		this.path = path;
		this.sourceEncoding = sourceEncoding;
		this.targetEncoding = targetEncoding;
	}

	public String getPath() {
		return path;
	}

	public String getSourceEncoding() {
		return sourceEncoding;
	}

	public String getTargetEncoding() {
		return targetEncoding;
	}

}
