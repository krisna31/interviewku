/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('question_answer_histories', {
    id: 'id',
    test_history_id: {
      type: 'integer',
      notNull: true,
      references: '"test_histories"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    job_field: {
      type: 'VARCHAR(50)',
      notNull: true,
    },
    job_position: {
      type: 'VARCHAR(50)',
      notNull: true,
    },
    audio_url: {
      type: 'TEXT',
      notNull: true,
    },
    score: {
      type: 'integer',
      notNull: true,
    },
    duration: {
      type: 'bigint',
      notNull: true,
    },
    retry_attempt: {
      type: 'integer',
      notNull: true,
      default: 0,
    },
    question: {
      type: 'TEXT',
      notNull: true,
    },
    feedback: {
      type: 'TEXT',
      notNull: true,
    },
    user_answer: {
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
      notNull: false,
    },
  });
};

exports.down = (pgm) => {
  pgm.dropTable('question_answer_histories');
};
