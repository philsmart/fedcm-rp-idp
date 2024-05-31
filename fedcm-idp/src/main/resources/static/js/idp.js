function isFedCMEnabled() {
  return !!window.IdentityCredential;
}

async function logout() {
	try {
		if (!isFedCMEnabled()) {
			return;
		}

		await IdentityCredential.logoutRPs([{
			url: "https://fedcm-demo.org/rp/logout",
			accountId: "1234",
		}]);
		console.log(`Logout out`);

	} catch (e) {
		console.log(`Logout failed ${e}`);
	}
};

function registerIdP(){
    IdentityProvider.register('http://idp.localhost:8080/idp/fedcm.json');
}

function unregisterIdP(){
     IdentityProvider.unregister('http://idp.localhost:8080/idp/fedcm.json');
}

