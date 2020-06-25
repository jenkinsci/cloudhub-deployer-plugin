package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.common.base.Strings;
import org.jenkinsci.plugins.cloudhubdeployer.common.LogLevelCategory;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import lombok.Getter;
import lombok.Setter;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;


public class LogLevels extends AbstractDescribableImpl<LogLevels> {

    @Setter
    @Getter
    public LogLevelCategory levelCategory;

    @Setter
    @Getter
    public String packageName;

    @DataBoundConstructor
    public LogLevels() {
    }

    @DataBoundSetter
    public void setLevelCategory(LogLevelCategory levelCategory) {
        this.levelCategory = levelCategory;
    }

    @DataBoundSetter
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Symbol("logLevels")
    @Extension
    public static class DescriptorImpl extends Descriptor<LogLevels> {

        public String getDisplayName() { return ""; }

        public ListBoxModel doFillLevelCategoryItems() {
            return LogLevelCategory.getFillItems();
        }


        public FormValidation doCheckPackageName(@QueryParameter final String packageName) {

            if (Strings.isNullOrEmpty(packageName)) {
                return FormValidation.error("Please fill in package name");
            }

            return FormValidation.ok();
        }
    }

}
