/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('questions', {
    id: 'id',
    job_field_id: {
      type: 'integer',
      notNull: true,
      references: '"job_fields"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    question: {
      type: 'TEXT',
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
  pgm.dropTable('questions');
};
