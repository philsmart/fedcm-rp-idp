/*
 * Copyright 2023 Jisc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package uk.ac.jisc.ti.demo.fedcm.model;

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
