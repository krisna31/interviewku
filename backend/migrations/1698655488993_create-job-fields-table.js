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
    INSERT INTO job_fields(name) 
      VALUES
        ('Bidang General'),
        ('Bidang Kuliner dan Restoran'),
        ('Bidang Penjualan dan Pemasaran'),
        ('Bidang Layanan Pelanggan'),
        ('Bidang Keuangan dan Akuntansi'),
        ('Bidang Pemasaran Digital dan Desain Grafis'),
        ('Bidang Manajemen Proyek dan Administrasi'),
        ('Bidang Teknis dan IT'),
        ('Bidang Logistik dan Pengiriman')
        ;
  `);
};

exports.down = (pgm) => {
  pgm.dropTable('job_fields');
};
