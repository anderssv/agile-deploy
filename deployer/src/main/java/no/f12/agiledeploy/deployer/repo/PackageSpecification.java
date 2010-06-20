package no.f12.agiledeploy.deployer.repo;

import java.io.File;

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

	public File getInstallationPath(File parent, String environment) {
		return new File(parent, this.artifactId + "/" + environment + "/current");
	}

	@Override
	public String toString() {
		return this.groupId + ":" + this.artifactId + ":" + this.version;
	}

	public RepositoryInformation getRepositoryInformation() {
		return new RepositoryInformation(this);
	}

	public FileSystemInformation getFileSystemInformation() {
		return new FileSystemInformation(this);
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

	public class FileSystemInformation {
		private PackageSpecification spec;

		private FileSystemInformation(PackageSpecification spec) {
			this.spec = spec;
		}

		public File getArtifactPath(File workingPath) {
			return new File(workingPath, spec.getArtifactId());
		}

		public File getArtifactDataDirectory(File workingPath, String environment) {
			return new File(getArtifactEnvironmentDirectory(workingPath, environment), "data");
		}

		public File getArtifactEnvironmentDirectory(File workingPath, String environment) {
			return new File(getArtifactPath(workingPath), environment);
		}

		public File getArtifactPropertiesDirectory(File workingPath, String environment) {
			return new File(getArtifactInstallationDirectory(workingPath, environment), "config");
		}

		public File getArtifactInstallationDirectory(File workingPath, String environment) {
			File environmentFile = getArtifactEnvironmentDirectory(workingPath, environment);
			return getUnpackDirectory(environmentFile);
		}
		
		public File getUnpackDirectory(File environmentDirectory) {
			return new File(environmentDirectory, "current");
		}
	}
}
