package org.jenkinsci.plugins.cloudhubdeployer;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.*;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;
import hudson.model.queue.Tasks;
import org.jenkinsci.plugins.cloudhubdeployer.common.RequestMode;
import org.jenkinsci.plugins.cloudhubdeployer.data.AutoScalePolicy;
import org.jenkinsci.plugins.cloudhubdeployer.data.LogLevels;
import org.jenkinsci.plugins.cloudhubdeployer.exception.CloudHubRequestException;
import org.jenkinsci.plugins.cloudhubdeployer.exception.ValidationException;
import org.jenkinsci.plugins.cloudhubdeployer.utils.Constants;
import hudson.*;
import hudson.model.*;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import org.jenkinsci.plugins.cloudhubdeployer.common.DebugMode;
import com.google.common.base.Strings;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.*;
import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import lombok.Getter;

import static com.cloudbees.plugins.credentials.CredentialsMatchers.anyOf;
import static com.cloudbees.plugins.credentials.CredentialsMatchers.instanceOf;


public class CloudHubDeployer extends Builder implements SimpleBuildStep {

    private static Logger LOGGER = Logger.getLogger(CloudHubDeployer.class.getName());

    /**
     * CloudHub Environment to Deploy
     */
    @Setter
    @Getter
    private String environmentId;

    /**
     * CloudHub Organization Id
     */
    @Setter
    @Getter
    private String orgId;

    /**
     * Operation to perform on api
     */
    @Setter
    @Getter
    private RequestMode requestMode;

    /**
     *  connection timeout request
     */
    @Setter
    @Getter
    private int timeoutConnection;

    /**
     * connection timeout request
     */
    @Setter
    @Getter
    private int timeoutResponse;

    /**
     * Run deployment in debug mode
     */
    @Setter
    @Getter
    private DebugMode debugMode;

    /**
     * Ignore Global Settings
     */
    @Setter
    @Getter
    private boolean ignoreGlobalSettings;

    /**
     * CloudHub app domain name
     */
    @Setter
    @Getter
    private String appName;

    /**
     * Mule Runtime Version
     */
    @Setter
    @Getter
    private String muleVersion;

    /**
     * Deployment Region
     */
    @Setter
    @Getter
    private String region;

    /**
     * Automatically restart application when not responding
     */
    @Setter
    @Getter
    private boolean autoStart;

    /**
     * CloudHub Credentials
     */
    @Setter
    @Getter
    private String credentialsId;

    /**
     * The path to the package to deploy relative to the workspace.
     */
    @Setter
    @Getter
    private String filePath;

    /**
     * Number of workers for API
     */
    @Setter
    @Getter
    private int workerAmount;

    /**
     * Worker Type
     */
    @Setter
    @Getter
    private String workerType;

    /**
     * Worker Weight
     */
    @Setter
    @Getter
    private String workerWeight;

    /**
     * Worker Cpu Capacity
     */
    @Setter
    @Getter
    private String workerCpu;

    /**
     * Worker Memory Capacity
     */
    @Setter
    @Getter
    private String workerMemory;

    /**
     * Enable Monitoring for API
     */
    @Setter
    @Getter
    private boolean monitoringEnabled;

    /**
     * Monotoring Auto Restart
     */
    @Setter
    @Getter
    private boolean monitoringAutoRestart;

    /**
     * Enable Logging
     */
    @Setter
    @Getter
    private boolean loggingNgEnabled;

    /**
     * Enable Persistent Queue
     */
    @Setter
    @Getter
    private boolean persistentQueues;

    /**
     * Enabled Encryption Persistent Queues
     */
    @Setter
    @Getter
    private boolean persistentQueuesEncrypted;

    /**
     * Use Object Store V1
     */
    @Setter
    @Getter
    private boolean objectStoreV1;

    /**
     * List of environment Variables for API
     */
    @Setter
    @Getter
    private List<org.jenkinsci.plugins.cloudhubdeployer.data.EnvVars> envVars;

