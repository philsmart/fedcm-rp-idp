
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
