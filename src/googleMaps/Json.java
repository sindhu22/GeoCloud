import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.json.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
 
public class Json
{
    
    public String convert() throws Exception
    {
        //
        // Creating an instance of HSSFWorkbook.
        //
        HSSFWorkbook workbook = new HSSFWorkbook();
 
        //
        // Create two sheets in the excel document and name it First Sheet and
        // Second Sheet.
        //
        HSSFSheet firstSheet = workbook.createSheet("FIRST SHEET");
        // HSSFSheet secondSheet = workbook.createSheet("SECOND SHEET");
 
        //
        // Manipulate the firs sheet by creating an HSSFRow wich represent a
        // single row in excel sheet, the first row started from 0 index. After
        // the row is created we create a HSSFCell in this first cell of the row
        // and set the cell value with an instance of HSSFRichTextString
        // containing the words FIRST SHEET.
        //
        HSSFRow rowA = firstSheet.createRow(0);
        HSSFCell cellA = rowA.createCell(0);
        cellA.setCellValue(new HSSFRichTextString("FIRST SHEET"));
 
        // HSSFRow rowB = secondSheet.createRow(0);
        // HSSFCell cellB = rowB.createCell(0);
        // cellB.setCellValue(new HSSFRichTextString("SECOND SHEET"));
 
        //
        // To write out the workbook into a file we need to create an output
        // stream where the workbook content will be written to.
        //
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(new File("sheet.xls"));
            workbook.write(fos);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.flush();
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        FileInputStream inp = new FileInputStream( "sheet.xls" );
        Workbook work = WorkbookFactory.create( inp );

        // Get the first Sheet.
        Sheet sheet = work.getSheetAt( 0 );

        // Start constructing JSON.
        JSONObject json = new JSONObject();

        // Iterate through the rows.
        JSONArray rows = new JSONArray();
        for ( Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); )
        {
            Row row = rowsIT.next();
            JSONObject jRow = new JSONObject();

            // Iterate through the cells.
            JSONArray cells = new JSONArray();
            for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
            {
                Cell cell = cellsIT.next();
                cells.put( cell.getStringCellValue() );
            }

            jRow.put( "cell", cells );
            rows.put( jRow );
        }

    // Create the JSON.
    json.put( "rows", rows );

    // Get the JSON text.
    // System.out.println(json.toString());
    return json.toString();


    }
}