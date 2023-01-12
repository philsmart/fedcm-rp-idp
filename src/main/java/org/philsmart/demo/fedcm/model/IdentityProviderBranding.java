
package org.philsmart.demo.fedcm.model;

/**
 * FedCM manifest identity provider branding.
 *
 */
public class IdentityProviderBranding {

    private final String backgroundColor;

    private final String color;

    private IdentityProviderBranding(final Builder builder) {
        this.backgroundColor = builder.backgroundColor;
        this.color = builder.color;
    }

    /**
     * @return the backgroundColor
     */
    public final String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @return the color
     */
    public final String getColor() {
        return color;
    }

    public static IBackgroundColorStage builder() {
        return new Builder();
    }

    public interface IBackgroundColorStage {
        public IColorStage withBackgroundColor(String backgroundColor);
    }

    public interface IColorStage {
        public IBuildStage withColor(String color);
    }

    public interface IBuildStage {
        public IdentityProviderBranding build();
    }

    public static final class Builder implements IBackgroundColorStage, IColorStage, IBuildStage {
        private String backgroundColor;

        private String color;

        private Builder() {
        }

        @Override
        public IColorStage withBackgroundColor(final String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        @Override
        public IBuildStage withColor(final String color) {
            this.color = color;
            return this;
        }

        @Override
        public IdentityProviderBranding build() {
            return new IdentityProviderBranding(this);
        }
    }

    // todo icons
}
