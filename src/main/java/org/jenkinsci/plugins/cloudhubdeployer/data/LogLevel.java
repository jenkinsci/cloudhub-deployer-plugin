
package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class LogLevel {

    @SerializedName("packageName")
    @Expose @Setter @Getter
    private String packageName;

    @SerializedName("level")
    @Expose @Setter @Getter
    private String level;

    @Override
    public String toString() {
        return "LogLevel{" +
                "packageName='" + packageName + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
