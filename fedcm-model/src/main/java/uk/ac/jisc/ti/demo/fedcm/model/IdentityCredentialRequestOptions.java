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

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityCredentialRequestOptions {

	private final List<IdentityProviderConfig> providers;

	private final IdentityCredentialRequestOptionsContext context;

    /**
     * @return the providers
     */
    @JsonProperty("providers")
    public final List<IdentityProviderConfig> getProviders() {
        return providers;
    }
    
    @JsonProperty("context")
    public final String getContext() {
    	return context.getName();
    }

	private IdentityCredentialRequestOptions(Builder builder) {
		this.providers = builder.providers;
		this.context = builder.context;
	}


	public static IProvidersStage builder() {
		return new Builder();
	}


	public interface IProvidersStage {
		public IBuildStage withProviders(List<IdentityProviderConfig> providers);
	}


	public interface IBuildStage {
		public IBuildStage withContext(IdentityCredentialRequestOptionsContext context);

		public IdentityCredentialRequestOptions build();
	}


	public static final class Builder implements IProvidersStage, IBuildStage {
		private List<IdentityProviderConfig> providers = Collections.emptyList();
		private IdentityCredentialRequestOptionsContext context = IdentityCredentialRequestOptionsContext.SIGNIN;

		private Builder() {
		}

		@Override
		public IBuildStage withProviders(List<IdentityProviderConfig> providers) {
			this.providers = providers;
			return this;
		}

		@Override
		public IBuildStage withContext(IdentityCredentialRequestOptionsContext context) {
			this.context = context;
			return this;
		}

		@Override
		public IdentityCredentialRequestOptions build() {
			return new IdentityCredentialRequestOptions(this);
		}
	}

}
