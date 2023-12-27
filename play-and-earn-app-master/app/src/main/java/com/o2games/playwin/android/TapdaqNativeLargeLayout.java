package com.o2games.playwin.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.o2games.playwin.android.R;

public class TapdaqNativeLargeLayout extends ConstraintLayout {

    private LayoutInflater mInflater;

    private FrameLayout mAdview;
    private TextView mTitleView;
    private TextView mSubtitleTextView;
    private TextView mBodyTextView;
    private TextView mCaptionTextView;
    private ImageView mImageView;
    private FrameLayout mButton;
    private TextView mButtonText;
    private FrameLayout mAdChoicesView;
    private FrameLayout mIconView;
    private TextView mPriceTextView;
    private TextView mStoreName;
    private RatingBar mStarRating;
    private FrameLayout mMediaView;

    public TapdaqNativeLargeLayout(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public TapdaqNativeLargeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public TapdaqNativeLargeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        View v = mInflater.inflate(R.layout.layout_native_ad_large, this, true);

        mTitleView = v.findViewById(R.id.large_native_title);
//        mSubtitleTextView = v.findViewById(R.id.large_native_subtitle);
        mBodyTextView = v.findViewById(R.id.large_native_body_textView);
        mCaptionTextView = v.findViewById(R.id.large_native_caption);
        mImageView = v.findViewById(R.id.large_native_image_View);
        mButton = v.findViewById(R.id.large_native_cta_Button);
        mButtonText = v.findViewById(R.id.large_native_cta_Button_text);
        mAdChoicesView = v.findViewById(R.id.large_native_adChoices);
        mIconView = v.findViewById(R.id.large_native_icon_View);
//        mPriceTextView = v.findViewById(R.id.price_textview);
        mStoreName = v.findViewById(R.id.large_native_storeName);
        mStarRating = v.findViewById(R.id.large_native_ratingBar);
        mMediaView = v.findViewById(R.id.large_native_media_View);
        mAdview = v.findViewById(R.id.large_native_ad_View);
    }

    public void clear(){
        mTitleView.setText("");
//        mSubtitleTextView.setText("");
        mBodyTextView.setText("");
        mCaptionTextView.setText("");
        mButtonText.setText("");
//        mPriceTextView.setText("");
        mStoreName.setText("");
        mStarRating.setRating(1);

        mAdview.removeAllViews();
        mMediaView.removeAllViewsInLayout();
        mMediaView.removeAllViews();
        mAdChoicesView.removeAllViews();
        mImageView.setImageBitmap(null);
        mIconView.removeAllViews();
    }

//    public void populate(TDMediatedNativeAd ad) {
//        clear();
//
//        if (ad != null) {
//            if (ad.getAdView() != null) {
//                ConstraintLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                mAdview.addView(ad.getAdView(), params);
//            }
//
//            if (ad.getMediaView() != null) {
//                ConstraintLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////                params.addRule(CENTER_IN_PARENT);
//                mMediaView.addView(ad.getMediaView(), params);
//            }
//
//            mTitleView.setText(ad.getTitle());
//            mBodyTextView.setText(ad.getBody());
//            mButtonText.setText(ad.getCallToAction());
//            ad.registerView(mButton);
//
////            if (ad.getSubtitle() != null) {
////                mSubtitleTextView.setText(ad.getSubtitle());
////            }
//
//            if (ad.getCaption() != null) {
//                mCaptionTextView.setText(ad.getCaption());
//            }
//
//            if (ad.getImages() != null) {
//                TDMediatedNativeAdImage image = ad.getImages().get(0);
//                if (image.getDrawable() != null) {
//                    mImageView.setImageDrawable(ad.getImages().get(0).getDrawable());
//                } else if (image.getUrl() != null) {
//                    new TClient().executeImageGET(getContext(), image.getUrl(), 0, 0, new HttpClientBase.ResponseImageHandler() {
//                        @Override
//                        public void onSuccess(Bitmap response) {
//                            mImageView.setImageBitmap(response);
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//
//                        }
//                    });
//                }
////                removeView(mImageView);
////                addView(mImageView, 1);
//            }
//
//            if (ad.getAppIconView() != null) {
//                mIconView.addView(ad.getAppIconView());
//            }
//
//            if (ad.getAdChoiceView() != null) {
//                ConstraintLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                mAdChoicesView.addView(ad.getAdChoiceView(), params);
//            }
//
//            if (ad.getVideoController() != null && ad.getVideoController().hasVideoContent()) {
//                ad.getVideoController().play();
//            }
//
////            if (ad.getPrice() != null) {
////                mPriceTextView.setText(ad.getPrice());
////            }
//
////            mStarRating.setRating(Float.parseFloat(String.valueOf(ad.getStarRating())));
//            Double doubleStarRating = ad.getStarRating();
//            float floatStarRating = doubleStarRating.floatValue();
//            mStarRating.setRating(floatStarRating);
//
//
//            if (ad.getStore() != null) {
//                mStoreName.setText(ad.getStore());
//            }
//
//            ad.trackImpression(getContext());
//        }
//    }
}
