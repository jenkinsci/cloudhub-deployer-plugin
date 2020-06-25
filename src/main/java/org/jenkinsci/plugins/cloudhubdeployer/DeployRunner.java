package org.jenkinsci.plugins.cloudhubdeployer;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import org.jenkinsci.plugins.cloudhubdeployer.common.ApiStatus;
import org.jenkinsci.plugins.cloudhubdeployer.common.RequestMode;
import org.jenkinsci.plugins.cloudhubdeployer.exception.CloudHubRequestException;
import org.jenkinsci.plugins.cloudhubdeployer.exception.ValidationException;
import org.jenkinsci.plugins.cloudhubdeployer.utils.CloudHubRequestUtils;
import org.jenkinsci.plugins.cloudhubdeployer.utils.Constants;
import org.jenkinsci.plugins.cloudhubdeployer.utils.DeployHelper;
import org.jenkinsci.plugins.cloudhubdeployer.utils.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.cloudhubdeployer.data.AppInfoJson;

import java.io.PrintStream;

public class DeployRunner {
    final Run<?, ?> run;

    final Launcher launcher;

    final TaskListener listener;

    final CloudHubDeployer cloudHubDeployer;

    final FilePath workspace;

    public DeployRunner(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener,
                        CloudHubDeployer cloudHubDeployer) {
        this.run = run;
        this.launcher = launcher;
        this.listener = listener;
        this.cloudHubDeployer = cloudHubDeployer;
        this.workspace = workspace;
    }

    public boolean perform() throws ValidationException, CloudHubRequestException, InterruptedException {
        final PrintStream logger = listener.getLogger();
        String loginResponseRaw;
        String cloudhubResponseBody;
        boolean apiStatus;
        String artifactPath = "null";

        if(!(cloudHubDeployer.getRequestMode().equals(RequestMode.DELETE)
                || cloudHubDeployer.getRequestMode().equals(RequestMode.RESTART))){
            artifactPath = DeployHelper.getArtifactPath(workspace, cloudHubDeployer.getFilePath());
        }

        CloudHubRequest cloudHubRequest = CloudHubRequestFactory.request().withUrl(Constants.CLOUDHUB_URL + Constants.URI_LOGIN)
                .withApiDomainName(cloudHubDeployer.getAppName())
                .withCloushubCredentials(StringUtils.isNotBlank(cloudHubDeployer.getCredentialsId()) ?
                        CredentialsProvider.findCredentialById(cloudHubDeployer.getCredentialsId(),
                                StandardCredentials.class, run) : null)
                .withFilePath(artifactPath)
                .withEnvironmetId(cloudHubDeployer.getEnvironmentId())
                .withAutoStart(Boolean.toString(cloudHubDeployer.isAutoStart()))
                .withTimeoutConnect(cloudHubDeployer.getTimeoutConnection())
                .withTimeoutResponse(cloudHubDeployer.getTimeoutResponse())
                .withDebugMode(cloudHubDeployer.getDebugMode())
                .withRequestMode(cloudHubDeployer.getRequestMode())
                .withLogger(logger)
                .build();


        if(!(cloudHubDeployer.getRequestMode().equals(RequestMode.UPDATE_FILE)
                || cloudHubDeployer.getRequestMode().equals(RequestMode.DELETE)
                || cloudHubDeployer.getRequestMode().equals(RequestMode.RESTART))){
            AppInfoJson appInfoJson= DeployHelper.buildAppInfoJson(cloudHubDeployer);
            cloudHubRequest.setAppInfoJson(appInfoJson);
        }


        loginResponseRaw = CloudHubRequestUtils.login(cloudHubRequest);

        if(JsonHelper.checkIfKeyExists(loginResponseRaw,Constants.LABEL_ACCESS_TOKEN))
            throw new CloudHubRequestException("CloudHub login request failed.");


        cloudHubRequest.setAccessToken(JsonHelper.parseAccessToken(loginResponseRaw));

        if(cloudHubRequest.getRequestMode().compareTo(RequestMode.CREATE_OR_UPDATE) == 0){
            cloudhubResponseBody = CloudHubRequestUtils.apiList(cloudHubRequest);

            boolean isApiPresent = JsonHelper.checkIfApiExists(cloudhubResponseBody,cloudHubRequest.getApiDomainName());

            if(isApiPresent){
                DeployHelper.logOutputStandard(logger,cloudHubRequest.getApiDomainName()
                        + " is already available in cloudhub doing update request.");
                cloudHubRequest.setRequestMode(RequestMode.UPDATE);
            }else {
                DeployHelper.logOutputStandard(logger,cloudHubRequest.getApiDomainName()
                        + " is not available in cloudhub doing create request.");
                cloudHubRequest.setRequestMode(RequestMode.CREATE);
            }
        }

        switch(cloudHubRequest.getRequestMode()) {
            case CREATE:
                cloudhubResponseBody = CloudHubRequestUtils.create(cloudHubRequest);
                break;
            case UPDATE:
                cloudhubResponseBody = CloudHubRequestUtils.update(cloudHubRequest);
                break;
            case UPDATE_FILE:
                cloudhubResponseBody = CloudHubRequestUtils.updateFile(cloudHubRequest);
                break;
            case RESTART:
                cloudhubResponseBody = CloudHubRequestUtils.restart(cloudHubRequest);
                break;
            case DELETE:
                cloudhubResponseBody = CloudHubRequestUtils.delete(cloudHubRequest);
                break;
            default:
                throw new CloudHubRequestException("Invalid request. " +
                        "Allowed values CREATE, UPDATE, CREATE_OR_UPDATE, UPDATE_FILE, RESTART or DELETE");
        }

        if(!cloudHubRequest.getRequestMode().equals(RequestMode.DELETE) && cloudHubDeployer.isVerifyDeployments()){
            DeployHelper.logOutputStandard(logger,"Verify Deployment is set. Verifying API Status");
            apiStatus = checkIfApiStarted(cloudHubRequest,logger, cloudHubDeployer.getVerifyIntervalInSeconds());
        }else {
            DeployHelper.logOutputStandard(logger,"Verify Deployment is not set. Check API status on cloudhub");
            apiStatus = true;
        }

        return apiStatus;
    }

