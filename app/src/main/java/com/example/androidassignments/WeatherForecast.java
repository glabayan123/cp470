package com.example.androidassignments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Context;
import android.util.Log;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    ImageView weather_Image;
    TextView textCurrent, textMin, textMax;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weather_Image = findViewById(R.id.weatherImage);
        textCurrent = findViewById(R.id.Currenttemp);
        textMin = findViewById(R.id.mintemp);
        textMax = findViewById(R.id.maxtemp);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        Spinner citySpinner = findViewById(R.id.citySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.canadian_cities,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = parent.getItemAtPosition(position).toString();
                new ForecastQuery().execute(city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        String minTemp;
        String maxTemp;
        String currentTemp;
        Bitmap weatherBitmap;

        @Override
        protected String doInBackground(String... args) {

            try {

                String city = args[0];
                String urlString =
                        "https://api.openweathermap.org/data/2.5/weather?q=" +
                                city + ",ca&APPID=3f24cef3afe7787ff729de9d68c9f3b3&mode=xml&units=metric";

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream stream = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(stream, "UTF-8");

                int eventType = parser.getEventType();
                String iconName = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    Log.i("PARSER", "Tag = " + parser.getName() + " | Event = " + eventType);

                    if (eventType == XmlPullParser.START_TAG) {

                        if ("temperature".equalsIgnoreCase(parser.getName())) {
                            currentTemp = parser.getAttributeValue(null, "value");
                            minTemp = parser.getAttributeValue(null, "min");
                            maxTemp = parser.getAttributeValue(null, "max");
                            publishProgress(25);
                        }

                        if ("weather".equalsIgnoreCase(parser.getName())) {
                            iconName = parser.getAttributeValue(null, "icon");
                            publishProgress(50);
                        }
                    }

                    eventType = parser.next();
                }

                String filename = iconName + ".png";
                Bitmap icon;

                if (fileExistance(filename)) {
                    FileInputStream fis = openFileInput(filename);
                    icon = BitmapFactory.decodeStream(fis);
                } else {
                    String iconURL = "https://openweathermap.org/img/w/" + iconName + ".png";

                    URL imgUrl = new URL(iconURL);
                    HttpURLConnection imgConn = (HttpURLConnection) imgUrl.openConnection();
                    imgConn.connect();
                    InputStream imgStream = imgConn.getInputStream();

                    icon = BitmapFactory.decodeStream(imgStream);

                    FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
                    icon.compress(Bitmap.CompressFormat.PNG, 80, fos);
                    fos.close();
                }

                weatherBitmap = icon;
                publishProgress(100);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            textCurrent.setText("Current: " + currentTemp + "°C");
            textMin.setText("Min: " + minTemp + "°C");
            textMax.setText("Max: " + maxTemp + "°C");

            weather_Image.setImageBitmap(weatherBitmap);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}
