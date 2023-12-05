/* eslint-disable no-unused-vars */
class ArticlesHandler {
  constructor({ articlesService }) {
    this._articlesService = articlesService;
  }

  async getAllArticlesHandler(request, h) {
    const articles = await this._articlesService.getAllArticles();

    return {
      success: true,
      message: 'Semua Articles berhasil ditemukan',
      data: {
        articles,
      },
    };
  }

  async getArticleById(request, h) {
    const { articleId } = request.params;
    const article = await this._articlesService.getArticleById(articleId);

    return {
      success: true,
      message: 'Article berhasil ditemukan',
      data: {
        ...article,
      },
    };
  }
}

module.exports = ArticlesHandler;