    private static boolean checkIfApiStarted(CloudHubRequest cloudHubRequest, PrintStream logger, int verifyIntervalInSeconds)
            throws CloudHubRequestException, InterruptedException {

        boolean isDeploymentInProgress = true;
        boolean isApiStarted = false;

        Thread.sleep(Constants.DEFAULT_VERIFY_INITIAL_DELAY);
        if(cloudHubRequest.getRequestMode().equals(RequestMode.CREATE)){
            while (isDeploymentInProgress) {
                String apiStatus = CloudHubRequestUtils.apiStatus(cloudHubRequest);
                String status = new Gson().fromJson(apiStatus, JsonObject.class)
                        .get(Constants.LABEL_API_STATUS).getAsString();
                if(status.equals(ApiStatus.STARTED.toString())){
                    isDeploymentInProgress = false;
                    isApiStarted = true;
                    logger.println("API deployment is completed");
                }else if(status.equals(ApiStatus.DEPLOYING.toString())){
                    logger.println("API deployment is in progress");
                    Thread.sleep(verifyIntervalInSeconds);
                }else {
                    logger.println("Deployment failed. Please review the logs");
                }

            }
        }else {
            while (isDeploymentInProgress) {
                String apiStatus = CloudHubRequestUtils.apiStatus(cloudHubRequest);
                if (JsonHelper.checkIfKeyExists(apiStatus, Constants.LABEL_DEPLOYMENT_UPDATE_STATUS)) {
                    String deploymentUpdateStatus = new Gson().fromJson(apiStatus, JsonObject.class)
                            .get(Constants.LABEL_DEPLOYMENT_UPDATE_STATUS).getAsString();
                    logger.println("API update is in progress");
                    Thread.sleep(verifyIntervalInSeconds);
                }else {
                    String status = new Gson().fromJson(apiStatus, JsonObject.class)
                            .get(Constants.LABEL_API_STATUS).getAsString();
                    if(status.equals(ApiStatus.STARTED.toString())){
                        isApiStarted = true;
                        logger.println("API update is completed");
                    }else {
                        logger.println("Update failed. Please review the logs");
                    }
                    isDeploymentInProgress = false;
                }

            }
        }

        return isApiStarted;
    }

}
