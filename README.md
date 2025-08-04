# Machine Readable Passport Utility

A Java utility library for extracting and parsing passport information from Machine Readable Zone (MRZ) data and passport images using Google Cloud Vision API.

## Features

- **MRZ Parsing**: Parse Machine Readable Zone strings from passports according to ICAO standards
- **Image Text Extraction**: Extract text from passport images using Google Cloud Vision API
- **Data Validation**: Built-in check digit validation for passport data integrity
- **Privacy Protection**: Mask sensitive information in passport images
- **Structured Data Output**: Convert raw MRZ data into structured passport objects

## Supported Data Fields

The library extracts the following passport information:

### Personal Information
- Surname and Given Names
- Date of Birth
- Gender (M/F/Unspecified)
- Nationality

### Document Information
- Passport Type
- Passport Number
- Issuing Country
- Expiration Date
- Personal Number (optional)

### Security Features
- Multiple check digits for data validation
- MRZ format validation

## Prerequisites

- Java 8 or higher
- Maven for dependency management
- Google Cloud Vision API credentials (for image processing)

## Dependencies

- Google Cloud Vision API (v1.14.0)
- Apache Commons IO (v2.4)
- JUnit (v3.8.1) for testing

## Installation

1. Clone the repository:
```bash
git clone https://github.com/usmananwar/MachineReadablePassportUtil.git
cd MachineReadablePassportUtil
```

2. Build the project using Maven:
```bash
mvn clean compile
```

3. For Google Vision API functionality, set up your credentials:
   - Create a Google Cloud project and enable the Vision API
   - Download the service account key file
   - Set the environment variable:
```bash
export GOOGLE_APPLICATION_CREDENTIALS="path/to/your/credentials.json"
```

## Usage

### Basic MRZ Parsing

```java
import machine.readable.zone.parser.MRZParser;
import machine.readable.zone.parser.MRZPassport;

// MRZ string from passport (2 lines, 44 characters each)
String mrzString = "P<TWNLIN<<CHIEN<SHENG<<<<<<<<<<<<<<<<<<<<<<<\r\n" +
                   "M000004264TWN6803029M9801048F122187664<<<<66";

try {
    MRZParser parser = new MRZParser(mrzString);
    MRZPassport passport = parser.getMRZPassport();
    
    System.out.println("Name: " + passport.getGivenNames() + " " + passport.getSurname());
    System.out.println("Passport Number: " + passport.getPassportNumber());
    System.out.println("Nationality: " + passport.getNationality());
    System.out.println("Date of Birth: " + passport.getDob());
    System.out.println("Expiration Date: " + passport.getExpirationDate());
} catch (Exception e) {
    e.printStackTrace();
}
```

### Image Text Extraction

```java
import text.detection.google.PassportTextExtraction;

// Process passport image and extract MRZ data
String imagePath = "path/to/passport/image.jpg";
String outputPath = "path/to/masked/output.jpg";

// This will extract text, detect MRZ, parse passport data, and create a masked image
PassportTextExtraction.main(new String[]{imagePath, outputPath});
```

### Pattern Matching Utilities

```java
import text.detection.utils.PatternMatcher;

String text = "some text containing MRZ data";

// Check if text contains MRZ pattern
boolean isMRZ = PatternMatcher.isMRZdetected(text);

// Check if text looks like a passport number
boolean isPassportNumber = PatternMatcher.isPassportNo(text);
```

## Project Structure

```
src/
├── main/java/
│   ├── machine/readable/zone/parser/     # Core MRZ parsing functionality
│   │   ├── MRZParser.java               # Main MRZ parser
│   │   ├── MRZPassport.java             # Passport data model
│   │   ├── MRZFormat.java               # MRZ format specifications
│   │   ├── MRZBaseDocument.java         # Base document class
│   │   ├── MRZException.java            # Custom exceptions
│   │   └── MRZSelectionCriteria.java    # Data extraction criteria
│   ├── sample/                          # Example implementations
│   │   ├── CustomMRZParser.java
│   │   └── PassportMRZParser.java
│   └── text/detection/                  # Image processing and text detection
│       ├── google/                      # Google Vision API integration
│       │   ├── PassportTextExtraction.java
│       │   ├── DrivingLicenseTextExtraction.java
│       │   └── components/              # Custom annotation components
│       └── utils/                       # Utility classes
│           ├── ImageMaskingUtil.java
│           └── PatternMatcher.java
```

## MRZ Format Support

Currently supports the standard passport MRZ format:
- **Type**: TD-3 (Passport)
- **Structure**: 2 lines, 44 characters each
- **Standard**: ICAO Document 9303

### Example MRZ Format:
```
Line 1: P<COUNTRY_CODE<SURNAME<<GIVEN_NAMES<<<<<<<<<<<<<<<
Line 2: PASSPORT_NO<CHECK_DIGIT<NATIONALITY<DOB<CHECK_DIGIT<GENDER<EXPIRY<CHECK_DIGIT<PERSONAL_NO<CHECK_DIGIT<CHECK_DIGIT
```

## Error Handling

The library includes comprehensive error handling:

- `MRZException`: Custom exception for MRZ parsing errors
- Format validation for MRZ strings
- Check digit validation
- Image processing error handling

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## License

This project is open source. Please check the license file for more details.

## Notes

- Ensure your Google Cloud Vision API credentials are properly configured for image processing features
- The library is designed primarily for passport MRZ parsing but includes utilities for other document types
- Image masking functionality helps protect sensitive information in processed images

## Support

For issues, questions, or contributions, please open an issue on GitHub.

---

**Disclaimer**: This utility is for educational and development purposes. Ensure compliance with applicable privacy laws and regulations when processing passport data.
