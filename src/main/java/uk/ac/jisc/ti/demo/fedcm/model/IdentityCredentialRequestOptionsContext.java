package uk.ac.jisc.ti.demo.fedcm.model;

public enum IdentityCredentialRequestOptionsContext {
	
	SIGNIN("signin"),
	SIGNUP("signup"),
	USE("use"),
	CONTINUE("continue");

	private final String name;
	
	private IdentityCredentialRequestOptionsContext(final String value) {
		name = value;
	}
	
	public String getName(){
		return name;
	}
}
