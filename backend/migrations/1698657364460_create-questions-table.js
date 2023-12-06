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
      unique: true,
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
      (1, 'Bisakah Anda menjelaskan apa yang membuat Anda tertarik untuk bekerja di perusahaan ini?'),
      (1, 'Apa yang Anda ketahui tentang perusahaan ini?'),
      (1, 'Kenapa anda memilih perusahaan ini daripada perusahaan lainnya?'),
      
      (2, 'Apa proyek atau pencapaian terbesar dalam karier Anda, dan apa berkontribusi Anda dalam proyek tersebut?'),
      (2, 'Bisakah Anda berbagi contoh tentang bagaimana Anda meningkatkan efisiensi atau produktivitas dalam project Anda sebelumnya?'),
      (2, 'Apakah Anda memiliki keterampilan khusus atau sertifikasi yang relevan dengan pekerjaan ini?'),
      (2, 'Apakah Anda memiliki pengalaman bekerja di dalam tim? Kontribusi apa yang anda berikan?'),

      (3, 'Mengapa anda melamar di posisi ini?'),
      (3, 'Apa yang anda ketahui mengenai posisi ini?'),
      (3, 'Berdasarkan yang anda ketahui, tugas apa saja yang dilakukan pada posisi ini?'),
      (3, 'Apa yang membuat Anda yakin bahwa posisi ini adalah langkah yang tepat dalam perkembangan karier Anda?'),
      (3, 'Apa yang Anda dapat pelajari dan kembangkan melalui posisi ini?'),

      (4, 'Bagaimana Anda mengatasi tantangan yang mungkin muncul di lingkungan kerja perusahaan ini?'),
      (4, 'Bagaimana Anda menghadapi situasi konflik atau perbedaan pendapat di lingkungan kerja?'),
      (4, 'Jika anda mendapatkan masalah apa yang anda lakukan?'),
      (4, 'Bisakah Anda memberikan contoh situasi di mana Anda menghadapi tantangan besar dan bagaimana Anda mengatasinya?'),
      (4, 'Bagaimana anda merencanakan agar masalah yang sama tidak akan terulang lagi dan masalah baru tidak akan timbul?'),
      (4, 'Apa pendekatan Anda dalam berkolaborasi dengan tim dan rekan kerja?'),

      (5, 'Bagaimana Anda berpikir kontribusi Anda akan mendukung tujuan dan pertumbuhan perusahaan?'),
      (5, 'Bagaimana Anda mengukur kesuksesan Anda di tempat kerja dan bagaimana Anda berencana untuk mencapainya di perusahaan ini?'),
      (5, 'Apakah anda merasa cocok dengan pekerjaan ini?'),
      (5, 'Apa keinginan anda dimasa yang akan datang setelah bergabung di perusahaan ini?'),
      (5, 'Jika anda diterima pada posisi yang anda inginkan, Hal apa yang akan anda lakukan untuk kedepannya?'),
      (5, 'Apa yang Anda harapkan dan dapat Anda kontribusikan kepada perusahaan ini?'),
      (5, 'Bagaimana Anda menjaga keseimbangan antara pekerjaan dan kehidupan pribadi?'),
      (5, 'Bagaimana Anda mengatasi kelelahan atau kejenuhan dalam pekerjaan?'),

      (6, 'Bagaimana Anda mendefinisikan konsep kuliner yang unik?'),
      (6, 'Apa pengalaman Anda dalam merancang dan mengelola menu restoran?'),
      (6, 'Bagaimana Anda mengatasi tantangan dalam pengelolaan stok dan persediaan makanan?'),
      (6, 'Bagaimana Anda memastikan kualitas makanan dan kebersihan di restoran Anda?'),
      (6, 'Bagaimana Anda mendekati pelanggan yang tidak puas dengan pengalaman mereka di restoran?'),
      (6, 'Bagaimana Anda memilih dan berkolaborasi dengan pemasok bahan baku?'),
      (6, 'Bagaimana Anda memastikan kepatuhan dengan standar keamanan pangan dan regulasi terkait?'),
      (6, 'Apa langkah-langkah yang Anda ambil untuk memotivasi dan melatih staf di restoran?'),
      (6, 'Bagaimana Anda menghadapi tren kuliner dan bagaimana Anda berencana untuk tetap relevan di pasar?'),
      (6, 'Bisakah Anda berbagi pengalaman Anda dalam menghadapi situasi darurat atau masalah teknis yang mempengaruhi operasional restoran?'),
      
      (7, 'Bisakah Anda menceritakan cara Anda dalam mengelola siklus penjualan dari awal hingga penutupan?'),
      (7, 'Bagaimana cara Anda mencari prospek atau pelanggan potensial untuk produk atau layanan yang Anda tawarkan?'),
      (7, 'Bagaimana Anda mengatasi penolakan atau hambatan dalam proses penjualan?'),
      (7, 'Apa metode pemasaran digital atau online yang Anda kuasai, dan bagaimana Anda menggunakannya dalam pekerjaan Anda?'),
      (7, 'Bagaimana Anda mengukur keberhasilan kampanye pemasaran yang Anda luncurkan?'),
      (7, 'Apa strategi Anda dalam membangun dan memelihara hubungan dengan pelanggan atau klien?'),
      (7, 'Bagaimana Anda memahami kebutuhan dan keinginan pelanggan Anda, dan bagaimana Anda menawarkan solusi yang sesuai?'),
      (7, 'Apakah Anda memiliki pengalaman dalam mengelola tim penjualan atau pemasaran? Bagaimana Anda memotivasi tim Anda?'),
      (7, 'Bagaimana Anda memastikan bahwa Anda selalu terhubung dengan tren dan perkembangan terbaru dalam industri pemasaran dan penjualan?'),
      (7, 'Bagaimana Anda mengidentifikasi tren pasar dan persaingan dalam industri yang Anda targetkan?'),
      
      (8, 'Apa definisi Anda tentang layanan pelanggan yang baik, dan bagaimana Anda menerapkannya dalam pekerjaan Anda?'),
      (8, 'Bisakah Anda memberikan contoh pengalaman layanan pelanggan yang sukses yang Anda alami atau Anda berikan?'),
      (8, 'Bagaimana Anda menangani pelanggan yang tidak puas atau marah, dan bagaimana Anda mencoba untuk memecahkan masalah mereka?'),
      (8, 'Apa langkah-langkah atau proses yang Anda ikuti dalam menyelesaikan permintaan atau masalah pelanggan?'),
      (8, 'Bagaimana Anda berkomunikasi dengan pelanggan, terutama dalam situasi yang memerlukan penjelasan atau pemecahan masalah kompleks?'),
      (8, 'Apakah Anda memiliki pengalaman dalam menggunakan sistem manajemen pelanggan (CRM) atau perangkat lunak sejenisnya? Bagaimana Anda menggunakannya?'),
      (8, 'Bagaimana Anda mengukur keberhasilan dalam memberikan layanan pelanggan? Apa indikator kinerja yang Anda gunakan?'),
      (8, 'Apakah Anda pernah berkontribusi dalam mengembangkan atau meningkatkan proses layanan pelanggan?'),
      (8, 'Bagaimana Anda memastikan konsistensi dalam pelayanan pelanggan, terutama ketika berurusan dengan banyak pelanggan?'),
      (8, 'Apa yang Anda lakukan untuk terus memperbarui pengetahuan Anda tentang produk atau layanan yang Anda dukung untuk memberikan informasi yang akurat dan bermanfaat kepada pelanggan?'),
      
      (9, 'Apa pengalaman Anda dalam mengelola administrasi dan akuntansi?'),
      (9, 'Bagaimana Anda memastikan keakuratan dan ketepatan waktu dalam proses pencatatan dan pelaporan keuangan?'),
      (9, 'Apa perangkat lunak atau sistem akuntansi yang Anda kuasai dan gunakan dalam pekerjaan Anda?'),
      (9, 'Bagaimana Anda mengatasi situasi di mana terdapat perbedaan atau ketidaksesuaian dalam data keuangan?'),
      (9, 'Apa tanggung jawab Anda dalam mengelola anggaran atau perencanaan keuangan?'),
      (9, 'Apakah Anda memiliki pengalaman dalam menyusun laporan keuangan, neraca, dan laporan laba rugi?'),
      (9, 'Bagaimana Anda menjaga kepatuhan perpajakan dan peraturan akuntansi dalam pekerjaan Anda?'),
      (9, 'Apakah Anda pernah berpartisipasi dalam audit internal atau eksternal? Bagaimana pengalaman Anda?'),
      (9, 'Bagaimana Anda mengorganisir dan mengelola dokumen dan catatan penting dalam pekerjaan administratif Anda?'),
      (9, 'Apa yang Anda lakukan untuk terus memperbarui pengetahuan Anda tentang perkembangan terbaru dalam akuntansi dan administrasi?'),
      
      (10, 'Bisakah Anda menjelaskan pengalaman Anda dalam desain grafis dan proyek yang telah Anda buat?'),
      (10, 'Apa yang membuat Anda tertarik dalam bidang desain grafis?'),
      (10, 'Bagaimana Anda mengukur keberhasilan desain grafis?'),
      (10, 'Apa alat dan perangkat lunak desain yang paling Anda kuasai?'),
      (10, 'Bagaimana Anda menghadapi tantangan dalam proyek desain grafis?'),
      (10, 'Bagaimana Anda menjaga diri Anda tetap terinspirasi dan kreatif dalam desain?'),
      (10, 'Bagaimana Anda bekerja dalam tim desain grafis?'),
      (10, 'Bisakah Anda memberikan contoh proyek desain yang paling Anda banggakan?'),
      (10, 'Apa tren terbaru dalam desain grafis yang Anda ikuti atau terapkan?'),
      (10, 'Bagaimana Anda mengelola waktu dan deadline dalam proyek desain grafis?'),
      
      (11, 'Apa yang Anda lakukan ketika menghadapi masalah perangkat keras atau perangkat lunak yang kritis?'),
      (11, 'Apa pendekatan Anda terhadap keamanan data dan perlindungan privasi dalam lingkungan IT?'),
      (11, 'Bagaimana Anda berkomunikasi dengan non-teknis tentang masalah teknis?'),
      (11, 'Apa yang Anda cari dalam pemilihan alat atau teknologi yang tepat untuk suatu proyek?'),
      (11, 'Bagaimana Anda memprioritaskan tugas dan proyek ketika Anda memiliki banyak permintaan yang bersaing?'),
      (11, 'Bagaimana Anda menjaga keterampilan teknis Anda tetap terasah dan relevan dalam industri yang terus berubah?'),
      (11, 'Bagaimana Anda mengelola ekspektasi pelanggan atau pemangku kepentingan dalam proyek IT?'),
      (11, 'Bagaimana jika anda mengalami kegagalan dalam mengerjakan proyek?'),
      (11, 'Bagaimana cara Anda mengatasi bila menerima tugas proyek yang menggunakan teknologi yang Anda tidak kuasai?'),
      (11, 'Bagaimana cara Anda beradaptasi dengan tim baru dalam mengerjakan proyek?'),
      
      (12, 'Apa pengalaman dan latar belakang Anda dalam bidang keamanan?'),
      (12, 'Bagaimana Anda memastikan keamanan fisik dan perlindungan aset perusahaan?'),
      (12, 'Apakah Anda memiliki pengalaman dalam merancang atau mengimplementasikan kebijakan keamanan?'),
      (12, 'Bagaimana Anda menangani situasi darurat atau insiden keamanan yang mungkin timbul di lingkungan kerja Anda?'),
      (12, 'Apakah Anda memiliki pengalaman dalam penggunaan perangkat teknologi keamanan, seperti sistem keamanan CCTV?'),
      (12, 'Bagaimana Anda memastikan kepatuhan terhadap peraturan keamanan dan kebijakan perusahaan?'),
      (12, 'Apa tindakan yang Anda ambil untuk mengurangi risiko keamanan dalam lingkungan kerja?'),
      (12, 'Bagaimana Anda berkolaborasi dengan pihak berwenang atau agen eksternal untuk keamanan?'),
      (12, 'Apa strategi Anda dalam mendidik dan melibatkan karyawan dalam prinsip-prinsip keamanan?'),
      (12, 'Bagaimana Anda menjaga diri Anda up-to-date dengan perkembangan terbaru dalam bidang keamanan?'),
      
      (13, 'Apa yang mendorong Anda untuk berkarir di bidang kesehatan?'),
      (13, 'Apa pengalaman Anda dalam merawat pasien atau bekerja di lingkungan kesehatan?'),
      (13, 'Bagaimana Anda menjaga diri Anda sendiri untuk mencegah penularan penyakit dari pasien?'),
      (13, 'Bagaimana Anda berkomunikasi dengan pasien atau klien yang mungkin berasal dari berbagai latar belakang?'),
      (13, 'Bagaimana Anda menjaga kepatuhan terhadap standar etika dan profesionalisme dalam praktik kesehatan?'),
      (13, 'Bagaimana Anda menghadapi situasi darurat atau tekanan tinggi dalam pekerjaan kesehatan?'),
      (13, 'Bagaimana Anda berkolaborasi dengan anggota tim kesehatan lainnya untuk merawat pasien atau klien?'),
      (13, 'Bagaimana Anda menjaga pengetahuan Anda tetap mutakhir dalam bidang kesehatan?'),
      (13, 'Apa yang Anda anggap sebagai tantangan terbesar dalam bidang kesehatan saat ini?'),
      (13, 'Bagaimana Anda melibatkan diri dalam mendukung promosi kesehatan masyarakat?'),
      
      (14, 'Apa yang memotivasi Anda untuk bekerja di bidang pendidikan?'),
      (14, 'Bagaimana Anda mempersiapkan diri untuk menghadapi beragam kebutuhan siswa?'),
      (14, 'Bagaimana Anda mengevaluasi keberhasilan siswa dalam pembelajaran?'),
      (14, 'Bagaimana Anda merencanakan dan menyampaikan materi pelajaran yang efektif?'),
      (14, 'Bagaimana Anda menangani siswa yang mungkin mengalami kesulitan belajar?'),
      (14, 'Bagaimana Anda berkomunikasi dengan orang tua atau wali murid?'),
      (14, 'Bagaimana Anda mendukung keberagaman dan inklusi dalam lingkungan pendidikan?'),
      (14, 'Bagaimana Anda terus meningkatkan keterampilan dan pengetahuan Anda dalam pendidikan?'),
      (14, 'Apa yang menurut Anda tantangan terbesar dalam pendidikan saat ini?'),
      (14, 'Bagaimana Anda mendukung pengembangan karakter dan keterampilan sosial siswa?'),
      
      (15, 'Apa pengalaman Anda dalam industri hiburan dan apa yang membuat Anda tertarik untuk berkarier di sini?'),
      (15, 'Apa yang membuat anda tertarik dalam bidang hiburan?'),
      (15, 'Bagaimana Anda berkolaborasi dengan tim kreatif dalam mengembangkan konsep atau ide hiburan?'),
      (15, 'Apa yang Anda lakukan untuk mengikuti tren terbaru dalam industri hiburan?'),
      (15, 'Apa pengalaman Anda dalam merencanakan, mengorganisir, atau mengelola acara hiburan atau pertunjukan?'),
      (15, 'Bagaimana Anda menilai keberhasilan proyek hiburan yang Anda kerjakan?'),
      (15, 'Apakah Anda memiliki pengalaman dalam berkomunikasi dengan artis atau talenta dalam industri hiburan?'),
      (15, 'Bagaimana Anda berurusan dengan masalah atau kendala yang muncul dalam produksi atau pelaksanaan acara hiburan?'),
      (15, 'Bagaimana Anda melihat perkembangan dalam hiburan digital dan media sosial yang memengaruhi industri ini?'),
      (15, 'Apa yang membuat Anda percaya bahwa Anda adalah kandidat yang tepat untuk berkontribusi dalam industri hiburan?'),
      
      (16, 'Apa pengalaman Anda dalam industri perhotelan, dan apa yang membuat Anda tertarik untuk berkarier di sini?'),
      (16, 'Apa pengalaman dan latar belakang Anda dalam perhotelan?'),
      (16, 'Bagaimana Anda memastikan pengalaman positif tamu dalam properti atau bisnis perhotelan yang Anda kelola?'),
      (16, 'Apa tindakan yang Anda ambil untuk memastikan kepatuhan dengan standar layanan dan regulasi industri?'),
      (16, 'Bagaimana Anda mengelola situasi yang melibatkan tamu yang tidak puas atau memiliki keluhan?'),
      (16, 'Apa strategi Anda dalam merancang program promosi atau paket khusus untuk meningkatkan okupansi atau hunian hotel?'),
      (16, 'Bila anda menjadi Staff Manager, bagaimana cara Anda mengawasi dan mengelola staf hotel, termasuk pelatihan dan pengembangan mereka?'),
      (16, 'Apakah Anda memiliki pengalaman dalam merencanakan dan mengelola acara atau konferensi di hotel?'),
      (16, 'Bagaimana Anda melihat perkembangan terbaru dalam industri perhotelan, termasuk tren digital dan teknologi?'),
      (16, 'Apa yang membuat Anda percaya bahwa Anda adalah kandidat yang tepat untuk berkontribusi dalam industri perhotelan?')
      ;  
  `);
};

exports.down = (pgm) => {
  pgm.dropTable('questions');
};
