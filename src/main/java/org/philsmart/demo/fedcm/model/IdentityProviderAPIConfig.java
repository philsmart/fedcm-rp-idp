
package org.philsmart.demo.fedcm.model;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * The IdP's fedCM manifest
 *
 */
public class IdentityProviderAPIConfig {

    private final String accountsEndpoint;

    private final String clientMetadataEndpoint;

    private final String idAssertionEndpoint;

    private final IdentityProviderBranding branding;

    private IdentityProviderAPIConfig(final Builder builder) {
        this.accountsEndpoint = builder.accountsEndpoint;
        this.clientMetadataEndpoint = builder.clientMetadataEndpoint;
        this.idAssertionEndpoint = builder.idAssertionEndpoint;
        this.branding = builder.branding;
    }

    @Override
    public String toString() {
        return "IdentityProviderAPIConfig [accountsEndpoint=" + accountsEndpoint + ", clientMetadataEndpoint="
                + clientMetadataEndpoint + ", idAssertionEndpoint=" + idAssertionEndpoint + ", branding=" + branding
                + "]";
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
        public IBrandingStage withIdAssertionEndpoint(String idAssertionEndpoint);
    }

    public interface IBrandingStage {
        public IBuildStage withBranding(IdentityProviderBranding branding);
    }

    public interface IBuildStage {
        public IdentityProviderAPIConfig build();
    }

    public static final class Builder implements IAccountsEndpointStage, IClientMetadataEndpointStage,
            IIdAssertionEndpointStage, IBrandingStage, IBuildStage {
        private String accountsEndpoint;

        private String clientMetadataEndpoint;

        private String idAssertionEndpoint;

        private IdentityProviderBranding branding;

        private Builder() {
        }

        @Override
        public IClientMetadataEndpointStage withAccountsEndpoint(final String accountsEndpoint) {
            this.accountsEndpoint = accountsEndpoint;
            return this;
        }

        @Override
        public IIdAssertionEndpointStage withClientMetadataEndpoint(final String clientMetadataEndpoint) {
            this.clientMetadataEndpoint = clientMetadataEndpoint;
            return this;
        }

        @Override
        public IBrandingStage withIdAssertionEndpoint(final String idAssertionEndpoint) {
            this.idAssertionEndpoint = idAssertionEndpoint;
            return this;
        }

        @Override
        public IBuildStage withBranding(final IdentityProviderBranding branding) {
            this.branding = branding;
            return this;
        }

        @Override
        public IdentityProviderAPIConfig build() {
            return new IdentityProviderAPIConfig(this);
        }
    }

}
