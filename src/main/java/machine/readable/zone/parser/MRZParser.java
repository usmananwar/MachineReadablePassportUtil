package machine.readable.zone.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MRZParser {
	private static String FILLER_CHARACTER = ";";

	private String mrzString;
	private MRZFormat format;
	private String[] rows;
	private List<Locale> localesList;

	public MRZParser(String mrzString) throws MRZException {
		try {
			this.mrzString = mrzString;
			format = MRZFormat.detectFormat(mrzString);
			rows = MRZFormat.getRows(mrzString);
			generateLocalList();
		} catch (Exception e) {
			throw new MRZException(e, mrzString, format);
		}
	}

	private void generateLocalList() {
		localesList = new ArrayList<Locale>();
		for (String country : Locale.getISOCountries()) {
			localesList.add(new Locale("", country));
		}
	}

	public MRZPassport getMRZPassport() throws MRZException {
		MRZPassport passport = new MRZPassport();
		try {
			// passport.setPassportType(parseString(new MRZSelectionCriteria(0, 1, 0,
			// FILLER_CHARACTER)));
			passport.setType(parseString(new MRZSelectionCriteria(1, 1, 0, FILLER_CHARACTER)));
			passport.setIssuingCountry(parseCountry(new MRZSelectionCriteria(2, 3, 0, FILLER_CHARACTER)));
			String[] names = parseNames(new MRZSelectionCriteria(5, 39, 0, FILLER_CHARACTER));
			passport.setSurname(names[0]);
			passport.setGivenNames(names[1]);
			passport.setPassportNumber(parseString(new MRZSelectionCriteria(0, 9, 1, FILLER_CHARACTER)));
			passport.setFirstCheckDigit(parseString(new MRZSelectionCriteria(9, 1, 1, FILLER_CHARACTER)));
			passport.setNationality(parseCountry(new MRZSelectionCriteria(10, 3, 1, FILLER_CHARACTER)));
			passport.setDob(parseDate(new MRZSelectionCriteria(13, 6, 1, FILLER_CHARACTER)));
			passport.setSecondCheckDigit(parseString(new MRZSelectionCriteria(19, 1, 1, FILLER_CHARACTER)));
			passport.setGender(parseString(new MRZSelectionCriteria(20, 1, 1, FILLER_CHARACTER)));
			passport.setExpirationDate(parseDate(new MRZSelectionCriteria(21, 6, 1, FILLER_CHARACTER)));
			passport.setThirdCheckDigit(parseString(new MRZSelectionCriteria(27, 1, 1, FILLER_CHARACTER)));
			passport.setPersonalNumber(parseString(new MRZSelectionCriteria(28, 14, 1, FILLER_CHARACTER)));
			passport.setFourthCheckDigit(parseString(new MRZSelectionCriteria(42, 1, 1, FILLER_CHARACTER)));
			passport.setFifthCheckDigit(parseString(new MRZSelectionCriteria(43, 1, 1, FILLER_CHARACTER)));
		} catch (Exception e) {
			throw new MRZException(e, mrzString, format);
		}
		return passport;
	}

	private Date parseDate(MRZSelectionCriteria criteria) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("YYMMDD");
		if (format.getDateFormat() != null) {
			dateFormat = new SimpleDateFormat(format.getDateFormat());
			return dateFormat.parse(parseString(criteria));
		}
		return dateFormat.parse(parseString(criteria));
	}

	private String parseCountry(MRZSelectionCriteria criteria) {
		String iso3Alph = parseString(criteria);
		String country = getCountryName(iso3Alph);
		if (country == null) {
			return iso3Alph;
		}
		return country;
	}

	private String parseString(MRZSelectionCriteria criteria) {
		StringBuilder sb = new StringBuilder();
		int position = criteria.fromIndex;
		String row = rows[criteria.rowIndex];
		while (position < criteria.toIndex) {
			if (row.charAt(position) == '<') {
				sb.append(criteria.fillerCharacter);
				position++;
			} else {
				sb.append(row.charAt(position));
				position++;
			}
		}
		return sb.toString();
	}

	private String getCountryName(String isoAlph3Code) {
		for (Locale locale : localesList) {
			if (locale.getISO3Country().equalsIgnoreCase(isoAlph3Code)) {
				return locale.getDisplayCountry();
			}
		}
		return null;
	}

	private String[] parseNames(MRZSelectionCriteria criteria) {
		String row = rows[criteria.rowIndex];
		String nameString = row.substring(criteria.fromIndex, criteria.toIndex);
		while (nameString.endsWith("<")) {
			nameString = nameString.substring(0, nameString.length() - 1);
		}
		String surnameString = nameString.split("<<")[0];
		String givenNamesString = nameString.split("<<")[1];
		String surname = parseString(
				new MRZSelectionCriteria(criteria.fromIndex, surnameString.length(), 0, FILLER_CHARACTER));
		String givenNames = parseString(new MRZSelectionCriteria(criteria.fromIndex + surnameString.length() + 2,
				givenNamesString.length(), 0, FILLER_CHARACTER));
		return new String[] { surname, givenNames };
	}

	public static void main(String args[]) {
		try {
			String mrzInfoString = "P<TWNLIN<<CHIEN<SHENG<<<<<<<<<<<<<<<<<<<<<<<\r\nM000004264 TWN 6803029M9801048 F122187664<<<<66";
			String resversedString = "P<INDRAICAISHWARYAccccccccccccccccccccc<<<\r\nF7802033<OIND7311017F1605011<<<<<<<<<<<<<<<4";
			String garbageString = "alsdjflasdjflasdjflasdj";
			MRZParser parser = new MRZParser(mrzInfoString);
			MRZPassport passport = parser.getMRZPassport();
			System.out.println(passport.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
