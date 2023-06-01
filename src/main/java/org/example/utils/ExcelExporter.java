package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private String[] columnNames;
	private ArrayList<String[]> data;
	private String sheetName = "";

	public ExcelExporter(String[] columnNames, ArrayList<String[]> data, String sheetName) {
		this.columnNames = columnNames;
		this.data = data;
		this.sheetName = sheetName;
		workbook = new XSSFWorkbook();

	}

	public ExcelExporter(String sheetName) {
		this.sheetName = sheetName;
		workbook = new XSSFWorkbook();

	}
	/**
	 * Imports data from an excel file
	 *
	 * @param pFileName the file name
	 * @return the data of the excel file as a list of list  (each line is a list of objects)
	 */
	public List<ArrayList<Object>> readFromExcel(String pFileName, int pSheet) throws  IOException {

		List<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();


		try {
			FileInputStream excelFile = new FileInputStream(new File(pFileName));
			workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(pSheet);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				ArrayList<Object> rowValues = new ArrayList<Object>();

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();

					if (currentCell.getCellType() == CellType.STRING) {

						rowValues.add(currentCell.getStringCellValue());

					} else if (currentCell.getCellType() == CellType.NUMERIC) {
						rowValues.add(currentCell.getNumericCellValue());
					}

				}

				data.add(rowValues);

			}
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}


		return data;

	}

	public static void main(String[] args) throws Exception{

		/*//Création d'un fichier excel
		ExcelExporter ex = new ExcelExporter(new String[]{"nom","prenom"},new String[][]{{"boudaa","Tarik"},{"Elyousfi","Mohamed"}}, "liste");
		ex.export("C:\\Users\\Microsoft\\Desktop\\test2023.xlsx");

		//lecture du fichier excel, chaque ligne est présentée sous forme de ArrayList<Object>
		//donc pour présenter toutes les lignes on peut utilisr une liste de liste, soit : List<ArrayList<Object>>
		List<ArrayList<Object>> data =    ex.readFromExcel("C:\\Users\\Microsoft\\Desktop\\test2023.xlsx", 0);

		for(int i =0; i< data.size(); i++){
			for(int j=0; j< data.get(i).size(); j++){
				System.out.print(data.get(i).get(j)+" | ");
			}
			System.out.println();
		}*/

	}
	public void export(String fileName) throws IOException {
		writeHeaderLine();
		writeDataLines();

		OutputStream outputStream = new FileOutputStream(fileName);
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet(sheetName);

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		int i = 0;
		for (String it : columnNames) {
			createCell(row, (i++), it, style);
		}

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (int i = 0; i < data.size(); i++) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			for (int j = 0; j < data.get(i).length; j++) {
				createCell(row, columnCount++, data.get(i)[j], style);
			}
		}

	}


}
