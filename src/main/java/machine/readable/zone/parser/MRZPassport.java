package machine.readable.zone.parser;

import java.util.Date;

public class MRZPassport extends MRZBaseDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// first row
	String passportType; // P, indicating a passport
	String type;// Type (for countries that distinguish between different types of passports)
	String issuingCountry; // Issuing country or organization (ISO 3166-1 alpha-3 code with modifications)
	String surname;// Surname, followed by two filler characters, followed by given names. Given
					// names are separated by single filler characters
	String givenNames;
	// second row
	String passportNumber; // Passport number
	String firstCheckDigit; // Check digit over digits 1–9
	String nationality; // Nationality (ISO 3166-1 alpha-3 code with modifications)
	Date dob; // Date of birth (YYMMDD)
	String secondCheckDigit;// Check digit over digits 14–19
	String gender; // Sex (M, F or < for male, female or unspecified)
	Date expirationDate; // Expiration date of passport (YYMMDD)
	String thirdCheckDigit;// Check digit over digits 22–27
	String personalNumber; // Personal number (may be used by the issuing country as it desires)
	String fourthCheckDigit; // Check digit over digits 29–42 (may be < if all characters are <)
	String fifthCheckDigit; // Check digit over digits 1–10, 14–20, and 22–43

	protected MRZPassport() {
		super(MRZFormat.PASSPORT);
	}

	public String getPassportType() {
		return passportType;
	}

	public void setPassportType(String passportType) {
		this.passportType = passportType;
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

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getFirstCheckDigit() {
		return firstCheckDigit;
	}

	public void setFirstCheckDigit(String firstCheckDigit) {
		this.firstCheckDigit = firstCheckDigit;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getSecondCheckDigit() {
		return secondCheckDigit;
	}

	public void setSecondCheckDigit(String secondCheckDigit) {
		this.secondCheckDigit = secondCheckDigit;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getThirdCheckDigit() {
		return thirdCheckDigit;
	}

	public void setThirdCheckDigit(String thirdCheckDigit) {
		this.thirdCheckDigit = thirdCheckDigit;
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

	@Override
	public String toString() {
		return "MRZPassport [type=" + type + ", issuingCountry=" + issuingCountry + ", surname=" + surname
				+ ", givenNames=" + givenNames + ", passportNumber=" + passportNumber + ", firstCheckDigit="
				+ firstCheckDigit + ", nationality=" + nationality + ", dateOfBirth=" + dob + ", secondCheckDigit="
				+ secondCheckDigit + ", gender=" + gender + ", expirationDate=" + expirationDate + ", thirdCheckDigit="
				+ thirdCheckDigit + ", fourthCheckDigit=" + fourthCheckDigit + "]";

	}

}
