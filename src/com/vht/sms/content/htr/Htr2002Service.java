package com.vht.sms.content.htr;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telix.sms.jms.DeliverMessage;
import com.vht.sms.content.ContentAbstract;
import com.vht.sms.content.db.LocalDatabasePooling;
import com.vht.sms.content.util.DateUtils;
import com.vht.sms.content.util.MyConst;
import com.vht.sms.content.util.StringUtils;

public class Htr2002Service extends ContentAbstract {
	static Log logger = LogFactory.getLog(Htr2002Service.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	static SimpleDateFormat ftime = new SimpleDateFormat("HH:mm");
	
	private static HtrProps props = HtrProps.getInstance();

	@Override
	protected synchronized Collection getMessages(DeliverMessage dm)throws Exception {
		Collection msgs = new ArrayList();
		try {
			messageType = MyConst.CHARGE;
			contentType = MyConst.TEXT;
			retriesNum = MyConst.SUCCESS;
			String msg = callHopTri(dm);
			String msg1 = xuLyHtr(dm);
			System.out.println(msg1);
			msgs.add(msg1.getBytes());
			return msgs;

			/*String msg = "Tong dai 8051 tam ngung de chot diem cuoi vu DX 15 - 16. Quy Dai Ly vui long giu lai ma so va nhan tin lai ke tu 8h sang ngay 21/03/16. Tran trong!";
			msgs.add(msg.getBytes());
			return msgs;*/
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return msgs;
		}

	}

	public synchronized String xuLyHtr(DeliverMessage dm) {
		try {
			String[] tokens = StringUtils.getObjectMessageUpperCase(dm
					.getMessage());
			if (tokens.length > 1) {
				String maUuDai = tokens[1];
				// kiem tra ma het han su dung
				if (kiemTraHetHanMaUuDai(maUuDai)) {
					String msg = props.getString("msg.code.enddate").replace("<code>", maUuDai);
					luuBaoCaoTrucTuyen(dm, msg, null, 0, "", "", "","", "", 0);
					return msg;
				}
				HopTriNguoiDung kiemTraDangKy = kiemTraDangKySoDtdd(dm.getUserId());
				String loaiTV = kiemTraSoDtddKhongDuocThamGia(dm.getUserId());
				// LoaiTV Deny
				if ("NONE".equalsIgnoreCase(loaiTV)) {
					luuBaoCaoTrucTuyen(dm, props.getString("user.deny"), null,0, "", "", "", "", "", 0);
					String msg = props.getString("user.deny");
					return msg;
				}
				// hopTriCode hop le
				if (kiemTraMaUuDai(maUuDai)) {
					HoptriCode hopTriCode = kiemTraMaUuDaiHopLe(maUuDai);
					long status = hopTriCode.getStatus();
					
					NumberFormat nf = new DecimalFormat("#,###,###");
					// ma da su dung
					if (status == 1) {
						String msg = props.getString("message.dupplicate")
								.replace("<code>", maUuDai);
						luuBaoCaoTrucTuyen(dm, msg, null, 0, "", "", "", "", "", 0);
						return msg;
					}
					updateMaDaSD(maUuDai);
					String tenCuaHang = null;
					String tenDaiLy = null;
					String tinh = null;
					String khuVuc = null;
					String phanLoaiDaiLy = "";
					String maKhc1 = null;
					String daiLyCap1 = null;
					String ghichu5 = "";
					long baoLuu = 0;
					long daSuDung = 0;
					long diemVuTruoc = 0;
					long tienDaThanhToan = 0;
					long tongDiemDoNhanTin = 0;
					long tongDiemLuyTien = 0;
					long tongTinNhan = 0;
					long tongTien = 0;
					long tongDoanhThu = 0;
					int soPhieuQstt = 0;
					int diemPhieuQstt = 0;
					
					String tenSP = hopTriCode.getTenSanPham();
					String tenSP1 = hopTriCode.getTenSP();
					String maSP = hopTriCode.getMaSanPham();
					long donGia = hopTriCode.getDonGia();
					long diemGoc = hopTriCode.getDiemso();
					int statusMt2 = hopTriCode.getStatusMt2();
					String serialCode = hopTriCode.getSerial();
					
					long soTien = hopTriCode.getSoTien();
				
					if (kiemTraDangKy != null) {
						baoLuu = kiemTraDangKy.getBaoLuu();
						tienDaThanhToan = kiemTraDangKy.getTienDaThanhToan();
						daSuDung = kiemTraDangKy.getDaSuDung();
						diemVuTruoc = kiemTraDangKy.getDiemVuTruoc();
						tenCuaHang = kiemTraDangKy.getTenCuaHang();
						tenDaiLy = kiemTraDangKy.getTenDaiLy();
						tinh = kiemTraDangKy.getTinh();
						khuVuc = kiemTraDangKy.getKhuVuc();
						phanLoaiDaiLy = kiemTraDangKy.getPhanLoaiDaiLy();
						maKhc1 = kiemTraDangKy.getMaKhc1();
						daiLyCap1 = kiemTraDangKy.getDaiLyCap1();
						ghichu5 = kiemTraDangKy.getGhiChu5();
						soPhieuQstt = kiemTraDangKy.getSoPhieuQstt();
						diemPhieuQstt = kiemTraDangKy.getDiemPhieuQstt();
						
						
						// update ngay thang nhan tin nguoi dung
						updateNguoiDungNhanTin(dm.getUserId());
					} else {
						// luu nguoi dung moi khi bang nguoi
						luuNguoiDungMoi(dm.getUserId());
					}
					int phonePromotion = phonePromotion(dm.getUserId());
					double heSoUuDai = layHeSoUuDai(dm.getUserId(), maUuDai,phonePromotion, maSP);
					long tongDiemGocVaCongThem = (long) Math.ceil(diemGoc* heSoUuDai);
					long diemCongThem = tongDiemGocVaCongThem - diemGoc;
					int sumTinNhanSP = sumTinNhanSP(maSP, dm.getUserId())+1;
					HoptriCodeTotal baoCaoTongHop = layThongTinTongHopTuSoDtdd(dm
							.getUserId());
					if (baoCaoTongHop != null) {
						// moi tin nhan +10.000
						tongTinNhan = baoCaoTongHop.getTongTinNhan() + 1;
						tongTien = baoCaoTongHop.getTongTien() + 10000;
						tongDiemDoNhanTin = baoCaoTongHop
								.getTongDiemDoNhanTin() + tongDiemGocVaCongThem;
						tongDiemLuyTien = baoLuu + tongDiemDoNhanTin
								+ diemPhieuQstt;
						tongDoanhThu = baoCaoTongHop.getTongDoanhThu() + donGia;
						// cap nhat khi co roi
						capNhatBaoCaoTongHop(dm.getUserId(), tongTinNhan,
								tongTien, tongDiemDoNhanTin, tongDiemLuyTien,
								tenCuaHang, tenDaiLy, tinh, khuVuc,
								phanLoaiDaiLy, maKhc1, daiLyCap1, diemVuTruoc,
								daSuDung, baoLuu, tienDaThanhToan,
								tongDoanhThu, donGia);
					} else {
						// tin dau tien // moi tin 10.000
						tongTinNhan = 1;
						tongTien = 10000;
						tongDiemDoNhanTin = tongDiemGocVaCongThem;
						tongDiemLuyTien = baoLuu + tongDiemDoNhanTin
								+ diemPhieuQstt;
						tongDoanhThu = hopTriCode.getDonGia();
						// tao moi khi chua co
						luuBaoCaoTongHop(dm.getId(), dm.getUserId(),
								tenCuaHang, tenDaiLy, tinh, khuVuc,
								phanLoaiDaiLy, maKhc1, daiLyCap1, diemVuTruoc,
								daSuDung, baoLuu, tongTinNhan, tongTien,
								tienDaThanhToan, tongDiemDoNhanTin,
								tongDiemLuyTien, tongDoanhThu, donGia);
					}
					// MT rieng ma da uu dai 07/01/2016 
					Date startDate = sdf.parse(props.getString("time.satart.0701"));
					Date endDate = sdf.parse(props.getString("time.end.0701"));
					
					if (checkDateTime(startDate, endDate) && (statusMt2 == 1)) {
						String giaiThuong = "";
						
						luuBaoCaoChiTiet(dm.getId(), dm.getUserId(), tenCuaHang, tenDaiLy, tinh, khuVuc, phanLoaiDaiLy,
								maKhc1, daiLyCap1, diemVuTruoc, daSuDung, baoLuu, maUuDai, tongTinNhan, tongTien,
								tienDaThanhToan, diemGoc, heSoUuDai, diemCongThem, tongDiemGocVaCongThem,
								tongDiemDoNhanTin, tongDiemLuyTien, tenSP, maSP, donGia, tongDoanhThu, serialCode, "");
						String msg = props.getString("msg.trungthuong");
						msg = msg.replace("<diemGoc>", diemGoc + "");
						msg = msg.replaceAll("<giaithuong>", giaiThuong);
						luuBaoCaoTrucTuyen(dm, msg, tenSP, donGia, "", "", "", "", "", 0);
						return msg;
					}
					if (heSoUuDai != 1) {// Co chuong trinh uu dai
						if (!kiemTraTinNhanDauTienSauTongKet(dm.getUserId())) {
							luuBaoCaoChiTiet(dm.getId(), dm.getUserId(),
									tenCuaHang, tenDaiLy, tinh, khuVuc,
									phanLoaiDaiLy, maKhc1, daiLyCap1,
									diemVuTruoc, daSuDung, baoLuu, maUuDai,
									tongTinNhan, tongTien, tienDaThanhToan,
									diemGoc, heSoUuDai, diemCongThem,
									tongDiemGocVaCongThem, tongDiemDoNhanTin,
									tongDiemLuyTien, tenSP, maSP, donGia,
									tongDoanhThu, serialCode, "");
							String msg = props.getString("msg.uudai.dautien");
							msg = msg.replace("<code>", maUuDai);
							msg = msg.replace("<diemGoc>", diemGoc + "");
							msg = msg.replace("<diemGocx2>",tongDiemGocVaCongThem + "");
							msg = msg.replace("<tongPhieu>", tongTinNhan + "");
							msg = msg.replace("<diemLuyTien>", tongDiemLuyTien+ "");
							msg = msg.replace("<tongTien>", nf.format(tongTien));
							msg = msg.replace("<diemQstt>", diemPhieuQstt + "");
							msg = msg.replace("<phieuQstt>", soPhieuQstt + "");
							msg = msg.replace("<tenSP>", tenSP1);
							msg = msg.replace("<sltinnhanSP>", sumTinNhanSP+ "");
							
							luuBaoCaoTrucTuyen(dm, msg, tenSP, donGia, "", "","", "", "", 0);
							return msg;
						} else {
							luuBaoCaoChiTiet(dm.getId(), dm.getUserId(),
									tenCuaHang, tenDaiLy, tinh, khuVuc,
									phanLoaiDaiLy, maKhc1, daiLyCap1,
									diemVuTruoc, daSuDung, baoLuu, maUuDai,
									tongTinNhan, tongTien, tienDaThanhToan,
									diemGoc, heSoUuDai, diemCongThem,
									tongDiemGocVaCongThem, tongDiemDoNhanTin,
									tongDiemLuyTien, tenSP, maSP, donGia,
									tongDoanhThu,serialCode, "");
							String msg = props.getString("msg.uudai.tieptheo");
							msg = msg.replace("<code>", maUuDai);
							msg = msg.replace("<diemGoc>", diemGoc + "");
							msg = msg.replace("<diemGocx2>",tongDiemGocVaCongThem + "");
							msg = msg.replace("<tongPhieu>", tongTinNhan + "");
							msg = msg.replace("<diemLuyTien>", tongDiemLuyTien+ "");
							msg = msg.replace("<tongTien>", nf.format(tongTien));
							msg = msg.replace("<tenSP>", tenSP1);
							msg = msg.replace("<sltinnhanSP>", sumTinNhanSP+ "");
							
							luuBaoCaoTrucTuyen(dm, msg, tenSP, donGia, "", "","", "", "", 0);
							return msg;
						}
					} else {
						if (loaiTV == null || loaiTV.equalsIgnoreCase("")) {
							// luu report chi tiet
							luuBaoCaoChiTiet(dm.getId(), dm.getUserId(),
									tenCuaHang, tenDaiLy, tinh, khuVuc,
									phanLoaiDaiLy, maKhc1, daiLyCap1,
									diemVuTruoc, daSuDung, baoLuu, maUuDai,
									tongTinNhan, tongTien, tienDaThanhToan,
									diemGoc, heSoUuDai, diemCongThem,
									tongDiemGocVaCongThem, tongDiemDoNhanTin,
									tongDiemLuyTien, tenSP, maSP, donGia,
									tongDoanhThu, serialCode, "");
							String msg = props.getString("msg.chua.dangky");
							msg = msg.replace("<code>", maUuDai);
							msg = msg.replace("<tongPhieu>", tongTinNhan + "");
							msg = msg.replace("<tongDiem>", tongDiemDoNhanTin+ "");
							msg = msg.replace("<tongTien>", nf.format(tongTien));
							msg = msg.replace("<tenSP>", tenSP1);
							msg = msg.replace("<sltinnhanSP>", sumTinNhanSP+ "");
							
							luuBaoCaoTrucTuyen(dm, msg, tenSP, donGia, "", "","", "", "", 0);
							return msg;
						} else {
							if (!kiemTraTinNhanDauTienSauTongKet(dm.getUserId())) {
								// tin dau tien sau tong ket
								// luu report chi tiet
								luuBaoCaoChiTiet(dm.getId(), dm.getUserId(),
										tenCuaHang, tenDaiLy, tinh, khuVuc,
										phanLoaiDaiLy, maKhc1, daiLyCap1,
										diemVuTruoc, daSuDung, baoLuu, maUuDai,
										tongTinNhan, tongTien, tienDaThanhToan,
										diemGoc, heSoUuDai, diemCongThem,
										tongDiemGocVaCongThem,
										tongDiemDoNhanTin, tongDiemLuyTien,
										tenSP, maSP, donGia, tongDoanhThu,serialCode,"");
								String msg = props.getString("msg.dautien.sautongket");
								msg = msg.replace("<code>", maUuDai);
								msg = msg.replace("<diemGoc>", diemGoc + "");
								msg = msg.replace("<tongTien>", ghichu5 + "");
								msg = msg.replace("<diemVuTruoc>", diemVuTruoc+ "");
								msg = msg.replace("<daSuDung>", daSuDung + "");
								msg = msg.replace("<baoLuu>", baoLuu + "");
								msg = msg.replace("<tongPhieu>", tongTinNhan+ "");
								msg = msg.replace("<diemLuyTien>",tongDiemLuyTien + "");
								msg = msg.replace("<diemQstt>", diemPhieuQstt+ "");
								msg = msg.replace("<soPhieuqstt>", soPhieuQstt+ "");
								msg = msg.replace("<tenSP>", tenSP1);
								msg = msg.replace("<sltinnhanSP>", sumTinNhanSP+ "");
								luuBaoCaoTrucTuyen(dm, msg, tenSP, donGia, "",
										"", "", "", "", 0);
								return msg;
							} else {
								luuBaoCaoChiTiet(dm.getId(), dm.getUserId(),
										tenCuaHang, tenDaiLy, tinh, khuVuc,
										phanLoaiDaiLy, maKhc1, daiLyCap1,
										diemVuTruoc, daSuDung, baoLuu, maUuDai,
										tongTinNhan, tongTien, tienDaThanhToan,
										diemGoc, heSoUuDai, diemCongThem,
										tongDiemGocVaCongThem,
										tongDiemDoNhanTin, tongDiemLuyTien,
										tenSP, maSP, donGia, tongDoanhThu, serialCode,"");
								String msg = props
										.getString("msg.tieptheo.sautongket");
								msg = msg.replace("<code>", maUuDai);
								msg = msg.replace("<tongPhieu>", tongTinNhan+ "");
								msg = msg.replace("<diemGoc>", diemGoc + "");
								msg = msg.replace("<diemLuyTien>",tongDiemLuyTien + "");
								msg = msg.replace("<tongTien>",nf.format(tongTien));
								msg = msg.replace("<thanhToan>",nf.format(tienDaThanhToan));
								msg = msg.replace("<tenSP>", tenSP1);
								msg = msg.replace("<sltinnhanSP>", sumTinNhanSP+ "");
								
								luuBaoCaoTrucTuyen(dm, msg, tenSP, donGia, "","", "", "", "", 0);
								return msg;
							}
						}
					}
				} else {
					String msg = props.getString("code.error").replace(
							"<tinNhan>", dm.getMessage());
					luuBaoCaoTrucTuyen(dm, msg, null, 0,"", "", "", "", "", 0);
					return msg;
				}

			} else {
				String msg = props.getString("code.error").replace("<tinNhan>",
						dm.getMessage());
				luuBaoCaoTrucTuyen(dm,msg, null, 0, "", "", "", "", "",0);
				return msg;
			}
		} catch (Exception e) {
			e.printStackTrace();
			String msg = props.getString("message.timeout");
			luuBaoCaoTrucTuyen(dm, msg, null, 0,"", "", "", "", "", 0);
			return msg;
		}
	}
	
	public synchronized String callHopTri(DeliverMessage dm) {
		String link = props.getString("htr.link");
		try {
			URL urlAgr = new URL(link);
			List<HttpHeader> requestList = new ArrayList<HttpHeader>();
			requestList.add(new HttpHeader("phone", dm.getUserId(), false));
			requestList.add(new HttpHeader("mo", dm.getMessage(), false));
			requestList.add(new HttpHeader("service", dm.getServiceId(), false));
			requestList.add(new HttpHeader("command_code", dm.getCommandCode(),false));
			requestList.add(new HttpHeader("user", "incomvht", false));
			requestList.add(new HttpHeader("pass", "Sms@2o14Inc0m", false));

			List<String> responseList = HTTPGateway.HttpPost(urlAgr,
					requestList);
			for (String item : responseList) {
				if (!item.isEmpty()) {
					return item;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return props.getString("message.timeout");
		}
		return props.getString("message.timeout");
	}

	// Su dung khi co cac chuong trinh uu dai danh cho mot so thanh vien va mot
	// so ma uu dai
	public static double layHeSoUuDai(String soDtdd, String maUuDai, int phonePromotion,String maSP) throws Exception {
		double theoSoDTDD = -1;
		Date startDate = sdf.parse(props.getString("time.satart.uu.dai.sdt"));
		Date endDate = sdf.parse(props.getString("time.end.uu.dai.sdt"));
		
		if (checkDateTime(startDate,endDate)) {
			theoSoDTDD = checkUuDai(maSP, phonePromotion , soDtdd);
		}
		if (theoSoDTDD > 1.) {
			return theoSoDTDD;
		} else {
			return 1.;
		}
	}

	// check so dien thoai duoc uu dai

	private static double checkUuDai(String maSP, int  phonePromotion, String soDtdd) {
		String[] masp = props.getString("ma.uu.dai").split("::");
		if (phonePromotion==1) {
			for (String ma : masp) {
					if (ma.equalsIgnoreCase(maSP)) {
					return props.getDouble("sdt.he.so.uu.dai");
				}
			}
		}
		return 1;
	}
	
	public static boolean checkDateTime(Date startDate, Date endDate) {
		try {
			Date currentDate = new Date();
			if (currentDate.compareTo(startDate) >= 0
					&& currentDate.compareTo(endDate) <= 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public static boolean checkTime(Date startTime, Date endTime ,Date currentTime) {
		try {
//			Date currentTime = new Date();
			if (currentTime.compareTo(startTime) >= 0
					&& currentTime.compareTo(endTime) <= 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	// Kiem tra tin nhan dau tien sau moi dot Tong ket chuong trinh
	public boolean kiemTraTinNhanDauTienSauTongKet(String soDtdd) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT SO_DTDD FROM HOPTRI_HTR_REPORT WHERE SO_DTDD=? AND NGAY_THANG >= ?";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		try {
			Date thoiGianTongKet = sdf.parse(props
					.getString("thoigian.tongket"));
			Timestamp thoiGianTongKetParse = new Timestamp(
					thoiGianTongKet.getTime());
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDtdd);
			ps.setTimestamp(2, thoiGianTongKetParse);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}

		return false;
	}

	// Kiem tra soDtdd da dang ky thanh vien voi Hop Tri hay chua
	public HopTriNguoiDung kiemTraDangKySoDtdd(String soDtdd) {
		HopTriNguoiDung hopTriNguoiDung = null;
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT TEN_CUA_HANG,TEN_DAI_LY,TINH,KHU_VUC,PHAN_LOAI_DAI_LY,MA_KHC1,DAI_LY_CAP1,DIEM_VU_TRUOC,DA_SU_DUNG,BAO_LUU,TIEN_DA_THANH_TOAN,GHI_CHU5"
				+ ", PHIEU_QSTT,DIEM_QSTT,LOAI_THANH_VIEN,GHI_CHU1,PROMOTION  FROM HOPTRI_NGUOIDUNG WHERE SO_DTDD=?";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDtdd);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				hopTriNguoiDung = new HopTriNguoiDung();
				hopTriNguoiDung.setTenCuaHang(rs.getString(1));
				hopTriNguoiDung.setTenDaiLy(rs.getString(2));
				hopTriNguoiDung.setTinh(rs.getString(3));
				hopTriNguoiDung.setKhuVuc(rs.getString(4));
				hopTriNguoiDung.setPhanLoaiDaiLy(rs.getString(5));
				hopTriNguoiDung.setMaKhc1(rs.getString(6));
				hopTriNguoiDung.setDaiLyCap1(rs.getString(7));
				hopTriNguoiDung.setDiemVuTruoc(rs.getLong(8));
				hopTriNguoiDung.setDaSuDung(rs.getLong(9));
				hopTriNguoiDung.setBaoLuu(rs.getLong(10));
				hopTriNguoiDung.setTienDaThanhToan(rs.getLong(11));
				hopTriNguoiDung.setGhiChu5(rs.getString(12));
				hopTriNguoiDung.setSoPhieuQstt(rs.getInt(13));
				hopTriNguoiDung.setDiemPhieuQstt(rs.getInt(14));
				hopTriNguoiDung.setLoaiThanhVien(rs.getString(15));
				hopTriNguoiDung.setGhiChu1(rs.getString(16));
				hopTriNguoiDung.setPromotion(rs.getInt(17));
				
				return hopTriNguoiDung;
			}
		} catch (SQLException e) {
			return null;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}

	public void luuNguoiDungMoi(String soDtdd) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO HOPTRI_NGUOIDUNG (SO_DTDD) VALUES(?)";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDtdd);
			ps.execute();
		} catch (Exception e) {
			logger.error("luu nguoi dung moi error :  " + e.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
	}

	public void updateNguoiDungNhanTin(String soDtdd) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "UPDATE HOPTRI_NGUOIDUNG SET NGAY_THANG = CURRENT_TIMESTAMP WHERE SO_DTDD=?";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDtdd);
			ps.execute();
		} catch (Exception e) {
			logger.error("update nguoi dung nhan tin error :  "
					+ e.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
	}

	public void luuBaoCaoChiTiet(long id, String soDtdd, String tenCuaHang,
			String tenDaiLy, String tinh, String khuVuc, String phanLoaiDaiLy,
			String maKhC1, String daiLyCap1, long diemVuTruoc, long daSuDung,
			long baoLuu, String maUuDai, long tongTinNhan, long tongTien,
			long tienDaThanhToan, long diemGoc, double heSoUuDai,
			long diemCongThem, long tongDiemGocVaCongThem,
			long tongDiemDoNhanTin, long tongDiemLuyTien, String sanPham,
			String maSanPham, long donGia, long tongDoanhThu, String ghichu1,
			String ghichu2) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO HOPTRI_HTR_REPORT(ID,SO_DTDD,TEN_CUA_HANG,TEN_DAI_LY,TINH,KHU_VUC,PHAN_LOAI_DAI_LY,MA_KHC1,DAI_LY_CAP1"
				+ ",DIEM_VU_TRUOC,DA_SU_DUNG,BAO_LUU,MA_UU_DAI,TONG_TIN_NHAN,TONG_TIEN,TIEN_DA_THANH_TOAN,DIEM_GOC,HE_SO_UU_DAI,"
				+ "DIEM_CONG_THEM,TONG_DIEM_GOC_VA_CONG_THEM,TONG_DIEM_DO_NHAN_TIN,TONG_DIEM_LUY_TIEN,SAN_PHAM,MA_SAN_PHAM,DON_GIA,TONG_DOANH_THU,GHI_CHU1,GHI_CHU2,NGAY_THANG)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			ps.setString(2, soDtdd);
			ps.setString(3, tenCuaHang);
			ps.setString(4, tenDaiLy);
			ps.setString(5, tinh);
			ps.setString(6, khuVuc);
			ps.setString(7, phanLoaiDaiLy);
			ps.setString(8, maKhC1);
			ps.setString(9, daiLyCap1);
			ps.setLong(10, diemVuTruoc);
			ps.setLong(11, daSuDung);
			ps.setLong(12, baoLuu);
			ps.setString(13, maUuDai);
			ps.setLong(14, tongTinNhan);
			ps.setLong(15, tongTien);
			ps.setLong(16, tienDaThanhToan);
			ps.setLong(17, diemGoc);
			ps.setDouble(18, heSoUuDai);
			ps.setLong(19, diemCongThem);
			ps.setLong(20, tongDiemGocVaCongThem);
			ps.setLong(21, tongDiemDoNhanTin);
			ps.setLong(22, tongDiemLuyTien);
			ps.setString(23, sanPham);
			ps.setString(24, maSanPham);
			ps.setLong(25, donGia);
			ps.setLong(26, tongDoanhThu);
			ps.setString(27, ghichu1);
			ps.setString(28, ghichu2);

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
	}

	public String kiemTraSoDtddKhongDuocThamGia(String soDtdd) {
		String result = "";
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT LOAI_THANH_VIEN FROM HOPTRI_NGUOIDUNG WHERE SO_DTDD=?";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDtdd);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString("LOAI_THANH_VIEN");
			}
		} catch (SQLException e) {
			return result;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
		return result;
	}
	public int phonePromotion(String soDtdd) {
		int result = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT promotion FROM HOPTRI_NGUOIDUNG WHERE SO_DTDD=?";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDtdd);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt("promotion");
			}
		} catch (SQLException e) {
			return result;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
		return result;
	}
	public HoptriCode kiemTraMaUuDaiHopLe(String maUuDai) {
		HoptriCode hopTriCode = null;
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT * FROM HOPTRI_CODE WHERE MA_UU_DAI=?";
		try { // SERIAL_CODE,TEN_SAN_PHAM,MA_SAN_PHAM,DON_GIA,STATUS
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, maUuDai);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				hopTriCode = new HoptriCode();
				hopTriCode.setSerialCode(rs.getString("SERIAL_CODE"));
				hopTriCode.setMaUuDai(maUuDai);
				hopTriCode.setTenSanPham(rs.getString("TEN_SAN_PHAM"));
				hopTriCode.setMaSanPham(rs.getString("MA_SAN_PHAM"));
				hopTriCode.setDonGia(rs.getLong("DON_GIA"));
				hopTriCode.setDiemso(rs.getLong("DIEM_SO"));
				hopTriCode.setStatus(rs.getLong("STATUS"));
				hopTriCode.setMaUuDai(rs.getString("MA_UU_DAI"));
				hopTriCode.setGiaiThuong(rs.getString("GIAI_THUONG"));
				hopTriCode.setSoTien(rs.getLong("SO_TIEN"));
				hopTriCode.setStatusMt2(rs.getInt("STATUS_MT2"));
				hopTriCode.setTenSP(rs.getString("TEN_SP"));
				hopTriCode.setSerial(rs.getString("SERIAL"));
				
				return hopTriCode;
			}
		} catch (SQLException e) {
			return null;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}

