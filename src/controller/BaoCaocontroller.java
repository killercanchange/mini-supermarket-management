package controller;

import TruycapDL.TruycapHoaDon;
import TruycapDL.TruycapSP;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.HoaDon;
import model.Sanpham;

public class BaoCaocontroller {

    private final TruycapHoaDon truycapHD = new TruycapHoaDon();
    private final TruycapSP truycapSP = new TruycapSP();

    // DOANH THU THEO NGÀY
    public double getDoanhThuTheoNgay(LocalDate ngay) {
        return truycapHD.getTongDoanhThuTheoNgay(ngay);
    }

    //  DOANH THU THEO THÁNG 
    public double getDoanhThuTheoThang(int thang, int nam) {
        return truycapHD.getTongDoanhThuTheoThang(thang, nam);
    }

    //  SẢN PHẨM BÁN CHẠY NHẤT 
    // Trả về danh sách SP được mua nhiều nhất dựa trên hóa đơn
    public List<String> getSanphamBanChay() {
        List<HoaDon> tatCaHD = truycapHD.getAllHoaDon();
        List<String> ketQua = new ArrayList<>();

        // Đếm số lượng bán theo từng maSP
        java.util.Map<String, Integer> demSoLuong = new java.util.HashMap<>();
        java.util.Map<String, String> tenSP = new java.util.HashMap<>();

        for (HoaDon hd : tatCaHD) {
            String ma = hd.getMaSP();
            demSoLuong.put(ma, demSoLuong.getOrDefault(ma, 0) + hd.getSoLuongMua());
            tenSP.put(ma, hd.getTenSP());
        }

        // Sắp xếp giảm dần theo số lượng bán
        demSoLuong.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .forEach(entry -> {
                ketQua.add(tenSP.get(entry.getKey()) + " — Đã bán: " + entry.getValue());
            });

        return ketQua;
    }

    //Hang ton kho can nhap
    public List<Sanpham> getHangCanNhap() {
        List<Sanpham> tatCaSP = truycapSP.getAllSanpham();
        List<Sanpham> hangCanNhap = new ArrayList<>();
        for (Sanpham sp : tatCaSP) {
            if (sp.isTonKhoThap()) hangCanNhap.add(sp);
        }
        return hangCanNhap;
    }

    //  Hang sap het han (30 ngay)
    public List<Sanpham> getHangSapHetHan() {
        List<Sanpham> tatCaSP = truycapSP.getAllSanpham();
        List<Sanpham> sapHetHan = new ArrayList<>();
        for (Sanpham sp : tatCaSP) {
            if (sp.isSapHetHan()) sapHetHan.add(sp);
        }
        return sapHetHan;
    }

    // HÀNG ĐÃ HẾT HẠN 
    public List<Sanpham> getHangHetHan() {
        List<Sanpham> tatCaSP = truycapSP.getAllSanpham();
        List<Sanpham> hetHan = new ArrayList<>();
        for (Sanpham sp : tatCaSP) {
            if (sp.isHetHan()) hetHan.add(sp);
        }
        return hetHan;
    }
}