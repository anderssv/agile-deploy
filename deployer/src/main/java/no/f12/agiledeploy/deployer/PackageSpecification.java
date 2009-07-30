package no.f12.agiledeploy.deployer;

public class PackageSpecification {

	private String groupId;
	private String artifactId;
	private String version;
	private String packageType;

	public PackageSpecification(String groupId, String artifactId, String version) {
		this(groupId, artifactId, version, "zip");
	}

	public PackageSpecification(String groupId, String artifactId, String version, String packageType) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.packageType = packageType;
	}

	public String getArtifactPath() {
		return this.groupId.replaceAll("\\.", "/") + "/" + this.artifactId + "/" + version;
	}

	public String getArtifactFileName() {
		return artifactId + "-" + version;
	}

	public boolean isSnapshot() {
		return this.version.endsWith("-SNAPSHOT");
	}

	public String getMetadataFilename() {
		return "maven-metadata.xml";
	}

	public String getFullFilename() {
		return this.getArtifactPath() + "/" + this.getArtifactFileName();
	}

	public String getArtifactId() {
		return this.artifactId;
	}

	public String getPackageType() {
		return this.packageType;
	}

	public String getArtifactFilename(String snapshotReplacement) {
		return this.getArtifactFileName().replaceAll("-SNAPSHOT", "-" + snapshotReplacement);
	}

}
