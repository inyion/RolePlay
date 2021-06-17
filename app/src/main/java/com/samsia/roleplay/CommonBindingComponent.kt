package com.samsia.roleplay

import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingComponent
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.samsia.roleplay.viewmodel.EditTextViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class CommonBindingComponent : DataBindingComponent {
    override fun getBaseBindAdapter(): BaseBindAdapter<*, *>? {
        return null
    }

    override fun getCommonBindingComponent(): CommonBindingComponent {
        return this
    }

    @BindingAdapter("loadWebView")
    fun loadWebView(view: WebView, webStr: LiveData<String>?) {
        view.loadUrl(webStr?.value!!)
    }

    @BindingAdapter("topIcon")
    fun topIcon(view: Button, icon: Int) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0)
    }

    @BindingAdapter("addActionIcon")
    fun addActionIcon(view: EditText, tabIcon: EditTextViewModel.TabIcon?) {
        val drawable = if (tabIcon != null) ResourcesCompat.getDrawable(
            view.resources,
            tabIcon.iconRes,
            null
        ) else null
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        view.setOnTouchListener{ v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                }
                MotionEvent.ACTION_UP -> {
                    if (drawable != null && event.x > v.width - v.paddingRight - drawable.intrinsicWidth!!) {
                        tabIcon?.action?.onTab(view.text.toString())
                        view.text.clear()
                    }
                }
                else -> {
                }
            }

            false
        }
    }

    @BindingAdapter("startIcon", "replaceIcon", requireAll = false)
    fun startIcon(view: TextView, iconStr: String?, replaceIcon: Int) {

        if (!TextUtils.isEmpty(iconStr)) {
            Glide.with(view).load(iconStr)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            view.post {
                                view.setCompoundDrawablesWithIntrinsicBounds(
                                    resource,
                                    null,
                                    null,
                                    null
                                )
                            }
                            return false
                        }
                    }).submit()
        } else {
            view.setCompoundDrawablesWithIntrinsicBounds(replaceIcon, 0, 0, 0)
        }
    }

    @BindingAdapter("buttonName")
    fun buttonName(view: Button, name: Int) {
        view.setText(name)
    }

    @BindingAdapter("setResourceImage")
    fun setResourceImage(view: ImageView, resourceId: Int) {
        view.setImageResource(resourceId)
    }

    @BindingAdapter("setDrawableStart")
    fun setDrawableStart(view: TextView, resourceId: Int) {
        view.setCompoundDrawablesWithIntrinsicBounds(resourceId, 0, 0, 0)
    }

    @BindingAdapter("setImageEnd")
    fun setImageEnd(view: TextView, iconStr: String?) {

        if (!TextUtils.isEmpty(iconStr)) {
            Glide.with(view).load(iconStr)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            view.post {
                                view.setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    resource,
                                    null
                                )
                            }
                            return false
                        }
                    }).submit()
        }
