package com.ty.service;

import com.ty.model.CurrencyConversionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

	private CurrencyService currencyService;

	@BeforeEach
	void setUp() {
		currencyService = Mockito.spy(new CurrencyService());
	}

	@Test
	void testConvertCurrency_SuccessfulConversion() {
		// Mock API response
		Map<String, Object> mockRates = Map.of("rates", Map.of("EUR", 0.954));

		doReturn(mockRates).when(currencyService).getExchangeRates("USD");

		CurrencyConversionRequest request = new CurrencyConversionRequest();
		request.setFrom("USD");
		request.setTo("EUR");
		request.setAmount(100);

		Map<String, Object> response = currencyService.convertCurrency(request);

		assertEquals("USD", response.get("from"));
		assertEquals("EUR", response.get("to"));
		assertEquals(100.0, response.get("amount"));
		assertEquals(94.5, response.get("convertedAmount"));
	}

	@Test
	void testConvertCurrency_InvalidBaseCurrency() {
		doReturn(null).when(currencyService).getExchangeRates("INVALID");

		CurrencyConversionRequest request = new CurrencyConversionRequest();
		request.setFrom("INVALID");
		request.setTo("EUR");
		request.setAmount(100);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			currencyService.convertCurrency(request);
		});

		assertTrue(exception.getMessage().contains("Invalid base currency"));
	}
}
