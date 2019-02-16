package com.helo.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.helo.payload.Token;

@Service
public class SpotifyService implements ISpotifyService {

	@Value("${spotify.api.auth.url}")
	private String spotifyAuthUrl;

	@Value("${spotify.api.search.url}")
	private String spotifySearchUrl;

	@Value("${spotify.api.client.id}")
	private String spotifyClientId;

	@Value("${spotify.api.client.secret}")
	private String spotifyClientSecret;

	/**
	 * Get the tracks using client credentials grant type and the name of the city
	 * also parse the response.
	 * 
	 * @param name is the genre of the playlist
	 * @return list of tracks by genre.
	 */
	public List<String> getTracksByCriteria(String name) {
		URI url = new UriTemplate(spotifySearchUrl).expand(name);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + getAccessToken(spotifyClientId, spotifyClientSecret));

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

		JSONArray tracks = new JSONObject(response.getBody()).getJSONObject("playlists").getJSONArray("items");
		List<String> trackNames = new ArrayList<>();

		for (int i = 0; i < tracks.length(); i++) {
			JSONObject item = tracks.getJSONObject(i);
			trackNames.add(item.get("name").toString());
		}

		return trackNames;
	}

	/**
	 * Use the aplication credential to get token to use the api.
	 * 
	 * @param username this is the client id
	 * @param password this is the client secret
	 * @return base 64 token
	 */
	private String getAccessToken(String username, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String plainCreds = username + ":" + password;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		headers.add("Authorization", "Basic " + base64Creds);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "client_credentials");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Token> response = restTemplate.postForEntity(spotifyAuthUrl, request, Token.class);

		return response.getBody().getAccess_token();
	}

}
