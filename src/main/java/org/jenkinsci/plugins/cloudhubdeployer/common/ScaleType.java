package org.jenkinsci.plugins.cloudhubdeployer.common;

import hudson.util.ListBoxModel;

public enum ScaleType {
    WORKER_COUNT, WORKER_SIZE;

    public static ListBoxModel getFillItems() {
        ListBoxModel items = new ListBoxModel();
        for (ScaleType scaleType : values()) {
            items.add(scaleType.name());
        }
        return items;
    }

    static public boolean isMember(ScaleType request) {
        ScaleType[] scaleTypes = ScaleType.values();
        for (ScaleType scaleType : scaleTypes)
            if (scaleType.compareTo(request) == 0)
                return true;
        return false;
    }
}
