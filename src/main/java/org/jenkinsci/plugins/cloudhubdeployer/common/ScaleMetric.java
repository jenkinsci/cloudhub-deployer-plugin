package org.jenkinsci.plugins.cloudhubdeployer.common;

import hudson.util.ListBoxModel;

public enum ScaleMetric {
    CPU, MEMORY;

    public static ListBoxModel getFillItems() {
        ListBoxModel items = new ListBoxModel();
        for (ScaleMetric scaleMetric : values()) {
            items.add(scaleMetric.name());
        }
        return items;
    }

    static public boolean isMember(ScaleMetric request) {
        ScaleMetric[] scaleMetrics = ScaleMetric.values();
        for (ScaleMetric scaleMetric : scaleMetrics)
            if (scaleMetric.compareTo(request) == 0)
                return true;
        return false;
    }
}
