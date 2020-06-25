package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class LoginPojo {

    @SerializedName("access_token")
    @Expose @Setter @Getter
    private String accessToken;

    @SerializedName("token_type")
    @Expose @Setter @Getter
    private String tokenType;

    @SerializedName("redirectUrl")
    @Expose @Setter @Getter
    private String redirectUrl;
}
