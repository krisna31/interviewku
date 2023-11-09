/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('articles', {
    id: 'id',
    title: {
      type: 'TEXT',
      notNull: true,
    },
    subtitle: {
      type: 'TEXT',
      notNull: true,
    },
    author: {
      type: 'VARCHAR(50)',
      notNull: true,
    },
    cover_img_url: {
      type: 'TEXT',
      notNull: true,
    },
    content: {
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
  pgm.dropTable('articles');
};
