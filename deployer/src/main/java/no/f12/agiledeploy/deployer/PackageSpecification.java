package no.f12.agiledeploy.deployer;

public class PackageSpecification {

	private String groupId;
	private String artifactId;
	private String version;
	private boolean snapshot;

	public PackageSpecification(String groupId, String artifactId, String version, boolean snapshot) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.snapshot = snapshot;
	}

	public PackageSpecification(String groupId, String artifactId, String version) {
		this(groupId, artifactId, version, false);
	}

	public String getArtifactPath() {
		String result = this.groupId.replaceAll("\\.", "/") + "/" + this.artifactId + "/" + version;
		return suffixIfSnapshot(result);
	}

	public Object getArtifactFileName() {
		String fileName = artifactId + "-" + version;
		return suffixIfSnapshot(fileName);
	}

	private String suffixIfSnapshot(String fileName) {
		if (isSnapshot()) {
			fileName += "-SNAPSHOT";
		}
		return fileName;
	}

	private boolean isSnapshot() {
		return this.snapshot;
	}

	public String getMetadataFilename() {
		return getArtifactPath() + "/maven-metadata.xml";
	}

	public String getFullFilename() {
		return this.getArtifactPath() + "/" + this.getArtifactFileName();
	}

	public String getArtifactId() {
		return this.artifactId;
	}

}
