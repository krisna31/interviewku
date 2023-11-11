/* eslint-disable camelcase */

exports.shorthands = undefined;

exports.up = (pgm) => {
  pgm.createTable('questions', {
    id: 'id',
    job_field_id: {
      type: 'integer',
      notNull: true,
      references: '"job_fields"',
      onDelete: 'cascade',
      onUpdate: 'cascade',
    },
    question: {
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
    INSERT INTO questions(job_field_id, question)
    VALUES
      (2, 'Bagaimana Anda mendefinisikan konsep kuliner yang unik?'),
      (2, 'Apa pengalaman Anda dalam merancang dan mengelola menu restoran?'),
      (2, 'Bagaimana Anda mengatasi tantangan dalam pengelolaan stok dan persediaan makanan?'),
      (2, 'Bagaimana Anda memastikan kualitas makanan dan kebersihan di restoran Anda?'),
      (2, 'Bagaimana Anda mendekati pelanggan yang tidak puas dengan pengalaman mereka di restoran?'),
      (2, 'Bagaimana Anda memilih dan berkolaborasi dengan pemasok bahan baku?'),
      (2, 'Bagaimana Anda memastikan kepatuhan dengan standar keamanan pangan dan regulasi terkait?'),
      (2, 'Apa langkah-langkah yang Anda ambil untuk memotivasi dan melatih staf di restoran?'),
      (2, 'Bagaimana Anda menghadapi tren kuliner dan bagaimana Anda berencana untuk tetap relevan di pasar?'),
      (2, 'Bisakah Anda berbagi pengalaman Anda dalam menghadapi situasi darurat atau masalah teknis yang mempengaruhi operasional restoran?'),
      (3, 'Bisakah Anda menceritakan cara Anda dalam mengelola siklus penjualan dari awal hingga penutupan?'),
      (3, 'Bagaimana cara Anda mencari prospek atau pelanggan potensial untuk produk atau layanan yang Anda tawarkan?'),
      (3, 'Bagaimana Anda mengatasi penolakan atau hambatan dalam proses penjualan?'),
      (3, 'Apa metode pemasaran digital atau online yang Anda kuasai, dan bagaimana Anda menggunakannya dalam pekerjaan Anda?'),
      (3, 'Bagaimana Anda mengukur keberhasilan kampanye pemasaran yang Anda luncurkan?'),
      (3, 'Apa strategi Anda dalam membangun dan memelihara hubungan dengan pelanggan atau klien?'),
      (3, 'Bagaimana Anda memahami kebutuhan dan keinginan pelanggan Anda, dan bagaimana Anda menawarkan solusi yang sesuai?'),
      (3, 'Apakah Anda memiliki pengalaman dalam mengelola tim penjualan atau pemasaran? Bagaimana Anda memotivasi tim Anda?'),
      (3, 'Bagaimana Anda memastikan bahwa Anda selalu terhubung dengan tren dan perkembangan terbaru dalam industri pemasaran dan penjualan?'),
      (3, 'Bagaimana Anda mengidentifikasi tren pasar dan persaingan dalam industri yang Anda targetkan?'),
      (4, 'Apa definisi Anda tentang layanan pelanggan yang baik, dan bagaimana Anda menerapkannya dalam pekerjaan Anda?'),
      (4, 'Bisakah Anda memberikan contoh pengalaman layanan pelanggan yang sukses yang Anda alami atau Anda berikan?'),
      (4, 'Bagaimana Anda menangani pelanggan yang tidak puas atau marah, dan bagaimana Anda mencoba untuk memecahkan masalah mereka?'),
      (4, 'Apa langkah-langkah atau proses yang Anda ikuti dalam menyelesaikan permintaan atau masalah pelanggan?'),
      (4, 'Bagaimana Anda berkomunikasi dengan pelanggan, terutama dalam situasi yang memerlukan penjelasan atau pemecahan masalah kompleks?'),
      (4, 'Apakah Anda memiliki pengalaman dalam menggunakan sistem manajemen pelanggan (CRM) atau perangkat lunak sejenisnya? Bagaimana Anda menggunakannya?'),
      (4, 'Bagaimana Anda mengukur keberhasilan dalam memberikan layanan pelanggan? Apa indikator kinerja yang Anda gunakan?'),
      (4, 'Apakah Anda pernah berkontribusi dalam mengembangkan atau meningkatkan proses layanan pelanggan?'),
      (4, 'Bagaimana Anda memastikan konsistensi dalam pelayanan pelanggan, terutama ketika berurusan dengan banyak pelanggan?'),
      (4, 'Apa yang Anda lakukan untuk terus memperbarui pengetahuan Anda tentang produk atau layanan yang Anda dukung untuk memberikan informasi yang akurat dan bermanfaat kepada pelanggan?'),
      (5, 'Apa pengalaman Anda dalam mengelola administrasi dan akuntansi?'),
      (5, 'Bagaimana Anda memastikan keakuratan dan ketepatan waktu dalam proses pencatatan dan pelaporan keuangan?'),
      (5, 'Apa perangkat lunak atau sistem akuntansi yang Anda kuasai dan gunakan dalam pekerjaan Anda?'),
      (5, 'Bagaimana Anda mengatasi situasi di mana terdapat perbedaan atau ketidaksesuaian dalam data keuangan?'),
      (5, 'Apa tanggung jawab Anda dalam mengelola anggaran atau perencanaan keuangan?'),
      (5, 'Apakah Anda memiliki pengalaman dalam menyusun laporan keuangan, neraca, dan laporan laba rugi?'),
      (5, 'Bagaimana Anda menjaga kepatuhan perpajakan dan peraturan akuntansi dalam pekerjaan Anda?'),
      (5, 'Apakah Anda pernah berpartisipasi dalam audit internal atau eksternal? Bagaimana pengalaman Anda?'),
      (5, 'Bagaimana Anda mengorganisir dan mengelola dokumen dan catatan penting dalam pekerjaan administratif Anda?'),
      (5, 'Apa yang Anda lakukan untuk terus memperbarui pengetahuan Anda tentang perkembangan terbaru dalam akuntansi dan administrasi?'),
      (6, 'Bisakah Anda menjelaskan pengalaman Anda dalam desain grafis dan proyek yang telah Anda buat?'),
      (6, 'Apa yang membuat Anda tertarik dalam bidang desain grafis?'),
      (6, 'Bagaimana Anda mengukur keberhasilan desain grafis?'),
      (6, 'Apa alat dan perangkat lunak desain yang paling Anda kuasai?'),
      (6, 'Bagaimana Anda menghadapi tantangan dalam proyek desain grafis?'),
      (6, 'Bagaimana Anda menjaga diri Anda tetap terinspirasi dan kreatif dalam desain?'),
      (6, 'Bagaimana Anda bekerja dalam tim desain grafis?'),
      (6, 'Bisakah Anda memberikan contoh proyek desain yang paling Anda banggakan?'),
      (6, 'Apa tren terbaru dalam desain grafis yang Anda ikuti atau terapkan?'),
      (6, 'Bagaimana Anda mengelola waktu dan deadline dalam proyek desain grafis?'),
      (8, 'Apa yang Anda lakukan ketika menghadapi masalah perangkat keras atau perangkat lunak yang kritis?'),
      (8, 'Apa pendekatan Anda terhadap keamanan data dan perlindungan privasi dalam lingkungan IT?'),
      (8, 'Bagaimana Anda berkomunikasi dengan non-teknis tentang masalah teknis?'),
      (8, 'Apa yang Anda cari dalam pemilihan alat atau teknologi yang tepat untuk suatu proyek?'),
      (8, 'Bagaimana Anda memprioritaskan tugas dan proyek ketika Anda memiliki banyak permintaan yang bersaing?'),
      (8, 'Bagaimana Anda menjaga keterampilan teknis Anda tetap terasah dan relevan dalam industri yang terus berubah?'),
      (8, 'Bagaimana Anda mengelola ekspektasi pelanggan atau pemangku kepentingan dalam proyek IT?'),
      (8, 'Bagaimana jika anda mengalami kegagalan dalam mengerjakan proyek?'),
      (8, 'Bagaimana cara Anda mengatasi bila menerima tugas proyek yang menggunakan teknologi yang Anda tidak kuasai?'),
      (8, 'Bagaimana cara Anda beradaptasi dengan tim baru dalam mengerjakan proyek?')
      ;  
  `)
};

exports.down = (pgm) => {
  pgm.dropTable('questions');
};
