package com.vht.sms.content.htr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.vht.sms.content.db.LocalDatabasePooling;

public class ImportThanhVien implements Runnable {
	private int fileType;
	private int codeFileId = -1;

	private Workbook workbook = null;// for excel

	private Connection connection;
	private static final String INSERT_CODE = "INSERT INTO HOPTRI_CODE"
			+ "(MA_UU_DAI,KI_TU_CUOI,SERIAL,SERIAL_CODE,SERIAL_NAME,SERIAL_STT,TEN_SAN_PHAM,MA_SAN_PHAM,NGAY_HET_HAN,DON_GIA,st,diem_so,TEN_SP) "
			+ "VALUES(?,?,?,?,?,?,?,?,to_timestamp(?,'dd/MM/yyyy HH24:MI:ss'),?,999,?,?)";

	private static final String UPDATE_CODE = "UPDATE HOPTRI_CODE SET KI_TU_CUOI=?,SERIAL=?,SERIAL_CODE=?,SERIAL_NAME=?,SERIAL_STT=?,TEN_SAN_PHAM =?"+
			", MA_SAN_PHAM=?, DON_GIA = ?,st=999,diem_so=?,TEN_SP=?  WHERE MA_UU_DAI =?";
	
	private static final String UPDATE_HOP_TRI_NGUOI_DUNG = "update hoptri_nguoidung set loai_thanh_vien=?, ten_cua_hang=?,ten_dai_ly=?,"
			+ "tinh=?,khu_vuc=?,ma_khc1=?,dai_ly_cap1=? , PHAN_LOAI_DAI_LY=?, ghi_chu1=?, ghi_chu2=?, ghi_chu3=?, ghi_chu4=?, ghi_chu5=? where so_dtdd=?";

	private static final String UPDATE_HOPTRI_HTR_REPORT = "update HOPTRI_HTR_REPORT set ten_cua_hang=?,ten_dai_ly=?,"
			+ "tinh=?,khu_vuc=?,ma_khc1=?,dai_ly_cap1=? , PHAN_LOAI_DAI_LY=?, ghi_chu1=?, ghi_chu2=?, ghi_chu3=?, ghi_chu4=?, ghi_chu5=? where so_dtdd=?";
	private static final String UPDATE_HOPTRI_HTR_REPORT_TOTAL = "update HOPTRI_HTR_REPORT_TOTAL set ten_cua_hang=?,ten_dai_ly=?,"
			+ "tinh=?,khu_vuc=?,ma_khc1=?,dai_ly_cap1=? , PHAN_LOAI_DAI_LY=?, ghi_chu1=?, ghi_chu2=?, ghi_chu3=?, ghi_chu4=?, ghi_chu5=? where so_dtdd=?";

	
	private static final String INSERT_HOP_TRI_NGUOI_DUNG = "INSERT INTO hoptri_nguoidung (so_dtdd,loai_thanh_vien, ten_cua_hang,ten_dai_ly,tinh,khu_vuc" +
			",ma_khc1,dai_ly_cap1 , PHAN_LOAI_DAI_LY, ghi_chu1, ghi_chu2, ghi_chu3, ghi_chu4, ghi_chu5) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

