package com.vht.sms.content.htr;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.vht.sms.content.db.LocalDatabasePooling;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;


public class HtrUtils {
	private static HtrProps props = HtrProps.getInstance();
	final int batchSize = 100;
	public static void getDataFromExcelHtr(int sh,String pathname, String pathCreate, int reSh) throws Exception{
		Workbook workbook = Workbook.getWorkbook(new File(pathname));
		Sheet sheet = workbook.getSheet(sh);
		int rows = sheet.getRows();

		WritableWorkbook write = Workbook.createWorkbook(new File(pathCreate));
		WritableSheet sheetWrite = write.createSheet("data_import" + reSh, reSh);
		sheetWrite.addCell(new Label(0, 0, "MA_UU_DAI"));
		sheetWrite.addCell(new Label(1, 0, "KI_TU_CUOI"));
		sheetWrite.addCell(new Label(2, 0, "SERIAL"));
		sheetWrite.addCell(new Label(3, 0, "SERIAL_CODE"));
		sheetWrite.addCell(new Label(4, 0, "SERIAL_NAME"));
		sheetWrite.addCell(new Label(5, 0, "SERIAL_STT"));
		sheetWrite.addCell(new Label(6, 0, "TEN_SAN_PHAM"));
		sheetWrite.addCell(new Label(7, 0, "MA_SAN_PHAM"));
		sheetWrite.addCell(new Label(8, 0, "NGAY_HET_HAN"));
		sheetWrite.addCell(new Label(9, 0, "DON_GIA"));
		sheetWrite.addCell(new Label(10, 0, "DIEM_SO"));
		sheetWrite.addCell(new Label(11, 0, "TEN_SP"));
		
		for (int i = 1; i < rows; i++) {
			String getExcelPin = sheet.getCell(0, i).getContents();
			String getExcelSerial = sheet.getCell(1, i).getContents();
			String getExcelMaSP = sheet.getCell(2, i).getContents();
			String getExcelTenSP = sheet.getCell(3, i).getContents();
			String maUuDai = getExcelPin.replaceAll("HTR ", "");
			String kiTuCuoi = getExcelPin.charAt(getExcelPin.length() -1) +"";
			System.out.println(i + "   " + kiTuCuoi);
			String serial = getExcelSerial;
			String serialCode = getExcelSerial.substring(0, 2);
			String serialName = getExcelSerial.substring(0, getExcelSerial.lastIndexOf("-"));
			long serialSTT = Long.parseLong(getExcelSerial.substring(getExcelSerial.lastIndexOf("-") + 1, getExcelSerial.length()));
			String tenSP = getExcelTenSP;
			String maSP = getExcelMaSP;
			String ngayHetHan="";
			String ten_SP ="";
			if(getExcelTenSP.startsWith("Hợp Trí")){
				ten_SP = getExcelTenSP.replaceAll("Hợp Trí", "Hop Tri");
			} else {
				ten_SP = ConvertUtf8.removeAccent(tenSP);
			}
//			String giaiThuong = sheet.getCell(4, i).getContents();
//			int triGia = Integer.parseInt(sheet.getCell(5, i).getContents());
			int  dongia = Integer.parseInt(props.getString("dongia."+maSP.trim()));
//			if (maSP.equalsIgnoreCase("FS-MA04")){
//			ngayHetHan = "30/09/2014 23:59:00";
//			} else {
				ngayHetHan ="31/05/2016 23:59:00";
//			}
			int diemso = Integer.parseInt(props.getString(maSP.trim()));
		
			sheetWrite.addCell(new Label(0, i, maUuDai.trim()));
			sheetWrite.addCell(new Label(1, i, kiTuCuoi.trim()));
			sheetWrite.addCell(new Label(2, i, serial.trim()));
			sheetWrite.addCell(new Label(3, i, serialCode.trim()));
			sheetWrite.addCell(new Label(4, i, serialName.trim()));
			sheetWrite.addCell(new Number(5, i, serialSTT));
			sheetWrite.addCell(new Label(6, i, tenSP.trim()));
			sheetWrite.addCell(new Label(7, i, maSP.trim()));
			sheetWrite.addCell(new Label(8, i, ngayHetHan.trim()));
			sheetWrite.addCell(new Number(9, i, dongia));
			sheetWrite.addCell(new Number(10, i, diemso));
			sheetWrite.addCell(new Label(11, i,ten_SP ));
//			sheetWrite.addCell(new Label(12, i, giaiThuong.trim()));
//			sheetWrite.addCell(new Number(13, i, triGia));
		}
		write.write();
		write.close();
		workbook.close();
	}
	
	
	public static void inserOrUpdateCodeToDB(String pathFile) {
		Connection conn = null;
		PreparedStatement ps = null;
		try{
//			String queryInsert = "INSERT INTO HOPTRI_CODE" +
//					"(MA_UU_DAI,KI_TU_CUOI,SERIAL,SERIAL_CODE,SERIAL_NAME,SERIAL_STT,TEN_SAN_PHAM,MA_SAN_PHAM,NGAY_HET_HAN,DON_GIA,st,diem_so,TEN_SP) " +
//					"VALUES(?,?,?,?,?,?,?,?,to_timestamp(?,'dd/MM/yyyy HH24:MI:ss'),?,406,?,?)"; 
//			String queryUpdate = "UPDATE HOPTRI_CODE SET KI_TU_CUOI=?,SERIAL=?,SERIAL_CODE=?,SERIAL_NAME=?,SERIAL_STT=?,TEN_SAN_PHAM =?"+
//					", MA_SAN_PHAM=?, DON_GIA = ?,st=406,diem_so=?,TEN_SP=?  WHERE MA_UU_DAI =?";
			
			String queryInsert = "INSERT INTO HOPTRI_CODE" +
					"(MA_UU_DAI,KI_TU_CUOI,SERIAL,SERIAL_CODE,SERIAL_NAME,SERIAL_STT,TEN_SAN_PHAM,MA_SAN_PHAM,NGAY_HET_HAN,DON_GIA,st,diem_so,TEN_SP, GIAI_THUONG,SO_TIEN,STATUS_MT2) " +
					"VALUES(?,?,?,?,?,?,?,?,to_timestamp(?,'dd/MM/yyyy HH24:MI:ss'),?,192,?,?,?,?,1)"; // status_mt2 =1 neu PL thuoc nhan tin 2 MT
			String queryUpdate = "UPDATE HOPTRI_CODE SET KI_TU_CUOI=?,SERIAL=?,SERIAL_CODE=?,SERIAL_NAME=?,SERIAL_STT=?,TEN_SAN_PHAM =?"+
					", MA_SAN_PHAM=?, DON_GIA = ?,st=192,diem_so=? ,TEN_SP=? ,GIAI_THUONG=?,SO_TIEN=? ,STATUS_MT2=1 WHERE MA_UU_DAI =?";
			
			Workbook workbook = Workbook.getWorkbook(new File(pathFile));
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();	
			for (int i = 1; i < rows; i++) {
				String maUuDai 		= sheet.getCell(0, i).getContents();
				String kiTuCuoi 	= sheet.getCell(1, i).getContents();
				String serial 		= sheet.getCell(2, i).getContents();
				String serialCode 	= sheet.getCell(3, i).getContents();
				String serialName 	= sheet.getCell(4, i).getContents();
				long serialSTT 		= Long.parseLong(sheet.getCell(5, i).getContents());
				String tenSP 		= sheet.getCell(6, i).getContents();
				String maSP 		= sheet.getCell(7, i).getContents();
				String ngayHetHan 	= sheet.getCell(8, i).getContents();
				long dongia 		= Long.parseLong(sheet.getCell(9, i).getContents());
				long diemso 		= Long.parseLong(sheet.getCell(10, i).getContents());
				String ten_SP 	= sheet.getCell(11, i).getContents();
				String giaiThuong 	= sheet.getCell(12, i).getContents();
				long triGia 		= Long.parseLong(sheet.getCell(13, i).getContents());
				String check = checkMaUuDai(maUuDai);
				if(check != null && check != ""){
					conn = LocalDatabasePooling.getInstance().getConnection();
					ps = conn.prepareStatement(queryUpdate);
					ps.setString(1, kiTuCuoi);
					ps.setString(2, serial);
					ps.setString(3, serialCode);
					ps.setString(4, serialName);
					ps.setLong(5, serialSTT);
					ps.setString(6, tenSP);
					ps.setString(7, maSP);
					ps.setLong(8, dongia);
					ps.setLong(9, diemso);
					ps.setString(10, ten_SP);
					ps.setString(11, giaiThuong);
					ps.setLong(12, triGia);
					ps.setString(13, maUuDai);
					System.out.println("UPDATE III :   " + i + " dongia :  " + dongia +"  Diem So: "+ diemso+"  Serial STT:  "+ serialSTT );
					ps.execute();
					
					if(conn != null) conn.close();
					if(ps != null) ps.close();
				}else{
					conn = LocalDatabasePooling.getInstance().getConnection();
					ps = conn.prepareStatement(queryInsert);
					ps.setString(1, maUuDai);
					ps.setString(2, kiTuCuoi);
					ps.setString(3, serial);
					ps.setString(4, serialCode);
					ps.setString(5, serialName);
					ps.setLong(6, serialSTT);
					ps.setString(7, tenSP);
					ps.setString(8, maSP);
					ps.setString(9, ngayHetHan);
					ps.setLong(10, dongia);
					ps.setLong(11, diemso);
					ps.setString(12, ten_SP);
					ps.setString(13, giaiThuong);
					ps.setLong(14, triGia);
					
					ps.execute();
					System.out.println("INSERT III :   " + i + ",  insert moi  existed :  " + maUuDai + " Ten_SP:   "+ ten_SP + "  TenSP:  "+ tenSP);
					if(conn != null) conn.close();
					if(ps != null) ps.close();
				}
			}
			workbook.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
				try {
					if(conn != null) conn.close();
					if(ps != null) ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		}
	}
	public static String checkMaUuDai(String maUuDai){
		Connection conn = null;
		PreparedStatement ps = null;
		String re = "";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			String sql = "SELECT MA_UU_DAI FROM HOPTRI_CODE WHERE MA_UU_DAI = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, maUuDai);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				re = resultSet.getString("MA_UU_DAI");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
				try {
					if(conn != null) conn.close();
					if(ps != null) ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return re;
	}
	
	public static void luuNguoiDungMoi(String soDtdd, int diemVuTruoc, int daSuDung, int baoLuu, String loaiTV, String date){
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO HOPTRI_NGUOIDUNG (SO_DTDD,DIEM_VU_TRUOC,DA_SU_DUNG,BAO_LUU,LOAI_THANH_VIEN,NGAY_THANG) VALUES(?,?,?,?,?,to_timestamp(?,'MM/dd/yyyy HH24:MI:ss'))";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDtdd.trim());
			ps.setInt(2, diemVuTruoc);
			ps.setInt(3, daSuDung);
			ps.setInt(4, baoLuu);
			ps.setString(5, loaiTV);
			ps.setString(6, date);
			ps.execute();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(conn != null) conn.close();
				if(ps != null) ps.close();
			} catch (Exception e2) {
			}
		}
	}
	
