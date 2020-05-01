
import java.io.File;
import java.io.IOException;

/**
 * Scan filess for byte patterns and provide result to be displayed
 * @author Prashanna Nepal
 *
 */
public class ByteScanner {
	public FileScanner s;
	public Pattern add;

	/**
	 *Formatted result of the scanning process
	 */
	public static String scanResult = "";
	
	/**
	 * Can not be instantiated 
	 */
	private ByteScanner() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Scan file's bytes for byte patterns
	 * @param pathName a path of a file to be scanned
	 * @return the result to be displayed for the user
	 * @throws IOException if the FileScaning failed
	 */
	public static String scanFileFolder(String pathName) throws IOException {
		File fileFolder = new File(pathName);
		if(fileFolder.isFile()) {
			FileScanner fs = new FileScanner(fileFolder);
			scanResult = fs.fileResult;
		}else if (fileFolder.isDirectory()) {
			scanResult += "Directory: " + fileFolder.getName() +"\r\n";
			File[] files = fileFolder.listFiles();
			for (File file : files) {
				if(file.isFile()) {
					FileScanner fs = new FileScanner(file);
					scanResult += fs.fileResult;
				}
			}
		}
		return scanResult;
	}

}

