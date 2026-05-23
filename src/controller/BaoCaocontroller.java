package controller;

import TruycapDL.TruycapHoaDon;
import TruycapDL.TruycapSP;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    // DOANH THU THEO THÁNG 
    public double getDoanhThuTheoThang(int thang, int nam) {
        return truycapHD.getTongDoanhThuTheoThang(thang, nam);
    }

    // SẢN PHẨM BÁN CHẠY NHẤT 
    // Giải pháp thông minh: Do cấu trúc bảng hoadon của bạn gom thông tin sản phẩm vào chuỗi ghi chu 
    // nên controller sẽ phân tích cú pháp chuỗi này để thống kê chính xác số lượng bán ra.
    public List<String> getSanphamBanChay() {
        List<HoaDon> tatCaHD = truycapHD.getAllHoaDon();
        List<String> ketQua = new ArrayList<>();

        java.util.Map<String, Integer> demSoLuong = new java.util.HashMap<>();

        for (HoaDon hd : tatCaHD) {
            String ghiChu = hd.getGhiChu();
            if (ghiChu != null && ghiChu.contains("Mua sản phẩm:") && ghiChu.contains("So luong:") || ghiChu.contains("Số lượng:")) {
                try {
                    // Trích xuất tên sản phẩm từ Ghi chú
                    String tenSP = ghiChu.substring(ghiChu.indexOf("Mua sản phẩm:") + 13, ghiChu.indexOf("|")).trim();
                    
                    // Trích xuất số lượng mua từ Ghi chú
                    String phanSoLuong = ghiChu.substring(ghiChu.indexOf("lượng:") != -1 ? ghiChu.indexOf("lượng:") + 6 : ghiChu.indexOf("luong:") + 6);
                    String soLuongStr = phanSoLuong.substring(0, phanSoLuong.indexOf("|")).trim();
                    int soLuongMua = Integer.parseInt(soLuongStr);

                    // Cộng dồn vào danh sách đếm
                    demSoLuong.put(tenSP, demSoLuong.getOrDefault(tenSP, 0) + soLuongMua);
                } catch (Exception e) {
                    // Bỏ qua các hóa đơn có định dạng ghi chú không chuẩn hoặc viết tay ngẫu nhiên
                }
            }
        }

        // Sắp xếp giảm dần theo số lượng bán và trả về chuỗi hiển thị lên giao diện
        demSoLuong.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .forEach(entry -> {
                ketQua.add(entry.getKey() + " — Đã bán: " + entry.getValue() + " sp");
            });

        return ketQua;
    }

    // HÀNG TỒN KHO CẦN NHẬP (Số lượng tồn kho nhỏ hơn hoặc bằng số lượng tối thiểu quy định)
    public List<Sanpham> getHangCanNhap() {
        List<Sanpham> tatCaSP = truycapSP.getAllSanpham();
        List<Sanpham> hangCanNhap = new ArrayList<>();
        for (Sanpham sp : tatCaSP) {
            // Đồng bộ kiểm tra: Nếu số lượng hiện tại thấp hơn mức tồn kho tối thiểu an toàn
            if (sp.getSoLuong() <= sp.getSoLuongToiThieu()) {
                hangCanNhap.add(sp);
            }
        }
        return hangCanNhap;
    }

    // HÀNG SẮP HẾT HẠN (Còn hạn nhưng nằm trong khoảng 30 ngày tới tính từ hôm nay)
    public List<Sanpham> getHangSapHetHan() {
        List<Sanpham> tatCaSP = truycapSP.getAllSanpham();
        List<Sanpham> sapHetHan = new ArrayList<>();
        LocalDate homNay = LocalDate.now();
        
        for (Sanpham sp : tatCaSP) {
            if (sp.getNgayHetHan() != null) {
                // Tính khoảng cách số ngày từ hôm nay đến ngày hết hạn của sản phẩm
                long soNgayConLai = ChronoUnit.DAYS.between(homNay, sp.getNgayHetHan());
                if (soNgayConLai > 0 && soNgayConLai <= 30) {
                    sapHetHan.add(sp);
                }
            }
        }
        return sapHetHan;
    }

    // HÀNG ĐÃ HẾT HẠN (Ngày hết hạn trước ngày hôm nay)
    public List<Sanpham> getHangHetHan() {
        List<Sanpham> tatCaSP = truycapSP.getAllSanpham();
        List<Sanpham> hetHan = new ArrayList<>();
        LocalDate homNay = LocalDate.now();
        
        for (Sanpham sp : tatCaSP) {
            if (sp.getNgayHetHan() != null && sp.getNgayHetHan().isBefore(homNay)) {
                hetHan.add(sp);
            }
        }
        return hetHan;
    }
}