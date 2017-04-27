package no.hit.kvisli.Hour17FlickrPhoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class ImageViewFragment extends Fragment {
    ImageView mImageView ;


    private OnFragmentInteractionListener mListener;

    public ImageViewFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {



        super.onActivityCreated(savedInstanceState);


        Bundle bundle= new Bundle();
        String url;
        if(bundle!=null){
            url=bundle.getString("photoURL");
            if(url!=null){
                LoadImage li = new LoadImage(mImageView,url);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mImageView=(ImageView) inflater.inflate(R.layout.fragment_image_view, container, false);

        return   mImageView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class LoadImage extends AsyncTask<String,String,Long> {

        ImageView mimageView;
        String mImageString;
        Bitmap mBitmap;
        public LoadImage(ImageView mimageView,String mImageString){
            this.mimageView=mimageView;
            this.mImageString=mImageString;
        }


        @Override
        protected Long doInBackground(String... params) {
            URLConnection connection;
            try {
                URL imageUrl= new URL(mImageString);

                 connection = imageUrl.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                mBitmap= BitmapFactory.decodeStream(is);
                return (0l);


            }catch (MalformedURLException e){
                return (1l);
            }catch (IOException e){
                return (1l);
            }



        }

        @Override
        protected void onPostExecute(Long aLong) {
            if(aLong==(0l))
           mImageView.setImageBitmap(mBitmap);
            super.onPostExecute(aLong);
        }
    }


}
