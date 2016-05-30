package com.mosai.corporatetraining.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.survey.SurveyQuestion;
import com.mosai.corporatetraining.util.ViewUtil;

public class SurveyQuestionFragment extends Fragment {
    private Button btn_answer1,btn_answer2,btn_answer3,btn_answer4,btn_answer5,btn_submit;
    private TextView tvQuestion;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private SurveyQuestion mParam2;

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
            mParam2 = (SurveyQuestion) getArguments().getSerializable(ARG_PARAM2);
        }
    }
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_survey_question, container, false);
        btn_answer1 = ViewUtil.findViewById(view,R.id.btn_anwser1);
        btn_answer2 = ViewUtil.findViewById(view,R.id.btn_anwser2);
        btn_answer3 = ViewUtil.findViewById(view,R.id.btn_anwser3);
        btn_answer4 = ViewUtil.findViewById(view,R.id.btn_anwser4);
        btn_answer5 = ViewUtil.findViewById(view,R.id.btn_anwser5);
        btn_submit = ViewUtil.findViewById(view,R.id.btn_submit);
        tvQuestion = ViewUtil.findViewById(view,R.id.tv_question);
        btn_submit.setSelected(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        addListener();
        super.onViewCreated(view, savedInstanceState);
    }

    private void addListener() {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int answer) {
        if (mListener != null) {
            mListener.onFragmentInteraction(answer);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSurveyQuetsionFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int answer);
    }
}
