
package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AutoScalePolicy {

    @SerializedName("id")
    @Expose @Setter @Getter
    private String id;

    @SerializedName("enabled")
    @Expose @Setter @Getter
    private Boolean enabled;

    @SerializedName("maxScale")
    @Expose @Setter @Getter
    private Integer maxScale;

    @SerializedName("minScale")
    @Expose @Setter @Getter
    private Integer minScale;

    @SerializedName("scaleType")
    @Expose @Setter @Getter
    private String scaleType;

    @SerializedName("description")
    @Expose @Setter @Getter
    private String description;

    @SerializedName("metric")
    @Expose @Setter @Getter
    private String metric;

    @SerializedName("scaleUpNextScaleWaitMins")
    @Expose @Setter @Getter
    private Integer scaleUpNextScaleWaitMins;

    @SerializedName("scaleDownNextScaleWaitMins")
    @Expose @Setter @Getter
    private Integer scaleDownNextScaleWaitMins;

    @SerializedName("scaleUp")
    @Expose @Setter @Getter
    private Scale scaleUp;

    @SerializedName("scaleDown")
    @Expose @Setter @Getter
    private Scale scaleDown;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("enabled", enabled).append("maxScale", maxScale).append("minScale", minScale).append("scaleType", scaleType).append("description", description).append("metric", metric).append("scaleUpNextScaleWaitMins", scaleUpNextScaleWaitMins).append("scaleDownNextScaleWaitMins", scaleDownNextScaleWaitMins).append("scaleUp", scaleUp).append("scaleDown", scaleDown).toString();
    }

}