    /**
     * Log Levels for Different Packages
     */
    @Setter
    @Getter
    private List<LogLevels> logLevels;

    /**
     * verify deployment after deployment
     */
    @Setter
    @Getter
    private boolean verifyDeployments;

    /**
     * Verify Timeout
     */
    @Setter
    @Getter
    private int verifyIntervalInSeconds;

    @Setter
    @Getter
    private List<AutoScalePolicy> autoScalePolicy;


    @Setter
    @Getter
    private Boolean enableAutoScalePolicy;

    @DataBoundSetter
    public void setRequestMode(RequestMode requestMode) {
        this.requestMode = requestMode;
    }

    @DataBoundSetter
    public void setTimeoutConnection(int timeoutConnection) {
        this.timeoutConnection = timeoutConnection;
    }

    @DataBoundSetter
    public void setTimeoutResponse(int timeoutResponse) {
        this.timeoutResponse = timeoutResponse;
    }

    @DataBoundSetter
    public void setDebugMode(DebugMode debugMode) {
        this.debugMode = debugMode;
    }

    @DataBoundSetter
    public void setIgnoreGlobalSettings(boolean ignoreGlobalSettings) {
        this.ignoreGlobalSettings = ignoreGlobalSettings;
    }

    @DataBoundSetter
    public void setRegion(String region) {
        this.region = region;
    }

    @DataBoundSetter
    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    @DataBoundSetter
    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    @DataBoundSetter
    public void setWorkerAmount(int workerAmount) {
        this.workerAmount = workerAmount;
    }

    @DataBoundSetter
    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    @DataBoundSetter
    public void setWorkerWeight(String workerWeight) {
        this.workerWeight = workerWeight;
    }

    @DataBoundSetter
    public void setWorkerCpu(String workerCpu) {
        this.workerCpu = workerCpu;
    }

    @DataBoundSetter
    public void setWorkerMemory(String workerMemory) {
        this.workerMemory = workerMemory;
    }

    @DataBoundSetter
    public void setMonitoringEnabled(boolean monitoringEnabled) {
        this.monitoringEnabled = monitoringEnabled;
    }

    @DataBoundSetter
    public void setMonitoringAutoRestart(boolean monitoringAutoRestart) {
        this.monitoringAutoRestart = monitoringAutoRestart;
    }

    @DataBoundSetter
    public void setLoggingNgEnabled(boolean loggingNgEnabled) {
        this.loggingNgEnabled = loggingNgEnabled;
    }

    @DataBoundSetter
    public void setPersistentQueues(boolean persistentQueues) {
        this.persistentQueues = persistentQueues;
    }

    @DataBoundSetter
    public void setPersistentQueuesEncrypted(boolean persistentQueuesEncrypted) {
        this.persistentQueuesEncrypted = persistentQueuesEncrypted;
    }

    @DataBoundSetter
    public void setObjectStoreV1(boolean objectStoreV1) {
        this.objectStoreV1 = objectStoreV1;
    }

    @DataBoundSetter
    public void setEnvVars(List<org.jenkinsci.plugins.cloudhubdeployer.data.EnvVars> envVars) {
        this.envVars = envVars;
    }

    @DataBoundSetter
    public void setLogLevels(List<LogLevels> logLevels) {
        this.logLevels = logLevels;
    }

    @DataBoundSetter
    public void setVerifyDeployments(boolean verifyDeployments) {
        this.verifyDeployments = verifyDeployments;
    }

    @DataBoundSetter
    public void setVerifyIntervalInSeconds(int verifyIntervalInSeconds) {
        this.verifyIntervalInSeconds = verifyIntervalInSeconds;
    }

    @DataBoundSetter
    public void setEnableAutoScalePolicy(Boolean enableAutoScalePolicy) {
        this.enableAutoScalePolicy = enableAutoScalePolicy;
    }

