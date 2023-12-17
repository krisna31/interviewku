package com.capstone.interviewku.ui.activities.tipsdetail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.capstone.interviewku.R
import com.capstone.interviewku.data.room.entity.ArticleEntity
import com.capstone.interviewku.databinding.ActivityTipsDetailBinding
import com.capstone.interviewku.util.Helpers

class TipsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTipsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTipsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.interview_tips_title)
        }

        val article = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_ARTICLE_ENTITY_KEY, ArticleEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_ARTICLE_ENTITY_KEY)
        }

        article?.let {
            Glide.with(this)
                .load(article.coverImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.bg_no_image)
                .placeholder(R.drawable.bg_no_image)
                .into(binding.ivCover)
            binding.tvTitle.text = article.title
            binding.tvAuthor.text = getString(R.string.author_template, article.author)
            binding.tvCreatedAt.text = getString(
                R.string.created_at_template,
                Helpers.tzTimeStringToDate(article.createdAt)?.let { date ->
                    Helpers.dateToIndonesianFormat(date)
                } ?: run {
                    "-"
                }
            )
            binding.tvContent.text = Helpers.stringEscapeRemover(article.content)
            binding.tvSource.text = getString(R.string.source_template, article.source)
        } ?: run {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_ARTICLE_ENTITY_KEY = "EXTRA_ARTICLE_ENTITY_KEY"
    }
}