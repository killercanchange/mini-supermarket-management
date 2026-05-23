// code kiểm tra kết nối CSDL, thêm sửa xóa nhân viên, thêm sửa xóa sản phẩm, hiển thị danh sách nhân viên, hiển thị danh sách sản phẩm

package controller;

import TruycapDL.TruycapHoaDon;
import TruycapDL.TruycapSP;
import java.time.LocalDate;
import java.util.List;
import model.HoaDon;
import model.Sanpham;

public class Salecontroller {

    private final TruycapHoaDon truycapHD = new TruycapHoaDon();
    private final TruycapSP truycapSP = new TruycapSP();

    // TẠO HÓA ĐƠN
    public boolean taoHoaDon(String maHD, String maSP, int soLuongMua, String hinhThucThanhToan) {

        // 1. Kiểm tra sản phẩm có tồn tại không
        Sanpham sp = truycapSP.getSanphamByMa(maSP);
        if (sp == null) {
            System.out.println("Lỗi: Không tìm thấy sản phẩm " + maSP);
            return false;
        }

        // 2. Kiểm tra đủ số lượng tồn kho
        if (sp.getSoLuong() < soLuongMua) {
            System.out.println("Lỗi: Tồn kho không đủ. Hiện còn: " + sp.getSoLuong());
            return false;
        }

        // 3. Kiểm tra sản phẩm hết hạn
        if (sp.isHetHan()) {
            System.out.println("Lỗi: Sản phẩm đã hết hạn sử dụng!");
            return false;
        }

        // 4. Tạo hóa đơn
        HoaDon hd = new HoaDon(maHD, maSP, sp.getTenSP(), soLuongMua, sp.getGiaBan(), LocalDate.now());
        boolean taoThanhCong = truycapHD.themHoaDon(hd);

        // 5. Cập nhật tồn kho sau khi bán
        if (taoThanhCong) {
            sp.setSoLuong(sp.getSoLuong() - soLuongMua);
            truycapSP.suaSanpham(sp);
        }

        return taoThanhCong;
    }

    //  TÍNH TIỀN TRẢ LẠI 
    public double tinhTienTraLai(double tongTien, double tienKhachDua) {
        if (tienKhachDua < tongTien) return -1; // báo hiệu không đủ tiền
        return tienKhachDua - tongTien;
    }

    //  KIỂM TRA HÌNH THỨC THANH TOÁN 
    public boolean kiemTraHinhThucThanhToan(String hinhThuc) {
        return hinhThuc.equalsIgnoreCase("tien mat")
            || hinhThuc.equalsIgnoreCase("chuyen khoan");
    }

    // LẤY DANH SÁCH HÓA ĐƠN TRONG NGÀY 
    public List<HoaDon> getHoaDonTrongNgay(LocalDate ngay) {
        return truycapHD.getHoaDonTheoNgay(ngay);
    }

    //  LẤY TỔNG DOANH THU TRONG NGÀY 
    public double getDoanhThuTrongNgay(LocalDate ngay) {
        return truycapHD.getTongDoanhThuTheoNgay(ngay);
    }
}