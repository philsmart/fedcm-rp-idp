
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
