package org.philsmart.demo.fedcm.model;

import com.fasterxml.jackson.annotation.JsonGetter;

/** IdentityProviderIcon model class.*/
public class IdentityProviderIcon {

	private String url;

	private long size;

	private IdentityProviderIcon(Builder builder) {
		this.url = builder.url;
		this.size = builder.size;
	}

	@JsonGetter("url")
	public String getUrl() {
		return url;
	}

	@JsonGetter("size")
	public long getSize() {
		return size;
	}

	public static IUrlStage builder() {
		return new Builder();
	}

	public interface IUrlStage {
		public IBuildStage withUrl(String url);
	}

	public interface IBuildStage {
		public IBuildStage withSize(long size);

		public IdentityProviderIcon build();
	}

	public static final class Builder implements IUrlStage, IBuildStage {
		private String url;
		private long size;

		private Builder() {
		}

		@Override
		public IBuildStage withUrl(String url) {
			this.url = url;
			return this;
		}

		@Override
		public IBuildStage withSize(long size) {
			this.size = size;
			return this;
		}

		@Override
		public IdentityProviderIcon build() {
			return new IdentityProviderIcon(this);
		}
	}

}
