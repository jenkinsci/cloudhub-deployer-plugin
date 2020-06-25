package org.jenkinsci.plugins.cloudhubdeployer.common;

import hudson.util.ListBoxModel;

public enum LogLevelCategory {

    DEBUG, WARN, ERROR, INFO;

    public static ListBoxModel getFillItems() {
        ListBoxModel items = new ListBoxModel();
        for (LogLevelCategory logLevelCategory : values()) {
            items.add(logLevelCategory.name());
        }
        return items;
    }
}