    @DataBoundSetter
    public void setAutoScalePolicy(List<AutoScalePolicy> autoScalePolicy) {
        this.autoScalePolicy = autoScalePolicy;
    }

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public CloudHubDeployer(String credentialsId , String environmentId, String orgId,
                            RequestMode requestMode, String appName, String muleVersion, String region,
                            String filePath) {
        this.environmentId = environmentId;
        this.orgId = orgId;
        this.requestMode = requestMode;
        this.appName = appName;
        this.credentialsId = credentialsId;
        this.muleVersion = muleVersion;
        this.region = region;
        this.filePath = filePath;
        this.envVars = new ArrayList<>();
        this.logLevels = new ArrayList<>();
        this.autoScalePolicy = new ArrayList<>();
        this.timeoutConnection = Constants.TIMEOUT_CONNECTION;
        this.timeoutResponse = Constants.TIMEOUT_RESPONSE;
        this.debugMode = DebugMode.DISABLED;
        this.ignoreGlobalSettings = Constants.DEFAULT_IGNORE_GLOBAL_SETTINGS;
        this.autoStart = Constants.DEFAULT_AUTOSTART;
        this.workerAmount = Constants.DEFAULT_WORKER_AMOUNT;
        this.workerType = Constants.DEFAULT_WORKER_TYPE;
        this.workerWeight = Constants.DEFAULT_WORKER_WEIGHT;
        this.workerCpu = Constants.DEFAULT_WORKER_CPU;
        this.workerMemory = Constants.DEFAULT_WORKER_MEMORY;
        this.monitoringEnabled = Constants.DEFAULT_MONITORING_ENABLED;
        this.monitoringAutoRestart = Constants.DEFAULT_MONITORING_AUTO_RESTART;
        this.loggingNgEnabled = Constants.DEFAULT_LOGGING_NG_ENABLED;
        this.persistentQueues = Constants.DEFAULT_PERSISTENT_QUEUES;
        this.persistentQueuesEncrypted = Constants.DEFAULT_PERSISTENT_QUEUES_ENCRYPTED;
        this.objectStoreV1 = Constants.DEFAULT_OBJECT_STORE_V1;
        this.verifyDeployments = Constants.DEFAULT_VERIFY_DEPLOYMENTS;
        this.verifyIntervalInSeconds = Constants.API_STATUS_CHECK_DELAY;
        this.enableAutoScalePolicy = Constants.DEFAULT_AUTOSCALE_POLICY;

    }

    @Override
    public void perform(Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher,
                        @Nonnull TaskListener listener) throws InterruptedException, IOException {

        try {


            if (!this.ignoreGlobalSettings) {

                this.updateFromGlobalConfiguration();

                if (Strings.isNullOrEmpty(this.orgId)) {
                    throw new CloudHubRequestException("Global settings org Id was not found.");
                }
                if (Strings.isNullOrEmpty(this.environmentId)) {
                    throw new CloudHubRequestException("Global settings environment Id/Name was not found.");
                }
                if (Strings.isNullOrEmpty(this.credentialsId)) {
                    throw new CloudHubRequestException("Global settings credentials Id was not found.");
                }

                if (this.timeoutResponse <= 0) {
                    setTimeoutResponse(Constants.TIMEOUT_RESPONSE);
                }

                if (this.timeoutConnection <= 0) {
                    setTimeoutConnection(Constants.TIMEOUT_CONNECTION);
                }

            }

            boolean cloudhubRequestStatus = new DeployRunner(run, workspace, launcher,
                    listener, this).perform();

            if(!cloudhubRequestStatus){
                String message = this.getRequestMode().toString() + " request on " + this.getAppName() + " failed";
                throw new CloudHubRequestException(message);
            }

        } catch (ValidationException | CloudHubRequestException exc) {
            throw new IOException("Deployment Failure", exc);
        }

    }

