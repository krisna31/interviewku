/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('user_identities', {
    id: 'id',
    user_id: {
      type: 'VARCHAR(50)',
      unique: true,
      notNull: true,
      references: '"users"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    job_position_id: {
      type: 'integer',
      notNull: true,
      references: '"job_positions"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    gender: {
      type: 'CHAR(1)',
      notNull: true,
    },
    date_birth: {
      type: 'date',
      notNull: true,
    },
    current_city: {
      type: 'VARCHAR(100)',
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
};

exports.down = (pgm) => {
  pgm.dropTable('user_identities');
};
