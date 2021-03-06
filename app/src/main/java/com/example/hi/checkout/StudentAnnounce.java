package com.example.hi.checkout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SRIDHARAN JOTHIRAMAN on 8/12/2017.
 */

public class StudentAnnounce extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_information);
        Bundle bundle = getIntent().getExtras();
        String dept = bundle.getString("department");
        String cid= bundle.getString("cid");
        RetrieveInfo(dept,cid);
    }

    public void draw(String res)
    {
        try {
            String[] arr = res.split("###");
            TableLayout ll = (TableLayout) findViewById(R.id.displayLinear1);
            for (int i = 0; i < arr.length; i++)
            {
                TableRow row = new TableRow(this);
                TableRow row1 = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView tv = new TextView(this);
                TextView t1 = new TextView(this);
                byte[] data = Base64.decode(arr[i], Base64.DEFAULT);
                String text = new String(data);
                tv.setText(text);
                t1.setText("\n");
                t1.setBackgroundResource(R.drawable.line);
                tv.setPadding(20, 30, 40, 50);
                // tv.setBackgroundResource(R.drawable.cell_shape);
                row.addView(tv);
                row1.addView(t1);
                ll.addView(row);
                ll.addView(row1);
            }
            Toast.makeText(getApplicationContext(),"Updated Successfully!!",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Failed to update!",Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void RetrieveInfo(final String dept,final String cid)
    {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            ProgressDialog pd = new ProgressDialog(StudentAnnounce.this);
            protected void onPreExecute()
            {
                pd.setMessage("Fetching...");
                pd.show();
            }
            protected String doInBackground(String... arg0)
            {
                try
                {
                    URL url = new URL("http://checkoutstaff.000webhostapp.com/retrieveinfo.php");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("de",dept);
                    postDataParams.put("ci",cid);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode=conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK)
                    {

                        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line="";
                        while((line = in.readLine()) != null)
                        {
                            sb.append(line);
                        }
                        in.close();
                        return sb.toString();
                    }
                    else
                    {
                        return new String("false : "+responseCode);
                    }
                }
                catch(Exception e)
                {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result)
            {
                pd.dismiss();
               draw(result);
            }

            public String getPostDataString(JSONObject params) throws Exception
            {
                StringBuilder result = new StringBuilder();
                boolean first = true;
                Iterator<String> itr = params.keys();
                while(itr.hasNext())
                {
                    String key= itr.next();
                    Object value = params.get(key);
                    if (first)
                        first = false;
                    else
                        result.append("&");
                    result.append(URLEncoder.encode(key, "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(value.toString(), "UTF-8"));

                }
                return result.toString();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(dept,cid);
    }
}
