import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestDemo02 {
    public static void main(String[] args) throws Exception {
        FileInputStream file = new FileInputStream("C:\\Users\\Meow\\Desktop\\saas.xlsx");
        if (file != null) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int i = 2;
            XSSFRow nRow = null;
            while ((nRow = sheet.getRow(i++)) != null) {
                for (int j = 1; j < 6; j++) {
                    XSSFCell cell = nRow.getCell(j);
                    CellType cellType = cell.getCellType();
                    System.out.println(cellType.name());
                    String str = cell.getStringCellValue();
                    System.out.println(str);
                }
            }
        }
    }
}
