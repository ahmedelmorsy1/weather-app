package com.example.whatsweatherapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;
    public void findWeather(View view){

        Log.i("cityName",cityName.getText().toString());

        //to hide keyboard
        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);

        DownloadTask task=new DownloadTask();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=" +cityName.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName =(EditText) findViewById(R.id.city);
        resultTextView=(TextView)findViewById(R.id.weather);
    }
    public class DownloadTask extends AsyncTask<String , Void , String> {

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {
                url =new URL(urls[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data =reader.read();
                while(data != -1){
                    char current =(char)data;
                    result +=current;
                    data=reader.read(); }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"couldn't find weather", Toast.LENGTH_LONG).show();}
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String message="";
                JSONObject jsonObject =new JSONObject(result);
                String weatherInfo=jsonObject.getString("weather");
                Log.i("weather content ",weatherInfo);
                JSONArray arr=new JSONArray(weatherInfo);
                for(int i=0;i<arr.length();i++) {
                    JSONObject jsonpart = arr.getJSONObject(i);

                    String main = "";
                    String description = "";
                    main = jsonpart.getString("main");
                    description = jsonpart.getString("description");

                    if (main != "" && description != "") {
                        message = main + ":" + description + "\r\n";
                    }
                }
                if(message !=""){
                    resultTextView.setText(message);}
else{Toast.makeText(getApplicationContext(),"couldn't find weather",Toast.LENGTH_LONG).show();}

            } catch (JSONException e) { e.printStackTrace(); }}}}
