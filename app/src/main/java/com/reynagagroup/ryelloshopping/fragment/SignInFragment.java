package com.reynagagroup.ryelloshopping.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.Activity.MainActivity;
import com.reynagagroup.ryelloshopping.R;

import info.hoang8f.widget.FButton;

import static com.reynagagroup.ryelloshopping.Activity.RegisterActivity.onResetPasswordFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    FButton signInBtn;
    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private TextInputEditText email,password;

    private ImageButton closeBtn;

    private TextView forgotPassword;

    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public  static boolean disableCloseBtn =false;

    public SignInFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        signInBtn=(FButton) itemView.findViewById(R.id.btn_signin);
        signInBtn.setButtonColor(getResources().getColor(R.color.colorAccent));
        dontHaveAnAccount = (TextView) itemView.findViewById(R.id.textView3);
        parentFrameLayout = getActivity().findViewById(R.id.frame_layout_register);

        email = itemView.findViewById(R.id.txtemail);
        password = itemView.findViewById(R.id.txtpassword);


        closeBtn = itemView.findViewById(R.id.close_signin);
        signInBtn = itemView.findViewById(R.id.btn_signin);

        forgotPassword = itemView.findViewById(R.id.forgot_pwd);

        progressBar = itemView.findViewById(R.id.progress_signin);

        firebaseAuth = FirebaseAuth.getInstance();

        if (disableCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }

        return itemView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });


    }



    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                signInBtn.setEnabled(true);

            }else {
                signInBtn.setEnabled(false);

            }
        }else{
            signInBtn.setEnabled(false);

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private  void checkEmailAndPassword(){
        if(email.getText().toString().matches(emailPattern)){
            if(password.length()>=8){

                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setEnabled(false);

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    mainIntent();

                                }else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signInBtn.setEnabled(true);

                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                Toast.makeText(getActivity(),"Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(),"Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void mainIntent() {
        getActivity().finishAffinity();
        DBqueries.clearData();
        MainActivity.currentFragment = -1;
        MainActivity.mainActivity = null;
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        MainActivity.showCart=false;
        startActivity(mainIntent);
        disableCloseBtn = false;

    }
}
