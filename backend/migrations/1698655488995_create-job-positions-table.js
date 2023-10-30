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
      notNull: true,
      default: pgm.func('current_timestamp'),
    },
  });
};

exports.down = (pgm) => {
  pgm.dropTable('job_positions');
};
