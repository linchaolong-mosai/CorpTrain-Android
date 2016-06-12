package com.mosai.corporatetraining.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.adpter.SurveyQuestionAdapter;
import com.mosai.corporatetraining.bean.survey.SurveyQuestion;
import com.mosai.corporatetraining.util.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SurveyQuestionFragment extends Fragment {
    private boolean firstLoad = true;
    private Button btn_submit;
    private TextView tvQuestion;
    private ListView lv;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    public SurveyQuestion question;

    private OnSurveyQuetsionFragmentInteractionListener mListener;

    public SurveyQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SurveyQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SurveyQuestionFragment newInstance(int param1, SurveyQuestion param2) {
        SurveyQuestionFragment fragment = new SurveyQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            question = (SurveyQuestion) getArguments().getSerializable(ARG_PARAM2);
        }
    }
    private View view;
    public SurveyQuestionAdapter adapter;
    private List<String> answers = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(firstLoad){
        view = inflater.inflate(R.layout.fragment_quiz_question, container, false);
        tvQuestion = ViewUtil.findViewById(view,R.id.tv_question);
        lv = ViewUtil.findViewById(view,R.id.lv);
//        btn_submit = ViewUtil.findViewById(view,R.id.btn_submit);

        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(firstLoad){
//        btn_submit.setSelected(false);
        answers = Arrays.asList(context.getResources().getStringArray(R.array.survey_level));
        adapter = new SurveyQuestionAdapter(context,answers,R.layout.item_listformat_quiz_question);
        lv.setAdapter(adapter);
        tvQuestion.setText(question.text);
        addListener();
            firstLoad=false;
        }
    }

    private void addListener() {
//        adapter.setClickCallback(new SurveyQuestionAdapter.ClickCallback() {
//            @Override
//            public void callback(int index) {
//                btn_submit.setSelected(true);
//            }
//        });
//            btn_submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(btn_submit.isSelected()){
////                        btn_submit.setSelected(false);
//                        onButtonPressed(adapter.index);
//                    }
//                }
//            });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int answer) {
        if (mListener != null) {
            mListener.onFragmentInteraction(mParam1,answer);
        }
    }
    private Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnSurveyQuetsionFragmentInteractionListener) {
            mListener = (OnSurveyQuetsionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSurveyQuetsionFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for ic_more information.
     */
    public interface OnSurveyQuetsionFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int index,int answer);
    }
}
