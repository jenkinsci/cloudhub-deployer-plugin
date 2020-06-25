package org.jenkinsci.plugins.cloudhubdeployer.common;

import hudson.util.ListBoxModel;

/**
 * Request Mode ENUM
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public enum RequestMode {

    CREATE, UPDATE, CREATE_OR_UPDATE , UPDATE_FILE, RESTART, DELETE;

    public static ListBoxModel getFillItems() {
        ListBoxModel items = new ListBoxModel();
        for (RequestMode requestMode : values()) {
            items.add(requestMode.name());
        }
        return items;
    }

    static public boolean isMember(RequestMode request) {
        RequestMode[] requestModes = RequestMode.values();
        for (RequestMode requestMode : requestModes)
            if (requestMode.compareTo(request) == 0)
                return true;
        return false;
    }

}
