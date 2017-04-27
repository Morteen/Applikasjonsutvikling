package no.hit.kvisli.Hour17FlickrPhoto;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// Fragment displaying list of photo-data of recent Flickr-photos
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PhotoListFragment extends ListFragment    {
	private String[] mTitles;
    private ArrayList <FlickrPhoto> photos;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MainActivity currentActivity = (MainActivity) this.getActivity();
        // Retrieve list of photos from Flickr
		photos = currentActivity.getPhotos();
        // Build array of photo-titles
		mTitles = new String[photos.size()];
		for (int i=0; i < photos.size(); i++){
			mTitles[i] =photos.get(i).title;
		}
        // Build new ArrayAdapter with phototitles for populating ListFragment
		setListAdapter(new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1, mTitles));
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Simple version from Hour 17: Just display selected position using Toast
		//Toast.makeText(this.getActivity().getApplicationContext(),
				//mTitles[position], Toast.LENGTH_SHORT).show();

		visBildeFragment(position);

	}
	private void visBildeFragment(int position){

		FlickrPhoto valgtBilde=photos.get(position);
		String photoURL=valgtBilde.getPhotoUrl(true);

		ImageViewFragment frag = new ImageViewFragment();
		Bundle bundle= new Bundle();

		bundle.putString("photoURL",photoURL);
		frag.setArguments(bundle);
		FragmentManager fm=getActivity().getFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();

		ft.replace(R.id.layout_container,frag);
		ft.addToBackStack("image");
		ft.commit();


	}
} 
