package com.example.github.repositories.common

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.github.repositories.R
import com.squareup.picasso.Picasso

fun View.visibility(show: Boolean) {
    if (show) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun ImageView.loadImage(url: String?) {
    Picasso.get()
        .load(url)
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .into(this)
}

fun IFailure.showToast(context: Context?) {
    Toast.makeText(context, this.errorModel.errorMsg, Toast.LENGTH_SHORT).show()
}