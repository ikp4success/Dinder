package com.independentprojectcis470;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.util.Log;

import com.beust.jcommander.Parameter;

public class YelpAPI {

	private static final String API_HOST = "api.yelp.com";
	private static final String DEFAULT_TERM = "dinner";
	private static final String DEFAULT_LOCATION = "Cleveland, OH";
	private static final String DEFAULT_LAT = "41.502733041";
	private static final String DEFAULT_LONG = "-81.6743860";
	private static final int SEARCH_LIMIT = 3;
	private static final String SEARCH_PATH = "/v2/search";
	private static final String BUSINESS_PATH = "/v2/business";

	/*
	 * Update OAuth credentials below from the Yelp Developers API site:
	 * http://www.yelp.com/developers/getting_started/api_access
	 */
	private static final String CONSUMER_KEY = "xphjJIUXYMkUPTara_K6fw";
	private static final String CONSUMER_SECRET = "JU3xizy0Ftpx3x1cRF3QZEHhLmM";
	private static final String TOKEN = "w_P4fQobrmGHdAFNmO4On0RlhmXAt3lb";
	private static final String TOKEN_SECRET = "k2tI945nAnQa8V_etgjaIT3500Y";

	OAuthService service;
	Token accessToken;

	/**
	 * Setup the Yelp API OAuth credentials.
	 * 
	 * @param consumerKey
	 *            Consumer key
	 * @param consumerSecret
	 *            Consumer secret
	 * @param token
	 *            Token
	 * @param tokenSecret
	 *            Token secret
	 */
	public YelpAPI(String consumerKey, String consumerSecret, String token,
			String tokenSecret) {
		this.service = new ServiceBuilder().provider(TwoStepOAuth.class)
				.apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}

	/**
	 * Creates and sends a request to the Search API by term and location.
	 * <p>
	 * See <a
	 * href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp
	 * Search API V2</a> for more info.
	 * 
	 * @param term
	 *            <tt>String</tt> of the search term to be queried
	 * @param location
	 *            <tt>String</tt> of the location
	 * @return <tt>String</tt> JSON Response
	 */
	public String searchForBusinessesByLocation(String term, String location,
			String SearchLimit) {
		OAuthRequest request = createOAuthRequest(SEARCH_PATH);
		try {
			if (term != null && location != null) {
				request.addQuerystringParameter("term", term);
				request.addQuerystringParameter("location", location);
			} else {
				request.addQuerystringParameter("term", DEFAULT_TERM);
				request.addQuerystringParameter("location", DEFAULT_LOCATION);
			}

			if (SearchLimit != null) {
				request.addQuerystringParameter("limit",
						String.valueOf(SearchLimit));
			} else {
				request.addQuerystringParameter("limit",
						String.valueOf(SEARCH_LIMIT));
			}
		} catch (NullPointerException nex) {
			Log.e("OAuthRequest request by location null exception",
					nex.toString());
		}
		return sendRequestAndGetResponse(request);
	}

	public String searchForBusinessesByLatLong(String term, String latitude,
			String longitude, String SearchLimit) {
		OAuthRequest request = createOAuthRequest(SEARCH_PATH);
		try {
			if (term != null && latitude != null && longitude != null) {
				request.addQuerystringParameter("term", term);
				request.addQuerystringParameter("ll", latitude + ","
						+ longitude);
			} else {
				request.addQuerystringParameter("term", DEFAULT_TERM);
				request.addQuerystringParameter("ll", DEFAULT_LAT + ","
						+ DEFAULT_LONG);
			}

			if (SearchLimit != null) {
				request.addQuerystringParameter("limit",
						String.valueOf(SearchLimit));
			} else {
				request.addQuerystringParameter("limit",
						String.valueOf(SEARCH_LIMIT));
			}
		} catch (NullPointerException nex) {
			Log.e("OAuthRequest request by location null exception",
					nex.toString());
		}
		return sendRequestAndGetResponse(request);
	}

	/**
	 * Creates and sends a request to the Business API by business ID.
	 * <p>
	 * See <a
	 * href="http://www.yelp.com/developers/documentation/v2/business">Yelp
	 * Business API V2</a> for more info.
	 * 
	 * @param businessID
	 *            <tt>String</tt> business ID of the requested business
	 * @return <tt>String</tt> JSON Response
	 */
	public String searchByBusinessId(String businessID) {
		OAuthRequest request = null;
		try {
			request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
		} catch (NullPointerException nex) {
			Log.e("OAuthRequest request by businessID null exception",
					nex.toString());
		}
		return sendRequestAndGetResponse(request);
	}

	/**
	 * Creates and returns an {@link OAuthRequest} based on the API endpoint
	 * specified.
	 * 
	 * @param path
	 *            API endpoint to be queried
	 * @return <tt>OAuthRequest</tt>
	 */
	private OAuthRequest createOAuthRequest(String path) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST
				+ path);
		return request;
	}

	/**
	 * Sends an {@link OAuthRequest} and returns the {@link Response} body.
	 * 
	 * @param request
	 *            {@link OAuthRequest} corresponding to the API request
	 * @return <tt>String</tt> body of API response
	 */
	private String sendRequestAndGetResponse(OAuthRequest request) {
		// System.out.println("Querying " + request.getCompleteUrl() + " ...");
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

}