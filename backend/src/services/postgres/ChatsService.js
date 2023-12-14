const { Pool } = require('pg');
const InvariantError = require('../../exceptions/InvariantError');

class ChatsService {
  constructor() {
    this._pool = new Pool();
  }

  async addChat({ userId, question, answer }) {
    const id = `chat-${userId}-${Date.now()}`;

    const query = {
      text: 'INSERT INTO chat VALUES($1, $2, $3, $4, $5) RETURNING id, question, answer, created_at, updated_at',
      values: [id, userId, question, answer, new Date().toISOString()],
    };

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new InvariantError('Chat gagal ditambahkan');
    }

    return result.rows[0];
  }

  async getAllChats({ limit, offset, userId }) {
    const query = {
      text: 'SELECT chat.id, users.id AS userId, chat.question, chat.answer, chat.created_at, chat.updated_at FROM chat LEFT JOIN users ON users.id = chat.user_id WHERE users.id = $1 ORDER BY chat.created_at DESC LIMIT $2 OFFSET $3',
      values: [userId, limit, offset],
    };

    const result = await this._pool.query(query);

    return result.rows;
  }

  async getTotalData() {
    const query = {
      text: 'SELECT COUNT(*) FROM chat',
    };

    const result = await this._pool.query(query);

    return result.rows[0].count;
  }

  async getChatById(chatId, userId) {
    const query = {
      text: 'SELECT chat.id, users.id AS userId, chat.question, chat.answer, chat.created_at, chat.updated_at FROM chat LEFT JOIN users ON users.id = chat.user_id WHERE chat.id = $1 AND users.id = $2',
      values: [chatId, userId],
    };

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new InvariantError('Chat tidak ditemukan');
    }

    return result.rows[0];
  }
}

module.exports = ChatsService;
