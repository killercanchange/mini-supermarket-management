package view;

import TruycapDL.TruycapKho;
import model.Kho;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class QuanLyKho extends JFrame {

    private JTextField txtMaKho, txtTenKho, txtDiaChi, txtSucChua, txtGhiChu, txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JTable tableKho;
    private DefaultTableModel tableModel;

    private TruycapKho truycapKho;

    public QuanLyKho() {
        truycapKho = new TruycapKho();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setTitle("Quản Lý Kho Hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ==== PANEL FORM NHẬP LIỆU (Top) ====
        JPanel pnlTop = new JPanel(new BorderLayout());
        
        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin Kho"));

        pnlForm.add(new JLabel("Mã Kho:"));
        txtMaKho = new JTextField();
        pnlForm.add(txtMaKho);

        pnlForm.add(new JLabel("Tên Kho:"));
        txtTenKho = new JTextField();
        pnlForm.add(txtTenKho);

        pnlForm.add(new JLabel("Địa Chỉ:"));
        txtDiaChi = new JTextField();
        pnlForm.add(txtDiaChi);

        pnlForm.add(new JLabel("Sức Chứa:"));
        txtSucChua = new JTextField();
        pnlForm.add(txtSucChua);

        pnlForm.add(new JLabel("Ghi Chú:"));
        txtGhiChu = new JTextField();
        pnlForm.add(txtGhiChu);
        
        pnlTop.add(pnlForm, BorderLayout.CENTER);

        // ==== PANEL TÌM KIẾM ====
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTimKiem.add(new JLabel("Tìm theo tên:"));
        txtTimKiem = new JTextField(15);
        btnTimKiem = new JButton("Tìm");
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);
        
        pnlTop.add(pnlTimKiem, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        // ==== BẢNG HIỂN THỊ DỮ LIỆU (Center) ====
        String[] columnNames = {"Mã Kho", "Tên Kho", "Địa Chỉ", "Sức Chứa", "Ghi Chú"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableKho = new JTable(tableModel);
        add(new JScrollPane(tableKho), BorderLayout.CENTER);

        // ==== PANEL NÚT CHỨC NĂNG (Bottom) ====
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        add(pnlButtons, BorderLayout.SOUTH);

        // ==========================================
        // ============ XỬ LÝ SỰ KIỆN ===============
        // ==========================================

        // Lấy dữ liệu từ bảng lên form khi click vào 1 dòng
        tableKho.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableKho.getSelectedRow();
                if (row >= 0) {
                    txtMaKho.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenKho.setText(tableModel.getValueAt(row, 1).toString());
                    txtDiaChi.setText(tableModel.getValueAt(row, 2).toString());
                    txtSucChua.setText(tableModel.getValueAt(row, 3).toString());
                    
                    Object ghiChu = tableModel.getValueAt(row, 4);
                    txtGhiChu.setText(ghiChu != null ? ghiChu.toString() : "");
                    
                    txtMaKho.setEditable(false); // Không cho sửa mã khi đang click chọn
                }
            }
        });

        // Nút Thêm
        btnThem.addActionListener(e -> {
            Kho kho = getKhoFromForm();
            if (kho != null) {
                if (truycapKho.themKho(kho)) {
                    JOptionPane.showMessageDialog(this, "Thêm kho thành công!");
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại (Trùng mã hoặc lỗi DB)!");
                }
            }
        });

        // Nút Sửa
        btnSua.addActionListener(e -> {
            Kho kho = getKhoFromForm();
            if (kho != null) {
                if (truycapKho.suaKho(kho)) {
                    JOptionPane.showMessageDialog(this, "Sửa kho thành công!");
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Sửa thất bại!");
                }
            }
        });

        // Nút Xóa
        btnXoa.addActionListener(e -> {
            String maKho = txtMaKho.getText().trim();
            if (maKho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kho để xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa kho này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (truycapKho.xoaKho(maKho)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }
        });

        // Nút Làm Mới
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadDataToTable();
        });

        // Nút Tìm Kiếm
        btnTimKiem.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText().trim();
            List<Kho> list = truycapKho.timKhoTheoTen(tuKhoa);
            fillTable(list);
        });
    }

    // ==== CÁC HÀM HỖ TRỢ ====

    // Hàm lấy dữ liệu từ DB đổ vào bảng
    private void loadDataToTable() {
        List<Kho> list = truycapKho.getAllKho();
        fillTable(list);
    }

    // Hàm điền List<Kho> vào table
    private void fillTable(List<Kho> list) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        for (Kho k : list) {
            tableModel.addRow(new Object[]{
                    k.getMaKho(), k.getTenKho(), k.getDiaChi(), k.getSucChua(), k.getGhiChu()
            });
        }
    }

    // Hàm xóa trắng form
    private void clearForm() {
        txtMaKho.setText("");
        txtTenKho.setText("");
        txtDiaChi.setText("");
        txtSucChua.setText("");
        txtGhiChu.setText("");
        txtTimKiem.setText("");
        txtMaKho.setEditable(true); // Mở lại cho phép nhập mã
        tableKho.clearSelection();
    }

    // Hàm lấy dữ liệu từ các ô TextField tạo thành Object Kho
    private Kho getKhoFromForm() {
        String ma = txtMaKho.getText().trim();
        String ten = txtTenKho.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sucChuaStr = txtSucChua.getText().trim();
        String ghiChu = txtGhiChu.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã kho và Tên kho không được để trống!");
            return null;
        }

        int sucChua = 0;
        try {
            if (!sucChuaStr.isEmpty()) {
                sucChua = Integer.parseInt(sucChuaStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên!");
            return null;
        }

        return new Kho(ma, ten, diaChi, sucChua, ghiChu);
    }

    // ==== HÀM MAIN ĐỂ CHẠY THỬ ====
    public static void main(String[] args) {
        // Chạy giao diện trên luồng sự kiện của Swing
        SwingUtilities.invokeLater(() -> {
            new QuanLyKho().setVisible(true);
        });
    }
}