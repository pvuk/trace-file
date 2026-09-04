package com.trace.file.design.factory;

/**
 * Factory to choose Concrete Factory class.</br>
 * 
 * ✅ Why Strategy Pattern Works Here
	Flexibility: Easy to add new formatting rules later.
	
	Clean separation: Each rule lives in its own class.
	
	Runtime choice: You can decide which formatter to use based on password length dynamically.</br>
	
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 01-September-2026 20:51:09
 */
public class FormatterFactory {

	public static FormatterInterface getFormatter(int digits) {
		switch (digits) {
		case 4:
			return new FourDigitFormatter(digits);
		case 8:
			return new EightDigitFormatter(digits);
		case 10:
			return new TenDigitFormatter(digits);
		case 12:
			return new TwelveDigitFormatter(digits);
		default:
			throw new IllegalArgumentException("Unsupported Length: " + digits);
		}
	}
}
