
package uk.ac.jisc.ti.demo.fedcm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityProviderConfig {

    private final String configURL;

    private final String clientId;

    private final String nonce;

    private IdentityProviderConfig(final Builder builder) {
        this.configURL = builder.configURL;
        this.clientId = builder.clientId;
        this.nonce = builder.nonce;
    }

    /**
     * @return the configURL
     */
    @JsonProperty("configURL")
    public final String getConfigURL() {
        return configURL;
    }

    /**
     * @return the clientId
     */
    @JsonProperty("clientId")
    public final String getClientId() {
        return clientId;
    }

    /**
     * @return the nonce
     */
    @JsonProperty("nonce")
    public final String getNonce() {
        return nonce;
    }

    public static IConfigURLStage builder() {
        return new Builder();
    }

    public interface IConfigURLStage {
        public IClientIdStage withConfigURL(String configURL);
    }

    public interface IClientIdStage {
        public IBuildStage withClientId(String clientId);
    }

    public interface IBuildStage {
        public IBuildStage withNonce(String nonce);

        public IdentityProviderConfig build();
    }

    public static final class Builder implements IConfigURLStage, IClientIdStage, IBuildStage {
        private String configURL;

        private String clientId;

        private String nonce;

        private Builder() {
        }

        @Override
        public IClientIdStage withConfigURL(final String configURL) {
            this.configURL = configURL;
            return this;
        }

        @Override
        public IBuildStage withClientId(final String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Override
        public IBuildStage withNonce(final String nonce) {
            this.nonce = nonce;
            return this;
        }

        @Override
        public IdentityProviderConfig build() {
            return new IdentityProviderConfig(this);
        }
    }

}
