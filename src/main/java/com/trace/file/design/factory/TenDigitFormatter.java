package com.trace.file.design.factory;

/**
 * Concrete Factory Class
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 01-September-2026 20:49:13
 */
public class TenDigitFormatter implements FormatterInterface {

	public TenDigitFormatter(int digits) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String format(long last) {
		return String.format("%010d", last);
	}

}