//        else {
//            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, replaceIcon, 0)
//        }
    }

    var maxMessageWidth: Int = 0
    var minImageWidthHeigh: Int = 0

    //    var maxMessageWidthForAd: Int = 0 // 요건 내부에선 쓸필요가 없음 케이스에따라 밖에서 들고올 데이터
    var maxCellSize: Int = 0
    var screenWidth: Int = 0

    @BindingAdapter("setImage", "setImageScaleType", "setResourceId", requireAll = false)
    fun setImage(
        view: ImageView,
        url: LiveData<String>,
        scaleType: ImageView.ScaleType?,
        resourceId: Int
    ) {
        setImage(view, url.value, scaleType, resourceId, view.width, view.height, null, false)
    }

    @BindingAdapter("setImage", "setImageScaleType", "setResourceId", requireAll = false)
    fun setImage(
        view: CircleImageView,
        url: String?,
        scaleType: ImageView.ScaleType?,
        resourceId: Int
    ) {
        view.setImageResource(0)
        if (TextUtils.isEmpty(url)) {

            if (resourceId > 0) {
                view.setImageResource(resourceId)
            }

        } else {
            view.visibility = View.VISIBLE
            Glide.with(view).load(url)
                    .listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            view.post {
                                if (scaleType != null) {
                                    view.scaleType = scaleType
                                }
                                view.setImageDrawable(resource)
                            }
                            return false
                        }
                    }).submit()
        }
    }

    @BindingAdapter(
        "setImage", "setImageScaleType", "setResourceId", // 기본기능
        "imageWidth", "imageHeight", // 탑 크롭 기능
        "createThumbnail",  //비디오 썸네일케이스
        "emptyVisibleGone", //이미지 비었을 시 gone처리
        requireAll = false
    )
    fun setImage(
        view: ImageView, url: String?, scaleType: ImageView.ScaleType?, resourceId: Int,
        imageWidth: Int, imageHeight: Int,
        thumbnailPath: String?, emptyVisibleGone: Boolean
    ) {

//        if (message != null && message.isSend &&ƒ
//                !TextUtils.isEmpty(message.local_media_path) && message.media_msg_width > 0 && message.media_msg_height > 0) {
//            // 미디어 전송시는 초기화 안하기로
//        } else {
        view.setImageResource(0)
//        }

        if (TextUtils.isEmpty(url)) {
            if (resourceId > 0) {
                view.setImageResource(resourceId)
            }

            if (emptyVisibleGone) {
                view.visibility = View.GONE
            }

        } else {
            view.visibility = View.VISIBLE

            Glide.with(view).load(url)
                    .listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {

                            view.post {

                                if (scaleType != null) {
                                    view.scaleType = scaleType
                                }
                                view.setImageDrawable(resource)
                            }
                            return false
                        }
                    }).submit()
        }
    }

    fun retriveVideoFrameFromVideo(videoPath: String): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, HashMap())
            else
                mediaMetadataRetriever.setDataSource(videoPath)
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)

        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

    @BindingAdapter("setString")
    fun setString(view: TextView, str: LiveData<String>) {
        setString(view, str.value)
    }

    @BindingAdapter("setString")
    fun setString(view: TextView, str: String?) {
        if (TextUtils.isEmpty(str)) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
            view.text = str
        }
    }

    @BindingAdapter("setSpannableString")
    fun setSpannableString(view: TextView, str: LiveData<SpannableString>) {
        if (TextUtils.isEmpty(str.value)) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
            view.text = str.value
        }
    }

    @BindingAdapter("setSpannable")
    fun setSpannable(view: TextView, str: Spannable?) {
        if (str != null) {
            view.text = str
        }
    }

    @BindingAdapter("setTextColorTheme")
    fun setTextColorTheme(view: TextView, attrId: Int?) {
        val outValue = TypedValue()
        if (attrId != null) {
            view.getSafeContext().theme.resolveAttribute(attrId, outValue, true)
            view.setTextColor(ContextCompat.getColor(view.getSafeContext(), outValue.resourceId))
        }
    }

    @BindingAdapter("setTextColor")
    fun setTextColor(view: TextView, colorCode: Int) {
        view.setTextColor(colorCode)
    }

    @BindingAdapter("textChanged")
    fun textChanged(view: EditText, textWatcher: TextWatcher?) {
        if (textWatcher != null) {
            view.addTextChangedListener(textWatcher)
        }
    }

    @BindingAdapter("setBackgroundTheme")
    fun setBackgroundTheme(view: View, attrId: Int?) {
        val outValue = TypedValue()
        if (attrId != null) {
            view.getSafeContext().theme.resolveAttribute(attrId, outValue, true)
            view.setBackgroundResource(outValue.resourceId)
        }
    }

    @BindingAdapter("setBackgroundResource")
    fun setBackgroundResource(view: View, resource: Drawable?) {
        if (resource != null) {
            view.background = resource
        }
    }

    @BindingAdapter("finish")
    fun finish(view: View, isFinish: LiveData<Boolean>?) {
        if (view.getSafeContext() is ViewModelActivity && isFinish != null && isFinish.value != null && isFinish.value as Boolean) {
            (view.getSafeContext() as ViewModelActivity).finish()
        }
    }

    @BindingAdapter("setTag")
    fun setTag(view: View, tag: Any?) {
        if (tag != null) {
            view.tag = tag
        }
    }

    var dialog: Dialog? = null
    private lateinit var messageGridView: androidx.recyclerview.widget.RecyclerView

    @BindingAdapter("copyToClipboard")
    fun copyToClipboard(view: View, str: LiveData<String>?) {
        if (view.getSafeContext() is ViewModelActivity && str != null && str.value != null) {
            val clip = view.getSafeContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clip.setPrimaryClip(ClipData.newPlainText("text", str.value))
//            ToastUtil.showToastCenter(view.getSafeContext().getString(R.string.copy_clipboard))
        }
    }

    @BindingAdapter("enable")
    fun enable(view: View, enable: LiveData<Boolean>?) {
        if (enable != null) {
            view.isEnabled = enable.value!!
        }
    }

    @BindingAdapter("layout_width", "layout_height", requireAll = false)
    fun setLayoutSize(view: View, width: Int, height: Int) {
        val layoutParams = view.layoutParams
        if (width > 0) {
            layoutParams.width = width
        }

        if (height > 0) {
            layoutParams.height = height
        }

        view.layoutParams = layoutParams
    }

    @BindingAdapter("contents_width", "contents_height", "max_width", requireAll = false)
    fun setLayoutSizeForChat(view: View, width: Int, height: Int, maxWidth: Int) {
        val layoutParams = view.layoutParams

        if (maxWidth > 0) {// 채팅이 기본값이 화면에 일정비율로 꽉채워 보여줘서
            layoutParams.width = maxWidth
        }

        if (height > 0 && width > 0) {

            if (maxCellSize == 0) {
                val dm = App.instance.resources.displayMetrics
                maxMessageWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX,
                    ((dm.widthPixels.toFloat()) * 0.61).toFloat(),
                    dm
                ).toInt()
//                maxMessageWidthForAd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, ((dm.widthPixels.toFloat()) * 0.70).toFloat(), dm).toInt()
                maxCellSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    542F,
                    App.instance.resources.displayMetrics
                ).toInt()
            }

            val rate: Float
            val width = width.toFloat()
            var height = height

            val viewWidth = maxWidth.toFloat()
            if (viewWidth > 0) {
                rate = viewWidth / width
                height = (height * rate).toInt()
            }

            //max제한이 없거나
            if (maxWidth == 0 || height <= maxCellSize) {
                layoutParams.height = height
            } else {
                layoutParams.height = maxCellSize
            }

        }

        view.layoutParams = layoutParams
    }

    @Throws(Exception::class)
    private fun invokeOnResume(webView: WebView, onResumeFlag: Int) {
        if (onResumeFlag == 1) {
            WebView::class.java.getMethod("onResume").invoke(webView)
            webView.resumeTimers()
        } else {
            WebView::class.java.getMethod("onPause").invoke(webView)
            webView.pauseTimers()
        }
    }
}