    private void updateFromGlobalConfiguration() {
        Jenkins jenkins = Jenkins.getInstance();

        if (jenkins !=  null) {
            GlobalCloudHubConfig.DescriptorImpl globalDescriptor = (GlobalCloudHubConfig.DescriptorImpl)
                    jenkins.getDescriptor(GlobalCloudHubConfig.class);

            if (globalDescriptor != null) {
                orgId = globalDescriptor.getOrgId();
                environmentId = globalDescriptor.getEnvironmentId();
                credentialsId = globalDescriptor.getCredentialsId();
                timeoutConnection = globalDescriptor.getTimeoutConnection();
                timeoutResponse = globalDescriptor.getTimeoutResponse();
                debugMode = globalDescriptor.getDebugMode();
            } else {
                LOGGER.warning("Could not load global settings.");
            }
        } else {
            LOGGER.warning("Could not load global settings.");
        }
    }


    @Symbol("cloudhubDeployer")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Setter
        @Getter
        private String environmentId;

        @Setter
        @Getter
        private String credentialsId;

        @Setter
        @Getter
        private String orgId;

        @Setter
        @Getter
        private int timeoutConnection;

        @Setter
        @Getter
        private int timeoutResponse;

        @Setter
        @Getter
        private DebugMode debugMode;

        @Setter
        @Getter
        private boolean ignoreGlobalSettings;

        public DescriptorImpl() {
           load();
        }

        public ListBoxModel doFillRequestModeItems() {
            return RequestMode.getFillItems();
        }

        public ListBoxModel doFillDefaultRequestModeItems() {
            return RequestMode.getFillItems();
        }

        public ListBoxModel doFillDebugModeItems() {
            return DebugMode.getFillItems();
        }

        public ListBoxModel doFillDefaultDebugModeItems() {
            return DebugMode.getFillItems();
        }

        public RequestMode getDefaultRequestMode() {
            return RequestMode.UPDATE;
        }

        public DebugMode getDefaultDebugMode() {
            return DebugMode.DISABLED;
        }

        public boolean getDefaultAutoStart() {
            return true;
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            if (!ignoreGlobalSettings) {
                updateFromGlobalConfiguration();
            }

            req.bindJSON(this, formData);
            save();

            return super.configure(req, formData);
        }

        @Override
        public String getDisplayName() {
            return "CloudHub Deployment";
        }

