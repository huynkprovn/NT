package com.vht.sms.content.htr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.math.NumberUtils;

import com.vht.sms.content.db.LocalDatabasePooling;

public class SupportHtr {
	
	public int capNhatDonGiaTheoMaSP(String maSp,long donGia){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement("UPDATE HOPTRI_CODE SET DON_GIA=? WHERE MA_SAN_PHAM=?");
			ps.setLong(1, donGia);
			ps.setString(2, maSp);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(conn != null) conn.close();
				if(ps != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return 0;
	}
	//Cap nhat don gia san pham
	//MaSanPham, tenSanPham, donGia
	public void capNhatDonGiaSP(String pathname) throws BiffException, FileNotFoundException, IOException{
		Workbook wb = Workbook.getWorkbook(new FileInputStream(new File(pathname)));
		Sheet sheetOut = wb.getSheet(0);
		int totalRow = sheetOut.getRows();
		SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
		String date=sdf2.format(new Date());
		PrintWriter error = new PrintWriter(new FileOutputStream(new File("CapNhatDonGiaLoi_"+date+".txt")),true);
		for(int i=1;i<totalRow;i++){
			String maSanPham = sheetOut.getCell(0, i).getContents();
			String tenSanPham = sheetOut.getCell(1, i).getContents();
			String donGia = sheetOut.getCell(2, i).getContents();
			if(maSanPham != null && donGia != null){
				System.out.println("Before:"+donGia);
				long donGiaLong = NumberUtils.toLong(donGia.trim(), 0);
				System.out.println("After:"+donGiaLong);
				int rs = capNhatDonGiaTheoMaSP(maSanPham.trim(), donGiaLong);
				if(rs == 0){
					error.write(maSanPham+"_"+tenSanPham+"\n");
				}
			}
		}
		System.out.println("Ket thuc");
		error.close();
		wb.close();
	}

	//Cap nhat thong tin HOPTRI_NGUOIDUNG
	public int capNhatHopTriNguoiDung(String soDtdd,String tenCuaHang,String tenDaiLy,String tinh,String khuVuc,String phanLoaiDaiLy,String maKhC1,String daiLyCap1,long diemVuTruoc,long daSuDung,long baoLuu){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement("UPDATE HOPTRI_NGUOIDUNG SET TEN_CUA_HANG=?,TEN_DAI_LY=?,TINH=?,KHU_VUC=?,PHAN_LOAI_DAI_LY=?,MA_KHC1=?,DAI_LY_CAP1=?,DIEM_VU_TRUOC=?,DA_SU_DUNG=?,BAO_LUU=? WHERE SO_DTDD=?");
			ps.setString(1, tenCuaHang);
			ps.setString(2, tenDaiLy);
			ps.setString(3, tinh);
			ps.setString(4, khuVuc);
			ps.setString(5, phanLoaiDaiLy);
			ps.setString(6, maKhC1);
			ps.setString(7, daiLyCap1);
			ps.setLong(8, diemVuTruoc);
			ps.setLong(9, daSuDung);
			ps.setLong(10, baoLuu);
			ps.setString(11, soDtdd);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(conn != null) conn.close();
				if(ps != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return 0;
	}
	
	//Them moi thong tin HOPTRI_NGUOIDUNG
	public boolean themMoiHopTriNguoiDung(String soDtdd,String tenCuaHang,String tenDaiLy,String tinh,String khuVuc,String phanLoaiDaiLy,String maKhC1,String daiLyCap1,long diemVuTruoc,long daSuDung,long baoLuu){
		System.out.println("Insert :"+soDtdd);
		Connection conn = null;
		PreparedStatement ps = null;
		String fields = "TEN_CUA_HANG,TEN_DAI_LY,TINH,KHU_VUC,PHAN_LOAI_DAI_LY,MA_KHC1,DAI_LY_CAP1,DIEM_VU_TRUOC,DA_SU_DUNG,BAO_LUU,SO_DTDD";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement("INSERT INTO HOPTRI_NGUOIDUNG("+fields+") VALUES(?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, tenCuaHang);
			ps.setString(2, tenDaiLy);
			ps.setString(3, tinh);
			ps.setString(4, khuVuc);
			ps.setString(5, phanLoaiDaiLy);
			ps.setString(6, maKhC1);
			ps.setString(7, daiLyCap1);
			ps.setLong(8, diemVuTruoc);
			ps.setLong(9, daSuDung);
			ps.setLong(10, baoLuu);
			ps.setString(11, soDtdd);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(conn != null) conn.close();
				if(ps != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return false;
	}
	
	//Cap nhat thogn tin gnuoi dung tu file Excel
	public void capNhatThongTinNguoiDungHtr(String pathname) throws BiffException, FileNotFoundException, IOException{
		Workbook wb = Workbook.getWorkbook(new FileInputStream(new File(pathname)));
		Sheet sheetOut = wb.getSheet(0);
		int totalRow = sheetOut.getRows();
		for(int i=1;i<totalRow;i++){
			String soDtdd = sheetOut.getCell(0, i).getContents();
			String tenCuaHang = sheetOut.getCell(1, i).getContents();
			String tenDaiLy = sheetOut.getCell(2, i).getContents();
			String tinh = sheetOut.getCell(3, i).getContents();
			String khuVuc = sheetOut.getCell(4, i).getContents();
			String phanLoaiDaiLy = sheetOut.getCell(5, i).getContents();
			String maKhC1 = sheetOut.getCell(6, i).getContents();
			String daiLyCap1 = sheetOut.getCell(7, i).getContents();
			String diemVuTruocStr = sheetOut.getCell(8, i).getContents();
			String daSuDungStr = sheetOut.getCell(9, i).getContents();
			String baoLuuStr = sheetOut.getCell(10, i).getContents();
			if(soDtdd != null){
				long diemVuTruoc = NumberUtils.toLong(diemVuTruocStr, 0);
				long daSuDung = NumberUtils.toLong(daSuDungStr, 0);
				long baoLuu = NumberUtils.toLong(baoLuuStr, 0);
				int rs = capNhatHopTriNguoiDung(soDtdd, tenCuaHang, tenDaiLy, tinh, khuVuc, phanLoaiDaiLy, maKhC1, daiLyCap1, diemVuTruoc, daSuDung, baoLuu);
				if(rs == 0){
					themMoiHopTriNguoiDung(soDtdd, tenCuaHang, tenDaiLy, tinh, khuVuc, phanLoaiDaiLy, maKhC1, daiLyCap1, diemVuTruoc, daSuDung, baoLuu);
				}
			}
		}
		System.out.println("Ket thuc");
		wb.close();
	}
	
	public static void main(String[] args) {
		SupportHtr support = new SupportHtr();
		try {
//			support.capNhatDonGiaSP("/app/sources/Data/Gateway/HopTri/HTR/Support/CapNhatDonGia_12112012.xls");
//			support.capNhatThongTinNguoiDungHtr("/app/sources/Data/Gateway/HopTri/HTR/Support/CapNhatThongTinNguoiDungHTR_12112012.xls");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
