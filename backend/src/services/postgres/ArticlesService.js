const { Pool } = require('pg');
const InvariantError = require('../../exceptions/InvariantError');

class ArticlesService {
  constructor() {
    this._pool = new Pool();
  }

  async getAllArticles({ limit = 10, offset = 0 }) {
    const query = {
      text: 'SELECT * FROM articles ORDER BY created_at DESC LIMIT $1 OFFSET $2',
      values: [limit, offset],
    };

    const result = await this._pool.query(query);

    return result.rows.map((article) => ({
      id: article.id,
      title: article.title,
      subtitle: article.subtitle,
      content: article.content,
      author: article.author,
      source: article.source,
      coverImgUrl: article.cover_img_url,
      createdAt: article.created_at,
      updatedAt: article.updated_at,
    }));
  }

  async getTotalData() {
    const query = {
      text: 'SELECT COUNT(*) FROM articles',
    };

    const result = await this._pool.query(query);

    return result.rows[0].count;
  }

  async getArticleById(articleId) {
    const query = {
      text: 'SELECT * FROM articles WHERE id = $1',
      values: [articleId],
    };

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new InvariantError('Article tidak valid');
    }

    return result.rows.map((article) => ({
      id: article.id,
      title: article.title,
      subtitle: article.subtitle,
      author: article.author,
      source: article.source,
      coverImgUrl: article.cover_img_url,
      content: article.content,
      createdAt: article.created_at,
      updatedAt: article.updated_at,
    }))[0];
  }
}

module.exports = ArticlesService;
