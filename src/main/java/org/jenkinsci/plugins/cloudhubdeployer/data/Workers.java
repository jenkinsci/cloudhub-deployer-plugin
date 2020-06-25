
package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class Workers {

    @SerializedName("amount")
    @Expose @Setter @Getter
    private Integer amount;

    @SerializedName("type")
    @Expose @Setter @Getter
    private Type type;

    @Override
    public String toString() {
        return "Workers{" +
                "amount=" + amount +
                ", type=" + type +
                '}';
    }
}
