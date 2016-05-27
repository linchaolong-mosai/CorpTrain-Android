package com.mosai.corporatetraining.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.adpter.QuizQuestionAdapter;
import com.mosai.corporatetraining.bean.quiz.Answer;
import com.mosai.corporatetraining.bean.quiz.Question;
import com.mosai.corporatetraining.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFratListenerQuizQuestion} interface
 * to handle interaction events.
 * Use the {@link QuizQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizQuestionFragment extends Fragment {

    private View view;
    public QuizQuestionAdapter adapter;
    private List<Answer> answers = new ArrayList<>();
    public Question question;
    private Context context;
    private static final String TAG = "tag";
    private OnFratListenerQuizQuestion mListener;

    public QuizQuestionFragment() {
    }

    public static QuizQuestionFragment newInstance(Question question) {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(TAG);
            answers.addAll(question.getAnswers());
        }
    }
    private TextView tvQuestion;
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_quiz_question, container, false);
        tvQuestion = ViewUtil.findViewById(view,R.id.tv_question);
        listView = ViewUtil.findViewById(view,R.id.lv);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvQuestion.setText(question.getText());
        adapter = new QuizQuestionAdapter(context,answers,R.layout.item_listformat_quiz_question);
        listView.setAdapter(adapter);
        addListener();
    }

    private void addListener() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFratListenerQuizQuestion) {
            mListener = (OnFratListenerQuizQuestion) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFratListenerQuizQuestion");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFratListenerQuizQuestion {
        // TODO: Update argument type and name
        void onFragmentInteraction(Question question);
    }
}
