package com.birdboys.birdbuddy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleBirdFragment extends Fragment {

    private static final String ARG_BIRD_NAME = "bird_name";

    private Bird bird;
    private ImageView birdImg;
    private TextView birdName;
    private TextView birdLink;
    private TextView birdDesc;
    private CheckBox birdSeen;

    public SingleBirdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String bird_name = (String) getArguments().getSerializable(ARG_BIRD_NAME);
        bird = new Bird(bird_name);
    }

    @Override
    public void onPause() {
        super.onPause();
        LifeList.get(getActivity()).updateBird(bird);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_bird, container, false);

        birdImg = (ImageView) view.findViewById(R.id.single_bird_frag_image);
        String birdURL = "https://www.allaboutbirds.org/guide/" + bird.getUri_name() + "/id";
        birdImg.setImageDrawable(SiteScrape.getBitmapDrawable(getActivity(), birdURL));

        birdName = (TextView) view.findViewById(R.id.single_bird_frag_name);
        birdName.setText(bird.getName());

        birdDesc = (TextView) view.findViewById(R.id.single_bird_frag_desc);
        birdDesc.setText(SiteScrape.getBirdDescription(birdURL));

        birdLink = (TextView) view.findViewById(R.id.single_bird_frag_link);
        birdLink.setClickable(true);
        birdLink.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='" + birdURL + "'> Learn More at All About Birds </a>";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            birdLink.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            birdLink.setText(Html.fromHtml(text));
        }

        birdSeen = (CheckBox) view.findViewById(R.id.single_bird_frag_seen);
        LifeList lifeList = LifeList.get(getActivity());
        lifeList.updateSeen(bird);
        birdSeen.setChecked(bird.isSeen());

        return view;
    }

    public static SingleBirdFragment newInstance(String birdName) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BIRD_NAME, birdName);

        SingleBirdFragment birdFragment = new SingleBirdFragment();
        birdFragment.setArguments(args);
        return birdFragment;
    }

}
