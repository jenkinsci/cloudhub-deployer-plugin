package org.jenkinsci.plugins.cloudhubdeployer;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import org.jenkinsci.plugins.cloudhubdeployer.common.DebugMode;
import org.jenkinsci.plugins.cloudhubdeployer.common.RequestMode;
import org.jenkinsci.plugins.cloudhubdeployer.data.AppInfoJson;
import lombok.Getter;
import lombok.Setter;
import org.jenkinsci.plugins.cloudhubdeployer.data.AutoScalePolicy;

import java.io.PrintStream;
import java.util.List;

/**
 * CloudHub Request Type
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public class CloudHubRequest {

    @Setter
    @Getter
    private String apiDomainName;
    @Setter
    @Getter
    private String orgId;
    @Setter
    @Getter
    private String envId;
    @Setter
    @Getter
    private String url;
    @Setter
    @Getter
    private int timeoutConnect;
    @Setter
    @Getter
    private int timeoutResponse;
    @Setter
    @Getter
    private PrintStream logger;
    @Setter
    @Getter
    private DebugMode debugMode;
    @Setter
    @Getter
    private RequestMode requestMode;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String password;
    @Setter
    @Getter
    private String autoStart;
    @Setter
    @Getter
    private AppInfoJson appInfoJson;
    @Setter
    @Getter
    private String accessToken;
    @Setter
    @Getter
    private String fileName;
    @Setter
    @Getter
    private String filePath;
    @Setter
    @Getter
    private StandardCredentials cloudhubCredentials;
    @Setter
    @Getter
    private boolean autoScalePolicyEnabled;
    @Setter
    @Getter
    private List<AutoScalePolicy> autoScalePolicy;

    @Override
    public String toString() {
        return "CloudhubRequest{" +
                "envId='" + envId + '\'' +
                ", url='" + url + '\'' +
                ", timeoutConnect=" + timeoutConnect +
                ", timeoutResponse=" + timeoutResponse +
                ", logger=" + logger +
                ", debugMode=" + debugMode +
                ", requestMode=" + requestMode +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", autoStart=" + autoStart +
                ", appInfoJson=" + appInfoJson +
                ", accessToken='" + accessToken + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    public String getUsersCredsJsonString() {
        UsernamePasswordCredentials passwordCredentials = (UsernamePasswordCredentials) cloudhubCredentials;
        return "{\"username\" : \""+ passwordCredentials.getUsername() +"\" , " +
                "\"password\" : \""+passwordCredentials.getPassword()+"\" }";
    }

}
