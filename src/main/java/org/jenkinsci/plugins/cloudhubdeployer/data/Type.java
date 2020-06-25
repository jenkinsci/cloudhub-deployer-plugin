
package org.jenkinsci.plugins.cloudhubdeployer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class Type {

    @SerializedName("name")
    @Expose @Setter @Getter
    private String name;

    @SerializedName("weight")
    @Expose @Setter @Getter
    private Double weight;

    @SerializedName("cpu")
    @Expose @Setter @Getter
    private String cpu;

    @SerializedName("memory")
    @Expose @Setter @Getter
    private String memory;

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                '}';
    }
}
