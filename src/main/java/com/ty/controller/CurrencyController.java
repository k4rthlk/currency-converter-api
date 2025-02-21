package com.ty.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ty.model.CurrencyConversionRequest;
import com.ty.service.CurrencyService;

@RestController
public class CurrencyController {
	private final CurrencyService currencyService;

	public CurrencyController(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@GetMapping("/rates")
	public ResponseEntity<Map<String, Object>> getExchangeRates(@RequestParam(defaultValue = "USD") String base) {
		return ResponseEntity.ok(currencyService.getExchangeRates(base));
	}

	@PostMapping("/convert")
	public ResponseEntity<Map<String, Object>> convertCurrency(@RequestBody CurrencyConversionRequest request) {
		return ResponseEntity.ok(currencyService.convertCurrency(request));
	}

}
