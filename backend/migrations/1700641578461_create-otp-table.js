/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('otps', {
    email: {
      type: 'VARCHAR(50)',
      notNull: true,
      unique: true,
    },
    otp: {
      type: 'char(6)',
      notNull: true,
    },
    expired_at: {
      type: 'timestamp',
      notNull: false,
    },
  });
};

exports.down = (pgm) => {
  pgm.dropTable('otps');
};
