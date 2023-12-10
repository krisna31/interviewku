const Joi = require('joi');

const PostChatPayloadSchema = Joi.object({
  question: Joi.string().required(),
}).label('Post Chat Payload');

const PostChatResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Chat berhasil dijawab').required(),
  data: Joi.object().keys({
    id: Joi.number().max(2147483647).default(1).required(),
    question: Joi.string().default('Apa itu interviewku?').required(),
    answer: Joi.string().default('Interviewku adalah platform untuk membantu kamu mempersiapkan interview').required(),
    createdAt: Joi.date().required(),
    updatedAt: Joi.date().allow(null),
  })
    .label('Chat')
    .required(),
}).label('Post Chat Response');

const GetArticlesResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Semua Articles berhasil ditemukan').required(),
  meta: Joi.object().keys({
    count: Joi.number().default(1).required(),
    currentPage: Joi.number().default(1).required(),
    totalData: Joi.number().default(1).required(),
    nextUrl: Joi.string().allow(null),
    previousUrl: Joi.string().allow(null),
    firstPageUrl: Joi.string().required(),
    lastPageUrl: Joi.string().required(),
    limit: Joi.number().default(10).required(),
  })
    .label('Meta')
    .required(),
  data: Joi.array().items(Joi.object().keys({
    id: Joi.number().max(2147483647).default(1).required(),
    title: Joi.string().default('Tips and Tricks for Interviews').required(),
    subtitle: Joi.string().default('Mastering the Interview Process').required(),
    author: Joi.string().default('interviewku team').required(),
    source: Joi.string().default('https://interviewku.tech').required(),
    content: Joi.string().default('# Tips and Tricks for Interviews\n\nYour article content goes here.').required(),
    coverImgUrl: Joi.string().default('url_to_cover_image').required(),
    createdAt: Joi.date().required(),
    updatedAt: Joi.date().allow(null),
  })
    .label('Articles'))
    .required(),
}).label('Get Articles Field Response');

const GetArticleResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Article berhasil ditemukan').required(),
  data: Joi.object().keys({
    id: Joi.number().max(2147483647).default(1).required(),
    title: Joi.string().default('Tips and Tricks for Interviews').required(),
    author: Joi.string().default('interviewku team').required(),
    source: Joi.string().default('https://interviewku.tech').required(),
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
  PostChatPayloadSchema,
  GetArticlesResponseSchema,
  PostChatResponseSchema,
  GetArticleResponseSchema,
};
