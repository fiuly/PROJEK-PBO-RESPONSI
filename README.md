# 💰 Aplikasi Catatan Keuangan (Financial Records App)

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Java_Swing-blue?style=for-the-badge)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](https://opensource.org/licenses/MIT)
[![Youtube](https://img.shields.io/badge/YouTube-Demo_Video-red?style=for-the-badge&logo=youtube&logoColor=white)](https://youtu.be/9qV9gjt59mY)

> **Responsi 2 Pemrograman Berorientasi Objek** > Aplikasi manajemen keuangan pribadi berbasis desktop dengan fitur pencatatan, statistik, dan ekspor data CSV.

---

## 📺 Video Demo
Lihat demonstrasi lengkap fitur dan cara penggunaan aplikasi di YouTube:

[![Video Demo](https://img.youtube.com/vi/9qV9gjt59mY/0.jpg)](https://youtu.be/9qV9gjt59mY)

*(Klik gambar di atas untuk memutar video)*

---

## 📑 Daftar Isi
- [Deskripsi Project](#-deskripsi-project)
- [Fitur Utama](#-fitur-utama)
- [Struktur Project (MVC)](#-struktur-project-mvc)
- [Implementasi Materi Kuliah](#-implementasi-materi-kuliah)
    - [Materi Sebelum UTS](#1-materi-sebelum-uts)
    - [Materi Setelah UTS](#2-materi-setelah-uts-non-gui)
- [Screenshots](#-screenshots)
- [Cara Menjalankan](#-cara-menjalankan)
- [Anggota Kelompok](#-anggota-kelompok)

---

## 📝 Deskripsi Project
**Aplikasi Catatan Keuangan** adalah perangkat lunak berbasis Java Desktop (Swing) yang dirancang untuk membantu pengguna melacak arus kas (pemasukan dan pengeluaran). Aplikasi ini dikembangkan untuk memenuhi tugas akhir mata kuliah **Pemrograman Berorientasi Objek**, dengan menerapkan konsep **MVC (Model-View-Controller)** sederhana untuk memisahkan logika bisnis, data, dan tampilan.

Tujuan utama aplikasi ini adalah memberikan kemudahan pencatatan keuangan harian dengan fitur unggulan seperti **Export/Import CSV** yang memungkinkan backup data secara fleksibel.

---

## 🚀 Fitur Utama
* ✅ **CRUD Transaksi:** Tambah, Edit, Hapus, dan Lihat data transaksi harian.
* 📊 **Statistik Real-time:** Dashboard yang menampilkan total saldo, total pemasukan, dan pengeluaran secara otomatis.
* 📂 **Export & Import CSV:** Simpan data ke file CSV dan baca kembali data dari file eksternal (Fitur Unggulan).
* 🔍 **Pencarian & Filter:** Cari transaksi berdasarkan kata kunci deskripsi, atau filter berdasarkan rentang tanggal dan kategori.
* 📈 **Laporan Keuangan:** Menampilkan ringkasan laporan bulanan dan tahunan dalam format pop-up yang informatif.

---

## 📂 Struktur Project (MVC)
Project ini disusun dengan struktur paket (*package*) yang rapi untuk memudahkan pengembangan:
