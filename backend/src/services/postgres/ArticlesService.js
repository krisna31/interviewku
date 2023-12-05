const { Pool } = require('pg');
const InvariantError = require('../../exceptions/InvariantError');

class ArticlesService {
  constructor() {
    this._pool = new Pool();
  }

  async getAllArticles() {
    const query = {
      text: 'SELECT * FROM articles',
    };

    const result = await this._pool.query(query);

    return result.rows.map((article) => ({
      id: article.id,
      title: article.title,
      subtitle: article.subtitle,
      content: article.content,
      coverImgUrl: article.cover_img_url,
      // createdAt: article.created_at,
      // updatedAt: article.updated_at,
    }));
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
      coverImgUrl: article.cover_img_url,
      content: article.content,
      createdAt: article.created_at,
      updatedAt: article.updated_at,
    }))[0];
  }
}

module.exports = ArticlesService;
