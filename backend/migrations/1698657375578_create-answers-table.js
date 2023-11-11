/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('answers', {
    id: 'id',
    question_id: {
      type: 'integer',
      notNull: true,
      references: '"questions"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    answer: {
      type: 'TEXT',
      notNull: true,
    },
    created_at: {
      type: 'timestamp',
      notNull: true,
      default: pgm.func('current_timestamp'),
    },
    updated_at: {
      type: 'timestamp',
      notNull: false,
    },
  });

  pgm.sql(`
    INSERT INTO answers (question_id, answer) VALUES
      (1, 'Konsep kuliner yang unik bagi saya melibatkan eksplorasi dan penciptaan cita rasa yang belum pernah ditemui sebelumnya. Ini melibatkan penggabungan bahan-bahan, teknik memasak yang inovatif, dan presentasi yang menarik.'),
      (1, 'Kuliner yang unik adalah tentang menciptakan pengalaman makan yang tak terlupakan. Ini mencakup harmoni antara cita rasa, tampilan visual, dan interaksi dengan tamu.'),
      (1, 'Konsep kuliner yang unik adalah tentang memadukan elemen-elemen tradisional dengan elemen-elemen modern untuk menciptakan sesuatu yang segar dan tak terduga. Ini bisa berarti menciptakan menu yang menggabungkan budaya kuliner berbeda.'),
      (1, 'Saya mendefinisikan konsep kuliner yang unik sebagai kemampuan untuk menghadirkan rasa dan estetika yang luar biasa dalam hidangan, sambil mempertahankan akar dan identitas kuliner yang kuat.'),
      (1, 'Bagi saya, kuliner yang unik adalah tentang berani bereksperimen dengan bahan-bahan dan teknik yang tidak konvensional. Ini melibatkan berpikir di luar kotak dan menciptakan pengalaman makan yang tidak terduga.'),
      (1, 'Konsep kuliner yang unik adalah tentang menceritakan cerita melalui hidangan. Ini mencakup menggambarkan sejarah, budaya, dan kreativitas dalam setiap piring, sehingga tamu merasa terlibat dan terhubung dengan makanan.'),
      (1, 'Saya mendefinisikan kuliner yang unik sebagai kombinasi antara seni memasak dan seni visual. Ini melibatkan presentasi yang estetis, penyajian yang kreatif, dan kemampuan untuk menggugah selera dan mata tamu.'),
      (1, 'Kuliner yang unik bagi saya adalah tentang mengekspresikan identitas kreatif koki melalui hidangan. Ini mencakup kreativitas dalam memilih bahan, merancang menu, dan menciptakan pengalaman makan yang tak tertandingi.'),
      (1, 'Konsep kuliner yang unik adalah tentang menciptakan hubungan emosional dengan makanan. Ini mencakup memahami preferensi dan ekspektasi tamu, dan menghadirkan sesuatu yang melebihi harapan mereka.'),
      (1, 'Bagi saya, kuliner yang unik adalah tentang menjelajahi perbatasan rasa dan tekstur. Ini melibatkan menciptakan hidangan yang menggabungkan kontras dalam elemen-elemen seperti manis dan asin, renyah dan lembut.'),
      (1, 'Saya mendefinisikan kuliner yang unik sebagai penggunaan bahan-bahan lokal yang langka dan eksotis untuk menciptakan hidangan yang belum pernah ada sebelumnya. Ini mencakup mendukung petani dan produsen lokal.'),
      (1, 'Konsep kuliner yang unik adalah tentang menghidupkan kembali hidangan tradisional dengan sentuhan modern. Ini mencakup penggunaan teknik memasak baru untuk menghadirkan warisan kuliner dalam tampilan yang segar.'),
      (1, 'Kuliner yang unik adalah tentang berfokus pada rasa unik dan kualitas bahan-bahan. Ini melibatkan pencarian bahan-bahan premium dan langka serta pemahaman mendalam tentang cara memaksimalkan rasa alami.'),
      (1, 'Saya mendefinisikan konsep kuliner yang unik sebagai kesediaan untuk menggabungkan unsur-unsur non-tradisional seperti seni, musik, dan desain dalam pengalaman makan. Ini menciptakan pengalaman yang holistik.'),
      (1, 'Bagi saya, kuliner yang unik adalah tentang berkolaborasi dengan rekan seniman kuliner dan menciptakan hidangan yang tidak hanya menggugah selera, tetapi juga menginspirasi dan mengejutkan.'),
      (1, 'Konsep kuliner yang unik adalah tentang berfokus pada pemeliharaan lingkungan dan etika berkelanjutan. Ini mencakup penggunaan bahan-bahan yang ramah lingkungan dan praktik memasak yang berkelanjutan.'),
      (1, 'Saya melihat kuliner yang unik sebagai pengembangan konsep kuliner khas yang mencerminkan kepribadian koki dan nilai-nilai yang dipegang oleh restoran atau tempat makan. Ini mencakup kesinambungan dan konsistensi dalam menyajikan citra kuliner yang unik.'),
      (1, 'Kuliner yang unik adalah tentang menjadikan pengalaman makan sebagai perjalanan yang tak terlupakan. Ini mencakup menciptakan setiap hidangan sebagai bagian dari narasi kuliner yang lebih besar.'),
      (1, 'Konsep kuliner yang unik adalah tentang mengejar kesempurnaan dalam setiap aspek hidangan, termasuk rasa, tampilan, dan pelayanan. Ini mencakup dedikasi untuk memberikan pengalaman makan yang tak tertandingi.'),
      (1, 'Bagi saya, kuliner yang unik adalah tentang keberanian untuk berinovasi dan tidak pernah puas dengan status quo. Ini mencakup mencari inspirasi dari berbagai budaya kuliner dan merangkul keberagaman rasa.')
      ;
  `)
};

exports.down = (pgm) => {
  pgm.dropTable('answers');
};
