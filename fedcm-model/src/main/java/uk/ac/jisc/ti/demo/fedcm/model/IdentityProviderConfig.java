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
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("providers")
@JsonDeserialize(builder = IdentityProviderConfig.Builder.class)
public class IdentityProviderConfig {

    private final String configURL;

    private final String clientId;

    private final String nonce;

    private final String loginHint;

    private final List<String> scope;

    private final String responseType;

    private final Map<String, String> params;

    private IdentityProviderConfig(final Builder builder) {
        this.configURL = builder.configURL;
        this.clientId = builder.clientId;
        this.nonce = builder.nonce;
        this.loginHint = builder.loginHint;
        this.scope = builder.scope;
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
    public List<String> getScope() {
        return scope;
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

        public IBuildStage withScope(List<String> scopes);

        public IBuildStage withResponseType(String responseType);

        public IBuildStage withParams(Map<String, String> params);

        public IdentityProviderConfig build();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static final class Builder implements IConfigURLStage, IClientIdStage, INonceStage, IBuildStage {
        private String configURL;

        private String clientId;

        private String nonce;

        private String loginHint;

        private List<String> scope = Collections.emptyList();

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
        public IBuildStage withScope(final List<String> scopes) {
            this.scope = scopes;
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

	@Override
	public String toString() {
		return "IdentityProviderConfig [configURL=" + configURL + ", clientId=" + clientId + ", nonce=" + nonce
				+ ", loginHint=" + loginHint + ", scope=" + scope + ", responseType=" + responseType + ", params="
				+ params + "]";
	}

}
