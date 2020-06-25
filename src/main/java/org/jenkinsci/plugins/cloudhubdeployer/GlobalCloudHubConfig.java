package org.jenkinsci.plugins.cloudhubdeployer;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import org.jenkinsci.plugins.cloudhubdeployer.common.DebugMode;
import org.jenkinsci.plugins.cloudhubdeployer.utils.Constants;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.GlobalConfiguration;
import jenkins.model.GlobalPluginConfiguration;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Plugin to set and test CloudHub global config settings.
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public class GlobalCloudHubConfig extends GlobalPluginConfiguration {
    private static Logger LOGGER = Logger.getLogger(GlobalCloudHubConfig.class.getName());

    @Extension
    public static final class DescriptorImpl extends Descriptor<GlobalConfiguration> {

        @Setter
        @Getter
        private String environmentId;

        /**
         * Credentials name
         */
        @Setter
        @Getter
        private String orgId;

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
         * run deployment in debug mode
         */
        @Setter
        @Getter
        private DebugMode debugMode;
        /**
         * Cloudhub Credentials
         */
        @Setter
        @Getter
        private String credentialsId;

        public DescriptorImpl() {
            load();
        }

        public ListBoxModel doFillDebugModeItems() {
            return DebugMode.getFillItems();
        }

        public DebugMode getDefaultDebugMode() {
            return DebugMode.DISABLED;
        }

        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item item) {

            final List<StandardCredentials> credentials = CredentialsProvider.lookupCredentials(StandardCredentials.class, item, ACL.SYSTEM, Collections.<DomainRequirement>emptyList());

            return new StandardListBoxModel()
                    .withEmptySelection()
                    //.withAll(credentials);
                    .withMatching(CredentialsMatchers.anyOf(CredentialsMatchers.instanceOf(StandardUsernamePasswordCredentials.class)), credentials);
        }

        @Override
        public String getDisplayName() {
            return Constants.PLUGIN_NAME;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            JSONObject json = (JSONObject) formData.get("globalCloudHubConfig");

            credentialsId = json.getString("credentialsId");
            orgId = json.getString("orgId");
            environmentId = json.getString("environmentId");

            try {
                timeoutConnection = Integer.parseInt(json.getString("timeoutConnection"));
            } catch (NumberFormatException nfe) {
                LOGGER.warning(String.format("Using default connection timeout of %s.", Constants.TIMEOUT_CONNECTION));
                timeoutConnection = Constants.TIMEOUT_CONNECTION;
            }

            try {
                timeoutResponse = Integer.parseInt(json.getString("timeoutResponse"));
            } catch (NumberFormatException nfe) {
                LOGGER.warning(String.format("Using default response timeout of %s.", Constants.TIMEOUT_RESPONSE));
                timeoutResponse = Constants.TIMEOUT_RESPONSE;
            }

            debugMode = DebugMode.valueOf(json.getString("debugMode"));

            req.bindJSON(this, formData);
            save();
            return super.configure(req, formData);
        }

        public FormValidation doTestConnection(@QueryParameter String credentialsId) {

            LOGGER.info(String.format("Testing CloudHub connectivity."));

            //Validation
            if (credentialsId.isEmpty()) {
                return FormValidation.warning("Please Specify valid credentials to test");
            }

            try {
                URL url = new URL(Constants.CLOUDHUB_URL + Constants.URI_LOGIN);
                URLConnection connection = url.openConnection();
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    httpConnection.setRequestMethod("POST");

                    int code = httpConnection.getResponseCode();
                    httpConnection.disconnect();
                    if (code > 400) {
                        return FormValidation.error("Could not connect to %s, with GET request. Code %s",
                                Constants.CLOUDHUB_URL, code);
                    }
                }
            } catch (IOException ioe) {
                final String ERROR_UNABLE_TO_CONNECT = String.format("Unable to connect to CloudHub at URL: %s",
                        Constants.CLOUDHUB_URL);
                Logger.getLogger(CloudHubDeployer.class.getName()).log(Level.WARNING, ERROR_UNABLE_TO_CONNECT, ioe);
                return FormValidation.error("%s - %s", ERROR_UNABLE_TO_CONNECT, ioe.getMessage());
            }

            return FormValidation.ok("Connected to " + Constants.CLOUDHUB_URL);
        }
    }
}
