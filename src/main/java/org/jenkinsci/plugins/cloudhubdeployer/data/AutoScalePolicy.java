
package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class AutoScalePolicy extends AbstractDescribableImpl<AutoScalePolicy> {

    @SerializedName("id")
    @Expose @Setter @Getter
    @DataBoundSetter
    private String id;

    @SerializedName("enabled")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Boolean enableAutoScalePolicy;

    @SerializedName("maxScale")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Integer maxScale;

    @SerializedName("minScale")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Integer minScale;

    @SerializedName("scaleType")
    @Expose @Setter @Getter
    @DataBoundSetter
    private String scaleType;

    @SerializedName("description")
    @Expose @Setter @Getter
    @DataBoundSetter
    private String autoScalePolicyName;

    @SerializedName("metric")
    @Expose @Setter @Getter
    private String scaleBasedOn;

    @SerializedName("scaleUpNextScaleWaitMins")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Integer scaleUpPeriodCount;

    @SerializedName("scaleDownNextScaleWaitMins")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Integer scaleDownPeriodCount;

    @SerializedName("scaleUp")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Scale scaleUp;

    @SerializedName("scaleDown")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Scale scaleDown;

    @DataBoundConstructor
    public AutoScalePolicy() {
    }

    @DataBoundSetter
    public void setScaleBasedOn(String scaleBasedOn) {
        this.scaleBasedOn = scaleBasedOn;
    }

    public Descriptor<AutoScalePolicy> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    @Symbol("AutoScalePolicy")
    @Extension
    public static class DescriptorImpl extends Descriptor<AutoScalePolicy> {

        public String getDisplayName() { return ""; }

        public FormValidation doCheckScaleBasedOn(@QueryParameter final String scaleBasedOn) {

            if (Strings.isNullOrEmpty(scaleBasedOn)) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }


    }

}
