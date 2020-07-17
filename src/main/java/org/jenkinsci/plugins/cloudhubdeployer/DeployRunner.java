package org.jenkinsci.plugins.cloudhubdeployer;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.jenkinsci.plugins.cloudhubdeployer.common.RequestMode;
import org.jenkinsci.plugins.cloudhubdeployer.exception.CloudHubRequestException;
import org.jenkinsci.plugins.cloudhubdeployer.exception.ValidationException;
import org.jenkinsci.plugins.cloudhubdeployer.utils.CloudHubRequestUtils;
import org.jenkinsci.plugins.cloudhubdeployer.utils.Constants;
import org.jenkinsci.plugins.cloudhubdeployer.utils.DeployHelper;
import org.jenkinsci.plugins.cloudhubdeployer.utils.JsonHelper;
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

        CloudHubRequest cloudHubRequest = CloudHubRequestFactory.request().withUrl(Constants.CLOUDHUB_URL + Constants.URI_LOGIN)
                .withApiDomainName(cloudHubDeployer.getAppName())
                .withCloushubCredentials(StringUtils.isNotBlank(cloudHubDeployer.getCredentialsId()) ?
                        CredentialsProvider.findCredentialById(cloudHubDeployer.getCredentialsId(),
                                StandardCredentials.class, run) : null)
                .withOrgId(cloudHubDeployer.getOrgId())
                .withEnvironmetId(cloudHubDeployer.getEnvironmentId())
                .withAutoStart(Boolean.toString(cloudHubDeployer.isAutoStart()))
                .withTimeoutConnect(cloudHubDeployer.getTimeoutConnection())
                .withTimeoutResponse(cloudHubDeployer.getTimeoutResponse())
                .withDebugMode(cloudHubDeployer.getDebugMode())
                .withRequestMode(cloudHubDeployer.getRequestMode())
                .withFilePath(workspace, cloudHubDeployer.getFilePath())
                .withAutoScalepolicy(cloudHubDeployer.getEnableAutoScalePolicy(),cloudHubDeployer.getAutoScalePolicy())
                .withLogger(logger)
                .build();


        if(!(cloudHubDeployer.getRequestMode().equals(RequestMode.UPDATE_FILE)
                || cloudHubDeployer.getRequestMode().equals(RequestMode.DELETE)
                || cloudHubDeployer.getRequestMode().equals(RequestMode.RESTART))){
            AppInfoJson appInfoJson= DeployHelper.buildAppInfoJson(cloudHubDeployer);
            cloudHubRequest.setAppInfoJson(appInfoJson);
        }


        loginResponseRaw = CloudHubRequestUtils.login(cloudHubRequest);

        if(!JsonHelper.checkIfKeyExists(loginResponseRaw,Constants.JSON_KEY_ACCESS_TOKEN))
            throw new CloudHubRequestException("CloudHub login request failed.");


        cloudHubRequest.setAccessToken(JsonHelper.parseAccessToken(loginResponseRaw));

        cloudhubResponseBody = null;

        cloudhubResponseBody = CloudHubRequestUtils.envList(cloudHubRequest);

        if(cloudhubResponseBody != null){
            String envID = DeployHelper.verifyOrGetEnvId(cloudhubResponseBody,cloudHubRequest.getEnvId());

            if(null == envID){
                throw new CloudHubRequestException("No environment exists with given name or id");
            }

            cloudHubRequest.setEnvId(envID);
        }

        cloudHubRequest.setRequestMode(DeployHelper.getFinalRequestMode(cloudHubRequest));

        cloudhubResponseBody = null;

        DeployHelper.logOutputStandard(logger, "Doing " + cloudHubRequest.getRequestMode().toString().toLowerCase()
                + " request for " + cloudHubRequest.getApiDomainName());

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

        if(null != cloudhubResponseBody) {
            DeployHelper.logOutputStandard(logger, "API " + cloudHubRequest.getRequestMode().toString().toLowerCase()
                    + " request on CloudHub is successful");
        }


        if(!cloudHubRequest.getRequestMode().equals(RequestMode.DELETE) && cloudHubDeployer.isVerifyDeployments()){
            DeployHelper.logOutputStandard(logger,"Verify deployment is set. Waiting for API to get started.");
            apiStatus = DeployHelper.checkIfApiStarted(cloudHubRequest,logger, cloudHubDeployer.getVerifyIntervalInSeconds());
        }else {
            DeployHelper.logOutputStandard(logger,"Verify deployment is not set. Check API status on CloudHub");
            apiStatus = true;
            DeployHelper.logOutputStandard(logger,"Deployment Success");
        }


        if(cloudHubRequest.isAutoScalePolicyEnabled()){

            JsonArray policyJsonArray = DeployHelper.checkIfAutoScalePolicyExists(cloudHubRequest,logger);

            cloudHubRequest.setAutoScalePolicy(DeployHelper.formatAutoScalePolicy(
                    cloudHubRequest.getAutoScalePolicy()));

            if(policyJsonArray.size() > 0){

                DeployHelper.logOutputStandard(logger,"Updating autoscale policy for " +
                        cloudHubRequest.getApiDomainName());

                cloudHubRequest.setAutoScalePolicy(DeployHelper.getFinalAutoScalePolicy(
                        cloudHubRequest.getAutoScalePolicy(),policyJsonArray));

                cloudhubResponseBody = CloudHubRequestUtils.updateAutoScalePolicy(cloudHubRequest);
            }else{

                DeployHelper.logOutputStandard(logger,"Creating autoscale policy for " +
                        cloudHubRequest.getApiDomainName());

                cloudhubResponseBody = CloudHubRequestUtils.createAutoScalePolicy(cloudHubRequest);
            }

            if(cloudhubResponseBody != null)
                DeployHelper.logOutputStandard(logger,"AutoScale Policy Applied to " +
                        cloudHubRequest.getApiDomainName());
        }

        return apiStatus;
    }

}
