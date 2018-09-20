package sample;

public class PassportMRZParser {

	// first row
	boolean passport; // P, indicating a passport
	String type;// Type (for countries that distinguish between different types of passports)
	String issuingCountry; // Issuing country or organization (ISO 3166-1 alpha-3 code with modifications)
	String surname;// Surname, followed by two filler characters, followed by given names. Given
					// names are separated by single filler characters
	String givenNames;

	// second row
	String passportNumber; // Passport number
	String firstCheckDigit; // Check digit over digits 1–9
	String nationality; // Nationality (ISO 3166-1 alpha-3 code with modifications)
	String dob; // Date of birth (YYMMDD)
	String secondCheckDigit;// Check digit over digits 14–19
	String gender; // Sex (M, F or < for male, female or unspecified)
	String expirationDate; // Expiration date of passport (YYMMDD)
	String thirdCheckDigit;// Check digit over digits 22–27
	String personalNumber; // Personal number (may be used by the issuing country as it desires)
	String fourthCheckDigit; // Check digit over digits 29–42 (may be < if all characters are <)
	String fifthCheckDigit; // Check digit over digits 1–10, 14–20, and 22–43

	int rowPosition = 0;

	private static String NAME_SEPARATOR = "-";
	private static String PASSPORT_FILLER = "";
	private static String GENDER_FILLER = "O";
	private static String PERSONAL_NO_FILLER = ";";

	public PassportMRZParser(String mrzInfoString) throws Exception {
		try {
			String first = mrzInfoString.split(System.lineSeparator())[0];
			String second = mrzInfoString.split(System.lineSeparator())[1];

			first = first.replaceAll("\\s", "");
			second = second.replaceAll("\\s", "");

			if (first.charAt(0) == 'P' && first.charAt(1) == '<') {
				parseMRZfirstRow(first);
				parseMRZsecondRow(second);
			} else {
				parseMRZfirstRow(second);
				parseMRZsecondRow(first);
			}

		} catch (Exception e) {
			throw new Exception("PassportMRZ exception", e);
		}
	}

	public static void main(String args[]) throws Exception {
		String mrzInfoString = "P<TWNLIN<<CHIEN<SHENG<<<<<<<<<<<<<<<<<<<<<<<\r\nM000004264 TWN 6803029M9801048 F122187664<<<<66";
		
		String resversedString = "P<INDRAICAISHWARYAccccccccccccccccccccc<<<\r\nF7802033<OIND7311017F1605011<<<<<<<<<<<<<<<4";
		
		PassportMRZParser passportMRZ = new PassportMRZParser(resversedString);

	}

	private void parseMRZfirstRow(String firstRow) throws Exception {
		rowPosition = 0;
		if (firstRow.charAt(0) == 'P') {
			passport = true;
		}
		issuingCountry = extractCountryCode(firstRow);
		rowPosition = 5;
		surname = extractSurname(firstRow);
		rowPosition++;
		givenNames = extractGivenNames(firstRow);
	}

	private void parseMRZsecondRow(String secondRow) throws Exception {

		rowPosition = 0;
		passportNumber = extractPassportNo(secondRow);
		rowPosition = 9;// check digit
		nationality = extractNationality(secondRow);
		rowPosition++;
		dob = extractDob(secondRow);
		rowPosition = 19;// check digit
		gender = extractGender(secondRow);
		rowPosition++;
		expirationDate = extractExpirationDate(secondRow);
		rowPosition = 43;// check digit
		personalNumber = extractPersonalNumber(secondRow);

	}

	private String extractPersonalNumber(String mrString) throws Exception {
		boolean isEol = false;
		StringBuilder sb = new StringBuilder();
		int position = 28;
		while (position < 42 && !isEol) {
			if (position <= mrString.length()) {
				if (mrString.charAt(position) == '<') {
					sb.append(PERSONAL_NO_FILLER);
					position++;
				} else {
					sb.append(mrString.charAt(position));
					position++;
				}
			} else {
				isEol = true;
			}
		}
		// rowPosition = position - 1;
		return sb.toString();
	}

	private String extractExpirationDate(String mrString) throws Exception {
		boolean isEol = false;
		StringBuilder sb = new StringBuilder();
		int position = 21;
		while (position < 27 && !isEol) {
			if (position <= mrString.length()) {
				if (mrString.charAt(position) == '<') {
					throw new Exception("Error while finding Expiration Date");
				} else {
					sb.append(mrString.charAt(position));
					position++;
				}
			} else {
				isEol = true;
			}
		}
		rowPosition = position - 1;
		return sb.toString();
	}

	private String extractGender(String mrString) throws Exception {
		boolean isEol = false;
		StringBuilder sb = new StringBuilder();
		int position = 20;
		while (position < 21 && !isEol) {
			if (position <= mrString.length()) {
				if (mrString.charAt(position) == '<') {
					sb.append(GENDER_FILLER);
					position++;
				} else {
					sb.append(mrString.charAt(position));
					position++;
				}
			} else {
				isEol = true;
			}
		}
		// rowPosition = position - 1;
		return sb.toString();
	}

