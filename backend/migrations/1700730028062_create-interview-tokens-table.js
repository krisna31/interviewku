/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('interview_tokens', {
    test_histories_id: {
      type: 'VARCHAR(50)',
      notNull: true,
      references: '"test_histories"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
      unique: true,
    },
    token: {
      type: 'char(32)',
      notNull: true,
    },
    expired_at: {
      type: 'timestamp',
      notNull: true,
      default: pgm.func('current_timestamp + interval \'1 day\''),
    },
  });
};

exports.down = (pgm) => {
  pgm.dropTable('interview_tokens');
};
