package no.hit.kvisli.Hour17FlickrPhoto;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Class holding data (except image) for one Flickr-photo
public class FlickrPhoto extends Object{
	String id;
	String owner;
	String secret;
	String server;
	String farm;
	String title;
	Boolean isPublic;
	Boolean isFriend;
	Boolean isFamily;

    // Build a FlickrPhoto-object from a JSONObject
	public FlickrPhoto(JSONObject jsonPhoto) throws JSONException{
		this.id       = (String)  jsonPhoto.optString("id");
		this.secret   = (String)  jsonPhoto.optString("secret");
		this.owner    = (String)  jsonPhoto.optString("owner");
		this.server   = (String)  jsonPhoto.optString("server");
		this.farm     = (String)  jsonPhoto.optString("farm");
		this.title    = (String)  jsonPhoto.optString("title");
		this.isPublic = (Boolean) jsonPhoto.optBoolean("ispublic");
		this.isFriend = (Boolean) jsonPhoto.optBoolean("isfriend");
		this.isFamily = (Boolean) jsonPhoto.optBoolean("isfamily");
	}
public String getPhotoUrl(boolean big){
	String format="n";
	if(big){
		format="c";
	}
	String url="http://farm"+this.farm+".staticflicker.com/"+this.server+"/"+this.id+"_"+this.secret+"_"+format+".jpg";

	return url;
}

    // Static method to build an ArrayList of FlickrPhoto-objects from a JSON-string
    public static ArrayList <FlickrPhoto> makePhotoList (String jsonPhotoData ) throws JSONException, NullPointerException {
		// ArrayList with FlickrPhoto's to be returned
		ArrayList <FlickrPhoto> flickrPhotos = new ArrayList<FlickrPhoto>();
		// Build JSONObject from string with JSON-data
		JSONObject data  = new JSONObject(jsonPhotoData);
		// Extract JSON-object named "photos"
		JSONObject photos = data.optJSONObject("photos");
		// Extract JSON-array named "photo"
		JSONArray photoArray = photos.optJSONArray("photo");
		// Loop through all photo-entries, build FlickrPhoto-objects and add to Arraylist
		for(int i = 0; i < photoArray.length(); i++) {
            // get one JSONObject from ArrayList
			JSONObject photo=	(JSONObject) photoArray.get(i);
            // Build FlickrPhoto-object
			FlickrPhoto currentPhoto = new FlickrPhoto (photo);
            // Insert into ArrayList
			flickrPhotos.add(currentPhoto);
		}
		// return ArrayList
		return flickrPhotos;
	}

}
