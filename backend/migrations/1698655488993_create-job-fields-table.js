/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('job_fields', {
    id: 'id',
    name: {
      type: 'VARCHAR(50)',
      notNull: true,
    },
    description: {
      type: 'TEXT',
      notNull: false,
    },
    created_at: {
      type: 'timestamp',
      notNull: true,
      default: pgm.func('current_timestamp'),
    },
    updated_at: {
      type: 'timestamp',
      notNull: false,
    },
  });

  pgm.sql(`
    INSERT INTO job_fields(name, description) 
    VALUES
      ('General - Tentang Perusahaan', 'Gambaran umum tentang identitas, tujuan, dan operasi perusahaan'),
      ('General - Latar Belakang User', 'Riwayat dan informasi terkait pengguna yang relevan'),
      ('General - Terkait Posisi', 'Hubungan serta relevansi dengan posisi atau jabatan yang dituju'),
      ('General - Softskill Situasional', 'Kemampuan dan keahlian yang relevan dalam situasi tertentu'),
      ('Penutup', 'Pernyataan penutup yang menggambarkan kesimpulan atau harapan akhir'),
      ('Kuliner dan Restoran', 'Bidang pekerjaan terkait dengan manajemen restoran dan kuliner'),
      ('Penjualan dan Pemasaran', 'Bidang pekerjaan yang fokus pada peran penjualan dan pemasaran'),
      ('Layanan Pelanggan', 'Bidang pekerjaan yang melibatkan peran layanan pelanggan'),
      ('Keuangan dan Akuntansi', 'Bidang pekerjaan terkait dengan keuangan dan akuntansi'),
      ('Pemasaran Digital dan Desain Grafis', 'Bidang pekerjaan yang menggabungkan pemasaran digital dan desain grafis'),
      ('Manajemen Proyek dan Administrasi', 'Bidang pekerjaan yang melibatkan manajemen proyek dan peran administratif'),
      ('Teknis dan IT', 'Bidang pekerjaan terkait dengan posisi teknis dan IT'),
      ('Logistik dan Pengiriman', 'Bidang pekerjaan yang melibatkan logistik dan peran pengiriman'),
      ('Keamanan', 'Bidang pekerjaan terkait dengan peran keamanan')
      ;  
  `);
};

exports.down = (pgm) => {
  pgm.dropTable('job_fields');
};
