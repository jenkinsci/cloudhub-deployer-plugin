package org.jenkinsci.plugins.cloudhubdeployer;

import org.jenkinsci.plugins.cloudhubdeployer.common.DebugMode;
import org.jenkinsci.plugins.cloudhubdeployer.common.RequestMode;
import org.jenkinsci.plugins.cloudhubdeployer.data.EnvVars;
import org.jenkinsci.plugins.cloudhubdeployer.data.LogLevels;
import org.jenkinsci.plugins.cloudhubdeployer.utils.Constants;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * This test uses the Jenkins test harness.
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public class CloudHubDeployerTest {

    private static final String ENVIRONMENT_ID = "<ENV_ID>";
    private static final String ORG_ID = "<ORG_ID>";
    private static final RequestMode REQUEST_MODE = RequestMode.CREATE;
    private static final int TIMEOUT_CONNECTION = 90000;
    private static final int TIMEOUT_RESPONSE = 90000;
    private static final DebugMode DEBUG_MODE = DebugMode.DISABLED;
    private static final boolean IGNORE_GLOBAL_SETTINGS = true;
    private static final String APP_NAME = "<API NAME>";
    private static final boolean AUTOSTART = true;
    private static final String CREDENTIALS_ID = "<Creds ID>";
    private static final String MULE_VERSION = "4.3.0";
    private static final String REGION = "us-east-1";
    private static final String FILE_PATH = "target/*.jar";
    private static final int WORKER_AMOUNT = 1;
    private static final String WORKER_TYPE = "Small";
    private static final String WORKER_WEIGHT = "0.2";
    private static final String WORKER_CPU = "0.2 vCore";
    private static final String WORKER_MEMORY = "1 GB memory";
    private static final boolean MONITORING_ENABLED = true;
    private static final boolean MONITORING_AUTO_RESTART = true;
    private static final boolean PERSISTENT_QUEUES = false;
    private static final boolean PERSISTENT_QUEUES_ENCRYPTED = false;
    private static final boolean OBJECT_STORE_V1 = false;
    private static final List<EnvVars> ENV_VARS = new ArrayList<>();
    private static final List<LogLevels> LOG_LEVELS = new ArrayList<>();
    private static final boolean VERIFY_DEPLOYMENTS = false;
    private static final String LOGIN_REQUEST_FAILED = "Login Request failed: ";
    private static final String ASSERT_LOG_TEXT = Constants.JSON_KEY_ACCESS_TOKEN;


    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

//    @Test
//    public void cloudHubLogin() throws BuilderException {
//
//        EnvVars envVar = new EnvVars();
//        envVar.setKey("some_key");
//        envVar.setValue("some_value");
//
//        ENV_VARS.add(envVar);
//
//        LogLevels logLevel = new LogLevels();
//        logLevel.setLevelCategory(LogLevelCategory.INFO);
//        logLevel.setPackageName("some_package");
//
//        LOG_LEVELS.add(logLevel);
//
//        try {
//            FreeStyleProject project = jenkinsRule.createFreeStyleProject();
//            project.getBuildersList().add(new CloudHubDeployer(CREDENTIALS_ID, ENVIRONMENT_ID, ORG_ID, REQUEST_MODE,
//                    APP_NAME,MULE_VERSION,REGION,FILE_PATH));
//
//            FreeStyleBuild build = project.scheduleBuild2(0).get();
//
//            String log = FileUtils.readFileToString(build.getLogFile());
//            assertThat(log, containsString(CloudHubDeployerTest.ASSERT_LOG_TEXT));
//        } catch (IOException | InterruptedException | ExecutionException ioe) {
//            throw new BuilderException(CloudHubDeployerTest.LOGIN_REQUEST_FAILED, ioe);
//        }
//    }

}
