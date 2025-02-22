package com.ty.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ty.model.CurrencyConversionRequest;

@Service
public class CurrencyService {
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

	public Map<String, Object> getExchangeRates(String base) {
		String url = API_URL + base;

		try {
			return restTemplate.getForObject(url, Map.class);
		} catch (RestClientException e) {
			throw new RuntimeException("Error fetching exchange rates. API might be down.");
		}
	}

	public Map<String, Object> convertCurrency(CurrencyConversionRequest request) {
		Map<String, Object> rates = getExchangeRates(request.getFrom());

		if (rates == null || !rates.containsKey("rates")) {
			throw new IllegalArgumentException("Invalid base currency: " + request.getFrom());
		}

		Map<String, Double> rateMap = (Map<String, Double>) rates.get("rates");

		if (!rateMap.containsKey(request.getTo())) {
			throw new IllegalArgumentException("Invalid target currency: " + request.getTo());
		}

		double conversionRate = rateMap.get(request.getTo());
		double convertedAmount = request.getAmount() * conversionRate;

		// ✅ Ensure response format matches challenge requirements
		BigDecimal roundedAmount = new BigDecimal(convertedAmount).setScale(2, RoundingMode.HALF_UP);

		return Map.of("from", request.getFrom(), "to", request.getTo(), "amount", request.getAmount(),
				"convertedAmount", roundedAmount.doubleValue());
	}
}
