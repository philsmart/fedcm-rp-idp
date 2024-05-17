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

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = IdentityCredentialRequestOptions.Builder.class)
public class IdentityCredentialRequestOptions {

	private final List<IdentityProviderConfig> providers;

	private final IdentityCredentialRequestOptionsContext context;

	private final String mode;
	
	private final String mediation;

	
	private IdentityCredentialRequestOptions(Builder builder) {
		this.providers = builder.providers;
		this.context = builder.context;
		this.mode = builder.mode;
		this.mediation = builder.mediation;
	}

	/**
	 * @return the providers
	 */
	@JsonProperty("providers")
	public final List<IdentityProviderConfig> getProviders() {
		return providers;
	}

	@JsonProperty("context")
	@JsonInclude(Include.NON_EMPTY)
	public final String getContext() {
		return context != null ? context.getName() : null;
	}
	
	@JsonProperty("mode")
	@JsonInclude(Include.NON_EMPTY)
	public final String getMode() {
		return mode;
	}
	
	@JsonProperty("mediation")
	@JsonInclude(Include.NON_EMPTY)
	public String getMediation() {
		return mediation;
	}

	
	public static Builder builder() {
		return new Builder();
	}
	
	

	@Override
	public String toString() {
		return "IdentityCredentialRequestOptions [providers=" + providers + ", context=" + context + ", mode=" + mode
				+ "]";
	}



	@JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
	public static final class Builder {
		private List<IdentityProviderConfig> providers = Collections.emptyList();
		private IdentityCredentialRequestOptionsContext context;
		private String mode;
		private String mediation;

		private Builder() {
		}

		public Builder withProviders(List<IdentityProviderConfig> providers) {
			this.providers = providers;
			return this;
		}

		public Builder withContext(IdentityCredentialRequestOptionsContext context) {
			this.context = context;
			return this;
		}

		public Builder withMode(String mode) {
			this.mode = mode;
			return this;
		}
		
		public Builder withMediation(String mediation) {
			this.mediation = mediation;
			return this;
		}

		public IdentityCredentialRequestOptions build() {
			return new IdentityCredentialRequestOptions(this);
		}
	}

}
