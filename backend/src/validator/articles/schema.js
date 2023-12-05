const Joi = require('joi');

const GetArticlesResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Semua Articles berhasil ditemukan').required(),
  data: Joi.object().keys({
    articles: Joi.array().items(Joi.object().keys({
      id: Joi.number().max(2147483647).default(1).required(),
      title: Joi.string().default('Tips and Tricks for Interviews').required(),
      subtitle: Joi.string().default('Mastering the Interview Process').required(),
      content: Joi.string().default('# Tips and Tricks for Interviews\n\nYour article content goes here.').required(),
      coverImgUrl: Joi.string().default('url_to_cover_image').required(),
      // createdAt: Joi.date().required(),
      // updatedAt: Joi.date().allow(null),
    })
      .label('Articles'))
      .required(),
  }),
}).label('Get Articles Field Response');

const GetArticleResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Article berhasil ditemukan').required(),
  data: Joi.object().keys({
    id: Joi.number().max(2147483647).default(1).required(),
    title: Joi.string().default('Tips and Tricks for Interviews').required(),
    author: Joi.string().default('interviewku team').required(),
    subtitle: Joi.string().default('Mastering the Interview Process').required(),
    content: Joi.string().default('# Tips and Tricks for Interviews\n\nYour article content goes here.').required(),
    coverImgUrl: Joi.string().default('url_to_cover_image').required(),
    createdAt: Joi.date().required(),
    updatedAt: Joi.date().allow(null),
  })
    .label('Articles')
    .required(),
}).label('Get Jobs Position Response');

module.exports = {
  GetArticlesResponseSchema,
  GetArticleResponseSchema,
};
