package app;

import app.model.Transaksi;
import app.service.KeuanganService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;

public class Main extends JFrame {

    private KeuanganService service;
    private JComboBox<String> cbJenis;
    private JComboBox<String> cbKategori;
    private JTextField tfDesk;
    private JTextField tfJumlah;
    private JTextField tfTanggal;
    private JTextField tfCari;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblSaldo;
    private JLabel lblPemasukan;
    private JLabel lblPengeluaran;
    private JLabel lblJumlahTransaksi;
    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Main() {
        service = new KeuanganService();
        setTitle("Aplikasi Catatan Keuangan - Versi Lengkap");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponent();
        loadTableFromService();
        updateStatistik();
    }

    private void initComponent() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pInput = createInputPanel();
        
        JPanel pTable = createTablePanel();
        
        JPanel pStats = createStatistikPanel();

        mainPanel.add(pInput, BorderLayout.NORTH);
        mainPanel.add(pTable, BorderLayout.CENTER);
        mainPanel.add(pStats, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Input Transaksi"));

        JPanel pForm = new JPanel(new GridLayout(3, 6, 5, 5));

        pForm.add(new JLabel("Tanggal (dd/MM/yyyy)"));
        pForm.add(new JLabel("Jenis"));
        pForm.add(new JLabel("Kategori"));
        pForm.add(new JLabel("Deskripsi"));
        pForm.add(new JLabel("Jumlah (Rp)"));
        pForm.add(new JLabel(""));

        tfTanggal = new JTextField(LocalDate.now().format(dateFormatter));
        cbJenis = new JComboBox<>(new String[]{"pemasukan", "pengeluaran"});
        cbKategori = new JComboBox<>(Transaksi.KATEGORI_PEMASUKAN);
        tfDesk = new JTextField();
        tfJumlah = new JTextField();
        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBackground(new Color(46, 204, 113));
        btnTambah.setForeground(Color.WHITE);

        pForm.add(tfTanggal);
        pForm.add(cbJenis);
        pForm.add(cbKategori);
        pForm.add(tfDesk);
        pForm.add(tfJumlah);
        pForm.add(btnTambah);

        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnExport = new JButton("Export CSV");
        JButton btnImport = new JButton("Import CSV");
        JButton btnLaporan = new JButton("Laporan");
        JButton btnReset = new JButton("Reset Data");
        
        btnEdit.setBackground(new Color(52, 152, 219));
        btnEdit.setForeground(Color.WHITE);
        btnHapus.setBackground(new Color(231, 76, 60));
        btnHapus.setForeground(Color.WHITE);
        btnReset.setBackground(new Color(149, 165, 166));
        btnReset.setForeground(Color.WHITE);

        pForm.add(btnEdit);
        pForm.add(btnHapus);
        pForm.add(btnExport);
        pForm.add(btnImport);
        pForm.add(btnLaporan);
        pForm.add(btnReset);

        panel.add(pForm, BorderLayout.CENTER);

        cbJenis.addActionListener(e -> updateKategoriComboBox());
        btnTambah.addActionListener(e -> onTambah());
        btnEdit.addActionListener(e -> onEdit());
        btnHapus.addActionListener(e -> onHapus());
        btnExport.addActionListener(e -> onExport());
        btnImport.addActionListener(e -> onImport());
        btnLaporan.addActionListener(e -> onLaporan());
        btnReset.addActionListener(e -> onReset());

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Transaksi"));

        JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSearch.add(new JLabel("Cari:"));
        tfCari = new JTextField(20);
        JButton btnCari = new JButton("Cari");
        JButton btnFilterTipe = new JButton("Filter Tipe");
        JButton btnFilterKategori = new JButton("Filter Kategori");
        JButton btnFilterTanggal = new JButton("Filter Tanggal");
        JButton btnResetFilter = new JButton("Reset Filter");

        pSearch.add(tfCari);
        pSearch.add(btnCari);
        pSearch.add(btnFilterTipe);
        pSearch.add(btnFilterKategori);
        pSearch.add(btnFilterTanggal);
        pSearch.add(btnResetFilter);

        model = new DefaultTableModel(
            new String[]{"No", "Tanggal", "Jenis", "Kategori", "Deskripsi", "Jumlah"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(250);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        JScrollPane scroll = new JScrollPane(table);

        panel.add(pSearch, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

     
        btnCari.addActionListener(e -> onCari());
        btnFilterTipe.addActionListener(e -> onFilterTipe());
        btnFilterKategori.addActionListener(e -> onFilterKategori());
        btnFilterTanggal.addActionListener(e -> onFilterTanggal());
        btnResetFilter.addActionListener(e -> {
            tfCari.setText("");
            loadTableFromService();
            updateStatistik();
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onEdit();
                }
            }
        });

        return panel;
    }

    private JPanel createStatistikPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Statistik"));

        lblSaldo = new JLabel("Saldo: Rp 0.00", SwingConstants.CENTER);
        lblPemasukan = new JLabel("Pemasukan: Rp 0.00", SwingConstants.CENTER);
        lblPengeluaran = new JLabel("Pengeluaran: Rp 0.00", SwingConstants.CENTER);
        lblJumlahTransaksi = new JLabel("Total: 0 transaksi", SwingConstants.CENTER);

        Font font = new Font("SansSerif", Font.BOLD, 12);
        lblSaldo.setFont(font);
        lblPemasukan.setFont(font);
        lblPengeluaran.setFont(font);
        lblJumlahTransaksi.setFont(font);

        lblSaldo.setForeground(new Color(52, 152, 219));
        lblPemasukan.setForeground(new Color(46, 204, 113));
        lblPengeluaran.setForeground(new Color(231, 76, 60));

        panel.add(lblSaldo);
        panel.add(lblPemasukan);
        panel.add(lblPengeluaran);
        panel.add(lblJumlahTransaksi);

        return panel;
    }

