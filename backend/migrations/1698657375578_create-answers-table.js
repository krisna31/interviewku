/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('answers', {
    id: 'id',
    question_id: {
      type: 'integer',
      notNull: true,
      references: '"questions"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    answer: {
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
  pgm.dropTable('answers');
};
