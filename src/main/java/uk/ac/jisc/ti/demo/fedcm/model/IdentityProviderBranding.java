
package uk.ac.jisc.ti.demo.fedcm.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * FedCM manifest identity provider branding.
 *
 */
public class IdentityProviderBranding {

	private final String backgroundColor;

	private final String color;

	private final List<IdentityProviderIcon> icons;

	private IdentityProviderBranding(Builder builder) {
		this.backgroundColor = builder.backgroundColor;
		this.color = builder.color;
		this.icons = builder.icons;
	}

	@JsonGetter("background_color")
	public String getBackgroundColor() {
		return backgroundColor;
	}

	@JsonGetter("color")
	public String getColor() {
		return color;
	}

	@JsonGetter("icons")
	public List<IdentityProviderIcon> getIcons() {
		return icons;
	}

	public static IBuildStage builder() {
		return new Builder();
	}

	public interface IBuildStage {
		public IBuildStage withBackgroundColor(String backgroundColor);

		public IBuildStage withColor(String color);

		public IBuildStage withIcons(List<IdentityProviderIcon> icons);

		public IdentityProviderBranding build();
	}

	public static final class Builder implements IBuildStage {
		private String backgroundColor;
		private String color;
		private List<IdentityProviderIcon> icons = Collections.emptyList();

		private Builder() {
		}

		@Override
		public IBuildStage withBackgroundColor(String backgroundColor) {
			this.backgroundColor = backgroundColor;
			return this;
		}

		@Override
		public IBuildStage withColor(String color) {
			this.color = color;
			return this;
		}

		@Override
		public IBuildStage withIcons(List<IdentityProviderIcon> icons) {
			this.icons = icons;
			return this;
		}

		@Override
		public IdentityProviderBranding build() {
			return new IdentityProviderBranding(this);
		}
	}

}
