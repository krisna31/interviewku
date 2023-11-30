/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('question_answer_histories', {
    id: {
      type: 'VARCHAR(50)',
      primaryKey: true,
    },
    test_history_id: {
      type: 'VARCHAR(50)',
      notNull: true,
      references: '"test_histories"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    question_order: {
      type: 'integer',
      notNull: true,
    },
    job_field_name: {
      type: 'VARCHAR(50)',
      notNull: false,
    },
    audio_url: {
      type: 'TEXT',
      notNull: false,
    },
    score: {
      type: 'float',
      notNull: false,
    },
    duration: {
      type: 'float',
      notNull: false,
    },
    retry_attempt: {
      type: 'integer',
      notNull: false,
    },
    question: {
      type: 'TEXT',
      notNull: true,
    },
    struktur_score: {
      type: 'float',
      notNull: false,
    },
    user_answer: {
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
};

exports.down = (pgm) => {
  pgm.dropTable('question_answer_histories');
};
