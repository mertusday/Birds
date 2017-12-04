package com.birdboys.birdbuddy;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class BirdsNearbyFragment extends Fragment {

    private static final String ARG_LAT = "lat";
    private static final String ARG_LON = "lon";

    private Double latitude;
    private Double longitude;

    private static final String TAG = "MainFragment";
    private RecyclerView recyclerView;
    private BirdAdapter birdAdapter;
    private ThumbnailDownloader<BirdHolder> thumbnailDownloader;

    private List<Bird> birdList;

    public static BirdsNearbyFragment newInstance(Double lat, Double lon) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LAT, lat);
        args.putSerializable(ARG_LON, lon);
        BirdsNearbyFragment fragment = new BirdsNearbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BirdsNearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        latitude = (Double) getArguments().getSerializable(ARG_LAT);
        longitude = (Double) getArguments().getSerializable(ARG_LON);

        try {
            birdList = new FetchBirdTask().execute().get().get(0);
        } catch (InterruptedException ite) {
            ite.printStackTrace();
        } catch (ExecutionException exe) {
            exe.printStackTrace();
        }

        setRetainInstance(true);
        Handler responseHandler = new Handler();
        thumbnailDownloader = new ThumbnailDownloader<>(responseHandler, getActivity());
        thumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<BirdHolder>() {
                    @Override
                    public void onThumbnailDownloaded(BirdHolder target, BitmapDrawable thumbnail) {
                        BitmapDrawable drawable = thumbnail;
                        target.bindDrawable(drawable);
                    }
                }, getActivity());
        thumbnailDownloader.start();
        thumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started.");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_birds_nearby, container, false);

        recyclerView = view.findViewById(R.id.nearby_frag_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();;
        thumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed.");
    }

    public void updateUI() {

        birdAdapter = new BirdAdapter(birdList);
        recyclerView.setAdapter(birdAdapter);

    }

    private class BirdHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Bird bird;
        private TextView birdName;
        private ImageView birdImg;
        private CheckBox birdSeen;

        public BirdHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.single_line, container, false));
            itemView.setOnClickListener(this);

            birdName = (TextView) itemView.findViewById(R.id.single_line_bird_name);
            birdImg = (ImageView) itemView.findViewById(R.id.single_line_bird_image);
            birdSeen = (CheckBox) itemView.findViewById(R.id.single_line_bird_seen);

        }

        public void bindDrawable(BitmapDrawable drawable) {
            birdImg.setImageDrawable(drawable);
        }

        public void bind(Bird targetBird) {
            bird = targetBird;
            LifeList lifeList = LifeList.get(getActivity());
            lifeList.updateSeen(bird);
            birdName.setText(bird.getName());
            birdSeen.setChecked(bird.isSeen());
        }

        @Override
        public void onClick(View view) {
            Intent intent = SingleBirdActivity.newIntent(getActivity(), bird.getName());
            startActivity(intent);
        }
    }

    private class BirdAdapter extends RecyclerView.Adapter<BirdHolder> {

        private List<Bird> birdList;

        public BirdAdapter(List<Bird> birds) {
            birdList = birds;
        }

        @Override
        public BirdHolder onCreateViewHolder(ViewGroup container, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BirdHolder(layoutInflater, container);
        }

        @Override
        public void onBindViewHolder(BirdHolder holder, int position) {
            Bird bird = birdList.get(position);
            String birdURL = "https://www.allaboutbirds.org/guide/" + bird.getUri_name() + "/id";
            thumbnailDownloader.queueThumbnail(holder, birdURL);
            holder.bind(bird);
        }

        @Override
        public int getItemCount() {
            return birdList.size();
        }
    }

    private class FetchBirdTask extends AsyncTask<Void,Void,List<List>> {
        @Override
        protected List<List> doInBackground(Void... params) {
            String[] LongLat = {""+longitude,""+latitude};
            List<List> birdLists = new Ebirdr(LongLat).fetchNewBirds();
            //birdList = birdLists.get(0);
            //sightingList = birdLists.get(1);
            return birdLists;
        }
    }

}