	private String extractDob(String mrString) throws Exception {
		boolean isEol = false;
		StringBuilder sb = new StringBuilder();
		int position = 13;
		while (position < 19 && !isEol) {
			if (position <= mrString.length()) {
				if (mrString.charAt(position) == '<') {
					throw new Exception("Error while finding D.O.B");
				} else {
					sb.append(mrString.charAt(position));
					position++;
				}
			} else {
				isEol = true;
			}
		}
		// rowPosition = position - 1;
		return sb.toString();
	}

	private String extractNationality(String mrString) throws Exception {

		boolean isEol = false;
		StringBuilder sb = new StringBuilder();
		int position = 10;
		while (position < 13 && !isEol) {
			if (position <= mrString.length()) {
				if (mrString.charAt(position) == '<') {
					throw new Exception("Error while finding Nationality");
				} else {
					sb.append(mrString.charAt(position));
					position++;
				}
			} else {
				isEol = true;
			}
		}
		// rowPosition = position - 1;
		return sb.toString();
	}

	private String extractPassportNo(String mrString) throws Exception {

		boolean isEol = false;
		StringBuilder sb = new StringBuilder();
		int position = 0;

		while (position < 9 && !isEol) {
			if (position <= mrString.length()) {
				if (mrString.charAt(position) == '<') {
					sb.append(PASSPORT_FILLER);
					position++;
				} else {
					sb.append(mrString.charAt(position));
					position++;
				}
			} else {
				isEol = true;
			}
		}
		// rowPosition = position - 1;
		return sb.toString();
	}

	private String extractGivenNames(String mrString) throws Exception {

		boolean isEol = false;
		StringBuilder sb = new StringBuilder();
		int foundCounter = 0;
		while (foundCounter < 2 && !isEol) {
			if (rowPosition <= mrString.length()) {
				if (mrString.charAt(rowPosition) == '<') {
					foundCounter++;
					rowPosition++;
				} else {
					if (foundCounter > 0) {
						foundCounter = 0;
						sb.append(NAME_SEPARATOR);
					}
					sb.append(mrString.charAt(rowPosition));
					rowPosition++;
				}
			} else {
				isEol = true;
			}
		}
		return sb.toString();
	}

	private String extractSurname(String mrString) throws Exception {
		boolean isFoundFirst = false; // <
		boolean isFoundSecond = false;// <
		boolean isEol = false;

		StringBuilder sb = new StringBuilder();

		while (!isFoundSecond && !isFoundFirst && !isEol) {
			if (rowPosition <= mrString.length()) {
				if (mrString.charAt(rowPosition) == '<') {
					isFoundFirst = true;
					rowPosition++;
					if (rowPosition <= mrString.length()) {
						if (rowPosition < mrString.length() && mrString.charAt(rowPosition) == '<') {
							isFoundSecond = true;
						} else {
							throw new Exception("Error while finding Given Names");
						}
					} else {
						isEol = true;
					}
				} else {
					sb.append(mrString.charAt(rowPosition));
					rowPosition++;
				}
			} else {
				isEol = true;
			}
		}
		return sb.toString();
	}

	private String extractCountryCode(String mrString) throws Exception {
		String result = null;
		int pos = 2;
		StringBuilder sb = new StringBuilder();
		result = sb.append(mrString.charAt(pos)).append(mrString.charAt(pos + 1)).append(mrString.charAt(pos + 2))
				.toString();
		return result;
	}

	public boolean isPassport() {
		return passport;
	}

	public void setPassport(boolean passport) {
		this.passport = passport;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIssuingCountry() {
		return issuingCountry;
	}

	public void setIssuingCountry(String issuingCountry) {
		this.issuingCountry = issuingCountry;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGivenNames() {
		return givenNames;
	}

	public void setGivenNames(String givenNames) {
		this.givenNames = givenNames;
	}

	public void setFirstCheckDigit(String firstCheckDigit) {
		this.firstCheckDigit = firstCheckDigit;
	}

	public void setSecondCheckDigit(String secondCheckDigit) {
		this.secondCheckDigit = secondCheckDigit;
	}

	public void setThirdCheckDigit(String thirdCheckDigit) {
		this.thirdCheckDigit = thirdCheckDigit;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	public String getFourthCheckDigit() {
		return fourthCheckDigit;
	}

	public void setFourthCheckDigit(String fourthCheckDigit) {
		this.fourthCheckDigit = fourthCheckDigit;
	}

	public String getFifthCheckDigit() {
		return fifthCheckDigit;
	}

	public void setFifthCheckDigit(String fifthCheckDigit) {
		this.fifthCheckDigit = fifthCheckDigit;
	}

	public String getFirstCheckDigit() {
		return firstCheckDigit;
	}

	public String getSecondCheckDigit() {
		return secondCheckDigit;
	}

	public String getThirdCheckDigit() {
		return thirdCheckDigit;
	}

}
