package com.almaviva.euregio.mock;

/**
 * Created by a.sciarretta on 18/04/2018.
 */

import com.loopj.android.http.*;

public class DistrictRestClient {
    private static final String BASE_URL = "https://test-fpas.prov.bz.it/familyapp/api/v1/districts";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
