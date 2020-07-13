package org.jenkinsci.plugins.cloudhubdeployer;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.google.common.base.Strings;
import hudson.FilePath;
import org.jenkinsci.plugins.cloudhubdeployer.common.DebugMode;
import org.jenkinsci.plugins.cloudhubdeployer.common.RequestMode;
import org.jenkinsci.plugins.cloudhubdeployer.data.AppInfoJson;
import org.jenkinsci.plugins.cloudhubdeployer.data.AutoScalePolicy;
import org.jenkinsci.plugins.cloudhubdeployer.exception.CloudHubRequestException;
import org.jenkinsci.plugins.cloudhubdeployer.exception.ValidationException;
import org.jenkinsci.plugins.cloudhubdeployer.utils.DeployHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Factory to create <code>Cloudhub Request</code> objects
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public class CloudHubRequestFactory {

    private final CloudHubRequest cloudHubRequest;

    private CloudHubRequestFactory() {
        this.cloudHubRequest = new CloudHubRequest();
    }

    public static CloudHubRequestFactory request() {
        return new CloudHubRequestFactory();
    }

    public CloudHubRequestFactory withUrl(final String url) {
        cloudHubRequest.setUrl(url);
        return this;
    }

    public CloudHubRequestFactory withApiDomainName(final String apiDomainName) {
        cloudHubRequest.setApiDomainName(apiDomainName);
        return this;
    }

    public CloudHubRequestFactory withCloushubCredentials(final StandardCredentials cloudhubCredentials) {
        cloudHubRequest.setCloudhubCredentials(cloudhubCredentials);
        return this;
    }

    public CloudHubRequestFactory withOrgId(String orgId) {
        cloudHubRequest.setOrgId(orgId);
        return this;
    }

    public CloudHubRequestFactory withEnvironmetId(final String value) {
        cloudHubRequest.setEnvId(value);
        return this;
    }

    public CloudHubRequestFactory withTimeoutConnect(final int timeoutConnect) {
        cloudHubRequest.setTimeoutConnect(timeoutConnect);
        return this;
    }

    public CloudHubRequestFactory withTimeoutResponse(final int timeoutResponse) {
        cloudHubRequest.setTimeoutResponse(timeoutResponse);
        return this;
    }

    public CloudHubRequestFactory withLogger(final PrintStream logger) {
        cloudHubRequest.setLogger(logger);
        return this;
    }

    public CloudHubRequestFactory withRequestMode(final RequestMode requestMode) {
        cloudHubRequest.setRequestMode(requestMode);
        return this;
    }

    public CloudHubRequestFactory withDebugMode(final DebugMode debugMode) {
        cloudHubRequest.setDebugMode(debugMode);
        return this;
    }

    public CloudHubRequestFactory withAutoStart(final String autoStart) {
        cloudHubRequest.setAutoStart(autoStart);
        return this;
    }

    public CloudHubRequestFactory withAppInfoJson(final AppInfoJson appInfoJson) {
        cloudHubRequest.setAppInfoJson(appInfoJson);
        return this;
    }

    public CloudHubRequestFactory withAccessToken(final String accessToken) {
        cloudHubRequest.setAccessToken(accessToken);
        return this;
    }

    public CloudHubRequestFactory withAutoScalepolicy(Boolean enableAutoScalePolicy, List<AutoScalePolicy> autoScalePolicy) {
        cloudHubRequest.setAutoScalePolicyEnabled(enableAutoScalePolicy);
        cloudHubRequest.setAutoScalePolicy(autoScalePolicy);
        return this;
    }

    public CloudHubRequestFactory withFilePath(FilePath workspace, final String filePath) {
        FilePath[] filePaths;
        try {

            if(null == filePath)
                return null;

            filePaths = workspace.list(filePath);

            if(filePaths.length > 0){
                cloudHubRequest.setFilePath(filePaths[0].getRemote());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return this;
    }

    public CloudHubRequest build() throws ValidationException {
        this.validate();
        return this.cloudHubRequest;
    }

    private boolean validate() throws ValidationException {

        if (Strings.isNullOrEmpty(cloudHubRequest.getApiDomainName())) {
            throw new ValidationException("Empty domain name");
        }

        if (null == cloudHubRequest.getCloudhubCredentials()) {
            throw new ValidationException("Empty credentials Id");
        }

        if (Strings.isNullOrEmpty(cloudHubRequest.getOrgId())) {
            throw new ValidationException("Empty organization Id");
        }

        if (Strings.isNullOrEmpty(cloudHubRequest.getEnvId())) {
            throw new ValidationException("Empty environment Id");
        }

        if (Strings.isNullOrEmpty(cloudHubRequest.getFilePath())) {
            throw new ValidationException("Empty file path");
        }

        if (null == this.cloudHubRequest.getRequestMode()) {
            throw new ValidationException("Null request mode");
        }

        if (null == this.cloudHubRequest.getLogger()) {
            throw new ValidationException("Null logger");
        }

        if (0 == this.cloudHubRequest.getTimeoutConnect()) {
            throw new ValidationException("0 connection timeout");
        }

        if (0 == this.cloudHubRequest.getTimeoutResponse()) {
            throw new ValidationException("0 response timeout");
        }

        if (!DebugMode.isMember(this.cloudHubRequest.getDebugMode())) {
            throw new ValidationException("Invalid debug mode");
        }

        if(!RequestMode.isMember(this.cloudHubRequest.getRequestMode())){
            throw new ValidationException("Invalid request mode");
        }

        if(this.cloudHubRequest.isAutoScalePolicyEnabled()){
            DeployHelper.validateAutoScalePolicy(this.cloudHubRequest.getAutoScalePolicy());
        }

        return true;
    }

}
