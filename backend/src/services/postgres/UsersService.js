const { Pool } = require('pg');
const { nanoid } = require('nanoid');
const bcrypt = require('bcrypt');
const InvariantError = require('../../exceptions/InvariantError');
const NotFoundError = require('../../exceptions/NotFoundError');
const AuthenticationError = require('../../exceptions/AuthenticationError');

class UsersService {
  constructor() {
    this._pool = new Pool();
  }

  async verifyNewEmail(email) {
    const query = {
      text: 'SELECT email FROM users WHERE email = $1',
      values: [email],
    };

    const result = await this._pool.query(query);

    if (result.rows.length > 0) {
      throw new InvariantError('Gagal menambahkan user. Email sudah digunakan.');
    }
  }

  async verifyUserId({ userId }) {
    const query = {
      text: 'SELECT id FROM users WHERE id = $1',
      values: [userId],
    };

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new NotFoundError('User tidak ditemukan');
    }
  }

  async verifyUserHaveOnlyOneIdentity({ userId }) {
    const query = {
      text: 'SELECT id FROM user_identities WHERE user_id = $1',
      values: [userId],
    };

    const result = await this._pool.query(query);

    if (result.rows.length) {
      throw new InvariantError('User hanya boleh memiliki satu identitas');
    }
  }

  async addUser({
    firstName,
    lastName,
    email,
    password,
  }) {
    await this.verifyNewEmail(email);
    const id = `user-${nanoid(16)}`;
    const hashedPassword = await bcrypt.hash(password, 10);
    const query = {
      text: 'INSERT INTO users VALUES($1, $2, $3, $4, $5) RETURNING id',
      values: [id, firstName, lastName, email, hashedPassword],
    };
    const result = await this._pool.query(query);
    if (!result.rows[0].id) {
      throw new InvariantError('User gagal ditambahkan');
    }
    return result.rows[0].id;
  }

  async getUserById(userId) {
    const query = {
      text: 'SELECT * FROM users WHERE id = $1',
      values: [userId],
    };
    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new NotFoundError('User tidak ditemukan');
    }

    return result.rows[0];
  }

  async verifyUserCredential(email, password) {
    const query = {
      text: 'SELECT id, password FROM users WHERE email = $1',
      values: [email],
    };

    const result = await this._pool.query(query);
    if (!result.rows.length) {
      throw new AuthenticationError('Kredensial yang Anda berikan salah');
    }

    const { id, password: hashedPassword } = result.rows[0];
    const match = await bcrypt.compare(password, hashedPassword);
    if (!match) {
      throw new AuthenticationError('Kredensial yang Anda berikan salah');
    }

    return id;
  }

  async verifyUserCredentialById(id, password) {
    const query = {
      text: 'SELECT password FROM users WHERE id = $1',
      values: [id],
    };

    const result = await this._pool.query(query);
    if (!result.rows.length) {
      throw new AuthenticationError('Kredensial yang Anda berikan salah');
    }

    const { password: hashedPassword } = result.rows[0];
    const match = await bcrypt.compare(password, hashedPassword);
    if (!match) {
      throw new AuthenticationError('Kredensial yang Anda berikan salah');
    }

    return id;
  }

  async editUserPassword(id, password) {
    const hashedPassword = await bcrypt.hash(password, 10);
    const query = {
      text: 'UPDATE users SET password = $1 WHERE id = $2',
      values: [hashedPassword, id],
    };
    const result = await this._pool.query(query);
    if (!result.rowCount) {
      throw new NotFoundError('Gagal memperbarui password. Id tidak ditemukan');
    }
  }

  async addUserIdentity({
    userId,
    jobPositionId,
    gender,
    dateBirth,
    currentCity,
  }) {
    const query = {
      text: 'INSERT INTO user_identities(user_id, job_position_id, gender, date_birth, current_city) VALUES($1, $2, $3, $4, $5) RETURNING *',
      values: [userId, jobPositionId, gender, dateBirth, currentCity],
    };

    await this.verifyUserId({ userId });

    await this.verifyUserHaveOnlyOneIdentity({ userId });

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new InvariantError('Identity gagal ditambahkan');
    }

    return result.rows[0];
  }

  async getUserIdentity(userId) {
    const query = {
      text: `
        SELECT *, name AS job_position_name FROM user_identities AS ui
        INNER JOIN job_positions AS jp ON ui.job_position_id = jp.id
        WHERE ui.user_id = $1;
      `,
      values: [userId],
    };

    await this.verifyUserId({ userId });

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new NotFoundError('Identity tidak ditemukan');
    }

    return result.rows[0];
  }

  async deleteUserIdentity(userId) {
    const query = {
      text: 'DELETE FROM user_identities WHERE user_id = $1 RETURNING user_id',
      values: [userId],
    };

    await this.verifyUserId({ userId });

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new NotFoundError('Identity gagal dihapus. Id tidak ditemukan');
    }
  }

  async editUserIdentity({
    userId,
    jobPositionId,
    gender,
    dateBirth,
    currentCity,
  }) {
    const query = {
      text: `
        UPDATE user_identities
        SET 
          ${jobPositionId !== undefined ? 'job_position_id = $2,' : ''}
          ${gender !== undefined ? 'gender = $3,' : ''}
          ${dateBirth !== undefined ? 'date_birth = $4,' : ''}
          ${currentCity !== undefined ? 'current_city = $5,' : ''}
          updated_at = NOW()
        WHERE user_id = $1
        RETURNING *;
        `,
      values: [
        userId,
        jobPositionId,
        gender,
        dateBirth,
        currentCity,
      ].filter((val) => val !== undefined),
    };

    await this.verifyUserId({ userId });

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new NotFoundError('Identity gagal diperbarui. Id tidak ditemukan');
    }

    return result.rows[0];
  }
}

module.exports = UsersService;
