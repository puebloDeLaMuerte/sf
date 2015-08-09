package smimport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import SMUtils.Lang;

public class SM_EXCELReader {

	private JPanel										pane;
	private JFileChooser								chooser;

	private POIFSFileSystem 							fs;
	private HSSFWorkbook 								wb;
	private HSSFSheet 									sheet;

	private ArrayList<LinkedHashMap<String, String>> 	lines;


	public SM_EXCELReader() {


		pane = new JPanel();
		chooser = new JFileChooser(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Microsoft Excel Files", "xls");
		chooser.setFileFilter(filter);
	}

	public File loadExcelData() {

		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogTitle(Lang.whereIsExcelFile);
		int returnVal = chooser.showOpenDialog( pane );
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " +
					chooser.getSelectedFile().getName());
		} else return null;

		FileInputStream stream = null;
		try {
			stream = new FileInputStream(chooser.getSelectedFile());

			fs = new POIFSFileSystem(stream);
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);

		} catch( Exception e) {
			System.err.println(e);
		}

		int i = sheet.getFirstRowNum();
		int z = sheet.getLastRowNum();

		System.out.println();
		System.out.println("the rows in this sheet go from " + i + " to " + z);
		System.out.println();

//		ArrayList<LinkedHashMap<String, String>> tempLines;

		lines = new ArrayList<LinkedHashMap<String, String>>();

		for( int r = 1; r<z; r++){

			HSSFRow theRow = sheet.getRow(r);

			if(theRow != null) {

				LinkedHashMap<String, String> ln = new LinkedHashMap<String, String>();

				// validate row
				
				boolean validRow = false;
				
				// test if the invNr field contains more than nothing (empty string)
				
				Cell testCell = theRow.getCell(0);
				String testString = null;
				if( testCell != null) {
					testString = testCell.getStringCellValue();
					if( !testString.isEmpty() ) validRow = true;
				}
				System.out.println("this string: "+testString+" evaluated to "+validRow);
				
				// transfer cell data to "lines" (internal data storage)
				
				if (validRow) {
					for (int j = 0; j < 7; j++) {

						Cell cell = theRow.getCell(j);

						if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {

							switch (j) {
							case 0:
								ln.put("inv-Nr", theRow.getCell(0)
										.getStringCellValue());
								break;
							case 1:
								ln.put("artist", theRow.getCell(1)
										.getStringCellValue());
								break;
							case 2:
								ln.put("title", theRow.getCell(2)
										.getStringCellValue());
								break;
							case 3:
								ln.put("imageSize", theRow.getCell(3)
										.getStringCellValue());
								break;
							case 4:
								ln.put("frameSize", theRow.getCell(4)
										.getStringCellValue());
								break;
							case 5:
								ln.put("passepartoutSize", theRow.getCell(5)
										.getStringCellValue());
								break;
							case 6:
								ln.put("fileName", theRow.getCell(6)
										.getStringCellValue());
								break;
							}
						}
					}
					lines.add(ln);
				}
			}
		}
		return chooser.getSelectedFile().getParentFile();
	}

	public String getInvNr(int _idx) {
		return lines.get(_idx).get("inv-Nr").trim();
	}
	
	public String getArtist(int _idx) {
		return lines.get(_idx).get("artist").trim();
	}
	
	public String getTitle(int _idx) {
		return lines.get(_idx).get("title").trim();
	}
	
	public int[] getSizeValues(int _idx, String _what) {
		
		String nb = lines.get(_idx).get(_what);
		
		if( nb != null ) {
			int[] size = cm_StringTo_mm_Float(nb);
			return size;
		} else {
			return null;
		}
	}

	private int[] cm_StringTo_mm_Float(String _in) {

		String[] nbs = _in.split("\\s+\\D*\\s+");
		Float[] nf  = new Float[2];
		int[]	nfI = new int[2];
		
		if( nbs != null && nbs.length == 2 ) {

			nbs[0] = nbs[0].replace(',', '.');
			nbs[1] = nbs[1].replace(',', '.');
			nf[0] = Float.parseFloat(nbs[0]);
			nf[1] = Float.parseFloat(nbs[1]);
			nf[0] *= 10;
			nf[1] *= 10;
			nfI[0] = nf[0].intValue();
			nfI[1] = nf[1].intValue();
		}
		return nfI;
	}
	
	public int getExcelLength() {

		if( lines != null && lines.size() > 0) {
			return lines.size();
		}
		else return -1;
	}



}
