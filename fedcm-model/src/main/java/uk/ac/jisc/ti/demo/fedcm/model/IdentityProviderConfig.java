
package uk.ac.jisc.ti.demo.fedcm.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class IdentityProviderConfig {

    private final String configURL;

    private final String clientId;

    private final String nonce;

    private final String loginHint;

    private final List<String> scopes;

    private final String responseType;

    private final Map<String, String> params;

    private IdentityProviderConfig(final Builder builder) {
        this.configURL = builder.configURL;
        this.clientId = builder.clientId;
        this.nonce = builder.nonce;
        this.loginHint = builder.loginHint;
        this.scopes = builder.scopes;
        this.responseType = builder.responseType;
        this.params = builder.params;
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

    @JsonProperty("loginHint")
    public String getLoginHint() {
        return loginHint;
    }

    @JsonProperty("scope")
    public List<String> getScopes() {
        return scopes;
    }

    @JsonProperty("params")
    @JsonInclude(Include.NON_EMPTY)
    public Map<String, String> getParams() {
        return params;
    }

    public String getResponseType() {
        return responseType;
    }

    public static IConfigURLStage builder() {
        return new Builder();
    }

    public interface IConfigURLStage {
        public IClientIdStage withConfigURL(String configURL);
    }

    public interface IClientIdStage {
        public INonceStage withClientId(String clientId);
    }

    public interface INonceStage {
        public IBuildStage withNonce(String nonce);
    }

    public interface IBuildStage {
        public IBuildStage withLoginHint(String loginHint);

        public IBuildStage withScopes(List<String> scopes);

        public IBuildStage withResponseType(String responseType);

        public IBuildStage withParams(Map<String, String> params);

        public IdentityProviderConfig build();
    }

    public static final class Builder implements IConfigURLStage, IClientIdStage, INonceStage, IBuildStage {
        private String configURL;

        private String clientId;

        private String nonce;

        private String loginHint;

        private List<String> scopes = Collections.emptyList();

        private String responseType;

        private Map<String, String> params = Collections.emptyMap();

        private Builder() {
        }

        @Override
        public IClientIdStage withConfigURL(final String configURL) {
            this.configURL = configURL;
            return this;
        }

        @Override
        public INonceStage withClientId(final String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Override
        public IBuildStage withNonce(final String nonce) {
            this.nonce = nonce;
            return this;
        }

        @Override
        public IBuildStage withLoginHint(final String loginHint) {
            this.loginHint = loginHint;
            return this;
        }

        @Override
        public IBuildStage withScopes(final List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        @Override
        public IBuildStage withResponseType(final String responseType) {
            this.responseType = responseType;
            return this;
        }

        @Override
        public IBuildStage withParams(final Map<String, String> params) {
            this.params = params;
            return this;
        }

        @Override
        public IdentityProviderConfig build() {
            return new IdentityProviderConfig(this);
        }
    }

}
