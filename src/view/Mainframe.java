package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mainframe extends JFrame {

    private JLabel lblXinChao, lblDongHo;
    private JButton btnQLSanPham, btnQLKhachHang, btnQLNhanVien, btnQLHoaDon, btnQLKho, btnQLNhaCungCap, btnDangXuat;

    public Mainframe() {
        initComponents();
        startClock(); // Chạy đồng hồ hiển thị thời gian thực hệ thống
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng & Kho Tổng Hợp");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Tắt màn hình chính sẽ dừng toàn bộ chương trình
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =========================================================
        // 1. THANH TIÊU ĐỀ TRÊN CÙNG (NORTH)
        // =========================================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(41, 128, 185)); // Xanh đậm phong cách phẳng (Flat design)
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        // Góc phải chứa thông tin tài khoản & đồng hồ thời gian thực
        JPanel pnlHeaderRight = new JPanel(new GridLayout(2, 1));
        pnlHeaderRight.setOpaque(false);
        
        lblXinChao = new JLabel("Xin chào, Quản trị viên", SwingConstants.RIGHT);
        lblXinChao.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblXinChao.setForeground(Color.WHITE);
        
        lblDongHo = new JLabel("00:00:00 - 01/01/2026", SwingConstants.RIGHT);
        lblDongHo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDongHo.setForeground(new Color(241, 196, 15)); // Chữ màu vàng nổi bật
        
        pnlHeaderRight.add(lblXinChao);
        pnlHeaderRight.add(lblDongHo);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // =========================================================
        // 2. KHU VỰC BẢNG ĐIỀU KHIỂN CHỨC NĂNG CHÍNH (CENTER)
        // =========================================================
        // Thiết lập dạng Grid 2 hàng 3 cột để phân bổ đều 6 nút quản lý tương ứng với 6 file của bạn
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

        JLabel lblStatus = new JLabel("Trạng thái: Đang kết nối cơ sở dữ liệu ổn định.");
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

        // 1. Mở Quản Lý Sản Phẩm
        btnQLSanPham.addActionListener(e -> {
            new QuanLySanPham().setVisible(true);
        });

        // 2. Mở Quản Lý Khách Hàng
        btnQLKhachHang.addActionListener(e -> {
            new QuanLyKhachHang().setVisible(true);
        });

        // 3. Mở Quản Lý Nhân Viên (Gọi class QuanLyNV tương ứng với khai báo bên trong file của bạn)
        btnQLNhanVien.addActionListener(e -> {
            // Lưu ý: File của bạn tên QuanLyNhanVien.java nhưng bên trong khai báo class QuanLyNV công khai
            new QuanLyNhanVien().setVisible(true); 
        });

        // 4. Mở Quản Lý Hóa Đơn (Mở giao diện khi bạn viết code hoàn chỉnh sau này)
        btnQLHoaDon.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Tính năng Quản Lý Hóa Đơn đang được xây dựng!");
            // new QuanLyHoaDon().setVisible(true);
        });

        // 5. Mở Quản Lý Kho
        btnQLKho.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Tính năng Quản Lý Kho đang được xây dựng!");
            // new QuanLyKho().setVisible(true);
        });

        // 6. Mở Quản Lý Nhà Cung Cấp
        btnQLNhaCungCap.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Tính năng Quản Lý Nhà Cung Cấp đang được xây dựng!");
            // new QuanLyNhaCungCap().setVisible(true);
        });

        // Xử lý sự kiện nút Đăng xuất
        btnDangXuat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có muốn thoát phiên làm việc của tài khoản này?", 
                    "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose(); // Đóng màn hình chính
                System.out.println("Đã đăng xuất thành công.");
            }
        });
    }

    /**
     * Hàm thiết kế nhanh khối nút bấm điều hướng (Dashboard Card Button)
     */
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

        // Tên nghiệp vụ lớn ở trung tâm nút
        JLabel lblBtnTitle = new JLabel(title, SwingConstants.CENTER);
        lblBtnTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBtnTitle.setForeground(Color.WHITE);

        // Mô tả chức năng nhỏ ở cạnh đáy nút
        JLabel lblBtnSub = new JLabel(subTitle, SwingConstants.CENTER);
        lblBtnSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBtnSub.setForeground(new Color(245, 245, 245));

        button.add(lblBtnTitle, BorderLayout.CENTER);
        button.add(lblBtnSub, BorderLayout.SOUTH);

        // Đổi hình con trỏ chuột thành hình bàn tay khi rê vào nút bấm
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    /**
     * Khởi chạy bộ đếm thời gian hệ thống thực tế chạy liên tục mỗi giây
     */
    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
            lblDongHo.setText(now.format(formatter));
        });
        timer.start();
    }

    public static void main(String[] args) {
        // Đồng bộ phong cách Nimbus giúp nút và bảng hiển thị mượt mà, phẳng hóa hiện đại
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /** Fallback default UI */ }

        SwingUtilities.invokeLater(() -> new Mainframe().setVisible(true));
    }
}