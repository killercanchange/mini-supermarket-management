package view;

import TruycapDL.TruycapHoaDon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.HoaDon;

public class QuanLyHoaDon extends JFrame {

    private JTextField txtMaHD, txtMaSP, txtTenSP,
            txtSoLuong, txtGiaBan, txtNgayLap, txtTimKiem;

    private JButton btnThem, btnSua, btnXoa,
            btnLamMoi, btnTimKiem;

    private JTable tableHD;
    private DefaultTableModel tableModel;

    private TruycapHoaDon truycapHoaDon;

    public QuanLyHoaDon() {
        truycapHoaDon = new TruycapHoaDon();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {

        setTitle("Hệ Thống Quản Lý Hóa Đơn");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // =====================================================
        // PANEL NHẬP LIỆU
        // =====================================================

        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        pnlForm.add(new JLabel("Mã Hóa Đơn:"));
        txtMaHD = new JTextField();
        pnlForm.add(txtMaHD);

        pnlForm.add(new JLabel("Mã Sản Phẩm:"));
        txtMaSP = new JTextField();
        pnlForm.add(txtMaSP);

        pnlForm.add(new JLabel("Tên Sản Phẩm:"));
        txtTenSP = new JTextField();
        pnlForm.add(txtTenSP);

        pnlForm.add(new JLabel("Số Lượng Mua:"));
        txtSoLuong = new JTextField();
        pnlForm.add(txtSoLuong);

        pnlForm.add(new JLabel("Giá Bán:"));
        txtGiaBan = new JTextField();
        pnlForm.add(txtGiaBan);

        pnlForm.add(new JLabel("Ngày Lập (yyyy-mm-dd):"));
        txtNgayLap = new JTextField();
        pnlForm.add(txtNgayLap);

        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));

        pnlNorth.add(pnlForm, BorderLayout.CENTER);

        // =====================================================
        // THANH CÔNG CỤ
        // =====================================================

        JPanel pnlToolbar = new JPanel(new BorderLayout());

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        btnThem = new JButton("Thêm Hóa Đơn");
        btnSua = new JButton("Sửa Hóa Đơn");
        btnXoa = new JButton("Xóa Hóa Đơn");
        btnLamMoi = new JButton("Làm Mới");

        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        txtTimKiem = new JTextField(15);
        btnTimKiem = new JButton("Tìm Kiếm");

        pnlSearch.add(new JLabel("Nhập mã HĐ:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);

        pnlToolbar.add(pnlActions, BorderLayout.WEST);
        pnlToolbar.add(pnlSearch, BorderLayout.EAST);

        pnlNorth.add(pnlToolbar, BorderLayout.SOUTH);

        add(pnlNorth, BorderLayout.NORTH);

        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
            "Mã HĐ",
            "Mã SP",
            "Tên SP",
            "Số Lượng",
            "Giá Bán",
            "Tổng Tiền",
            "Ngày Lập"
        };

        tableModel = new DefaultTableModel(columns, 0);

        tableHD = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(tableHD);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Danh sách hóa đơn")
        );

        add(scrollPane, BorderLayout.CENTER);

        // =====================================================
        // SỰ KIỆN CLICK TABLE
        // =====================================================

        tableHD.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int row = tableHD.getSelectedRow();

