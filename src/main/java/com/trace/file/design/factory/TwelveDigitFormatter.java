package com.trace.file.design.factory;

/**
 * Concrete Factory Class
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 01-September-2026 20:46:01
 */
public class TwelveDigitFormatter implements FormatterInterface {

	public TwelveDigitFormatter(int digits) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String format(long last) {
		return String.format("%012d", last);
	}

}
