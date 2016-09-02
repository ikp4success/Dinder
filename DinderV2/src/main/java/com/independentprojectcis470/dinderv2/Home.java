package com.independentprojectcis470.dinderv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.independentprojectcis470.YelpAPI;
import com.independentprojectcis470.dinderv2.model.CardModel;
import com.independentprojectcis470.dinderv2.view.CardContainer;
import com.independentprojectcis470.dinderv2.view.SimpleCardStackAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Home extends Fragment implements LocationListener {
	View rootView;
	private CardContainer mCardContainer;
	private SimpleCardStackAdapter adapter;

	private LocationManager locationManager;
	private String provider;

	private String latitude;
	private String longitude;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//adding due to phone with no service
		String DEFAULT_LAT = "41.502733041";
		String DEFAULT_LONG = "-81.6743860";

		//((GlobalVariables)getContext().getApplicationContext()).setLikes(new HashMap<String, String>());

		LinearLayout wrapper = new LinearLayout(getActivity()); // http://stackoverflow.com/questions/13348455/can-you-use-the-merge-tag-with-fragments
		// , can only
		// use merge
		// tags with
		// activity
		rootView = inflater.inflate(R.layout.activity_home, wrapper, true);

		mCardContainer = (CardContainer) rootView.findViewById(R.id.layoutview);

		adapter = new SimpleCardStackAdapter(Home.this.getActivity());

		// Get the location manager
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
		// Define the criteria how to select the location in provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);

			Double lat = location.getLatitude();
			Double lng = location.getLongitude();

			latitude = String.valueOf(lat);
			longitude = String.valueOf(lng);
		} else {
			Log.e("Location: ", "Could not obtain location.");
			latitude = DEFAULT_LAT;
			longitude = DEFAULT_LONG;
		}

		Log.d("LAT", latitude);
		Log.d("LONG", longitude);
		
		new progression().execute();
		return wrapper;
	}

	public class progression extends AsyncTask<Void, Void, ArrayList<String>> {
		ProgressDialog dialog = new ProgressDialog(Home.this.getActivity());
		ArrayList<String> imageTitleArray = new ArrayList<String>();
		ArrayList<String> display_addressarray = new ArrayList<String>();

		@Override
		protected void onPreExecute() {
			this.dialog.setCancelable(false);
			this.dialog
					.setMessage("Loading restaurants around you, Please Wait.......we know you're hungry!");
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setProgress(0);
			this.dialog.setMax(30);
			this.dialog.show();
		}

		@Override
		protected ArrayList<String> doInBackground(Void... params)
				throws NullPointerException {
			try {
				/*
				 * This is run on a background thread, so we can sleep here or
				 * do whatever we want without blocking UI thread. A more
				 * advanced use would download chunks of fixed size and call
				 * publishProgress();
				 */

				ArrayList<String> imageArray = new ArrayList<String>();

				String term = "dinner";
				String searchLimit = "9";

				JSONArray results = getYelpResults(term, searchLimit);

				for (int i = 0; i < results.size(); i++) {

					JSONObject each_Business = (JSONObject) results.get(i);
					String imageurl = each_Business.get("image_url").toString();
					imageArray.add(imageurl.replace("ms.jpg", "ls.jpg"));
					String imageTitle = each_Business.get("name").toString();
					imageTitleArray.add(imageTitle);

					JSONObject location_business = (JSONObject) each_Business
							.get("location");
					JSONArray displayAddress = (JSONArray) location_business
							.get("display_address");
					display_addressarray.add(displayAddress.get(0).toString());

				}
				return imageArray;

			} catch (Exception e) {
				Log.e("tag", e.getMessage());
				/*
				 * The task failed
				 */

			}

			/*
			 * The task succeeded
			 */
			return null;
		}

		protected void onProgressUpdate(Void... params) {
			super.onProgressUpdate(params);

		}

		@Override
		protected void onPostExecute(ArrayList<String> imageArray) {
			super.onPostExecute(imageArray);
			// UNIVERSAL IMAGE LOADER SETUP
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.imageScaleType(ImageScaleType.EXACTLY)
					.displayer(new FadeInBitmapDisplayer(300)).build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					getActivity()).defaultDisplayImageOptions(defaultOptions).build();

			// END - UNIVERSAL IMAGE LOADER SETUP
			final ImageLoader imageLoader = ImageLoader.getInstance(); // Get
																		// singleton
																		// instance
			ImageLoader.getInstance().init(config);
			imageLoader.init(ImageLoaderConfiguration.createDefault(Home.this
					.getActivity()));
			ImageSize targetSize = new ImageSize(1480, 1313);

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.ic_drawer)
					.cacheInMemory(false)
					.build();

			for (int i = 0; i < imageArray.size(); i++) {

				final AtomicReference<String> imastr_Title = new AtomicReference<String>(imageTitleArray.get(i));
				final AtomicReference<String> displayAddr_Title = new AtomicReference<String>(display_addressarray.get(i));
				if (i < imageArray.size()) {
					imageLoader.loadImage(imageArray.get(i), null, options,
							new SimpleImageLoadingListener() {
								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									final CardModel cd = new CardModel(imastr_Title.get(),
											displayAddr_Title.get(),
											new BitmapDrawable(getResources(),
													Bitmap.createScaledBitmap(
															loadedImage, 1100,
															900, true)));
									cd.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
										@Override
										public void onLike() {
											//add restaurant to queue of liked
											Toast.makeText(getActivity(),
													cd.getTitle() + " looks good!",
													Toast.LENGTH_SHORT).show();
											List<String> titles = ((GlobalVariables)getContext().getApplicationContext()).getTitles();
											boolean add = true;
											for(String title : titles){
												if(title.equals(cd.getTitle())){
													//don't add
													add = false;
												}
											}
											if(add) {
												//address as param 1 and title as param 2
												((GlobalVariables) getContext().getApplicationContext()).addToLikedRestaurantList(cd.getTitle(), cd.getDescription());
											}

										}

										@Override
										public void onDislike() {
											//display message
											Toast.makeText(getActivity(),
													"Disliked the restaurant.",
													Toast.LENGTH_SHORT).show();
										}
									});
									adapter.add(cd);

									mCardContainer.setAdapter(adapter);

								}
							});
				}
				if (i == imageArray.size() - 1) {
					Resources r = getResources();
					CardModel cdend = new CardModel("End of Line",
							"Card will refresh when you're in a new location",
							r.getDrawable(R.drawable.aboutredico));
					cdend.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
						@Override
						public void onLike() {
						}

						@Override
						public void onDislike() {}
					});

					adapter.add(cdend);

					mCardContainer.setAdapter(adapter);

				}

			}

			if (this.dialog.isShowing()) {

				this.dialog.dismiss();

			}

		}
	}

	public Bitmap changetoBitmap(String urls) {
		String urldisplay = urls;
		Bitmap mIcon11 = null;
		try {
			InputStream instream = new java.net.URL(urldisplay).openStream();
			mIcon11 = ImageResize(instream, 100, 100);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	public Bitmap ImageResize(InputStream instream, int x, int y)
			throws MalformedURLException, IOException {

		Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
				BitmapFactory.decodeStream(instream), x, y);
		// ThumbImage.recycle();
		return ThumbImage;

	}

	public JSONArray getYelpResults(String term, String RequestLimit) {

		final String CONSUMER_KEY = "xphjJIUXYMkUPTara_K6fw";
		final String CONSUMER_SECRET = "JU3xizy0Ftpx3x1cRF3QZEHhLmM";
		final String TOKEN = "w_P4fQobrmGHdAFNmO4On0RlhmXAt3lb";
		final String TOKEN_SECRET = "k2tI945nAnQa8V_etgjaIT3500Y";

		YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN,
				TOKEN_SECRET);
		String searchResponseJSON = yelpApi.searchForBusinessesByLatLong(term,
				latitude, longitude, RequestLimit);

		JSONParser parser = new JSONParser();
		JSONObject response = null;
		try {
			response = (JSONObject) parser.parse(searchResponseJSON);
		} catch (ParseException pe) {
			Log.e("Json Error Title", "Error: could not parse JSON response:");
			Log.e("Json response", searchResponseJSON);
			// System.exit(1);
		}
		JSONArray businesses = (JSONArray) response.get("businesses");

		return businesses;

	}

	/* Request updates at startup */
	@Override
	public void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	public void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		Double lat = (Double) (location.getLatitude());
		Double lng = (Double) (location.getLongitude());
		latitude = String.valueOf(lat);
		longitude = String.valueOf(lng);
		Log.d("LAT", latitude);
		Log.d("LONG", longitude);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this.getActivity(), "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this.getActivity(), "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

}
