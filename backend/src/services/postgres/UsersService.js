/* eslint-disable class-methods-use-this */
/* eslint-disable no-plusplus */
const { Pool } = require('pg');
const { nanoid } = require('nanoid');
const bcrypt = require('bcrypt');
const InvariantError = require('../../exceptions/InvariantError');
const NotFoundError = require('../../exceptions/NotFoundError');
const AuthenticationError = require('../../exceptions/AuthenticationError');
const { dateFromDBToRightFormatDate, utcToLocalTimeZone } = require('../../utils');

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

    result.rows[0].created_at = utcToLocalTimeZone(result.rows[0].created_at);
    result.rows[0].updated_at = utcToLocalTimeZone(result.rows[0].updated_at);

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

  async editUser({
    firstName,
    lastName,
    email = undefined,
    password = undefined,
    id,
  }) {
    const query = {
      text: this.buildQueryForEditUserBasicData(firstName, lastName, email, password),
      values: [
        id,
        firstName,
        lastName,
        email,
        password,
      ].filter((value) => value !== undefined),
    };

    console.log(query);

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new NotFoundError('User gagal diperbarui. Id tidak ditemukan');
    }

    result.rows[0].created_at = utcToLocalTimeZone(result.rows[0].created_at);
    result.rows[0].updated_at = utcToLocalTimeZone(result.rows[0].updated_at);

    return result.rows[0];
  }

  buildQueryForEditUserBasicData(firstName, lastName, email, password) {
    let index = 2;
    let queryText = 'UPDATE users SET';

    if (firstName !== undefined) queryText += ` first_name = $${index++},`;
    if (lastName !== undefined) queryText += ` last_name = $${index++},`;
    if (email !== undefined) queryText += ` email = $${index++},`;
    if (password !== undefined) queryText += ` password = $${index++},`;

    queryText += ' updated_at = NOW() WHERE id = $1 RETURNING *;';
    return queryText;
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

    result.rows[0].date_birth = dateFromDBToRightFormatDate(result.rows[0].date_birth);

    result.rows[0].created_at = utcToLocalTimeZone(result.rows[0].created_at);
    result.rows[0].updated_at = utcToLocalTimeZone(result.rows[0].updated_at);

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

    result.rows[0].date_birth = dateFromDBToRightFormatDate(result.rows[0].date_birth);

    result.rows[0].created_at = utcToLocalTimeZone(result.rows[0].created_at);
    result.rows[0].updated_at = utcToLocalTimeZone(result.rows[0].updated_at);

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
    firstName,
    lastName,
    userId,
    jobPositionId,
    gender,
    dateBirth,
    currentCity,
  }) {
    const query = {
      text: this.buildQueryForEditIdentity(jobPositionId, gender, dateBirth, currentCity),
      values: [
        userId,
        jobPositionId,
        gender,
        dateBirth,
        currentCity,
      ].filter((value) => value !== undefined),
    };

    await this.verifyUserId({ userId });

    const result = await this._pool.query(query);

    const user = await this.editUser({
      firstName,
      lastName,
      id: userId,
    });

    if (!result.rows.length) {
      throw new NotFoundError('Identity gagal diperbarui. Id tidak ditemukan');
    }

    result.rows[0].first_name = user.first_name;
    result.rows[0].last_name = user.last_name;

    result.rows[0].date_birth = dateFromDBToRightFormatDate(result.rows[0].date_birth);

    result.rows[0].created_at = utcToLocalTimeZone(result.rows[0].created_at);
    result.rows[0].updated_at = utcToLocalTimeZone(result.rows[0].updated_at);

    return result.rows[0];
  }

  buildQueryForEditIdentity(jobPositionId, gender, dateBirth, currentCity) {
    let index = 2;
    let queryText = 'UPDATE user_identities SET';

    if (jobPositionId !== undefined) queryText += ` job_position_id = $${index++},`;
    if (gender !== undefined) queryText += ` gender = $${index++},`;
    if (dateBirth !== undefined) queryText += ` date_birth = $${index++},`;
    if (currentCity !== undefined) queryText += ` current_city = $${index++},`;

    queryText += ' updated_at = NOW() WHERE user_id = $1 RETURNING *;';
    return queryText;
  }
}

module.exports = UsersService;
