
package uk.ac.jisc.ti.demo.fedcm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialRequestOptions {

    private final IdentityCredentialRequestOptions identity;

    /**
     * @return the identity
     */
    @JsonProperty("identity")
    public final IdentityCredentialRequestOptions getIdentity() {
        return identity;
    }

    private CredentialRequestOptions(final Builder builder) {
        this.identity = builder.identity;
    }

    public static IIdentityStage builder() {
        return new Builder();
    }

    public interface IIdentityStage {
        public IBuildStage withIdentity(IdentityCredentialRequestOptions identity);
    }

    public interface IBuildStage {
        public CredentialRequestOptions build();
    }

    public static final class Builder implements IIdentityStage, IBuildStage {
        private IdentityCredentialRequestOptions identity;

        private Builder() {
        }

        @Override
        public IBuildStage withIdentity(final IdentityCredentialRequestOptions identity) {
            this.identity = identity;
            return this;
        }

        @Override
        public CredentialRequestOptions build() {
            return new CredentialRequestOptions(this);
        }
    }

}
