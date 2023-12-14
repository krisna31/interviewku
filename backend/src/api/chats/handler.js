/* eslint-disable no-unused-vars */
class ChatsHandler {
  constructor({ chatsService, machineLearningService }) {
    this._chatsService = chatsService;
    this._machineLearningService = machineLearningService;
  }

  async postChatHandler(request, h) {
    const { id: userId } = request.auth.credentials;
    const { question } = request.payload;

    const answer = await this._machineLearningService.getAnswerFromQuestion(question);

    const chatData = await this._chatsService.addChat({ userId, question, answer });

    return h.response({
      success: true,
      message: 'Chat berhasil dijawab',
      data: {
        id: chatData.id,
        question: chatData.question,
        answer: chatData.answer,
        createdAt: chatData.created_at,
        updatedAt: chatData.updated_at,
      },
    }).code(201);
  }

  async getAllChatsHandler(request, h) {
    // get userId from auth
    const { id: userId } = request.auth.credentials;

    // get page and limit
    const { page = 1, limit = 10 } = request.query;
    const startIndex = (page - 1) * limit;
    const endIndex = page * limit;

    // get total data
    const totalData = await this._chatsService.getTotalData();

    // get all chats
    const chats = await this._chatsService.getAllChats({
      limit,
      offset: startIndex,
      userId,
    });

    const baseUrl = process.env.APP_BASE_URL;

    return {
      success: true,
      message: 'Chats berhasil ditemukan',
      meta: {
        count: chats.length || 0,
        currentPage: +page,
        totalData: +totalData || 0,
        nextUrl: endIndex < totalData ? `${baseUrl}/chats?page=${+page + 1}&limit=${limit}` : null,
        previousUrl: startIndex > 0 ? `${baseUrl}/chats?page=${+page - 1}&limit=${limit}` : null,
        firstPageUrl: `${baseUrl}/chats?page=1&limit=${limit}`,
        lastPageUrl: `${baseUrl}/chats?page=${Math.ceil(totalData / limit)}&limit=${limit}`,
        limit: +limit,
      },
      data: chats.map((chat) => ({
        id: chat.id,
        userId: chat.userId,
        question: chat.question,
        answer: chat.answer,
        createdAt: chat.created_at,
        updatedAt: chat.updated_at,
      })),
    };
  }

  async getChatById(request, h) {
    const { chatId } = request.params;
    const { id: userId } = request.auth.credentials;
    const chat = await this._chatsService.getChatById(chatId, userId);

    return {
      success: true,
      message: 'Chat berhasil ditemukan',
      data: {
        id: chat.id,
        userId: chat.userId,
        question: chat.question,
        answer: chat.answer,
        createdAt: chat.created_at,
        updatedAt: chat.updated_at,
      },
    };
  }
}

module.exports = ChatsHandler;
