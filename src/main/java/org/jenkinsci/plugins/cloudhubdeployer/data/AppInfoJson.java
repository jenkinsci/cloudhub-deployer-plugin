
package org.jenkinsci.plugins.cloudhubdeployer.data;

import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class AppInfoJson {

    @SerializedName("domain")
    @Expose @Setter @Getter
    private String domain;

    @SerializedName("muleVersion")
    @Expose @Setter @Getter
    private MuleVersion muleVersion;

    @SerializedName("region")
    @Expose @Setter @Getter
    private String region;

    @SerializedName("monitoringEnabled")
    @Expose @Setter @Getter
    private Boolean monitoringEnabled;

    @SerializedName("monitoringAutoRestart")
    @Expose @Setter @Getter
    private Boolean monitoringAutoRestart;

    @SerializedName("properties")
    @Expose @Setter @Getter
    private Map<String, String>  properties;

    @SerializedName("workers")
    @Expose @Setter @Getter
    private Workers workers;

    @SerializedName("loggingNgEnabled")
    @Expose @Setter @Getter
    private Boolean loggingNgEnabled;

    @SerializedName("persistentQueues")
    @Expose @Setter @Getter
    private Boolean persistentQueues;

    @SerializedName("objectStoreV1")
    @Expose @Setter @Getter
    private Boolean objectStoreV1;

    @SerializedName("persistentQueuesEncrypted")
    @Expose @Setter @Getter
    private Boolean persistentQueuesEncrypted;

    @SerializedName("logLevels")
    @Expose @Setter @Getter
    private List<LogLevel> logLevels = null;


    @Override
    public String toString() {
        return "AppInfoJson{" +
                "domain='" + domain + '\'' +
                ", muleVersion=" + muleVersion +
                ", region='" + region + '\'' +
                ", monitoringEnabled=" + monitoringEnabled +
                ", monitoringAutoRestart=" + monitoringAutoRestart +
                ", properties=" + properties +
                ", workers=" + workers +
                ", loggingNgEnabled=" + loggingNgEnabled +
                ", persistentQueues=" + persistentQueues +
                ", objectStoreV1=" + objectStoreV1 +
                ", persistentQueuesEncrypted=" + persistentQueuesEncrypted +
                ", logLevels=" + logLevels +
                '}';
    }
}
