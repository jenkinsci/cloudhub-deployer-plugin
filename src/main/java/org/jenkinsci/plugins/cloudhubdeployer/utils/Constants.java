package org.jenkinsci.plugins.cloudhubdeployer.utils;

/**
 * Constants
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public final class Constants {

    public static final String PLUGIN_NAME = "CloudHub Deployer";
    public static final String PLUGIN_LABEL = "cloudhub deployer";
    public static final Integer TIMEOUT_CONNECTION = 120000;
    public static final Integer TIMEOUT_RESPONSE = 120000;
    public static final String LABEL_CONTENT_TYPE = "Content-Type";
    public static final String LABEL_ACCEPT = "Accept";
    public static final String CLOUDHUB_URL = "https://anypoint.mulesoft.com";
    public static final String API_URI_V2 = "/cloudhub/api/v2";
    public static final String API_URI_NO_VERSION = "/cloudhub/api";
    public static final String URI_LOGIN = "/accounts/login";
    public static final String URI_APPLICATION = "/applications";
    public static final String URI_DELETE = "/applications";
    public static final String URI_RESTART = "/applications";
    public static final String MEDIA_TYPE_APP_JSON = "application/json; charset=utf-8";
    public static final String MEDIA_TYPE_PLAIN_TEXT = "plain/text; charset=utf-8";
    public static final String MEDIA_TYPE_OCTET_STREAM = "application/octet-stream";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String LABEL_ANYPNT_ENV_ID = "X-ANYPNT-ENV-ID";
    public static final String LABEL_AUTHORIZATION = "Authorization";
    public static final String LABEL_AUTOSTART = "autoStart";
    public static final String LABEL_APP_INFO_JSON = "appInfoJson";
    public static final String JSON_KEY_ACCESS_TOKEN = "access_token";
    public static final String JSON_KEY_API_STATUS = "status";
    public static final String JSON_KEY_DEPLOYMENT_UPDATE_STATUS = "deploymentUpdateStatus";
    public static final int HTTP_OK = 200;
    public static final int HTTP_MULTI_CHOICES = 300;
    public static final String FIELD_VALUE = "Value";
    public static final Integer DEFAULT_VERIFY_INITIAL_DELAY = 23000;
    public static final Integer API_STATUS_CHECK_DELAY = 30000;
    public static final String PATTERN_WORKER_CPU = "[0-9.]{0,2}.( vCore)";
    public static final String PATTERN_WORKER_MEMORY = "[0-9.]{0,2}.( vCore)";
    public static final String SUFFIX_WORKER_CPU = " vCore";
    public static final String SUFFIX_WORKER_MEMORY = " memory";
    public static final String LABEL_FILE = "file";
    public static final String LABEL_API_DOMAIN = "domain";
    public static final boolean DEFAULT_AUTOSTART = true;
    public static final int DEFAULT_WORKER_AMOUNT = 1;
    public static final String DEFAULT_WORKER_TYPE = "Small";
    public static final String DEFAULT_WORKER_WEIGHT = "0.2";
    public static final String DEFAULT_WORKER_CPU = "0.2";
    public static final String DEFAULT_WORKER_MEMORY = "1 GB";
    public static final boolean DEFAULT_MONITORING_ENABLED = false;
    public static final boolean DEFAULT_MONITORING_AUTO_RESTART = false;
    public static final boolean DEFAULT_LOGGING_NG_ENABLED = false;
    public static final boolean DEFAULT_PERSISTENT_QUEUES = false;
    public static final boolean DEFAULT_PERSISTENT_QUEUES_ENCRYPTED = false;
    public static final boolean DEFAULT_OBJECT_STORE_V1 = false;
    public static final boolean DEFAULT_VERIFY_DEPLOYMENTS = true;
    public static final boolean DEFAULT_IGNORE_GLOBAL_SETTINGS = false;
    public static final Boolean DEFAULT_AUTOSCALE_POLICY = false;
    public static final String JSON_KEY_AUTOSCALE_POLICY_ID = "id";
    public static final int DEFALT_POLICY_INDEX = 0;


    private Constants() {

    }
}
