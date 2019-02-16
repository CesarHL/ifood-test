package com.helo.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.helo.exception.NoTracksException;
import com.helo.payload.Track;
import com.helo.payload.Weather;

@Service
public class WeatherService implements IWeatherService {

	private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

	@Value("${weather.api.url}")
	private String weatherApiUrl;

	@Value("${weather.api.appid}")
	private String weatherApiKey;

	@Value("${weather.api.url.coordinates}")
	private String weatherApiCoordinatesUrl;

	@Autowired
	private SpotifyService spotifyService;

	/**
	 * Search the temperature with city as parameter and convert it to Celsius.
	 * 
	 * @param city the name of the city.
	 * @return
	 */
	public Track getTracksByCityWeather(String city) throws NoTracksException {
		logger.info("Requesting current weather for {}", city);
		URI url = new UriTemplate(weatherApiUrl).expand(city, weatherApiKey);
		Weather response = invoke(url, Weather.class);
		List<String> trackList = getTracksByCriteria((int) kelvinToCelsius(response.getMain().getTemp()));
		if (!trackList.isEmpty()) {
			Track track = new Track();
			track.setTacks(trackList);
			return track;
		} else {
			throw new NoTracksException();
		}
	}

	/**
	 * Search the temperature with given lat and lon.
	 * 
	 * @param lat coordinate in earth
	 * @param lon negative coordinate in earth
	 * @return
	 * @throws NoTracksException
	 */
	public Track weatherByCoordinates(String lat, String lon) throws NoTracksException {
		logger.info("Requesting current weather for {} and {}", lat, lon);
		URI url = new UriTemplate(weatherApiCoordinatesUrl).expand(lat, lon, weatherApiKey);
		Weather response = invoke(url, Weather.class);
		List<String> trackList = getTracksByCriteria((int) kelvinToCelsius(response.getMain().getTemp()));
		if (!trackList.isEmpty()) {
			Track track = new Track();
			track.setTacks(trackList);
			return track;
		} else {
			throw new NoTracksException();
		}
	}

	/**
	 * Fallback method in case weather api does not respond
	 * 
	 * @param city
	 * @return
	 */
	public List<String> getTracksByCityWeatherDefault(String city) {
		List<String> serverContentList = new ArrayList<>();
		serverContentList.add("No server content" + city);
		return serverContentList;
	}

	/**
	 * Generic method to invoke services with rest template.
	 * 
	 * @param url          is the endpoint of the api
	 * @param responseType is the payload
	 * @return json body of the request
	 */
	private <T> T invoke(URI url, Class<T> responseType) {
		RestTemplate restTemplate = new RestTemplate();
		RequestEntity<?> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<T> response = restTemplate.exchange(request, responseType);
		return response.getBody();
	}

	/**
	 * The weather api returns the temperature in kelvin by default.
	 * 
	 * @param temp is the temperature in kelvin
	 * @return the convertion of kelvin to celsius
	 */
	private double kelvinToCelsius(String temp) {
		return Double.valueOf(temp) - 273.0;
	}

	/**
	 * It takes the temperature looking for specific temperatures.
	 * 
	 * @param temp is the temperature in Celsius.
	 * @return list of tracks by weather and temperature.
	 * @throws NoTracksException if the request is empty.
	 */
	private List<String> getTracksByCriteria(Integer temp) {
		if (temp > 30) {
			return spotifyService.getTracksByCriteria("party");
		} else if (temp >= 15 && temp <= 30) {
			return spotifyService.getTracksByCriteria("pop");
		} else if (temp >= 10 && temp <= 14) {
			return spotifyService.getTracksByCriteria("rock");
		} else {
			return spotifyService.getTracksByCriteria("classical");
		}
	}

}