	public static void luuNguoiDungFromExcel(String pathFile){
		try{
			Workbook workbook = Workbook.getWorkbook(new File(pathFile));
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
				String sdt 		= sheet.getCell(0, i).getContents();
				int diemVuTruoc 		= Integer.parseInt(sheet.getCell(1, i).getContents());
				int daSuDung 		= Integer.parseInt(sheet.getCell(2, i).getContents());
				int baoLuu 		= Integer.parseInt(sheet.getCell(3, i).getContents());
				String loaiTV 		= sheet.getCell(4, i).getContents();
				String date 		= sheet.getCell(5, i).getContents();
				luuNguoiDungMoi(sdt, diemVuTruoc, daSuDung, baoLuu, loaiTV, date);
				System.out.println( "sdt : "+ sdt  + ", diemVuTruoc :  " + diemVuTruoc + ", daSuDung : " + daSuDung
						 + ", baoLuu : " + baoLuu+ ", loaiTV :  " + loaiTV + ",date :  " + date + "III : " + i );
			} 
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static String checkSDTOnTotal(String sdt){
		Connection conn = null;
		PreparedStatement ps = null;
		String re = "";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			String sql = "SELECT so_dtdd FROM HOPTRI_HTR_REPORT_TOTAL WHERE so_dtdd = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdt);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				re = resultSet.getString("so_dtdd");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
				try {
					if(conn != null) conn.close();
					if(ps != null) ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return re;
	}
	// tong ket diem
	public static String checkSDTOnNguoiDung(String sdt){
		Connection conn = null;
		PreparedStatement ps = null;
		String re = "";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			String sql = "SELECT so_dtdd FROM HOPTRI_NGUOIDUNG WHERE so_dtdd = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdt);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				re = resultSet.getString("so_dtdd");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
				try {
					if(conn != null) conn.close();
					if(ps != null) ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return re;
	}
	
	public static void updateTongKetNguoiDung(int diemVuTruoc, int diemDaSuDung,int baoLuu, String loaiTV, String sdt,int phieuqstt){
		Connection conn = null;
		PreparedStatement ps = null;
		String sqlUpdateTongKetNguoiDung = "UPDATE HOPTRI_NGUOIDUNG SET DIEM_VU_TRUOC=?, DA_SU_DUNG=?, BAO_LUU=?, LOAI_THANH_VIEN=?,phieu_qstt=? WHERE SO_DTDD=?";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sqlUpdateTongKetNguoiDung);
			
			ps.setLong(1, diemVuTruoc);
			ps.setLong(2, diemDaSuDung);
			ps.setLong(3, baoLuu);
			ps.setString(4, loaiTV);
			ps.setLong(5, phieuqstt);
			ps.setString(6, sdt);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	 static void updateTongKetTotalNga(int diemVuTruoc, int diemDaSuDung,int baoLuu, String sdt){
		Connection conn = null;
		PreparedStatement ps = null;
		String sqlUpdateTongKetNguoiDung = "UPDATE HOPTRI_HTR_REPORT_TOTAL SET DIEM_VU_TRUOC=?, DA_SU_DUNG=?, BAO_LUU=? WHERE SO_DTDD=?";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sqlUpdateTongKetNguoiDung);
			
			ps.setLong(1, diemVuTruoc);
			ps.setLong(2, diemDaSuDung);
			ps.setLong(3, baoLuu);
			ps.setString(4, sdt);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void updateTongKetTotal(int diemVuTruoc, int diemDaSuDung,int baoLuu, String sdt){
		Connection conn = null;
		PreparedStatement ps = null;
//		String sqlUpdateTongKetNguoiDung = "UPDATE HOPTRI_HTR_REPORT_TOTAL SET DIEM_VU_TRUOC=?, DA_SU_DUNG=?, BAO_LUU=?, TIEN_DA_THANH_TOAN=0,DON_GIA=0 WHERE SO_DTDD=?";
		String sqlUpdateTongKetNguoiDung = "UPDATE HOPTRI_HTR_REPORT_TOTAL SET DIEM_VU_TRUOC=?, DA_SU_DUNG=?, BAO_LUU=? WHERE SO_DTDD=?";
		String sqlupdate="update hoptri_htr_report_total set diem_vu_truoc=?,da_su_dung=?,bao_luu=? where so_dtdd=?";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sqlupdate);
			
			ps.setLong(1, diemVuTruoc);
			ps.setLong(2, diemDaSuDung);
			ps.setLong(3, baoLuu);
			ps.setString(4, sdt);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void insertTongKetTotal(int diemVuTruoc, int diemDaSuDung,int baoLuu,int tienDaThanhToan, String dLC1, String khuVuc, String maKHC1,
			String phanLoaiDL, String tenCuaHang, String tinh, String ghiChu2, String tenDL, String sdt){
		Connection conn = null;
		PreparedStatement ps = null;
		String sqlInsertTongKetNguoiDung = "INSERT INTO HOPTRI_HTR_REPORT_TOTAL(" +
				"ID" +
				",SO_DTDD" +
				",DIEM_VU_TRUOC," +
				"DA_SU_DUNG," +
				"BAO_LUU," +
				"KHU_VUC," +
				"DAI_LY_CAP1," +
				"MA_KHC1," +
				"PHAN_LOAI_DAI_LY," +
				"TEN_CUA_HANG," +
				"TEN_DAI_LY" +
				",TINH" +
				",GHI_CHU2) " +
				"VALUES (HIBERNATE_SEQUENCE.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sqlInsertTongKetNguoiDung);
			
			ps.setString(1, sdt);
			ps.setLong(2, diemVuTruoc);
			ps.setLong(3, diemDaSuDung);
			ps.setLong(4, baoLuu);
			ps.setString(5, khuVuc);
			ps.setString(6, dLC1);
			ps.setString(7, maKHC1);
			ps.setString(8, phanLoaiDL);
			ps.setString(9, tenCuaHang);
			ps.setString(10, tenDL);
			ps.setString(11, tinh);
			ps.setString(12, ghiChu2);
			
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void updateTongKetNguoiDungFromExcel(String pathFile){
		try{
			Workbook workbook = Workbook.getWorkbook(new File(pathFile));
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
				String sdt = sheet.getCell(0, i).getContents();
				String loaiTV = sheet.getCell(1, i).getContents();
				int diemVuTruoc = Integer.parseInt(sheet.getCell(2, i).getContents());
				int diemDaSuDung = Integer.parseInt(sheet.getCell(3, i).getContents());
				int baoLuu = Integer.parseInt(sheet.getCell(4, i).getContents());
				int sophieu = Integer.parseInt(sheet.getCell(5, i).getContents());
//				String check = checkSDTOnNguoiDung(sdt);
				String check = checkSDTOnTotal(sdt);
				if(check != null && check != ""){
					System.out.println("III :  " + i);
//					updateTongKetNguoiDung(diemVuTruoc, diemDaSuDung, baoLuu, loaiTV, sdt, sophieu);
					updateTongKetTotalNga(diemVuTruoc, diemDaSuDung, baoLuu, sdt);
					System.out.println(":) nga");
				}else{
					System.out.println("Noooooooooooooooooooooooooooooo Existed" + sdt);
				}
			} 
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateTongKetTotalFromExcel(String pathFile){
		try{
			Workbook workbook = Workbook.getWorkbook(new File(pathFile));
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
				
			}
			workbook.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void checkMaUuDaiFromExcel(String path){
		try{
			Workbook workbook = Workbook.getWorkbook(new File(path));
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
				String maUuDai = sheet.getCell(0, i).getContents();
				String check = checkMaUuDai(maUuDai);
				if(check != null && check != ""){
//					System.out.println("check maUuDai:   " + check);
				}else{
//					System.out.println("NOOOOOOOOOOOOOOOOOOO :   " + maUuDai);
				}
			}
			workbook.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateDonGiaMoi(  String maSP, String tenSP,int diemso, int dongia){
		Connection conn = null;
		PreparedStatement ps = null;
		String query = "update hoptri_code set DIEM_SO=?, don_gia=? where ma_san_pham=? ";
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, diemso);
			ps.setInt(2, dongia);
			ps.setString(3, maSP); 
//			ps.setString(3, tenSP);
			
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	public static void updateDonGiaMoiFromExcel(String pathFile){
		try{
			Workbook workbook = Workbook.getWorkbook(new File(pathFile));
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
			
				String maSP = sheet.getCell(0, i).getContents();
//				String tenSP = sheet.getCell(1, i).getContents();
				int diemso = Integer.parseInt(sheet.getCell(1, i).getContents());
				int dongia = Integer.parseInt(sheet.getCell(2, i).getContents());
				
				System.out.println("IIII :  " + i +", MA :  " + maSP + "  diemso SP:  "+diemso+  "   Don gia   :  "+ dongia);
				updateDonGiaMoi( maSP,"",diemso, dongia);
			}
			workbook.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	public static void updateTenSP(  String maSP, String tenSP, String  maUUDai){
		Connection conn = null;
		PreparedStatement ps = null;
//		String query = "update hoptri_code set don_gia=? where ma_san_pham=? and ten_san_pham=?";
		String query = "UPDATE hoptri_code set ten_san_pham=? , MA_SAN_PHAM=?, DON_GIA = 1485715 WHERE MA_UU_DAI= ?";
		
		try{
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(query);
			 
			ps.setString(1, tenSP);
			ps.setString(2, maSP); 
			ps.setString(3, maUUDai);
			
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	public static void updateTenSPFromExcel(String pathFile){
		try{
			Workbook workbook = Workbook.getWorkbook(new File(pathFile));
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
			
				String maUuDai = sheet.getCell(0, i).getContents();
				String maSP = sheet.getCell(1, i).getContents();
				String tenSP = sheet.getCell(2, i).getContents();
//				int dongia = 1485715 ;//Integer.parseInt(sheet.getCell(2, i).getContents());
				
				
				System.out.println("IIII :  " + i +", MA :  " + maSP + "  Ten SP:  "+tenSP+  "   Don gia   :  "+ maUuDai);
				updateTenSP( maSP,tenSP, maUuDai);
			}
			workbook.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
//			HtrUtils.getDataFromExcelHtr(0,"/vht/SMS/HTR/2014/capnhatma/PL220.xls","/vht/SMS/HTR/2014/capnhatma/PL220_Import.xls",0);
			//HtrUtils.getDataFromExcelHtr(0,"E:\\Nga\\NGA\\HTR\\Capnhatma\\PL226.xls","E:\\Nga\\NGA\\HTR\\Capnhatma\\PL226_Import.xls",0);
//			HtrUtils.inserOrUpdateCodeToDB("/vht/SMS/HTR/2014/capnhatma/PL192_Import.xls"); 
//			HtrUtils.getDataFromExcelHtr(0,"/app/vht/HTR/CapNhatMa/PL198.xls","/app/vht/HTR/CapNhatMa/PL198_Import.xls",0);
//			HtrUtils.inserOrUpdateCodeToDB("/app/vht/HTR/CapNhatMa/PL187_Import.xls");
			
//			HtrUtils.updateDonGiaMoiFromExcel("/vht/SMS/HTR/2014/capnhatma/UpdateDG109.xls");
//			HtrUtils.checkMaUuDaiFromExcel("D:\\VHT\\TechDocs\\SMS\\HTR\\data.dieuchinh.23.01.2013\\data1_import.xls");
//			HtrUtils.updateTongKetNguoiDungFromExcel("/vht/SMS/HTR/2014/capnhatma/PL173.xls"); // Thuc hien tong ket moi dot 1,4,7,10
//			HtrUtils.insertNguoiDungMoiFromExcel("/vht/SMS/HTR/2014/capnhatma/newPL144.xl");  
//			HtrUtils.updateLTVGhiChuFromExcel("/vht/SMS/HTR/2014/capnhatma/PL147Ghichu.xls");
//			HtrUtils.updateDSDaiLyFromExcel("/app/vht/HTR/CapNhatMa/DsKHPL165.xls");
//			HtrUtils.UpdateGhiChuFromExcel("/vht/SMS/HTR/2014/capnhatma/ten.xls");
//			HtrUtils.updateDonGiaMoiFromExcel("/vht/SMS/HTR/2014/capnhatma/PLDiemSo_donGia.xls");
//			HtrUtils.updateTenSPFromExcel("/vht/SMS/HTR/2014/capnhatma/Nga.xls");

			//HtrUtils.getDataFromExcelHtr(0,"/Users/an/Dropbox/Projects/VHT/Tech Support/HTR/PL234.xls","/Users/an/Dropbox/Projects/VHT/Tech Support/HTR/PL234_Import.xls",0);
			//HtrUtils.getDataFromExcelHtr(0,"C:\\Users\\Administrator\\Dropbox\\Tech Support\\HTR\\PL234_test.xls","C:\\Users\\Administrator\\Dropbox\\Tech Support\\HTR\\PL234_test_Import.xls",0);

			HtrUtils.getDataFromExcelHtr(0,"C:\\Users\\Administrator\\Dropbox\\Tech Support\\HTR\\PL_TESTTONGDAI6089.xls","C:\\Users\\Administrator\\Dropbox\\Tech Support\\HTR\\PL_TESTTONGDAI6089_Report.xls",0);
			System.out.println("Hello World!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}