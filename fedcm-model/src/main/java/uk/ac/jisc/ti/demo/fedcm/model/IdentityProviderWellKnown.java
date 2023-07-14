package uk.ac.jisc.ti.demo.fedcm.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.annotation.Nonnull;

public class IdentityProviderWellKnown {
	
	private final List<String> providerURLs;
	
	/**
	 * Constructor 
	 * @param urls the provider URLs
	 */
	public IdentityProviderWellKnown(@Nonnull List<String> urls) {
		providerURLs = urls;
	}

	/**
	 * Get the providerURLs.
	 *
	 * @return the providerURLs
	 */
	@JsonGetter("provider_urls")
	public List<String> getProviderURLs() {
		return providerURLs;
	}


}
