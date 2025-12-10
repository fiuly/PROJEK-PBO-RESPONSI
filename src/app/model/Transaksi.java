package app.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaksi {
    public static final String TIPE_PEMASUKAN = "pemasukan";
    public static final String TIPE_PENGELUARAN = "pengeluaran";
    
    public static final String[] KATEGORI_PEMASUKAN = {"Gaji", "Bonus", "Investasi", "Lainnya"};
    public static final String[] KATEGORI_PENGELUARAN = {"Makanan", "Transport", "Belanja", "Tagihan", "Hiburan", "Kesehatan", "Lainnya"};
    
    private String tipe;  
    private String deskripsi;
    private double jumlah;
    private LocalDate tanggal;
    private String kategori;
    
    public Transaksi(String tipe, String deskripsi, double jumlah, String kategori) {
        this(tipe, deskripsi, jumlah, LocalDate.now(), kategori);
    }
    
    public Transaksi(String tipe, String deskripsi, double jumlah, LocalDate tanggal, String kategori) {
        validateTipe(tipe);
        validateJumlah(jumlah);
        validateDeskripsi(deskripsi);
        
        this.tipe = tipe.toLowerCase();
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.tanggal = tanggal != null ? tanggal : LocalDate.now();
        this.kategori = kategori != null ? kategori : "Lainnya";
    }
    
    private void validateTipe(String tipe) {
        if (tipe == null || tipe.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipe transaksi tidak boleh kosong");
        }
        if (!tipe.equalsIgnoreCase(TIPE_PEMASUKAN) && !tipe.equalsIgnoreCase(TIPE_PENGELUARAN)) {
            throw new IllegalArgumentException("Tipe transaksi harus 'pemasukan' atau 'pengeluaran'");
        }
    }
    
    private void validateJumlah(double jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0");
        }
    }
    
    private void validateDeskripsi(String deskripsi) {
        if (deskripsi == null || deskripsi.trim().isEmpty()) {
            throw new IllegalArgumentException("Deskripsi tidak boleh kosong");
        }
    }
    
    public String getTipe() {
        return tipe;
    }
    
    public String getDeskripsi() {
        return deskripsi;
    }
    
    public double getJumlah() {
        return jumlah;
    }
    
    public LocalDate getTanggal() {
        return tanggal;
    }
    
    public String getKategori() {
        return kategori;
    }
    
    public String getTanggalString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return tanggal.format(formatter);
    }
    
    public boolean isPemasukan() {
        return tipe.equalsIgnoreCase(TIPE_PEMASUKAN);
    }
    
    public boolean isPengeluaran() {
        return tipe.equalsIgnoreCase(TIPE_PENGELUARAN);
    }
    
    public static String[] getKategoriByTipe(String tipe) {
        if (tipe.equalsIgnoreCase(TIPE_PEMASUKAN)) {
            return KATEGORI_PEMASUKAN;
        } else {
            return KATEGORI_PENGELUARAN;
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | %s | Rp %.2f | %s", 
            getTanggalString(), tipe, kategori, jumlah, deskripsi);
    }
}