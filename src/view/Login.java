package view;



import java.awt.*;
import javax.swing.*;


public class Login extends JFrame {

    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cbVaiTro;
    private JButton btnDangNhap, btnThoat;

    public Login() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Hệ Thống - Đăng Nhập");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- 1. TIÊU ĐỀ FORM ---
        JPanel pnlTitle = new JPanel();
        pnlTitle.setBackground(new Color(41, 128, 185));
        pnlTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        pnlTitle.add(lblTitle);
        add(pnlTitle, BorderLayout.NORTH);

        // --- 2. KHU VỰC NHẬP LIỆU CHÍNH ---
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng tài khoản
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlCenter.add(new JLabel("Tài Khoản:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlCenter.add(txtTaiKhoan, gbc);

        // Dòng mật khẩu
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        pnlCenter.add(new JLabel("Mật Khẩu:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlCenter.add(txtMatKhau, gbc);

        // Dòng vai trò
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        pnlCenter.add(new JLabel("Vai Trò:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7;
        cbVaiTro = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});
        cbVaiTro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlCenter.add(cbVaiTro, gbc);

        add(pnlCenter, BorderLayout.CENTER);

        // --- 3. THANH ĐIỀU KHIỂN NÚT BẤM (SOUTH) ---
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlSouth.setBackground(new Color(245, 245, 245));

        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDangNhap.setBackground(new Color(46, 204, 113));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnThoat.setFocusPainted(false);

        pnlSouth.add(btnDangNhap);
        pnlSouth.add(btnThoat);
        add(pnlSouth, BorderLayout.SOUTH);

        // =========================================================
        // XỬ LÝ SỰ KIỆN ĐĂNG NHẬP PHÂN QUYỀN
        // =========================================================
        btnDangNhap.addActionListener(e -> {
            String taiKhoan = txtTaiKhoan.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword()).trim();
            String vaiTroSelected = cbVaiTro.getSelectedItem().toString();

            if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Tài khoản và Mật khẩu!");
                return;
            }

            // Tài khoản kiểm tra mẫu cố định
            boolean hopLe = false;
            if (vaiTroSelected.equals("Quản lý") && taiKhoan.equals("admin") && matKhau.equals("123")) {
                hopLe = true;
            } else if (vaiTroSelected.equals("Nhân viên") && taiKhoan.equals("nv01") && matKhau.equals("123")) {
                hopLe = true;
            }

            if (hopLe) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công với quyền: " + vaiTroSelected);
                this.dispose(); // Đóng cửa sổ Login
                
                // Mở Mainframe và truyền dữ liệu phân quyền sang
                new Mainframe(taiKhoan, vaiTroSelected).setVisible(true); 
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên tài khoản, mật khẩu hoặc sai vai trò đăng nhập!", 
                        "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Xử lý nút Thoát
        btnThoat.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /* Fallback */ }

        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}