package com.o2games.playwin.android.adapter;

//public class GamezopGameAdapter extends RecyclerView.Adapter<GamezopGameAdapter.GamezopGameViewHolder> {
//
//    ConnectivityManager connectivityManager;
//
//    Activity context;
//    ArrayList<GameModel> gamezopGameModels;
//    //    ProgressBar progressBar5;
//    AlertDialog progressDialog;
//
//    private InterstitialAd admobInterstitial;
//
//    public GamezopGameAdapter(Activity context, ArrayList<GameModel> modelData) {
//        this.context = context;
//        this.gamezopGameModels = modelData;
//    }
//
//    @Override
//    public GamezopGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_gamezop_game, null);
//        return new GamezopGameViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GamezopGameViewHolder holder, int position) {
//        GameModel model = gamezopGameModels.get(position);
//
//        holder.gamezopCvIcon.setImageResource(model.getGamezopGameIcon());
//        holder.gamezopCvText.setText(model.getGamezopGameText().getText().toString());
//
//        Glide.with(context)
//                .load(model.getItemImage())
//                .into(holder.gamezopCvIcon);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                new MyTask(v.getContext()).execute();
//                Intent intent = null;
//                switch (position) {
//                    case 0:
//                        if (!internetConnected(context)) {
//                            noInternetDialog();
//                        } else {
//                            showInterstitialAdFrom(v, 0);
//                        }
//                        break;
//                    case 1:
//                        if (!internetConnected(context)) {
//                            noInternetDialog();
//                        } else {
//                            showInterstitialAdFrom(v, 1);
//                        }
//                        break;
//                    case 2:
//                        if (!internetConnected(context)) {
//                            noInternetDialog();
//                        } else {
//                            showInterstitialAdFrom(v, 2);
//                        }
//                        break;
//                    case 3:
//                        /**
//                         * No Ad shown this time
//                         * **/
//                        intent = new Intent(v.getContext(), PhotoActivity.class);
//                        Toast.makeText(context, "Please wait. Loading....", Toast.LENGTH_LONG).show();
//                        context.startActivity(intent);
//                        break;
//                    case 4:
//                        if (!internetConnected(context)) {
//                            noInternetDialog();
//                        } else {
//                            showInterstitialAdFrom(v, 4);
//                        }
//                        break;
//                    case 5:
//                        if (!internetConnected(context)) {
//                            noInternetDialog();
//                        } else {
////                            intent = new Intent(v.getContext(), SignupActivity.class);
////                            Toast.makeText(context, "Please wait. Loading....", Toast.LENGTH_LONG).show();
////                            context.startActivity(intent);
////                            Toast.makeText(context, "COMING SOON", Toast.LENGTH_SHORT).show();
//                            showToastComingSoon(v);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//        });
//    }
//
//    private void showToastComingSoon(View rootView1) {
//        rootView1 = LayoutInflater.from(context).inflate(R.layout.layout_toast_custom,
//                (ConstraintLayout) rootView1.findViewById(R.id.custom_toast_constraint));
//
////        LayoutInflater inflater = getLayoutInflater();
////        rootView1 = inflater.inflate(R.layout.layout_toast_custom,
////                (ConstraintLayout) rootView1.findViewById(R.id.custom_toast_constraint));
//
//        CardView toastCardView = rootView1.findViewById(R.id.toast_custom_card_view);
//        TextView toastText = rootView1.findViewById(R.id.toast_custom_text);
//        ImageView toastIcon = rootView1.findViewById(R.id.toast_custom_image);
//
//        toastCardView.setBackgroundResource(R.drawable.bg_custom_toast);
//        toastText.setText("COMING SOON");
//        toastIcon.setImageResource(R.drawable.coming_soon);
//
//        Toast toast = new Toast(context);
//        toast.setGravity(Gravity.CENTER, 0, 500);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(rootView1);
//
//        toast.show();
//    }
//
//    @Override
//    public int getItemCount() {
//        return gamezopGameModels.size();
//    }
//
//    public class GamezopGameViewHolder extends RecyclerView.ViewHolder {
//        ImageView gamezopCvIcon;
//        TextView gamezopCvText;
//
//        public GamezopGameViewHolder(@NonNull View itemView) {
//            super(itemView);
//            gamezopCvIcon = itemView.findViewById(R.id.gamezop_cv_icon);
//            gamezopCvText = itemView.findViewById(R.id.gamezop_cv_text);
//        }
//    }
//
//    private void toBlink(View view) {
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.blink);
//
//    }
//
//    private void noInternetDialog() {
//        AlertDialog.Builder noInternetDialog = new AlertDialog.Builder(context);
//        noInternetDialog.setMessage("Please check your internet connection");
//
//        AlertDialog alertDialog = noInternetDialog.create();
//
//        alertDialog.show();
//    }
//
//
//    /*****Checking Internet Connectivity****/
//    private boolean internetConnected(Context context) {
//
//        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
//        NetworkInfo mobileDataConnection = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
//
//        if ((wifiConnection != null && wifiConnection.isConnected()
//                || (mobileDataConnection != null && mobileDataConnection.isConnected()))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//    /*****Checking Internet Connectivity****/
//
//    private void showProgressDialog(View v) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View rootView1 = LayoutInflater.from(context).inflate(R.layout.layout_dialog_progress,
//                (ConstraintLayout) v.findViewById(R.id.dialog_progress_constraint));
//        builder.setView(rootView1);
//        builder.setCancelable(false);
//
//        progressDialog = builder.create();
//
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.show();
//    }
//
//
//
//}
