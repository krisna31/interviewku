/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('job_positions', {
    id: 'id',
    job_field_id: {
      type: 'integer',
      notNull: true,
      references: '"job_fields"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
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
    INSERT INTO job_positions(job_field_id, name, description) 
    VALUES
      (1, 'General', 'An all-encompassing role that involves various responsibilities within an organization.'),
      (2, 'Barista', 'Responsible for preparing and serving coffee and beverages.'),
      (2, 'Crew Outlet', 'Involved in daily operations and customer service in the outlet.'),
      (2, 'Waiter/Waitress', 'Responsible for taking orders and serving customers.'),
      (2, 'Cook', 'Responsible for food preparation and cooking.'),
      (2, 'Cook Helper', 'Assists the cook in food preparation.'),
      (2, 'Kitchen', 'Involved in kitchen operations and food preparation.'),
      (2, 'Kitchen Crew', 'Part of the kitchen team responsible for various tasks.'),
      (2, 'Bartender', 'Prepares and serves beverages, often alcoholic drinks.'),
      (3, 'Sales Executive', 'Responsible for sales strategies and client interactions.'),
      (3, 'Marketing', 'Involved in planning and executing marketing campaigns.'),
      (3, 'Sales', 'Responsible for selling products or services.'),
      (3, 'Sales Marketing', 'Combines sales and marketing responsibilities.'),
      (3, 'Account Executive', 'Manages client accounts and relationships.'),
      (3, 'Sales Manager', 'Oversees and manages a sales team.'),
      (3, 'Sales Engineer', 'Provides technical support for sales activities.'),
      (3, 'Marketing Executive', 'Executes marketing plans and strategies.'),
      (3, 'Sales Supervisor', 'Supervises and coordinates sales activities.'),
      (3, 'Sales Representative', 'Represents a company’s products or services to customers.'),
      (3, 'Business Development', 'Focuses on developing and expanding business opportunities.'),
      (3, 'Sales Counter', 'Handles sales transactions at a counter.'),
      (4, 'Customer Service', 'Provides assistance and resolves customer inquiries or issues.'),
      (4, 'Pramuniaga', 'Memberikan bantuan dan menyelesaikan pertanyaan atau masalah pelanggan.'),
      (4, 'Staff Toko', 'Bertanggung jawab atas operasi harian toko.'),
      (4, 'Staff Laundry', 'Bertanggung jawab atas layanan pencucian.'),
      (4, 'Receptionist', 'Responsible for front desk operations and customer inquiries.'),
      (4, 'Telemarketing', 'Conducts marketing activities over the phone.'),
      (4, 'Host Live Streaming', 'Conducts live streaming sessions as a host.'),
      (5, 'Kasir', 'Bertanggung jawab atas transaksi kas di toko atau restoran.'),
      (5, 'Accounting Staff', 'Assists in accounting tasks and financial record-keeping.'),
      (5, 'Staff Accounting', 'Involved in various accounting responsibilities.'),
      (5, 'Accounting', 'Handles financial and accounting tasks.'),
      (5, 'Finance Staff', 'Assists in financial tasks within an organization.'),
      (6, 'Digital Marketing', 'Executes marketing strategies in digital channels.'),
      (6, 'Social Media Specialist', 'Specializes in managing and creating content for social media.'),
      (6, 'Graphic Designer', 'Creates visual content and designs.'),
      (7, 'Admin', 'Handles administrative tasks within an organization.'),
      (7, 'Content Creator', 'Creates various forms of content for online platforms.'),
      (7, 'Staff Administrasi', 'Bertanggung jawab atas tugas administratif.'),
      (7, 'Project Manager', 'Manages and oversees projects within an organization.'),
      (8, 'Teknisi', 'Handles technical tasks and troubleshoots technical issues.'),
      (8, 'Server', 'Manages server-related tasks and operations.'),
      (8, 'Web Developer', 'Develops and maintains websites.'),
      (8, 'Android Developer', 'Specializes in developing Android applications.'),
      (8, 'Game Developer', 'Develops and designs games.'),
      (9, 'Driver', 'Drives and transports goods or individuals.'),
      (9, 'Kurir', 'Bertanggung jawab atas pengantaran barang.'),
      (9, 'Field Collection', 'Collects payments or documents in the field.'),
      (10, 'Security', 'Responsible for ensuring the security and safety of an area or establishment.')
      ;
  `);
};

exports.down = (pgm) => {
  pgm.dropTable('job_positions');
};
