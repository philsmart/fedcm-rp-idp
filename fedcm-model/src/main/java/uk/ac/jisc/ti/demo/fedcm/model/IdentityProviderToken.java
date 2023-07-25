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
