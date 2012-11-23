package ds02.server.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class Formats {

	private static final ThreadLocal<NumberFormat> numberFormats = new ThreadLocal<NumberFormat>() {

		@Override
		protected NumberFormat initialValue() {
			return NumberFormat.getInstance();
		}

	};

	private static final ThreadLocal<DateFormat> dateFormats = new ThreadLocal<DateFormat>() {

		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("dd.MM.yyyy hh:mm z");
		}

	};

	private Formats() {

	}

	public static NumberFormat getNumberFormat() {
		return numberFormats.get();
	}

	public static DateFormat getDateFormat() {
		return dateFormats.get();
	}
}
