

// giao diện chính của phần mềm, hiển thị các chức năng chính như quản lý nhân viên, quản lý sản phẩm, quản lý hóa đơn, báo cáo doanh thu, v.v.


package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mainframe extends JFrame {

    private JLabel lblXinChao, lblDongHo, lblStatus;
    private JButton btnQLSanPham, btnQLKhachHang, btnQLNhanVien, btnQLHoaDon, btnQLKho, btnQLNhaCungCap, btnDangXuat;
    
    // Biến lưu trữ vai trò để đồng bộ trong suốt phiên làm việc
    private String vaiTroNguoiDung; 

    // Hàm khởi tạo nhận thông tin từ màn hình Login truyền sang
    public Mainframe(String taiKhoan, String vaiTro) {
        this.vaiTroNguoiDung = vaiTro;
        initComponents();
        startClock(); // Khởi chạy đồng hồ thời gian thực
        
        // Cập nhật thông tin chào mừng hiển thị góc phải
        lblXinChao.setText("Xin chào, " + taiKhoan + " [" + vaiTro + "]");
        
        // CƠ CHẾ PHÂN QUYỀN ĐỒNG BỘ:
        if (vaiTro.equalsIgnoreCase("Nhân viên")) {
            // Khóa tất cả các quyền quản trị, chỉ giữ lại Quản lý sản phẩm
            btnQLKhachHang.setEnabled(false);
            btnQLNhanVien.setEnabled(false);
            btnQLHoaDon.setEnabled(false);
            btnQLKho.setEnabled(false);
            btnQLNhaCungCap.setEnabled(false);
            
            // Đổi thông báo trạng thái phía dưới thanh trạng thái
            lblStatus.setText("Trạng thái: Tài khoản Nhân viên (Hạn chế quyền truy cập).");
        } else {
            lblStatus.setText("Trạng thái: Đang kết nối cơ sở dữ liệu với quyền Quản lý.");
        }
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng & Kho Tổng Hợp");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng mainframe sẽ tắt toàn bộ app
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =========================================================
        // 1. THANH TIÊU ĐỀ TRÊN CÙNG (NORTH)
        // =========================================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(41, 128, 185)); // Xanh đậm Flat design
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        // Góc phải chứa thông tin tài khoản & đồng hồ
        JPanel pnlHeaderRight = new JPanel(new GridLayout(2, 1));
        pnlHeaderRight.setOpaque(false);
        
        lblXinChao = new JLabel("Xin chào, Quản trị viên", SwingConstants.RIGHT);
        lblXinChao.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblXinChao.setForeground(Color.WHITE);
        
        lblDongHo = new JLabel("00:00:00 - 01/01/2026", SwingConstants.RIGHT);
        lblDongHo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDongHo.setForeground(new Color(241, 196, 15)); // Màu vàng nổi bật
        
        pnlHeaderRight.add(lblXinChao);
        pnlHeaderRight.add(lblDongHo);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // =========================================================
        // 2. KHU VỰC BẢNG ĐIỀU KHIỂN CHỨC NĂNG CHÍNH (CENTER)
        // =========================================================
        JPanel pnlMenu = new JPanel(new GridLayout(2, 3, 20, 20));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Nút 1: Quản Lý Sản Phẩm ---
        btnQLSanPham = createMenuButton("Quản Lý Sản Phẩm", "Hàng hóa, phân loại, hạn sử dụng", new Color(46, 204, 113));
        pnlMenu.add(btnQLSanPham);

        // --- Nút 2: Quản Lý Khách Hàng ---
        btnQLKhachHang = createMenuButton("Quản Lý Khách Hàng", "Thông tin khách, lịch sử tương tác", new Color(155, 89, 182));
        pnlMenu.add(btnQLKhachHang);

        // --- Nút 3: Quản Lý Nhân Viên ---
        btnQLNhanVien = createMenuButton("Quản Lý Nhân Viên", "Hồ sơ nhân sự, tài khoản, vai trò", new Color(52, 152, 219));
        pnlMenu.add(btnQLNhanVien);

        // --- Nút 4: Quản Lý Hóa Đơn ---
        btnQLHoaDon = createMenuButton("Quản Lý Hóa Đơn", "Lập hóa đơn, doanh thu bán hàng", new Color(230, 126, 34));
        pnlMenu.add(btnQLHoaDon);

        // --- Nút 5: Quản Lý Kho ---
        btnQLKho = createMenuButton("Quản Lý Kho Hàng", "Tồn kho, phiếu nhập, phiếu xuất", new Color(26, 188, 156));
        pnlMenu.add(btnQLKho);

        // --- Nút 6: Quản Lý Nhà Cung Cấp ---
        btnQLNhaCungCap = createMenuButton("Quản Lý Nhà Cung Cấp", "Đối tác phân phối, nguồn cung ứng", new Color(52, 73, 94));
        pnlMenu.add(btnQLNhaCungCap);

        add(pnlMenu, BorderLayout.CENTER);

        // =========================================================
        // 3. THANH TRẠNG THÁI PHÍA DƯỚI (SOUTH)
        // =========================================================
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        pnlFooter.setBackground(new Color(236, 240, 241));

        lblStatus = new JLabel("Trạng thái: Đang kết nối cơ sở dữ liệu ổn định.");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(127, 140, 141));
        pnlFooter.add(lblStatus, BorderLayout.WEST);

        btnDangXuat = new JButton("Đăng Xuất");
        btnDangXuat.setFocusPainted(false);
        pnlFooter.add(btnDangXuat, BorderLayout.EAST);

        add(pnlFooter, BorderLayout.SOUTH);

        // =========================================================
        // XỬ LÝ SỰ KIỆN ĐIỀU HƯỚNG MỞ CỬA SỔ (LISTENERS)
        // =========================================================
        btnQLSanPham.addActionListener(e -> new QuanLySanPham().setVisible(true));
        btnQLKhachHang.addActionListener(e -> new QuanLyKhachHang().setVisible(true));
        btnQLNhanVien.addActionListener(e -> new QuanLyNhanVien().setVisible(true));
        btnQLHoaDon.addActionListener(e -> new QuanLyHoaDon().setVisible(true));
        btnQLKho.addActionListener(e -> new QuanLyKho().setVisible(true));
        btnQLNhaCungCap.addActionListener(e -> new QuanLyNhaCungCap().setVisible(true));

        // Nút Đăng xuất quay về Login
        btnDangXuat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có muốn thoát phiên làm việc của tài khoản này?", 
                    "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose(); // Đóng màn hình chính
                new Login().setVisible(true); // Trở về giao diện Login
            }
        });
    }

    private JButton createMenuButton(String title, String subTitle, Color themeColor) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(themeColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeColor.darker(), 1),
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));

        JLabel lblBtnTitle = new JLabel(title, SwingConstants.CENTER);
        lblBtnTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBtnTitle.setForeground(Color.WHITE);

        JLabel lblBtnSub = new JLabel(subTitle, SwingConstants.CENTER);
        lblBtnSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBtnSub.setForeground(new Color(245, 245, 245));

        button.add(lblBtnTitle, BorderLayout.CENTER);
        button.add(lblBtnSub, BorderLayout.SOUTH);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
            lblDongHo.setText(now.format(formatter));
        });
        timer.start();
    }

    public static void main(String[] args) {
        // Mặc định chạy ứng dụng từ màn hình Đăng nhập (Login), 
        // Hàm main này chỉ dùng để test giao diện nhanh nếu cần.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /** Fallback */ }

        SwingUtilities.invokeLater(() -> new Mainframe("Quản trị viên", "Quản lý").setVisible(true));
    }
}