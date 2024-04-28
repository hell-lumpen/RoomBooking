package org.mai.roombooking.services.Shedule;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mai.roombooking.entities.Group;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Сервис для прасинга Excel файлов данных о группах, выгруженных из ИАСУ
 */

@Slf4j
public class ExcelParser {
    FileInputStream fileInputStream;
    Workbook workbook;

    public List<Group> parseGroupFile(@NonNull String pathFile) {
        if (!openFile(pathFile))
            return new ArrayList<>();

        List<Group> groups = parseFile();
        log.info("Groups parsed from local file: path = " + pathFile);

        return groups;
    }

    public List<Group> parseGroupFile(@NonNull MultipartFile file) {
        if (!openFile(file))
            return new ArrayList<>();
        else
            return parseFile();
    }

    private boolean openFile(@NonNull String fileName) {
        try {
            fileInputStream = new FileInputStream(fileName);
            workbook = new XSSFWorkbook(fileInputStream);
            return true;
        } catch (IOException e) {
            log.error("Get groups error on open file: path = " + fileName + "/n"
                    + e.getMessage() + "/n"
                    + Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    private boolean openFile(@NonNull MultipartFile file) {
        try {
            fileInputStream = (FileInputStream) file;
            workbook = new XSSFWorkbook(fileInputStream);
            return true;
        } catch (IOException e) {
            log.error("Get groups error on open file: MultipartFile = " + file.getName() + "/n"
                    + e.getMessage() + "/n"
                    + Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    private @NonNull List<Group> parseFile() {
        int startRow = 14;
        int groupNameCell = 0;
        int specialtyCell = 3;
        int groupSizeCell = 4;

        List<Group> res = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);
        int currentRowInd = startRow;
        Row currentRow = sheet.getRow(currentRowInd);

        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);


        while (!currentRow.getCell(0).toString().equals("Итого")) {
            if (!pattern.matcher(currentRow.getCell(0).toString()).matches() &&
                    !currentRow.getCell(groupSizeCell).toString().equals("#NULL!"))
                res.add(Group.builder()
                        .name(currentRow.getCell(groupNameCell).toString())
                        .specialty(currentRow.getCell(specialtyCell).toString())
                        .size((int) Math.round(Double.parseDouble(currentRow.getCell(groupSizeCell).toString())))
                        .build());
            currentRow = sheet.getRow(++currentRowInd);
        }


        try {
            fileInputStream.close();
            workbook.close();
        } catch (IOException ex) {
            System.err.println("Error in closing File");
        }

        return res;
    }
}