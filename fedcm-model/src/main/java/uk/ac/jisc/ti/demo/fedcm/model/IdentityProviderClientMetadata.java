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

public class IdentityProviderClientMetadata {

    private final String privacyPolicyUrl;

    private final String termsOfServiceUrl;

    private IdentityProviderClientMetadata(final Builder builder) {
        this.privacyPolicyUrl = builder.privacyPolicyUrl;
        this.termsOfServiceUrl = builder.termsOfServiceUrl;
    }

    @Override
    public String toString() {
        return "IdentityProviderClientMetadata [privacyPolicyUrl=" + privacyPolicyUrl + ", termsOfServiceUrl="
                + termsOfServiceUrl + "]";
    }

    /**
     * @return the privacyPolicyUrl
     */
    @JsonGetter("privacy_policy_url")
    public final String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    /**
     * @return the termsOfServiceUrl
     */
    @JsonGetter("terms_of_service_url")
    public final String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public static IBuildStage builder() {
        return new Builder();
    }

    public interface IBuildStage {
        public IBuildStage withPrivacyPolicyUrl(String privacyPolicyUrl);

        public IBuildStage withTermsOfServiceUrl(String termsOfServiceUrl);

        public IdentityProviderClientMetadata build();
    }

    public static final class Builder implements IBuildStage {
        private String privacyPolicyUrl;

        private String termsOfServiceUrl;

        private Builder() {
        }

        @Override
        public IBuildStage withPrivacyPolicyUrl(final String privacyPolicyUrl) {
            this.privacyPolicyUrl = privacyPolicyUrl;
            return this;
        }

        @Override
        public IBuildStage withTermsOfServiceUrl(final String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
            return this;
        }

        @Override
        public IdentityProviderClientMetadata build() {
            return new IdentityProviderClientMetadata(this);
        }
    }

}