        private void updateFromGlobalConfiguration() {
            Jenkins jenkins = Jenkins.getInstance();

            if (jenkins !=  null) {
                GlobalCloudHubConfig.DescriptorImpl globalDescriptor = (GlobalCloudHubConfig.DescriptorImpl)
                        jenkins.getDescriptor(GlobalCloudHubConfig.class);

                if (globalDescriptor != null) {
                    orgId = globalDescriptor.getOrgId();
                    environmentId = globalDescriptor.getEnvironmentId();
                    credentialsId = globalDescriptor.getCredentialsId();
                    timeoutConnection = globalDescriptor.getTimeoutConnection();
                    timeoutResponse = globalDescriptor.getTimeoutResponse();
                    debugMode = globalDescriptor.getDebugMode();
                } else {
                    LOGGER.warning("Could not load global settings.");
                }
            } else {
                LOGGER.warning("Could not load global settings.");
            }
        }

        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item item, @QueryParameter String url,
                                                     @QueryParameter String credentialsId) {

            if (item == null && !Jenkins.get().hasPermission(Jenkins.ADMINISTER) ||
                    item != null && !item.hasPermission(Item.EXTENDED_READ)) {
                return new StandardListBoxModel().includeCurrentValue(credentialsId);
            }

            return new StandardListBoxModel()
                    .includeEmptyValue()
                    .includeMatchingAs(
                            item instanceof Queue.Task
                                    ? Tasks.getAuthenticationOf((Queue.Task) item) : ACL.SYSTEM,
                            item, StandardUsernameCredentials.class, URIRequirementBuilder.fromUri(url).build(),
                            CredentialsMatchers.instanceOf(StandardUsernamePasswordCredentials.class))
                    .includeCurrentValue(credentialsId);
        }

        public FormValidation doCheckCredentialsId(@AncestorInPath Item project,
                                                   @QueryParameter final String credentialsId) {

            if (project == null && !Jenkins.get().hasPermission(Jenkins.ADMINISTER) ||
                    project != null && !project.hasPermission(Item.EXTENDED_READ)) {
                return FormValidation.ok();
            }

            if (Strings.isNullOrEmpty(credentialsId)) {
                return FormValidation.error("Please fill in CloudHub credentials");
            }

            return FormValidation.ok();
        }

        private FormValidation checkFieldNotEmpty(String value, String field) {
            value = StringUtils.strip(value);

            if (value == null || value.equals("")) {
                return FormValidation.error("");
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckHostUrl(@QueryParameter final String hostUrl) {

            if (Strings.isNullOrEmpty(hostUrl)) {
                return FormValidation.error("Please fill in host Url");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckEnvironmentId(@QueryParameter final String environmentId) {

            if (Strings.isNullOrEmpty(environmentId)) {
                return FormValidation.error("Please fill in environment ID");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckOrgId(@QueryParameter final String orgId) {

            if (Strings.isNullOrEmpty(orgId)) {
                return FormValidation.error("Please fill in organization ID");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckTimeoutConnection(@QueryParameter final String timeoutConnection) {

            if (Strings.isNullOrEmpty(timeoutConnection)) {
                return FormValidation.error("Please fill in timeout connection");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckTimeoutResponse(@QueryParameter final String timeoutResponse) {

            if (Strings.isNullOrEmpty(timeoutResponse)) {
                return FormValidation.error("Please fill in timeout response");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckAppName(@QueryParameter final String appName) {

            if (Strings.isNullOrEmpty(appName)) {
                return FormValidation.error("Please specify API ame");
            }

            if (!appName.matches("(^[A-Za-z0-9-]{3,42})")) {
                return FormValidation.error("Doesn't start or end with dash\n" +
                        "Doesn't start with 'internal-'\n" +
                        "Maximum length: 42 characters\n" +
                        "Minimum length: 3 characters\n" +
                        "Only letters, numbers or dashes");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckMuleVersion(@QueryParameter final String muleVersion) {

            if (Strings.isNullOrEmpty(muleVersion)) {
                return FormValidation.error("Please fill in muleVersion");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckRegion(@QueryParameter final String region) {

            if (Strings.isNullOrEmpty(region)) {
                return FormValidation.error("Please fill in region");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckWorkerAmount(@QueryParameter final int workerAmount) {
            
            if (workerAmount <= 0 || workerAmount > 8) {
                return FormValidation.error("Worker count should greater then 0 and less then 9");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckWorkerType(@QueryParameter final String workerType) {

            if (Strings.isNullOrEmpty(workerType)) {
                return FormValidation.error("Please fill in worker type");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckWorkerWeight(@QueryParameter final String workerWeight) {

            if (Strings.isNullOrEmpty(workerWeight)) {
                return FormValidation.error("Please fill in worker weight");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckWorkerCpu(@QueryParameter final String workerCpu) {

            if (Strings.isNullOrEmpty(workerCpu)) {
                return FormValidation.error("Please fill in worker Cpu");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckWorkerMemory(@QueryParameter final String workerMemory) {

            if (Strings.isNullOrEmpty(workerMemory)) {
                return FormValidation.error("Please fill in worker memory");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckFilePath(@QueryParameter final String filePath) {

            if (Strings.isNullOrEmpty(filePath)) {
                return FormValidation.error("Please fill in file path");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckVerifyTimeoutInMinutes(@QueryParameter final String verifyTimeoutInMinutes) {

            if (Strings.isNullOrEmpty(verifyTimeoutInMinutes)) {
                return FormValidation.error("Please specify verify timeout");
            }

            if (!verifyTimeoutInMinutes.matches("([1-9]\\d*)")) {
                return FormValidation.error("Field should contains only number");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckFileType(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please select one option.");

            return FormValidation.ok();
        }

    }

}
