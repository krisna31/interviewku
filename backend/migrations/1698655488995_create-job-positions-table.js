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
    INSERT INTO job_positions(job_field_id, name) 
      VALUES
        (1, 'General'),
        (2, 'Barista'),
        (2, 'Crew Outlet'),
        (2, 'Waiter/Waitress'),
        (2, 'Cook'),
        (2, 'Cook Helper'),
        (2, 'Kitchen'),
        (2, 'Kitchen Crew'),
        (2, 'Bartender'),
        (3, 'Sales Executive'),
        (3, 'Marketing'),
        (3, 'Sales'),
        (3, 'Sales Marketing'),
        (3, 'Account Executive'),
        (3, 'Sales Manager'),
        (3, 'Sales Engineer'),
        (3, 'Marketing Executive'),
        (3, 'Sales Supervisor'),
        (3, 'Sales Representative'),
        (3, 'Business Development'),
        (3, 'Sales Counter'),
        (4, 'Customer Service'),
        (4, 'Pramuniaga'),
        (4, 'Staff Toko'),
        (4, 'Staff Laundry'),
        (4, 'Receptionist'),
        (4, 'Telemarketing'),
        (4, 'Host Live Streaming'),
        (5, 'Kasir'),
        (5, 'Accounting Staff'),
        (5, 'Staff Accounting'),
        (5, 'Accounting'),
        (5, 'Finance Staff'),
        (6, 'Digital Marketing'),
        (6, 'Social Media Specialist'),
        (6, 'Graphic Designer'),
        (7, 'Admin'),
        (7, 'Content Creator'),
        (7, 'Staff Administrasi'),
        (7, 'Project Manager'),
        (8, 'Teknisi'),
        (8, 'Server'),
        (8, 'Web Developer'),
        (8, 'Android Developer'),
        (8, 'Game Developer'),
        (9, 'Driver'),
        (9, 'Kurir'),
        (9, 'Field Collection')
        ;
  `);
};

exports.down = (pgm) => {
  pgm.dropTable('job_positions');
};
