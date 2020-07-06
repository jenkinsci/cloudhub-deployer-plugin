
package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Scale {

    @SerializedName("value")
    @Expose @Setter @Getter
    private Integer value;

    @SerializedName("periodMins")
    @Expose @Setter @Getter
    private Integer periodMins;
    
    @SerializedName("periodCount")
    @Expose @Setter @Getter
    private Integer periodCount;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("value", value).append("periodMins", periodMins).append("periodCount", periodCount).toString();
    }

}
