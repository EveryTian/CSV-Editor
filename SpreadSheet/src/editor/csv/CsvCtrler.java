package editor.csv;

import java.io.IOException;
import java.util.List;

public class CsvCtrler { // Control a CSV file with read and write method from CsvWrapper class.
	
	private String fileName;
	
	public CsvCtrler(String fileName) {
		this.fileName = fileName;
	}
	
	public CsvInfo read() throws IOException {
		CsvWrapper csvWrapper = new CsvWrapper(fileName, CsvWrapper.OperationType.READ);
		List<String[]> csv = csvWrapper.read();
		csvWrapper.close();
		return new CsvInfo(csv);
	}
	
	public void write(List<String[]> csv) throws IOException {
		CsvWrapper csvWrapper = new CsvWrapper(fileName, CsvWrapper.OperationType.WRITE);
		csvWrapper.write(csv);
		csvWrapper.close();
	}
	
	public void write(CsvInfo csv) throws IOException {
		write(csv.getCsvContent());
	}
	
	public static CsvInfo readCsvFile(String fileName) throws IOException {
		return new CsvCtrler(fileName).read();
	}
	
	public static void writeCsvFile(String fileName, List<String[]> csv) throws IOException {
		new CsvCtrler(fileName).write(csv);
	}
	
	public static void writeCsvFile(String fileName, CsvInfo csv) throws IOException {
		new CsvCtrler(fileName).write(csv);
	}

}
