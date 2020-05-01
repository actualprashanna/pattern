
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

/**
 * Read file and store byte patterns to scan in a file
 * 
 * @author Prashanna Nepal
 */
public class Pattern {
	


/**
* valid Byte patterns from pattern-file to scan in a file
*/
public static List<byte[]> scanBytes = new ArrayList<byte[]>(
Arrays.asList(DatatypeConverter.parseHexBinary("414243"),DatatypeConverter.parseHexBinary("58595A")));

/**
* invalid rows from pattern-file
*/
public static List<String> invalidRow = new ArrayList<String>(Arrays.asList("123RSADNXJ", "12 er s d sd"));

private Pattern() {
	
}

/**
* Read and save patterns from a pattern file
* @param patternFilePath the file path of pattern file
* @throws IOException if the file could be read form the given path
*/
public static void readPatternFile(String patternLocation) throws IOException {
	BufferedReader reader;
	reader = new BufferedReader(new FileReader(patternLocation));
	String row = reader.readLine();
		while (row != null) {
		row = row.toUpperCase();
		if (checkLine(row)) {
			row = row.replace(" ", "");
			scanBytes.add(DatatypeConverter.parseHexBinary(row));
			} else {
			invalidRow.add(row);
			}
			// read next row
			row = reader.readLine();
		}
		reader.close();
	}

/**
* Validate if the row contains paired hex digits separate by space 
* @param row the row to be validated
* @return if the row is valid or not
*/
private static boolean checkLine(String row) {
	row = row.trim();
	if (!checkPairedHex(row))
		return false;

	if (!checkHex(row))
		return false;
		
	if (row.length() < 2)
		return false;
		
		return true;
}

/**
* Check if all the characters are hex digits
* 
* @param row the row to be checked
* @return the boo if the row passes the test
*/
private static boolean checkHex(String row) {
	for (char hexaDecimalCharacter : row.toCharArray()) {
		if (!Character.toString(hexaDecimalCharacter).matches("\\d|[A-F]| ")) {
			return false;
			}
		}
		return true;
	}

/**
* Check if the row contains only two digits separated by space
* 
* @param row the row to be checked
* @return the boo if the row passes
*/
private static boolean checkPairedHex(String row) {
	String[] spliter = row.split(" ");
	for (int i = 0; i < spliter.length; i++) {
		if (spliter[i].length() > 2)
		return false;
		}
		return true;
	}

/**
* Returns the stored patterns in a format to display in the screen
* 
* @return the array of string in format of two digits and space
*/
public static String[] getSpacedPatternStrings() {
	String[] output = new String[scanBytes.size()];
	for (int i = 0; i < output.length; i++) {
		output[i] = DatatypeConverter.printHexBinary(scanBytes.get(i)).replaceAll("..(?!$)", "$0 ");
		}
		return output;
	}

}