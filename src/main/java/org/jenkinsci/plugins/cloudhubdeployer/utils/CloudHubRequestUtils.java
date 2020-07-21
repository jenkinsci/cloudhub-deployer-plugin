package org.jenkinsci.plugins.cloudhubdeployer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jenkinsci.plugins.cloudhubdeployer.CloudHubRequest;
import org.jenkinsci.plugins.cloudhubdeployer.common.DebugMode;
import org.jenkinsci.plugins.cloudhubdeployer.exception.CloudHubRequestException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Utility class for HTTP requests to CloudHub REST API.
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public final class CloudHubRequestUtils {

    private CloudHubRequestUtils() { }

    public static String login(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {
        HttpPost httpPost = new HttpPost(cloudhubRequest.getUrl());

        CloseableHttpClient httpclient = CloudHubRequestUtils.getHttpClient(cloudhubRequest, httpPost);

        httpPost.addHeader(Constants.LABEL_CONTENT_TYPE, Constants.MEDIA_TYPE_APP_JSON);
        httpPost.addHeader(Constants.LABEL_ACCEPT, Constants.MEDIA_TYPE_APP_JSON);
        String responseBody = null;

        try {
            StringEntity input = new StringEntity(cloudhubRequest.getUsersCredsJsonString());
            httpPost.setEntity(input);

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());


            responseBody = httpclient.execute(httpPost, responseHandler);
        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub login Request Failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String envList(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpGet httpGet = new HttpGet(Constants.CLOUDHUB_URL + "/accounts/api/organizations/"
                + cloudhubRequest.getOrgId() + "/environments" );

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpGet.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());

        String responseBody = null;

        try {

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpGet, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Get environment under organization or business group id failed." +
                    "Check organization or business group id");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String create(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpPost httpPost = new HttpPost(Constants.CLOUDHUB_URL + Constants.API_URI_V2 + Constants.URI_APPLICATION);

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpPost.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpPost.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());

        String responseBody = null;

        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                                        .addTextBody(Constants.LABEL_AUTOSTART, cloudhubRequest.getAutoStart())
                                        .addTextBody(Constants.LABEL_APP_INFO_JSON,
                                                new Gson().toJson(cloudhubRequest.getAppInfoJson()))
                                        .addBinaryBody(Constants.LABEL_FILE,
                new File(cloudhubRequest.getFilePath()));


        try {

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpPost, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub Create/Deploy App Failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String update(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpPut httpPut = new HttpPut(Constants.CLOUDHUB_URL + Constants.API_URI_V2 + Constants.URI_APPLICATION
                        + "/" + cloudhubRequest.getApiDomainName());

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpPut.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpPut.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());

        String responseBody = null;

        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addTextBody(Constants.LABEL_AUTOSTART, cloudhubRequest.getAutoStart())
                .addTextBody(Constants.LABEL_APP_INFO_JSON,
                        new Gson().toJson(cloudhubRequest.getAppInfoJson()))
                .addBinaryBody(Constants.LABEL_FILE,
                        new File(cloudhubRequest.getFilePath()));


        try {

            HttpEntity multipart = builder.build();
            httpPut.setEntity(multipart);

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpPut, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub update App Failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String updateFile(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpPost httpPost = new HttpPost(Constants.CLOUDHUB_URL + Constants.API_URI_V2 + Constants.URI_APPLICATION
                    + "/" + cloudhubRequest.getApiDomainName() + "/files");

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpPost.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpPost.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());

        String responseBody = null;

        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody(Constants.LABEL_FILE,
                        new File(cloudhubRequest.getFilePath()));

        try {

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpPost, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub Create/Deploy App Failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String delete(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpDelete httpDelete = new HttpDelete(Constants.CLOUDHUB_URL + Constants.API_URI_V2 +
                Constants.URI_APPLICATION + "/" + cloudhubRequest.getApiDomainName());

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpDelete.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpDelete.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());

        String responseBody = null;

        try {

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpDelete, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub delete API failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String restart(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpPost httpPost = new HttpPost(Constants.CLOUDHUB_URL + Constants.API_URI_NO_VERSION + Constants.URI_APPLICATION
                + "/"+ cloudhubRequest.getApiDomainName() + "/status");

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpPost.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpPost.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());
        httpPost.addHeader(Constants.LABEL_CONTENT_TYPE,Constants.MEDIA_TYPE_APP_JSON);
        String responseBody = null;


        try {

            httpPost.setEntity(new StringEntity("{\"status\":\"restart\"}"));

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpPost, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub api restart failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String apiStatus(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpGet httpGet = new HttpGet(Constants.CLOUDHUB_URL + Constants.API_URI_V2 + Constants.URI_APPLICATION
                + "/"+ cloudhubRequest.getApiDomainName());

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpGet.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpGet.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());

        String responseBody = null;


        try {

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpGet, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub api status check failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String apiList(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpGet httpGet = new HttpGet(Constants.CLOUDHUB_URL + Constants.API_URI_V2 + Constants.URI_APPLICATION);

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpGet.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpGet.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());

        String responseBody = null;


        try {

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpGet, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub api list check failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String createAutoScalePolicy(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpPost httpPost = new HttpPost(Constants.CLOUDHUB_URL + Constants.API_URI_V2 + Constants.URI_APPLICATION
                + "/" + cloudhubRequest.getApiDomainName() + "/autoscalepolicies");


        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpPost.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpPost.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());
        httpPost.addHeader(Constants.LABEL_ACCEPT, Constants.MEDIA_TYPE_APP_JSON);
        httpPost.addHeader(Constants.LABEL_CONTENT_TYPE, Constants.MEDIA_TYPE_APP_JSON);

        String responseBody = null;

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        try {

            httpPost.setEntity(new StringEntity(gson.toJson(cloudhubRequest.getAutoScalePolicy().get(0))));

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpPost, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub create autoscale policy call failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String getAutoScalePolicy(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpGet httpPut = new HttpGet(Constants.CLOUDHUB_URL + Constants.API_URI_V2 + Constants.URI_APPLICATION
                + "/" + cloudhubRequest.getApiDomainName() + "/autoscalepolicies");


        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpPut.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpPut.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());
        httpPut.addHeader(Constants.LABEL_ACCEPT, Constants.MEDIA_TYPE_APP_JSON);

        String responseBody = null;

        try {

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpPut, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub get autoscale policies call failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    public static String updateAutoScalePolicy(final CloudHubRequest cloudhubRequest) throws CloudHubRequestException {

        HttpPut httpPut = new HttpPut(Constants.CLOUDHUB_URL + Constants.API_URI_V2 + Constants.URI_APPLICATION
                + "/" + cloudhubRequest.getApiDomainName() + "/autoscalepolicies/" +
                cloudhubRequest.getAutoScalePolicy().get(0).getId());


        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpPut.addHeader(Constants.LABEL_ANYPNT_ENV_ID,cloudhubRequest.getEnvId());
        httpPut.addHeader(Constants.LABEL_AUTHORIZATION,"Bearer " + cloudhubRequest.getAccessToken());
        httpPut.addHeader(Constants.LABEL_CONTENT_TYPE, Constants.MEDIA_TYPE_APP_JSON);
        httpPut.addHeader(Constants.LABEL_ACCEPT, Constants.MEDIA_TYPE_APP_JSON);

        String responseBody = null;

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        try {


            httpPut.setEntity(new StringEntity(gson.toJson(
                    cloudhubRequest.getAutoScalePolicy().get(Constants.DEFALT_POLICY_INDEX))));

            ResponseHandler<String> responseHandler = CloudHubRequestUtils.getResponseHandler(cloudhubRequest
                    .getDebugMode(), cloudhubRequest.getLogger());

            responseBody = httpclient.execute(httpPut, responseHandler);

        } catch (IOException ioe) {
            cloudhubRequest.getLogger().println(ExceptionUtils.getFullStackTrace(ioe));
            throw new CloudHubRequestException("Cloudhub update autoscale policies call Failed.");
        } finally {
            CloudHubRequestUtils.closeHttpClient(httpclient, cloudhubRequest);
        }
        return responseBody;
    }

    private static ResponseHandler<String> getResponseHandler(final DebugMode debugMode, final PrintStream logger) {

        return response -> {
            if (debugMode.equals(DebugMode.ENABLED)) {
                logger.println("Response Headers: ");
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    logger.println(String.format("%s=%s", header.getName(), header.getValue()));
                }
            }

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (status >= Constants.HTTP_OK && status < Constants.HTTP_MULTI_CHOICES) {

                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            } else {
                if (entity != null) {
                    DeployHelper.logOutputStandard(logger,"Response from Cloudhub : ",EntityUtils.toString(entity));
                }
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            return null;
        };
    }

    private static CloseableHttpClient getHttpClient(final CloudHubRequest cloudhubRequest, final HttpRequestBase
            httpRequestBase) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(cloudhubRequest.getTimeoutConnect())
                .setConnectTimeout(cloudhubRequest.getTimeoutConnect())
                .setConnectionRequestTimeout(cloudhubRequest.getTimeoutResponse())
                .build();

        httpRequestBase.setConfig(requestConfig);

        return httpclient;
    }

    private static void closeHttpClient(final CloseableHttpClient httpclient, final CloudHubRequest cloudhubRequest) {
        if (httpclient != null) {
            try {
                httpclient.close();
            } catch (IOException ioe) {
                cloudhubRequest.getLogger().printf("IO Exception was encountered when closing HTTP client.  %s%n",
                        ioe);
            }
        }
    }
}
