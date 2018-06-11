package com.firebaseapp.firebaseapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    // Edit text
    private EditText editTextEmail;
    private EditText editTextPassword;

    // Buttons
    private Button btnLogin;
    private Button btnLogOut;
    private Button btnCriar;
    private Button btnResetPassword;
    private Button btnUpdateEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.edEmail);
        editTextPassword = (EditText) findViewById(R.id.edPassword);

        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCriar = (Button) findViewById(R.id.btnCriar);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
        btnUpdateEmail = (Button) findViewById(R.id.btnUpdateEmail);

        btnUpdateEmail.setVisibility(View.GONE);
        btnLogOut.setVisibility(View.GONE);

        // Botao Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String senha = editTextPassword.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Informe um email valido!", Toast.LENGTH_SHORT).show();
                } else if (senha.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Informe a senha!", Toast.LENGTH_SHORT).show();
                } else {
                    efetuarLogin(email, senha);
                    Toast.makeText(getApplicationContext(), "Bem vindo " + email, Toast.LENGTH_SHORT).show();

                    btnLogin.setVisibility(View.GONE);
                    btnCriar.setVisibility(View.GONE);
                    btnLogOut.setVisibility(View.VISIBLE);
                    btnUpdateEmail.setVisibility(View.VISIBLE);
                    btnResetPassword.setVisibility(View.GONE);
                    editTextPassword.setEnabled(false);
                }

            }
        });

        // Botao Logout
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();

                btnLogin.setVisibility(View.VISIBLE);
                btnCriar.setVisibility(View.VISIBLE);
                btnLogOut.setVisibility(View.GONE);
                btnResetPassword.setVisibility(View.VISIBLE);
                btnUpdateEmail.setVisibility(View.GONE);
                editTextPassword.setEnabled(true);
                editTextEmail.setEnabled(true);
                editTextEmail.setText("");
                editTextPassword.setText("");

            }
        });

        // Botao criar conta
        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String senha = editTextPassword.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Informe um email valido!", Toast.LENGTH_SHORT).show();
                } else if (senha.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Informe a senha!", Toast.LENGTH_SHORT).show();
                } else {
                    createUser(email, senha);
                    Toast.makeText(getApplicationContext(), "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botao Recuperar Senha
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString();
                if (!email.isEmpty()){
                    resetarSenha(email);
                    Toast.makeText(getApplicationContext(), "Senha alterada com sucesso, verifique seu email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Informe um email valido!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botao Alterar Email
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Informe um email valido!", Toast.LENGTH_SHORT).show();
                } else {
                    alterarEmail(email);
                    Toast.makeText(getApplicationContext(), "Email Alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseUser != null){
                    Log.w("FirebaseAuth", "Login: " + firebaseUser.getUid());
                } else {
                    Log.w("FirebaseAuth", "logOut");
                }
            }
        };
    }


    /**
     * Cria novo usario email e senha
     * @param email
     * @param password
     */
    protected void createUser (String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).
            addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.w("CREATE_USER", "EmailAndPassword: " + task.isSuccessful());
                }
            }).
            addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });

    }

    /**
     * Efetua Login do Usuario informado
     * @param email
     * @param senha
     */
    private void efetuarLogin(String email, String senha){
        firebaseAuth.signInWithEmailAndPassword(email, senha).
            addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.w("LOGIN_USER", "EmailAndPassword: " + task.isSuccessful());
                }
            }).
            addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
    }


    /**
     *  Reseta senha do email
     * @param password
     */
    private void resetarSenha(String password){
        firebaseAuth.sendPasswordResetEmail(password).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.w("PASSWORD", "ResetPassword: " + task.isSuccessful());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *  Alterar Email
     * @param email
     */
    private void alterarEmail(String email){
        firebaseUser.updateEmail(email).
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.w("EMAIL", "ChangeEmail: " + task.isSuccessful());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}