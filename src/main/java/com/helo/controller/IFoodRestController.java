package com.helo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.helo.exception.NoTracksException;
import com.helo.payload.Track;
import com.helo.service.WeatherService;
import com.helo.util.Constants;

@RestController
@RequestMapping("/api/v1/ifood")
public class IFoodRestController {

	private Map<String, Object> response = new HashMap<>();

	@Autowired
	private WeatherService weatherService;

	/**
	 * This expose a rest service which response a playlist from spotify using the
	 * name of a city as parameter.
	 * 
	 * @param city the name of the city
	 * @return a list of tracks by weather
	 */
	@GetMapping("/tracks")
	public ResponseEntity<?> getTracksByCityWeather(@RequestParam String city) {
		try {
			Track tracks = weatherService.getTracksByCityWeather(city);
			return new ResponseEntity<>(tracks, HttpStatus.OK);
		} catch (NoTracksException e) {
			response.put(Constants.MESSAGE, "There are not tracks");
			response.put(Constants.ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		} catch (HttpClientErrorException e) {
			response.put(Constants.MESSAGE, "No valid parameter");
			response.put(Constants.ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * This expose a rest service which response a playlist from spotify using the
	 * coordinates as parameters.
	 * 
	 * @param lat coordinate in earth
	 * @param lon negative coordinate in earth
	 * @return a list of tracks by weather
	 */
	@GetMapping("/coordinates")
	public ResponseEntity<?> getTracksByCityCordinates(@RequestParam String lat, @RequestParam String lon) {
		try {
			return new ResponseEntity<>(weatherService.weatherByCoordinates(lat, lon), HttpStatus.OK);
		} catch (NoTracksException e) {
			response.put(Constants.MESSAGE, "There are not tracks");
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		} catch (HttpClientErrorException e) {
			response.put(Constants.MESSAGE, "No valid parameter");
			response.put(Constants.ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

}
