const nodemailer = require('nodemailer');

class MailService {
  constructor() {
    this._transporter = nodemailer.createTransport({
      host: process.env.SMTP_HOST,
      port: process.env.SMTP_PORT,
      auth: {
        user: process.env.SMTP_USER,
        pass: process.env.SMTP_PASSWORD,
      },
    });
  }

  sendEmail(targetEmail, content) {
    if (process.env.MAIL_SERVICE === 'false') {
      console.info(`Email sent to: ${targetEmail} without mail service`);
      return;
    }
    const message = {
      from: 'interviewku-reset-password@interviewku.tech',
      // from: 'interviewku',
      to: targetEmail,
      subject: 'Reset Password Interviewku',
      html: content,
    };

    return this._transporter.sendMail(message);
  }
}

module.exports = MailService;
