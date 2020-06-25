package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.common.base.Strings;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import lombok.Getter;
import lombok.Setter;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;


public class EnvVars extends AbstractDescribableImpl<EnvVars> {

    @Setter
    @Getter
    public String key;

    @Setter
    @Getter
    public String value;

    @DataBoundConstructor
    public EnvVars() {
    }

    @DataBoundSetter
    public void setKey(String key) {
        this.key = key;
    }

    @DataBoundSetter
    public void setValue(String value) {
        this.value = value;
    }

    @Symbol("envVars")
    @Extension
    public static class DescriptorImpl extends Descriptor<EnvVars> {

        public String getDisplayName() { return ""; }

        public FormValidation doCheckKey(@QueryParameter final String key) {

            if (Strings.isNullOrEmpty(key)) {
                return FormValidation.error("Please fill in key");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckValue(@QueryParameter final String value) {

            if (Strings.isNullOrEmpty(value)) {
                return FormValidation.error("Please fill in value");
            }

            return FormValidation.ok();
        }
    }

}
