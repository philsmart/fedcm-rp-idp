# fedcm-rp-idp

Basic spring boot projects to test the [FedCM](https://fedidcg.github.io/FedCM/) APIs for both the Identity Provider and Relying Party.

| Project | Description | Runnable |
|---------|-------------|----------|
| fedcm-model | The API model interfaces and implementation | no |
| fedcm-idp | A mocked Identity Provider that provides the endpoints require for FedCM testing | yes |
| fedcm-rp | A mocked Relying Party for initiating the FedCM API to authentication with the fedcm-idp | yes |
| fedcm-multi | A project that runs both fedcm-rp and fedcm-idp inside the same Tomcat container for testing both components on the same host | yes |

Both The RP and IdP can be run standalone in their own embedded Tomcat container on port 8080. 


## Building the projects

To build the projects:

1. Clone the repository
2. Ensure you have [Apache Maven](https://maven.apache.org/) and Java17 installed
3. From the root directory run: `mvn clean package`
4. This will create three runnable projects, each can be started as follows:
    1. For the IdP, `java -jar fedcm-idp/target/fedcm-idp-0.0.1-SNAPSHOT.jar`
    2. For the RP, `java -jar fedcm-rp/target/fedcm-rp-0.0.1-SNAPSHOT.jar`
    3. For both together, `java -jar fedcm-multi/target/fedcm-multi-0.0.1-SNAPSHOT.jar`
5. All components start on the standard HTTP port (8080), hence without adjustment you can not run the IdP and RP as standalone services on the same host — that is what the multi project is for. 
	1. To sign in to the IdP, navigate to `http://idp.localhost:8080/idp` and click sign in. You do not need any credentials, it just creates a session for you on the IdP.
	2. For the RP, navigate to `https://rp.localhost:8080/rp` and try out the FedCM features. 
	
## Configuring the projects

If you want to override any of the default properties, you can supply appropriate VM options when running the JAR. The following options are useful:

| Option | Default | Description |
|---------|--------|-------------|
|server.port|8080 | the server port | 
|fedcm.rp.idpConfig| Internal config for `http://idp.localhost:8080/fedcm.json` | location of the CredentialRequestOptions the RP uses to configure IdPs | 
| fedcm.idp.idpAccounts| Internal accounts JSON file | location of the IdentityProviderAccounts the IdP uses as the account information for the user|

Options are supplied using the standard Java command line system property syntax, for example (you only need to provide the options you need):

```
java -Dfedcm.idp.rootdomain=idp.localhost:8080 -Dserver.port=8080 -Dfedcm.idp.hostname=idp.localhost:8080 -Dfedcm.rp.idpConfig=file:config.json -jar fedcm-multi/target/fedcm-multi-0.0.1-SNAPSHOT.jar

```


# Copyright and License

   Copyright 2023 Jisc

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
