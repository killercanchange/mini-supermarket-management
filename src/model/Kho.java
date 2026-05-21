package model;

public class Kho {

    private String maKho;
    private String tenKho;
    private String diachi;
    private int sucChua; // Sức chứa tối đa của kho (ví dụ: số lượng sản phẩm)
    private String ghiChu;

    // Constructor rỗng
    public Kho() {}

    // Constructor đầy đủ
    public Kho(String maKho, String tenKho, String diachi, int sucChua, String ghiChu) {
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.diachi = diachi;
        this.sucChua = sucChua;
        this.ghiChu = ghiChu;
    }

    // GETTER & SETTER

    public String getMaKho() { return maKho; }
    public void setMaKho(String maKho) { this.maKho = maKho; }

    public String getTenKho() { return tenKho; }
    public void setTenKho(String tenKho) { this.tenKho = tenKho; }

    public String getDiaChi() { return diachi; }
    public void setDiaChi(String diachi) { this.diachi = diachi; }

    public int getSucChua() { return sucChua; }
    public void setSucChua(int sucChua) { this.sucChua = sucChua; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    @Override
    public String toString() {
        return "Kho{" +
                "maKho='" + maKho + '\'' +
                ", tenKho='" + tenKho + '\'' +
                ", diachi='" + diachi + '\'' +
                ", sucChua=" + sucChua +
                '}';
    }
}