
package uk.ac.jisc.ti.demo.fedcm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Token response type.
 */
@JsonInclude(Include.NON_NULL)
public class IdentityProviderToken {

    private final String token;

    private final String continueOn;

    private IdentityProviderToken(final Builder builder) {
        this.token = builder.token;
        this.continueOn = builder.continueOn;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("continue_on")
    public String getContinueOn() {
        return continueOn;
    }

    public static ITokenStage builder() {
        return new Builder();
    }

    public interface ITokenStage {
        public IBuildStage withToken(String token);
    }

    public interface IBuildStage {
        public IBuildStage withContinueOn(String continueOn);

        public IdentityProviderToken build();
    }

    public static final class Builder implements ITokenStage, IBuildStage {
        private String token;

        private String continueOn;

        private Builder() {
        }

        @Override
        public IBuildStage withToken(final String token) {
            this.token = token;
            return this;
        }

        @Override
        public IBuildStage withContinueOn(final String continueOn) {
            this.continueOn = continueOn;
            return this;
        }

        @Override
        public IdentityProviderToken build() {
            return new IdentityProviderToken(this);
        }
    }

    @Override
    public String toString() {
        return "IdentityProviderToken [token=" + token + ", continueOn=" + continueOn + "]";
    }

}
