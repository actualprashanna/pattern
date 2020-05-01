import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;
class test {
	
	@Test
	public void testFileReader() throws Exception{
		Pattern.readPatternFile(new File("src\\test\\pattern.txt").getPath());
		assertTrue(Pattern.scanBytes.size() > 0);
	}
	
	@Test
	public void testprintPattern() {
		String testPattern = new String("41 42 43");
		assertEquals(testPattern, Pattern.getSpacedPatternStrings()[0]);
	}

}
