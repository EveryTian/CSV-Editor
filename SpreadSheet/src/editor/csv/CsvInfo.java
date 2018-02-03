package editor.csv;

import java.util.List;

public class CsvInfo { // A CSV table with length properties.
	
	private List<String[]> csvContent;
	private int rowNum = 0;
	private int colNum = 0;
	
	public CsvInfo(List<String[]> csvContent) {
		this.csvContent = csvContent;
		if (this.csvContent == null) {
			return;
		}
		rowNum = this.csvContent.size(); // Set the properties.
		for (String[] csvRow : this.csvContent) {
			int csvRowLength = csvRow.length;
			if (csvRowLength > colNum) {
				colNum = csvRowLength;
			}
			for (int i = 0; i < csvRowLength; ++i) {
				if (csvRow[i] == null) {
					csvRow[i] = ""; // Normalize the CSV cell element.
				}
			}
		}
	}
	
	public List<String[]> getCsvContent() {
		return csvContent;
	}
	
	public int getRowNum() {
		return rowNum;
	}
	
	public int getColNum() {
		return colNum;
	}
	
}
