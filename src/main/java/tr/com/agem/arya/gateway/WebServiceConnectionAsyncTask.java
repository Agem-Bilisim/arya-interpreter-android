package tr.com.agem.arya.gateway;

import android.content.Context;
import android.os.AsyncTask;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import tr.com.agem.core.gateway.model.AryaRequest;


public class WebServiceConnectionAsyncTask extends AsyncTask<String, Void, String> {
    AryaRequest request;
    String url;
    Context context;

    public WebServiceConnectionAsyncTask(String url, AryaRequest request, Context context) {

        this.request = request;
        this.url = url;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        return AryaInterpreterHelper.callUrl(url, request);
    }

    @Override
    protected void onPostExecute(String responseStr) {
        super.onPostExecute(responseStr);
    }

}