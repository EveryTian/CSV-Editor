package editor.csv;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CsvWrapper { // Wrap the CSVReader and CSVWriter class from opencsv.
	
	public enum OperationType {
		READ, WRITE
	}
	
	private String fileName;
	OperationType operationType;
	CSVReader csvReader;
	CSVWriter csvWriter;
	
	public CsvWrapper(String fileName, OperationType operationType) throws IOException {
		this.fileName = fileName;
		this.operationType = operationType;
		switch (this.operationType) {
		case READ:
			csvReader = new CSVReader(new FileReader(this.fileName));
			break;
		case WRITE:
			csvWriter = new CSVWriter(new FileWriter(this.fileName));
			break;
		}
	}
	
	List<String[]> read() throws IOException {
		if (csvReader == null) {
			return null;
		}
		return csvReader.readAll();
	}
	
	void write(List<String[]> csv) {
		if (csvWriter == null) {
			return;
		}
		csvWriter.writeAll(csv);
	}
	
	public void close() throws IOException {
		if (csvReader != null) {
			csvReader.close();
		}
		if (csvWriter != null) {
			csvWriter.close();
		}
	}
}
