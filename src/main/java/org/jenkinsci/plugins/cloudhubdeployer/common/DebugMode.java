package org.jenkinsci.plugins.cloudhubdeployer.common;

import hudson.util.ListBoxModel;

/**
 * Debug Mode ENUM
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public enum DebugMode {

    ENABLED, DISABLED;

    public static ListBoxModel getFillItems() {
        ListBoxModel items = new ListBoxModel();
        for (DebugMode debugMode : values()) {
            items.add(debugMode.name());
        }
        return items;
    }

    static public boolean isMember(DebugMode mode) {
        DebugMode[] debugModes = DebugMode.values();
        for (DebugMode debugMode : debugModes)
            if (debugMode.compareTo(mode) == 0)
                return true;
        return false;
    }

}
