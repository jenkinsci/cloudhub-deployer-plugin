
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
    @DataBoundSetter
    private String scaleBasedOn;

    @SerializedName("scaleUpNextScaleWaitMins")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Integer scaleUpNextScaleWaitMins;

    @SerializedName("scaleDownNextScaleWaitMins")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Integer scaleDownNextScaleWaitMins;

    @SerializedName("scaleUp")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Scale scaleUp;

    @SerializedName("scaleDown")
    @Expose @Setter @Getter
    @DataBoundSetter
    private Scale scaleDown;

    @Expose(serialize = false, deserialize = false)
    @Setter @Getter
    @DataBoundSetter
    private Integer scaleUpValue;

    @Expose(serialize = false, deserialize = false)
    @Setter @Getter
    @DataBoundSetter
    private Integer scaleUpPeriodCount;

    @Expose(serialize = false, deserialize = false)
    @Setter @Getter
    @DataBoundSetter
    private Integer scaleDownValue;

    @Expose(serialize = false, deserialize = false)
    @Setter @Getter
    @DataBoundSetter
    private Integer scaleDownPeriodCount;


    @DataBoundConstructor
    public AutoScalePolicy() {
    }

    public Descriptor<AutoScalePolicy> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    @Override
    public String toString() {
        return "AutoScalePolicy{" +
                "id='" + id + '\'' +
                ", enableAutoScalePolicy=" + enableAutoScalePolicy +
                ", maxScale=" + maxScale +
                ", minScale=" + minScale +
                ", scaleType='" + scaleType + '\'' +
                ", autoScalePolicyName='" + autoScalePolicyName + '\'' +
                ", scaleBasedOn='" + scaleBasedOn + '\'' +
                ", scaleUpNextScaleWaitMins=" + scaleUpNextScaleWaitMins +
                ", scaleDownNextScaleWaitMins=" + scaleDownNextScaleWaitMins +
                ", scaleUp=" + scaleUp +
                ", scaleDown=" + scaleDown +
                '}';
    }

    @Symbol("AutoScalePolicy")
    @Extension
    public static class DescriptorImpl extends Descriptor<AutoScalePolicy> {

        public String getDisplayName() { return ""; }

        public FormValidation doCheckAutoScalePolicyName(@QueryParameter final String autoScalePolicyName) {

            if (Strings.isNullOrEmpty(autoScalePolicyName)) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckScaleBasedOn(@QueryParameter final String scaleBasedOn) {

            if (Strings.isNullOrEmpty(scaleBasedOn)) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckScaleType(@QueryParameter final String scaleType) {

            if (Strings.isNullOrEmpty(scaleType)) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckMinScale(@QueryParameter final int minScale) {

            if (minScale <= 0) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckMaxScale(@QueryParameter final int maxScale) {

            if (maxScale <= 0) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckScaleUpValue(@QueryParameter final int scaleUpValue) {

            if (scaleUpValue <= 0) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckScaleUpPeriodCount(@QueryParameter final int scaleUpPeriodCount) {

            if (scaleUpPeriodCount <= 0) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckScaleUpNextScaleWaitMins(@QueryParameter final int scaleUpNextScaleWaitMins) {

            if (scaleUpNextScaleWaitMins <= 0) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckScaleDownValue(@QueryParameter final int scaleDownValue) {

            if (scaleDownValue <= 0) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckScaleDownPeriodCount(@QueryParameter final int scaleDownPeriodCount) {

            if (scaleDownPeriodCount <= 0) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckScaleDownNextScaleWaitMins(@QueryParameter final int scaleDownNextScaleWaitMins) {

            if (scaleDownNextScaleWaitMins <= 0) {
                return FormValidation.error("Please fill in field");
            }

            return FormValidation.ok();
        }

    }

}
