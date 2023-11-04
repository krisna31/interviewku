/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('test_histories', {
    id: 'id',
    user_id: {
      type: 'VARCHAR(50)',
      unique: true,
      notNull: true,
      references: '"users"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    audio_url: {
      type: 'TEXT',
      notNull: true,
    },
    mode: {
      type: 'VARCHAR(50)',
      notNull: true,
    },
    job_field: {
      type: 'VARCHAR(50)',
      notNull: true,
    },
    job_position: {
      type: 'VARCHAR(50)',
      notNull: true,
    },
    duration: {
      type: 'bigint',
      notNull: true,
    },
    status: {
      type: 'boolean',
      notNull: true,
    },
    feedback: {
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
  pgm.dropTable('test_histories');
};
