package text.detection.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {

	public static final String KOREA_IDENTITY_NUMBER_LIKE_CONTAINING_STR = "^[\\s\\S]*[0-9]{5}-[0-9]{5,}[\\s\\S]*$"; // matches
																														// strings
																														// containing
																														// numbers
																														// similar
																														// to
																														// ID
	public static final String KOREA_IDENTITY_NUMBER_CONTAINING_STR = "^[\\s\\S]*[0-9]{6}-[0-9]{7}[\\s\\S]*$"; // matches
																												// strings
																												// containing
																												// the
																												// ID
	public static final String KOREA_DRIVING_LICENSE_CONTAINING_STR = "^[\\s\\S]*[0-9]{2}-[0-9]{6}-[0-9]{2}[\\s\\S]*$"; // matches
																														// strings
																														// containing
																														// the
																														// Koran
																														// driving
																														// license
																														// number
	public static final String KOREA_DRIVING_LICENSE = "[0-9]{2}-[0-9]{6}-[0-9]{2}"; // matches Koran driving license
																						// number
	public static final String PASSPORT_MRZ = "^(?=.*<)(?=.*[0-9])(?=.*[A-Z])[A-Za-z0-9<]+$"; // matches strings like
																								// MRZ

	public static boolean isPassportNoLike(String value) {
		value = value.replace(System.lineSeparator(), "");
		value = value.replaceAll("\\s", "");
		Pattern idPattern = Pattern.compile("^(?=.*[0-9])[A-Za-z0-9]{6,}$");
		Matcher patternMatcher = idPattern.matcher(value);
		if (patternMatcher.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isPassportNo(String value) {
		value = value.replace(System.lineSeparator(), "");
		value = value.replaceAll("\\s", "");
		Pattern idPattern = Pattern.compile("^(?=.*[0-9])[A-Za-z0-9]{6,9}$");
		Matcher patternMatcher = idPattern.matcher(value);
		if (patternMatcher.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isMRZdetected(String text) {
		text = text.replace(System.lineSeparator(), "");
		text = text.replaceAll("\\s", "");
		Pattern idPattern = Pattern.compile(PASSPORT_MRZ, Pattern.CASE_INSENSITIVE);
		Matcher patternMatcher = idPattern.matcher(text);
		return patternMatcher.find();
	}

	public static boolean isMatchedCaseSensitive(String pattern, String text) {
		Pattern idPattern = Pattern.compile(pattern);
		Matcher patternMatcher = idPattern.matcher(text);
		return patternMatcher.matches();
	}

	public static boolean isMatched(String pattern, String text) {
		Pattern idPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher patternMatcher = idPattern.matcher(text);
		return patternMatcher.matches();
	}

	public static boolean isContained(String pattern, String text) {
		Pattern idPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher patternMatcher = idPattern.matcher(text);
		return patternMatcher.find();
	}

	public static String getMatchedPart(String pattern, String text) {
		Pattern idPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher patternMatcher = idPattern.matcher(text);
		if (patternMatcher.find()) {
			return patternMatcher.group(0);
		}
		return null;
	}

	public static void main(String... args) throws Exception {

		System.out.println(isMatched(KOREA_IDENTITY_NUMBER_CONTAINING_STR,
				"|자통차운전면허증(Drivet's Licenise)대구 890820-1234567  성 명 : JOSEPH BUCHMAN"));

		System.out.println(isMatched(KOREA_IDENTITY_NUMBER_CONTAINING_STR,
				"|자통차운전면허증(Drivet's Licenise)대구 890820-1234567  성 명 : JOSEPH BUCHMAN"));

		System.out.println(isMatched(KOREA_DRIVING_LICENSE_CONTAINING_STR, "대구 07-051914-00"));

		System.out.println(isMatched("성 명 :", "성 명 : JOSEPH BUCHMAN"));
		System.out.println(isContained("성 명 :", "성 명 : JOSEPH BUCHMAN"));

		System.out.println(isMatched(KOREA_DRIVING_LICENSE, "07-051914-00"));

		System.out.println(getMatchedPart(KOREA_DRIVING_LICENSE, "대구 07-051914-00"));
	}

}
