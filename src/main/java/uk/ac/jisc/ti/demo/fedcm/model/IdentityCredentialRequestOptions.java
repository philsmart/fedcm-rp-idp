
package uk.ac.jisc.ti.demo.fedcm.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityCredentialRequestOptions {

    private final List<IdentityProviderConfig> providers;

    private IdentityCredentialRequestOptions(final Builder builder) {
        this.providers = builder.providers;
    }

    /**
     * @return the providers
     */
    @JsonProperty("providers")
    public final List<IdentityProviderConfig> getProviders() {
        return providers;
    }

    public static IProvidersStage builder() {
        return new Builder();
    }

    public interface IProvidersStage {
        public IBuildStage withProviders(List<IdentityProviderConfig> providers);
    }

    public interface IBuildStage {
        public IdentityCredentialRequestOptions build();
    }

    public static final class Builder implements IProvidersStage, IBuildStage {
        private List<IdentityProviderConfig> providers = Collections.emptyList();

        private Builder() {
        }

        @Override
        public IBuildStage withProviders(final List<IdentityProviderConfig> providers) {
            this.providers = providers;
            return this;
        }

        @Override
        public IdentityCredentialRequestOptions build() {
            return new IdentityCredentialRequestOptions(this);
        }
    }

}
