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

/**
 * The IdP's fedCM manifest
 *
 */
public class IdentityProviderAPIConfig {

	private final String accountsEndpoint;

	private final String clientMetadataEndpoint;

	private final String idAssertionEndpoint;

	private final String signinUrl;

	private final IdentityProviderBranding branding;

	
	private IdentityProviderAPIConfig(Builder builder) {
		this.accountsEndpoint = builder.accountsEndpoint;
		this.clientMetadataEndpoint = builder.clientMetadataEndpoint;
		this.idAssertionEndpoint = builder.idAssertionEndpoint;
		this.signinUrl = builder.signinUrl;
		this.branding = builder.branding;
	}
	
	 /**
     * @return the accountsEndpoint
     */
    @JsonGetter("accounts_endpoint")
    public final String getAccountsEndpoint() {
        return accountsEndpoint;
    }

    /**
     * @return the clientMetadataEndpoint
     */
    @JsonGetter("client_metadata_endpoint")
    public final String getClientMetadataEndpoint() {
        return clientMetadataEndpoint;
    }

    /**
     * @return the idAssertionEndpoint
     */
    @JsonGetter("id_assertion_endpoint")
    public final String getIdAssertionEndpoint() {
        return idAssertionEndpoint;
    }
    
    /**
     * @return the idAssertionEndpoint
     */
    @JsonGetter("signin_url")
    public final String getSigninUrl() {
        return signinUrl;
    }

    /**
     * @return the branding
     */
    @JsonGetter("branding")
    public final IdentityProviderBranding getBranding() {
        return branding;
    }

	
	public static IAccountsEndpointStage builder() {
		return new Builder();
	}

	
	public interface IAccountsEndpointStage {
		public IClientMetadataEndpointStage withAccountsEndpoint(String accountsEndpoint);
	}

	
	public interface IClientMetadataEndpointStage {
		public IIdAssertionEndpointStage withClientMetadataEndpoint(String clientMetadataEndpoint);
	}

	
	public interface IIdAssertionEndpointStage {
		public IBuildStage withIdAssertionEndpoint(String idAssertionEndpoint);
	}

	
	public interface IBuildStage {
		public IBuildStage withSigninUrl(String signinUrl);

		public IBuildStage withBranding(IdentityProviderBranding branding);

		public IdentityProviderAPIConfig build();
	}

	
	public static final class Builder
			implements IAccountsEndpointStage, IClientMetadataEndpointStage, IIdAssertionEndpointStage, IBuildStage {
		private String accountsEndpoint;
		private String clientMetadataEndpoint;
		private String idAssertionEndpoint;
		private String signinUrl;
		private IdentityProviderBranding branding;

		private Builder() {
		}

		@Override
		public IClientMetadataEndpointStage withAccountsEndpoint(String accountsEndpoint) {
			this.accountsEndpoint = accountsEndpoint;
			return this;
		}

		@Override
		public IIdAssertionEndpointStage withClientMetadataEndpoint(String clientMetadataEndpoint) {
			this.clientMetadataEndpoint = clientMetadataEndpoint;
			return this;
		}

		@Override
		public IBuildStage withIdAssertionEndpoint(String idAssertionEndpoint) {
			this.idAssertionEndpoint = idAssertionEndpoint;
			return this;
		}

		@Override
		public IBuildStage withSigninUrl(String signinUrl) {
			this.signinUrl = signinUrl;
			return this;
		}

		@Override
		public IBuildStage withBranding(IdentityProviderBranding branding) {
			this.branding = branding;
			return this;
		}

		@Override
		public IdentityProviderAPIConfig build() {
			return new IdentityProviderAPIConfig(this);
		}
	}

}
