package no.f12.agiledeploy.deployer.repo;


public class PackageSpecification {

	private String groupId;
	private String artifactId;
	private String version;
	private String packageType;

	public PackageSpecification(String groupId, String artifactId, String version, String packageType) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.packageType = packageType;
	}

	public String getArtifactFileName() {
		return artifactId + "-" + version;
	}

	public boolean isSnapshot() {
		return this.version.endsWith("-SNAPSHOT");
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

	@Override
	public String toString() {
		return this.groupId + ":" + this.artifactId + ":" + this.version;
	}

	public RepositoryInformation getRepositoryInformation() {
		return new RepositoryInformation(this);
	}

	public class RepositoryInformation {
		private PackageSpecification spec;

		private RepositoryInformation(PackageSpecification spec) {
			this.spec = spec;
		}

		public String getArtifactPath() {
			return getArtifactInternalPath() + "/" + version;
		}

		private String getArtifactInternalPath() {
			return spec.groupId.replaceAll("\\.", "/") + "/" + spec.artifactId;
		}

		public String getFullFilename() {
			return this.getArtifactPath() + "/" + spec.getArtifactFileName();
		}

		public String getMetadataFilename() {
			return "maven-metadata.xml";
		}

		public String getMetadataPath() {
			if (spec.isSnapshot()) {
				return this.getArtifactPath();
			}
			return this.getArtifactInternalPath();
		}
	}

}
