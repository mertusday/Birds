package com.birdboys.birdbuddy;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeenBirdsFragment extends Fragment {

    private static final String TAG = "SeenFragment";

    private LifeList lifeList;
    private RecyclerView recyclerView;
    private BirdAdapter birdAdapter;
    ThumbnailDownloader<BirdHolder> thumbnailDownloader;

    private List<Bird> birdList;

    public SeenBirdsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lifeList = LifeList.get(getActivity());

        List<Bird> demoBirds = SiteScrape.getBirdList();

        for(int i=0; i < 8; i++) {
            lifeList.addBird(demoBirds.get(i));
        }

        lifeList.addBird(new Bird("Mallard"));

        System.out.println("HELP " + demoBirds.size());
        System.out.println("HELP " + lifeList.getBirds().size());

        birdList = lifeList.getBirds();

        Collections.sort(birdList, new Comparator<Bird>() {
            @Override
            public int compare(final Bird object1, final Bird object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });

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
        View view = inflater.inflate(R.layout.fragment_seen_birds, container, false);

        recyclerView = view.findViewById(R.id.seen_birds_frag_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        LifeList lifeList = LifeList.get(getActivity());
        List<Bird> birds = lifeList.getBirds();

        if(birdAdapter == null) {
            birdAdapter = new BirdAdapter(birds);
            recyclerView.setAdapter(birdAdapter);
        } else {
            birdAdapter.setBirds(birds);
            birdAdapter.notifyDataSetChanged();
        }
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
            birdName.setText(bird.getName());
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

        public void setBirds(List<Bird> birds) {
            birdList = birds;
        }
    }
}
