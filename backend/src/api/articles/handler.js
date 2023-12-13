/* eslint-disable no-unused-vars */
class ArticlesHandler {
  constructor({ articlesService }) {
    this._articlesService = articlesService;
  }

  async getAllArticlesHandler(request, h) {
    // get page and limit
    const { page = 1, limit = 10 } = request.query;
    const startIndex = (page - 1) * limit;
    const endIndex = page * limit;

    // get total data
    const totalData = await this._articlesService.getTotalData();

    // get all articles
    const articles = await this._articlesService.getAllArticles({
      limit,
      offset: startIndex,
    });

    const baseUrl = process.env.APP_BASE_URL;

    return {
      success: true,
      message: 'Articles berhasil ditemukan',
      meta: {
        count: articles.length || 0,
        currentPage: +page,
        totalData: +totalData || 0,
        nextUrl: endIndex < totalData ? `${baseUrl}/articles?page=${+page + 1}&limit=${limit}` : null,
        previousUrl: startIndex > 0 ? `${baseUrl}/articles?page=${+page - 1}&limit=${limit}` : null,
        firstPageUrl: `${baseUrl}/articles?page=1&limit=${limit}`,
        lastPageUrl: `${baseUrl}/articles?page=${Math.ceil(totalData / limit)}&limit=${limit}`,
        limit: +limit,
      },
      data: articles,
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
