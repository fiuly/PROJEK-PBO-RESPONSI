package app.service;

import app.model.Transaksi;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class KeuanganService {
    private ArrayList<Transaksi> daftarTransaksi = new ArrayList<>();
    
    public void tambahTransaksi(String tipe, String deskripsi, double jumlah, LocalDate tanggal, String kategori) {
        try {
            Transaksi t = new Transaksi(tipe, deskripsi, jumlah, tanggal, kategori);
            daftarTransaksi.add(t);
            System.out.println("Transaksi berhasil ditambahkan!");
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
    
    public void tambahTransaksi(String tipe, String deskripsi, double jumlah) {
        tambahTransaksi(tipe, deskripsi, jumlah, LocalDate.now(), "Lainnya");
    }
    
    public void updateTransaksi(int index, Transaksi transaksi) {
        if (index < 0 || index >= daftarTransaksi.size()) {
            throw new IndexOutOfBoundsException("Index transaksi tidak valid");
        }
        daftarTransaksi.set(index, transaksi);
    }
    
    public void hapusTransaksi(int index) {
        if (index < 0 || index >= daftarTransaksi.size()) {
            throw new IndexOutOfBoundsException("Index transaksi tidak valid");
        }
        daftarTransaksi.remove(index);
    }
    
    public ArrayList<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }
    
    public double hitungSaldo() {
        double saldo = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.isPemasukan()) {
                saldo += t.getJumlah();
            } else {
                saldo -= t.getJumlah();
            }
        }
        return saldo;
    }
    
    public double hitungTotalPemasukan() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.isPemasukan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }
    
    public double hitungTotalPengeluaran() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.isPengeluaran()) {
                total += t.getJumlah();
            }
        }
        return total;
    }
    
    public ArrayList<Transaksi> filterByTipe(String tipe) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (t.getTipe().equalsIgnoreCase(tipe)) {
                hasil.add(t);
            }
        }
        return hasil;
    }
    
    public ArrayList<Transaksi> filterByKategori(String kategori) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (t.getKategori().equalsIgnoreCase(kategori)) {
                hasil.add(t);
            }
        }
        return hasil;
    }
    
    public ArrayList<Transaksi> filterByTanggal(LocalDate dari, LocalDate sampai) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            LocalDate tgl = t.getTanggal();
            if ((tgl.isEqual(dari) || tgl.isAfter(dari)) && 
                (tgl.isEqual(sampai) || tgl.isBefore(sampai))) {
                hasil.add(t);
            }
        }
        return hasil;
    }
    
    public ArrayList<Transaksi> cariTransaksi(String keyword) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        String keywordLower = keyword.toLowerCase();
        for (Transaksi t : daftarTransaksi) {
            if (t.getDeskripsi().toLowerCase().contains(keywordLower)) {
                hasil.add(t);
            }
        }
        return hasil;
    }
    
    public ArrayList<Transaksi> urutkanByTanggal(boolean descending) {
        ArrayList<Transaksi> sorted = new ArrayList<>(daftarTransaksi);
        Collections.sort(sorted, new Comparator<Transaksi>() {
            @Override
            public int compare(Transaksi t1, Transaksi t2) {
                if (descending) {
                    return t2.getTanggal().compareTo(t1.getTanggal());
                } else {
                    return t1.getTanggal().compareTo(t2.getTanggal());
                }
            }
        });
        return sorted;
    }
    
    public Map<String, Double> getStatistikKategori(String tipe) {
        Map<String, Double> stats = new HashMap<>();
        for (Transaksi t : daftarTransaksi) {
            if (t.getTipe().equalsIgnoreCase(tipe)) {
                String kat = t.getKategori();
                stats.put(kat, stats.getOrDefault(kat, 0.0) + t.getJumlah());
            }
        }
        return stats;
    }
    
    public ArrayList<Transaksi> getLaporanBulanan(int tahun, int bulan) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        YearMonth ym = YearMonth.of(tahun, bulan);
        LocalDate mulai = ym.atDay(1);
        LocalDate akhir = ym.atEndOfMonth();
        return filterByTanggal(mulai, akhir);
    }
    
    public ArrayList<Transaksi> getLaporanTahunan(int tahun) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        LocalDate mulai = LocalDate.of(tahun, 1, 1);
        LocalDate akhir = LocalDate.of(tahun, 12, 31);
        return filterByTanggal(mulai, akhir);
    }
    
    public double hitungSaldoPeriode(LocalDate dari, LocalDate sampai) {
        double saldo = 0;
        ArrayList<Transaksi> transaksiPeriode = filterByTanggal(dari, sampai);
        for (Transaksi t : transaksiPeriode) {
            if (t.isPemasukan()) {
                saldo += t.getJumlah();
            } else {
                saldo -= t.getJumlah();
            }
        }
        return saldo;
    }
    
    public void resetData() {
        daftarTransaksi.clear();
    }
    
    public int getJumlahTransaksi() {
        return daftarTransaksi.size();
    }
}