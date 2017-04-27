package no.hit.kvisli.Hour17FlickrPhoto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;


import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
    ProgressBar mProgressBar;

    private ArrayList<FlickrPhoto> mPhotos = new ArrayList<FlickrPhoto>();

    // Replace KEY and SIG with new values from https://www.flickr.com/services/api/explore/flickr.photos.getRecent
   // private final static String API_KEY ="e737c63bdef5cb28bb3d2790fb2c7768";
    //private final static String API_SIG ="72b1cdfcf6876e40a20c7424427ec26c";
    // Build URL-string to endpoint for retrieving data about newest photos from Flickr
    private final static String photoListUrl ="https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=906e25f0690cfbd53c7ddec7fede56f1&format=json&nojsoncallback=1&api_sig=02bdb7824e0a95dd92212cc504bff93d";



      // "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=" + API_KEY +
                                           // "&format=json&nojsoncallback=1&api_sig=" + API_SIG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display progressbar while fetching data from Flickr asynchronously
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

        // Check is network is available
        if (isOnline()){
            LoadPhotos task = new LoadPhotos();
            task.execute();
        }else{
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this.getApplicationContext(), "Please connect to retrieve photos", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<FlickrPhoto> getPhotos() {
        return mPhotos;
    }
    public void setPhotos(ArrayList<FlickrPhoto> mPhotos) {
        this.mPhotos = mPhotos;
    }

    // Check if network connection is available
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    **/

    // Display fragment with list og photo-titles
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showList(){
        PhotoListFragment photoListFragment = new PhotoListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.layout_container, photoListFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    // Private inner class for doing stuff asynchronously
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class LoadPhotos extends AsyncTask<String , String , Long > {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Long result) {
            if (result==0){
                // Show fragment with photo-list if data was found
                MainActivity.this.showList();
            }else{
                Toast.makeText(MainActivity.this.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected Long doInBackground(String... params) {
            // Get list og photos asynchronously via http-connection to Flickr endpoint
            HttpURLConnection connection = null;
            try {
                URL dataUrl = new URL(photoListUrl);
                connection = (HttpURLConnection) dataUrl.openConnection();
                connection.connect();
                int status = connection.getResponseCode();
                Log.d("connection", "status " + status);
                if (status == HttpURLConnection.HTTP_OK ){
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String responseString;
                    StringBuilder sb = new StringBuilder();
                    while ((responseString = reader.readLine()) != null) {
                        sb = sb.append(responseString);
                    }
                    //String photoData = sb.toString();
                    mPhotos = FlickrPhoto.makePhotoList(sb.toString());
                    Log.d("connection", sb.toString());
                    return (0l);
                }else{
                    return (1l);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return (1l);
            }
            catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
                return (1l);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return (1l);
            } catch (JSONException e) {
                e.printStackTrace();
                return (1l);
            } finally {
                connection.disconnect();
            }
        }
    }

}
