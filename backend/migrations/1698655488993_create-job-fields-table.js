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
      ('General', 'General job field description'),
      ('Kuliner dan Restoran', 'Job field related to culinary and restaurant management'),
      ('Penjualan dan Pemasaran', 'Job field focusing on sales and marketing roles'),
      ('Layanan Pelanggan', 'Job field involving customer service roles'),
      ('Keuangan dan Akuntansi', 'Job field related to finance and accounting'),
      ('Pemasaran Digital dan Desain Grafis', 'Job field combining digital marketing and graphic design'),
      ('Manajemen Proyek dan Administrasi', 'Job field involving project management and administrative roles'),
      ('Teknis dan IT', 'Job field related to technical and IT positions'),
      ('Logistik dan Pengiriman', 'Job field involving logistics and shipping roles'),
      ('Keamanan', 'Job field related to security roles')
      ;  
  `);
};

exports.down = (pgm) => {
  pgm.dropTable('job_fields');
};