    private void updateKategoriComboBox() {
        String jenis = (String) cbJenis.getSelectedItem();
        cbKategori.removeAllItems();
        String[] kategori = Transaksi.getKategoriByTipe(jenis);
        for (String kat : kategori) {
            cbKategori.addItem(kat);
        }
    }

    private void onTambah() {
        try {
            String tanggalStr = tfTanggal.getText().trim();
            String jenis = ((String) cbJenis.getSelectedItem()).trim();
            String kategori = (String) cbKategori.getSelectedItem();
            String desk = tfDesk.getText().trim();
            String jumlahStr = tfJumlah.getText().trim();

            if (tanggalStr.isEmpty() || desk.isEmpty() || jumlahStr.isEmpty()) {
                throw new IllegalArgumentException("Semua field harus diisi");
            }

            LocalDate tanggal = LocalDate.parse(tanggalStr, dateFormatter);
            double jumlah = Double.parseDouble(jumlahStr.replaceAll(",", ""));

            service.tambahTransaksi(jenis, desk, jumlah, tanggal, kategori);
            loadTableFromService();
            updateStatistik();

           
            tfDesk.setText("");
            tfJumlah.setText("");
            tfTanggal.setText(LocalDate.now().format(dateFormatter));

            JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah! Gunakan dd/MM/yyyy");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah tidak valid!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void onEdit() {
        int sel = table.getSelectedRow();
        if (sel == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin diedit");
            return;
        }

        try {
            ArrayList<Transaksi> data = service.getDaftarTransaksi();
            Transaksi t = data.get(sel);

            String tanggalStr = JOptionPane.showInputDialog(this, "Tanggal (dd/MM/yyyy):", t.getTanggalString());
            if (tanggalStr == null) return;
            
            String jenis = (String) JOptionPane.showInputDialog(this, "Jenis:", "Edit",
                    JOptionPane.PLAIN_MESSAGE, null, 
                    new String[]{"pemasukan", "pengeluaran"}, t.getTipe());
            if (jenis == null) return;

            String[] kategoriOptions = Transaksi.getKategoriByTipe(jenis);
            String kategori = (String) JOptionPane.showInputDialog(this, "Kategori:", "Edit",
                    JOptionPane.PLAIN_MESSAGE, null, kategoriOptions, t.getKategori());
            if (kategori == null) return;

            String desk = JOptionPane.showInputDialog(this, "Deskripsi:", t.getDeskripsi());
            if (desk == null) return;

            String jumlahStr = JOptionPane.showInputDialog(this, "Jumlah:", String.valueOf(t.getJumlah()));
            if (jumlahStr == null) return;

            LocalDate tanggal = LocalDate.parse(tanggalStr, dateFormatter);
            double jumlah = Double.parseDouble(jumlahStr.replaceAll(",", ""));

            service.updateTransaksi(sel, new Transaksi(jenis, desk, jumlah, tanggal, kategori));
            loadTableFromService();
            updateStatistik();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil diupdate!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onHapus() {
        int sel = table.getSelectedRow();
        if (sel == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi untuk dihapus");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Hapus transaksi terpilih?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            service.hapusTransaksi(sel);
            loadTableFromService();
            updateStatistik();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onExport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan CSV");
        chooser.setSelectedFile(new File("transaksi_" + LocalDate.now() + ".csv"));
        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        File f = chooser.getSelectedFile();
        try (FileWriter fw = new FileWriter(f)) {
            fw.append("Tanggal,Jenis,Kategori,Deskripsi,Jumlah\n");
            for (Transaksi t : service.getDaftarTransaksi()) {
                fw.append(t.getTanggalString()).append(",");
                fw.append(t.getTipe()).append(",");
                fw.append(t.getKategori()).append(",");
                fw.append("\"").append(t.getDeskripsi().replace("\"", "\"\"")).append("\",");
                fw.append(String.valueOf(t.getJumlah())).append("\n");
            }
            fw.flush();
            JOptionPane.showMessageDialog(this, "Export berhasil ke: " + f.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal export: " + ex.getMessage());
        }
    }

    private void onImport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pilih File CSV");
        int res = chooser.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        File f = chooser.getSelectedFile();
        int imported = 0;
        int errors = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (parts.length >= 5) {
                        LocalDate tanggal = LocalDate.parse(parts[0].trim(), dateFormatter);
                        String jenis = parts[1].trim();
                        String kategori = parts[2].trim();
                        String deskripsi = parts[3].trim().replace("\"", "");
                        double jumlah = Double.parseDouble(parts[4].trim());
                        
                        service.tambahTransaksi(jenis, deskripsi, jumlah, tanggal, kategori);
                        imported++;
                    }
                } catch (Exception ex) {
                    errors++;
                }
            }
            loadTableFromService();
            updateStatistik();
            JOptionPane.showMessageDialog(this, 
                String.format("Import selesai!\nBerhasil: %d\nGagal: %d", imported, errors));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal import: " + ex.getMessage());
        }
    }

    private void onLaporan() {
        String[] options = {"Bulanan", "Tahunan", "Statistik Kategori"};
        String pilihan = (String) JOptionPane.showInputDialog(this, "Pilih jenis laporan:",
                "Laporan", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        if (pilihan == null) return;

        if (pilihan.equals("Bulanan")) {
            showLaporanBulanan();
        } else if (pilihan.equals("Tahunan")) {
            showLaporanTahunan();
        } else {
            showStatistikKategori();
        }
    }

    private void showLaporanBulanan() {
        String bulanStr = JOptionPane.showInputDialog(this, "Masukkan bulan (1-12):", 
            String.valueOf(LocalDate.now().getMonthValue()));
        String tahunStr = JOptionPane.showInputDialog(this, "Masukkan tahun:", 
            String.valueOf(LocalDate.now().getYear()));
        
        if (bulanStr == null || tahunStr == null) return;

        try {
            int bulan = Integer.parseInt(bulanStr);
            int tahun = Integer.parseInt(tahunStr);
            ArrayList<Transaksi> laporan = service.getLaporanBulanan(tahun, bulan);
            
            StringBuilder sb = new StringBuilder();
            sb.append("LAPORAN BULANAN ").append(bulan).append("/").append(tahun).append("\n\n");
            
            double totalMasuk = 0, totalKeluar = 0;
            for (Transaksi t : laporan) {
                if (t.isPemasukan()) totalMasuk += t.getJumlah();
                else totalKeluar += t.getJumlah();
            }
            
            sb.append("Total Pemasukan: Rp ").append(df.format(totalMasuk)).append("\n");
            sb.append("Total Pengeluaran: Rp ").append(df.format(totalKeluar)).append("\n");
            sb.append("Saldo: Rp ").append(df.format(totalMasuk - totalKeluar)).append("\n");
            sb.append("Jumlah Transaksi: ").append(laporan.size()).append("\n");
            
            JOptionPane.showMessageDialog(this, sb.toString(), "Laporan Bulanan", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void showLaporanTahunan() {
        String tahunStr = JOptionPane.showInputDialog(this, "Masukkan tahun:", 
            String.valueOf(LocalDate.now().getYear()));
        
        if (tahunStr == null) return;

        try {
            int tahun = Integer.parseInt(tahunStr);
            ArrayList<Transaksi> laporan = service.getLaporanTahunan(tahun);
            
            StringBuilder sb = new StringBuilder();
            sb.append("LAPORAN TAHUNAN ").append(tahun).append("\n\n");
            
            double totalMasuk = 0, totalKeluar = 0;
            for (Transaksi t : laporan) {
                if (t.isPemasukan()) totalMasuk += t.getJumlah();
                else totalKeluar += t.getJumlah();
            }
            
            sb.append("Total Pemasukan: Rp ").append(df.format(totalMasuk)).append("\n");
            sb.append("Total Pengeluaran: Rp ").append(df.format(totalKeluar)).append("\n");
            sb.append("Saldo: Rp ").append(df.format(totalMasuk - totalKeluar)).append("\n");
            sb.append("Jumlah Transaksi: ").append(laporan.size()).append("\n");
            
            JOptionPane.showMessageDialog(this, sb.toString(), "Laporan Tahunan", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void showStatistikKategori() {
        String[] options = {"pemasukan", "pengeluaran"};
        String tipe = (String) JOptionPane.showInputDialog(this, "Pilih tipe:",
                "Statistik Kategori", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        if (tipe == null) return;

        Map<String, Double> stats = service.getStatistikKategori(tipe);
        StringBuilder sb = new StringBuilder();
        sb.append("STATISTIK KATEGORI - ").append(tipe.toUpperCase()).append("\n\n");
        
        if (stats.isEmpty()) {
            sb.append("Tidak ada data");
        } else {
            for (Map.Entry<String, Double> entry : stats.entrySet()) {
                sb.append(entry.getKey()).append(": Rp ").append(df.format(entry.getValue())).append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(this, sb.toString(), "Statistik Kategori", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void onCari() {
        String keyword = tfCari.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan kata kunci pencarian");
            return;
        }
        ArrayList<Transaksi> hasil = service.cariTransaksi(keyword);
        displayTransaksiList(hasil);
    }

    private void onFilterTipe() {
        String[] options = {"pemasukan", "pengeluaran"};
        String tipe = (String) JOptionPane.showInputDialog(this, "Pilih tipe:",
                "Filter Tipe", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (tipe != null) {
            ArrayList<Transaksi> hasil = service.filterByTipe(tipe);
            displayTransaksiList(hasil);
        }
    }

    private void onFilterKategori() {
        String kategori = JOptionPane.showInputDialog(this, "Masukkan kategori:");
        if (kategori != null && !kategori.trim().isEmpty()) {
            ArrayList<Transaksi> hasil = service.filterByKategori(kategori);
            displayTransaksiList(hasil);
        }
    }

    private void onFilterTanggal() {
        String dariStr = JOptionPane.showInputDialog(this, "Dari tanggal (dd/MM/yyyy):");
        if (dariStr == null) return;
        String sampaiStr = JOptionPane.showInputDialog(this, "Sampai tanggal (dd/MM/yyyy):");
        if (sampaiStr == null) return;

        try {
            LocalDate dari = LocalDate.parse(dariStr, dateFormatter);
            LocalDate sampai = LocalDate.parse(sampaiStr, dateFormatter);
            ArrayList<Transaksi> hasil = service.filterByTanggal(dari, sampai);
            displayTransaksiList(hasil);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah!");
        }
    }

    private void onReset() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Hapus semua data transaksi?", "Konfirmasi Reset", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.resetData();
            loadTableFromService();
            updateStatistik();
            JOptionPane.showMessageDialog(this, "Semua data telah dihapus!");
        }
    }

    private void displayTransaksiList(ArrayList<Transaksi> list) {
        model.setRowCount(0);
        int no = 1;
        for (Transaksi t : list) {
            model.addRow(new Object[]{
                no++,
                t.getTanggalString(),
                t.getTipe(),
                t.getKategori(),
                t.getDeskripsi(),
                df.format(t.getJumlah())
            });
        }
    }

    private void loadTableFromService() {
        ArrayList<Transaksi> data = service.urutkanByTanggal(true); // Urutkan terbaru
        displayTransaksiList(data);
    }

    private void updateStatistik() {
        double saldo = service.hitungSaldo();
        double pemasukan = service.hitungTotalPemasukan();
        double pengeluaran = service.hitungTotalPengeluaran();
        int jumlah = service.getJumlahTransaksi();

        lblSaldo.setText("Saldo: Rp " + df.format(saldo));
        lblPemasukan.setText("Pemasukan: Rp " + df.format(pemasukan));
        lblPengeluaran.setText("Pengeluaran: Rp " + df.format(pengeluaran));
        lblJumlahTransaksi.setText("Total: " + jumlah + " transaksi");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}