	private static final String UPDATE_STEP = "UPDATE path_files SET steps = 2, rows = ? WHERE id = ?";
	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static String[] getObjectMessage(String Message) {
		StringTokenizer tokenizer = new StringTokenizer(Message, " ");
		String returns[] = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreElements())
			returns[i++] = tokenizer.nextToken();
		return returns;
	}

	private void updateCodeFromExcel() {
		PreparedStatement ppsInsCode = null;
		Sheet sheet = this.workbook.getSheetAt(0);

		int startRow = 1;// dong tieu de
		int lastRowNum = this.getLastRowNum(sheet, 9);
		int count = 0;
		final int batchSize = 500;
		try {
			count = 0;
			Cell maUuDai = null;
			Cell kiTuCuoi = null;
			Cell serial = null;
			Cell serialCode = null;
			Cell serialName = null;
			Cell serialSTT = null;
			Cell tenSP = null;
			Cell maSP = null;
			Cell ngayHetHan = null;
			Cell dongia = null;
			Cell diemso = null;
			Cell ten_SP = null;
			Row row = null;
			ppsInsCode = this.connection
					.prepareStatement(UPDATE_CODE);
			for (int rowIndex = startRow; rowIndex <= lastRowNum; rowIndex++) {
				row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				maUuDai = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
				maUuDai.setCellType(Cell.CELL_TYPE_STRING);

				kiTuCuoi = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
				kiTuCuoi.setCellType(Cell.CELL_TYPE_STRING);

				serial = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
				serial.setCellType(Cell.CELL_TYPE_STRING);

				serialCode = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
				serialCode.setCellType(Cell.CELL_TYPE_STRING);

				serialName = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
				serialName.setCellType(Cell.CELL_TYPE_STRING);

				serialSTT = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
				serialSTT.setCellType(Cell.CELL_TYPE_STRING);

				tenSP = row.getCell(6, Row.CREATE_NULL_AS_BLANK);
				tenSP.setCellType(Cell.CELL_TYPE_STRING);

				maSP = row.getCell(7, Row.CREATE_NULL_AS_BLANK);
				maSP.setCellType(Cell.CELL_TYPE_STRING);

				ngayHetHan = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
				ngayHetHan.setCellType(Cell.CELL_TYPE_STRING);

				dongia = row.getCell(9, Row.CREATE_NULL_AS_BLANK);
				dongia.setCellType(Cell.CELL_TYPE_STRING);

				diemso = row.getCell(10, Row.CREATE_NULL_AS_BLANK);
				diemso.setCellType(Cell.CELL_TYPE_STRING);

				ten_SP = row.getCell(11, Row.CREATE_NULL_AS_BLANK);
				ten_SP.setCellType(Cell.CELL_TYPE_STRING);

				String mud = maUuDai.getStringCellValue();
				String ktc = kiTuCuoi.getStringCellValue();
				String se = serial.getStringCellValue();
				String sc = serialCode.getStringCellValue();
				String sn = serialName.getStringCellValue();
				String stt = serialSTT.getStringCellValue();
				String tsp = tenSP.getStringCellValue();
				String msp = maSP.getStringCellValue();
				String nhh = ngayHetHan.getStringCellValue();
				String dg = dongia.getStringCellValue();
				String ds = diemso.getStringCellValue();
				String ten_sp = ten_SP.getStringCellValue();
				ppsInsCode.setString(1, ktc);
				ppsInsCode.setString(2, se);
				ppsInsCode.setString(3, sc);
				ppsInsCode.setString(4, sn);
				ppsInsCode.setLong(5, Long.parseLong(stt));
				ppsInsCode.setString(6, tsp);
				ppsInsCode.setString(7, msp);
				ppsInsCode.setLong(8, Long.parseLong(dg));
				ppsInsCode.setLong(9, Long.parseLong(ds));
				ppsInsCode.setString(10, ten_sp);
//				ppsInsCode.setString(11, nhh);
				ppsInsCode.setString(11, mud);
				System.out.println(rowIndex + "  MaUuDai:  " + mud
						+ "  ten_sp:  " + tsp + "  ten_sp:  " + ten_sp);
				ppsInsCode.addBatch();
				if (++count % batchSize == 0) {
					ppsInsCode.executeBatch();
				}
			}
			ppsInsCode.executeBatch();
			System.out.println("Done :)  ");
			if (ppsInsCode != null) {
				ppsInsCode.close();
			}
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
	}

	private void insertCodeFromExcel() {
		PreparedStatement ppsInsCode = null;
		Sheet sheet = this.workbook.getSheetAt(0);

		int startRow = 1;// dong tieu de
		int lastRowNum = this.getLastRowNum(sheet, 9);
        int count, countInsert, countUpdate = 0;
		final int batchSize = 100;
		try {
            count = countInsert = countUpdate = 0;
			Cell maUuDai = null;
			Cell kiTuCuoi = null;
			Cell serial = null;
			Cell serialCode = null;
			Cell serialName = null;
			Cell serialSTT = null;
			Cell tenSP = null;
			Cell maSP = null;
			Cell ngayHetHan = null;
			Cell dongia = null;
			Cell diemso = null;
			Cell ten_SP = null;
			Row row = null;
			ppsInsCode = this.connection
					.prepareStatement(INSERT_CODE);
			for (int rowIndex = startRow; rowIndex <= lastRowNum; rowIndex++) {
				row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				maUuDai = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
				maUuDai.setCellType(Cell.CELL_TYPE_STRING);

				kiTuCuoi = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
				kiTuCuoi.setCellType(Cell.CELL_TYPE_STRING);

				serial = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
				serial.setCellType(Cell.CELL_TYPE_STRING);

				serialCode = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
				serialCode.setCellType(Cell.CELL_TYPE_STRING);

				serialName = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
				serialName.setCellType(Cell.CELL_TYPE_STRING);

				serialSTT = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
				serialSTT.setCellType(Cell.CELL_TYPE_STRING);

				tenSP = row.getCell(6, Row.CREATE_NULL_AS_BLANK);
				tenSP.setCellType(Cell.CELL_TYPE_STRING);

				maSP = row.getCell(7, Row.CREATE_NULL_AS_BLANK);
				maSP.setCellType(Cell.CELL_TYPE_STRING);

				ngayHetHan = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
				ngayHetHan.setCellType(Cell.CELL_TYPE_STRING);

				dongia = row.getCell(9, Row.CREATE_NULL_AS_BLANK);
				dongia.setCellType(Cell.CELL_TYPE_STRING);

				diemso = row.getCell(10, Row.CREATE_NULL_AS_BLANK);
				diemso.setCellType(Cell.CELL_TYPE_STRING);

				ten_SP = row.getCell(11, Row.CREATE_NULL_AS_BLANK);
				ten_SP.setCellType(Cell.CELL_TYPE_STRING);

				String mud = maUuDai.getStringCellValue();
				String ktc = kiTuCuoi.getStringCellValue();
				String se = serial.getStringCellValue();
				String sc = serialCode.getStringCellValue();
				String sn = serialName.getStringCellValue();
				String stt = serialSTT.getStringCellValue();
				String tsp = tenSP.getStringCellValue();
				String msp = maSP.getStringCellValue();
				String nhh = ngayHetHan.getStringCellValue();
				String dg = dongia.getStringCellValue();
				String ds = diemso.getStringCellValue();
				String ten_sp = ten_SP.getStringCellValue();
				
				ppsInsCode.setString(1, mud);
				ppsInsCode.setString(2, ktc);
				ppsInsCode.setString(3, se);
				ppsInsCode.setString(4, sc);
				ppsInsCode.setString(5, sn);
				ppsInsCode.setLong(6, Long.parseLong(stt));
				ppsInsCode.setString(7, tsp);
				ppsInsCode.setString(8, msp);
				ppsInsCode.setString(9, nhh);
				ppsInsCode.setLong(10, Long.parseLong(dg));
				ppsInsCode.setLong(11, Long.parseLong(ds));
				ppsInsCode.setString(12, ten_sp);

                /*
				System.out.println(rowIndex + " Insert moi  MaUuDai:  " + mud
						+ "  ten_sp:  " + tsp + "  ten_sp:  " + ten_sp);
				ppsInsCode.addBatch();
				if (++count % batchSize == 0) {
					ppsInsCode.executeBatch();
				}
				*/

                String check = HtrUtils.checkMaUuDai(mud);
                if(check != null && check != "")
                {
                    countUpdate++;
                    System.out.println(countUpdate + " " + check  + ": existed");
                } else {
                    countInsert++;
                    System.out.println(rowIndex + " Insert moi  MaUuDai:  " + mud
                            + "  ten_sp:  " + tsp + "  ten_sp:  " + ten_sp);
                    ppsInsCode.addBatch();
                    if (++count % batchSize == 0) {
                        ppsInsCode.executeBatch();
                    }
                }
			}
			ppsInsCode.executeBatch();
			System.out.println("Done :)  ");
			if (ppsInsCode != null) {
				ppsInsCode.close();
			}
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
	}
	
	private void updateNguoiDungExcel() {
		PreparedStatement ppsInsCode = null;
		Sheet sheet = this.workbook.getSheetAt(0);

		int startRow = 1;// dong tieu de
		int lastRowNum = this.getLastRowNum(sheet, 9);
		int count = 0;
		final int batchSize = 100;
		try {
			count = 0;
			Cell soDtdd = null;
			Cell loaiTV = null;
			Cell tenCH = null;
			Cell tenDL = null;
			Cell tinh = null;
			Cell khuVuc = null;
			Cell phanLoaiDL = null;
			Cell maKHc1 = null;
			Cell daiLyC1 = null;
			Cell ghichu1 = null;
			Cell ghichu2 = null;
			Cell ghichu3 = null;
			Cell ghichu4 = null;
			Cell ghichu5 = null;
			Row row = null;
			ppsInsCode = this.connection
					.prepareStatement(UPDATE_HOP_TRI_NGUOI_DUNG);
			for (int rowIndex = startRow; rowIndex <= lastRowNum; rowIndex++) {
				row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				soDtdd = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
				soDtdd.setCellType(Cell.CELL_TYPE_STRING);

				loaiTV = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
				loaiTV.setCellType(Cell.CELL_TYPE_STRING);

				tenCH = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
				tenCH.setCellType(Cell.CELL_TYPE_STRING);

				tenDL = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
				tenDL.setCellType(Cell.CELL_TYPE_STRING);

				tinh = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
				tinh.setCellType(Cell.CELL_TYPE_STRING);

				khuVuc = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
				khuVuc.setCellType(Cell.CELL_TYPE_STRING);

				phanLoaiDL = row.getCell(6, Row.CREATE_NULL_AS_BLANK);
				phanLoaiDL.setCellType(Cell.CELL_TYPE_STRING);

				maKHc1 = row.getCell(7, Row.CREATE_NULL_AS_BLANK);
				maKHc1.setCellType(Cell.CELL_TYPE_STRING);

				daiLyC1 = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
				daiLyC1.setCellType(Cell.CELL_TYPE_STRING);

				ghichu1 = row.getCell(9, Row.CREATE_NULL_AS_BLANK);
				ghichu1.setCellType(Cell.CELL_TYPE_STRING);

				ghichu2 = row.getCell(10, Row.CREATE_NULL_AS_BLANK);
				ghichu2.setCellType(Cell.CELL_TYPE_STRING);

				ghichu3 = row.getCell(11, Row.CREATE_NULL_AS_BLANK);
				ghichu3.setCellType(Cell.CELL_TYPE_STRING);

				ghichu4 = row.getCell(12, Row.CREATE_NULL_AS_BLANK);
				ghichu4.setCellType(Cell.CELL_TYPE_STRING);

				ghichu5 = row.getCell(13, Row.CREATE_NULL_AS_BLANK);
				ghichu5.setCellType(Cell.CELL_TYPE_STRING);

				String sodtdd = soDtdd.getStringCellValue();
				String loaitv = loaiTV.getStringCellValue();
				String tench = tenCH.getStringCellValue();
				String tendl = tenDL.getStringCellValue();
				String tinH = tinh.getStringCellValue();
				String khuvuc = khuVuc.getStringCellValue();
				String phanldl = phanLoaiDL.getStringCellValue();
				String makhc1 = maKHc1.getStringCellValue();
				String dailyc1 = daiLyC1.getStringCellValue();
				String ghiChu1 = ghichu1.getStringCellValue();
				String ghiChu2 = ghichu2.getStringCellValue();
				String ghiChu3 = ghichu3.getStringCellValue();
				String ghiChu4 = ghichu4.getStringCellValue();
				String ghiChu5 = ghichu5.getStringCellValue();
				
				ppsInsCode.setString(1, loaitv.trim());
				ppsInsCode.setString(2, tench.trim());
				ppsInsCode.setString(3, tendl.trim());
				ppsInsCode.setString(4, tinH.trim());
				ppsInsCode.setString(5, khuvuc.trim());
				ppsInsCode.setString(6, makhc1.trim());
				ppsInsCode.setString(7, dailyc1.trim());
				ppsInsCode.setString(8, phanldl.trim());
				ppsInsCode.setString(9, ghiChu1.trim());
				ppsInsCode.setString(10, ghiChu2.trim());
				ppsInsCode.setString(11, ghiChu3.trim());
				ppsInsCode.setString(12, ghiChu4.trim());
				ppsInsCode.setString(13, ghiChu5.trim());
				ppsInsCode.setString(14, sodtdd.trim());

				System.out.println(rowIndex + "  sodtdd:  " + sodtdd
						+ "  tench:  " + tench + "  tendl:  " + tendl);
				ppsInsCode.addBatch();
				if (++count % batchSize == 0) {
					ppsInsCode.executeBatch();
				}
			}
			ppsInsCode.executeBatch();
			System.out.println("Done :)  ");
			if (ppsInsCode != null) {
				ppsInsCode.close();
			}
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
	}
	
	private void insertNguoiDungExcel() {
		PreparedStatement ppsInsCode = null;
		Sheet sheet = this.workbook.getSheetAt(0);

		int startRow = 1;// dong tieu de
		int lastRowNum = this.getLastRowNum(sheet, 9);
		int count, countInsert, countUpdate = 0;
		final int batchSize = 100;
		try {
			count = countInsert = countUpdate = 0;
			Cell soDtdd = null;
			Cell loaiTV = null;
			Cell tenCH = null;
			Cell tenDL = null;
			Cell tinh = null;
			Cell khuVuc = null;
			Cell phanLoaiDL = null;
			Cell maKHc1 = null;
			Cell daiLyC1 = null;
			Cell ghichu1 = null;
			Cell ghichu2 = null;
			Cell ghichu3 = null;
			Cell ghichu4 = null;
			Cell ghichu5 = null;
			Row row = null;
			ppsInsCode = this.connection
					.prepareStatement(INSERT_HOP_TRI_NGUOI_DUNG);
			for (int rowIndex = startRow; rowIndex <= lastRowNum; rowIndex++) {
				row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				soDtdd = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
				soDtdd.setCellType(Cell.CELL_TYPE_STRING);

				loaiTV = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
				loaiTV.setCellType(Cell.CELL_TYPE_STRING);

				tenCH = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
				tenCH.setCellType(Cell.CELL_TYPE_STRING);

				tenDL = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
				tenDL.setCellType(Cell.CELL_TYPE_STRING);

				tinh = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
				tinh.setCellType(Cell.CELL_TYPE_STRING);

				khuVuc = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
				khuVuc.setCellType(Cell.CELL_TYPE_STRING);

				phanLoaiDL = row.getCell(6, Row.CREATE_NULL_AS_BLANK);
				phanLoaiDL.setCellType(Cell.CELL_TYPE_STRING);

				maKHc1 = row.getCell(7, Row.CREATE_NULL_AS_BLANK);
				maKHc1.setCellType(Cell.CELL_TYPE_STRING);

				daiLyC1 = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
				daiLyC1.setCellType(Cell.CELL_TYPE_STRING);

				ghichu1 = row.getCell(9, Row.CREATE_NULL_AS_BLANK);
				ghichu1.setCellType(Cell.CELL_TYPE_STRING);

				ghichu2 = row.getCell(10, Row.CREATE_NULL_AS_BLANK);
				ghichu2.setCellType(Cell.CELL_TYPE_STRING);

				ghichu3 = row.getCell(11, Row.CREATE_NULL_AS_BLANK);
				ghichu3.setCellType(Cell.CELL_TYPE_STRING);

				ghichu4 = row.getCell(12, Row.CREATE_NULL_AS_BLANK);
				ghichu4.setCellType(Cell.CELL_TYPE_STRING);

				ghichu5 = row.getCell(13, Row.CREATE_NULL_AS_BLANK);
				ghichu5.setCellType(Cell.CELL_TYPE_STRING);

				String sodtdd = soDtdd.getStringCellValue();
				String loaitv = loaiTV.getStringCellValue();
				String tench = tenCH.getStringCellValue();
				String tendl = tenDL.getStringCellValue();
				String tinH = tinh.getStringCellValue();
				String khuvuc = khuVuc.getStringCellValue();
				String phanldl = phanLoaiDL.getStringCellValue();
				String makhc1 = maKHc1.getStringCellValue();
				String dailyc1 = daiLyC1.getStringCellValue();
				String ghiChu1 = ghichu1.getStringCellValue();
				String ghiChu2 = ghichu2.getStringCellValue();
				String ghiChu3 = ghichu3.getStringCellValue();
				String ghiChu4 = ghichu4.getStringCellValue();
				String ghiChu5 = ghichu5.getStringCellValue();
				
				ppsInsCode.setString(1, sodtdd.trim());
				ppsInsCode.setString(2, loaitv.trim());
				ppsInsCode.setString(3, tench.trim());
				ppsInsCode.setString(4, tendl.trim());
				ppsInsCode.setString(5, tinH.trim());
				ppsInsCode.setString(6, khuvuc.trim());
				ppsInsCode.setString(7, makhc1.trim());
				ppsInsCode.setString(8, dailyc1.trim());
				ppsInsCode.setString(9, phanldl.trim());
				ppsInsCode.setString(10, ghiChu1.trim());
				ppsInsCode.setString(11, ghiChu2.trim());
				ppsInsCode.setString(12, ghiChu3.trim());
				ppsInsCode.setString(13, ghiChu4.trim());
				ppsInsCode.setString(14, ghiChu5.trim());

				String check = HtrUtils.checkSDTOnNguoiDung(sodtdd);
                if(check != null && check != "")
				{
                    countUpdate++;
                    System.out.println(countUpdate + " " + check  + ": existed");
				} else {
                    countInsert++;
                    System.out.println(countInsert + " Insert NguoiDung:   sodtdd:  " + sodtdd
                            + "  tench:  " + tench + "  tendl:  " + tendl);
                    ppsInsCode.addBatch();
                    if (++count % batchSize == 0) {
                        ppsInsCode.executeBatch();
                    }
				}

			}
			ppsInsCode.executeBatch();
			System.out.println("Done :)  ");
			if (ppsInsCode != null) {
				ppsInsCode.close();
			}
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
	}

	private int getLastRowNum(Sheet sheet, int colNum) {
		int lastRowIndex = sheet.getLastRowNum();
		Row row = null;
		while (lastRowIndex > 0) {
			row = sheet.getRow(lastRowIndex);
			if (row != null) {
				boolean flag = false;
				for (int i = 0; i < colNum; i++) {
					Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
					if (cell != null) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						if (!cell.getStringCellValue().trim().equals("")) {
							flag = true;
							break;
						}
					}
				}
				if (flag) {
					break;
				}
			}
			lastRowIndex--;
		}

		return lastRowIndex;
	}
	private void updateHtrReportExcel() {
		PreparedStatement ppsInsCode = null;
		Sheet sheet = this.workbook.getSheetAt(0);

		int startRow = 1;// dong tieu de
		int lastRowNum = this.getLastRowNum(sheet, 9);
		int count = 0;
		final int batchSize = 100;
		try {
			count = 0;
			Cell soDtdd = null;
			Cell loaiTV = null;
			Cell tenCH = null;
			Cell tenDL = null;
			Cell tinh = null;
			Cell khuVuc = null;
			Cell phanLoaiDL = null;
			Cell maKHc1 = null;
			Cell daiLyC1 = null;
			Cell ghichu1 = null;
			Cell ghichu2 = null;
			Cell ghichu3 = null;
			Cell ghichu4 = null;
			Cell ghichu5 = null;
			Row row = null;
			ppsInsCode = this.connection
					.prepareStatement(UPDATE_HOPTRI_HTR_REPORT);
//			ppsInsCode = this.connection
//					.prepareStatement(UPDATE_HOPTRI_HTR_REPORT_TOTAL);
			for (int rowIndex = startRow; rowIndex <= lastRowNum; rowIndex++) {
				row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				soDtdd = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
				soDtdd.setCellType(Cell.CELL_TYPE_STRING);

				loaiTV = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
				loaiTV.setCellType(Cell.CELL_TYPE_STRING);

				tenCH = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
				tenCH.setCellType(Cell.CELL_TYPE_STRING);

				tenDL = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
				tenDL.setCellType(Cell.CELL_TYPE_STRING);

				tinh = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
				tinh.setCellType(Cell.CELL_TYPE_STRING);

				khuVuc = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
				khuVuc.setCellType(Cell.CELL_TYPE_STRING);

				phanLoaiDL = row.getCell(6, Row.CREATE_NULL_AS_BLANK);
				phanLoaiDL.setCellType(Cell.CELL_TYPE_STRING);

				maKHc1 = row.getCell(7, Row.CREATE_NULL_AS_BLANK);
				maKHc1.setCellType(Cell.CELL_TYPE_STRING);

				daiLyC1 = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
				daiLyC1.setCellType(Cell.CELL_TYPE_STRING);

				ghichu1 = row.getCell(9, Row.CREATE_NULL_AS_BLANK);
				ghichu1.setCellType(Cell.CELL_TYPE_STRING);

				ghichu2 = row.getCell(10, Row.CREATE_NULL_AS_BLANK);
				ghichu2.setCellType(Cell.CELL_TYPE_STRING);

				ghichu3 = row.getCell(11, Row.CREATE_NULL_AS_BLANK);
				ghichu3.setCellType(Cell.CELL_TYPE_STRING);

				ghichu4 = row.getCell(12, Row.CREATE_NULL_AS_BLANK);
				ghichu4.setCellType(Cell.CELL_TYPE_STRING);

				ghichu5 = row.getCell(13, Row.CREATE_NULL_AS_BLANK);
				ghichu5.setCellType(Cell.CELL_TYPE_STRING);

				String sodtdd = soDtdd.getStringCellValue();
				String loaitv = loaiTV.getStringCellValue();
				String tench = tenCH.getStringCellValue();
				String tendl = tenDL.getStringCellValue();
				String tinH = tinh.getStringCellValue();
				String khuvuc = khuVuc.getStringCellValue();
				String phanldl = phanLoaiDL.getStringCellValue();
				String makhc1 = maKHc1.getStringCellValue();
				String dailyc1 = daiLyC1.getStringCellValue();
				String ghiChu1 = ghichu1.getStringCellValue();
				String ghiChu2 = ghichu2.getStringCellValue();
				String ghiChu3 = ghichu3.getStringCellValue();
				String ghiChu4 = ghichu4.getStringCellValue();
				String ghiChu5 = ghichu5.getStringCellValue();
				ppsInsCode.setString(1, tench.trim());
				ppsInsCode.setString(2, tendl.trim());
				ppsInsCode.setString(3, tinH.trim());
				ppsInsCode.setString(4, khuvuc.trim());
				ppsInsCode.setString(5, makhc1.trim());
				ppsInsCode.setString(6, dailyc1.trim());
				ppsInsCode.setString(7, phanldl.trim());
				ppsInsCode.setString(8, ghiChu1.trim());
				ppsInsCode.setString(9, ghiChu2.trim());
				ppsInsCode.setString(10, ghiChu3.trim());
				ppsInsCode.setString(11, ghiChu4.trim());
				ppsInsCode.setString(12, ghiChu5.trim());
				ppsInsCode.setString(13, sodtdd.trim());

				System.out.println(rowIndex + " Update HTR_Report:  sodtdd:  " + sodtdd
						+ "  tench:  " + tench + "  tendl:  " + tendl);
				ppsInsCode.addBatch();
				if (++count % batchSize == 0) {
					ppsInsCode.executeBatch();
				}
			}
			ppsInsCode.executeBatch();
			System.out.println("Done :)  ");
			if (ppsInsCode != null) {
				ppsInsCode.close();
			}
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
	}
	
	public ImportThanhVien(String filePath) {

		// String dbUrl = properties.getProperty("mysql.url");
		// String dbUsr = properties.getProperty("mysql.usr");
		// String dbPwd = properties.getProperty("mysql.pwd");

		try {
			this.connection = LocalDatabasePooling.getInstance()
					.getConnection();
		} catch (SQLException e) {
			// log
		}
		// Entry.DEBUG.info("Got File " + filePath);
		// Entry.DEBUG.info("Load data to list (Java Object)...");

		try {
			if (filePath.toLowerCase().endsWith(".xls")) {
				workbook = WorkbookFactory.create(new File(filePath));
				this.fileType = 1;
			} else if (filePath.toLowerCase().endsWith(".xlsx")) {
				workbook = WorkbookFactory
						.create(new FileInputStream(filePath));
				this.fileType = 1;
			}
		} catch (InvalidFormatException e) {
			// Entry.ERROR.log(Level.SEVERE, e.getMessage(), e);
		} catch (FileNotFoundException e) {
			// Entry.ERROR.log(Level.SEVERE, e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			// Entry.ERROR.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			// Entry.ERROR.log(Level.SEVERE, e.getMessage(), e);
		}

		// Entry.DEBUG.info("Data loaded to list.");
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		int lastRowNum = 0;
		// Entry.DEBUG.info("Code Importing Proccess Start");
		int ty = this.fileType;
		if (ty == 1) {
			Sheet sheet = this.workbook.getSheetAt(0);
			lastRowNum = this.getLastRowNum(sheet, 9);
		}

		switch (this.fileType) {

		case 1:
			this.updateNguoiDungExcel();
			break;
		}
		// Entry.DEBUG.info("Importing Finished. ( ~ "
		// + (long) ((System.currentTimeMillis() - start) / 1000) + "s )");
		try {
			PreparedStatement ppsUpdate = connection
					.prepareStatement(UPDATE_STEP);
			ppsUpdate.setInt(1, lastRowNum);
			ppsUpdate.setInt(2, this.codeFileId);
			ppsUpdate.executeUpdate();
			ppsUpdate.close();
		} catch (SQLException e) {
			// Entry.ERROR.log(Level.SEVERE, e.getMessage(), e);
		}
		// Entry.DEBUG
		// .info("---------------------------------------------------------------");
	}

	public int getCodeFileId() {
		return codeFileId;
	}

	public void setCodeFileId(int codeFileId) {
		this.codeFileId = codeFileId;
	}

	private void testInsert() {
		try {
			PreparedStatement ppsInsCode = null;

			ppsInsCode = this.connection
					.prepareStatement("INSERT INTO AN (ID, DESCRIPTION) VALUES(?,?)");

			ppsInsCode.setInt(1, 2);
			ppsInsCode.setString(2, "test ne");
			ppsInsCode.addBatch();
			ppsInsCode.executeBatch();
			System.out.println("Done :)  ");

			if (ppsInsCode != null) {
				ppsInsCode.close();
			}
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		String pa = "C:\\Users\\Administrator\\Dropbox\\Tech Support\\HTR\\PLU\\PL20160513_PLU.xls";
		//String pa = "/Users/an/Dropbox/Projects/VHT/Tech Support/HTR/PLU/PL20160513_PLU.xls";
		//String pa = "/Users/an/Dropbox/Projects/VHT/Tech Support/HTR/PL234_Import.xls";

		//String pa = "C:\\Users\\Administrator\\Dropbox\\Tech Support\\HTR\\PLU\\PL20160513_PLU.xls";
		try {
			ImportThanhVien daemon = new ImportThanhVien(pa);

			//daemon.testInsert();

            //TODO: ############# UPDATE NGUOIDUNG #############
			//daemon.insertNguoiDungExcel();
			daemon.updateNguoiDungExcel();


            //TODO: #############  UPDATE PHULUC  #############
			//daemon.insertCodeFromExcel();
			//daemon.updateCodeFromExcel();

			System.out.println("Hello World!");
		} catch (Exception e) {
			// Entry.ERROR.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
		}
	}

}