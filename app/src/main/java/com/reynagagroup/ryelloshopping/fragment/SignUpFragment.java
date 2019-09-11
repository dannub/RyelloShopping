package com.reynagagroup.ryelloshopping.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reynagagroup.ryelloshopping.MainActivity;
import com.reynagagroup.ryelloshopping.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.widget.FButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    FButton signUpBtn;
    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private TextInputEditText email,fullname,pwd,confirmpwd,hp;

    private ImageButton closeBtn;



    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View itemView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signUpBtn=(FButton) itemView.findViewById(R.id.btn_signup);
        signUpBtn.setButtonColor(getResources().getColor(R.color.colorAccent));

       alreadyHaveAnAccount = (TextView) itemView.findViewById(R.id.textView3);

        parentFrameLayout = getActivity().findViewById(R.id.frame_layout_register);
       email = itemView.findViewById(R.id.txtemail);
       fullname = itemView.findViewById(R.id.txtfullname);
       pwd = itemView.findViewById(R.id.txtpassword);
       confirmpwd = itemView.findViewById(R.id.txtconfirmpassword);
       hp= itemView.findViewById(R.id.txthp);
        closeBtn = itemView.findViewById(R.id.close_signup);


       progressBar = itemView.findViewById(R.id.progress_signup);
       firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
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
        fullname.addTextChangedListener(new TextWatcher() {
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
        hp.addTextChangedListener(new TextWatcher() {
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
        pwd.addTextChangedListener(new TextWatcher() {
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
        confirmpwd.addTextChangedListener(new TextWatcher() {
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

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: send Data to firebase
                checkEmailAndPassword();
            }
        });
    }

    private void checkEmailAndPassword() {
        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.error);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());


        if(email.getText().toString().matches(emailPattern)){
            if(pwd.getText().toString().equals(confirmpwd.getText().toString())){

                progressBar.setVisibility(View.VISIBLE);
                signUpBtn.setEnabled(false);


                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pwd.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    Map<String,Object> userdata = new HashMap<>();

                                    userdata.put("fullname",fullname.getText().toString());
                                    userdata.put("email",email.getText().toString());
                                    userdata.put("hp",hp.getText().toString());


                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                        ///MAPS
                                                        Map<String ,Object> wishlistMap = new HashMap<>();
                                                        wishlistMap.put("list_size",(long)0);

                                                        Map<String ,Object> ratingMap = new HashMap<>();
                                                        wishlistMap.put("list_size",(long)0);

                                                        ///MAPS

                                                        final List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");


                                                        List<Map<String,Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(wishlistMap);
                                                        documentFields.add(ratingMap);

                                                        for (int x = 0;x<documentNames.size();x++){

                                                            final int finalX = x;
                                                            userDataReference.document(documentNames.get(x))
                                                                    .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        if (finalX == documentNames.size()-1) {
                                                                            mainIntent();
                                                                        }
                                                                    }else {
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        signUpBtn.setEnabled(true);
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }


                                                    }else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                }else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signUpBtn.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }else{
                confirmpwd.setError("Password doesn't matched!");
            }
        }else {
            email.setError("Invalid Email!");
        }
    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullname.getText())){
                if(!TextUtils.isEmpty(hp.getText())) {
                    if(!TextUtils.isEmpty(pwd.getText())&&pwd.length()>= 8){
                        if (!TextUtils.isEmpty(confirmpwd.getText())){
                            signUpBtn.setEnabled(true);

                        }else{
                            signUpBtn.setEnabled(false);
                               }
                    }else{
                        signUpBtn.setEnabled(false);
                       }
                }else{
                    signUpBtn.setEnabled(false);
                   }
            }
            else{
                signUpBtn.setEnabled(false);
                 }
        }else {
            signUpBtn.setEnabled(false);
           }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private  void mainIntent(){
        getActivity().finishAffinity();
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        disableCloseBtn = false;

    }

}