                if (row >= 0) {

                    txtMaHD.setText(tableModel.getValueAt(row, 0).toString());
                    txtMaSP.setText(tableModel.getValueAt(row, 1).toString());
                    txtTenSP.setText(tableModel.getValueAt(row, 2).toString());
                    txtSoLuong.setText(tableModel.getValueAt(row, 3).toString());
                    txtGiaBan.setText(tableModel.getValueAt(row, 4).toString());

                    Object ngayLapObj = tableModel.getValueAt(row, 6);

                    txtNgayLap.setText(
                            ngayLapObj != null
                                    ? ngayLapObj.toString()
                                    : ""
                    );

                    txtMaHD.setEditable(false);
                }
            }
        });

        // =====================================================
        // NÚT THÊM
        // =====================================================

        btnThem.addActionListener(e -> {

            String maHD = txtMaHD.getText().trim();

            if (maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Mã hóa đơn không được để trống!");
                return;
            }

            if (truycapHoaDon.getHoaDonByMa(maHD) != null) {
                JOptionPane.showMessageDialog(this,
                        "Mã hóa đơn đã tồn tại!");
                return;
            }

            HoaDon hd = getHoaDonFromForm();

            if (hd != null) {

                if (truycapHoaDon.themHoaDon(hd)) {

                    JOptionPane.showMessageDialog(this,
                            "Thêm hóa đơn thành công!");

                    clearForm();
                    loadDataToTable();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Thêm thất bại!");
                }
            }
        });

        // =====================================================
        // NÚT SỬA
        // =====================================================

        btnSua.addActionListener(e -> {

            String maHD = txtMaHD.getText().trim();

            if (maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn hóa đơn cần sửa!");
                return;
            }

            HoaDon hd = getHoaDonFromForm();

            if (hd != null) {

                if (truycapHoaDon.suaHoaDon(hd)) {

                    JOptionPane.showMessageDialog(this,
                            "Cập nhật thành công!");

                    clearForm();
                    loadDataToTable();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Cập nhật thất bại!");
                }
            }
        });

        // =====================================================
        // NÚT XÓA
        // =====================================================

        btnXoa.addActionListener(e -> {

            String maHD = txtMaHD.getText().trim();

            if (maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn hóa đơn cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa hóa đơn này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                if (truycapHoaDon.xoaHoaDon(maHD)) {

                    JOptionPane.showMessageDialog(this,
                            "Xóa thành công!");

                    clearForm();
                    loadDataToTable();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Xóa thất bại!");
                }
            }
        });

        // =====================================================
        // NÚT LÀM MỚI
        // =====================================================

        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadDataToTable();
        });

        // =====================================================
        // NÚT TÌM KIẾM
        // =====================================================

        btnTimKiem.addActionListener(e -> {

            String maHD = txtTimKiem.getText().trim();

            if (maHD.isEmpty()) {

                loadDataToTable();

            } else {

                HoaDon hd = truycapHoaDon.getHoaDonByMa(maHD);

                tableModel.setRowCount(0);

                if (hd != null) {

                    tableModel.addRow(new Object[]{
                        hd.getMaHD(),
                        hd.getMaSP(),
                        hd.getTenSP(),
                        hd.getSoLuongMua(),
                        hd.getGiaBan(),
                        hd.getTongTien(),
                        hd.getNgayLap()
                    });

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy hóa đơn!");
                }
            }
        });
    }

    // =====================================================
    // LOAD DỮ LIỆU TABLE
    // =====================================================

    private void loadDataToTable() {
        fillTable(truycapHoaDon.getAllHoaDon());
    }

    // =====================================================
    // ĐỔ DỮ LIỆU LÊN TABLE
    // =====================================================

    private void fillTable(List<HoaDon> list) {

        tableModel.setRowCount(0);

        for (HoaDon hd : list) {

            tableModel.addRow(new Object[]{
                hd.getMaHD(),
                hd.getMaSP(),
                hd.getTenSP(),
                hd.getSoLuongMua(),
                hd.getGiaBan(),
                hd.getTongTien(),
                hd.getNgayLap()
            });
        }
    }

    // =====================================================
    // LẤY DỮ LIỆU TỪ FORM
    // =====================================================

    private HoaDon getHoaDonFromForm() {

        String maHD = txtMaHD.getText().trim();
        String maSP = txtMaSP.getText().trim();
        String tenSP = txtTenSP.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();
        String giaBanStr = txtGiaBan.getText().trim();
        String ngayLapStr = txtNgayLap.getText().trim();

        if (maSP.isEmpty() || tenSP.isEmpty()
                || soLuongStr.isEmpty()
                || giaBanStr.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Không được để trống dữ liệu!");
            return null;
        }

        int soLuong;
        double giaBan;

        try {

            soLuong = Integer.parseInt(soLuongStr);
            giaBan = Double.parseDouble(giaBanStr);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Số lượng hoặc giá bán không hợp lệ!");
            return null;
        }

        LocalDate ngayLap = null;

        if (!ngayLapStr.isEmpty()) {

            try {

                ngayLap = LocalDate.parse(ngayLapStr);

            } catch (DateTimeParseException e) {

                JOptionPane.showMessageDialog(this,
                        "Ngày lập sai định dạng yyyy-mm-dd!");
                return null;
            }
        }

        return new HoaDon(
                maHD,
                maSP,
                tenSP,
                soLuong,
                giaBan,
                ngayLap
        );
    }

    // =====================================================
    // CLEAR FORM
    // =====================================================

    private void clearForm() {

        txtMaHD.setText("");
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtSoLuong.setText("");
        txtGiaBan.setText("");
        txtNgayLap.setText("");
        txtTimKiem.setText("");

        txtMaHD.setEditable(true);

        tableHD.clearSelection();
    }

    // =====================================================
    // MAIN TEST
    // =====================================================

    public static void main(String[] args) {

        try {

            for (UIManager.LookAndFeelInfo info
                    : UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {

                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(() -> {
            new QuanLyHoaDon().setVisible(true);
        });
    }
}