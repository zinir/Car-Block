package com.projects.nir.myapplication.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Zilkha on 29/11/2014.
 */
public class SearchWebAsyncTask extends AsyncTask<String, String, String> {


    private Context mActivity;
    private ProgressDialog mDialog;
    private StringBuilder Response;
    private IAsyncCallBack TaskSearchCallBack;
    private String _progressString;

    public SearchWebAsyncTask(Context context,IAsyncCallBack searchCallBack,boolean hasProgress,String progressString) {
        mActivity = context;
        TaskSearchCallBack = searchCallBack;
        if (hasProgress)
            mDialog = new ProgressDialog(context);
        else
            mDialog = null;
        _progressString = progressString;
    }

    protected String doInBackground(String... serachString) {
        return search(serachString[0]);
    }

    protected void onPreExecute() {
        // Reset the progress bar...
        if (mDialog !=null)
        {
            mDialog.setTitle("Please Wait");
            mDialog.setMessage(_progressString);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }

    protected void onProgressUpdate(String... progress) {
    }

    protected void onPostExecute(String result) {
        if (TaskSearchCallBack != null)
            TaskSearchCallBack.SearchTaskDone(result);

        if (mDialog !=null)
        {
            // Close the progress dialog
            mDialog.dismiss();
        }
    }

    private String search(String searchString) {
        BufferedReader input = null;
        HttpURLConnection httpCon = null;
        InputStream input_stream = null;
        InputStreamReader input_stream_reader = null;
        Response = new StringBuilder();
        try {
            URL url = new URL(searchString);
            httpCon = (HttpURLConnection) url.openConnection();
            if (httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int lengthOfFile =  httpCon.getContentLength();
            input_stream = httpCon.getInputStream();
            input_stream_reader = new InputStreamReader(input_stream);
            input = new BufferedReader(input_stream_reader);
            String line;
            long total = 0;
            while ((line = input.readLine()) != null) {
                total += line.length();
                Response.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input_stream_reader.close();
                    input_stream.close();
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (httpCon != null) {
                    httpCon.disconnect();
                }
            }
        }
        return Response.toString();
    }

    public String getResponse() {
        return Response.toString();
    }
}
