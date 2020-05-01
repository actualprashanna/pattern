
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * Read a selected file and provide the result in a assigned format
 * 
 * @author Prashanna Nepal
 *
 */
public class FileScanner {

	public Pattern b;

	/**
	 * Sort the Value Pairs by key in a Map
	 * 
	 * @param <K> Generic Type Key
	 * @param <V> Generic Type Value
	 * @param map The map to sort
	 * @return sorted Map by key
	 * 
	 * @see https://stackoverflow.com/questions/2864840/treemap-sort-by-value
	 * 
	 */
	static <K extends Comparable<? super K>, V> SortedSet<Map.Entry<K, V>> entriesSortedByKeys(Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = e1.getKey().compareTo(e2.getKey());
				return res != 0 ? res : 1;
			}
		});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	/**
	 * name of the file to be scanned for
	 */
	private String docName = "";

	/**
	 * bytes of the file saved as string
	 */
	private byte[] content;

	/**
	 * Result of searched content
	 */
	public Map<Integer, byte[]> searchResult = new TreeMap<Integer, byte[]>();

	/**
	 * formatted result of the scan to be stored here
	 */
	public String fileResult = "Filename: ";

	/**
	 * @param file the file to read and scan byte patterns
	 * @throws IOException if reading bytes from file did not work
	 */
	public FileScanner(File file) throws IOException {
		docName = file.getName();
		fileResult += docName + "\r\n";
		bytesFromFile(file);
		scanFile();
		fillFileResult();
	}

	private void fillFileResult() {
		boolean isPatternFound = false;
		Set<Map.Entry<Integer, byte[]>> enteries = entriesSortedByKeys(searchResult);
		if (searchResult.size() > 0) {
			for (Entry<Integer, byte[]> entry : enteries) {
				fileResult += String.format("Pattern found: %s, at offset: %d (0x%s) within the file.\r\n",
						DatatypeConverter.printHexBinary(entry.getValue()), entry.getKey(),
						Integer.toHexString(entry.getKey()));
			}
			isPatternFound = true;
		}

		if (!isPatternFound)
			fileResult = "No Patterns Found.\r\n";
	}

	/**
	 * Scan file with pattern using Pattern static properties
	 * 
	 * @see https://stackoverflow.com/questions/7194522/how-to-putall-on-java-hashmap-contents-of-one-to-another-but-not-replace-existi
	 */
	private void scanFile() {
		for (byte[] pattern : Pattern.scanBytes) {
			Map<Integer, byte[]> tmp = new HashMap<Integer, byte[]>(indexOfPattern(content, pattern));
			if (!searchResult.isEmpty())
				tmp.keySet().removeAll(searchResult.keySet());
			searchResult.putAll(tmp);
			searchResult.putAll(indexOfPattern(content, pattern));
		}
	}

	public Map<Integer, byte[]> indexOfPattern(byte[] source, byte[] pattern) {
		int sourceLength = source.length;
		int patternLength = pattern.length;

		Map<Integer, byte[]> answer = new TreeMap<>();

		int loopTo = sourceLength - patternLength;
		if (loopTo < 0) {
			return answer;
		}

		for (int j = 0; j < loopTo; j++) {
			if (source[j] == pattern[0]) {
				boolean exists = true;
				for (int i = 0; i < patternLength; i++) {
					if (source[j + i] != pattern[i]) {
						exists = false;
					}
				}
				if (exists) {
					answer.put(j, pattern);
				}
			}
		}
		return answer;
	}

	/**
	 * Read all bytes in and store it in string format
	 * @param file the file to read bytes from
	 * @throws IOException if the file could not be read
	 */
	private void bytesFromFile(File doc) throws IOException {
		content = Files.readAllBytes(doc.toPath());
	}
}