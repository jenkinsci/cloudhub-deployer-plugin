
package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class MuleVersion {

    @SerializedName("version")
    @Expose @Setter @Getter
    private String version;

    public MuleVersion(String version) {
        this.version = version;
    }

}