		return null;
	}

	public synchronized boolean kiemTraMaUuDai(String maUuDai) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT * FROM HOPTRI_CODE WHERE MA_UU_DAI=?";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, maUuDai);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}

		return false;
	}
	// Dem so luong tin nhan theo maSP	
	public int sumTinNhanSP(String maSP, String soDT) {
		Connection conn = null;
		PreparedStatement ps = null;
		String time = props.getString("thoi.gian.count.sp");

		/*String sql = "SELECT count(ma_uu_dai) as RS FROM HOPTRI_HTR_REPORT WHERE SO_DTDD= ?  and MA_SAN_PHAM= ? and to_char(NGAY_THANG," +
				"'MM/yyyy') in("+  time+") ";*/

		String startTime = props.getString("thoi.gian.count.start");
		String endTime = props.getString("thoi.gian.count.end");
		String sql = "SELECT count(ma_uu_dai) as RS FROM HOPTRI_HTR_REPORT WHERE SO_DTDD= ?  and MA_SAN_PHAM= ? and " +
				"NGAY_THANG BETWEEN TO_DATE ("+startTime+", 'yyyy/mm/dd') AND TO_DATE ("+endTime+", 'yyyy/mm/dd')";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDT);
			ps.setString(2, maSP);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int result = rs.getInt("RS");
				return result;
			}
		} catch (SQLException e) {
			return 0;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}

		return 0;
	}
	
	public synchronized boolean kiemTraHetHanMaUuDai(String maUuDai) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT * FROM HOPTRI_CODE WHERE MA_UU_DAI=? and NGAY_HET_HAN < CURRENT_TIMESTAMP";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, maUuDai);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}

		return false;
	}

	public void luuBaoCaoTrucTuyen(DeliverMessage dm, String mt,String tenSanPham, long donGia, String tenCH, String vung,
			String khuVuc, String giaiThuong, String triGia, int status) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO HOPTRI_HTR_REPORT_ONLINE(ID,SO_DTDD,CU_PHAP,DAU_SO,MO,MT,NGAY_THANG,SAN_PHAM,DON_GIA,TEN_CUA_HANG,VUNG,KHU_VUC,"
				+ "GIAI_THUONG,TRI_GIA,STATUS) VALUES(HIBERNATE_SEQUENCE.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, dm.getUserId());
			ps.setString(2, dm.getCommandCode());
			ps.setString(3, dm.getServiceId());
			ps.setString(4, dm.getMessage());
			ps.setString(5, mt);
			ps.setTimestamp(6, dm.getReceiveDate());
			ps.setString(7, tenSanPham);
			ps.setLong(8, donGia);
			ps.setString(9, tenCH);
			ps.setString(10, vung);
			ps.setString(11, khuVuc);
			ps.setString(12, giaiThuong);
			ps.setString(13, triGia);
			ps.setLong(14, status);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
	}

	public HoptriCodeTotal layThongTinTongHopTuSoDtdd(String soDtdd) {

		HoptriCodeTotal hopTriCodeTotal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT SO_DTDD,BAO_LUU,DA_SU_DUNG,DIEM_VU_TRUOC,TIEN_DA_THANH_TOAN,TONG_DIEM_DO_NHAN_TIN,TONG_DIEM_LUY_TIEN,TONG_TIEN,TONG_TIN_NHAN,TONG_DOANH_THU FROM HOPTRI_HTR_REPORT_TOTAL WHERE SO_DTDD=?";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, soDtdd);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				hopTriCodeTotal = new HoptriCodeTotal();
				hopTriCodeTotal.setSoDtdd(rs.getString(1));
				hopTriCodeTotal.setBaoLuu(rs.getLong(2));
				hopTriCodeTotal.setDaSuDung(rs.getLong(3));
				hopTriCodeTotal.setDiemVuTruoc(rs.getLong(4));
				hopTriCodeTotal.setTienDaThanhToan(rs.getLong(5));
				hopTriCodeTotal.setTongDiemDoNhanTin(rs.getLong(6));
				hopTriCodeTotal.setTonDiemLuyTien(rs.getLong(7));
				hopTriCodeTotal.setTongTien(rs.getLong(8));
				hopTriCodeTotal.setTongTinNhan(rs.getLong(9));
				hopTriCodeTotal.setTongDoanhThu(rs.getLong(10));
				return hopTriCodeTotal;
			}
		} catch (SQLException e) {
			return null;
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}

		return null;
	}

	public void luuBaoCaoTongHop(long id, String soDtdd, String tenCuaHang,
			String tenDaiLy, String tinh, String khuVuc, String phanLoaiDaiLy,
			String maKhC1, String daiLyCap1, long diemVuTruoc, long daSuDung,
			long baoLuu, long tongTinNhan, long tongTien, long tienDaThanhToan,
			long tongDiemDoNhanTin, long tongDiemLuyTien, long tongDoanhThu,
			long donGia) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO HOPTRI_HTR_REPORT_TOTAL(ID,SO_DTDD,TEN_CUA_HANG,TEN_DAI_LY,TINH,KHU_VUC,PHAN_LOAI_DAI_LY,MA_KHC1,DAI_LY_CAP1"
				+ ",DIEM_VU_TRUOC,DA_SU_DUNG,BAO_LUU,TONG_TIN_NHAN,TONG_TIEN,TIEN_DA_THANH_TOAN,"
				+ "TONG_DIEM_DO_NHAN_TIN,TONG_DIEM_LUY_TIEN,TONG_DOANH_THU,DON_GIA,NGAY_THANG) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			ps.setString(2, soDtdd);
			ps.setString(3, tenCuaHang);
			ps.setString(4, tenDaiLy);
			ps.setString(5, tinh);
			ps.setString(6, khuVuc);
			ps.setString(7, phanLoaiDaiLy);
			ps.setString(8, maKhC1);
			ps.setString(9, daiLyCap1);
			ps.setLong(10, diemVuTruoc);
			ps.setLong(11, daSuDung);
			ps.setLong(12, baoLuu);
			ps.setLong(13, tongTinNhan);
			ps.setLong(14, tongTien);
			ps.setLong(15, tienDaThanhToan);
			ps.setLong(16, tongDiemDoNhanTin);
			ps.setLong(17, tongDiemLuyTien);
			ps.setLong(18, tongDoanhThu);
			ps.setLong(19, donGia);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
	}

	public void capNhatBaoCaoTongHop(String soDtdd, long tongTinNhan,
			long tongTien, long tongDiemDoNhanTin, long tongDiemLuyTien,
			String tenCuaHang, String tenDaiLy, String tinh, String khuVuc,
			String phanLoaiDaiLy, String maKhC1, String daiLyCap1,
			long diemVuTruoc, long daSuDung, long baoLuu, long tienDaThanhToan,
			long tongDoanhThu, long donGia) {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "UPDATE HOPTRI_HTR_REPORT_TOTAL SET NGAY_THANG=CURRENT_TIMESTAMP,TONG_TIN_NHAN=?,TONG_TIEN=?,TONG_DIEM_DO_NHAN_TIN=?,TONG_DIEM_LUY_TIEN=?,"
				+ "TEN_CUA_HANG=?,TEN_DAI_LY=?,TINH=?,KHU_VUC=?,PHAN_LOAI_DAI_LY=?,MA_KHC1=?,DAI_LY_CAP1=?,DIEM_VU_TRUOC=?,DA_SU_DUNG=?,BAO_LUU=?,TIEN_DA_THANH_TOAN=?,TONG_DOANH_THU=?,DON_GIA=? WHERE SO_DTDD=?";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, tongTinNhan);
			ps.setLong(2, tongTien);
			ps.setLong(3, tongDiemDoNhanTin);
			ps.setLong(4, tongDiemLuyTien);
			ps.setString(5, tenCuaHang);
			ps.setString(6, tenDaiLy);
			ps.setString(7, tinh);
			ps.setString(8, khuVuc);
			ps.setString(9, phanLoaiDaiLy);
			ps.setString(10, maKhC1);
			ps.setString(11, daiLyCap1);
			ps.setLong(12, diemVuTruoc);
			ps.setLong(13, daSuDung);
			ps.setLong(14, baoLuu);
			ps.setLong(15, tienDaThanhToan);
			ps.setLong(16, tongDoanhThu);
			ps.setLong(17, donGia);
			ps.setString(18, soDtdd);
			ps.execute();
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (Exception e2) {
			}
		}
	}

	class HoptriCode {

		String maUuDai;
		String tenSanPham;
		String maSanPham;
		String serialCode;
		String serial;
		long donGia;
		long diemso;
		long status;
		String giaiThuong;
		long soTien;
		int statusMt2;
		String tenSP;
		
		public String getSerial() {
			return serial;
		}

		public void setSerial(String serial) {
			this.serial = serial;
		}
		public int getStatusMt2() {
			return statusMt2;
		}

		public void setStatusMt2(int statusMt2) {
			this.statusMt2 = statusMt2;
		}

		public String getGiaiThuong() {
			return giaiThuong;
		}

		public void setGiaiThuong(String giaiThuong) {
			this.giaiThuong = giaiThuong;
		}

		public long getSoTien() {
			return soTien;
		}

		public void setSoTien(long soTien) {
			this.soTien = soTien;
		}

		public String getMaUuDai() {
			return maUuDai;
		}

		public void setMaUuDai(String maUuDai) {
			this.maUuDai = maUuDai;
		}

		public String getTenSanPham() {
			return tenSanPham;
		}

		public void setTenSanPham(String tenSanPham) {
			this.tenSanPham = tenSanPham;
		}

		public String getMaSanPham() {
			return maSanPham;
		}

		public void setMaSanPham(String maSanPham) {
			this.maSanPham = maSanPham;
		}

		public String getSerialCode() {
			return serialCode;
		}

		public void setSerialCode(String serialCode) {
			this.serialCode = serialCode;
		}

		public long getDonGia() {
			return donGia;
		}

		public void setDonGia(long donGia) {
			this.donGia = donGia;
		}

		public long getDiemso() {
			return diemso;
		}

		public void setDiemso(long diemso) {
			this.diemso = diemso;
		}

		public long getStatus() {
			return status;
		}

		public void setStatus(long status) {
			this.status = status;
		}
		
		public String getTenSP() {
			return tenSP;
		}

		public void setTenSP(String tenSP) {
			this.tenSP = tenSP;
		}

	}

	class HoptriCodeTotal {
		String soDtdd;
		long baoLuu;
		long daSuDung;
		long diemVuTruoc;
		long tienDaThanhToan;
		long tongDiemDoNhanTin;
		long tonDiemLuyTien;
		long tongTien;
		long tongTinNhan;
		long tongDoanhThu;

		public HoptriCodeTotal() {
		}

		public String getSoDtdd() {
			return soDtdd;
		}

		public void setSoDtdd(String soDtdd) {
			this.soDtdd = soDtdd;
		}

		public long getBaoLuu() {
			return baoLuu;
		}

		public void setBaoLuu(long baoLuu) {
			this.baoLuu = baoLuu;
		}

		public long getDaSuDung() {
			return daSuDung;
		}

		public void setDaSuDung(long daSuDung) {
			this.daSuDung = daSuDung;
		}

		public long getDiemVuTruoc() {
			return diemVuTruoc;
		}

		public void setDiemVuTruoc(long diemVuTruoc) {
			this.diemVuTruoc = diemVuTruoc;
		}

		public long getTienDaThanhToan() {
			return tienDaThanhToan;
		}

		public void setTienDaThanhToan(long tienDaThanhToan) {
			this.tienDaThanhToan = tienDaThanhToan;
		}

		public long getTongDiemDoNhanTin() {
			return tongDiemDoNhanTin;
		}

		public void setTongDiemDoNhanTin(long tongDiemDoNhanTin) {
			this.tongDiemDoNhanTin = tongDiemDoNhanTin;
		}

		public long getTonDiemLuyTien() {
			return tonDiemLuyTien;
		}

		public void setTonDiemLuyTien(long tonDiemLuyTien) {
			this.tonDiemLuyTien = tonDiemLuyTien;
		}

		public long getTongTien() {
			return tongTien;
		}

		public void setTongTien(long tongTien) {
			this.tongTien = tongTien;
		}

		public long getTongTinNhan() {
			return tongTinNhan;
		}

		public void setTongTinNhan(long tongTinNhan) {
			this.tongTinNhan = tongTinNhan;
		}

		public long getTongDoanhThu() {
			return tongDoanhThu;
		}

		public void setTongDoanhThu(long tongDoanhThu) {
			this.tongDoanhThu = tongDoanhThu;
		}
	}

	class HopTriNguoiDung {
		String tenCuaHang;
		String tenDaiLy;
		String tinh;
		String khuVuc;
		String phanLoaiDaiLy;
		String maKhc1;
		String daiLyCap1;
		long diemVuTruoc;
		long daSuDung;
		long baoLuu;
		long tienDaThanhToan;
		String ghiChu1;
		String ghiChu5;
		int soPhieuQstt;
		int diemPhieuQstt;
		String loaiThanhVien;
		int promotion;

		public int getPromotion() {
			return promotion;
		}

		public void setPromotion(int promotion) {
			this.promotion = promotion;
		}

		public String getGhiChu1() {
			return ghiChu1;
		}

		public void setGhiChu1(String ghiChu1) {
			this.ghiChu1 = ghiChu1;
		}

		public String getTenCuaHang() {
			return tenCuaHang;
		}

		public void setTenCuaHang(String tenCuaHang) {
			this.tenCuaHang = tenCuaHang;
		}

		public String getTenDaiLy() {
			return tenDaiLy;
		}

		public void setTenDaiLy(String tenDaiLy) {
			this.tenDaiLy = tenDaiLy;
		}

		public String getTinh() {
			return tinh;
		}

		public void setTinh(String tinh) {
			this.tinh = tinh;
		}

		public String getKhuVuc() {
			return khuVuc;
		}

		public void setKhuVuc(String khuVuc) {
			this.khuVuc = khuVuc;
		}

		public String getPhanLoaiDaiLy() {
			return phanLoaiDaiLy;
		}

		public void setPhanLoaiDaiLy(String phanLoaiDaiLy) {
			this.phanLoaiDaiLy = phanLoaiDaiLy;
		}

		public String getMaKhc1() {
			return maKhc1;
		}

		public void setMaKhc1(String maKhc1) {
			this.maKhc1 = maKhc1;
		}

		public String getDaiLyCap1() {
			return daiLyCap1;
		}

		public void setDaiLyCap1(String daiLyCap1) {
			this.daiLyCap1 = daiLyCap1;
		}

		public long getDiemVuTruoc() {
			return diemVuTruoc;
		}

		public void setDiemVuTruoc(long diemVuTruoc) {
			this.diemVuTruoc = diemVuTruoc;
		}

		public long getDaSuDung() {
			return daSuDung;
		}

		public void setDaSuDung(long daSuDung) {
			this.daSuDung = daSuDung;
		}

		public long getBaoLuu() {
			return baoLuu;
		}

		public void setBaoLuu(long baoLuu) {
			this.baoLuu = baoLuu;
		}

		public long getTienDaThanhToan() {
			return tienDaThanhToan;
		}

		public void setTienDaThanhToan(long tienDaThanhToan) {
			this.tienDaThanhToan = tienDaThanhToan;
		}

		public String getGhiChu5() {
			return ghiChu5;
		}

		public void setGhiChu5(String ghiChu5) {
			this.ghiChu5 = ghiChu5;
		}

		public int getSoPhieuQstt() {
			return soPhieuQstt;
		}

		public void setSoPhieuQstt(int soPhieuQstt) {
			this.soPhieuQstt = soPhieuQstt;
		}

		public int getDiemPhieuQstt() {
			return diemPhieuQstt;
		}

		public void setDiemPhieuQstt(int diemPhieuQstt) {
			this.diemPhieuQstt = diemPhieuQstt;
		}

		public String getLoaiThanhVien() {
			return loaiThanhVien;
		}

		public void setLoaiThanhVien(String loaiThanhVien) {
			this.loaiThanhVien = loaiThanhVien;
		}
		
	}

	public synchronized void updateMaDaSD(String maUuDai) {
		Connection conn = null;
		PreparedStatement ps = null;
		String query = "update hoptri_code set STATUS='1' where ma_uu_dai=? ";
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, maUuDai);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void insertSMSFromFile(String pathFile){
		try{
			Workbook workbook = Workbook.getWorkbook(new File(pathFile));
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
			
				String soDTDD = sheet.getCell(0, i).getContents();
				String tinNhan = sheet.getCell(1, i).getContents();
				
				DeliverMessage dm = new DeliverMessage();
				
				dm.setId(new Date().getTime());
				dm.setUserId(soDTDD);
				dm.setCommandCode("HTR");
				dm.setServiceId("8051");
				dm.setMessage(tinNhan);
				dm.setReceiveDate(DateUtils.createTimestamp());
				Htr2002Service htr = new Htr2002Service();
				System.out.println(htr.getMessages(dm));
			}
			workbook.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {

		DeliverMessage dm = new DeliverMessage();
		
		dm.setId(new Date().getTime());
		dm.setUserId("84937145822");//U59644ZB
		dm.setCommandCode("HTR");
		dm.setServiceId("8051");
		dm.setMessage("Htr X89184NB");
		dm.setMobileOperator("VMS");
		dm.setReceiveDate(DateUtils.createTimestamp());
		Htr2002Service htr = new Htr2002Service();
		try {
			htr.getMessages(dm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
