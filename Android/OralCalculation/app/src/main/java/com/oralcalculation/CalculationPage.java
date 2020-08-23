package com.oralcalculation;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oralcalculation.databinding.FragmentCalculationPageBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculationPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculationPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalculationPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculationPage.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculationPage newInstance(String param1, String param2) {
        CalculationPage fragment = new CalculationPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyViewModel myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
        myViewModel.setActivity(getActivity());

        FragmentCalculationPageBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_calculation_page,container,false);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(getActivity());
        //return inflater.inflate(R.layout.fragment_calculation_page, container, false);
        myViewModel.initAll();
        return binding.getRoot();
    }
}