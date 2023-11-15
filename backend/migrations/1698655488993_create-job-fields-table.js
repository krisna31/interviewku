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
      ('Administrasi dan Akuntansi', 'Bidang pekerjaan terkait dengan keuangan dan akuntansi'),
      ('Desain Grafis', 'Bidang pekerjaan yang berfokus pada desain grafis'),
      ('IT', 'Bidang pekerjaan terkait dengan posisi IT dan komputer'),
      ('Keamanan', 'Bidang pekerjaan terkait dengan peran keamanan'),
      ('Kesehatan', 'Industri yang berfokus pada perawatan medis dan kesehatan, melibatkan dokter, perawat, dan tenaga kesehatan lainnya.'),
      ('Pendidikan', 'Industri yang berkaitan dengan proses pendidikan dan pengajaran, melibatkan guru, profesor, konselor, dan staf pendidikan.'),
      ('Hiburan', 'Industri yang menawarkan hiburan melalui seni pertunjukan, film, dan musik, melibatkan aktor, musisi, produser, dan teknisi hiburan.'),
      ('Perhotelan', 'Industri layanan akomodasi dan kebutuhan tamu, termasuk manajer hotel, staf penerima tamu, koki, pelayan, dan personel layanan lainnya.')
      ;  
  `);
};

exports.down = (pgm) => {
  pgm.dropTable('job_fields');
};
