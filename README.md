# Cloudhub Deployment Jenkins Plugin

This plugin adds Cloudhub deployment abilities to build steps.

This plugin allows you to deploy into Cloudhub, Updating API with new Version, Restarting and Deleting API. 

## Overview and Setting up

In order to understand how this plugin works, you need to understand how Cloudhub and Api Deployment in Cloudhub works.

After building and installing the plugin, some simple configuration need to done for your project.

**Freestyle**

1. Open up your project configuration
1. In the `Add Build Step` section, select "CloudHub Deployment"
1. Application Name, Request Mode, Environment ID, and CloudHub Credentials are all
required options. Other Option may or may not be needed based on Request Mode.
1. For few configurations, there are two options
  1. Set them at global plugin configuration level(go to Manage Jenkins -> Configure System -> CloudHub Settings).
  To use this option dont uncheck the "Use global settings" check box.
  1. Or Set them at the CloudHub Deployment build step configuration, Simply uncheck the "Use global settings" check box 
  and fill the field.
1. Fill all other filed accordingly see help section if not sure. 

**Pipeline**

You can also use this plugin in pipeline (Jenkinsfile). 

1.  Create a [Jenkins Pipeline](https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Plugin) project
1.  Use the Pipeline Snippet Generator
1.  For 'Sample Step', choose 'step: General Build Step'
1.  For 'Build Step', choose 'CloudHub Deployment'
1.  Populate variables as described above and then 'Generate Groovy'

Here are some samples to use the plugin in pipeline script:

	cloudhubDeployer(environmentId :'XXXXX-XXXX-XXX-XXXX-XXXX-XXXXXXXX', orgId :'XXXXX-XXXX-XXX-XXXX-XXXX-XXXXXXXX', 
	requestMode : RequestMode.CREATE , appName :'some-application-name', credentialsId :'CLOUDHUB_ACCESS', 
	muleVersion :'4.3.0', region :'us-east-1', filePath :'target/*.jar', timeoutConnection : 90000, 
	timeoutResponse : 90000, debugMode : DebugMode.DISABLED, ignoreGlobalSettings : false, autoStart : true, 
	workerAmount : 1, workerType : 'Small', workerWeight : '0.2',workerCpu : '0.2', workerMemory : '1 GB', 
	monitoringEnabled : true, monitoringAutoRestart : true, loggingNgEnabled : true, persistentQueues : false, 
	persistentQueuesEncrypted : false, objectStoreV1 : false, envVars : [envVars(key : 'env' , value :  'dev')], 
	logLevels : [logLevels(levelCategory : LogLevelCategory.DEBUG ,packageName : 'packageName')], verifyDeployments : true)

License
-------

This plugin is licensed under MIT License. See the LICENSE file for more information.

