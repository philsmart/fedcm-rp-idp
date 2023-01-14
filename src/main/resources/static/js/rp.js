function isFedCMEnabled() {
  return !!window.IdentityCredential;
}
console.log(isFedCMEnabled() ? "Modern FedCM is available" : "Modern FedCM is not available");

function isOldFedCMEnabled() {
  return !(!window.FederatedCredential || !FederatedCredential.prototype.login);
}
console.log(isOldFedCMEnabled() ? "Old FedCM is available" : "Old FedCM is not available");

async function login() {
  try {
    if (!isOldFedCMEnabled() && !isFedCMEnabled()) {
      return;
    }

    // In this example, https://idp.example is the IdP's URL.
    var idToken = await navigator.credentials.get({
      identity: {
        providers: [{
          configURL: "https://fedcm-demo.org/fedcm.json", // IdP domain
          clientId: "https://fedcm-demo.org/", // Client ID of the RP
          nonce: "5678", // Nonce (random value)
        }]
      }
    });

    console.log(`received token: ${idToken.token}`);
  } catch (e) {
    console.log(`rejected with ${e}`);
  }
};