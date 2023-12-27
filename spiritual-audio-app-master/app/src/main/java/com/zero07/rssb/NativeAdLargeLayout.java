package com.zero07.rssb;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAdImage;
import com.tapdaq.sdk.network.HttpClientBase;
import com.tapdaq.sdk.network.TClient;

public class NativeAdLargeLayout extends ConstraintLayout {

    private LayoutInflater mInflater;

    private FrameLayout mAdview;
    private TextView mTitleView;
//    private TextView mSubtitleTextView;
    private TextView mBodyTextView;
    private TextView mCaptionTextView;
    private ImageView mImageView;
    private Button mButton;
    private FrameLayout mAdChoicesView;
    private FrameLayout mIconView;
    private TextView mPriceTextView;
    private TextView mStoreTextView;
    private TextView mStarRating;
    private FrameLayout mMediaView;

    public NativeAdLargeLayout(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public NativeAdLargeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public NativeAdLargeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        View v = mInflater.inflate(R.layout.layout_native_large_ad, this, true);

        mTitleView = v.findViewById(R.id.large_native_title);
//        mSubtitleTextView = v.findViewById(R.id.native_subtitle);
        mBodyTextView = v.findViewById(R.id.large_native_body_textView);
//        mCaptionTextView = v.findViewById(R.id.caption_textview);
        mImageView = v.findViewById(R.id.large_native_image_View);
        mButton = v.findViewById(R.id.large_native_cta_Button);
        mAdChoicesView = v.findViewById(R.id.large_native_adChoices);
        mIconView = v.findViewById(R.id.large_native_icon_View);
//        mPriceTextView = v.findViewById(R.id.price_textview);
//        mStoreTextView = v.findViewById(R.id.store_textview);
//        mStarRating = v.findViewById(R.id.star_rating_textview);
        mMediaView = v.findViewById(R.id.large_native_media_View);
        mAdview = v.findViewById(R.id.large_native_ad_View);
    }

    public void clear(){
        mTitleView.setText("");
//        mSubtitleTextView.setText("");
        mBodyTextView.setText("");
//        mCaptionTextView.setText("");
        mButton.setText("");
//        mPriceTextView.setText("");
//        mStoreTextView.setText("");
//        mStarRating.setText("");

        mAdview.removeAllViews();
        mMediaView.removeAllViews();
        mAdChoicesView.removeAllViews();
        mImageView.setImageBitmap(null);
        mIconView.removeAllViews();
    }

    public void populate(TDMediatedNativeAd ad) {
        clear();

        if (ad != null) {
            if (ad.getAdView() != null) {
                ConstraintLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mAdview.addView(ad.getAdView(), params);
            }

            if (ad.getMediaView() != null) {
                ConstraintLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                params.addRule(CENTER_IN_PARENT);
                mMediaView.addView(ad.getMediaView(), params);
            }

            mTitleView.setText(ad.getTitle());
            mBodyTextView.setText(ad.getBody());
            mButton.setText(ad.getCallToAction());
            ad.registerView(mButton);

//            if (ad.getSubtitle() != null) {
//                mSubtitleTextView.setText(ad.getSubtitle());
//            }

            if (ad.getImages() != null) {
                TDMediatedNativeAdImage image = ad.getImages().get(0);
                if (image.getDrawable() != null) {
                    mImageView.setImageDrawable(ad.getImages().get(0).getDrawable());
                } else if (image.getUrl() != null) {
                    new TClient().executeImageGET(getContext(), image.getUrl(), 0, 0, new HttpClientBase.ResponseImageHandler() {
                        @Override
                        public void onSuccess(Bitmap response) {
                            mImageView.setImageBitmap(response);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
//                removeView(mImageView);
//                addView(mImageView, 1);
            }

            if (ad.getAppIconView() != null) {
                mIconView.addView(ad.getAppIconView());
            }

            if (ad.getAdChoiceView() != null) {
                ConstraintLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mAdChoicesView.addView(ad.getAdChoiceView(), params);
            }

            if (ad.getVideoController() != null && ad.getVideoController().hasVideoContent()) {
                ad.getVideoController().play();
            }

//            if (ad.getCaption() != null) {
//                mCaptionTextView.setText(ad.getCaption());
//            }

//            if (ad.getPrice() != null) {
//                mPriceTextView.setText(ad.getPrice());
//            }

//            mStarRating.setText(Double.toString(ad.getStarRating()));

//            if (ad.getStore() != null) {
//                mStoreTextView.setText(ad.getStore());
//            }

            ad.trackImpression(getContext());
        }
    }
}
